package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firatnet.wts.R;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.database.SafetyDbHelper;
import com.firatnet.wts.entities.Phone;

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
                for (Phone phone: phones) {
                    sendSMS(phone.getNumber(), preferenceHelper.getSettingValueMessage());
                }
            }
        });

    }



    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


}
