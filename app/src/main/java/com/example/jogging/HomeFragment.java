package com.example.jogging;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private LinkedList<HashMap<String,String>> data;
    AlertDialog.Builder objdbr;
    private  MainActivity mainActivity;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.home_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myAdapter = new MyAdapter();
        doData();
        recyclerView.setAdapter(myAdapter);
        return view;
    }




    private void doData(){
        //在這邊抓取資料 並塞入data中
        data = new LinkedList<>();
        for(int i = 0; i<5;i++){
            HashMap<String,String> row = new HashMap<>();
            int ran = (int)(Math.random()*100);
            row.put("title","Title:"+ran+"testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
            data.add(row);
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        class MyViewHolder extends RecyclerView.ViewHolder {
            private Button item;
            private View itemView;
            private TextView title;
            public MyViewHolder(View v) {
                super(v);
                itemView = v;
                title = itemView.findViewById(R.id.item_article);
                item = (Button) itemView.findViewById(R.id.homeitem);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
//                        由此處新增點選各項item後的內容顯示
                        objdbr = new AlertDialog.Builder(mainActivity);
                        LayoutInflater inflater = LayoutInflater.from(mainActivity);
                         v = inflater.inflate(R.layout.homepage_dialog,null);
                        objdbr.setTitle("請輸入您的飲食資料：");

                        objdbr.setView(v);

                        final View finalV = v;
                        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //回傳輸入的值，再用Toast顯示。
                                EditText breakfast = (EditText)(finalV.findViewById(R.id.breakfast));
                                EditText lunch =(EditText)(finalV.findViewById(R.id.lunch));
                                EditText dinner =(EditText)(finalV.findViewById(R.id.dinner));
                                EditText extra =(EditText)(finalV.findViewById(R.id.extrafood));
                                prc_showmessage(breakfast.getText().toString(),lunch.getText().toString(),dinner.getText().toString(),extra.getText().toString());
                            }
                        }).show();
                    }
                });
            }
        }

        public void prc_showmessage(String breakfast,String lunch,String dinner,String extra)
        {
            Toast objtoast = Toast.makeText(mainActivity,"早餐:"+breakfast+"午餐:"+lunch+"晚餐:"+dinner+"額外飲食:"+extra, Toast.LENGTH_SHORT);
            objtoast.show();
            //在此處將飲食紀錄存入
        }


        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_item, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;

        }
        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {

            //在這邊抓取資料內容
            holder.title.setText(data.get(position).get("title"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("hank","Click"+position);
                }
            });

        }
        @Override
        public int getItemCount() {
            //在這邊抓取資料筆數
            if(data!=null){return data.size();}
            return 0;
        }
    }

}