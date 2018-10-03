package com.hora.priyank.trackingapp.ui.activity;

import android.os.Bundle;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.dao.MyFragmentListenerImpl;
import com.hora.priyank.trackingapp.ui.fragment.NewChildFragment;
import com.hora.priyank.trackingapp.util.Utility;


public class TrackingActivity extends DefaultActivity implements MyFragmentListenerImpl {

    private final NewChildFragment mComposerFragment = new NewChildFragment();

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        username = getIntent().getStringExtra(Utility.KEY_USER_NAME);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Child Track");
    }

    @Override
    public void onFabButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString(Utility.KEY_USER_NAME, username);
        bundle.putString(Utility.KEY_NAME,getName());
        mComposerFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_tracking, mComposerFragment)
                .addToBackStack(null)
                .commit();
    }

    public String getUserName() {
        return username;
    }

    public String getName(){
        return getIntent().getStringExtra(Utility.KEY_NAME);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

}
