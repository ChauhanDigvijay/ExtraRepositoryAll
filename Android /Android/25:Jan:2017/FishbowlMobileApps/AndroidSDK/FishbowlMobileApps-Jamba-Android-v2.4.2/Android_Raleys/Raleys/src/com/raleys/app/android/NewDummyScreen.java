package com.raleys.app.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class NewDummyScreen extends Activity {
	private RaleysApplication _app;
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_app = (RaleysApplication) getApplication();
		RelativeLayout _backgroundLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams main_layout_params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		main_layout_params.width = _app.getScreenWidth();
		main_layout_params.height = _app.getScreenHeight();
		_backgroundLayout.setBackgroundColor(Color.BLACK);
		setContentView(_backgroundLayout);
	}

	@Override
	public void onResume() {
		nextScreen();
		super.onResume();
	}

	private void nextScreen() {
		 Intent intent = new Intent(NewDummyScreen.this, SplashScreen.class);

		try {
			Intent rIntent = getIntent();
			if (rIntent.hasExtra("clpnid")) {
				Log.i("NewDummyScreen", rIntent.getExtras().toString());
				intent.putExtras(rIntent);
			}
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
