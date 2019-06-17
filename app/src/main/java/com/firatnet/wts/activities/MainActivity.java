package com.firatnet.wts.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.firatnet.wts.R;
import com.firatnet.wts.classes.PreferenceHelper;
import com.firatnet.wts.entities.Student;
import com.firatnet.wts.phoneauth.PhoneNumberAuthActivity;
import com.firatnet.wts.phoneauth.VerifyPhoneActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public LinearLayout help_line1, buysell_line2, admin_line3;
    public PreferenceHelper helper;
    private CircleImageView photo_nav_header;
    private Context context;
    ImageLoaderConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            String email1 = getIntent().getStringExtra("email");
            String password1 = getIntent().getStringExtra("password");

            Student student = new Student();
            student.setEmail(email1);
            student.setPassword(password1);

            helper.setLoginState(true);
            helper.saveUser(student);

        } catch (Exception ignored) {

        }

        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        help_line1 = findViewById(R.id.help_line1);
        buysell_line2 = findViewById(R.id.buysell_line2);
        admin_line3 = findViewById(R.id.admin_line3);
        setSupportActionBar(toolbar);
        helper = new PreferenceHelper(getApplicationContext());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        context=this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String name = helper.getSettingValueName();
        String email= helper.getSettingValueEmail();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        photo_nav_header= hView.findViewById(R.id.photo);
        TextView name_tv = hView.findViewById(R.id.name_tv);
        TextView email_tv = hView.findViewById(R.id.email_tv);
        name_tv.setText(name);
        email_tv.setText(email);
        getUserPhoto();

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




    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserPhoto();
//        businesses.clear();
//        getRecentBusiness();
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
            imageLoader.displayImage(helper.getSettingValuePhotoUrl(), photo_nav_header, options);
        else
            photo_nav_header.setBackgroundResource(R.drawable.user_default);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(MainActivity.this, FeedBackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_mypost) {
            Intent intent = new Intent(MainActivity.this, MyPostsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        } else if (id == R.id.nav_send) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            }
            catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        else if (id == R.id.nav_logout) {
            helper.setLoginState(false);
            helper.deleteUser();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
