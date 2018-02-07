package com.clp.sdk;

import org.json.JSONObject;

import com.clp.Analytic.ClypAnalytic;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 setContentView(R.layout.activity_main);
		
	     ClypAnalytic.sharedInstance().init(this, "http://jamba.clpqa.com/clpapi/", "91225258ddb5c8503dce33719c5deda7");
	     ClypAnalytic.sharedInstance().recordEvent(new JSONObject());
	     ClypAnalytic.sharedInstance().recordEvent(new JSONObject());
	     ClypAnalytic.sharedInstance().recordEvent(new JSONObject());

		
	}

}
