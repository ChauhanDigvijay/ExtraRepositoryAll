//package com.Preferences;
//
//import android.content.SharedPreferences;
//
//
//
///**
// * Created by Nauman Afzaal on 19/05/15.
// */
//public class SharedPreferenceHandler {
//    public static String LastProductUpdate = "LastProductUpdate";
//    public static String LastSearchedLocation = "LastSearchedLocation";
//    public static String IsFirstSplash = "IsFirstSplash";
//    public static String FirstLaunch = "FirstLaunch";
//    public static String FirstPurchase = "FirstPurchase";
//    public static String LastSelectedStore = "LastSelectedStore";
//
//    static String UserDefaults = "UserDefaults";
//    static SharedPreferenceHandler preferences;
//
//    public static SharedPreferenceHandler getInstance() {
//        if (preferences == null) {
//            preferences = new SharedPreferenceHandler();
//        }
//        return preferences;
//    }
//
//    public static void put(String key, String obj) {
//        SharedPreferences.Editor ed = preferences.edit();
//        ed.putString(key, obj);
//        ed.commit();
//    }
//
//    public static void put(String key, long obj) {
//        SharedPreferences.Editor ed = preferences.edit();
//        ed.putLong(key, obj);
//        ed.commit();
//    }
//
//    public static void put(String key, boolean value) {
//        SharedPreferences.Editor ed = preferences.edit();
//        ed.putBoolean(key, value);
//        ed.commit();
//    }
//
//    public static boolean getBoolean(String key, boolean defVal) {
//        return SharedPreferenceHandler.getInstance().getBoolean(key, defVal);
//    }
//
//    public static String getString(String key, String defVal) {
//        return SharedPreferenceHandler.getInstance().getString(key, defVal);
//    }
//
//    public static void delete(String key) {
//        SharedPreferences.Editor ed = preferences.edit();
//        ed.remove(key);
//        ed.commit();
//    }
//}
