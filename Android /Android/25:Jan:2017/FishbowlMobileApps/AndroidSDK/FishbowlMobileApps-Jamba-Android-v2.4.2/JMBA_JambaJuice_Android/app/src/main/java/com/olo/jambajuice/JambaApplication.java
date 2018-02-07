package com.olo.jambajuice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.crashlytics.android.Crashlytics;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.Member;
import com.Preferences.FBPreferences;

import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.Config;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
//import com.instabug.library.Instabug;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;

import com.olo.jambajuice.Activites.NonGeneric.Settings.PushNotificationActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.FBSDKLoginServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.Feedback;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderProduct;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.Location.LocationManager;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.Utils;
import com.parse.Parse;
import com.parse.ParseObject;
import com.wearehathway.apps.incomm.Interfaces.InCommUserProfileServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommSDKConfiguration;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Models.InCommUserProfile;
import com.wearehathway.apps.incomm.Services.InCommService;
import com.wearehathway.apps.incomm.Services.InCommUserService;
import com.wearehathway.apps.olo.Services.OloService;
import com.wearehathway.apps.spendgo.Services.SpendGoService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class JambaApplication extends Application implements Application.ActivityLifecycleCallbacks, FBSdk.OnFBSdkRegisterListener, FBSdk.OnFBCustomerRegisterListener {

    public static String information;
    public static String version;
    public static String versioncode;
    public static int FREQUENCY;
    public static String forceupdate;
    static JambaApplication instance;
    public FBSdk fbsdkObj;
    public int categoryProductDownloadQueue = 0;
    SharedPreferences sharefequency;
    AlertDialog.Builder blueToothAlertBuilder;
    private int created;
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    private BroadcastReceiver mRegistrationBroadcastReceiver;//GCM
    private BroadcastReceiver mMessageReceived;//GCM
    private String mPushToken;
    private Store selectedStore;
    public Location mCurrentLocation;

    public static String getForceupdate() {
        return forceupdate;
    }

    public static void setForceupdate(String forceupdate) {
        JambaApplication.forceupdate = forceupdate;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        JambaApplication.version = version;
    }

    public static String getVersionCode() {
        return versioncode;
    }

    public static void setVersionCode(String versionCode) {
        JambaApplication.versioncode = versionCode;
    }

    public static String getInformation() {
        return information;
    }

    public static void setInformation(String message) {
        JambaApplication.information = message;
    }

    public static int getFREQUENCY() {
        return FREQUENCY;
    }

    public static void setFREQUENCY(int FREQUENCY) {
        JambaApplication.FREQUENCY = FREQUENCY;
    }

    public static JambaApplication getAppContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(this);
        initializeThirdPartySdks();
        initializeAppServiceSdks();
        UserService.loadSession();
        initializeFBSDK();
        if (checkPlayServices()) {
            trackFirstLaunch();
        }
        updateAndroidSecurityProvider();
        pushNotificationRecieve();
    }

    public void setmCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    //    private void startLocationUpdate() {
