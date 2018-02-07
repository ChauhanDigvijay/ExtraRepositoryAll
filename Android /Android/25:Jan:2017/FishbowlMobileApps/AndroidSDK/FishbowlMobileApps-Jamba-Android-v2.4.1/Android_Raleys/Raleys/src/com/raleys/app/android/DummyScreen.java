package com.raleys.app.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

//this class exists solely to determine the usable screen height which can only be determined after an Activity is visible
public class DummyScreen extends Activity {
	private RaleysApplication _app;

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
		nextScreen();
	}

	@Override
	public void onPause() {
		super.onPause();
		_app.resetScreenHeight(this);
		_app.adjustScreenHeight(this); // adjusts height based on status bar
										// location, only works after the screen
										// is visible
	}

	private void nextScreen() {

		try {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent rIntent = getIntent();
					 Intent intent = new Intent(DummyScreen.this,
							NewDummyScreen.class);
					if (rIntent.hasExtra("clpnid")) {
						Log.i("DummyScreen", rIntent.getExtras().toString());
						intent.putExtras(rIntent);
					}
					startActivity(intent);
					DummyScreen.this.finish();
				}
			}, 10);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
