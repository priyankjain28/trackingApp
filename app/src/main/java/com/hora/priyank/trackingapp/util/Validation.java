package com.hora.priyank.trackingapp.util;

import android.text.TextUtils;

/**
 * Created by Priyank Jain on 30-09-2018.
 */
public class Validation {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
