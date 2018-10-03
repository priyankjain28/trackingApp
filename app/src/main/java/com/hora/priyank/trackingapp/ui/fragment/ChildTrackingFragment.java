package com.hora.priyank.trackingapp.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.dao.MyFragmentListenerImpl;
import com.hora.priyank.trackingapp.data.model.Event;
import com.hora.priyank.trackingapp.data.model.User;
import com.hora.priyank.trackingapp.databinding.ChildTrackFragmentBinding;
import com.hora.priyank.trackingapp.ui.activity.TrackingActivity;
import com.hora.priyank.trackingapp.ui.adapter.TrackingChildrenAdapter;
import com.hora.priyank.trackingapp.ui.viewmodel.ChildTrackViewModel;
import com.hora.priyank.trackingapp.ui.viewmodel.EventListViewModel;
import com.hora.priyank.trackingapp.util.DateComparator;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Priyank Jain on 29-09-2018.
 */
public class ChildTrackingFragment extends Fragment implements OnMapReadyCallback {

    //region Variable declaration
    private static final String TAG = "ChildTrackingFragment";
    private ChildTrackFragmentBinding mBinding;
    private ChildTrackViewModel childTrackViewModel;
    private MyFragmentListenerImpl mFragmentCallback;
    private SupportMapFragment mapFragment;
    private EventListViewModel eventModel;
    private GoogleMap mMap;
    private TrackingChildrenAdapter mTrackingChildrenAdapter;
    private TrackingActivity trackingActivity;
    private List<LatLng> latLngs;
    private int counter = 0;
    private HashMap<String, User> userHashMap = new HashMap<String, User>();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    //endregion

    //region Fragment Lifecycle component
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrackingChildrenAdapter = new TrackingChildrenAdapter(ChildTrackingFragment.this);
        childTrackViewModel = ViewModelProviders.of(getActivity()).get(ChildTrackViewModel.class);
        eventModel = ViewModelProviders.of(getActivity()).get(EventListViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.child_track_fragment, container, false);
        intializingView();
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        trackingActivity = (TrackingActivity) context;
        try {
            mFragmentCallback = (MyFragmentListenerImpl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.recyclerviewChild.setAdapter(mTrackingChildrenAdapter);
        setupDataAndMapView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Peeru onResume");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Peeru onPause");
        super.onPause();
    }
    //endregion

    //region Map setup and refresh
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    private void refreshMap() {
        mMap.clear();
        mapFragment.onResume();
    }
    //endregion

    //region Initializing view on Fragment
    private void intializingView() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.recyclerviewChild.setLayoutManager(linearLayoutManager);
        mBinding.fabChild.setOnClickListener(v -> {
            mFragmentCallback.onFabButtonClicked();
        });
        mBinding.resetMapView.setOnClickListener(v -> {
            setupDataAndMapView();
        });
    }
    //endregion

    //region Set data on recycler view and map
    public void setupDataAndMapView() {
        // Update the list when the data changes
        if (childTrackViewModel != null) {
            LiveData<List<User>> liveData = childTrackViewModel.getAssociateUserList(trackingActivity.getUserName());
            liveData.observe(getActivity(), (List<User> user) -> {
                if (user != null && !user.isEmpty()) {
                    mBinding.childrenText.setText("You have "+user.size()+" children(s) ");
                    mBinding.resetMapView.setVisibility(View.VISIBLE);
                    mTrackingChildrenAdapter.setUserList(user, trackingActivity.getUserName());
                    counter = 0;
                    markChildrenOnMapView(user);
                } else {
                    refreshMap();
                    mBinding.childrenText.setText("No children's found");
                    mBinding.resetMapView.setVisibility(View.GONE);
                    user.clear();
                    mTrackingChildrenAdapter.setUserList(user, trackingActivity.getUserName());
                }
            });
        }
    }
    //endregion

    //region Extract user name from User List
    private Set<String> getAllUserNameFromList(List<User> user) {
        userHashMap = new HashMap<String, User>();
        for (User u : user) {
            userHashMap.put(u.getEmail(), u);
        }
        return userHashMap.keySet();
    }
    //endregion

    //region Mark children on map view
    public void markChildrenOnMapView(List<User> uList) {
        refreshMap();
        Set<String> userList = getAllUserNameFromList(uList);
        counter = 0;
        latLngs = new ArrayList<LatLng>();
        for (String email : userList) {
            displayDataOnMap(email, userList.size());
        }
    }

    public void displayDataOnMap(String email, Integer userListSize) {
        int color = new Random().nextInt(360);
        if (eventModel != null) {
            LiveData<List<Event>> liveData = eventModel.getEventListLiveData(email);
            liveData.observe(getActivity(), (List<Event> mEntities) -> {
                counter++;
                if (mEntities.size() != 0) {
                    Collections.sort(mEntities, new DateComparator());
                    for (Event e : mEntities) {
                        LatLng position = new LatLng(e.getLat(), e.getLon());
                        latLngs.add(position);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(userHashMap.get(e.getUserName()).getFirstName()+"("+e.getTitle() + ", " + e.getType()+")")
                                .snippet(Utility.dateTimeCoversion(e.getCreateDate()))
                                .icon(BitmapDescriptorFactory.defaultMarker(color))
                        );
                        marker.showInfoWindow();
                    }
                }
                if (counter == userListSize) cameraUpdateCall(latLngs);
            });
        }
    }
    //endregion

    // region Delete Record with Alert message
    public void deleteRecord(String parentEmail, String childEmail, int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setMessage("Are you sure you want to delete?");
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#56C98F'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        childTrackViewModel.deleteRecord(parentEmail, childEmail);
                        mTrackingChildrenAdapter.notifyDataSetChanged();
                        //setupDataAndMapView();
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#56C98F'>No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //endregion

    //region Camera Bound with Area
    public void cameraUpdateCall(List<LatLng> latLngs) {
        if (latLngs.size() > 0) {
            builder = new LatLngBounds.Builder();
            for (LatLng position : latLngs)
                builder.include(position);
            if (areBoundsTooSmall(builder.build(), 100)) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(), 14));
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 5));
            }
        }
    }

    private boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0] < minDistanceInMeter;
    }

    //endregion
}
