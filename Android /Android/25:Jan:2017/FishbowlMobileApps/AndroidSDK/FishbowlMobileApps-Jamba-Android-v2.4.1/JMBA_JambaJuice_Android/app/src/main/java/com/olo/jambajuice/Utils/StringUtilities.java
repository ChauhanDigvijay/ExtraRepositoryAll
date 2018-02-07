package com.olo.jambajuice.Utils;

import android.text.TextUtils;

/**
 * *
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class StringUtilities {

    public static boolean isValidString(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isValidDoubleToString(String string) {
        if (string == null || string.equals("") || string.equals("0") || string.equals(" ") || string.equals("0.0") || string.equals("US$0.0")) {
            return false;
        }
        return true;
    }

    public static boolean isValidIntergerToString(String string) {

        if (string == null || string.equals("") || string.equals("0.0")) {
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static final boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


}
