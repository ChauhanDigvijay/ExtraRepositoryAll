package com.fishbowl.basicmodule.Controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Gcm.FBRegistrationIntentService;
import com.fishbowl.basicmodule.Interfaces.FBSettingCallback;
import com.fishbowl.basicmodule.Models.FBBeaconsItem;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBDigitalEventListItem;
import com.fishbowl.basicmodule.Models.FBMobileSettingListItem;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBMainService;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBSettingService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by digvijay(dj)
 */
@SuppressLint("UseSparseArrays")
public class FBSdk {


    public static final String CLP_PUSH_MESSAGE_RECEIVED = "CLP_PUSH_MESSAGE_RECEIVED";
    public static final String CLP_REGISTRATION_COMPLETE = "CLP_REGISTRATION_COMPLETE";
    public static final String CLP_GCM_PAYLOAD = "CLP_GCM_PAYLOAD";
    public static final String FB_GCM_TOKEN = "FB_GCM_TOKEN";
    protected static final String TAG = "FBSdk";
    public static String SERVER_URL = "";// QA

    public static String OUTSIDE_REGION = "OUTSIDE_REGION";
    public static String INSIDE_REGION = "INSIDE_REGION";
    public static String INSIDE_STORE = "INSIDE_STORE";
    public static Context context;
    public static FBSdk instance;
    // callback
    public static String CLP_SETTINGS_UPDATE_CALLBACK = "CLP_SETTINGS_UPDATE_CALLBACK";
    public static String CLP_GEOFENCE_CALLBACK = "CLP_GEOFENCE_CALLBACK";
    public static String CLP_STARTBEACON_CALLBACK = "CLP_STARTBEACON_CALLBACK";
    public static String CLP_CALLBACK = "CLP_CALLBACK";
    public static FBConfig mFBConfig;
    private static int CONNECTION_TIMEOUT = 120000;
    private static String CLP_SDK_FILE_NAME = "fbSdk.txt";
    // use default
    public final double DEFAULT_LATITUDE = 38.577160;
    public final double DEFAULT_LONGITUDE = -121.495560;
    // current gps priority
    public String currentLocationRequest;
    public ArrayList<Geofence> mGeofenceList;
    public Map<Integer, FBStoresItem> storesMap = new HashMap<Integer, FBStoresItem>();
    public Map<Integer, Integer> storesMapforId = new HashMap<Integer, Integer>();
    public boolean beaconStarted;
    public Location mCurrentLocation;
    // /Mobile Settings
    public Date lastGeofenceStartCall;
    public Date lastLocationUpdateCall;
    public String mPushToken;
    public boolean isLocationService;
    List<FBBeaconsItem> allBeaconsForStores = new ArrayList<FBBeaconsItem>();
    int NOTIFICATION_ID = 0;
    Boolean sdk = false;
    private FBCustomerItem mCurrCustomer;
    // /persistant
    private File clpSdkDataFileDir;
    private File clpSdkDataFile;
    // GCM
    private BroadcastReceiver mRegistrationBroadcastReceiver;// GCM
    private BroadcastReceiver mMessageReceived;// GCM
    private String mPassDataUrl = "";


    public FBSdk(Context context, String sdkPointingUrl) {
        try {
            SERVER_URL = sdkPointingUrl;
            this.context = context;
            initfishbowlManager();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Constructor
    public FBSdk() {
    }

    // private OnFBSdkRegisterListener onCLPSdkRegisterListener;
    // private OnFBCustomerRegisterListener onCLPCustomerRegisterListener;

    //Constructor
    public FBSdk(Context context, String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener, FBConfig FBConfig, FBCustomerItem customer, Boolean sdk) {
        try {
            this.context = context;
            initManager();
            //   initloyaltyManager();
            this.mFBConfig = FBConfig;
            SERVER_URL = sdkPointingUrl;
            this.sdk = sdk;
            // check for GPS status
            checkGPSEnabledByUser();


            getToken(context, sdkPointingUrl, sdkListener, FBConfig, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static FBSdk sharedInstanceWithKey(Context ctx, String sdkPointingUrl,
                                              OnFBSdkRegisterListener listener, FBConfig FBConfig,
                                              FBCustomerItem customer, Boolean sdk) {
        if (instance == null) {
            instance = new FBSdk(ctx, sdkPointingUrl, listener, FBConfig, customer, sdk);
        }

        return instance;
    }

    public static FBSdk sharedInstance(Context ctx, String sdkPointingUrl) {
        if (instance == null) {
            instance = new FBSdk(ctx, sdkPointingUrl);
        }
        return instance;
    }

    //Singalton Object
    public static FBSdk sharedInstance(Context ctx) {

        return instance;
    }

    public static boolean urlExists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                    .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDeviceType() {
        return FBConstant.DEVICE_TYPE;
    }

    public static String getDevice_os_ver() {
        return FBConstant.device_os_ver;
    }

    public void initialation(Context context, String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener,
                             FBConfig FBConfig, FBCustomerItem customer) {

        // Broadcast Receiver for receiving token
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPushToken = intent.getStringExtra(FB_GCM_TOKEN);
                if (StringUtilities.isValidString(mPushToken)) {
                    //   registerCustomer(mCurrCustomer, sdkListener);
                    //FBSdkData.setGcmToken(mPushToken);
                    FBPreferences.sharedInstance(context).setPushToken(mPushToken);
                    sdkListener.onFBRegistrationSuccess(new FBCustomerItem());// we can used this callback when token is update again.
                } else {
                    sdkListener.onFBRegistrationError("GCM Registration Failed");
                }
            }
        };

        // Broadcast Receiver for Geofence
        mMessageReceived = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // do If TRIGGER_GEOFENCE is on
//                if (FBSdkData.mobileSettings.TRIGGER_PUSH_NOTIFICATION > 0) {
//                    createNotificationMessage(intent);
//
//                } else {
//
//                }


                createNotificationMessage(intent);
            }
        };

        //Broadcast Receiver for receiving token
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(CLP_REGISTRATION_COMPLETE));

        // Broadcast Receiver for receiving message
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceived, new IntentFilter(CLP_PUSH_MESSAGE_RECEIVED));

