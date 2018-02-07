package com.fishbowl.basicmodule.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by digvijay(dj)
 */
public class FBActionActivity extends Activity {
    public static int FLAG_FROM_MINIMIZE = 71303168;
    public static int FLAG_FROM_BACKGROUND = 336609280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Launch the App
        int constants = getIntent().getFlags();

        if (getIntent().getFlags() == FLAG_FROM_BACKGROUND) {
            String packageName = this.getPackageName();
            Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            launchIntent.putExtras(getIntent());
            startActivity(launchIntent);
        } else {
            String packageName = this.getPackageName();
            Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.putExtras(getIntent());
            startActivity(launchIntent);
            if (FBSdk.sharedInstance(this) != null) {
                FBSdk.sharedInstance(this).processPushMessage(getIntent());
            }

        }
        finish();
    }
}
