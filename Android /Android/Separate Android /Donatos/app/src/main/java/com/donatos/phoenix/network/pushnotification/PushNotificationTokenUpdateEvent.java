package com.donatos.phoenix.network.pushnotification;

public class PushNotificationTokenUpdateEvent {
    private final String token;

    public PushNotificationTokenUpdateEvent(String str) {
        this.token = str;
    }

    public String getToken() {
        return this.token;
    }
}
