package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;

public class DetailSettingActivity extends AppCompatActivity {
    private TextView email;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting);

    }
    @Override
    protected void onResume() {
        super.onResume();
        email = (TextView)findViewById(R.id.email);
        sp = getSharedPreferences("email",MODE_PRIVATE);
        email.setText(sp.getString("email","xxx@gmail.com"));
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

}