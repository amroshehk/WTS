package com.firatnet.wst.activities;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import com.firatnet.wst.R;
import com.firatnet.wst.classes.PreferenceHelper;
import com.firatnet.wst.entities.Student;


import android.view.View;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class MainActivity extends BaseActivity {


    public LinearLayout help_line1, buysell_line2, admin_line3,news_line4;
    public PreferenceHelper helper;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
//        //setSupportActionBar(toolbar);
//        helper = new PreferenceHelper(context);
//        try {
//            String email1 = getIntent().getStringExtra("email");
//            String password1 = getIntent().getStringExtra("password");
//            String phone = getIntent().getStringExtra("phone");
//            String name = getIntent().getStringExtra("name");
//
//            Student student = new Student();
//            student.setEmail(email1);
//            student.setPassword(password1);
//            student.setPhone(phone);
//            student.setName(name);
//
//            helper.setLoginState(true);
//            helper.saveUser(student);
//
//        } catch (Exception ignored) {
//
//        }


      // Toolbar toolbar = findViewById(R.id.toolbar);
        help_line1 = findViewById(R.id.help_line1);
        buysell_line2 = findViewById(R.id.buysell_line2);
        admin_line3 = findViewById(R.id.admin_line3);
        news_line4 = findViewById(R.id.news_line4);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        context=this;

        help_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        buysell_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyOrSellActivity.class);
                startActivity(intent);
            }
        });
        admin_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminCornerActivity.class);
                startActivity(intent);
            }
        });
        news_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        businesses.clear();
//        getRecentBusiness();
    }




    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
