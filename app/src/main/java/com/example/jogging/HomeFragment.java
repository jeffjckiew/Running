package com.example.jogging;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private LinkedList<HashMap<String,String>> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                    }
                });
            }
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
            return data.size();
        }
    }

}