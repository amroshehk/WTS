package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.firatnet.wts.classes.StaticMethod;
import com.firatnet.wts.entities.Student;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.firatnet.wts.R;


import org.json.JSONException;
import org.json.JSONObject;

import static com.firatnet.wts.classes.JsonTAG.TAG_DEPARTMENT;
import static com.firatnet.wts.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wts.classes.JsonTAG.TAG_FACULTY;
import static com.firatnet.wts.classes.JsonTAG.TAG_LEVEL;
import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wts.classes.JsonTAG.TAG_PASSWORD;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHOTO_URL;
import static com.firatnet.wts.classes.JsonTAG.TAG_SUBJECT;
import static com.firatnet.wts.classes.URLTAG.REGISTER_URL;

public class RegisterActivity extends AppCompatActivity {

    private String phonenumber;
    private Context context;
    private TextInputEditText name_et,email_et,pw_signup_et,confpw_signup_et;
    public Button btnSignup;
    private TextView error2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_et=findViewById(R.id.name_et);
        email_et=findViewById(R.id.email_et);
        pw_signup_et=findViewById(R.id.pw_signup_et);
        confpw_signup_et=findViewById(R.id.confpw_signup_et);
        btnSignup=findViewById(R.id.btnSignup);
        error2=findViewById(R.id.error2);

        context=this;

        //get intent Extra
        phonenumber = getIntent().getStringExtra("phonenumber");
        //counterName = getIntent().getStringExtra("counterName");


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name= Objects.requireNonNull(name_et.getText()).toString();
                String email= Objects.requireNonNull(email_et.getText()).toString();
                String pw=  Objects.requireNonNull(pw_signup_et.getText()).toString();
                String conf_pw=  Objects.requireNonNull(confpw_signup_et.getText()).toString();

                String error_m="";
                if(email.equals("") || name.equals("")||pw.equals("")||conf_pw.equals(""))
                {
                    error_m=" Please Enter All Filed";
                }
                else if(!isValidEmail(email))
                {
                    error_m="Please Enter Valid Email";
                }
                else if(!pw.equals(conf_pw))
                {
                    error_m="Password does not match Confirm Password";
                }
                error2.setText(error_m);
                if(error_m.equals(""))
                {

                    if (StaticMethod.ConnectChecked(context))
                    {
                       // final Student student =new Student(name,email,phonenumber,counterName);
                        final Student student =new Student(name,email,phonenumber,pw);


                      //  token = SharedPrefManager.getInstance(context).getDeviceToken();
                     //   String msg = getString(R.string.msg_token_fmt, token);
                       // Log.d("RegisterActivity Tokrn", msg);
                        // Toast.makeText(RegisterActivity.this, msg, ToastENGTH_LONG).show();
                        RegisterNewUserServer(student);


                    } else {

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


    }


    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    private void RegisterNewUserServer(final Student student) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest( Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();

                    if (obj.getString(TAG_MESSAGE).equals("The student successfully registered")) {

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("email", student.getEmail());
                    intent.putExtra("password", student.getPassword());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    } else  {
                        Toast.makeText(getApplicationContext(),"The student not register", Toast.LENGTH_SHORT).show();
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
                                        "This email is already exists",
                                        Toast.LENGTH_LONG).show();
                                break;

                            case 401:
                                Toast.makeText(getApplicationContext(),
                                        "This email is already exists",
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
                params.put(TAG_NAME, student.getName());
                params.put(TAG_EMAIL, student.getEmail());
                params.put(TAG_PHONE, student.getPhone());
                params.put(TAG_PASSWORD, student.getPassword());
//                params.put(TAG_PHOTO_URL, student.getPassword());
//                params.put(TAG_FACULTY, student.getPassword());
//                params.put(TAG_DEPARTMENT, student.getPassword());
//                params.put(TAG_SUBJECT, student.getPassword());
//                params.put(TAG_LEVEL, student.getPassword());

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
