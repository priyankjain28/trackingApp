package com.hora.priyank.trackingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.databinding.NewPostFragmentBinding;
import com.hora.priyank.trackingapp.ui.viewmodel.EventListViewModel;
import com.hora.priyank.trackingapp.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.LOCATION_SERVICE;


public class NewPostFragment extends Fragment {
    //region Varaible Declaration
    private static final String SELECT_MESSAGE = "Please select category type";
    private static final String OTHERS_VALUE = "Others";
    private NewPostFragmentBinding mBinding;
    private EventListViewModel mViewModel;
    private String mUserName;
    private LocationManager locationManager;
    private Location location;
    private ArrayAdapter<String> dataAdapter;
    private List<String> eventType = new ArrayList<String>();
    private String title, type;
    private String TAG = "NewPostFragment";
    private String transaction, createTime;
    //endregion

    //region Fragment Lifecycle component
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.new_post_fragment, container, false);
        setInstruction();
        return mBinding.getRoot();
    }
    //endregion

    //region Set instruction
    private void setInstruction() {
        initializeLayout();
        setBindingVariableEvent();
        setAndLoadDataOnDataAdapter();
        setButtonClickListner();
    }
    //endregion

    //region Intialize Layout
    @SuppressLint("MissingPermission")
    private void initializeLayout() {
        eventType = new ArrayList<String>();
        mUserName = getArguments().getString(Utility.KEY_USER_NAME);
        transaction = getArguments().getString(Utility.KEY_TRANSACTION);
        createTime = getArguments().getString(Utility.KEY_TRANSACTION_TIME);
        mViewModel = ViewModelProviders.of(getActivity()).get(EventListViewModel.class);
        if (transaction.equals("new")) location = getLastKnownLocation();
    }
    //endregion

    //region Set variable and event handling
    private void setBindingVariableEvent() {
        mBinding.welcome.append("" + getArguments().getString(Utility.KEY_NAME));
        if (transaction.equals("update")) {
            mBinding.textStatic.setText(Utility.EVENT_UPDATE_NOTE);
            mBinding.sendButton.setText("Update");
        } else {
            mBinding.textStatic.setText(Utility.EVENT_ADD_NOTE);
            mBinding.sendButton.setText("Submit");
        }
        textEventListner(mBinding.titleEvent);
        textEventListner(mBinding.typeEvent);
        spinnerEventListner(mBinding.eventSpinner);
    }
    //endregion

    //region Track Location
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    //endregion

    //region Set LiveData on Adapter
    private void setAndLoadDataOnDataAdapter() {
        if (mViewModel != null) {
            LiveData<Set<String>> liveData = mViewModel.getEventTypeList();

            liveData.observe(getActivity(), (Set<String> mEntities) -> {
                eventType.addAll(mEntities);
                eventType.add(OTHERS_VALUE);
                //eventType.notify();
            });
        }
        eventType.add(SELECT_MESSAGE);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, eventType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }
    //endregion

    //region Send data on Firebase
    private void setButtonClickListner() {
        mBinding.eventSpinner.setAdapter(dataAdapter);
        mBinding.sendButton.setOnClickListener(view -> {
            if (type.equals(OTHERS_VALUE)) {
                type = mBinding.typeEvent.getText().toString();
                mViewModel.createTypeAndSentToDataBase(type);
            }
            title = mBinding.titleEvent.getText().toString();
            if (!type.equals(SELECT_MESSAGE)) {
                storeAndUpdateDataOnDatabase();
            } else {
                Toast.makeText(getContext(), SELECT_MESSAGE + " or add category (Others)", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void storeAndUpdateDataOnDatabase() {
        if (transaction.equals("new")) {
            mViewModel.createAndSendToDataBase(
                    mUserName,
                    title,
                    type,
                    location.getLatitude(),
                    location.getLongitude(),
                    String.valueOf(System.currentTimeMillis()));
            eventStoredBackToFragment();
        } else if (transaction.equals("update")) {
            mViewModel.updateToDatabase(
                    mUserName,
                    title,
                    type,
                    createTime
            );
            eventStoredBackToFragment();
        }
    }

    private void eventStoredBackToFragment() {
        mBinding.typeEvent.setText("");
        mBinding.titleEvent.setText("");
        dismissKeyboard();
        getActivity().onBackPressed();
    }
    //endregion

    //region Spinner Listner
    private void spinnerEventListner(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getSelectedItem().toString();
                if (type.equals(OTHERS_VALUE)) {
                    mBinding.typeEvent.setVisibility(View.VISIBLE);
                    mBinding.sendButton.setEnabled(false);
                } else if (type.equals(SELECT_MESSAGE) || mBinding.titleEvent.getText().toString().trim().length() == 0) {
                    mBinding.sendButton.setEnabled(false);
                } else {
                    mBinding.typeEvent.setText("");
                    mBinding.sendButton.setEnabled(true);
                    mBinding.typeEvent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //endregion

    //region Edittext Listner
    private void textEventListner(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mBinding.sendButton.setEnabled(true);
                } else {
                    mBinding.sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    //endregion

    //region DismissKeyboard
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
    //endregion
}
