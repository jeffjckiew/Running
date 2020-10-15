package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;
import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;

public class DetailSetting_aboutyouActivity extends AppCompatActivity {
    private TextView showGender,showTall,showWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_aboutyou);
        showGender = findViewById(R.id.showgender);
        showTall = findViewById(R.id.showtall);
        showWeight = findViewById(R.id.showweight);

    }

    public void lastpage(View view) {
        finish();
    }

    public void editGender(View view) {
        GenderPickerDialog dialog=new GenderPickerDialog(DetailSetting_aboutyouActivity.this);
        dialog.setOnSelectingGender(new GenderPickerDialog.OnGenderSelectListener() {
            @Override
            public void onSelectingGender(String value) {
                Toast.makeText(DetailSetting_aboutyouActivity.this, "Selected "+value, Toast.LENGTH_SHORT).show();
                showGender.setText(value);
                //此處的value應該存於偏好設定中
            }
        });
        dialog.show();

    }

    public void edittall(View view) {
        NumberPickerDialog dialog=new NumberPickerDialog(DetailSetting_aboutyouActivity.this, 130, 230, new NumberPickerDialog.NumberPickerCallBack() {
            @Override
            public void onSelectingValue(int value) {
                Toast.makeText(DetailSetting_aboutyouActivity.this, "Selected "+String.valueOf(value), Toast.LENGTH_SHORT).show();
                showTall.setText(String.valueOf(value));
                //此處的value應該存於偏好設定中
            }
        });
        dialog.show();
    }

    public void editweight(View view) {
        NumberPickerDialog dialog=new NumberPickerDialog(DetailSetting_aboutyouActivity.this, 25, 200, new NumberPickerDialog.NumberPickerCallBack() {
            @Override
            public void onSelectingValue(int value) {
                Toast.makeText(DetailSetting_aboutyouActivity.this, "Selected "+String.valueOf(value), Toast.LENGTH_SHORT).show();
                showWeight.setText(String.valueOf(value));
                //此處的value應該存於偏好設定中
            }
        });
        dialog.show();
    }
}