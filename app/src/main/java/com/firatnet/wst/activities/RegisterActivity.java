package com.firatnet.wst.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.firatnet.wst.BuildConfig;
import com.firatnet.wst.classes.GalleryUtil;
import com.firatnet.wst.classes.ProcessProfilePhotoTask;
import com.firatnet.wst.classes.StaticMethod;
import com.firatnet.wst.classes.VolleyMultipartRequest;
import com.firatnet.wst.entities.Student;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.firatnet.wst.R;


import org.json.JSONException;
import org.json.JSONObject;

import static com.firatnet.wst.classes.JsonTAG.TAG_ADDRESS;
import static com.firatnet.wst.classes.JsonTAG.TAG_DEPARTMENT;
import static com.firatnet.wst.classes.JsonTAG.TAG_DESIGNATION;
import static com.firatnet.wst.classes.JsonTAG.TAG_EMAIL;
import static com.firatnet.wst.classes.JsonTAG.TAG_FACULTY;
import static com.firatnet.wst.classes.JsonTAG.TAG_HOSTELER;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID_PROOF;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID_PROOF_LIST;
import static com.firatnet.wst.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wst.classes.JsonTAG.TAG_NAME;
import static com.firatnet.wst.classes.JsonTAG.TAG_PASSWORD;
import static com.firatnet.wst.classes.JsonTAG.TAG_PHONE;
import static com.firatnet.wst.classes.JsonTAG.TAG_PHOTO;
import static com.firatnet.wst.classes.JsonTAG.TAG_RESULTS;
import static com.firatnet.wst.classes.JsonTAG.TAG_SUBJECT;
import static com.firatnet.wst.classes.URLTAG.REGISTER_URL;
import static com.firatnet.wst.classes.URLTAG.SAVE_ID_PROOOF_URL;
import static com.firatnet.wst.classes.URLTAG.SAVE_PHOTO_URL;

public class RegisterActivity extends AppCompatActivity {

    private String phonenumber;
    private Context context;
    private TextInputEditText name_et, email_et, pw_signup_et, confpw_signup_et;
    private TextInputEditText faculty_et, designation_et, subject_et, correspondence_address_et, department_et, hosteler_et;

    TextView select_idproof_image, select_your_photo;
    public Button btnSignup;
    private TextView error2;
    private ProgressDialog progressDialog;

    private Bitmap bitmap_idproof_image;
    private Bitmap bitmap_your_photo;

    int idprooforyourphoto = 0;//if 1 => id proof if 2 => your image

    private Spinner select_id_proof_list;
    ImageView idproof_photo, your_photo;
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;
    private static final String TEMP_PHOTO_FILE = "tempPhoto.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        pw_signup_et = findViewById(R.id.pw_signup_et);
        confpw_signup_et = findViewById(R.id.confpw_signup_et);
        btnSignup = findViewById(R.id.btnSignup);
        error2 = findViewById(R.id.error2);

        faculty_et = findViewById(R.id.faculty_et);
        designation_et = findViewById(R.id.designation_et);
        subject_et = findViewById(R.id.subject_et);
        correspondence_address_et = findViewById(R.id.correspondence_address_et);
        department_et = findViewById(R.id.department_et);
        hosteler_et = findViewById(R.id.hosteler_et);

        idproof_photo = findViewById(R.id.idproof_photo);
        your_photo = findViewById(R.id.your_photo);

        select_idproof_image = findViewById(R.id.select_idproof_image);
        select_your_photo = findViewById(R.id.select_your_photo);

        select_id_proof_list = findViewById(R.id.select_id_proof_list);


        context = this;

        //get intent Extra
        phonenumber = getIntent().getStringExtra("phonenumber");
        //counterName = getIntent().getStringExtra("counterName");


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = Objects.requireNonNull(name_et.getText()).toString();
                String email = Objects.requireNonNull(email_et.getText()).toString();
                String pw = Objects.requireNonNull(pw_signup_et.getText()).toString();
                String conf_pw = Objects.requireNonNull(confpw_signup_et.getText()).toString();
                String address = Objects.requireNonNull(correspondence_address_et.getText()).toString();
                String department = Objects.requireNonNull(department_et.getText()).toString();
                String hosteler = Objects.requireNonNull(hosteler_et.getText()).toString();
                String faculty = Objects.requireNonNull(faculty_et.getText()).toString();
                String subject = Objects.requireNonNull(subject_et.getText()).toString();
                String designation = Objects.requireNonNull(designation_et.getText()).toString();
                String id_proof_list = select_id_proof_list.getSelectedItem().toString();

                String error_m = "";
                if (email.equals("") || name.equals("") || pw.equals("") || conf_pw.equals("")) {
                    error_m = " Please enter all required filed";
                } else if (!isValidEmail(email)) {
                    error_m = "Please enter valid email";
                } else if (!pw.equals(conf_pw)) {
                    error_m = "Password does not match Confirm Password";
                } else if (select_id_proof_list.getSelectedItemPosition() == 0) {
                    error_m = "Please select identity proof";
                }
                error2.setText(error_m);
                if (error_m.equals("")) {

                    if (StaticMethod.ConnectChecked(context)) {
                        // final Student student =new Student(name,email,phonenumber,counterName);
                        final Student student = new Student(name, email, phonenumber, pw);
                        student.setAddress(address);
                        student.setDepartment(department);
                        student.setHosteler(hosteler);
                        student.setFaculty(faculty);
                        student.setSubject(subject);
                        student.setDesignation(designation);
                        student.setId_proof_list(id_proof_list);


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

        bitmap_idproof_image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.camera);
        bitmap_your_photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.camera);

