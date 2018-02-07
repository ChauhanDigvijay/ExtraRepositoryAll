package com.identity.arx.firebaseservice;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.identity.arx.faculty.TakeAttendanceActivity;
import com.identity.arx.general.AppNotification;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            if (remoteMessage.getNotification() != null) {
                AppNotification.setNotificationMessage(this, (String) remoteMessage.getData().get("title"), (String) remoteMessage.getData().get("body"), 1, TakeAttendanceActivity.class);
            }
            if (remoteMessage.getData().size() > 0) {
                try {
                    new JSONObject(remoteMessage.getData().toString()).toString();
                } catch (Exception e) {
                }
            }
        }
    }
}
