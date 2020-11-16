package com.example.jogging;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends AppCompatActivity {
    AlertDialog.Builder objdbr;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView showname,showarea,showmotto;
    private CircleImageView myphoto;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        showname = findViewById(R.id.name);
        showarea = findViewById(R.id.area);
        showmotto = findViewById(R.id.motto);
        myphoto = (CircleImageView)findViewById(R.id.myphoto);

        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Sellect Picture"), PICK_IMAGE);
            }
        });
        if(readImage()!=null){
            myphoto.setImageBitmap(StringToBitMap(readImage()));
        }

        sp = getSharedPreferences("jogging",MODE_PRIVATE);
        editor = sp.edit();

//        String username = sp.getString("userName","姓名");
        showname.setText(sp.getString("userName","姓名"));

//        String userarea = sp.getString("userArea","地區");
        showarea.setText(sp.getString("userArea","地區"));

//        String usermotto = sp.getString("userMotto","座右銘");
        showmotto.setText(sp.getString("userMotto","座右銘"));
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
        EditText name = (EditText)(v.findViewById(R.id.dialog_name));
        name.setText(sp.getString("userName","姓名"));
        EditText area =(EditText)(v.findViewById(R.id.dialog_area));
        area.setText(sp.getString("userArea","地區"));
        EditText motto =(EditText)(v.findViewById(R.id.dialog_motto));
        motto.setText(sp.getString("userMotto","座右銘"));

        //設定AlertDialog的View。
        objdbr.setView(v);
        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //回傳輸入的值，再用Toast顯示。
                EditText name = (EditText)(v.findViewById(R.id.dialog_name));
                EditText area =(EditText)(v.findViewById(R.id.dialog_area));
                EditText motto =(EditText)(v.findViewById(R.id.dialog_motto));
                prc_showmessage(name.getText().toString(),area.getText().toString(),motto.getText().toString());
            }
        }).show();
    }


    //將以下方法改成存入內存
    public void prc_showmessage(String name,String area,String motto)
    {
        Toast objtoast = Toast.makeText(this,"姓名:"+name+"地區:"+area+"座右銘:"+motto, Toast.LENGTH_SHORT);
        objtoast.show();
        showname.setText(name);
        showarea.setText(area);
        showmotto.setText(motto);
        editor.putString("userName",name);
        editor.putString("userArea",area);
        editor.putString("userMotto",motto);
        editor.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                myphoto.setImageBitmap(bitmap);
                saveImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String temp= Base64.encodeToString(b, Base64.DEFAULT);
            FileOutputStream file = openFileOutput("imageStore.txt",MODE_PRIVATE);
            file.write(temp.getBytes());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readImage(){
        try {
            FileInputStream fin  =  openFileInput("imageStore.txt");
            BufferedReader br  = new BufferedReader(new InputStreamReader(fin));
            StringBuffer sb = new StringBuffer();
            String line;
            while((line=br.readLine())!= null){
                sb.append(line);
            }
            fin.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}