        idproof_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idprooforyourphoto = 1;
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        });

        your_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idprooforyourphoto = 2;
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        });


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
                    Toast.makeText(RegisterActivity.this, "Permission denied to Write on your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void imageBrowse() {
        //if everything is ok we will open image chooser
        //  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  startActivityForResult(i, 100);
        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                performCrop(picturePath);
            }
        }

        if (requestCode == RESULT_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                //   Bundle extras = data.getExtras();
                //  bitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView

                File tempFile = getTempFile();
                processPhotoUpdate(tempFile);


            }
        }
    }


    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                contentUri = FileProvider.getUriForFile(RegisterActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        f);

            } else {
                contentUri = Uri.fromFile(f);
            }
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);
            cropIntent.putExtra("scale", true);

            // retrieve data on return
            cropIntent.putExtra("return-data", false);
            // start the activity - we handle returning in onActivityResult
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Unfortunately your device does not support cropping image";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        if (isSDCARDMounted()) {

            File folder = new File(Environment.getExternalStorageDirectory(), "Businessapp");//create folder
            folder.mkdir();

            File f = new File(Environment.getExternalStorageDirectory() + "/Businessapp/", TEMP_PHOTO_FILE);
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "File IO issue, can\\'t write temp file on this system", Toast.LENGTH_LONG).show();
            }
            return f;
        } else {
            return null;
        }
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    /*
     *  processes a temp photo file from
     */
    private void processPhotoUpdate(File tempFile) {
        @SuppressLint("StaticFieldLeak") ProcessProfilePhotoTask task = new ProcessProfilePhotoTask() {

            @Override
            protected void onPostExecute(Bitmap result) {

                if (idprooforyourphoto == 1) {
                    bitmap_idproof_image = result;
                    idproof_photo.setImageBitmap(bitmap_idproof_image);
                    select_idproof_image.setVisibility(View.GONE);
                } else if (idprooforyourphoto == 2) {
                    bitmap_your_photo = result;
                    your_photo.setImageBitmap(bitmap_your_photo);
                    select_your_photo.setVisibility(View.GONE);
                }

            }

        };
        task.execute(tempFile);

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void RegisterNewUserServer(final Student student) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();

                    if (obj.getString(TAG_MESSAGE).equals("The student successfully registered")) {

                        obj = obj.getJSONObject(TAG_RESULTS);
                        String id = obj.getString(TAG_ID);

                        student.setId(id);
                        Toast.makeText(getApplicationContext(), obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                        if (bitmap_idproof_image == null & bitmap_your_photo == null) {
                            goToLogin(student);
                        } else if (bitmap_idproof_image != null) {
                            SaveIdProofServer(student, bitmap_idproof_image);
                        } else if (bitmap_your_photo != null) {
                            SaveImageServer(student, bitmap_your_photo);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "The student not register", Toast.LENGTH_SHORT).show();
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
                params.put(TAG_FACULTY, student.getFaculty());
                params.put(TAG_DEPARTMENT, student.getDepartment());
                params.put(TAG_SUBJECT, student.getSubject());
                params.put(TAG_DESIGNATION, student.getDesignation());
                params.put(TAG_ADDRESS, student.getAddress());
                params.put(TAG_ID_PROOF_LIST, student.getId_proof_list());
                params.put(TAG_HOSTELER, student.getHosteler());

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


    private void SaveImageServer(final Student student, final Bitmap bitmap) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Save photo.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, SAVE_PHOTO_URL,
                new Response.Listener<NetworkResponse>() {


                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            progressDialog.dismiss();
                            if (obj.getBoolean("success")) {


                                Toast.makeText(getApplicationContext(), obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                goToLogin(student);
                            } else {

                                Toast.makeText(getApplicationContext(), "Error File not correct", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = "";
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    JSONObject jsonObject = new JSONObject(responseBody);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }) {
            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //   params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_ID, student.getId());

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put(TAG_PHOTO, new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }


        };

        //adding the request to volley

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    void goToLogin(Student student) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("email", student.getEmail());
        intent.putExtra("password", student.getPassword());
        intent.putExtra("phone", student.getPhone());
        intent.putExtra("name", student.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void SaveIdProofServer(final Student student, final Bitmap bitmap) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Save identity proof.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, SAVE_ID_PROOOF_URL,
                new Response.Listener<NetworkResponse>() {


                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            progressDialog.dismiss();
                            if (obj.getBoolean("success")) {
                                Toast.makeText(getApplicationContext(), obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                                if (bitmap_your_photo != null) {
                                    SaveImageServer(student, bitmap_your_photo);
                                } else {
                                    goToLogin(student);
                                }


                            } else {

                                Toast.makeText(getApplicationContext(), "Error File not correct", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = "";
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    JSONObject jsonObject = new JSONObject(responseBody);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }) {
            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //   params.put("Content-Type", "application/json; charset=utf-8");
                params.put(TAG_ID, student.getId());

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put(TAG_ID_PROOF, new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }


        };

        //adding the request to volley

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
