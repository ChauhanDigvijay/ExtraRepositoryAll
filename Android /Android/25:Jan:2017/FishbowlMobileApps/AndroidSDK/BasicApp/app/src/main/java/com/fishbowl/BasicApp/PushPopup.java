package com.fishbowl.BasicApp;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by digvijaychauhan on 26/09/17.
 */

public class PushPopup  extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {


            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);

            } catch (Exception e) {

            }
        }
    }

}
