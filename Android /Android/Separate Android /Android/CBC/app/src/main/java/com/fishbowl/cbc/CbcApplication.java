package com.fishbowl.cbc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.fishbowl.apps.olo.Services.OloService;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.fishbowl.cbc.businesslogic.models.User;
import com.fishbowl.cbc.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VT027 on 4/22/2017.
 */

/**
 * Base Application class for CBC Application
 */

public class CbcApplication extends Application implements Application.ActivityLifecycleCallbacks, FBSdk.OnFBSdkRegisterListener {

    static CbcApplication sInstance;
    public FBSdk fbsdkObj;
    public int categoryProductDownloadQueue = 0;

    public final static String MSG = "LOG_MSG";


    /**
     * onCreate : creating instances and initializing all sdks and library used in app
     */

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        registerActivityLifecycleCallbacks(this);
        initializeFishbowlSDK();
        initializeOloSdk();

    }

    public static synchronized CbcApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    // Initialize Fishbowl SDK
    private void initializeFishbowlSDK() {
        FBService.initialize(this, Constants.sdkPointingUrl(Constants.FBSdkNewQA), "null");

        FBCustomerItem customer = new FBCustomerItem();
        //Check google play exist

        /**
         * TODO: uncomment when google play services is installed(1)
         */
        // if (checkPlayServices()) {
        //check user is already login
//            if (UserService.isUserAuthenticated()) {
//                User user = UserService.getUser();
//                customer = collectCustomerData(user);
//            }
        // CLP Registration
        FBConfig fbconfig = new FBConfig();

            /*
            commented for testing in bertuccis, available when key changed.
             */
        //fbconfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");

        //QA
        FBConfig.setClient_secret(getStringWithId(R.string.fb_client_secret));
        FBConfig.setClient_tenantid(getStringWithId(R.string.fb_tenant_id));
        FBConfig.setClient_id(getStringWithId(R.string.fb_client_id));

        fbconfig.setGcmSenderId("");
        //getString(R.string.gcm_sender_id_development));

        //fbconfig.setPushIconResource(R.drawable.jamba_header_logo);
        fbsdkObj = FBSdk.sharedInstanceWithKey(
                this, Constants.sdkPointingUrl(Constants.FBSdkNewQA), this, fbconfig, customer, false);
        // http://jamba.clpcloud.com/

        /**
         * TODO: uncomment when google play services is installed(2)
         */
//        } else {
//            try {
//                android.support.v7.app.AlertDialog.Builder alertdialog = new android.support.v7.app.AlertDialog.Builder(this);
//                alertdialog.setTitle("Error");
//                alertdialog.setMessage("Google Play Service is Missing")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                System.exit(0);
//                            }
//                        });
//                alertdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        System.exit(0);
//                    }
//                });
//                alertdialog.setCancelable(false);
//                alertdialog.show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    // Initialize OLO SDK
    private void initializeOloSdk() {
        OloService.initialize(this, getStringWithId(R.string.olo_base_url), getStringWithId(R.string.olo_api_key));

    }

    //Sign Up using Fishbowl SDK
    public void registerCustomer(User user) {

        FBCustomerItem customer = collectCustomerData(user);

        final JSONObject object = new JSONObject();
        try {

            object.put("firstName", customer.getFirstName());
            object.put("lastName", customer.getLastName());
            object.put("email", customer.getEmailID());
            object.put("phone", customer.getCellPhone());
            object.put("smsOptIn", user.isEnableSmsOpt());
            object.put("emailOptIn", user.isEnableEmailOpt());
            object.put("pushOptIn", user.isEnablePushOpt());
            object.put("addressState", customer.getAddressState());
            object.put("addressStreet", customer.getAddressLine1());
            object.put("addressCity", customer.getAddressCity());
            object.put("addressZipCode", customer.getAddressZip());
            object.put("favoriteStore", customer.getHomeStore());
            object.put("dob", user.getDob());
            object.put("gender", customer.getCustomerGender());
            object.put("username", customer.getEmailID());
            object.put("password", customer.getLoginPassword());
            object.put("deviceId", customer.getDeviceID());
            object.put("sendWelcomeEmail", "ss");
            object.put("loginID", customer.getLoginID());
            object.put("storeCode", user.getFavoriteStoreCode());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {
                Log.e(MSG, String.valueOf(response));
            }
        });
    }

    // Collect customer from user and assign to fbcustomeritem
    private FBCustomerItem collectCustomerData(User user) {
        SharedPreferences shared = getSharedPreferences("HomeActivity", Context.MODE_PRIVATE);

        FBCustomerItem customer = new FBCustomerItem();
        customer.firstName = user.getFirstname();
        customer.lastName = user.getLastname();
        customer.emailID = user.getEmailaddress();
        customer.customerAge = "";
        customer.customerGender = "";
        customer.cellPhone = user.getContactnumber();
        customer.loyalityNo = "";
        customer.addressLine1 = "";
        customer.addressLine2 = "";
        customer.addressCity = "";
        customer.addressState = "";
        customer.addressZip = "";
        customer.favoriteDepartment = "";
        customer.customerTenantID = "";
        customer.statusCode = 1;
        customer.deviceId = FBUtility.getAndroidDeviceID(getInstance());
        customer.dateOfBirth = user.getDob();
        //  customer.homeStore = user.getFavoriteStoreCode();
        if (StringUtilities.isValidString(user.getFavoriteStoreCode())) {
            int storecode = Integer.parseInt(user.getFavoriteStoreCode());
            customer.homeStore = Integer.toString(storecode);

        } else {
            int storecode = 145; //Default store - Emeryville
            customer.homeStore = Integer.toString(storecode);
        }
//        if(StringUtilities.isValidString(customer.homeStore)) {
//            int storecode=Integer.parseInt(customer.getHomeStore());
//
//            if (shared.getInt(String.valueOf(storecode), 0) > 0) {
//                customer.homeStoreID = String.valueOf(shared.getInt(String.valueOf(storecode), 0));
//            }
//        }
        customer.pushOpted = user.isEnablePushOpt() ? "Y" : "N";
        customer.smsOpted = user.isEnableSmsOpt() ? "Y" : "N";
        customer.emailOpted = user.isEnableEmailOpt() ? "Y" : "N";
        customer.phoneOpted = "N";
        customer.adOpted = "N";
        customer.loyalityRewards = String.valueOf(user.getTotalPoints());
        return customer;
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                //googleApiAvailability.getErrorDialog(context, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private String getStringWithId(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onFBRegistrationSuccess(FBCustomerItem fbCustomerItem) {

    }

    @Override
    public void onFBRegistrationError(String s) {

    }
}
