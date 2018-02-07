package com.fishbowl.basicmodule.Controllers;
/**
 * Created by digvijay(dj)
 */
import android.app.Activity;
import android.os.Bundle;

import com.fishbowl.basicmodule.Analytics.FBAnalytic;

import org.json.JSONObject;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 //setContentView(R.layout.activity_main);
	     FBAnalytic.sharedInstance().init(this, "http://jamba.clpqa.com/clpapi/", "91225258ddb5c8503dce33719c5deda7");
	     FBAnalytic.sharedInstance().recordEvent(new JSONObject());
	     FBAnalytic.sharedInstance().recordEvent(new JSONObject());
	     FBAnalytic.sharedInstance().recordEvent(new JSONObject());
		
	}

}