        // GCM Registration init
        this.mCurrCustomer = customer;
        Intent gcmRegIntent = new Intent(context, FBRegistrationIntentService.class);
        gcmRegIntent.putExtra("SENDER_ID", this.mFBConfig.getGcmSenderId());
        context.startService(gcmRegIntent);

    }

    public boolean checkAppEventFlag(){

            return true;
        }


    public void getToken(final Context context, final String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener, final FBConfig FBConfig, final FBCustomerItem customer) {
        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(context));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Manager initialization
    public void initManager() {

        FBStoreService.sharedInstance().init(this);
    //    FBLocationService.sharedInstance().init(this);
        FBViewMobileSettingsService.sharedInstance().init(this);
        FBMainService.sharedInstance().init(this);
        FBSessionService.sharedInstance().init(this);
        FBSettingService.sharedInstance().init(this);
        getAllMobileSetting();

    }

    //////////////////////////////////////////////////////////////////
    ////////////////////File related Function /////////////////////////

    //Manager initialization
    public void initfishbowlManager() {

        FBViewMobileSettingsService.sharedInstance().init(this);

    }


    public FBConfig getClpConfig() {
        return mFBConfig;
    }



    public Location getmCurrentLocation() {
        if (mCurrentLocation == null) {
            return getCachedLocation();
        } else {
            return mCurrentLocation;
        }
    }

    public void setmCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }
    //////////////////////////////////////////////////////////////////
    ////////////////////Notification Function /////////////////////////

    public boolean getIsLocationService() {

        if ((int) Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return isLocationService;
    }

    public Location getCachedLocation() {

        if (isLocationService) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..

                LocationManager manager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);

                Location cachedGPSLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);// cached
                // gps
                // location
                Location cachedPASSIVELocation = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);// cached
                // gps/wifi/cellular
                // location
                Location cachedNETWORKLocation = manager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);// cached
                // wifi/cellular
                // location

                if (cachedGPSLocation != null) {
                    // show cached gps location
                    return cachedGPSLocation;
                } else if (cachedPASSIVELocation != null) {
                    // show cached gps/wifi/cellular location
                    return cachedPASSIVELocation;
                } else if (cachedNETWORKLocation != null) {
                    // show cached wifi/cellular location
                    return cachedNETWORKLocation;
                } else {
                    // set default location
                    Location defaultLoc = new Location("DEFAULT");
                    defaultLoc.setLatitude(DEFAULT_LATITUDE);
                    defaultLoc.setLongitude(DEFAULT_LONGITUDE);
                    return defaultLoc;
                }
            } else {

                Location defaultLoc = new Location("DEFAULT");
                defaultLoc.setLatitude(DEFAULT_LATITUDE);
                defaultLoc.setLongitude(DEFAULT_LONGITUDE);
                return defaultLoc;
            }

        } else {

            Location defaultLoc = new Location("DEFAULT");
            defaultLoc.setLatitude(DEFAULT_LATITUDE);
            defaultLoc.setLongitude(DEFAULT_LONGITUDE);
            return defaultLoc;
        }

    }

    private void createNotificationMessage(Intent payloadIntent) {

        // Application Name
        String appName = FBUtility.getAppName(context);
        // Increment notification id
        NOTIFICATION_ID++;

        String message = "Message";
        String payloadString = payloadIntent.getStringExtra("aps");

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(payloadString);
            if (jsonObj.has("alert")) {
                message = jsonObj.getString("alert");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, FBActionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.putExtras(payloadIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // NotificationCompat.Builder notificationBuilder = new
        // NotificationCompat.Builder(
        // context).setSmallIcon(mRegId).setContentTitle("Jamba")
        // .setContentText(title).setAutoCancel(true)
        // .setSound(defaultSoundUri).setContentIntent(pendingIntent);

        //
        NotificationCompat.Builder notificationBuilder;
        // Lollipop fix
        if (Build.VERSION.SDK_INT < 20) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(mFBConfig.getPushIconResource())
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(appName)
                    .setContentText(message).setAutoCancel(true)
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText(message)).setTicker(appName)

                    .setContentIntent(pendingIntent).setSound(defaultSoundUri);
        } else {
            Bitmap largeIcon = BitmapFactory.decodeResource(
                    context.getResources(), mFBConfig.getPushIconResource());
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(mFBConfig.getPushIconResource())
                    .setLargeIcon(largeIcon)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(appName)
                    .setContentText(message).setAutoCancel(true)
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText(message)).setTicker(appName)
                    .setContentIntent(pendingIntent).setSound(defaultSoundUri);
        }

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager
                .notify(NOTIFICATION_ID, notificationBuilder.build());


    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? mFBConfig.getSmallpushIconResource() : mFBConfig.getSmallpushIconResource();
    }

    // Pass book start
    protected boolean openPassBook(Uri uri) {
        if (null != context && FBUtility.isNetworkAvailable(context)) {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                final String strPackageName = "com.attidomobile.passwallet";
                Intent startIntent = new Intent();
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.setAction(Intent.ACTION_VIEW);
                Intent passWalletLaunchIntent = packageManager
                        .getLaunchIntentForPackage(strPackageName);
                if (null == passWalletLaunchIntent) { // PassWallet isn't
                    // installed, open
                    // Google Play:
                    if (FBUtility.checkPlayServices(context)) {
                        String strReferrer = "";
                        try {
                            final String strEncodedURL = URLEncoder.encode(
                                    uri.toString(), "UTF-8");
                            strReferrer = "&referrer=" + strEncodedURL;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            strReferrer = "";
                        }
                        try {
                            startIntent.setData(Uri
                                    .parse("market://details?id="
                                            + strPackageName + strReferrer));
                            context.startActivity(startIntent);
                        } catch (android.content.ActivityNotFoundException anfe) {
                            // Google Play not installed, open via website
                            startIntent
                                    .setData(Uri
                                            .parse("http://play.google.com/store/apps/details?id="
                                                    + strPackageName
                                                    + strReferrer));
                            context.startActivity(startIntent);
                        }
                    }
                } else {
                    final String strClassName = "com.attidomobile.passwallet.activity.TicketDetailActivity";
                    startIntent.setClassName(strPackageName, strClassName);
                    startIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startIntent.setDataAndType(uri,
                            "application/vnd.apple.pkpass");
                    context.startActivity(startIntent);
                    return true;
                }
            }
        }
        return false;
    }

    public void openWeb(Uri uri) {
        Intent browserOpenIntent = new Intent(Intent.ACTION_VIEW).setData(uri);
        browserOpenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserOpenIntent);
    }


    ///////////////////////////////////////////////////
    //////////////Services ////////////////////////
    ///////////////////////////////////////////////////
