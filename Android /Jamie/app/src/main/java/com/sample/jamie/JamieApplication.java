package com.sample.jamie;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBService;

import static com.google.android.gms.vision.barcode.Barcode.URL;


/**
 * Created by digvijay(dj)
 */
public class JamieApplication extends Application implements Application.ActivityLifecycleCallbacks, FBSdk.OnFBSdkRegisterListener {
    static JamieApplication instance;
    public FBSdk fbsdkObj;
    Boolean sdk = true;
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    public static JamieApplication getAppContext()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        initializeFishbowlSDK();

    }


    private void initializeFishbowlSDK()
    {
        try {

            FBService.initialize(this, Constants.sdkPointingUrl("Your Base URL"), "null");
            FBCustomerItem customer = new FBCustomerItem();


            {

                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");

                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
                FBConfig.setClient_tenantid("1173");
                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");

                FBConfig.setGcmSenderId("355242573491");
                FBConfig.setPushIconResource(R.drawable.ic_launcher);
                FBConfig.setSmallpushIconResource(R.drawable.ic_launcher);

                FBSdk  fbsdkObj = FBSdk.sharedInstanceWithKey(this, "Your Base URL"), this, FBConfig, customer, false);
            }
        } catch (Exception e) {

        }
    }
   // here "Your Base URL" indicate pointing  your url


    @Override
    public void onFBRegistrationSuccess(String FBCustomerItem) {

    }


    public void onFBRegistrationError(String error) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        created++;

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        created--;
        if (created == 0) {

        }
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        return resumed > paused;
    }


}
