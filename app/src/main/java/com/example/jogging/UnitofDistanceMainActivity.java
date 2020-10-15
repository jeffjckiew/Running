package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class UnitofDistanceMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unitof_distance_main);
    }

    public void lastpage(View view) {
        finish();
    }

    public void setKM(View view) {
        Toast.makeText(UnitofDistanceMainActivity.this,"KM", Toast.LENGTH_SHORT).show();
    }

    public void setML(View view) {
        Toast.makeText(UnitofDistanceMainActivity.this,"ML", Toast.LENGTH_SHORT).show();
    }
}