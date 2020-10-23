package com.example.jogging;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private HomeFragment home;
    private RunFragment run;
    private RecordFragment record;
    private TextView showpagename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        run = new RunFragment();
        record = new RecordFragment();
        showpagename = findViewById(R.id.showpagename);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container,home);
        transaction.commit();
        showpagename.setText("飲食紀錄");

    }

    public void personalSetting(View view) {
        Intent intent = new Intent(this,PersonalActivity.class);
        startActivity(intent);
//        startActivityForResult(intent,475);
    }

    public void homeBtn(View view) {
        showpagename.setText("飲食紀錄");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,home);
        transaction.commit();
    }

    public void runBtn(View view) {
        showpagename.setText("跑步");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,run);
        transaction.commit();
//        Intent intent = new Intent(this,MapsActivity.class);
//        startActivity(intent);
    }

    public void recordBtn(View view) {
        showpagename.setText("紀錄");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,record);
        transaction.commit();
    }

//    public void addition(View view) {
//        // 這裡按了之後，新增每日紀錄
//    }
}