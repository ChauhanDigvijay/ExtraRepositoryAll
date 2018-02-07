package com.raleys.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.raleys.app.android.Raleys;
import com.raleys.app.android.RaleysApplication;
import com.raleys.app.android.SplashScreen;

public class RegisterApp extends AsyncTask<Void, Void, String> {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PUSH_BUNDLE = "bundle";
	private static final String TAG = "Raleys";
	Context ctx;
	GoogleCloudMessaging gcm;

	String regid = null;
	private int appVersion;
	String id;
	RaleysApplication _appNew;

	public RegisterApp(Context ctx, GoogleCloudMessaging gcm,
			RaleysApplication _app, int appVersion) {
		this.ctx = ctx;
		this.gcm = gcm;
		this.appVersion = appVersion;
		this._appNew = _app;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... arg0) {

		// id = car.userid;
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(ctx);
			}
			regid = gcm.register(RaleysApplication.SENDER_ID);
			_appNew.regid = regid;
			// car.register_id = regid;
			msg = "Device registered, registration ID=" + regid;
//			Log.i("GCM - REGID", regid);
			Raleys _raleys = Raleys.shared(this._appNew);
			// _raleys.userRegister();

			final SharedPreferences prefs = ctx.getSharedPreferences(
					SplashScreen.class.getSimpleName(), Context.MODE_PRIVATE);

//			Log.i(TAG, "Saving regId on app version " + appVersion);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("registration_id", regid);
			editor.putInt("appVersion", appVersion);
			editor.commit();

		} catch (Exception ex) {
			msg = "Error :" + ex.getMessage();
		}
		return msg;
	}

	@Override
	protected void onPostExecute(String result) {
		if (regid != null && regid.length() > 0) {
			// _appNew.customerRegistration(regid);
			Raleys.shared(this._appNew).userRegister();
		}
		super.onPostExecute(result);
	}
}
