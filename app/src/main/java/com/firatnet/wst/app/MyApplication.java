package com.firatnet.wst.app;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Context context=getBaseContext();
       FirebaseApp.initializeApp(context);
    }
}
