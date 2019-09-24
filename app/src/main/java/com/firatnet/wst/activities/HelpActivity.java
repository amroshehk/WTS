package com.firatnet.wst.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firatnet.wst.R;
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.database.SafetyDbHelper;
import com.firatnet.wst.entities.Phone;

import java.util.ArrayList;

public class HelpActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());

        SafetyDbHelper helper = new SafetyDbHelper(getApplicationContext());
        final ArrayList<Phone> phones = helper.getAllPhones();

        Button help_btn = findViewById(R.id.send);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String address="";
                String separator = "; ";


                if(android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")){
                    separator = ", ";
                }
                for (int i=0 ;i<phones.size();i++) {
                    //sendSMS(phone.getNumber(), preferenceHelper.getSettingValueMessage());
                    if(i==0)
                        address=phones.get(i).getNumber();
                    if(i>0)
                        address =address+separator+phones.get(i).getNumber();
                }
               // Now my below code is working properly for SAMSUNG devices.
                try {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("address", address);
                    sendIntent.putExtra("sms_body", preferenceHelper.getSettingValueMessage());
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }



    public void sendSMS(String phoneNo, String msg) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(getApplicationContext(), "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(),ex.getMessage(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
    }


}
