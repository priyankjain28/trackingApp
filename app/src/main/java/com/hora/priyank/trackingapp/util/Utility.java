package com.hora.priyank.trackingapp.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Priyank Jain on 29-09-2018.
 */
public class Utility {
    public static final String CHILD_CONSTANT = "Child";
    public static final String PARENT_CONSTANT = "Parent";
    public static final String INVALID_PASSWORD = "Please enter right password ";
    public static final String USER_NOT_FOUND = "User not exist";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_NAME = "name";
    public static final String TRACKING_APP = "Track";
    public static final String USER_ROLE_MANDATORY = "Please select user type its mandatory";
    public static final String CHILD_NOT_FOUND = "Child not found";
    public static final String USER_ROLE_MISMATCH = "Given user role is not Child" ;
    public static final String CHILD_EXIST = "Children already exist";
    public static final String CHILD_ADDED = "Child added!!";
    public static final String KEY_TRANSACTION = "transaction";
    public static final String CHILDTRACKING_EVENT_TYPE = "ChildTracking/EventType/" ;
    public static final String CHILDTRACKING_EVENT = "ChildTracking/Event/" ;
    public static final String EVENT_ADD_NOTE = "\n"+"You are doing something..\n Please mark here to make it special";
    public static final String KEY_TRANSACTION_TIME = "time";
    public static final String EVENT_UPDATE_NOTE = "Update your existing event data..\n ";
    public static String CHILDTRACKING_USER = "ChildTracking/User/";
    public static String CHILDTRACKING_RELATION = "ChildTracking/Relation/";


    public static String dotToStarConverter(String s){
        return String.valueOf(s).replace(".", "*");
    }
    public static String dateTimeCoversion(String createDate) {
        try {
            long yourmilliseconds = Long.valueOf(createDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(yourmilliseconds);
            return sdf.format(resultdate).toString();

        } catch (Exception e) {
            return "";
        }
    }

    public static void textEventListner(EditText editText, Button button) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
   }
}
