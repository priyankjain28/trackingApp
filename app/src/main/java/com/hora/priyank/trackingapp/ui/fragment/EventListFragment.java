package com.hora.priyank.trackingapp.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.dao.MyFragmentListenerImpl;
import com.hora.priyank.trackingapp.data.model.Event;
import com.hora.priyank.trackingapp.databinding.MessageListFragmentBinding;
import com.hora.priyank.trackingapp.ui.activity.ChildrenActivity;
import com.hora.priyank.trackingapp.ui.adapter.EventAdapter;
import com.hora.priyank.trackingapp.ui.viewmodel.EventListViewModel;
import com.hora.priyank.trackingapp.util.DateComparator;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.Collections;
import java.util.List;


public class EventListFragment extends Fragment {

    //region Variable Declaration
    private static final String TAG = "EventListFragment";
    private MessageListFragmentBinding mBinding;
    private EventListViewModel mModel;
    private MyFragmentListenerImpl mFragmentCallback;
    private EventAdapter mEventAdapter;
    private ChildrenActivity childrenActivity;
    //endregion

    //region Fragment Lifecycle Component
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventAdapter = new EventAdapter(getContext(),getActivity());
        mModel = ViewModelProviders.of(getActivity()).get(EventListViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.message_list_fragment,
                container, false);
        intializingView();
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        childrenActivity = (ChildrenActivity) context;
        Log.d(TAG, "Peeru on attach");
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
        mBinding.recyclerview.setAdapter(mEventAdapter);
        displayEventLiveData();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    //endregion

    //region Intializing view
    private void intializingView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(layoutManager);
        mBinding.fab.setOnClickListener(v -> {
            mFragmentCallback.onFabButtonClicked();
        });
    }
    //endregion

    //region Display Event List Data
    private void displayEventLiveData() {
        if (mModel != null) {
            LiveData<List<Event>> liveData = mModel.getEventListLiveData(childrenActivity.getUserName());
            liveData.observe(getActivity(), (List<Event> mEntities) -> {
                Collections.sort(mEntities, new DateComparator());
                if (mEntities.size() == 0 || mEntities.isEmpty()) {
                    mBinding.noEntTag.setVisibility(View.VISIBLE);
                    mBinding.noEntTag.setText("Hi " + childrenActivity.getName() + Utility.EVENT_ADD_NOTE);
                } else {
                    mBinding.noEntTag.setVisibility(View.GONE);
                    mEventAdapter.setMessageList(mEntities);
                }
            });
        }
    }
    //endregion
}
