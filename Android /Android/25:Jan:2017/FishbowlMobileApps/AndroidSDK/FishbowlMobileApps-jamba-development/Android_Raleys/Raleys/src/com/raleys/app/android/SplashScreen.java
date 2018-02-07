package com.raleys.app.android;

import java.util.concurrent.atomic.AtomicInteger;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SplashScreen extends Activity {
	int bottomWidth;
	int bottomHeight;

	// GCM
	GoogleCloudMessaging gcm;
	String regid;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	AtomicInteger msgId = new AtomicInteger();
	LocationManager locationManager;

	boolean gpsStatus = false;
	boolean hasGps;
	boolean _gpsstatus;

	public boolean scheduled = false;
	ImageView image;
	public static final long DELAY = 2000;
	Context context;
	private RaleysApplication _app;
	Handler newHanler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			context = this;
			_app = (RaleysApplication) getApplication();
			image = new ImageView(this);
			image.setBackgroundResource(R.drawable.splash_screen);
			image.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			setContentView(image);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Handler newHandler = new Handler();
			newHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					nextScreen();
				}
			}, 1500);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void nextScreen() {

		try {
			Intent intent = null;
			if (_app.isLoggedIn() == true) {
				intent = new Intent(SplashScreen.this, ShoppingScreen.class);
			} else {
				intent = new Intent(SplashScreen.this, LoginScreen.class);
			}
			Intent rIntent = getIntent();
			if (rIntent.hasExtra("clpnid")) {
				intent.putExtras(rIntent.getExtras());
			}
			startActivity(intent);
			SplashScreen.this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
