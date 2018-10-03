package com.hora.priyank.trackingapp.init;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


public class TrackingApp extends Application {
    private static Context APP_CONTEXT=null;
    @Override
    public void onCreate() {
        super.onCreate();
        TrackingApp.APP_CONTEXT = getApplicationContext();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
    public static Context getAppContext() {
        return APP_CONTEXT;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
