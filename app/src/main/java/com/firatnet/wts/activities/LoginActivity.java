package com.firatnet.wts.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.firatnet.wts.R;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.classes.StaticMethod;
import com.firatnet.wts.entities.Register;
import com.firatnet.wts.phoneauth.PhoneNumberAuthActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wts.classes.JsonTAG.TAG_COUNTRY;
import static com.firatnet.wts.classes.JsonTAG.TAG_CREATED_AT;
import static com.firatnet.wts.classes.JsonTAG.TAG_DATA;
import static com.firatnet.wts.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wts.classes.JsonTAG.TAG_ID;
import static com.firatnet.wts.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wts.classes.JsonTAG.TAG_PASSWORD;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHOTO_URL;
import static com.firatnet.wts.classes.JsonTAG.TAG_STATUS;
import static com.firatnet.wts.classes.URLTAG.LOGIN_URL;

public class LoginActivity extends Activity {

    private TextInputEditText email_et, pw_signin_et;
    public Button btnSignin;
    private TextView error;
    private Context context;
    private ProgressDialog progressDialog;
    private static JSONArray jsonArray = null;
    public String email, password;
    String token;

    public PreferenceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        helper = new PreferenceHelper(getApplicationContext());

        if (helper.getLoginState().equals("1")){
            Intent home = new Intent(getBaseContext(), MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email_et = findViewById(R.id.email_et);
        pw_signin_et = findViewById(R.id.pw_signin_et);
        btnSignin = findViewById(R.id.btnSignin);
        error = findViewById(R.id.error);
        context = this;

        try {
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            email_et.setText(email);
            pw_signin_et.setText(password);
        } catch (Exception ignored) {

        }


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Objects.requireNonNull(email_et.getText()).toString();
                final String pw = Objects.requireNonNull(pw_signin_et.getText()).toString();

                if (isInputsValid()) {
                    if (StaticMethod.ConnectChecked(context)) {


//                        token = SharedPrefManager.getInstance(context).getDeviceToken();
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Login Tokrn", msg);

                      LoginServer(email, pw, token);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getBaseContext(), PhoneNumberAuthActivity.class);
                startActivity(signup);
            }
        });

    }


    private boolean isInputsValid() {

        if ( !isValidEmail(email_et.getText().toString())) {
            // nameLayout.setErrorEnabled(true);
            email_et.setError("Please enter correct email");
            return false;
        }

        else if ( !isPasswordValid(pw_signin_et.getText().toString()) ) {

            pw_signin_et.setError("Please enter password");
            return false;
        }

        return true;

    }


    public boolean isPasswordValid(String target) {
//        return Pattern.matches("^(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,16}$", target);
        return !target.isEmpty();
    }


    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void LoginServer(final String email, final String pw,final  String token) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    if (obj.getString("message").equals("User logged in successfully")) {


                        obj = obj.getJSONObject(TAG_DATA);


                        String id = obj.getString(TAG_ID);
                        String name = obj.getString(TAG_NAME);
                        String email = obj.getString(TAG_EMAIL);

                        String created_at = obj.getString(TAG_CREATED_AT);
                        //String updated_at = obj.getString(TAG_UPDATED_AT);


                        String phone = obj.getString(TAG_PHONE);
                        String country = obj.getString(TAG_COUNTRY);

                      //  String generated_id = obj.getString(TAG_GENERATED_ID);
                        String status = obj.getString(TAG_STATUS);
                        String photo_url = obj.getString(TAG_PHOTO_URL);

                      //  String ip = obj.getString(TAG_IP);

                    //       String default_file = "";
                    //        if (obj.getString(TAG_DEFAULT_FILE) != null)
                    //         default_file = obj.getString(TAG_DEFAULT_FILE);

                        Register register=new Register(id,name,email,created_at,phone,country,status,photo_url);

                        helper.setLoginState(true);
                        helper.saveUser(register);

//                        //30 day
//                        Long tsLong = System.currentTimeMillis()/1000;//time stamp
//                        long cuuentTimeexpired=tsLong+(30*24*60*60)+1;//
//                        helper.setSettingValueLoginDataExpired(Long.toString(cuuentTimeexpired));

                        //       Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        /*intent.putExtra(TAG_EMAIL, email);*/
                        /*intent.putExtra(TAG_GENERATED_ID, generated_id);
                         */
                        startActivity(intent);


                    } else if (obj.getString("message").equals("Incorrect email or password")) {
                        error.setText("Incorrect email or password");
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

//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    JSONObject jsonObject = new JSONObject(responseBody);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Incorrect email or password", Toast.LENGTH_SHORT).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_EMAIL, email);
                params.put(TAG_PASSWORD, pw);
            //    params.put(TAG_API_TOKEN, token);
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
