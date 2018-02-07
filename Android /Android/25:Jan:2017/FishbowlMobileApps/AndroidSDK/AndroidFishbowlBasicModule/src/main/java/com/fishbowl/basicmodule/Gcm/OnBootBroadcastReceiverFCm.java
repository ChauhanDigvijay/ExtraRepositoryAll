package com.fishbowl.basicmodule.Gcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBActionActivity;
import com.fishbowl.basicmodule.Fcm.FBFirebaseMessagingService;
import com.fishbowl.basicmodule.Utils.Config;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.fishbowl.basicmodule.Controllers.FBSdk.context;


/**
 * Created by digvijaychauhan on 21/09/17.
 */

public class OnBootBroadcastReceiverFCm  extends FirebaseMessagingService {
    private static final String TAG = FBFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;


//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);

                handleDataMessage(object,remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(context)) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(context);
//            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }



    private void handleDataMessage(JSONObject datajson,RemoteMessage remoteMessage) {
        //   Log.e(TAG, "push json: " + json.toString());

        try {

            String title = FBUtility.getAppName(context);
            String message = remoteMessage.getNotification().getBody();;
            String imageUrl = datajson.getString("previewimage");
            String timestamp = datajson.getString("ed");


            if (!NotificationUtils.isAppIsInBackground(context)) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("datapayloadjson", datajson.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent resultIntent = new Intent(context, FBActionActivity.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("datapayloadjson", datajson.toString());
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(context, title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(context, title, message, timestamp, resultIntent, imageUrl);
                }

//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(context);
//                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(context, FBActionActivity.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("datapayloadjson", datajson.toString());


                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(context, title, message, timestamp, resultIntent);
                    Log.e(TAG, "Djwithimage: " + remoteMessage.getData().toString());
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(context, title, message, timestamp, resultIntent, imageUrl);
                    Log.e(TAG, "Djwithoutimage: " + remoteMessage.getData().toString());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}


