package com.raleys.locationservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStartService extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
/*		Intent intent = new Intent(context, LocationService.class);
		context.startService(intent);*/
		Log.i("Autostart", "started");
	}
}
