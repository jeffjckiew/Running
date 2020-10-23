package com.example.jogging;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class RunFragment extends Fragment {
    private Button btnPause;
    private Button btnContinue;
    private MainActivity mainActivity;
    private Timer timer;
    private Task1 timetask;
    private TextView showtime;
    private UIHandler uiHandler;
    private int ss;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        mainActivity = (MainActivity) getActivity();
        btnPause = (Button) view.findViewById(R.id.btn_pause);
        showtime = view.findViewById(R.id.runtime);
        uiHandler=new UIHandler();

        btnContinue = view.findViewById(R.id.btn_continue);
        //暫停按鈕事件
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timetask!=null){
                    timetask.cancel();
                    timetask=null;
                    btnContinue.setEnabled(true);
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
            }
        });

        return view;
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

}