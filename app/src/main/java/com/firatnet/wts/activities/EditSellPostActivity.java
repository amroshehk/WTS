package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firatnet.wts.R;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.classes.StaticMethod;
import com.firatnet.wts.entities.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wts.classes.JsonTAG.TAG_DESCRIPTION;
import static com.firatnet.wts.classes.JsonTAG.TAG_ID;
import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_TITLE;
import static com.firatnet.wts.classes.URLTAG.ADD_POSt_URL;
import static com.firatnet.wts.classes.URLTAG.EDIT_POSt_URL;

public class EditSellPostActivity extends AppCompatActivity {

    EditText title_et, description_et;
    TextView error2;
    Button edit_btn;
    private ProgressDialog progressDialog;
    public PreferenceHelper helper;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sell_post);
        Intent intent=getIntent();
        Post post=intent.getParcelableExtra("EDIT");
        title_et = findViewById(R.id.title_et);
        description_et = findViewById(R.id.description_et);
        edit_btn = findViewById(R.id.edit_btn);
        error2 = findViewById(R.id.error2);
        helper = new PreferenceHelper(getApplicationContext());
        context=this;
        title_et.setText(post.getTitle());
        description_et.setText(post.getDescription());
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_= Objects.requireNonNull(title_et.getText()).toString();
                String description_= Objects.requireNonNull(description_et.getText()).toString();
                String error_m="";
                if(title_.equals("") || description_.equals(""))
                {
                    error_m=" Please Enter All Filed";
                }
                error2.setText(error_m);
                if(error_m.equals(""))
                {

                    if (StaticMethod.ConnectChecked(context))
                    {

                        final Post Post =new Post(title_,description_);
                       EditPostServer(Post);


                    } else {

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void EditPostServer(final Post post) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, EDIT_POSt_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();

                    if (obj.getString(TAG_MESSAGE).equals("Post edited successfully")) {

                      finish();
                        Toast.makeText(context, "Post edited successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "The student is not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        progressDialog.dismiss();

                        switch (volleyError.networkResponse.statusCode) {

                            case 400:
                                Toast.makeText(getApplicationContext(),
                                        "here is validation errors",
                                        Toast.LENGTH_LONG).show();
                                break;

                            case 401:
                                Toast.makeText(getApplicationContext(),
                                        "here is validation errors",
                                        Toast.LENGTH_LONG).show();
                                break;

                            case 402:
                                Toast.makeText(getApplicationContext(),
                                        "There is validation errors",
                                        Toast.LENGTH_LONG).show();
                                break;

                            case 404:
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't reach the server",
                                        Toast.LENGTH_LONG).show();
                                break;

                        }

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_ID, String.valueOf(post.getId()));
                params.put(TAG_TITLE, post.getTitle());
                params.put(TAG_DESCRIPTION, post.getDescription());


                return params;

            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}
