package com.hbh.honeybaked.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.hbh.honeybaked.constants.PreferenceConstants;
import java.util.HashSet;
import java.util.Set;

public class PreferenceHelper {

    private SharedPreferences _sharedPrefs;
    private Context mContext;
    private Editor _prefsEditor ;
    public PreferenceHelper(Context context) {
        this.mContext = context;
        this._sharedPrefs = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME_MAIN, 0);
        _prefsEditor=this._sharedPrefs.edit();
    }

    public String getStringValue(String keyName) {
        return this._sharedPrefs.getString(keyName, "");
    }

    public String getStringValue(String keyName, String defaultValue) {
        return this._sharedPrefs.getString(keyName, defaultValue);
    }

    public void saveStringValue(String keyName, String text) {
        this._prefsEditor.putString(keyName, text);
        this._prefsEditor.commit();
    }

    public int getIntValue(String keyName) {
        return this._sharedPrefs.getInt(keyName, 0);
    }

    public int getIntValue(String keyName, int defaultValue) {
        return this._sharedPrefs.getInt(keyName, defaultValue);
    }

    public void saveIntValue(String keyName, int intValue) {
        this._prefsEditor.putInt(keyName, intValue);
        this._prefsEditor.commit();
    }

    public boolean getBooleanValue(String keyName) {
        return this._sharedPrefs.getBoolean(keyName, false);
    }

    public void saveBooleanValue(String keyName, boolean text) {
        this._prefsEditor.putBoolean(keyName, text);
        this._prefsEditor.commit();
    }

    public Set<String> getStringSetValue(String keyName) {
        return this._sharedPrefs.getStringSet(keyName, new HashSet());
    }

    public void saveStringSetValue(String keyName, Set<String> text) {
        this._prefsEditor.putStringSet(keyName, text);
        this._prefsEditor.commit();
    }

    public void clearPreference(String sPreferenceName) {
        Editor _clearPrefsEditor = this.mContext.getSharedPreferences(sPreferenceName, 0).edit();
        _clearPrefsEditor.clear();
        _clearPrefsEditor.commit();
    }
}
