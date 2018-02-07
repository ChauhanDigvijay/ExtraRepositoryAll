package com.android.Jcenter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.android.jcenter_projectlibrary.Controllers.FBSdk;
import com.android.jcenter_projectlibrary.Models.FBConfig;
import com.android.jcenter_projectlibrary.Utils.FBConstant;


public class LoyaltyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    static LoyaltyApplication instance;
    public FBSdk clpsdkObj;
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    public static LoyaltyApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeCLPSDK();

    }


    private void initializeCLPSDK() {
        try {
            {
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
                FBConfig.setGcmSenderId(FBConstant.gcm_sender_id_dev);
                FBConfig.setPushIconResource(R.drawable.ic_launcher);
                // qa jamba
                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
                FBConfig.setClient_tenantid("1173");
                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");

                LoyaltyApplication.instance.clpsdkObj = FBSdk.sharedInstance(this, Constants.sdkPointingUrl(Constants.QA));
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }


    private String getStringWithId(int id) {
        return getResources().getString(id);
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
