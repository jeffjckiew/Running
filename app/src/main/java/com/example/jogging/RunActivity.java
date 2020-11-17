package com.example.jogging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import at.markushi.ui.CircleButton;

public class RunActivity extends AppCompatActivity implements LocationListener{
    private SwitchCompat sw_metric;
    private CircleButton btnFinish;
    private CircleButton btnPause;
    private CircleButton btnStartC;
    private int ss;
    private Timer timer;
    private RunActivity.Task1 timetask;
    private TextView showtime,showdistance,showspeed;
    private RunActivity.UIHandler uiHandler;
    private boolean isRun = false;
    float transRunSpeed;
    float transDistance;
    AlertDialog.Builder objdbr;
    private TextView runUnit;
    int min;
    Double pace;
    String JSON_URL ="http://192.168.3.25:8080/jogging-hibernate-spring-tx/run/insert";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        bindButton();
        bindView();
        uiHandler=new RunActivity.UIHandler();

        //以下為GPS
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }else{
            doStuff();
        }

        this.updateSpeed(null);

        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RunActivity.this.updateSpeed(null);
                if (isChecked){
                    runUnit.setText("公里");
                }else {
                    runUnit.setText("英里");
                }
            }
        });

    }

    private void bindView() {
        showtime=findViewById(R.id.runing_time);
        showdistance=findViewById(R.id.runing_distance);
        showspeed=findViewById(R.id.runing_speed);
        runUnit = findViewById(R.id.run_unit);
    }

    private void bindButton() {
        btnFinish=findViewById(R.id.finishRunning);
        btnPause=findViewById(R.id.pauseRunning);
        btnStartC=findViewById(R.id.startRunning);
        sw_metric = findViewById(R.id.sw_metric);

        //暫停按鈕事件
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timetask!=null){
                    timetask.cancel();
                    timetask=null;
                    btnStartC.setEnabled(true);
                    isRun=false;
                }
            }
        });
        //繼續按鈕事件
        btnStartC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetask= new Task1();
                timer.schedule(timetask,1*1000,1*1000);
                btnStartC.setEnabled(false);
                isRun=true;
            }
        });
        //結束按鈕事件
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStartC.setEnabled(true);
                isRun=false;
                showAllDetailDialog(v);
                if(timetask!=null){
                    timetask.cancel();
                    timetask=null;
                }
                showtime.setText("00:00:00");
                showdistance.setText("0.00");
                showspeed.setText("00.00''");
            }
        });

    }
    //跑步結算
    public void showAllDetailDialog(View view) {
        objdbr = new AlertDialog.Builder(this);

        //取得自訂的版面。
        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.runinfo_dialog,null);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        Double distancef = Double.parseDouble((String) showdistance.getText());
        if(distancef<1){
            pace = 1/distancef/min;
        }else{
            pace = distancef/min;
        }

        objdbr.setTitle("本次跑步資訊如下");
        final TextView rundate = (TextView)(v.findViewById(R.id.dialog_rundate));
        rundate.setText("紀錄日期"+year+"年"+month+"月"+day+"日");
        final TextView rundistance =(TextView)(v.findViewById(R.id.dialog_rundistance));
        rundistance.setText("距離"+showdistance.getText()+runUnit.getText());
        final TextView runspeed =(TextView)(v.findViewById(R.id.dialog_runspeed));
        runspeed.setText("平均配速:"+pace);
        final TextView runtime =(TextView)(v.findViewById(R.id.dialog_runtime));
        runtime.setText("總花費時間:"+showtime.getText());
        //設定AlertDialog的View。
        objdbr.setView(v);
        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在這邊將跑步資訊存入資料庫
                insertRunData((String) rundate.getText(), (String) rundistance.getText(),
                        (String)runspeed.getText(),(String)runtime.getText());
                finish();
            }
        }).show();
    }

    private void insertRunData(String date,String rundistance,String runspeed,String runtime) {
        RequestQueue queue = Volley.newRequestQueue(RunActivity.this.getApplicationContext());

        JSONObject object = new JSONObject ();
        Log.v("hank",JSON_URL);
        try {
            object.put("date",date);
            object.put("distance",rundistance);
            object.put("runtime",runtime);
            object.put("pace",runspeed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("hank",object.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }


    //GPS相關
    @SuppressLint("MissingPermission")
    private void doStuff() {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        Toast.makeText(this,"開始跑步",Toast.LENGTH_SHORT).show();
    }
    private void updateSpeed(CLocation location){
        if(isRun){
        float nCurrentSpeed = 0 ;
        float npance = 0 ;
        if(location!=null){
            location.setbUseMetricUnits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();

        }
        Formatter fmtSpeed = new Formatter(new StringBuilder());
        Formatter fmtDistance = new Formatter(new StringBuilder());
        Formatter kmDistance = new Formatter(new StringBuilder());
        if(nCurrentSpeed>1){
            transRunSpeed = 60/nCurrentSpeed;
            transDistance += nCurrentSpeed/3600;
        }else{
            transRunSpeed=0;
        }

        fmtSpeed.format(Locale.US,"%5.2f",transRunSpeed);
        fmtDistance.format(Locale.US,"%5.2f",transDistance);
        kmDistance.format(Locale.US,"%5.2f",transDistance*1.61);
        String strCurrentSpeed = fmtSpeed.toString();
        String strDistance = fmtDistance.toString();
        String strKMDistance = kmDistance.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ","0");
        showspeed.setText(strCurrentSpeed+"''");

        if(this.useMetricUnits()){
            //km
            showdistance.setText(strKMDistance);
        }else{
            //mile
            showdistance.setText(strDistance);
        }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                doStuff();
            }else{
                finish();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null){
            CLocation mylocation = new CLocation(location,this.useMetricUnits());
            this.updateSpeed(mylocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private boolean useMetricUnits(){
        return sw_metric.isChecked();
    }



    //以下為時間
    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                int sec = ss%60;
                min = ss/60;
                int hou = ss/3600;
                String second = sec>9?""+sec:"0"+sec;
                String minute = min>9?""+min:"0"+min;
                String hour = hou>9?""+hou:"0"+hou;
                showtime.setText(hour+":"+minute+":"+second);
            }
        }
    }


    public class Task1 extends TimerTask {
        @Override
        public void run() {
            ss++;
            uiHandler.sendEmptyMessage(0);
        }
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




}