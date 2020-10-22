package com.example.jogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailSetting_emailActivity extends AppCompatActivity {
    private EditText enter_email,re_enter_email;
    private TextView email;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_email);

        enter_email = (EditText)findViewById(R.id.enter_email);
        re_enter_email = (EditText)findViewById(R.id.re_enter_email);
        email = (TextView)findViewById(R.id.email);

        sp = getSharedPreferences("email",MODE_PRIVATE);
        editor = sp.edit();

//        String mail = sp.getString("mail","xxx@gmail.com");
//        Log.v("brad",mail);
        email.setText(sp.getString("email","xxx@gmail.com"));


    }

    public void lastpage(View view) {
        finish();
    }

    public void checkmail(View view) {
        String email1 = enter_email.getText().toString().trim();
        String email2 = re_enter_email.getText().toString().trim();
        //在此判斷email是否相同 相同的話存入資料表中

        if (!isEmailValid(email1)){
            Toast.makeText(DetailSetting_emailActivity.this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            if (email1.equals(email2)) {
                Toast.makeText(DetailSetting_emailActivity.this, "編輯成功", Toast.LENGTH_SHORT).show();
                editor.putString("email", enter_email.getText().toString());
                editor.commit();
                finish();
            } else {
                Log.v("brad", enter_email.getText().toString());
                Log.v("brad", re_enter_email.getText().toString());
                Toast.makeText(DetailSetting_emailActivity.this, "輸入e-mail必須相同，請重新輸入", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}