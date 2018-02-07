package com.donatos.phoenix.network.pushnotification;

import android.util.Log;
import com.donatos.phoenix.p134b.C2508l;
import com.google.firebase.messaging.C3932a;
import com.google.firebase.messaging.FirebaseMessagingService;

public class PushNotificationMessagingService extends FirebaseMessagingService {
    private static final String PUSH_NOTIFICATION_ID = "google.message_id";
    private static final String PUSH_NOTIFICATION_KEY = "payload";
    private static final String TAG = "Firebase";

    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    public void onMessageReceived(C3932a c3932a) {
        if (c3932a.m12956c() != null) {
            Log.d(TAG, "Message Notification Body: " + c3932a.m12956c().m12953b());
            C2508l.m7347a().m7349a(new PushNotificationReceivedEvent(c3932a));
        }
    }
}
