package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;

public class DetailSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting);
    }

    public void lastpage(View view) {
        finish();
    }


    public void aboutYou(View view) {
        Intent intent = new Intent(this,DetailSetting_aboutyouActivity.class);
        startActivity(intent);
    }

    public void email(View view) {
        Intent intent = new Intent(this,DetailSetting_emailActivity.class);
        startActivity(intent);
    }

    public void unit(View view) {
        Intent intent = new Intent(this,UnitofDistanceMainActivity.class);
        startActivity(intent);
    }
}