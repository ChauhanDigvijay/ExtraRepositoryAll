package com.fishbowl.basicmodule.Controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fishbowl.basicmodule.Utils.FBUtility;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by digvijay(dj)
 */
public class LocationServiceCheck extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent data) {

		FBSdk _FBSdk = FBSdk.sharedInstance(context);
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
		cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		long restartMin = TimeUnit.SECONDS.toMinutes(_FBSdk
				.getLOCATION_SERVICE_REFRESH_TIME());
		cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)
				+ (int) restartMin);// schedule service restart for every min
		cal.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
		cal.set(Calendar.DATE, calendar.get(Calendar.DATE));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));

		// set alarm to start service again after receiving broadcast
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, LocationServiceCheck.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		am.cancel(pendingIntent);
		am.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);


		//check location service is running
		if (!FBUtility.isMyServiceRunning(LocationService.class, context)) {
			intent = new Intent(context, LocationService.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent);
		} else {
//			intent = new Intent();
//			intent.setAction(fbSdk.CLP_SETTINGS_UPDATE_CALLBACK);
//			context.sendBroadcast(intent);
		}

	}

}
