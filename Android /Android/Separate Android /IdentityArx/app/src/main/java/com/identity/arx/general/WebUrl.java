package com.identity.arx.general;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WebUrl {
    public static final String GET_PROFILE = "http://182.73.181.173/IdentityRestAPI/user/getProfile";
    public static final String NEXT_TWO_LECTURE_DETAILS = "http://182.73.181.173/IdentityRestAPI/user/nextTwoLectDetails";
    public static final String SELECT_INSTITUTE = "http://182.73.181.173/IdentityRestAPI/user/iRecord";
    public static final String SERVER_ADDRESS = "http://182.73.181.173/IdentityRestAPI/user/";
    public static final String lOGIN = "http://182.73.181.173/IdentityRestAPI/user/main_db1/loginCheck";
    public SharedPreferences sharedPreference;
    public Editor sharedPreferenceEdit;
}
