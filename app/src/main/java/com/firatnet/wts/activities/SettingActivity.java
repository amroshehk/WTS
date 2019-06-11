package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firatnet.wts.R;
import com.firatnet.wts.adapter.RecyclerNumbersCardAdapter;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.database.SafetyDbHelper;
import com.firatnet.wts.entities.Phone;

import java.util.ArrayList;

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

//    public void clear() {
//        phones.clear();
//        adapter=new RecyclerNumbersCardAdapter(phones,context,nonumber);
//        recyclerView.setAdapter(adapter);
//    }

}
