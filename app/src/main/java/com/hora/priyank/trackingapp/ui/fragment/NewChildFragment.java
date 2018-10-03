package com.hora.priyank.trackingapp.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hora.priyank.trackingapp.R;
import com.hora.priyank.trackingapp.data.model.User;
import com.hora.priyank.trackingapp.databinding.NewChildFragmentBinding;
import com.hora.priyank.trackingapp.ui.viewmodel.ChildTrackViewModel;
import com.hora.priyank.trackingapp.util.Utility;

/**
 * Created by Priyank Jain on 29-09-2018.
 */
public class NewChildFragment extends Fragment {
    //region Variable Declaration
    private NewChildFragmentBinding mBinding;
    private ChildTrackViewModel mViewModel;
    private String mUserName;
    private String TAG = "NewChildFragment";
    //endregion

    //region Fragment Lifecycle Componenet
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "Hora onCreateView");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.new_child_fragment, container, false);
        initializeLayout();
        setBindingVariableEvent();
        setButtonClickListner();
        return mBinding.getRoot();
    }
    //endregion

    //region Intialize layout
    private void initializeLayout() {
        mUserName = getArguments().getString(Utility.KEY_USER_NAME);
        mViewModel = ViewModelProviders.of(getActivity()).get(ChildTrackViewModel.class);
    }
    //endregion

    //region Set variable and event handle
    private void setBindingVariableEvent() {
        mBinding.welcomeNote.append(getArguments().getString(Utility.KEY_NAME));
        mBinding.childEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mBinding.search.setEnabled(true);
                } else {
                    mBinding.search.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    //endregion

    //region Submit new post
    private void setButtonClickListner() {
        mBinding.search.setOnClickListener(view -> {
            String childUser = mBinding.childEmail.getText().toString();
            if (mViewModel != null) {
                LiveData<User> liveData = mViewModel.getUserLiveData(childUser);
                liveData.observe(getActivity(), (User mEntities) -> {
                    if (mEntities != null && mEntities.getEmail() != null) {
                        if (mEntities.getUserRole().equals(Utility.CHILD_CONSTANT)) {
                            mViewModel.createChildParentRelation(mUserName, mEntities);
                            mBinding.childEmail.setText("");
                            dismissKeyboard();
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), Utility.USER_ROLE_MISMATCH, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), Utility.CHILD_NOT_FOUND, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    //endregion

    //region Dismiss keyboard
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
    //endregion
}
