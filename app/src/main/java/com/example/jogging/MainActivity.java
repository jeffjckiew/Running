package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private HomeFragment home;
    private RunFragment run;
    private RecordFragment record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        run = new RunFragment();
        record = new RecordFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container,home);
        transaction.commit();
    }

    public void personalSetting(View view) {
        Intent intent = new Intent(this,PersonalActivity.class);
        startActivity(intent);
//        startActivityForResult(intent,475);
    }

    public void homeBtn(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,home);
        transaction.commit();
    }

    public void runBtn(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,run);
        transaction.commit();
    }

    public void recordBtn(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,record);
        transaction.commit();
    }
}