package com.raleys.gcm;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.raleys.app.android.ShoppingScreen;
import com.raleys.app.android.SplashScreen;

public class PushHandlerActivity extends Activity {

	private static String TAG = "PushHandlerActivity";
	private Timer splashTimer;
	private boolean scheduled = false;
	private Context context;

	/*
	 * this activity will be started if the user touches a notification that we
	 * own. We send it's data off to the push plugin for processing. If needed,
	 * we boot up the main activity to kickstart the application.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		context = this;

		boolean forground = isInForeground();
		if (ShoppingScreen.active && forground) {
			checkPushNotification();
			finish();
		} else {
			forceMainActivityReload();
			splashTimer = new Timer();
			splashTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					scheduled = true;
					checkPushNotification();
					PushHandlerActivity.this.finish();
				}
			}, (SplashScreen.DELAY * 2));
		}
	}

	// private boolean isAppForground() {
	//
	// ActivityManager mActivityManager = (ActivityManager)
	// getSystemService(Context.ACTIVITY_SERVICE);
	// List<RunningAppProcessInfo> l = mActivityManager
	// .getRunningAppProcesses();
	// Iterator<RunningAppProcessInfo> i = l.iterator();
	// while (i.hasNext()) {
	// RunningAppProcessInfo info = i.next();
	//
	// if (info.uid == getApplicationInfo().uid
	// && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
	// return true;
	// }
	// }
	// return false;
	// }

	public boolean isInForeground() {
		try {
			ActivityManager activityManager = (ActivityManager) getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> services = activityManager
					.getRunningTasks(Integer.MAX_VALUE);

			if (services.get(0).topActivity
					.getPackageName()
					.toString()
					.equalsIgnoreCase(
							getApplicationContext().getPackageName().toString()))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void checkPushNotification() {
		try {
			Bundle originalExtras = getIntent().getExtras().getBundle(
					com.raleys.gcm.RegisterApp.PUSH_BUNDLE);
			if (originalExtras != null) {
				String message = originalExtras
						.getString(com.raleys.gcm.RegisterApp.EXTRA_MESSAGE);
				Intent aintent = new Intent(context,
						com.raleys.gcm.PushAlertDialog.class);
				// aintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				aintent.putExtra(com.raleys.gcm.RegisterApp.EXTRA_MESSAGE,
						message);
				startActivity(aintent);
				getIntent().removeExtra(com.raleys.gcm.RegisterApp.PUSH_BUNDLE);

				// GCMIntentService.cancelNotification(context,
				// (originalExtras.getInt("key")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Forces the main activity to re-launch if it's unloaded.
	 */
	private void forceMainActivityReload() {
		PackageManager pm = getPackageManager();
		Intent launchIntent = pm
				.getLaunchIntentForPackage(getApplicationContext()
						.getPackageName());
		startActivity(launchIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (scheduled)
				splashTimer.cancel();
			splashTimer.purge();
		} catch (Exception e) {
		}
	}
	public void CancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx
                .getSystemService(ns);
        nMgr.cancel(notifyId);
    }

}