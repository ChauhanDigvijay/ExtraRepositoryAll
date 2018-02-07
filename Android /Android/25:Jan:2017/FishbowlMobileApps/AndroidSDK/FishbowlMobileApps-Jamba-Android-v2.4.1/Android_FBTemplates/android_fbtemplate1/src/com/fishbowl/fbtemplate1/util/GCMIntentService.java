package com.fishbowl.fbtemplate1.util;


import org.json.JSONException;
import org.json.JSONObject;

import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Controller.FB_DBOffer;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.OfferItem;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    static final String TAG = "GCMDemo";
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.i("NOTIF", " : received ");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
                String title = "";
                String url = "";
                String sound = "";
                try {
					String apsStr = intent.getExtras().getString("aps");
					String eddate= intent.getExtras().getString("ed");
					
					if (apsStr != null) {
						JSONObject apsContent;
						apsContent = new JSONObject(apsStr);
						if (apsContent.has("alert")) {
							title = apsContent.getString("alert");
						//	url = apsContent.getString("url");
							sound = apsContent.getString("sound");
							OfferItem offer =new OfferItem();
							
						//	offer.setOfferIUrl(url);
							offer.setOfferIName(title);
							offer.setOfferIOther(eddate);
							offer.setOfferIItem(sound);
							FB_DBOffer.getInstance().createOrderConfirm(offer);	
							
							FishbowlTemplate1App.getInstance().setCountoffer(FB_DBOffer.getInstance().getAllItem().size()+1);
						}
						
						updateOffer(intent);
/*						if (apsContent.has("sound")) {
							sound_url = apsContent.getString("sound");
							if (sound_url != null) {
								sound_url = sound_url.toLowerCase();
								// File f = new File("" + Uri.parse(sound_url));
								// fileName = f.getName();
								newFileName = sound_url.replace(".wma", "");
								newFileName = newFileName.replace(".wav", "");
								newFileName = newFileName.replace(".caf", "");
							}
						}*/

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
                // Post notification of received message.
                sendNotification(title);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    
    private void updateOffer(Intent intent)
    {
    	 Bundle extras = intent.getExtras();
			

	}
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
    	Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
    	NotificationManager    mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
               new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setLargeIcon(largeIcon)
        .setContentTitle("OFFER")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
       .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        
    }
}