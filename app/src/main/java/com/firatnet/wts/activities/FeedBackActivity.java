package com.firatnet.wts.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firatnet.wts.R;
import com.firatnet.wts.classes.PreferenceHelper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.firatnet.wts.classes.JsonTAG.TAG_DESCRIPTION;
import static com.firatnet.wts.classes.JsonTAG.TAG_ISSUE;
import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_STUDENT_ID;
import static com.firatnet.wts.classes.JsonTAG.TAG_TITLE;
import static com.firatnet.wts.classes.URLTAG.ADD_FEEDBACK_URL;

public class FeedBackActivity extends AppCompatActivity {


    private Spinner issue;
    private EditText message;
    private Button send;

    private ProgressDialog progressDialog;
    private PreferenceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        issue = findViewById(R.id.select_issue);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        helper = new PreferenceHelper(getBaseContext());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (issue.getSelectedItem().toString().equals("Select")) {
                    Snackbar.make(findViewById(R.id.body), "Please select an issue", Snackbar.LENGTH_LONG).show();
                } else if (message.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.body), "Please enter your feedback", Snackbar.LENGTH_LONG).show();
                } else {
                    sendFeedback();
                }
            }
        });


    }

    private void sendFeedback() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending feedback");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, ADD_FEEDBACK_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    message.setText("");
                    //Toast.makeText(getBaseContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.body), "Feedback sent successfully", Snackbar.LENGTH_LONG).show();
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
                params.put(TAG_STUDENT_ID, helper.getSettingValueId());
                params.put(TAG_ISSUE, issue.getSelectedItem().toString());
                params.put(TAG_MESSAGE, message.getText().toString());


                return params;

            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }
}
