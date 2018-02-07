package com.fishbowl.BasicApp;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.crashlytics.android.Crashlytics;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


/**
 * Created by digvijay(dj)
 */
public class AuthApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, FBSdk.OnFBSdkRegisterListener {
    static AuthApplication instance;
    public FBSdk fbsdkObj;
    Boolean sdk = true;
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    public static AuthApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());
      //  initializeCrashlytics();
        instance = this;
        Customfont.setDefaultFont(this, "MONOSPACE", "BistroRegular.ttf");
        initializeCLPSDK();
        printHashKey();

        MultiDex.install(this);


    }


    private void initializeCrashlytics() {

        try {
            Fabric.with(this, new Crashlytics());

        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here!
        }


    }


    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fishbowl.BasicApp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initializeCLPSDK() {
        try {

            FBService.initialize(this, Constants.sdkPointingUrl(Constants.ClpSdkNewQA), "null");
            FBCustomerItem customer = new FBCustomerItem();


            {

                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");


                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
                FBConfig.setClient_tenantid("1173");
                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");


                fbsdkObj = FBSdk.sharedInstanceWithKey(this, Constants.sdkPointingUrl(Constants.ClpSdkNewQA), this, FBConfig, customer, false);
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }


    private String getStringWithId(int id) {
        return getResources().getString(id);
    }


    @Override
    public void onFBRegistrationSuccess(String FBCustomerItem) {
    //    deviceUpdate();
    }

    public void deviceUpdate() {


        JSONObject object = new JSONObject();

        Member member = FBUserService.sharedInstance().member;

        try {
            object.put("memberid", FBPreferences.sharedInstance(this).getUserMemberforAppId());
           // Constants.alertDialogShow(AuthApplication.this, "MemberID of user " + FBPreferences.sharedInstance(this).getUserMemberforAppId());
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
           // Constants.alertDialogShow(AuthApplication.this, "GCM Push token of user " + FBPreferences.sharedInstance(this).getPushToken());
            object.put("appId", "com.fishbowl.BasicApp");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().deviceUpdate(object, new FBUserService.FBDeviceUpdateCallback() {
            @Override
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    try {
                        Constants.alertDialogShow(AuthApplication.this, " AuthApplication deviceUpdate Success Message" + response);
                        //  EventCheck();
                        //  memberUpdate();
                        // getRefreshToken();

                        //updateGiftAndroidSavePay(collectGiftCardCreateRequest());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Constants.alertDialogShow(AuthApplication.this, "deviceUpdate Error Message");
                   // FBUtils.tryHandleTokenExpiry(AuthApplication.this, error);
                }
            }
        });
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
