package com.identity.arx.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.identity.arx.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_USERNAME = "uname";
    private static final String PREF_NAME = "Java4you";
    int PRIVATE_MODE = 0;
    Context _context;
    Editor editor;
    SharedPreferences pref;

    public SessionManager(Context context) {
        this._context = context;
        this.pref = this._context.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public void createLoginSession(String username, String password) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(KEY_USERNAME, username);
        this.editor.putString(KEY_PWD, password);
        this.editor.commit();
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            this._context.startActivity(new Intent(this._context, LoginActivity.class).addFlags(67108864).setFlags(268435456));
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap();
        user.put(KEY_USERNAME, this.pref.getString(KEY_USERNAME, null));
        user.put(KEY_PWD, this.pref.getString(KEY_PWD, null));
        return user;
    }

    public void logoutUser() {
        this.editor.clear();
        this.editor.commit();
        this._context.startActivity(new Intent(this._context, LoginActivity.class).addFlags(67108864).setFlags(268435456));
    }

    public boolean isLoggedIn() {
        return this.pref.getBoolean(IS_LOGIN, false);
    }
}
