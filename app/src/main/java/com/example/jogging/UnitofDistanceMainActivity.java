package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class UnitofDistanceMainActivity extends AppCompatActivity {
    private RadioButton radiokm,radiomi;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unitof_distance_main);

        radiokm = findViewById(R.id.radiokm);
        radiomi = findViewById(R.id.radiomi);
//        sp = getSharedPreferences("transfer",MODE_PRIVATE);
        sp = getSharedPreferences("transfer",MODE_PRIVATE);
        editor = sp.edit();



        radiokm.setChecked(sp.getBoolean("checked",false));
        radiomi.setChecked(sp.getBoolean("checked",false));
    }

    public void lastpage(View view) {
        finish();
    }

    public void setKM(View view) {
        Toast.makeText(UnitofDistanceMainActivity.this,"KM", Toast.LENGTH_SHORT).show();
        editor.putString("transfer", radiokm.getHint().toString());
        editor.putBoolean("checked",radiomi.isChecked());
        editor.commit();
    }

    public void setMI(View view) {
        Toast.makeText(UnitofDistanceMainActivity.this,"MI", Toast.LENGTH_SHORT).show();
        editor.putString("transfer", radiomi.getHint().toString());
        editor.putBoolean("checked",radiomi.isChecked());
        editor.commit();
    }


}