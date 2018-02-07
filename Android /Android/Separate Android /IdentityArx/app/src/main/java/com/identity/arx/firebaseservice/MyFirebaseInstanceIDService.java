package com.identity.arx.firebaseservice;

import android.content.SharedPreferences.Editor;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.identity.arx.general.AppSharedPrefrence;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public void onTokenRefresh() {
        super.onTokenRefresh();
        storeRegIdInPref(FirebaseInstanceId.getInstance().getToken());
    }

    private void storeRegIdInPref(String token) {
        Editor editor = AppSharedPrefrence.getSharedPrefrence(getApplicationContext()).edit();
        editor.putString("NOTIFICATION_REGISTER_ID", token);
        editor.commit();
    }
}
