
package com.fishbowl.BasicApp;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


/**
 * Created by digvijay(dj)
 */
public class AuthApplication extends Application implements Application.ActivityLifecycleCallbacks,FBSdk.OnFBSdkRegisterListener {
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    public FBSdk fbsdkObj;
    static AuthApplication instance;
    Boolean sdk=true;
    public static AuthApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());
        initializeCrashlytics();
        instance = this;
        Customfont.setDefaultFont(this, "MONOSPACE", "BistroRegular.ttf");
        initializeCLPSDK();
        printHashKey();
    }



    private void initializeCrashlytics() {

        try {
            Fabric.with(this, new Crashlytics());

        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here!
        }


    }


    public void printHashKey(){
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

            FBService.initialize(this, Constants.sdkPointingUrl(Constants.ClpSdkNewApi),"null");
            FBCustomerItem customer = new FBCustomerItem();

            //if (FBUtility.checkPlayServices(this))
            {
                //check user is already login
                /*if (USer.isUserAuthenticated()) {
                    User user = UserService.getUser();
                    customer = collectCustomerData(user);
                }*/
                // CLP Registration
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
                //stg_hbh
                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBC17");
                FBConfig.setClient_tenantid("10051");
                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B517");


//                //sea_island stagging
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCA2");
//                FBConfig.setClient_tenantid("1702");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5B2");

                //sea_island  stagging
//	public static String client_id ="201969E1BFD242E189FE7B6297B1B5B2";//selisland - stg
//	public static String client_secret ="C65A0DC0F28C469FB7376F972DEFBCA2"; //selisland - stg
//public static String  client_tenantid="1702";// for seaisland
                //          https://stg-seaisland.fishbowlcloud.com/clpapi

////                //qa_jamba
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
//                FBConfig.setClient_tenantid("1173");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");


                //staging jamba
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
//                FBConfig.setClient_tenantid("1173");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A9");

                //prodution jamba
//                FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB8");
//                FBConfig.setClient_tenantid("1173");
//                FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A5");


                FBConfig.setGcmSenderId("503777186107");
                FBConfig.setPushIconResource(R.mipmap.ic_launcher);
                FBConfig.setSmallpushIconResource(R.drawable.test);
                //clpsdkObj = FBSdk.sharedInstance(this, Constants.sdkPointingUrl(Constants.portalstagingdeltaco));

                fbsdkObj = FBSdk.sharedInstanceWithKey(this, Constants.sdkPointingUrl(Constants.ClpSdkNewApi), this, FBConfig, customer,false);
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
//        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
//            @Override
//            public void OnAllStoreCallback(JSONObject response, Exception error) {
//
//                if(response!=null){
//
//
//                }
//
//            }
//        });
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
        if(created==0){

        }
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        return resumed > paused;
    }




}
