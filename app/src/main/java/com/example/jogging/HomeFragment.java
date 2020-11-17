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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<PostModel> postList;
    AlertDialog.Builder objdbr;
    private  MainActivity mainActivity;
    private FloatingActionButton addrecord;
    private JSONArray result;
    View view;
    String savedata;
    String JSON_URL ="http://192.168.3.25:8080/jogging-hibernate-spring-tx/json/";
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
                        addFoods(breakfast.getText().toString(),lunch.getText().toString(),dinner.getText().toString(),extra.getText().toString());
                        mainActivity.reload(HomeFragment.this);
                    }
                }).show();


            }
        });
        return view;
    }


    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
//        String JSON_URL ="http://10.0.102.100:8080/jogging-hibernate-spring-tx/json/";
        String select_URL =JSON_URL+"diet.record";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, select_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i< response.length(); i++){
                    JSONObject j = null;
                    try {
                        j = response.getJSONObject(i);
                        PostModel postModel = new PostModel();
                        postModel.setId(Integer.parseInt(j.getString("id")));
                        postModel.setDate(j.getString("date"));
                        postModel.setBreakfast(j.getString("breakfast"));
                        postModel.setLunch(j.getString("lunch"));
                        postModel.setDinner(j.getString("dinner"));
                        postModel.setExtra(j.getString("extra"));
                        postList.add(postModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                myAdapter = new MyAdapter(getActivity().getApplicationContext(),postList);
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



    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

//        Context mContext;
        List<PostModel> postList;

        public MyAdapter(Context mContext, List<PostModel> postList) {
//            this.mContext = mContext;
            this.postList = postList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {
            private FloatingActionButton item,delete;
            private View itemView;
            private TextView breakfast,lunch,dinner,extra,id,date;



            public MyViewHolder(View v) {
                super(v);
                itemView = v;
//                title = (TextView)itemView.findViewById(R.id.item_article);
                item = itemView.findViewById(R.id.homeitem);
                delete = itemView.findViewById(R.id.homeitem_delete);
                id=(TextView) itemView.findViewById(R.id.item_id);
                breakfast = (TextView) itemView.findViewById(R.id.item_breakfast);
                lunch = (TextView)itemView.findViewById(R.id.item_lunch);
                dinner = (TextView)itemView.findViewById(R.id.item_dinner);
                extra = (TextView)itemView.findViewById(R.id.item_extrafood);
                date = (TextView)itemView.findViewById(R.id.home_item_date);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
//                        由此處新增點選各項item後的內容顯示
                        objdbr = new AlertDialog.Builder(mainActivity);
                        LayoutInflater inflater = LayoutInflater.from(mainActivity);
                        v = inflater.inflate(R.layout.homepage_dialog,null);
                        objdbr.setTitle("更新飲食資料：");

                        objdbr.setView(v);

                        final View finalV = v;
                        objdbr.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //回傳輸入的值，再用Toast顯示。
                                TextView dialogid = (TextView) (finalV.findViewById(R.id.dialogid));
                                dialogid.setText(id.getText());
                                EditText breakfast = (EditText)(finalV.findViewById(R.id.breakfast));
                                EditText lunch =(EditText)(finalV.findViewById(R.id.lunch));
                                EditText dinner =(EditText)(finalV.findViewById(R.id.dinner));
                                EditText extra =(EditText)(finalV.findViewById(R.id.extrafood));
                                prc_showmessage(breakfast.getText().toString(),lunch.getText().toString(),dinner.getText().toString(),extra.getText().toString());
                                updateFoods((String) dialogid.getText(),breakfast.getText().toString(),lunch.getText().toString(),dinner.getText().toString(),extra.getText().toString());
                                mainActivity.reload(HomeFragment.this);
                            }

                        }).show();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletFoods((String) id.getText());
                        mainActivity.reload(HomeFragment.this);
                    }
                });

            }
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
            holder.id.setText(postList.get(position).getId()+"");
            holder.breakfast.setText("早餐: " + postList.get(position).getBreakfast());
            holder.lunch.setText("午餐: " + postList.get(position).getLunch());
            holder.dinner.setText("晚餐: " + postList.get(position).getDinner());
            holder.extra.setText("額外: " + postList.get(position).getExtra());
            holder.date.setText(postList.get(position).getDate());
        }
        @Override
        public int getItemCount() {
            //在這邊抓取資料筆數
            return postList.size();
        }

    }
    public void prc_showmessage(String breakfast,String lunch,String dinner,String extra)
    {
        Toast objtoast = Toast.makeText(mainActivity,"早餐:"+breakfast+"午餐:"+lunch+"晚餐:"+dinner+"額外飲食:"+extra, Toast.LENGTH_SHORT);
        objtoast.show();
        //在此處將飲食紀錄存入
    }
    public void updateFoods(final String dialogid, final String breakfast,
                            final String lunch, final String dinner, final String extra) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        //        String JSON_URL ="http://10.0.102.100:8080/jogging-hibernate-spring-tx/json/";
        String update_URL =JSON_URL+"update/"+dialogid;
        JSONObject  object = new JSONObject ();
        Log.v("hank",JSON_URL);
        try {
            object.put("breakfast",breakfast);
            object.put("lunch",lunch);
            object.put("dinner",dinner);
            object.put("extra",extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            Log.v("hank",object.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, update_URL, object, new Response.Listener<JSONObject>() {
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

    public void addFoods(final String breakfast,
                            final String lunch, final String dinner, final String extra) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
//        String add_URL ="http://10.0.102.100:8080/jogging-hibernate-spring-tx/json/insert";
        String add_URL =JSON_URL+"insert";
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String foodDate = year+"年"+month+"月"+day+"日";
        JSONObject  object = new JSONObject ();
        Log.v("hank",JSON_URL);
        try {
            object.put("breakfast",breakfast);
            object.put("lunch",lunch);
            object.put("dinner",dinner);
            object.put("extra",extra);
            object.put("date",foodDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("hank",object.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, add_URL, object, new Response.Listener<JSONObject>() {
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

    public void deletFoods(String id) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
//        String JSON_URL ="http://10.0.102.100:8080/jogging-hibernate-spring-tx/json/";
        String delet_URL =JSON_URL+"delete/"+id;
        Log.v("hank",JSON_URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, delet_URL, null, new Response.Listener<JSONObject>() {
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

