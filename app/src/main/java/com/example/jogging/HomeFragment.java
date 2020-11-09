package com.example.jogging;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<PostModel> postList;
    AlertDialog.Builder objdbr;
    private  MainActivity mainActivity;
    private FloatingActionButton addrecord;
    private JSONArray result;
    View view;
    public HomeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        addrecord = (FloatingActionButton)view.findViewById(R.id.btn_addrecordoffood);


        recyclerView = view.findViewById(R.id.home_recycleView);
        recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();
        getData();


        addrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast objtoast = Toast.makeText(mainActivity,"早餐:", Toast.LENGTH_SHORT);
                objtoast.show();
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
                        myAdapter.prc_showmessage(breakfast.getText().toString(),lunch.getText().toString(),dinner.getText().toString(),extra.getText().toString());
                    }
                }).show();


            }
        });
        return view;
    }


    private void getData(){
//        192.168.1.235

        //在這邊抓取資料 並塞入data中
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        String url = "http://10.0.102.244:8080/jogging-jdbc/test.jsp";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject j = null;
                Document html = Jsoup.parse(response);
                Element body = html.body();
                String b = body.toString().replaceAll("<.*>","").trim();
                Log.d("tag",b);


                try {
                    j = new JSONObject(new String(b.getBytes(),"UTF-8"));
                    Log.d("tag",j.toString());
                    result = j.getJSONArray("diet_record");
                    Log.d("tag",result.toString());
                    for (int i = 0; i < result.length(); i++) {

                        JSONObject jsonObject = result.getJSONObject(i);

                        PostModel postModel = new PostModel();
                        postModel.setBreakfast(jsonObject.getString("breakfast"));
                        postModel.setLunch(jsonObject.getString("lunch"));
                        postModel.setDinner(jsonObject.getString("dinner"));
                        postModel.setExtra(jsonObject.getString("extra"));
                        postList.add(postModel);

                        Log.d("tag","" + jsonObject.getString("breakfast"));
                        Log.v("tag", "" + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                myAdapter = new MyAdapter(getActivity().getApplicationContext(),postList);
                recyclerView.setAdapter(myAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);


    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        Context mContext;
        List<PostModel> postList;

        public MyAdapter(Context mContext, List<PostModel> postList) {
            this.mContext = mContext;
            this.postList = postList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {
            private Button item;
            private View itemView;
            private TextView title,breakfast,lunch,dinner,extra;



            public MyViewHolder(View v) {
                super(v);
                itemView = v;
//                title = (TextView)itemView.findViewById(R.id.item_article);
                item = (Button) itemView.findViewById(R.id.homeitem);
                breakfast = (TextView) itemView.findViewById(R.id.item_breakfast);
                lunch = (TextView)itemView.findViewById(R.id.item_lunch);
                dinner = (TextView)itemView.findViewById(R.id.item_dinner);
                extra = (TextView)itemView.findViewById(R.id.item_extrafood);

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

            View v = LayoutInflater.from(mainActivity).inflate(R.layout.home_item, parent, false);
            MyViewHolder vHolder = new MyViewHolder(v);
            return vHolder;

        }
        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {

            //在這邊抓取資料內容
//            holder.title.setText(postList.get(position).getBreakfast());

            Log.d("tag",postList.get(position).getBreakfast());
            holder.breakfast.setText("早餐: " + postList.get(position).getBreakfast());
            holder.lunch.setText("午餐: " + postList.get(position).getLunch());
            holder.dinner.setText("晚餐: " + postList.get(position).getDinner());
            holder.extra.setText("額外飲食: " + postList.get(position).getExtra());

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.v("hank","Click"+position);
//                }
//            });

        }
        @Override
        public int getItemCount() {
            //在這邊抓取資料筆數
            return postList.size();
        }

    }


}