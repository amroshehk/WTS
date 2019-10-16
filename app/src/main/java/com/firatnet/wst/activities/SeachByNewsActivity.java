package com.firatnet.wst.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.classes.StaticMethod;
import com.firatnet.wst.entities.Category;
import com.firatnet.wst.entities.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.firatnet.wst.classes.JsonTAG.TAG_CATEGORY_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_CONTENT;
import static com.firatnet.wst.classes.JsonTAG.TAG_CREATED_AT;
import static com.firatnet.wst.classes.JsonTAG.TAG_ERROR;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_KEYWORDS;
import static com.firatnet.wst.classes.JsonTAG.TAG_PIC_URL;
import static com.firatnet.wst.classes.JsonTAG.TAG_RESULTS;
import static com.firatnet.wst.classes.JsonTAG.TAG_SUCCESS;
import static com.firatnet.wst.classes.JsonTAG.TAG_UPDATED_AT;
import static com.firatnet.wst.classes.URLTAG.GET_NEWS_URL;
import static com.firatnet.wst.classes.URLTAG.SEARCH_BY_NEWS_URL;

public class SeachByNewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public TextView nonews;
    ArrayList<News> news;
    private ProgressDialog progressDialog;
    public PreferenceHelper helper;
    Context context;
    private static JSONArray postsJsonArray = null;
    private SearchView serach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_by_news);

        recyclerView = findViewById(R.id.recyclerview);
        serach = findViewById(R.id.serach);
        nonews = findViewById(R.id.nonews);

        recyclerView.setLayoutManager(layoutManager);
        context = this;
        layoutManager = new LinearLayoutManager(context);
        helper = new PreferenceHelper(context);
        news = new ArrayList<>();

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        serach.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        serach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (StaticMethod.ConnectChecked(context)) {
                    clear();
                    nonews.setVisibility(View.GONE);
//                    progressDialog.dismiss();
                    GetNewsServer(query);
                    return false;

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void GetNewsServer(final String query) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, SEARCH_BY_NEWS_URL, new Response.Listener<String>() {
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_KEYWORDS, query);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.getCache().clear();
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    public void clear() {
        news.clear();
        nonews.setVisibility(View.GONE);
        adapter = new RecyclerAllNewsCardAdapter(news, context, nonews);
        recyclerView.setAdapter(adapter);
    }
}
