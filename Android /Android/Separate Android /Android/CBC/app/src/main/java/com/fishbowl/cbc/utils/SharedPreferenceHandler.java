package com.fishbowl.cbc.utils;

import android.content.SharedPreferences;

import com.fishbowl.cbc.CbcApplication;

/**
 * Created by VT027 on 5/22/2017.
 */

public class SharedPreferenceHandler {
    public static String LastProductUpdate = "LastProductUpdate";
    public static String LastSearchedLocation = "LastSearchedLocation";
    public static String IsFirstSplash = "IsFirstSplash";
    public static String FirstLaunch = "FirstLaunch";
    public static String FirstPurchase = "FirstPurchase";
    public static String LastSelectedStore = "LastSelectedStore";

    static String UserDefaults = "UserDefaults";
    static SharedPreferences preferences;

    public static SharedPreferences getInstance() {
        if (preferences == null) {
            preferences = CbcApplication.getInstance().getSharedPreferences(UserDefaults, 0);
        }
        return preferences;
    }

    public static void put(String key, String obj) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(key, obj);
        ed.apply();
    }

    public static void put(String key, long obj) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(key, obj);
        ed.apply();
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putBoolean(key, value);
        ed.apply();
    }

    public static boolean getBoolean(String key, boolean defVal) {
        return SharedPreferenceHandler.getInstance().getBoolean(key, defVal);
    }

    public static String getString(String key, String defVal) {
        return SharedPreferenceHandler.getInstance().getString(key, defVal);
    }

    public static void delete(String key) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.remove(key);
        ed.apply();
    }

}
