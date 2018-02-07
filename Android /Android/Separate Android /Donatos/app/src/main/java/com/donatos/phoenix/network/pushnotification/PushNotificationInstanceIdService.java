package com.donatos.phoenix.network.pushnotification;

import android.util.Log;
import com.donatos.phoenix.p134b.C2508l;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class PushNotificationInstanceIdService extends FirebaseInstanceIdService {
    public void onTokenRefresh() {
        String b = FirebaseInstanceId.m12888a().m12896b();
        Log.d("Firebase", "Refreshed token: " + b);
        C2508l.m7347a().m7349a(new PushNotificationTokenUpdateEvent(b));
    }
}
