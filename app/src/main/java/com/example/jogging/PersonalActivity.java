package com.example.jogging;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalActivity extends AppCompatActivity {
    AlertDialog.Builder objdbr;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }

    //進入設定畫面
    public void detailSetting(View view) {
        Intent intent = new Intent(this,DetailSettingActivity.class);
        startActivity(intent);
    }

    //導入自定義的Dialog
    public void showEditDialog(View view) {
        objdbr = new AlertDialog.Builder(this);

        //取得自訂的版面。
        LayoutInflater inflater = LayoutInflater.from(PersonalActivity.this);
        final View v = inflater.inflate(R.layout.personal_dialog,null);

        objdbr.setTitle("請輸入您的資料：");

        //設定AlertDialog的View。
        objdbr.setView(v);

        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //回傳輸入的值，再用Toast顯示。
                EditText name = (EditText)(v.findViewById(R.id.name));
                EditText area =(EditText)(v.findViewById(R.id.area));
                EditText motto =(EditText)(v.findViewById(R.id.motto));
                prc_showmessage(name.getText().toString(),area.getText().toString(),motto.getText().toString());
            }
        }).show();
    }


    //將以下方法改成存入外存
    public void prc_showmessage(String name,String area,String motto)
    {
        Toast objtoast = Toast.makeText(this,"姓名:"+name+"地區:"+area+"座右銘:"+motto, Toast.LENGTH_SHORT);
        objtoast.show();
    }
}