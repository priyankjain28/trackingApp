package com.hora.priyank.trackingapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.init.UserSession;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private UserSession session;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        // User Session Manager
        session = new UserSession(getApplicationContext());
        sharedPreferences = getSharedPreferences(Utility.TRACKING_APP, Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                if(!session.checkLogin()){
                    HashMap<String, String> userDetail = session.getUserDetails();
                    if(userDetail.get(UserSession.KEY_USER_ROLE).equals(Utility.CHILD_CONSTANT))
                        i=new Intent(SplashScreen.this,ChildrenActivity.class);
                    else
                        i=new Intent(SplashScreen.this,TrackingActivity.class);

                    i.putExtra(Utility.KEY_USER_NAME, userDetail.get(UserSession.KEY_EMAIL));
                    i.putExtra(Utility.KEY_NAME, userDetail.get(UserSession.KEY_NAME));
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

