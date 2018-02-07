package com.clp.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.clp.sdk.CLPSdk;
import com.google.android.gms.gcm.GcmListenerService;


public class CLPGCMListenerService extends GcmListenerService {
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // Listener called when New Push Message is received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Intent messageReceived = new Intent(CLPSdk.CLP_PUSH_MESSAGE_RECEIVED);
        messageReceived.putExtras(data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageReceived);
    }
}