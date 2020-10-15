package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DetailSetting_emailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_email);
    }

    public void lastpage(View view) {
        finish();
    }

    public void checkmail(View view) {
        //在此判斷email是否相同 相同的話存入資料表中
    }
}