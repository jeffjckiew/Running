package com.example.jogging;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RunFragment extends Fragment {
    private Button btnFinish;
    AlertDialog.Builder objdbr;
    private Button btnPause;
    private Button btnContinue;
    private MainActivity mainActivity;
    private Timer timer;
    private Task1 timetask;
    private TextView showtime;
    private UIHandler uiHandler;
    private int ss;
    SensorManager sm;
    Sensor sensor;
    float LastX,Lasty,LastZ;
    double S;
    long LastTime;
    TextView distance,speed;
    float i = 0;
    boolean startRun = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_run, container, false);
        mainActivity = (MainActivity) getActivity();
        btnPause = (Button) view.findViewById(R.id.btn_pause);
        btnContinue = (Button)view.findViewById(R.id.btn_continue);
        btnFinish = (Button)view.findViewById(R.id.btn_finishrun);
        sm =(SensorManager)mainActivity.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(SL,sensor,SensorManager.SENSOR_DELAY_GAME);
        speed = view.findViewById(R.id.runspeed);
        showtime = view.findViewById(R.id.runtime);
        distance=view.findViewById(R.id.rundistance);
        uiHandler=new UIHandler();


        //暫停按鈕事件
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timetask!=null){
                    timetask.cancel();
                    timetask=null;
                    btnContinue.setEnabled(true);
                    startRun=false;
                    btnContinue.setText("繼續");
                }
            }
        });
        //繼續按鈕事件
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetask= new Task1();
                timer.schedule(timetask,1*1000,1*1000);
                btnContinue.setEnabled(false);
                btnContinue.setText("");
                startRun=true;
            }
        });

        //結束跑步
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinue.setText("開始");
                btnContinue.setEnabled(true);
                showAllDetailDialog(view);
                if(timetask!=null){
                    timetask.cancel();
                    timetask=null;
                }
                //將所有計算歸零
                ss=0;
                showtime.setText("00:00:00");
                startRun=false;
                distance.setText("0.00公里");
                speed.setText("0");
            }
        });


        return view;
    }

    public void showAllDetailDialog(View view) {
        objdbr = new AlertDialog.Builder(mainActivity);

        //取得自訂的版面。
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        final View v = inflater.inflate(R.layout.runinfo_dialog,null);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        objdbr.setTitle("本次跑步資訊如下");
        TextView rundate = (TextView)(v.findViewById(R.id.dialog_rundate));
        rundate.setText("紀錄日期"+year+"年"+month+"月"+day+"日");
        TextView rundistance =(TextView)(v.findViewById(R.id.dialog_rundistance));
        rundistance.setText(distance.getText());
        TextView runspeed =(TextView)(v.findViewById(R.id.dialog_runspeed));
        runspeed.setText("平均配速:"+speed.getText());
        TextView runtime =(TextView)(v.findViewById(R.id.dialog_runtime));
        runtime.setText("總花費時間:"+showtime.getText());
        //設定AlertDialog的View。
        objdbr.setView(v);
        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在這邊將跑步資訊存入資料庫
//                TextView rundate = (TextView)(v.findViewById(R.id.dialog_rundate));
//                TextView rundistance =(TextView)(v.findViewById(R.id.dialog_rundistance));
//                TextView runspeed =(TextView)(v.findViewById(R.id.dialog_runspeed));
//                TextView runtime =(TextView)(v.findViewById(R.id.dialog_runtime));
//                prc_showmessage(name.getText().toString(),area.getText().toString(),motto.getText().toString());
            }
        }).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        timer=new Timer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
    }

    //以下為計時功能code
    private class Task1 extends TimerTask {
        @Override
        public void run() {
            ss++;
            uiHandler.sendEmptyMessage(0);
        }
    }
    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                int sec = ss%60;
                int min = ss/60;
                int hou = ss/3600;
                String second = sec>9?""+sec:"0"+sec;
                String minute = min>9?""+min:"0"+min;
                String hour = hou>9?""+hou:"0"+hou;
                showtime.setText(hour+":"+minute+":"+second);
            }
        }
    }

    //此處開始計算距離及跑步速度
    private SensorEventListener SL = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            long CurrentTime = System.currentTimeMillis();
            long xTime = CurrentTime-LastTime;
            if(xTime<80) return;
            float x =event.values[0];
            float y =event.values[1];
            float z =event.values[2];
            float DX = x-LastX;
            float DY = y-Lasty;
            float DZ = z-LastZ;
            S = Math.sqrt(Math.pow(DX,2)+Math.pow(DY,2)+Math.pow(DZ,2))/xTime*1000;

            if(S>=150 && S<=200 && startRun){
//                tv.setText("初位置:"+x+","+y+";"+z+","+"末位置:"+DX+","+DY+","+DZ);
                i++;
                distance.setText(String.valueOf((i*78)/100000)+"公里");
                speed.setText(""+S);
            }
            LastTime=CurrentTime;
            LastX=x;
            Lasty=y;
            LastZ=z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    protected void onDestory(){
        super.onDestroy();
        sm.unregisterListener(SL);
    }
}