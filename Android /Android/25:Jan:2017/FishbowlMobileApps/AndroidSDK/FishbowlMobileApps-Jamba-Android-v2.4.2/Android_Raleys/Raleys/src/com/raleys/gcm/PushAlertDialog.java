package com.raleys.gcm;

import com.raleys.app.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class PushAlertDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_alert_dialog);

		try {
			TextView txtview = (TextView) findViewById(R.id.notification_message);
			txtview.setText(getIntent().getExtras().getString(
					com.raleys.gcm.RegisterApp.EXTRA_MESSAGE));
		} catch (Exception e) {
			e.printStackTrace();
			this.finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.push_alert_dialog, menu);
		return true;
	}

	public void close(View v) {
		this.finish();
	}

}
