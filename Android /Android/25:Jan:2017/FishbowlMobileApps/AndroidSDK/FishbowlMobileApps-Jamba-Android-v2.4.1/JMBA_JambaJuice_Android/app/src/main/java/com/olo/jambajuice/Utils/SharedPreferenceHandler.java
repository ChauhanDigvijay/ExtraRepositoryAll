package com.olo.jambajuice.Utils;

import android.content.SharedPreferences;

import com.olo.jambajuice.JambaApplication;

/**
 * Created by Nauman Afzaal on 19/05/15.
 */
public class SharedPreferenceHandler {
    public static String LastProductUpdate = "LastProductUpdate";
    public static String LastSearchedLocation = "LastSearchedLocation";
    public static String IsFirstSplash = "IsFirstSplash";
    public static String FirstLaunch = "FirstLaunch";
    public static String FirstPurchase = "FirstPurchase";
    public static String LastSelectedStore = "LastSelectedStore";
    public static String PushToken = "PushToken";

    static String UserDefaults = "UserDefaults";
    static SharedPreferences preferences;

    public static SharedPreferences getInstance() {
        if (preferences == null) {
            preferences = JambaApplication.getAppContext().getSharedPreferences(UserDefaults, 0);
        }
        return preferences;
    }

    public static void put(String key, String obj) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(key, obj);
        ed.commit();
    }

    public static void put(String key, long obj) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(key, obj);
        ed.commit();
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putBoolean(key, value);
        ed.commit();
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
        ed.commit();
    }
}