//    public void mobileSettings(String cusId) {
//
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if(state)
//                    Log.d("Mobile Settings  Api","Success");
//                else
//                    Log.d("Mobile Settings Api","Fail");
//            }
//        });
//    }

    private void checkGPSEnabledByUser() {
        if (!FBUtility.isLocationServiceProviderAvailable(context)) {
        }
    }



    //services
    public void getAllMobileSetting() {
        FBSettingService.sharedInstance().getMobileSetting(new FBSettingCallback() {
            @Override
            public void onFBSettingCallback(FBMobileSettingListItem mobilesetting, FBDigitalEventListItem digitalevent, Exception error) {
                if (mobilesetting != null) {

                }
            }
        });
    }


    public boolean getGpsPermission() {
        // return FBSdkData.isGpsPermission();
        return true;
    }


    public String getAndroidOs() {
        return FBUtility.getAndroidOs();
    }

    public interface OnFBSdkRegisterListener {
        public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem);

        public void onFBRegistrationError(String error);
    }

    public interface OnFBCustomerRegisterListener {
        public void onFBCustomerRegistrationSuccess(FBCustomerItem FBCustomerItem, Exception error);

        public void onFBCustomerRegistrationError(String error);

        public void onFBCustomerUpdateSuccess(JSONObject response, Exception error);
    }

    public interface FBEventCallback {
        public void OnFBEventCallback(JSONArray response, Exception error);
    }


}