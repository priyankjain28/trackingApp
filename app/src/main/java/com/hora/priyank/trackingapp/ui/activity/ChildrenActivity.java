package com.hora.priyank.trackingapp.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.dao.MyFragmentListenerImpl;
import com.hora.priyank.trackingapp.ui.fragment.NewPostFragment;
import com.hora.priyank.trackingapp.util.Utility;

public class ChildrenActivity extends DefaultActivity implements MyFragmentListenerImpl {

    private final NewPostFragment mComposerFragment = new NewPostFragment();
    private String username;
    private String TAG="ChildrenActivity";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        username = getIntent().getStringExtra(Utility.KEY_USER_NAME);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Events Track");
    }

    @Override
    public void onFabButtonClicked() {
        initializeBundleVariable();
        newEvent();
        checkLocationPermission();
    }

    private void initializeBundleVariable() {
        bundle = new Bundle();
        bundle.putString(Utility.KEY_USER_NAME, username);
        bundle.putString(Utility.KEY_NAME,getName());
    }

    private void newEvent(){
        bundle.putString(Utility.KEY_TRANSACTION,"new");
        bundle.putString(Utility.KEY_TRANSACTION_TIME,String.valueOf(System.currentTimeMillis()));
    }

    public String getUserName(){
        return username;
    };
    public String getName(){
        return getIntent().getStringExtra(Utility.KEY_NAME);
    };


    //region Call New Post with Permission Check
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            mComposerFragment.setArguments(bundle);
            callNewPostFragment();
        }
    }

    public void callNewPostFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mComposerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNewPostFragment();
                } else {
                    checkLocationPermission();
                }
                return;
            }
        }
    }
    //endregion

    public void updateEvent(String createDate) {
        initializeBundleVariable();
        bundle.putString(Utility.KEY_TRANSACTION,"update");
        bundle.putString(Utility.KEY_TRANSACTION_TIME,createDate);
        mComposerFragment.setArguments(bundle);
        callNewPostFragment();
    }
}
