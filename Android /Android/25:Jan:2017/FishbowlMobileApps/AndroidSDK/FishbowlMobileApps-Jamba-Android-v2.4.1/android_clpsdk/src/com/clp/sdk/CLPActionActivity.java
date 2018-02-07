package com.clp.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CLPActionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Launch the App
        String packageName = this.getPackageName();
        Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.putExtras(getIntent());
        
        if(CLPSdk.sharedInstance(this)!=null){
        CLPSdk.sharedInstance(this).processPushMessage(getIntent());
        }
        
        startActivity(launchIntent);
        finish();
    }
}
