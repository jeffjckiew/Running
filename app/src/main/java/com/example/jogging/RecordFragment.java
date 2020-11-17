package com.example.jogging;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RecordFragment extends Fragment {
    private FragmentManager fragmentManager;
    private RecordFragment record;
    private RecyclerView recyclerView;
    private RecordFragment.MyAdapter myAdapter;
    private List<PostModelRun> postRunList;
//    AlertDialog.Builder objdbr;
    private  MainActivity mainActivity;
    String JSON_URL ="http://192.168.3.25:8080/jogging-hibernate-spring-tx/run/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mainActivity = (MainActivity) getActivity();
        recyclerView = view.findViewById(R.id.record_recycleView);
        recyclerView.setHasFixedSize(true);
        postRunList = new ArrayList<>();
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        myAdapter = new RecordFragment.MyAdapter();
        getRecord();
//        recyclerView.setAdapter(myAdapter);

        return view;
    }
    private void getRecord(){
        //在這邊抓取資料 並塞入data中
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
//        String JSON_URL ="http://10.0.102.100:8080/jogging-hibernate-spring-tx/run/";
        String select_URL =JSON_URL+"run.record" ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, select_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i< response.length(); i++){
                    JSONObject j = null;
                    try {
                        j = response.getJSONObject(i);
                        PostModelRun postModelRun = new PostModelRun();
                        postModelRun.setId(Integer.parseInt(j.getString("id")));
                        postModelRun.setDate(j.getString("date"));
                        postModelRun.setRuntime(j.getString("runtime"));
                        postModelRun.setDistance(j.getString("distance"));
                        postModelRun.setPace(j.getString("pace"));
                        postRunList.add(postModelRun);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                myAdapter = new MyAdapter(getActivity().getApplicationContext(),postRunList);
                recyclerView.setAdapter(myAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorResponse"+error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);

    }



    private class MyAdapter extends RecyclerView.Adapter<RecordFragment.MyAdapter.MyViewHolder>{
        List<PostModelRun> postRunList;
        public MyAdapter(Context applicationContext, List<PostModelRun> postRunList) {
            this.postRunList = postRunList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private FloatingActionButton deleteItem;
            private View itemView;
            private TextView runDate,runDataId,runTime,runPace,runDistance;
            public MyViewHolder(View v) {
                super(v);
                itemView = v;
                runDate = itemView.findViewById(R.id.run_item_date);
                runDataId = itemView.findViewById(R.id.run_item_id);
                runTime = itemView.findViewById(R.id.run_item_time);
                runPace = itemView.findViewById(R.id.run_item_pace);
                runDistance = itemView.findViewById(R.id.run_item_distance);
                deleteItem= (FloatingActionButton) itemView.findViewById(R.id.run_item_delete);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteRunData((String)runDataId.getText());
                        mainActivity.reload(RecordFragment.this);
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
            holder.runDataId.setText(postRunList.get(position).getId()+"");
            holder.runDate.setText(postRunList.get(position).getDate());
            holder.runTime.setText(postRunList.get(position).getRuntime());
            holder.runPace.setText(postRunList.get(position).getPace());
            holder.runDistance.setText(postRunList.get(position).getDistance());
        }

        @Override
        public int getItemCount() {
            return postRunList.size();
        }
    }

    private void deleteRunData(String id ) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String delete_URL =JSON_URL+"delete/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, delete_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }


}