package com.fishbowl.LoyaltyTabletApp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.fishbowl.LoyaltyTabletApp.Utils.Constants;
import com.fishbowl.LoyaltyTabletApp.Utils.Customfont;
import com.fishbowl.loyaltymodule.Controllers.FBSdk;
import com.fishbowl.loyaltymodule.Models.FBConfig;
import com.fishbowl.loyaltymodule.Models.FBCustomerItem;
import com.fishbowl.loyaltymodule.Services.FBService;
import com.fishbowl.loyaltymodule.Utils.FBConstant;

import io.fabric.sdk.android.Fabric;

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
        Customfont.setDefaultFont(this, "MONOSPACE", "Proxima_Regular.ttf");
        initializeCrashlytics();
        initializeCLPSDK();
    }

    private void initializeCrashlytics() {

        try {
            Fabric.with(this, new Crashlytics());

        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here!
        }
    }

    private void initializeCLPSDK() {
        try {
            FBService.initialize(this, Constants.sdkPointingUrl(Constants.FBSdkHBH_STG), "null");
            FBCustomerItem customer = new FBCustomerItem();
            {
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
                FBConfig.setGcmSenderId(FBConstant.gcm_sender_id_dev);
                FBConfig.setPushIconResource(R.drawable.logomain);
                LoyaltyApplication.instance.clpsdkObj = FBSdk.sharedInstance(this, Constants.sdkPointingUrl(Constants.FBSdkHBH_STG));
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }


    private String getStringWithId(int id) {
        return getResources().getString(id);
    }


    public void onCLPRegistrationSuccess(FBCustomerItem FBCustomerItem) {
    }

    public void onCLPRegistrationError(String error) {
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
