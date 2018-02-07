package com.identity.arx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.identity.arx.firebaseservice.Config;
import com.identity.arx.general.MasterActivity;


public class SplashActivity extends MasterActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    class C07581 extends BroadcastReceiver {
        C07581() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
            } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                Toast.makeText(SplashActivity.this.getApplicationContext(), "Push notification: " + intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        super.onCreate(savedInstanceState);
        setActionBarTitle("");
        if (this.sharedPreference.getString("LOGIN_STATUS", "").equalsIgnoreCase("second")) {
            intent = new Intent(this, PaswordActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
        this.mRegistrationBroadcastReceiver = new C07581();
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
