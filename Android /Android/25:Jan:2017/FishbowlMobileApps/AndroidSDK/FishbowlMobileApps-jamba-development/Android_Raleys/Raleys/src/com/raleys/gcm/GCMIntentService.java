package com.raleys.gcm;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.raleys.app.android.R;
import com.raleys.app.android.RaleysApplication;

public class GCMIntentService extends IntentService {

	public GCMIntentService() {
		super("GcmIntentService");
	}

	public static int NOTIFICATION_ID = 237;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	RaleysApplication _app;
	String url;
	String couponId;
	String promo_code;
	String sound_url;
	String userId;
	String externalId;
	String acceptGroup;
	String ptitle;
	String endDate;
	String fileName;
	String newFileName;
	String CURRENT_NOTIFICATION_STRING;
	Intent notifIntent;
	Bundle extras;

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("GCMIntentService", "onHandleIntent");
		extras = intent.getExtras();
		_app = (RaleysApplication) getApplication();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				try {
					String apsStr = intent.getExtras().getString("aps");
					JSONObject jsonObj = new JSONObject(apsStr);
					if (jsonObj.has("alert")) {
						ptitle = jsonObj.getString("alert");
					}
					if (jsonObj.has("sound")) {
						sound_url = jsonObj.getString("sound");
						if (sound_url != null) {
							sound_url = sound_url.toLowerCase();
							sound_url = sound_url.replace(".wma", "");
							sound_url = sound_url.replace(".wav", "");
							sound_url = sound_url.replace(".caf", "");
						}
					}
					sendNotification(intent, ptitle, sound_url);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
//			Log.i("Message", "Received: " + extras.toString());
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(Intent intent, String msg,
			String soundfileName) {

		try {

			// Application Name
			String appName = getAppName(this);
			// Increment notification id
			NOTIFICATION_ID++;
			// Tag Name of Notification
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
			int hourofday = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			int millisecond = cal.get(Calendar.MILLISECOND);

			StringBuilder MY_NOTIFICATION_ID = new StringBuilder(500);
			MY_NOTIFICATION_ID.append(year).append(month).append(dayofmonth)
					.append(hourofday).append(minute).append(minute)
					.append(second).append(millisecond);
//			Log.i("ID : ", "" + MY_NOTIFICATION_ID);
			CURRENT_NOTIFICATION_STRING = MY_NOTIFICATION_ID.toString();

			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notifIntent = new Intent(this,
					com.raleys.app.android.DummyScreen.class);
			notifIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Set is notification received
			notifIntent.putExtra("NotificationString", "GCM");
			notifIntent.putExtras(extras);
//			Log.i("notifIntent", notifIntent.getExtras().toString());

			notifIntent
					.setClass(this, com.raleys.app.android.DummyScreen.class);
			notifIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(this,
					NOTIFICATION_ID, notifIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			NotificationCompat.Builder mBuilder;
			//Lollipop fix
			if(android.os.Build.VERSION.SDK_INT<20){
				mBuilder = new NotificationCompat.Builder(
						this)
						.setSmallIcon(R.drawable.app_icon)
						.setWhen(System.currentTimeMillis())
						.setContentTitle(appName)
						.setStyle(
								new NotificationCompat.BigTextStyle().bigText(msg))
						.setTicker(appName).setContentText(msg).setAutoCancel(true);
			}else{
				Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
				mBuilder = new NotificationCompat.Builder(
						this)
						.setSmallIcon(R.drawable.app_icon)
						.setLargeIcon(largeIcon)
						.setColor(Color.WHITE)
						.setWhen(System.currentTimeMillis())
						.setContentTitle(appName)
						.setStyle(
								new NotificationCompat.BigTextStyle().bigText(msg))
						.setTicker(appName).setContentText(msg).setAutoCancel(true);
			}

			mBuilder.setContentIntent(contentIntent);

			@SuppressWarnings("deprecation")
			Notification notification = mBuilder.getNotification();

			mNotificationManager.notify(CURRENT_NOTIFICATION_STRING,
					NOTIFICATION_ID, notification);

			// Get sound notification URI
			Uri notificationuri = _app.getNotificationURI(soundfileName);
			if (notificationuri != null) {
				Ringtone ringtone;
				ringtone = RingtoneManager.getRingtone(this, notificationuri);
				ringtone.play();
			}
//			_app.clpsdkObj.playSound(soundfileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Get Application Name
	private String getAppName(GCMIntentService context) {
		CharSequence appName = context.getPackageManager().getApplicationLabel(
				context.getApplicationInfo());

		return (String) appName;
	}

}
