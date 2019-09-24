package com.firatnet.wst.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.firatnet.wst.classes.StaticMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wst.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wst.classes.JsonTAG.TAG_ERROR;
import static com.firatnet.wst.classes.JsonTAG.TAG_SUCCESS;
import static com.firatnet.wst.classes.URLTAG.GET_RESET_PW_URL;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView error,tandc,privacy;
    private EditText email_et;
    private Button send_btn;
    private ProgressDialog progressDialog;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tandc = findViewById(R.id.tandc);
        privacy = findViewById(R.id.privacy);
        email_et = findViewById(R.id.email_et);
        send_btn = findViewById(R.id.send_btn);
        context=this;
        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, TermsConditionActivity.class);
                intent.putExtra("URL","https://ivyn.in/app/terms_and_conditions.html");
                intent.putExtra("TCorPP","2");
                startActivity(intent);

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, TermsConditionActivity.class);
                intent.putExtra("URL","https://ivyn.in/app/privacy_policy.html");
                intent.putExtra("TCorPP","1");
                startActivity(intent);

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = Objects.requireNonNull(email_et.getText()).toString();
                if (isInputsValid()) {
                if (StaticMethod.ConnectChecked(context)) {


                    resetPasswordServer(email);

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
            }
        });
    }
    private boolean isInputsValid() {

        if ( !isValidEmail(Objects.requireNonNull(email_et.getText()).toString())) {
            // nameLayout.setErrorEnabled(true);
            email_et.setError("Please enter correct email");
            return false;
        }

        return true;

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void resetPasswordServer(final String email ) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("waiting ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, GET_RESET_PW_URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    if (obj.getBoolean(TAG_SUCCESS)) {

                        Toast.makeText(getApplicationContext(),"you are receive your password on email", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    } else if (!obj.getBoolean(TAG_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Your Email not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Your Email not found", Toast.LENGTH_SHORT).show();
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
                Log.i("TAGvLogin",message);
//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    JSONObject jsonObject = new JSONObject(responseBody);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Your Email not found", Toast.LENGTH_SHORT).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_EMAIL, email);
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
}
