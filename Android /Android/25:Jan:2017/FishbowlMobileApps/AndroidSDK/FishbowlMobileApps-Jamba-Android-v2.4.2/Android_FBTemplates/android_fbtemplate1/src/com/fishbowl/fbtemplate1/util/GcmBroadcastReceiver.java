package com.fishbowl.fbtemplate1.util;




import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.activity.MenuActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("GcmBroadcastReceiver", "GcmBroadcastReceiver");
		final String registrationId = intent.getStringExtra("registration_id");
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMIntentService.class.getName());

		FishbowlTemplate1App fishTApp=FishbowlTemplate1App.getInstance();

		MenuActivity mainac=fishTApp.getMainActivity();
		if(fishTApp.getLoggedInUser()!=null&&(FB_DBUser.getInstance().getAllUsers().size()>0))
		{
			UserItem user=	fishTApp.getLoggedInUser();

			if( mainac!=null && StringUtilities.isValidString(registrationId))
			{
				FishbowlTemplate1App.getInstance().regid = registrationId;
				final SharedPreferences prefs = mainac.getSharedPreferences(
						mainac.getLocalClassName(), Context.MODE_PRIVATE);

				//			Log.i(TAG, "Saving regId on app version " + appVersion);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("registration_id", registrationId);
				if(user!=null)	
				{

					{
						//user.setPushToken(registrationId);
						//update user
						if(user!=null && !StringUtilities.isValidString(user.getPushToken())){
							user.setPushToken(registrationId);
							FB_DBUser.getInstance().createUpdateUser(user, true);
							FB_DBUser.getInstance().CreateCustomerWithCompany(user);
						}else{
							if(user!=null && StringUtilities.isValidString(user.getPushToken()) && registrationId!=null && !registrationId.equalsIgnoreCase(user.getPushToken()))
							{
								user.setPushToken(registrationId);
								FB_DBUser.getInstance().createUpdateUser(user, true);
								FB_DBUser.getInstance().CreateCustomerWithCompany(user);

							}
						}
					}
					editor.putInt("appVersion", FishbowlTemplate1App.getInstance().getAppVersion(fishTApp.getMainActivity()));
					editor.commit();
				}


			
			}
			intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			// Start the service, keeping the device awake while it is launching.
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);
		}
	}
}
	
