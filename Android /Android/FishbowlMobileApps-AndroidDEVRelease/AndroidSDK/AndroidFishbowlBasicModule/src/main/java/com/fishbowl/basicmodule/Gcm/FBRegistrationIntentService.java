package com.fishbowl.basicmodule.Gcm;
/**
 * Created by digvijay(dj)
 */
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
/**
 * Created by digvijay(dj)
 */
public class FBRegistrationIntentService extends IntentService {


    public FBRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token=new String();
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);

            String gcmSenderID=intent.getStringExtra("SENDER_ID");
            token = instanceID.getToken(gcmSenderID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d("CLP_LOG", "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(FBSdk.CLP_REGISTRATION_COMPLETE);
        registrationComplete.putExtra(FBSdk.FB_GCM_TOKEN,token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}