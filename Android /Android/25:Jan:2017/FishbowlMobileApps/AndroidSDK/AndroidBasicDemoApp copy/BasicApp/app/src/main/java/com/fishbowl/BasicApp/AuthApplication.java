
package com.fishbowl.BasicApp;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FontsManager;
import com.crashlytics.android.Crashlytics;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceCallback;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBRestaurantListItem;
import com.fishbowl.basicmodule.Services.FBRestaurantService;
import com.fishbowl.basicmodule.Services.FBService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by digvijay(dj)
 */
public class AuthApplication extends Application implements Application.ActivityLifecycleCallbacks, FBSdk.OnFBSdkRegisterListener {
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    public FBSdk clpsdkObj;

    static AuthApplication instance;
    Boolean sdk=true;


    @Override
    public void onCreate() {
        super.onCreate();
       // initializeCrashlytics();
        instance = this;
        FontsManager.setDefaultFont(this, "MONOSPACE", "fonts/helvetica_regular.ttf");
        FontsManager.sharedInstance(this);
        initializeCLPSDK();
        printHashKey();

    }
    public static AuthApplication getAppContext()
    {
        return instance;
    }

    private void initializeCrashlytics() {

        try {
            Fabric.with(this, new Crashlytics());

        } catch (Exception e) {
            Crashlytics.logException(e);
        }


    }



    public void printHashKey(){
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

            FBService.initialize(this, Constants.sdkPointingUrl(Constants.Test),"null");

            FBCustomerItem customer = new FBCustomerItem();
            {
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
                FBConfig.setPushIconResource(R.drawable.ic_launcher);
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A5");
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB8");
//                FBConfig.setClient_tenantid("1173");

////                // qa jamba
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
//                FBConfig.setClient_tenantid("1173");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");

                 ////hbh
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB3");
//                FBConfig.setClient_tenantid("581");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A3");

               // test
                FBConfig.setClient_secret("C65A0DC0F28C469FB7336D972DEFBCB9");
                FBConfig.setClient_tenantid("1301");
                FBConfig.setClient_id("201969E1BFD242W189FE7D6297B1B5A4");



                FBConfig.setGcmSenderId("503777186107");
                FBConfig.setPushIconResource(R.mipmap.ic_launcher);
                FBConfig.setSmallpushIconResource(R.drawable.ic_launcher);

                AuthApplication.instance.clpsdkObj = FBSdk.sharedInstanceWithKey(this, Constants.sdkPointingUrl(Constants.Test), this, FBConfig, customer,false);
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }

    private String getStringWithId(int id) {
        return getResources().getString(id);
    }



    @Override
    public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem) {

            FBRestaurantService.getAllRestaurants(new FBRestaurantServiceCallback() {
                @Override
                public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
                    if (error != null)

                    {


                    } else {

                    }
                }
            });


    }

    public void onFBRegistrationError(String error){

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
            FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_CLOSE);
        }
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        return resumed > paused;
    }




}
