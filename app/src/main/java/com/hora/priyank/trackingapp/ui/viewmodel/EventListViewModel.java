package com.hora.priyank.trackingapp.ui.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hora.priyank.trackingapp.data.FirebaseQueryLiveData;
import com.hora.priyank.trackingapp.data.model.Event;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EventListViewModel extends ViewModel {
    private DatabaseReference dataRef;
    private List<Event> mList = new ArrayList<>();
    private Set<String> etList = new HashSet<>();

    //region Event List Live Data
    @NonNull
    public LiveData<List<Event>> getEventListLiveData(String user) {
        user = String.valueOf(user).replace(".", "*");
        String childRef = Utility.CHILDTRACKING_EVENT + user;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(dataRef);

        LiveData<List<Event>> mEventLiveData =
                Transformations.map(mLiveData, new Deserializer());

        return mEventLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Event>> {
        @Override
        public List<Event> apply(DataSnapshot dataSnapshot) {
            mList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Event msg = snap.getValue(Event.class);
                mList.add(msg);
            }
            return mList;
        }
    }
    //endregion


    //region Event type List
    @NonNull
    public LiveData<Set<String>> getEventTypeList() {
        String childRef = Utility.CHILDTRACKING_EVENT_TYPE;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(dataRef);

        LiveData<Set<String>> mEventTypeList =
                Transformations.map(mLiveData, new DeserializerEventType());

        return mEventTypeList;
    }

    private class DeserializerEventType implements Function<DataSnapshot, Set<String>> {
        @Override
        public Set<String> apply(DataSnapshot dataSnapshot) {
            etList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                String msg = snap.getValue(String.class);
                etList.add(msg);
            }
            return etList;
        }

    }
    //endregion

/*
    private final MutableLiveData<Boolean> messageUploadIsSuccessful = new MutableLiveData<>();

    public MutableLiveData<Boolean> getMessageUploadIsSuccessful() {
        return messageUploadIsSuccessful;
    }
*/

    //region Create new event
    public void createAndSendToDataBase(String userName, String title, String type, Double lat, Double lon, String dateTime) {
        Event entity = new Event(userName, title, type, lat, lon, dateTime);
        final String user = String.valueOf(userName).replace(".", "*");

        // push the new message to Firebase
        dataRef = FirebaseDatabase.getInstance().getReference().child(Utility.CHILDTRACKING_EVENT + user + "/" + dateTime);
        dataRef.setValue(entity);

        dataRef = FirebaseDatabase.getInstance().getReference().child(Utility.CHILDTRACKING_USER + user + "/lastUpdate");
        dataRef.setValue(dateTime);
    }
    //endregion

    //region Update Event
    public void updateToDatabase(String userName, String title, String type, String dateTime) {
        final String user = String.valueOf(userName).replace(".", "*");
        // push the new message to Firebase
        dataRef = FirebaseDatabase.getInstance().getReference().child(Utility.CHILDTRACKING_EVENT + user + "/" + dateTime);
        dataRef.child("title").setValue(title);
        dataRef.child("type").setValue(type);
    }
    //endregion

    //region Create Event type
    public void createTypeAndSentToDataBase(String type) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Utility.CHILDTRACKING_EVENT_TYPE)
                .push()
                .setValue(type);
    }
    //endregion

}


