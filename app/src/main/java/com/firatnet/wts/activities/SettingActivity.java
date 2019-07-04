package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.firatnet.wts.adapter.RecyclerNumbersCardAdapter;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.database.SafetyDbHelper;
import com.firatnet.wts.entities.Phone;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.firatnet.wts.classes.JsonTAG.TAG_MESSAGE;
import static com.firatnet.wts.classes.JsonTAG.TAG_NUMBER;
import static com.firatnet.wts.classes.JsonTAG.TAG_STUDENT_ID;
import static com.firatnet.wts.classes.URLTAG.ADD_PHONE_URL;

public class SettingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
   public TextView nonumber;

    EditText phone_no_et,message_et;
    Button add_btn,save_btn;
    ArrayList<Phone> phones;
    Context context;
    PreferenceHelper helper;
    private ProgressDialog progressDialog;
//    public Dialog dialog2;
//    public Button cancel;
//    public Button ensure;
//    public TextView item_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView = findViewById(R.id.recyclerview);
        nonumber = findViewById(R.id.nonumber);
        phone_no_et = findViewById(R.id.phone_no_et);
        message_et = findViewById(R.id.message_et);
        add_btn = findViewById(R.id.add_btn);
        save_btn = findViewById(R.id.save_btn);
        context = this;
        layoutManager = new LinearLayoutManager(context);
        helper=new PreferenceHelper(context);


        final SafetyDbHelper dh = new SafetyDbHelper(context);
        phones = dh.getAllPhones();

//        dialog2 = new Dialog(context);
//        dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog2.setContentView(R.layout.delete_dialog_layout);
//        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//
//        cancel =  dialog2.findViewById(R.id.cancel);
//        ensure =  dialog2.findViewById(R.id.ensure);
//        item_name =  dialog2.findViewById(R.id.item_name);
//        item_name.setText("Delete All Notifications ?");
//
//
//        // if button is clicked, close the custom dialog
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog2.dismiss();
//            }
//        });
//
//
//        // if button is clicked, close the custom dialog
//        ensure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SafetyDbHelper dh = new SafetyDbHelper(context);
//                dh.de();
//                clear();
//                nonotify.setVisibility(View.VISIBLE);
//                Toast.makeText(context,"Delete all notification", Toast.LENGTH_SHORT).show();
//                dialog2.dismiss();
//            }
//        });
        recyclerView.setLayoutManager(layoutManager);

        if (phones.size() == 0) {
            nonumber.setVisibility(View.VISIBLE);
        } else {

            adapter = new RecyclerNumbersCardAdapter(phones, context, nonumber);
            recyclerView.setAdapter(adapter);
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_no_et.getText().toString();
                Phone phone=new Phone();
                if(num.isEmpty() || num.length() < 10)
                {
                    Toast.makeText(context,"Valid number is required",Toast.LENGTH_LONG).show();
                }
                else
                {
                    phone.setNumber(num);
                    dh.saveNumber(phone);

                    phones.add(phone);
                    nonumber.setVisibility(View.GONE);
                    adapter=new RecyclerNumbersCardAdapter(phones,context,nonumber);
                    recyclerView.setAdapter(adapter);
                    phone_no_et.setText("");

                    addPhoneNumber(num);

                }

            }
        });

        message_et.setText(helper.getSettingValueMessage());
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!message_et.getText().toString().equals(""))
                {
                    helper.saveMessage(message_et.getText().toString());
                    Toast.makeText(context,"Saving message",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context,"please enter message",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void addPhoneNumber(final String num) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("add new number");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, ADD_PHONE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    progressDialog.dismiss();
                    //Toast.makeText(getBaseContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                params.put(TAG_NUMBER, num);


                return params;

            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
//    public void clear() {
//        phones.clear();
//        adapter=new RecyclerNumbersCardAdapter(phones,context,nonumber);
//        recyclerView.setAdapter(adapter);
//    }
    }
}
