package com.firatnet.wts.activities;


import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.firatnet.wts.BuildConfig;
import com.firatnet.wts.R;

import com.firatnet.wts.classes.GalleryUtil;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.classes.StaticMethod;

import com.firatnet.wts.entities.Receiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


import static com.firatnet.wts.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wts.classes.JsonTAG.TAG_ERROR;

import static com.firatnet.wts.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wts.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wts.classes.JsonTAG.TAG_RESULTS;
import static com.firatnet.wts.classes.JsonTAG.TAG_SUCCESS;

import static com.firatnet.wts.classes.URLTAG.GET_ALL_RECEIVERS_URL;


public class AdminCornerActivity extends BaseActivity {

    EditText title_et, message_et;
    Spinner spinner;
    Button send_btn;
    ImageButton browse_btn;
    TextView error2,file_name;
    private final int GALLERY_ACTIVITY_CODE = 200;
    File file;
    ArrayList<Receiver> receivers;
    ArrayList<String> receivers_list;
    ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;
    public PreferenceHelper helper;
    Context context;
    String email_to,message,title;
    private static JSONArray JsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_corner);

        title_et = findViewById(R.id.title_et);
        message_et = findViewById(R.id.message_et);
        spinner = findViewById(R.id.spinner);
        send_btn = findViewById(R.id.send_btn);
        browse_btn = findViewById(R.id.browse_btn);
        error2 = findViewById(R.id.error2);
        file_name = findViewById(R.id.file_name);
        context = this;

        receivers_list=new ArrayList<>();
        receivers=new ArrayList<>();
        receivers_list.add("Select....");
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 title = Objects.requireNonNull(title_et.getText()).toString();
                 message = Objects.requireNonNull(message_et.getText()).toString();

                String error_m = "";
                if (title.equals("") || message.equals("")) {
                    error_m = " Please Enter All Filed";
                }
//                else if(spinner.getSelectedItemPosition()==0)
//                {
//                    error_m="Please select any item  _TO_";
//                }
                else if(file==null)
                {
                    error_m="Please browse image";
                }
                error2.setText(error_m);
                if (error_m.equals("")) {
                    if (StaticMethod.ConnectChecked(context)) {

                     //   email_to =receivers.get(spinner.getSelectedItemPosition()-1).getEmail();
                        email_to ="pawantiwari598@gmail.com";
                        sendImage();

                    } else {

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        browse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AdminCornerActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        });

//        if (StaticMethod.ConnectChecked(context)) {
//
//            GetMYpOSTServer();
//        } else {
//
//            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    imageBrowse();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AdminCornerActivity.this, "Permission denied to Write on your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    // to attach an image to email
    public void sendImage() {
        // TODO Auto-generated method stub

        Intent i = new Intent(Intent.ACTION_SEND);

        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setType("image/jpeg");
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            contentUri = FileProvider.getUriForFile(AdminCornerActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            i.putExtra(Intent.EXTRA_STREAM, contentUri); //Your image file
        } else {
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath())); //Your image file
        }

      //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_STREAM, Uri.parse(picturePath));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email_to});
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(i,"Send email via:"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String    picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                file = new File(picturePath);
                file_name.setText(file.getName());
            }
              }
}


    private void imageBrowse() {
        //if everything is ok we will open image chooser
        //  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  startActivityForResult(i, 100);
        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    private void GetMYpOSTServer() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting ... ");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, GET_ALL_RECEIVERS_URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    if (obj.getBoolean(TAG_SUCCESS)) {

                        JsonArray = obj.getJSONArray(TAG_RESULTS);


                        for (int i = 0; i < JsonArray.length(); i++) {

                            obj = JsonArray.getJSONObject(i);
//                            int id = Integer.parseInt(obj.getString(TAG_ID));
                            String name = obj.getString(TAG_NAME);
                            String email = obj.getString(TAG_EMAIL);
                            String phone_no = obj.getString(TAG_PHONE);


                            Receiver receiver = new Receiver(name, email, phone_no);
                            receivers.add(receiver);
                            receivers_list.add(name);

                        }
                        adapter =new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, receivers_list);
                         spinner.setAdapter(adapter);

                    } else if (!obj.getBoolean(TAG_ERROR)) {
                        Toast.makeText(getApplicationContext(), "There isn't any receivers", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "There isn't any receivers", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "This student doesn't have any posts 3", Toast.LENGTH_SHORT).show();

            }
        }
        ) {

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
