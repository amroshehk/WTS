package com.firatnet.wst.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.classes.StaticMethod;
import com.firatnet.wst.entities.Student;
import com.firatnet.wst.phoneauth.PhoneNumberAuthActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wst.classes.JsonTAG.TAG_DEPARTMENT;
import static com.firatnet.wst.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wst.classes.JsonTAG.TAG_ERROR;
import static com.firatnet.wst.classes.JsonTAG.TAG_FACULTY;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_LEVEL;
import static com.firatnet.wst.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wst.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wst.classes.JsonTAG.TAG_PASSWORD;
import static com.firatnet.wst.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wst.classes.JsonTAG.TAG_PHOTO_URL;
import static com.firatnet.wst.classes.JsonTAG.TAG_RESULTS;
import static com.firatnet.wst.classes.JsonTAG.TAG_SUBJECT;
import static com.firatnet.wst.classes.URLTAG.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_et, pw_signin_et;
    public Button btnSignin,forot_pw_btn;
    private TextView error,tandc,privacy;
    private Context context;
    private ProgressDialog progressDialog;
  //  private static JSONArray jsonArray = null;
    public String email, password;
    //String token;

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

        context=this;
        email_et = findViewById(R.id.email_et);
        pw_signin_et = findViewById(R.id.pw_signin_et);
        pw_signin_et.setTransformationMethod(new PasswordTransformationMethod());

        Typeface typeface= ResourcesCompat.getFont(context,  R.font.roboto_thin);
        pw_signin_et.setTypeface(typeface);

        btnSignin = findViewById(R.id.btnSignin);
        forot_pw_btn = findViewById(R.id.forot_pw_btn);
        error = findViewById(R.id.error);
        tandc = findViewById(R.id.tandc);
        privacy = findViewById(R.id.privacy);
        context = this;

        try {
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            email_et.setText(email);
            pw_signin_et.setText(password);
        } catch (Exception ignored) {

        }
        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TermsConditionActivity.class);
                intent.putExtra("URL","https://ivyn.in/app/terms_and_conditions.html");
                intent.putExtra("TCorPP","2");
                startActivity(intent);

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TermsConditionActivity.class);
                intent.putExtra("URL","https://ivyn.in/app/privacy_policy.html");
                intent.putExtra("TCorPP","1");
                startActivity(intent);

            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Objects.requireNonNull(email_et.getText()).toString();
                final String pw = Objects.requireNonNull(pw_signin_et.getText()).toString();

                if (isInputsValid()) {
                    if (StaticMethod.ConnectChecked(context)) {


//                        token = SharedPrefManager.getInstance(context).getDeviceToken();
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("Login Tokrn", msg);

                      LoginServer(email, pw);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        forot_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

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

        if ( !isValidEmail(Objects.requireNonNull(email_et.getText()).toString())) {
            // nameLayout.setErrorEnabled(true);
            email_et.setError("Please enter correct email");
            return false;
        }

        else if ( !isPasswordValid(Objects.requireNonNull(pw_signin_et.getText()).toString()) ) {

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


    private void LoginServer(final String email, final String pw) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    if (obj.getString(TAG_MESSAGE).equals("Student logged in successfully")) {


                        obj = obj.getJSONObject(TAG_RESULTS);
                        String id = obj.getString(TAG_ID);
                        String name = obj.getString(TAG_NAME);
                        String email = obj.getString(TAG_EMAIL);
                        String phone = obj.getString(TAG_PHONE);
                        String photo_url = obj.getString(TAG_PHOTO_URL);
                        String faculty = obj.getString(TAG_FACULTY);
                        String department = obj.getString(TAG_DEPARTMENT);
                        String subject = obj.getString(TAG_SUBJECT);
                        String level = obj.getString(TAG_LEVEL);


                        Student student =new Student(id,name,email,phone,photo_url,faculty,department,subject,level);

                        helper.setLoginState(true);
                        helper.saveUser(student);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    } else if (!obj.getBoolean(TAG_ERROR)) {
                        Toast.makeText(getApplicationContext(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
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