//        LocationManager locationManager = LocationManager.getInstance(this);
//        if (locationManager.isLocationServicesEnabled()) {
//            LocationManager.getInstance(this).startLocationUpdates(new LocationUpdatesCallback() {
//                @Override
//                public void onLocationCallback(Location location) {
//                    mCurrentLocation = location;
//                }
//
//                @Override
//                public void onConnectionFailedCallback() {
//                    LocationManager.getInstance(getApplicationContext()).stopLocationUpdates();
//                }
//            });
//        } else {
//            //Notify user that location service is not enabled
//            locationManager.showLocationServiceNotAvailableAlert();
//        }
//    }

    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            //GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), , 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    public void initializeInCommSDK() {
        InCommSDKConfiguration configuration = new InCommSDKConfiguration();
        configuration.baseUrl = getStringWithId(R.string.incomm_base_url);
        configuration.clientId = getStringWithId(R.string.incomm_client_id);
        configuration.applicationToken = DataManager.getInstance().getInCommToken();
        configuration.brandId = getStringWithId(R.string.incomm_brand_id);
        InCommService.initialize(instance, configuration);

    }

    private void trackFirstLaunch() {
        boolean isFirstLaunch = SharedPreferenceHandler.getBoolean(SharedPreferenceHandler.FirstLaunch, true);
        if (isFirstLaunch) {
            //make null exception to open this
            // Not sure about this code, move track code to below
//            FBMobileSettingService.sharedInstance().getMobileSetting("0", new FBMobileSettingService.FBMobileSettingCallback() {
//                @Override
//                public void OnFBMobileSettingCallback(boolean b, Exception e) {
//                    //startLocationUpdate();
//                    JambaAnalyticsManager.sharedInstance().track_EventForGuestByName(FBEventSettings.FIRST_TIME_LAUNCH);
//
//                }
//            });

            JambaAnalyticsManager.sharedInstance().track_EventForGuestByName(FBEventSettings.FIRST_TIME_LAUNCH);

            AnalyticsManager.getInstance().trackEvent("application", "first_time_launch");
            SharedPreferenceHandler.put(SharedPreferenceHandler.FirstLaunch, false);
            AnalyticsManager.getInstance().trackEvent("application", "did_launch");

        }
    }

    private void initializeThirdPartySdks() {

        initializeParse();
        //initializeInstaBug();
      //  initializeAppsFlyer(); //Enable it for Live use
        initializeCrashlytics();//temp dev fix
        //initializeUrbanAirship();// removed urbanairship
    }

    private void initializeCrashlytics() {
        try {
            Fabric.with(this, new Crashlytics());
        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here!
        }
    }

    public void deviceUpdate() {
        //  if(FBPreferences.sharedInstance(this).getPushToken() != null) {

        if (FBPreferences.sharedInstance(this).getPushToken() != null) {
            SharedPreferenceHandler.put(SharedPreferenceHandler.PushToken, FBPreferences.sharedInstance(this).getPushToken());
        }


        Log.d("FB_SDK_DeviceUpdate", "Device update started.");
        JSONObject object = new JSONObject();
        try {
            //Skip for Guest user
            if (FBPreferences.sharedInstance(this).getUserMemberforAppId() == null) {
                Log.d("FB_SDK_DeviceUpdate", "Device update ignored for guest user");
                return;
            }
            object.put("memberid", FBPreferences.sharedInstance(this).getUserMemberforAppId());
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOSVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("appId", BuildConfig.APPLICATION_ID);
            // Is Logged-In user and push-opt-in enabled
            if (FBPreferences.sharedInstance(getApplicationContext()).getUserMemberforAppId() != null && FBPreferences.sharedInstance(getApplicationContext()).IsPushOptIn()) {
                object.put("pushToken", SharedPreferenceHandler.getString(SharedPreferenceHandler.PushToken, null));
            } else {
                object.put("pushToken", "");
            }

            Log.d("FB_SDK_DeviceUpdate", object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JAMBAAPP", "push token: " + SharedPreferenceHandler.getString(SharedPreferenceHandler.PushToken, null));

        FBUserService.sharedInstance().deviceUpdateffojamba(object, new FBUserService.FBDeviceUpdateCallback() {
            @Override
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {

                if (response != null) {
                    try {
                        Log.d("FB_SDK_DeviceUpdate", response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("FB_SDK", "DeviceUpdate failed");
                }
            }
        });
//        }else{
//            FBSdk.sharedInstance(getApplicationContext()).initialation(this,Constants.sdkPointingUrl(Constants.getEnvironment()),this,Constants.getFBConfig(Constants.getEnvironment()),null);
//        }
    }


    private void initializeAppsFlyer() {
        AppsFlyerLib.setAppsFlyerKey(getStringWithId(R.string.appsflyer_key));
        AppsFlyerLib.sendTracking(this);
    }


    public void initializeFBSDK() {
        try {
            FBService.initialize(this, Constants.sdkPointingUrl(Constants.getEnvironment()), "null");

            FBCustomerItem customer = new FBCustomerItem();
            //Check google play exist
            if (checkPlayServices()) {
                //check user is already login
                if (UserService.isUserAuthenticated()) {
                    User user = UserService.getUser();
                    customer = collectCustomerData(user);
                }

                fbsdkObj = FBSdk.sharedInstanceWithKey(
                        this, Constants.sdkPointingUrl(Constants.getEnvironment()), this, Constants.getFBConfig(Constants.getEnvironment()), customer, false);
            } else {
                try {
                    android.support.v7.app.AlertDialog.Builder alertdialog = new android.support.v7.app.AlertDialog.Builder(this);
                    alertdialog.setTitle("Error");
                    alertdialog.setMessage("Google Play Service is Missing")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.exit(0);
                                }
                            });
                    alertdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            System.exit(0);
                        }
                    });
                    alertdialog.setCancelable(false);
                    alertdialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }

    }


    private FBCustomerItem collectCustomerData(User user) {
        SharedPreferences shared = getSharedPreferences("HomeActivity", Context.MODE_PRIVATE);


        FBCustomerItem customer = new FBCustomerItem();
        customer.firstName = user.getFirstname();
        customer.lastName = user.getLastname();
        customer.emailID = user.getEmailaddress();
        customer.loginID = user.getSpendGoId();
        customer.customerAge = "";
        customer.customerGender = "";
        customer.cellPhone = user.getContactnumber();
        if (user.getSpendGoId() != null) {
            customer.memberid = Integer.parseInt(user.getSpendGoId());
        }
        customer.loyalityNo = "";
        customer.addressLine1 = "";
        customer.addressLine2 = "";
        customer.addressCity = "";
        customer.addressState = "";
        customer.addressZip = "";
        customer.favoriteDepartment = "";
        customer.customerTenantID = "";
        customer.statusCode = 1;
        customer.deviceId = FBUtility.getAndroidDeviceID(getAppContext());
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


    //Login FBSDK user
    public void loginFb(final User user, final FBSDKLoginServiceCallBack callBack) {

        final JSONObject object = new JSONObject();
        try {
            object.put("username", user.getEmailaddress());
            object.put("deviceId", FBUtility.getAndroidDeviceID(getAppContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (StringUtilities.isValidString(user.getSpendGoId())) {
            FBPreferences.sharedInstance(this).setExternalCustomerIdforapp(user.getSpendGoId());
            FBPreferences.sharedInstance(this).setSpendGoAuthTokenforapp(user.getSpendGoAuthToken());
        }
        String signature = SpendGoService.getInstance().getSignature(user.getSpendGoId());
        FBPreferences.sharedInstance(getAppContext()).setClassSignatureforapp(signature);

        FBUserService.sharedInstance().loginMemberforjamba(object, getStringWithId(R.string.spendgo_base_url), new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        boolean successFlag = response.getBoolean("successFlag");
                        if (successFlag) {
                            String accessTokenForApp = response.getString("accessToken");
                            FBPreferences.sharedInstance(instance).setAccessTokenforapp(accessTokenForApp);
                            getMember(callBack);
                        } else {
                            callBack.fbSdkLoginCallBack(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    callBack.fbSdkLoginCallBack(false);
                }

            }
        });


    }

//    public void loginFb(User user,final FBSDKLoginServiceCallBack callBack) {
//
//        FBCustomerItem customer = collectCustomerData(user);
//
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("firstName", customer.getFirstName());
//            object.put("lastName", customer.getLastName());
//            object.put("email", customer.getEmailID());
//            object.put("phone", customer.getCellPhone());
//            object.put("smsOptIn", "false");
//            object.put("emailOptIn", "true");
//            object.put("pushOptIn", "true");
//            object.put("addressState", customer.getAddressState());
//            object.put("addressStreet", customer.getAddressLine1());
//            object.put("addressCity", customer.getAddressCity());
//            object.put("addressZipCode", customer.getAddressZip());
//            object.put("favoriteStore", customer.getHomeStore());
//            object.put("dob", customer.getDateOfBirth());
//            object.put("gender", customer.getCustomerGender());
//            object.put("username", customer.getEmailID());
//            object.put("password", customer.getLoginPassword());
//            object.put("deviceId",customer.getDeviceID());
//            object.put("sendWelcomeEmail", "ss");
//            object.put("loginID", customer.getLoginID());
//            object.put("storeCode", user.getFavoriteStoreCode());
//            if(StringUtilities.isValidString(user.getSpendGoId())) {
//                object.put("externalCustomerId", user.getSpendGoId());
//                FBPreferences.sharedInstance(this).setExternalCustomerIdforapp(user.getSpendGoId());
//                FBPreferences.sharedInstance(this).setSpendGoAuthTokenforapp(user.getSpendGoAuthToken());
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //String signature = "OXFwFRgjHGC42h68YIKQUZCXEeh4owwVsldBSSFwwaI=";
//        String signature = SpendGoService.getInstance().getSignature(user.getSpendGoId());
//        FBPreferences.sharedInstance(getAppContext()).setClassSignatureforapp(signature);
//
//        FBUserService.sharedInstance().loginMemberforjamba(object, new FBUserService.FBLoginMemberCallback(){
//            public void onLoginMemberCallback(JSONObject response, Exception error){
//                if(response!=null) {
//                    try {
//                        boolean successFlag = response.getBoolean("successFlag");
//                        if(successFlag) {
//                            String accessTokenForApp = response.getString("accessToken");
//                            FBPreferences.sharedInstance(instance).setAccessTokenforapp(accessTokenForApp);
//                            callBack.fbSdkLoginCallBack(true);
//                            getMember();
//                        }else{
//                            callBack.fbSdkLoginCallBack(false);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }else{
//                    callBack.fbSdkLoginCallBack(false);
//                }
//
//            }
//        });
//
//
//    }

    //Sign Up FBSDK
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
            if (StringUtilities.isValidString(user.getSpendGoId())) {
                object.put("externalCustomerId", user.getSpendGoId());
                FBPreferences.sharedInstance(this).setExternalCustomerIdforapp(user.getSpendGoId());
                FBPreferences.sharedInstance(this).setSpendGoAuthTokenforapp(user.getSpendGoAuthToken());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().createMemberforjamba(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {

            }
        });


    }


    public void getMember(final FBSDKLoginServiceCallBack callback) {
        FBSdk fbSdk = FBSdk.sharedInstance(getAppContext());
        final FBSdkData FBSdkData = fbSdk.getFBSdkData();
        final JSONObject object = new JSONObject();
        final Member member = FBUserService.sharedInstance().member;
        FBUserService.sharedInstance().getMemberforjamba(object, FBPreferences.sharedInstance(this).getExternalCustomerIdforapp(), new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        boolean successFlag = response.has("successFlag") ? response.getBoolean("successFlag") : false;
                        if (successFlag) {
                            member.initWithJson(response, getAppContext());
                            String pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
                            String deviceId = response.has("deviceId") ? response.getString("deviceId") : "";

                            if (com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(pushToken) && com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(deviceId)) {
                                FBPreferences.sharedInstance(getAppContext()).setDeviceId(deviceId);
                                FBPreferences.sharedInstance(getAppContext()).setPushToken(pushToken);
                            }
                            Long memberId = response.has("customerID") ? response.getLong("customerID") : 0;

                            FBSdkData.currCustomer = new FBCustomerItem();
                            FBSdkData.currCustomer.tenantid = response.has("customerTenantID") ? Long.parseLong(response.getString("customerTenantID")) : 0;
                            FBSdkData.currCustomer.memberid = response.has("customerID") ? Long.parseLong(response.getString("customerID")) : 0;
                            FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
                            FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
                            FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
                            FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
                            FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
                            FBSdkData.currCustomer.pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
                            FBSdkData.currCustomer.pushOpted = response.has("pushOpted") ? response.getString("pushOpted") : "";

                            //Log.d("fbSdk save cust persis :",FBSdkData.currCustomer.toString());
                            //memberId = Long.parseLong("2173431557");
                            //FBSdkData.currCustomer.memberid = memberId;
                            FBSdkData.setCustomer(FBSdkData.currCustomer);
                            // FBPreferences.sharedInstance(getApplicationContext()).setPushToken(FBSdkData.currCustomer.pushToken);

                            //DataManager.getInstance().setPushedOpt(response.getBoolean("pushOpted"));
                            FBPreferences.sharedInstance(getApplicationContext()).setPushOptIn(response.has("pushOpted") ? response.getBoolean("pushOpted") : false);


                            FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(memberId);
                            deviceUpdate();
                            trackFirstLaunch();
                            callback.fbSdkLoginCallBack(true);
                        } else {
                            FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
                            callback.fbSdkLoginCallBack(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
                    callback.fbSdkLoginCallBack(false);
                }

            }
        });
    }

    public void updateFavouriteStore(User user) {
        FBCustomerItem customer = collectCustomerData(user);
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", FBPreferences.sharedInstance(getApplicationContext()).getUserMemberId());
            object.put("storeCode", customer.homeStore);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            FBUserService.sharedInstance().favourteStoreUpdate(object, new FBUserService.FBFavouriteStoreUpdateCallback() {
                @Override
                public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error) {
                    if (response != null) {

                    } else if ((FBUtility.getErrorDescription(error).equalsIgnoreCase(FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN))) {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerInfo(User user) {
        FBCustomerItem customer = collectCustomerData(user);
        final JSONObject object = new JSONObject();
        try {
            //	object.put("memberid", customer.memberid);
            object.put("firstName", customer.getFirstName());
            object.put("lastName", customer.getLastName());
            object.put("email", customer.getEmailID());
            object.put("emailOptIn", user.isEnableEmailOpt());
            object.put("phone", customer.getCellPhone());
            object.put("smsOptIn", user.isEnableSmsOpt());
            object.put("pushOptIn", user.isEnablePushOpt());
            object.put("addressStreet", customer.getAddressLine1());
            object.put("addressCity", customer.addressCity);
            object.put("addressZipCode", customer.getAddressZip());
            object.put("favoriteStore", customer.homeStore);// change jjdkksdlfsldjflsd kfjsdlknfl
            object.put("dob", customer.getDateOfBirth());
            object.put("gender", customer.getCustomerGender());
            object.put("username", "");
            //	object.put("password", customer.getLoginPassword());
            object.put("deviceId", customer.getDeviceID());
            object.put("sendWelcomeEmail", "ss");
            object.put("loginID", customer.getLoginID());
            object.put("storeCode", customer.homeStore);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            FBUserService.sharedInstance().memberUpdate(object, new FBUserService.FBMemberUpdateCallback() {
                @Override
                public void onMemberUpdateCallback(JSONObject response, Exception error) {
                    if (response != null) {
                        try {
                            //DataManager.getInstance().setPushedOpt(object.getBoolean("pushOptIn"));
                            FBPreferences.sharedInstance(getApplicationContext()).setPushOptIn(object.getBoolean("pushOptIn"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if ((FBUtility.getErrorDescription(error).equalsIgnoreCase(FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN))) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callIncommUpdateMethod(final User user) {
        try {
            if (GiftCardDataManager.getInstance().getInCommUser() == null) {
                //create incomm user
                Log.d("Update Incomm Profile", "Incomm user id not found");

                //testing
                if (InCommService.getInstance() == null) {
                    this.initializeInCommSDK();
                }

                InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
                    @Override
                    public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {

                        if (inCommUser != null) {
                            GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                            String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
                            updateIncommCustomerInfo(user);
                        }
                    }
                });

            } else {
                updateIncommCustomerInfo(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateIncommCustomerInfo(User user) {
        try {
            //update incomm user details
            String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            InCommUserService.updateUserProfileWithUserId(collectInCommProfileData(user, userId), new InCommUserProfileServiceCallBack() {
                @Override
                public void onUserProfileServiceCallback(InCommUserProfile inCommUserProfile, Exception exception) {
                    if (inCommUserProfile != null) {
                        GiftCardDataManager.getInstance().setInCommUserProfile(inCommUserProfile);
                        Log.d("Update Incomm Profile", "user profile updated successfully");
                    } else {
                        Log.d("Update Incomm Profile", "Incomm user profile update failed");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> collectInCommProfileData(User user, String userId) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Id", Long.parseLong(userId));
        parameters.put("FirstName", (user.getFirstname() != null) ? user.getFirstname() : "");
        parameters.put("LastName", (user.getLastname() != null) ? user.getLastname() : "");
        parameters.put("EmailAddress", (user.getEmailaddress() != null) ? user.getEmailaddress() : "");
        parameters.put("IsActive", true);
        Log.d("parameters", parameters.toString());
        return parameters;
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        boolean isPlayServiceAvailable = false;
//        if (resultCode == ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                isPlayServiceAvailable = true;
//            } else {
//                Log.i("Google Play Error", "This device is not supported.");
//                isPlayServiceAvailable = false;
//            }
//        }
//        return isPlayServiceAvailable;
//    }
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


    //Removed UrbanAirship
//    private void initializeUrbanAirship()
//    {
//        AirshipConfigOptions options = new AirshipConfigOptions();
//        String prodAppKey = getStringWithId(R.string.prod_urban_airship_app_key);
//        //Only set production to true if production key is available. (i.e. For Production Build)
//        if (prodAppKey.length() == 0)
//        {
//            options.developmentAppKey = getStringWithId(R.string.dev_urban_airship_app_key);
//            options.developmentAppSecret = getStringWithId(R.string.dev_urban_airship_app_secret);
//            options.inProduction = false;
//        }
//        else
//        {
//            options.productionAppKey = prodAppKey;
//            options.productionAppSecret = getStringWithId(R.string.prod_urban_airship_app_secret);
//            options.inProduction = true;
//        }
//        options.gcmSender = getStringWithId(R.string.google_api_project_number);
//        UAirship.takeOff(this, options);
//        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
//        UAirship.shared().getInAppMessageManager().setDisplayAsapEnabled(true); // Enabling display ASAP will attempt to display the in-app message on arrival(Returning to app from background).
//    }

    private void initializeParse() {
        // Enable Local Datastore.
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Feedback.class);
        ParseObject.registerSubclass(RecentOrderProduct.class);

        // Parse.initialize(this, getStringWithId(R.string.parse_application_id), getStringWithId(R.string.parse_client_key));

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getStringWithId(R.string.parse_application_id))
                .clientKey(getStringWithId(R.string.parse_client_key))
                .enableLocalDataStore()
                .server(getStringWithId(R.string.parse_url)) // The trailing slash is important.
                .build()
        );
    }

    private void initializeAppServiceSdks() {
        OloService.initialize(this, getStringWithId(R.string.olo_base_url), getStringWithId(R.string.olo_api_key));
        SpendGoService.initialize(this, getStringWithId(R.string.spendgo_base_url), getStringWithId(R.string.spendgo_mobile_url), getStringWithId(R.string.spendgo_xclass_key), getStringWithId(R.string.spendgo_secret_key));
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
        // Not sure, about the usage of below codes, So disable them
//        if (FBSdk.sharedInstance(activity.getApplicationContext()) != null) {
//            deviceUpdate();
//        }

        if (checkPlayServices()) {
            if (FBMobileSettingService.sharedInstance() != null && fbsdkObj != null) {
                FBMobileSettingService.sharedInstance().getMobileSetting("0", new FBMobileSettingService.FBMobileSettingCallback() {
                    @Override
                    public void OnFBMobileSettingCallback(boolean b, Exception e) {
                        if(DataManager.disableEvents) {
                            FBMobileSettingService.sharedInstance().mobileSettings.TRIGGER_APP_EVENTS = 0;
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        //Incase user pressed home button, hide basket flag.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (shouldHideBasketFlag()) {
                    BasketFlagViewManager.getInstance().removeBasketFlag();
                }
            }
        }, 300);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;


        hideBasketFlagIfNecessary();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        created--;
        if (created == 0) {
            if (FBPreferences.sharedInstance(getApplicationContext()).getAccessToken() != null) {
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_CLOSE);
            } else {
                Utils.getTokenAndSendEventsByName(this, FBEventSettings.APP_CLOSE);
            }
        }
        if (created == 0) {
            if (StringUtilities.isValidString(version) && StringUtilities.isValidString(forceupdate))
                if (Utils.getVersionNameOnly().equalsIgnoreCase(version) && forceupdate.equalsIgnoreCase("1")) {
                    sharefequency = getSharedPreferences("JambaApplication", Context.MODE_PRIVATE);
                    SharedPreferences.Editor keyValuesEditor = sharefequency.edit();
                    if (sharefequency.getInt("Frequency", 0) < FREQUENCY) {
                        keyValuesEditor.putInt("Frequency", sharefequency.getInt("Frequency", 0) + 1);
                        keyValuesEditor.commit();
                    }
                }

        }
    }

    private void hideBasketFlagIfNecessary() {
        if (shouldHideBasketFlag()) {
            AnalyticsManager.getInstance().sendPendingEvents();
            BasketFlagViewManager.getInstance().removeBasketFlag();
        }
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        return resumed > paused;
    }

    private boolean shouldHideBasketFlag() {
        return !isApplicationVisible() || !isApplicationInForeground();
    }


//    @Override
//    public void onFBCustomerRegistrationSuccess(FBCustomerItem FBCustomerItem) {
//        Log.i("CLP_SDK", "Customer Reg success");
//    }

    @Override
    public void onFBCustomerRegistrationSuccess(FBCustomerItem fbCustomerItem, Exception e) {
        Log.i("FB_SDK", "Customer Reg success");
    }

    @Override
    public void onFBCustomerRegistrationError(String error) {
        Log.i("FB_SDK", "Customer Reg error: " + error);
    }

    @Override
    public void onFBCustomerUpdateSuccess(JSONObject jsonObject, Exception e) {
        Log.i("FB_SDK", "SDK onFBCustomerUpdateSuccess");
    }

//    @Override
//    public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem) {
//        Log.i("FB_SDK", "SDK registration success");
//        // As of now, we dont use location service from fbsdk
//        //fbsdkObj.startLocationService();
//        deviceUpdate();
//    }

    @Override
    public void onFBRegistrationSuccess(String s) {
        Log.i("FB_SDK", "SDK registration success");
        SharedPreferenceHandler.put(SharedPreferenceHandler.PushToken,s);
        // As of now, we dont use location service from fbsdk
        //fbsdkObj.startLocationService();
         deviceUpdate();
    }

    @Override
    public void onFBRegistrationError(String error) {
        Log.i("FB_SDK", "SDK registration error: " + error);
    }

    public Store getSelectedStore() {
        return selectedStore;
    }

    public void setSelectedStore(Store store) {
        this.selectedStore = store;
    }

    private void pushNotificationRecieve() {
        // Broadcast Receiver for receiving token To get BroadCast
        BroadcastReceiver mMessageReceived = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

//                Intent i = intent;
//                Bundle extras = i.getExtras();
//                {
//                    if (extras != null) {
//
//                        String apsStr = i.getExtras().getString("aps");
//                        try {
//                            if (apsStr != null) {
//                                JSONObject apsJson = new JSONObject(apsStr);
//                                if (apsJson.has("alert")) {
//                                    String title = apsJson.getString("alert");
//                                    String offerid = "";
//                                    String custId = "";
//                                    String promoCode = "";
//                                    if (extras.getString("offerid") != null) {
//                                        offerid = extras.getString("offerid");
//                                    }
//
//
//                                    if (extras.getString("custid") != null) {
//                                        custId = extras.getString("custid");
//                                    }
//
//                                    if(extras.getString("pc") != null){
//                                        promoCode = extras.getString("pc");
//                                    }
//
//                                    long currCustId = FBPreferences.sharedInstance(context).getUserMemberId();
//                                    long offerCustId = Long.parseLong(custId);
//                                    if (currCustId == offerCustId) {
//                                        System.out.println("equals");
//                                    }
//
//                                    if (isApplicationVisible()) {
//                                        if (StringUtilities.isValidString(title)) {
//                                            if (StringUtilities.isValidString(custId)) {
//                                                if (currCustId == offerCustId) {
//                                                    Intent intent1 = new Intent(context, PushNotificationActivity.class);
//                                                    Bundle exs = new Bundle();
//                                                    exs.putString("Title", title);
//                                                    exs.putString("offerId", offerid);
//                                                    exs.putString("custId", custId);
//                                                    exs.putString("promoCode",promoCode);
//                                                    intent1.putExtras(exs);
//                                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    startActivity(intent1);
//                                                }
//                                            }
//                                        }
//
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

                Intent i = intent;
                Bundle extras = i.getExtras();
                {
                    if (extras != null) {

                        String data = i.getExtras().getString("datapayloadjson");
                        String title = i.getExtras().getString("message");
                        try {
                            if (data != null) {
                                JSONObject dataJson = new JSONObject(data);
                                String offerid = "";
                                String custId = "";
                                String promoCode = "";
                                if (dataJson.getString("offerid") != null) {
                                    offerid = dataJson.getString("offerid");
                                }


                                if (dataJson.getString("custid") != null) {
                                    custId = dataJson.getString("custid");
                                }

                                if (dataJson.getString("pc") != null) {
                                    promoCode = dataJson.getString("pc");
                                }

                                long currCustId = FBPreferences.sharedInstance(context).getUserMemberId();
                                long offerCustId = Long.parseLong(custId);
                                if (currCustId == offerCustId) {
                                    System.out.println("equals");
                                }

                                if (isApplicationVisible()) {
                                    if (StringUtilities.isValidString(title)) {
                                        if (StringUtilities.isValidString(custId)) {
                                            if (currCustId == offerCustId) {
                                                Intent intent1 = new Intent(context, PushNotificationActivity.class);
                                                Bundle exs = new Bundle();
                                                exs.putString("Title", title);
                                                exs.putString("offerId", offerid);
                                                exs.putString("custId", custId);
                                                exs.putString("promoCode", promoCode);
                                                intent1.putExtras(exs);
                                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent1);
                                            }
                                        }
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        };

        // Broadcast Receiver for receiving message
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceived,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    public void clearMemberId() {
        if (JambaApplication.getAppContext().fbsdkObj.getFBSdkData().getCurrCustomer() != null) {
            JambaApplication.getAppContext().fbsdkObj.getFBSdkData().setCustomer(null);
        }
        FBUserService.sharedInstance().member.customerID = null;
    }

}
