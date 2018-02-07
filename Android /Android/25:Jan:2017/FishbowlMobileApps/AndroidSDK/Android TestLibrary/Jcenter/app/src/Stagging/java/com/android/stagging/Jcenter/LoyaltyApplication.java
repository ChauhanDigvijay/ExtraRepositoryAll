package com.android.stagging.Jcenter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.android.Jcenter.R;
import com.android.jcenterlibrary.Controllers.FBSdk;
import com.android.jcenterlibrary.Models.FBConfig;
import com.android.jcenterlibrary.Services.FBService;
import com.android.jcenterlibrary.Utils.FBConstant;


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
            FBService.initialize(this, this.getResources().getString(R.string.fb_base_url), "null");

            {
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey(this.getResources().getString(R.string.fb_api_key));
                FBConfig.setGcmSenderId(FBConstant.gcm_sender_id_dev);
                FBConfig.setPushIconResource(R.drawable.ic_launcher);
                LoyaltyApplication.instance.clpsdkObj = FBSdk.sharedInstance(this, this.getResources().getString(R.string.fb_base_url));
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
