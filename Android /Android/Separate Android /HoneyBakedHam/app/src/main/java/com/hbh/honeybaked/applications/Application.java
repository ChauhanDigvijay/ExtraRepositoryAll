package com.hbh.honeybaked.applications;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdk.OnFBSdkRegisterListener;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBService;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.Constants;
import com.hbh.honeybaked.supportingfiles.TypefaceUtil;

public class Application extends MultiDexApplication implements OnFBSdkRegisterListener {
    private static boolean activityVisible;





    public void onCreate() {
        super.onCreate();
       // TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.consumerKey), getResources().getString(R.string.consumerSecret));
       // FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/ralewayregular.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/ralewaybold.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SANS", "fonts/ralewaymedium.ttf");
        initializeCLPSDK();
    }

    private void initializeCLPSDK() {
        try {
            FBService.initialize(this, Constants.sdkPointingUrl(10), "null");
            FBCustomerItem customer = new FBCustomerItem();
            FBConfig FBConfig = new FBConfig();
            FBConfig.setGcmSenderId("329847007167");
            FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
            FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB3");
            FBConfig.setClient_tenantid("581");
            FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A3");
            FBConfig.setPushIconResource(R.mipmap.ic_launcher);
            FBSdk.sharedInstanceWithKey(this, Constants.sdkPointingUrl(10), this, FBConfig, customer, Boolean.valueOf(false));
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }



    public void onFBRegistrationSuccess(FBCustomerItem fbCustomerItem) {
    }

    public void onFBRegistrationError(String s) {
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
