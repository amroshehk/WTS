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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.firatnet.wst.BuildConfig;
import com.firatnet.wst.R;
import com.firatnet.wst.classes.GalleryUtil;
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.classes.ProcessProfilePhotoTask;
import com.firatnet.wst.classes.StaticMethod;
import com.firatnet.wst.classes.VolleyMultipartRequest;
import com.firatnet.wst.entities.Category;
import com.firatnet.wst.entities.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firatnet.wst.classes.JsonTAG.TAG_CATEGORY;
import static com.firatnet.wst.classes.JsonTAG.TAG_CONTACT_NO;
import static com.firatnet.wst.classes.JsonTAG.TAG_DESCRIPTION;
import static com.firatnet.wst.classes.JsonTAG.TAG_ID;
import static com.firatnet.wst.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wst.classes.JsonTAG.TAG_POST_IMAGE_URL;
import static com.firatnet.wst.classes.JsonTAG.TAG_PRICE;
import static com.firatnet.wst.classes.JsonTAG.TAG_TITLE;
import static com.firatnet.wst.classes.URLTAG.EDIT_POSt_URL;

public class EditSellPostActivity extends AppCompatActivity {

    EditText title_et, description_et,contact_no_et,price_et;
    TextView error2;
    Button edit_btn;
    private ProgressDialog progressDialog;
    public PreferenceHelper helper;
    Context context;
    private Spinner category_sp;
    boolean checkfirst = true;
    ArrayList<Category> categories ;
    ArrayList<String> categories_list ;
    ArrayAdapter<String> spiner_adapter;
    private Bitmap bitmap;

    ImageView pic;
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;
    private static final String TEMP_PHOTO_FILE = "tempPhoto.jpg";
    Post edit_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sell_post);
        Intent intent=getIntent();
        edit_post=intent.getParcelableExtra("EDIT");
        title_et = findViewById(R.id.title_et);
        contact_no_et = findViewById(R.id.contact_no_et);
        price_et = findViewById(R.id.price_et);
        description_et = findViewById(R.id.description_et);
        category_sp = findViewById(R.id.category_sp);
        pic = findViewById(R.id.image);

        edit_btn = findViewById(R.id.edit_btn);
        error2 = findViewById(R.id.error2);
        helper = new PreferenceHelper(getApplicationContext());
        context=this;

        categories=new ArrayList<>();
        categories_list=new ArrayList<>();
        categories=intent.getParcelableArrayListExtra("CATEGORIES");
//        categories_list.add("All");
        int position = 0;
        for (int i=0;i<categories.size();i++)
        {
            categories_list.add(categories.get(i).getCategory());
            if (categories_list.get(i).equals(edit_post.getCategory()))
            position=i;
        }

        spiner_adapter =new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categories_list);
        category_sp.setAdapter(spiner_adapter);
        category_sp.setSelection(position);

        ImageLoaderConfiguration config;
        ImageLoader imageLoader = ImageLoader.getInstance();
        config= new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        if (!edit_post.getPost_image_url().isEmpty())
            imageLoader.displayImage(edit_post.getPost_image_url(), pic, options);
        else
            pic.setBackgroundResource(R.drawable.no_image);




        price_et.setText(edit_post.getPrice());
        contact_no_et.setText(edit_post.getContact_no());
        title_et.setText(edit_post.getTitle());
        description_et.setText(edit_post.getDescription());
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_= Objects.requireNonNull(title_et.getText()).toString();
                String description_= Objects.requireNonNull(description_et.getText()).toString();
                String category_= Objects.requireNonNull(category_sp.getSelectedItem()).toString();
                String price_= Objects.requireNonNull(price_et.getText()).toString();
                String contact_no_= Objects.requireNonNull(contact_no_et.getText()).toString();
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
                        edit_post.setTitle(title_);
                        edit_post.setDescription(description_);
                        edit_post.setCategory(category_);
                        edit_post.setPrice(price_);
                        edit_post.setContact_no(contact_no_);
                       EditPostServer(edit_post);


                    } else {

                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditSellPostActivity.this,
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
                    Toast.makeText(EditSellPostActivity.this, "Permission denied to Write on your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//
//            //getting the image Uri
//            Uri imageUri = data.getData();
//            try {
//                //getting bitmap object from uri
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//
//                //displaying selected image to imageview
//
//                filePath = getPath(imageUri);
//                ChangeImageServer(student_form_db.getId(),bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String    picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                performCrop(picturePath);
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
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
                contentUri = FileProvider.getUriForFile(EditSellPostActivity.this,
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

            File f = new File(Environment.getExternalStorageDirectory()+"/Businessapp/" ,TEMP_PHOTO_FILE);
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

    private boolean isSDCARDMounted(){
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }
    /*
     *  processes a temp photo file from
     */
    private void processPhotoUpdate(File tempFile) {
        @SuppressLint("StaticFieldLeak") ProcessProfilePhotoTask task = new ProcessProfilePhotoTask(){

            @Override
            protected void onPostExecute(Bitmap result) {
                bitmap=result;
                pic.setImageBitmap(bitmap);
            }

        };
        task.execute(tempFile);

    }
    private void imageBrowse() {
        //if everything is ok we will open image chooser
        //  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  startActivityForResult(i, 100);
        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }
    private void EditPostServer(final Post post) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, EDIT_POSt_URL, new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    JSONObject obj = new JSONObject(new String(response.data));
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
                params.put(TAG_CATEGORY, post.getCategory());
                params.put(TAG_PRICE, post.getPrice());
                params.put(TAG_CONTACT_NO, post.getContact_no());
                return params;

            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                if(bitmap!=null)
                params.put(TAG_POST_IMAGE_URL, new VolleyMultipartRequest.DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));

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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
