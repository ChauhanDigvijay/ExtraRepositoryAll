package com.donatos.phoenix.network.pushnotification;

import com.google.firebase.messaging.C3932a;

public class PushNotificationReceivedEvent {
    private final C3932a message;

    public PushNotificationReceivedEvent(C3932a c3932a) {
        this.message = c3932a;
    }

    public C3932a getMessage() {
        return this.message;
    }
}
