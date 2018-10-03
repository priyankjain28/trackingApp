package com.hora.priyank.trackingapp.data;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private final ValueEventListener valueListener = new mValueEventListener();

    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(valueListener);
            listenerRemovePending = false;
        }
    };

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference dbReference){
        this.query = dbReference;
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            query.addValueEventListener(valueListener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        // Listener removal is schedule on a two second delay

        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }


    private class mValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG,  "Cannot listen to query " + query, databaseError.toException());
        }
    }


}
