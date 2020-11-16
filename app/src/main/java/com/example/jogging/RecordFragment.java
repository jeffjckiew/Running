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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.LinkedList;

public class RecordFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecordFragment.MyAdapter myAdapter;
    private LinkedList<HashMap<String,String>> data;
    AlertDialog.Builder objdbr;
    private  MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mainActivity = (MainActivity) getActivity();
        recyclerView = view.findViewById(R.id.record_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myAdapter = new RecordFragment.MyAdapter();
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
            row.put("recordtitle","Record:"+ran+"recordrecordrecordrecordrecord");
            row.put("date","Date:"+ran);
            data.add(row);
        }
    }



    private class MyAdapter extends RecyclerView.Adapter<RecordFragment.MyAdapter.MyViewHolder>{
        class MyViewHolder extends RecyclerView.ViewHolder {
            private FloatingActionButton item;
            private View itemView;
            private TextView title;
            public MyViewHolder(View v) {
                super(v);
                itemView = v;
                title = itemView.findViewById(R.id.item_record);
                item = (FloatingActionButton) itemView.findViewById(R.id.record);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
//                        由此處新增點選各項item後的內容顯示
                        //TODO
//                        由此處新增點選各項item後的內容顯示
                        objdbr = new AlertDialog.Builder(mainActivity);
                        LayoutInflater inflater = LayoutInflater.from(mainActivity);
                        v = inflater.inflate(R.layout.record_dialog,null);
                        objdbr.setTitle("此次跑步詳細資訊如下：");

                        objdbr.setView(v);

                        final View finalV = v;
                        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //回傳輸入的值，再用Toast顯示。
                                TextView breakfast = (TextView)(finalV.findViewById(R.id.showdistance));
                                TextView lunch =(TextView)(finalV.findViewById(R.id.showspeed));
                                TextView dinner =(TextView)(finalV.findViewById(R.id.showtime));
                            }
                        }).show();
                    }
                });
            }
        }


        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.record_item, parent, false);
            RecordFragment.MyAdapter.MyViewHolder vh = new RecordFragment.MyAdapter.MyViewHolder(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
            holder.title.setText(data.get(position).get("recordtitle"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("hank","Click"+position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}