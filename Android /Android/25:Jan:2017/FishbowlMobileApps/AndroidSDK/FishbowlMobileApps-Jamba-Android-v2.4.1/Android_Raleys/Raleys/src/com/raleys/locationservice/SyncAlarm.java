package com.raleys.locationservice;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.raleys.app.android.RaleysApplication;

public class SyncAlarm extends BroadcastReceiver {
	private static final String UPDATE_GPS_SETTINGS = "UPDATE_GPS_SETTINGS";
	RaleysApplication _app;
	int localGpsSwitchOnDuration;

	@Override
	public void onReceive(Context context, Intent data) {
		_app = (RaleysApplication) context.getApplicationContext();
	/*	if (_app.beaconSchedule == null
				|| _app.beaconSchedule.gpsSwitchOnDuration == 0) {
			localGpsSwitchOnDuration = 120;
		} else {
			localGpsSwitchOnDuration = _app.beaconSchedule.gpsSwitchOnDuration;
		}
*/
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
		cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));

		// start service after 2min
//		cal.set(Calendar.MINUTE,
//				calendar.get(Calendar.MINUTE)
//						+ 2);//schedule service restart for every 2min
		long restartMin=TimeUnit.SECONDS.toMinutes(localGpsSwitchOnDuration);
		cal.set(Calendar.MINUTE,
				calendar.get(Calendar.MINUTE)
						+(int)restartMin);//schedule service restart for every 2min
		cal.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
		cal.set(Calendar.DATE, calendar.get(Calendar.DATE));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));

		// set alarm to start service again after receiving broadcast
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, SyncAlarm.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		am.cancel(pendingIntent);
		am.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
/*
		if (!isMyServiceRunning(LocationService.class, context)) {
			intent = new Intent(context, LocationService.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent);
		} else {
			Intent new_intent = new Intent();
			new_intent.setAction(UPDATE_GPS_SETTINGS);
			context.sendBroadcast(new_intent);
		}*/
	}

	private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
