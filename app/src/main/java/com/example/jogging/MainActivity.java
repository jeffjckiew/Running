package com.example.jogging;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private HomeFragment home;
    private RecordFragment record;
    private ImageButton  homeBtn,runBtn,recordBtn;
    private ImageButton personlSet;
    private StartRunFragment startRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        record = new RecordFragment();
        startRun = new StartRunFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container,home);
        transaction.commit();
        bindButton();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindButton() {
        personlSet=findViewById(R.id.personlSet_btn);
        homeBtn=findViewById(R.id.home_btn);
        runBtn=findViewById(R.id.run_btn);
        recordBtn=findViewById(R.id.record_btn);

        personlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PersonalActivity.class);
                startActivity(intent);
            }
        });
        personlSet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    personlSet.setAlpha((float) 0.2);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    personlSet.setAlpha((float) 1);
                }
                return false;
            }
        });


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container,home);
                transaction.commit();

                if (homeBtn.isPressed()) {
                    runBtn.setImageResource(R.drawable.run);
                    recordBtn.setImageResource(R.drawable.record);
                    homeBtn.setImageResource(R.drawable.home1);
                }
//                else {
//                    homeBtn.setImageResource(R.drawable.home);
//                }
            }
        });

        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,RunActivity.class);
//                startActivity(intent);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container,startRun);
                transaction.commit();

                if (runBtn.isPressed()) {
                    homeBtn.setImageResource(R.drawable.home);
                    recordBtn.setImageResource(R.drawable.record);
                    runBtn.setImageResource(R.drawable.run1);
                }
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container,record);
                transaction.commit();

                if (recordBtn.isPressed()) {
                    homeBtn.setImageResource(R.drawable.home);
                    recordBtn.setImageResource(R.drawable.record1);
                    runBtn.setImageResource(R.drawable.run);
                }
            }
        });

    }


}