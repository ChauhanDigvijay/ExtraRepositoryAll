package com.womensafety.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Preference {
    public static String CELL = "CELL";
    public static String CODE = "CODE";
    public static String PIN = "PIN";
    public static String USER_NAME = "USER_NAME";
    private Editor edit ;
    private SharedPreferences preferences;

    public Preference(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
          edit = this.preferences.edit();
    }

    public void saveString(String strKey, String strValue) {
        this.edit.putString(strKey, strValue);
    }

    public void saveInt(String strKey, int value) {
        this.edit.putInt(strKey, value);
    }

    public void saveBoolean(String strKey, boolean value) {
        this.edit.putBoolean(strKey, value);
    }

    public void saveLong(String strKey, Long value) {
        this.edit.putLong(strKey, value.longValue());
    }

    public void saveDouble(String strKey, String value) {
        this.edit.putString(strKey, value);
    }

    public void remove(String strKey) {
        this.edit.remove(strKey);
    }

    public void commit() {
        this.edit.commit();
    }

    public String getString(String strKey, String defaultValue) {
        return this.preferences.getString(strKey, defaultValue);
    }

    public boolean getBoolean(String strKey, boolean defaultValue) {
        return this.preferences.getBoolean(strKey, defaultValue);
    }

    public int getInt(String strKey, int defaultValue) {
        return this.preferences.getInt(strKey, defaultValue);
    }

    public double getDouble(String strKey, double defaultValue) {
        return Double.parseDouble(this.preferences.getString(strKey, "" + defaultValue));
    }

    public long getLongInPreference(String strKey) {
        return this.preferences.getLong(strKey, 0);
    }

    public void removePreferenceFile() {
        this.edit.clear();
        this.edit.commit();
    }
}
