package com.hora.priyank.trackingapp.ui.viewmodel;

import android.app.Activity;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hora.priyank.trackingapp.data.FirebaseQueryLiveData;
import com.hora.priyank.trackingapp.data.model.User;
import com.hora.priyank.trackingapp.init.TrackingApp;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyank Jain on 28-09-2018.
 */
public class ChildTrackViewModel extends ViewModel {
    private DatabaseReference dataRef;
    private User user = new User();
    private List<User> uList = new ArrayList<User>();
    FirebaseQueryLiveData mLiveData;
    private Boolean flag = false;

    //region User List Live data
    @NonNull
    public LiveData<User> getUserLiveData(String userName) {
        String childRef = Utility.CHILDTRACKING_USER;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        mLiveData = new FirebaseQueryLiveData(dataRef.orderByChild("email").equalTo(userName));
        LiveData<User> mUserData = Transformations.map(mLiveData, new DeserializerUser());
        return mUserData;
    }
    //endregion

    //region Parent Child relation list
    public LiveData<List<User>> getAssociateUserList(String userName) {
        userName = String.valueOf(userName).replace(".", "*");
        String childRef = Utility.CHILDTRACKING_RELATION + userName;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        mLiveData = new FirebaseQueryLiveData(dataRef);

        LiveData<List<User>> mEventLiveData =
                Transformations.map(mLiveData, new DeserializerUserList());

        return mEventLiveData;
    }
    //endregion

    //region Deserializer to fetch records
    private class DeserializerUser implements Function<DataSnapshot, User> {
        @Override
        public User apply(DataSnapshot dataSnapshot) {
            user = new User();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                user = snap.getValue(User.class);
            }
            return user;
        }
    }

    private class DeserializerUserList implements Function<DataSnapshot, List<User>> {

        @Override
        public List<User> apply(DataSnapshot dataSnapshot) {
            uList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
                User user = snap.getValue(User.class);
                uList.add(user);
            }
            return uList;
        }
    }
    //endregion
    /*
    private final MutableLiveData<Boolean> messageUploadIsSuccessful = new MutableLiveData<>();

    public MutableLiveData<Boolean> getMessageUploadIsSuccessful() {
        return messageUploadIsSuccessful;
    }*/

    //region Delete Relation
    public void deleteRecord(String parent, String children) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query userQuery = ref.child(Utility.CHILDTRACKING_RELATION + Utility.dotToStarConverter(parent)).orderByChild("email").equalTo(children);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userRecord : dataSnapshot.getChildren()) {
                    userRecord.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    //endregion

    //region Create Parent Child Relation
    public void createChildParentRelation(String userName, User mEntities) {
        // push the new Child to Firebase
        userName = Utility.dotToStarConverter(userName);
        dataRef = FirebaseDatabase.getInstance().getReference().child(Utility.CHILDTRACKING_RELATION + userName);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //If email exists then toast shows else store the data on new key
                    if (data.getValue(User.class).getEmail().equals(user.getEmail())) {
                        exist = true;
                        Toast.makeText(TrackingApp.getAppContext(), Utility.CHILD_EXIST, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!exist) {
                    mEntities.setPassword("*********");
                    dataRef.child(Utility.dotToStarConverter(mEntities.getEmail())).setValue(mEntities);
                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
            }
        });
    }
    //endregion

    //region Create New User
    public void creatNewUser(final User user, Activity activity) {
        flag = false;
        dataRef = FirebaseDatabase.getInstance().getReference().child(Utility.CHILDTRACKING_USER);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //If email exists then toast shows else store the data on new key
                    if (data.getValue(User.class).getEmail().equals(user.getEmail())) {
                        exist = true;
                        Toast.makeText(TrackingApp.getAppContext(), "E-mail already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!exist) {
                    dataRef.child(Utility.dotToStarConverter(user.getEmail())).setValue(user);
                    activity.onBackPressed();
                }
                flag = !exist;
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
            }
        });
    }
    //endregion
}


