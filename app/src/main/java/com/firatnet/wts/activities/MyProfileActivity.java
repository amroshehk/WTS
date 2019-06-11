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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.firatnet.wts.entities.Student;
import com.google.android.material.textfield.TextInputEditText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wts.classes.JsonTAG.TAG_DEPARTMENT;
import static com.firatnet.wts.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wts.classes.JsonTAG.TAG_FACULTY;
import static com.firatnet.wts.classes.JsonTAG.TAG_ID;
import static com.firatnet.wts.classes.JsonTAG.TAG_LEVEL;
import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wts.classes.JsonTAG.TAG_PASSWORD;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHOTO_URL;
import static com.firatnet.wts.classes.JsonTAG.TAG_SUBJECT;
import static com.firatnet.wts.classes.URLTAG.EDIT_PROFILE_URL;
import static com.firatnet.wts.classes.URLTAG.REGISTER_URL;

public class MyProfileActivity extends AppCompatActivity {
    private TextInputEditText name_et,email_et,phone_et,faculty_et,department_et,subject_et,level_et;
    private Context context;
    public Button btnEdit;
    private TextView error2;
    private ProgressDialog progressDialog;
    private ImageButton edit_image;
    ImageView photo;
    ImageLoaderConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        photo=findViewById(R.id.photo);

        name_et=findViewById(R.id.name_et);
        email_et=findViewById(R.id.email_et);

        phone_et=findViewById(R.id.phone_et);
        faculty_et=findViewById(R.id.faculty_et);
        department_et=findViewById(R.id.department_et);
        subject_et=findViewById(R.id.subject_et);
        level_et=findViewById(R.id.level_et);

        btnEdit=findViewById(R.id.btnEdit);
        edit_image=findViewById(R.id.edit_image);
        error2=findViewById(R.id.error2);

        context=this;

        initProfile();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name= Objects.requireNonNull(name_et.getText()).toString();
                String email= Objects.requireNonNull(email_et.getText()).toString();

                String phone= Objects.requireNonNull(phone_et.getText()).toString();
                String faculty= Objects.requireNonNull(faculty_et.getText()).toString();
                String department= Objects.requireNonNull(department_et.getText()).toString();

                String subject= Objects.requireNonNull(subject_et.getText()).toString();
                String level= Objects.requireNonNull(level_et.getText()).toString();


                String error_m="";
                if(email.equals("") || name.equals("")||phone.equals(""))
                {
                    error_m=" Please Enter All Required Filed";
                }
                else if(!isValidEmail(email))
                {
                    error_m="Please Enter Valid Email";
                }

                error2.setText(error_m);
                if(error_m.equals(""))
                {

                    if (StaticMethod.ConnectChecked(context))
                    {
                        PreferenceHelper helper=new PreferenceHelper(context);

                        final Student student =new Student(helper.getSettingValueId(),name,email,phone,helper.getSettingValuePhotoUrl()
                                ,faculty,department,subject,level);

                       EditProfielUserServer(student);


                    } else {

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyProfileActivity.this,ImagesViewerActivity.class);
                startActivity(intent);
            }
        });
    }
    public void  initProfile()
    {  PreferenceHelper helper = new PreferenceHelper(context);

        name_et.setText(helper.getSettingValueName());
        email_et.setText(helper.getSettingValueEmail());

        phone_et.setText(helper.getSettingValuePhone());
        faculty_et.setText(helper.getSettingValueFaculty());
        department_et.setText(helper.getSettingValueDepartment());
        subject_et.setText(helper.getSettingValueSubject());
        level_et.setText(helper.getSettingValueLevel());


        getUserPhoto();
    }

    void getUserPhoto() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        PreferenceHelper helper = new PreferenceHelper(context);
        if (!helper.getSettingValuePhotoUrl().isEmpty())
            imageLoader.displayImage(helper.getSettingValuePhotoUrl(), photo, options);
        else
            photo.setBackgroundResource(R.drawable.user_default);

    }
    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void EditProfielUserServer(final Student student) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest( Request.Method.POST, EDIT_PROFILE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();

                    if (obj.getString(TAG_MESSAGE).equals("The student successfully edited")) {

                        PreferenceHelper helper = new PreferenceHelper(context);
                        helper.saveUser(student);
                        Toast.makeText(getApplicationContext(),"The student successfully edited", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
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
                params.put(TAG_ID, student.getId());
                params.put(TAG_NAME, student.getName());
//                params.put(TAG_EMAIL, student.getEmail());
//                params.put(TAG_PHONE, student.getPhone());
                params.put(TAG_PHOTO_URL, student.getPhoto_url());
                params.put(TAG_FACULTY, student.getFaculty());
                params.put(TAG_DEPARTMENT, student.getDepartment());
                params.put(TAG_SUBJECT, student.getSubject());
                params.put(TAG_LEVEL, student.getSubject());
//                params.put(TAG_PASSWORD, student.getPassword());

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
