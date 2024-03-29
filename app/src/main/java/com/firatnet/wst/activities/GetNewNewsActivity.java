package com.firatnet.wst.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firatnet.wst.R;
import com.firatnet.wst.adapter.RecyclerAllNewsCardAdapter;
import com.firatnet.wst.adapter.RecyclerAllPostsCardAdapter;
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.classes.StaticMethod;
import com.firatnet.wst.entities.Category;
import com.firatnet.wst.entities.News;
import com.firatnet.wst.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.firatnet.wst.classes.JsonTAG.TAG_CATEGORY;
import static com.firatnet.wst.classes.JsonTAG.TAG_CATEGORY_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_CONTACT_NO;
import static com.firatnet.wst.classes.JsonTAG.TAG_CONTENT;
import static com.firatnet.wst.classes.JsonTAG.TAG_CREATED_AT;
import static com.firatnet.wst.classes.JsonTAG.TAG_DESCRIPTION;
import static com.firatnet.wst.classes.JsonTAG.TAG_ERROR;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_PIC_URL;
import static com.firatnet.wst.classes.JsonTAG.TAG_POST_IMAGE_URL;
import static com.firatnet.wst.classes.JsonTAG.TAG_PRICE;
import static com.firatnet.wst.classes.JsonTAG.TAG_RESULTS;
import static com.firatnet.wst.classes.JsonTAG.TAG_SUCCESS;
import static com.firatnet.wst.classes.JsonTAG.TAG_TITLE;
import static com.firatnet.wst.classes.JsonTAG.TAG_UPDATED_AT;
import static com.firatnet.wst.classes.URLTAG.GET_ALL_POStS_URL;
import static com.firatnet.wst.classes.URLTAG.GET_NEWS_URL;

public class GetNewNewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public TextView nonews;
    ArrayList<News> news;
    ArrayList<News> news_filtered;
    private ProgressDialog progressDialog;
    public PreferenceHelper helper;
    Context context;
    private static JSONArray postsJsonArray = null;
    private Spinner category_sp;
    boolean checkfirst = true;
    ArrayList<Category> categories ;
    ArrayList<String> categories_list ;
    ArrayList<String> categories_id_list ;
    ArrayAdapter<String> spiner_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_new_news);
        recyclerView = findViewById(R.id.recyclerview);
        category_sp = findViewById(R.id.category_sp);
        nonews = findViewById(R.id.nonews);

        recyclerView.setLayoutManager(layoutManager);
        context = this;
        layoutManager = new LinearLayoutManager(context);
        helper = new PreferenceHelper(context);
        news = new ArrayList<>();
        news_filtered = new ArrayList<>();

        Intent intent=getIntent();
        categories=new ArrayList<>();
        categories_list=new ArrayList<>();
        categories_id_list=new ArrayList<>();
        categories=intent.getParcelableArrayListExtra("CATEGORIES");
        categories_list.add("All");
        categories_id_list.add("All");

        for (int i=0;i<categories.size();i++)
        {
            categories_list.add(categories.get(i).getCategory());
        }
        for (int i=0;i<categories.size();i++)
        {
            categories_id_list.add(String.valueOf(categories.get(i).getId()));
        }

        spiner_adapter =new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categories_list);
        category_sp.setAdapter(spiner_adapter);

        if (StaticMethod.ConnectChecked(context)) {
            GetNewsServer();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cat=categories_list.get(position);
                String cat_id=categories_id_list.get(position);
                if(cat.equals("All"))
                {
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new RecyclerAllNewsCardAdapter(news, context, nonews);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    news_filtered = new ArrayList<>();
                    for (int i=0;i<news.size();i++)
                    {
                        if(news.get(i).getCategory_id().equals(cat_id))
                        {
                            news_filtered.add(news.get(i));
                        }
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new RecyclerAllNewsCardAdapter(news_filtered, context, nonews);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void GetNewsServer() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, GET_NEWS_URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    if (obj.getBoolean(TAG_SUCCESS)) {

                        postsJsonArray = obj.getJSONArray(TAG_RESULTS);


                        for (int i = 0; i < postsJsonArray.length(); i++) {

                            obj = postsJsonArray.getJSONObject(i);
                            int id = Integer.parseInt(obj.getString(TAG_ID));
                            String content = obj.getString(TAG_CONTENT);
                            String pic_url = obj.getString(TAG_PIC_URL);
                            String created_at = obj.getString(TAG_CREATED_AT);
                            String updated_at = obj.getString(TAG_UPDATED_AT);
                            String category_id = obj.getString(TAG_CATEGORY_ID);



                            News news_o = new News(id, category_id,content,pic_url, created_at, updated_at);
                            news.add(news_o);

                        }
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new RecyclerAllNewsCardAdapter(news, context, nonews);
                        recyclerView.setAdapter(adapter);

                    } else if (!obj.getBoolean(TAG_ERROR)) {
                        Toast.makeText(getApplicationContext(), "This student doesn't have any news", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "This student doesn't have any news", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    // Toast.makeText(getApplicationContext(), "error JSONException", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                String message = "";
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The email is not found, please register!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Log.i("TAGvLogin", message);
//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    JSONObject jsonObject = new JSONObject(responseBody);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "This student doesn't have any news", Toast.LENGTH_SHORT).show();

            }
        }
        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json; charset=utf-8");
//                params.put(TAG_ID, id);
//                return params;
//            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.getCache().clear();
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }
}
