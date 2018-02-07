package com.fishbowl.basicmodule.Controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
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
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Gcm.FBRegistrationIntentService;
import com.fishbowl.basicmodule.Interfaces.FBCommOrderIdCallback;
import com.fishbowl.basicmodule.Interfaces.FBCommTokenCallback;
import com.fishbowl.basicmodule.Interfaces.FBIncommResponseCallback;
import com.fishbowl.basicmodule.Interfaces.FBOfferSummaryCallback;
import com.fishbowl.basicmodule.Interfaces.FBOrderIdCallback;
import com.fishbowl.basicmodule.Interfaces.FBOrderValueCallback;
import com.fishbowl.basicmodule.Interfaces.FBOrderValuesCallback;
import com.fishbowl.basicmodule.Interfaces.FBUpdateInCommTransactionCallback;
import com.fishbowl.basicmodule.Interfaces.FBUpdateTransactionCallback;
import com.fishbowl.basicmodule.Interfaces.FbOfferPushCallback;
import com.fishbowl.basicmodule.Models.FBBeaconsItem;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBMobileEventsItem;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.R;
import com.fishbowl.basicmodule.Services.FBBeaconService;
import com.fishbowl.basicmodule.Services.FBBeaconService.FBBeaconsForStoreNoCallback;
import com.fishbowl.basicmodule.Services.FBCustomerService;
import com.fishbowl.basicmodule.Services.FBGiftService;
import com.fishbowl.basicmodule.Services.FBLocationService;
import com.fishbowl.basicmodule.Services.FBMainService;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBOfferService;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBTokenService;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by digvijay(dj)
 */
@SuppressLint("UseSparseArrays")
public class FBSdk {
    //public static String SERVER_URL = "http://jamba.clpcloud.com/clpapi/";// Jamba
    public static String SERVER_URL = "";// QA
    //public static String SERVER_URL = "http://demo.clpdemo.com/clpapi/";// Demo
    // public static String mobileAPIKey = "";

    // List<Beacons> allBeaconsForStores = new ArrayList<Beacons>();
    public static String PERSISTENT_ALL_DATA = "PERSISTENT_ALL_DATA";
    public static String PERSISTENT_CONTEXT = "PERSISTENT_CONTEXT";
    public static String PERSISTENT_FILE_NAME = "PERSISTENT_FILE_NAME";
    public static String PERSISTENT_CUSTOMER = "PERSISTENT_CUSTOMER";
    public static String PERSISTENT_GCM_TOKEN = "PERSISTENT_GCM_TOKEN";
    public static String PERSISTENT_CLP_CONFIG = "PERSISTENT_CLP_CONFIG";
    public static String PERSISTENT_ALL_BEACON_STORE_LIST = "PERSISTENT_ALL_BEACON_STORE_LIST";
    public static String PERSISTENT_ALL_SORTED_STORE_LIST = "PERSISTENT_ALL_SORTED_STORE_LIST";
    public static String PERSISTENT_MOBILE_SETTINGS = "PERSISTENT_MOBILE_SETTINGS";
    public static String PERSISTENT_EVENTS_SETTINGS = "PERSISTENT_EVENTS_SETTINGS";
    public static String PERSISTENT_BLUETOOTH_PERMISSION = "PERSISTENT_BLUETOOTH_PERMISSION";
    public static String PERSISTENT_GPS_PERMISSION = "PERSISTENT_GPS_PERMISSION";
    public static String PERSISTENT_LATITUDE = "PERSISTENT_LATITUDE";
    public static String PERSISTENT_LONGITUDE = "PERSISTENT_LONGITUDE";
    private static int CONNECTION_TIMEOUT = 120000;

    // current gps priority
    public String currentLocationRequest;
    public static String OUTSIDE_REGION = "OUTSIDE_REGION";
    public static String INSIDE_REGION = "INSIDE_REGION";
    public static String INSIDE_STORE = "INSIDE_STORE";

    public ArrayList<Geofence> mGeofenceList;
    public Map<Integer, FBStoresItem> storesMap = new HashMap<Integer, FBStoresItem>();
    public Map<Integer, Integer> storesMapforId = new HashMap<Integer, Integer>();
    protected static final String TAG = "FBSdk";
    public boolean beaconStarted;
    public static Context context;

    public static FBSdk instance;
    private FBCustomerItem mCurrCustomer;
    public Location mCurrentLocation;

    // /persistant
    private File clpSdkDataFileDir;
    private File clpSdkDataFile;
    private FBSdkData FBSdkData;
    private static String CLP_SDK_FILE_NAME = "fbSdk.txt";

    // /Mobile Settings
    public Date lastGeofenceStartCall;
    public Date lastLocationUpdateCall;

    // callback
    public static String CLP_SETTINGS_UPDATE_CALLBACK = "CLP_SETTINGS_UPDATE_CALLBACK";
    public static String CLP_GEOFENCE_CALLBACK = "CLP_GEOFENCE_CALLBACK";
    public static String CLP_STARTBEACON_CALLBACK = "CLP_STARTBEACON_CALLBACK";
    public static String CLP_CALLBACK = "CLP_CALLBACK";

    // use default
    public final double DEFAULT_LATITUDE = 38.577160;
    public final double DEFAULT_LONGITUDE = -121.495560;
    List<FBBeaconsItem> allBeaconsForStores = new ArrayList<FBBeaconsItem>();


    // GCM
    private BroadcastReceiver mRegistrationBroadcastReceiver;// GCM
    private BroadcastReceiver mMessageReceived;// GCM
    public String mPushToken;
    public static FBConfig mFBConfig;
    public static final String CLP_PUSH_MESSAGE_RECEIVED = "CLP_PUSH_MESSAGE_RECEIVED";
    public static final String CLP_REGISTRATION_COMPLETE = "CLP_REGISTRATION_COMPLETE";
    public static final String CLP_GCM_PAYLOAD = "CLP_GCM_PAYLOAD";
    public static final String FB_GCM_TOKEN = "FB_GCM_TOKEN";
    private String mPassDataUrl = "";
    int NOTIFICATION_ID = 0;
    Boolean sdk=false;
    public boolean isLocationService;



    public interface OnFBSdkRegisterListener {
        public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem);
        public void onFBRegistrationError(String error);
    }


    public interface OnFBCustomerRegisterListener {
        public void onFBCustomerRegistrationSuccess(FBCustomerItem FBCustomerItem, Exception error);
        public void onFBCustomerRegistrationError(String error);
        public void onFBCustomerUpdateSuccess(JSONObject response, Exception error);
    }

    // private OnFBSdkRegisterListener onCLPSdkRegisterListener;
    // private OnFBCustomerRegisterListener onCLPCustomerRegisterListener;

    public static FBSdk sharedInstanceWithKey(Context ctx, String sdkPointingUrl,
                                              OnFBSdkRegisterListener listener, FBConfig FBConfig,
                                              FBCustomerItem customer, Boolean sdk) {
        if (instance == null) {
            instance = new FBSdk(ctx,sdkPointingUrl, listener, FBConfig, customer,sdk);
        }

        return instance;
    }


    public static FBSdk sharedInstance(Context ctx, String sdkPointingUrl) {
        if (instance == null) {
            instance = new FBSdk(ctx,sdkPointingUrl);
        }
        return instance;
    }


    public FBSdk(Context context, String sdkPointingUrl) {
        try {
            SERVER_URL = sdkPointingUrl;
            this.context = context;
            initfishbowlManager();


            clpSdkDataFile = new File(clpSdkDataFileDir.getAbsolutePath() + "/" + CLP_SDK_FILE_NAME);
            try {
                if (clpSdkDataFile.exists() == false || FBSdkData.fileHasContent() == false) {
                    createNewDataFile(clpSdkDataFile);
                } else {
                    FBSdkData.refresh();
                }
            } catch (Exception e) {
                createNewDataFile(clpSdkDataFile);
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void initialation(Context context, String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener,
                             FBConfig FBConfig, FBCustomerItem customer)
    {
        //Mobile Setting Call
       // mobileSettings("0");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LocationServiceCheck.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_ONE_SHOT);
        am.cancel(pendingIntent);
        am.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);

        // Broadcast Receiver for receiving token
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPushToken = intent.getStringExtra(FB_GCM_TOKEN);
                if (StringUtilities.isValidString(mPushToken)) {
                    //   registerCustomer(mCurrCustomer, sdkListener);
                    FBSdkData.setGcmToken(mPushToken);
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
                if (FBSdkData.mobileSettings.TRIGGER_PUSH_NOTIFICATION > 0) {
                    createNotificationMessage(intent);
                    localLog("Push callback method", "TRIGGER_PUSH_NOTIFICATION is on");
                } else {
                    localLog("Push callback method", "TRIGGER_PUSH_NOTIFICATION is off");
                }
            }
        };

        //Broadcast Receiver for receiving token
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(CLP_REGISTRATION_COMPLETE));

        // Broadcast Receiver for receiving message
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceived,new IntentFilter(CLP_PUSH_MESSAGE_RECEIVED));

        // GCM Registration init
        this.mCurrCustomer = customer;
        Intent gcmRegIntent = new Intent(context,FBRegistrationIntentService.class);
        gcmRegIntent.putExtra("SENDER_ID", this.mFBConfig.getGcmSenderId());
        context.startService(gcmRegIntent);

    }


    //Singalton Object
    public static FBSdk sharedInstance(Context ctx) {

        return instance;
    }

    //Constructor
    public FBSdk() {
    }

    //Constructor
    public FBSdk(Context context, String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener, FBConfig FBConfig, FBCustomerItem customer, Boolean sdk) {
        try {
            this.context = context;
            initManager();
            //   initloyaltyManager();
            this.mFBConfig = FBConfig;
            SERVER_URL = sdkPointingUrl;
            this.sdk=sdk;
            // check for GPS status
            checkGPSEnabledByUser();

            // //PERSISTENT FILE///
            clpSdkDataFileDir = context.getApplicationContext().getFilesDir();

            FBSdkData = new FBSdkData(context.getApplicationContext(), CLP_SDK_FILE_NAME);

            String str=clpSdkDataFileDir.getAbsolutePath() + "/" + CLP_SDK_FILE_NAME;

            clpSdkDataFile = new File(clpSdkDataFileDir.getAbsolutePath() ,CLP_SDK_FILE_NAME);

            //   Log.d("Dj",str);
            try {
                if (clpSdkDataFile.exists() == false|| FBSdkData.fileHasContent() == false) {
                    createNewDataFile(clpSdkDataFile);
                } else {
                    FBSdkData.refresh();
                }
            } catch (Exception e) {
                createNewDataFile(clpSdkDataFile);
                e.printStackTrace();
            }
            getToken(context,sdkPointingUrl, sdkListener, FBConfig, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  getToken(final Context context, final String sdkPointingUrl, final OnFBSdkRegisterListener sdkListener, final FBConfig FBConfig, final FBCustomerItem customer)
    {
        JSONObject object=new JSONObject();
        try
        {
            object.put("clientId",FBConstant.client_id);
            object.put("clientSecret",FBConstant.client_secret);
            object.put("deviceId",FBUtility.getAndroidDeviceID(context));
            object.put("tenantId",FBConstant.client_tenantid);

        }catch (Exception e){
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback()
        {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if(response!=null) {
                    try {
                        String secratekey=response.getString("message");
                        FBPreferences.sharedInstance(instance.context).setAccessToken(secratekey);
                        instance.initialation(context,sdkPointingUrl, sdkListener, FBConfig, customer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    instance.initialation(context, sdkPointingUrl, sdkListener, FBConfig, customer);
                }

            }
        });
    }

    //Manager initialization
    public void initManager(){
        //  FBBeaconService.sharedInstance().init(this);
        FBStoreService.sharedInstance().init(this);
        FBCustomerService.sharedInstance().init(this);
        FBMobileSettingService.sharedInstance().init(this);
        FBLocationService.sharedInstance().init(this);
        FBUserService.sharedInstance().init(this);
        FBViewMobileSettingsService.sharedInstance().init(this);
        FBUserOfferService.sharedInstance().init(this);
        FBOfferService.sharedInstance().init(this);
        FBGiftService.sharedInstance().init(this);
        FBMainService.sharedInstance().init(this);
        FBSessionService.sharedInstance().init(this);
        FBTokenService.sharedInstance(this);
    }

    //Manager initialization
    public void initfishbowlManager(){
        //Service and manager
        FBUserService.sharedInstance().init(this);
        FBViewMobileSettingsService.sharedInstance().init(this);
        FBUserOfferService.sharedInstance().init(this);
    }

    //////////////////////////////////////////////////////////////////
    ////////////////////File related Function /////////////////////////

    private void createNewDataFile(File file) {
        try {
            file.createNewFile();
            FBSdkData.currCustomer = null;
            FBSdkData.allCLPBeaconStoreList = null;
            FBSdkData.allStoresList = null;
            FBSdkData.save(); // init
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public FBConfig getClpConfig(){
        return mFBConfig;
    }

    //////////////////////////////////////////////////////////////////
    ////////////////////Location Service Function /////////////////////////


    public void startLocationService() {
        // if (!isMyServiceRunning(LocationService.class, context)) {
        Intent intent = new Intent(context, LocationService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
        // } else {
        // Intent intent = new Intent();„
        // intent.setAction(CLP_CALLBACK);
        // intent.putExtra(CLP_CALLBACK, CLP_SETTINGS_UPDATE_CALLBACK);
        // context.sendBroadcast(intent);
        // }
    }

    public Location getmCurrentLocation() {
        if (mCurrentLocation == null) {
            return getCachedLocation();
        } else {
            return mCurrentLocation;
        }
    }
    public boolean getIsLocationService(){

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

        }
        else {

            Location defaultLoc = new Location("DEFAULT");
            defaultLoc.setLatitude(DEFAULT_LATITUDE);
            defaultLoc.setLongitude(DEFAULT_LONGITUDE);
            return defaultLoc;
        }

    }
    //////////////////////////////////////////////////////////////////
    ////////////////////Notification Function /////////////////////////

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

        localLog("Push callback method", "Dj notification recived");
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? mFBConfig.getSmallpushIconResource() : mFBConfig.getSmallpushIconResource();
    }



    public void processPushMessage(Intent intent) {
        localLog("processPushMessage :", intent.getExtras().toString());
        try {
            String alert = "";
            mPassDataUrl = intent.getStringExtra("url");// passbook url
            String ntype = intent.getStringExtra("ntype");
            String apsStr = intent.getExtras().getString("aps");
            JSONObject apsJson = new JSONObject(apsStr);
            if (apsJson.has("alert")) {
                alert = apsJson.getString("alert");// message
            }
            String couponid = "";
            // String eoid = "";
            // String custid = "";
            String clpnid = "";

            String sound_url = "";
            if (apsJson.has("sound")) {
                sound_url = apsJson.getString("sound");
                if (sound_url != null) {
                    sound_url = sound_url.toLowerCase(Locale.US);
                    sound_url = sound_url.replace(".wma", "");
                    sound_url = sound_url.replace(".wav", "");
                    sound_url = sound_url.replace(".caf", "");
                }
            }

            //  FBUtility.playAndroidSound(context, sound_url);
            if (intent.hasExtra("clpnid")) {
                clpnid = intent.getStringExtra("clpnid");// clp Notification Id
            }
            // if (intent.hasExtra("custid")) {
            // custid = intent.getStringExtra("custid");
            // }
            if (intent.hasExtra("offerid")) {
                couponid = intent.getStringExtra("offerid");
            }

            // url = "http://10.0.0.20/passbook/pass.pkpass";
            // mPassDataUrl = "http://192.168.1.25/testpush/pass1.pkpass";
            if (mPassDataUrl != null && mPassDataUrl.length() != 0) {
                // ntype = "";
                // url = "http://www.google.com";
                if (ntype == null || ntype.equals("pass") || ntype.equals("")) {
                    new Thread() {

                        public void run() {
                            if (urlExists(mPassDataUrl)) {
                                // djopenPassBook(Uri.parse(mPassDataUrl));
                            } else {
                            }
                        }
                    }.start();
                } else if (ntype.equals("web")) {
                    // dj openWeb(Uri.parse(mPassDataUrl));
                }

            }
            if (alert != null && alert.length() != 0) {
                // do not do anything
            }

            JSONObject obj = new JSONObject();
            obj.put("notifid", clpnid);
            obj.put("offerid", couponid);
            obj.put("memberid", FBSdkData.getCurrCustomer());
            obj.put("device_type", FBConstant.DEVICE_TYPE);
            obj.put("device_os_ver", FBUtility.getAndroidOs());
            obj.put("device_carrier", FBUtility.getDeviceCarier(context));
            obj.put("event_time", FBUtility.formatedCurrentDate());
            obj.put("channelId", 5);


            obj.put("event_name", "PUSH_OPEN");
            localLog("PushOpen : ", obj.toString());
            //   FBOfferService.sharedInstance().sendOfferEvent(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getInCommToken(final JSONObject tokenjson,final FBIncommResponseCallback callback)
    {
        FBGiftService.sharedInstance().getFBCommToken(tokenjson, new FBCommTokenCallback(){


            public void OnFBCommTokenCallback(JSONObject response, Exception error){

                if (error == null && response != null) {
                    try {
                        callback.OnFBIncommResponseCallback(response, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    callback.OnFBIncommResponseCallback(null, error);
                }
            }

        });
    }


    public void Ordervalue(final JSONObject orderjson,final FBOrderValuesCallback callback)
    {
        FBGiftService.sharedInstance().getFBOrdervalue(orderjson, new FBOrderValueCallback(){


            @Override
            public void OnFBOrderValueCallback(JSONObject response, Exception error){

                if (error == null && response != null) {
                    try {
                        if (callback != null) {
                            callback.OnFBOrderValuesCallback(response, null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    callback.OnFBOrderValuesCallback(null,error);
                }
            }

        });
    }

    public void getInCommOrderId(final JSONObject tokenjson, final FBOrderIdCallback callback)
    {

        FBGiftService.sharedInstance().getFBCommOrderId(tokenjson, new FBCommOrderIdCallback(){


            @Override
            public void OnFBCommOrderIdCallback(JSONObject response, Exception error){

                if (error == null && response != null) {
                    try {
                        if (callback != null) {
                            callback.OnFBOrderIdCallback(response,null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else

                {
                    callback.OnFBOrderIdCallback(null,error);
                }
            }

        });
    }

    public void updateInCommTransaction(final JSONObject tokenjson, final FBUpdateTransactionCallback callback)
    {

        FBGiftService.sharedInstance().FBupdateInCommTransaction(tokenjson,new FBUpdateInCommTransactionCallback(){




            @Override
            public void OnFBUpdateInCommTransactionCallback(JSONObject response, Exception error){

                if (error == null && response != null) {
                    try {
                        if (callback != null) {
                            callback.OnFBUpdateTransactionCallback(response,null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    callback.OnFBUpdateTransactionCallback(null,error);
                }
            }

        });
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

    private void checkGPSEnabledByUser() {
        if (!FBUtility.isLocationServiceProviderAvailable(context)) {
        }
    }


    public void getFBOffer(JSONObject offer, String customId, final FBOfferSummaryCallback callback) {
        FBOfferService.sharedInstance().getFBOffer(offer, customId, new FBOfferService.FBOfferCallback() {

            @Override
            public void OnFBOfferCallback(JSONObject response, String error) {
                callback.onClypOfferyCallback(response, error);
            }
        });

    }


    @SuppressWarnings("rawtypes")
    public void displayLocalPushNotification(String message, Class cls, int id,
                                             Context ctx) {
        if (getENABLE_LOCAL_NOTIFICATION() == 0)
            return;
        Context context = ctx;
        if (id == 0) {
            id = R.drawable.ic_launcher;
        }
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(id)
                .setContentTitle("Local Notification")
                .setStyle(
                        new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        // unique notification id
        // Tag Name of Notification
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int millisecond = cal.get(Calendar.MILLISECOND);
        int NOTIFICATION_ID = 1;
        StringBuilder MY_NOTIFICATION_ID = new StringBuilder(500);
        MY_NOTIFICATION_ID.append(year).append(month).append(dayofmonth)
                .append(hourofday).append(minute).append(minute).append(second)
                .append(millisecond);
        localLog("ID : ", "" + MY_NOTIFICATION_ID);
        String CURRENT_NOTIFICATION_STRING = MY_NOTIFICATION_ID.toString();
        mNotificationManager.notify(CURRENT_NOTIFICATION_STRING,
                NOTIFICATION_ID, mBuilder.build());
    }



    public void localLog(String label, String msg) {
        if (getENABLE_LOCAL_NOTIFICATION() == 0 && label == null && msg == null)
            return;
        Log.i(label, msg);
    }

    /*
    Used to update event to server rather then using google analytic
    */
    public boolean checkAppEventFlag(){
        if (FBSdkData.mobileSettings.TRIGGER_APP_EVENTS > 0) {
            localLog("fbSdk : UpdateAppEvent ", "TRIGGER_APP_EVENT is on from server");
            return true;
        }else {
            localLog("fbSdk : UpdateAppEvent ", "TRIGGER_APP_EVENT is off from server");
            return false;
        }
    }

    public boolean checkAppErrorFlag(){
        if (FBSdkData.mobileSettings.TRIGGER_ERROR_EVENT > 0) {
            localLog("fbSdk : UpdateAppEvent ", "TRIGGER_APP_ERRORS is on from server");
            return true;
        }else {
            localLog("fbSdk : UpdateAppEvent ", "TRIGGER_APP_ERRORS is off from server");
            return false;
        }
    }

    ///////////////////////////////////////////////////
    //////////////Services ////////////////////////
    ///////////////////////////////////////////////////
    public void mobileSettings(String cusId) {


        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                if(state)
                    Log.d("Mobile Settings  Api","Success");
                else
                    Log.d("Mobile Settings Api","Fail");
            }
        });
    }

    public void getFBOfferPass(JSONObject offerpromo, String offerId, boolean isPMIntegrated, final com.fishbowl.basicmodule.Interfaces.FBOfferPassCallback callback) {

        FBOfferService.sharedInstance().getFBOfferPass(offerpromo,offerId,isPMIntegrated, new FBOfferService.FBOfferPassCallback(){

            @Override
            public void OnFBOfferPassCallback(byte[] response1,
                                              String error) {

                //return array of byte

                callback.onClypOfferPassCallback(response1, error);

            }
        });

    }


    public void getFBOfferByOfferId(JSONObject offerpromo, String itemId, final FbOfferPushCallback callback) {
        FBOfferService.sharedInstance().getFBOfferByOfferId(offerpromo, itemId, new FBOfferService.FBOfferByIdCallback() {
            @Override
            public void onFBOfferByIdCallback(JSONObject response, Exception error) {
                callback.onFBOfferPush(response,error);
            }
        } );

    }

    //now its deprecated from production

//    public void getFBOfferPromo(JSONObject offerpromo, String itemId, Boolean isPMIntegrated, final com.fishbowl.basicmodule.Interfaces.FBOfferPromoCallback callback) {
//        FBOfferService.sharedInstance().getFBOfferPromo(offerpromo, itemId, isPMIntegrated,  new FBOfferService.FBOfferPromoCallback(){
//
//            @Override
//            public void OnFBOfferPromoCallback(JSONObject response,
//                                               String error) {
//                callback.onClypOfferyCallback(response, error);
//
//            }
//
//
//
//        });
//
//    }

    //now its deprecated from production

//    public void sendOfferEvent( JSONObject data){
//
//        FBOfferService.sharedInstance().sendOfferEvent(data);
//
//    }


    //now its deprecated from production
//    public void getClypOffer(JSONObject offer, String customId, final FBOfferSummaryCallback callback) {
//        FBOfferService.sharedInstance().getFBOffer(offer, customId, new FBOfferCallback() {
//
//            @Override
//            public void OnFBOfferCallback(JSONObject response, String error) {
//                callback.onClypOfferyCallback(response, error);
//            }
//        });
//
//    }


    //now its deprecated from production
//    public void getredeemedservices(JSONObject offer,String itemId,final FBRedeemedSummaryCallback callback) {
//
//        FBOfferService.sharedInstance().getFBRedeemedOffer(offer, itemId, new FBOfferService.FBRedeemedCallback() {
//            @Override
//            public void OnFBRedeemedCallback(JSONObject response, String error) {
//                callback.onClypRedeemedCallback(response, error);
//
//            }
//        });
//
//    }


    //now its deprecated from production
//
//    public void logoutClpSdk() {
//        registerGuest();
//        FBSdkData.setCustomer(new FBCustomerItem());
//    }


    //services
    public void getAllStores() {
        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback(){
            @Override
            public void OnAllStoreCallback(JSONObject response, Exception error){
                if(response!=null){
                    Intent intent = new Intent();
                    if (FBUtility.isMyServiceRunning(LocationService.class, context)) {//removed ! mark by vaseem
                        intent.setAction(CLP_CALLBACK);
                        intent.putExtra(CLP_CALLBACK,CLP_SETTINGS_UPDATE_CALLBACK);
                        context.sendBroadcast(intent);
                    } else {
//                        intent = new Intent(context,LocationService.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startService(intent);
                    }
                }
            }
        });
    }




//    //services
//    public void getAllSearchStores(JSONObject store, String query, final FBSearchStoreCallback callback) {
//        FBStoreService.sharedInstance().getSearchAllStore(store,query,new FBStoreService.FBAllSearchStoreCallback(){
//            @Override
//            public void OnAllSearchStoreCallback(List<FBStoresItem> response , Exception error){
//                if(response!=null){
//                    callback.onClypSearchStore(response, error);
//                }
//            }
//        });
//    }





    public void getBeaconsForStoreNo(ArrayList<String> stores) {
        FBBeaconService.sharedInstance().getBeaconsForStoreNo(stores, new FBBeaconsForStoreNoCallback(){


            @Override
            public void OnFBBeaconsForStoreNoCallback(JSONObject response, String error) {
                if(response!=null){
                    //Location service is running and beaconListInGeofence contain some geogfecnce
                    if (! FBBeaconService.sharedInstance().beaconListInGeofence.isEmpty()&& FBUtility.isMyServiceRunning(LocationService.class,context)) {
                        Intent intent = new Intent();
                        intent.setAction(CLP_CALLBACK);
                        intent.putExtra(CLP_CALLBACK,CLP_STARTBEACON_CALLBACK);
                        context.sendBroadcast(intent);
                        // startBeaconForStore(allBeaconsForStores);
                    }
                }

            }
        });
    }

    //now its deprecated from production
//    // Customer Registration/Update after login
//    public void saveCustomer(FBCustomerItem customer, final OnFBCustomerRegisterListener custRegListener) {
//        FBCustomerService.sharedInstance().saveCustomer(customer,custRegListener ,new FBSaveCustomerCallback() {
//            @Override
//            public void OnFBSaveCustomerCallback(JSONObject response, String error) {
//                if(response!=null)
//                    Log.d("FBSdk","Success");
//                else
//                    Log.d("FBSdk","Fail");
//            }
//        });
//    }


    //now its deprecated from production
    // Customer Registration on CLP SDK Init
//    public void registerCustomer(FBCustomerItem customer, final OnFBSdkRegisterListener sdkRegListener) {
//        FBCustomerService.sharedInstance().registerCustomer(customer,sdkRegListener ,new FBCustomerService.FBCustomerRegisterCallback() {
//
//                    @Override
//                    public void OnFBCustomerRegisterCallback(JSONObject response, String error) {
//                        if(response!=null)
//                            Log.d("FBSdk","Success");
//                        else
//                            Log.d("FBSdk","Fail");
//                    }
//                }
//        );
//    }



    //now its deprecated from production
//    // Customer Registration on CLP SDK Init
//    private void registerGuest() {
//        FBCustomerService.sharedInstance().registerGuest(new FBGuestRegisterCallback(){
//                                                             @Override
//                                                             public void onFBGuestRegisterCallback(JSONObject response, String error) {
//                                                                 if(response!=null)
//                                                                     Log.d("FBSdk","Success");
//                                                                 else
//                                                                     Log.d("FBSdk","Fail");
//                                                             }
//                                                         }
//        );
//    }




    public void beaconInRange(Context contx, FBMobileEventsItem mobileEvents) {
        FBBeaconService.sharedInstance().beaconInRange(mobileEvents, new FBBeaconService.FBBeaconsInRangeCallback(){
            @Override
            public void OnFBBeaconsInRangeCallback(JSONObject response, String error) {
                if(response!=null)
                    Log.d("FBSdk","beaconInRange Success");
                else
                    Log.d("FBSdk","beaconInRange Fail");
            }
        });
    }

    ///////////////////////////////////////////////////
    //////////////GETTER SETTER////////////////////////
    ///////////////////////////////////////////////////


    public FBSdkData getFBSdkData() {
        return FBSdkData;
    }


    public boolean getBluetoothPermission() {
        return FBSdkData.isBluetoothPermission();
    }

    public boolean getGpsPermission() {
        // return FBSdkData.isGpsPermission();
        return true;
    }

    public void setBluetoothPermission(boolean bluetoothPermission) {
        FBSdkData.setBluetoothPermission(bluetoothPermission);
        // FBSdkData.save();
    }

    public void setGpsPermission(boolean gpsPermission) {
        FBSdkData.setGpsPermission(true);
        // FBSdkData.save();
    }

    public List<FBStoresItem> getAllCLPBeaconStoreList() {
        return FBSdkData.getAllCLPBeaconStoreList();
    }

    public List<FBStoresItem> getAllStoreList() {
        return FBSdkData.getAllStoresList();
    }

    public long getDISTANCE_FILTER() {
        return FBSdkData.mobileSettings.getDISTANCE_FILTER();
    }

    public float getDISTANCE_STORE() {
        return FBSdkData.mobileSettings.getDISTANCE_STORE();
    }

    public float getGEOFENCE_RADIUS() {
        return FBSdkData.mobileSettings.getGEOFENCE_RADIUS();
    }

    public float getGEOFENCE_MIN_RADIUS() {
        return FBSdkData.mobileSettings.getGEOFENCE_MIN_RADIUS();
    }

    public long getSTORE_REFRESH_TIME() {
        return FBSdkData.mobileSettings.getSTORE_REFRESH_TIME();
    }

    public int getMAX_STORE_COUNT() {
        return FBSdkData.mobileSettings.getMAX_STORE_COUNT_ANDROID();
    }

    public int getMAX_BEACON_COUNT() {
        return FBSdkData.mobileSettings.getMAX_STORE_COUNT_ANDROID();
    }

    public int getENABLE_LOCAL_NOTIFICATION() {
        return FBSdkData.mobileSettings.ENABLE_LOCAL_NOTIFICATION;
    }

    public long getOUT_SIDE_UPDATE_INTERVAL() {
        return FBSdkData.mobileSettings.getOUT_SIDE_UPDATE_INTERVAL();
    }

    public long getOUT_SIDE_FASTEST_UPDATE_INTERVAL() {
        return FBSdkData.mobileSettings.getOUT_SIDE_FASTEST_UPDATE_INTERVAL();
    }

    public long getIN_SIDE_UPDATE_INTERVAL() {
        return FBSdkData.mobileSettings.getIN_SIDE_UPDATE_INTERVAL();
    }

    public long getIN_SIDE_FASTEST_UPDATE_INTERVAL() {
        return FBSdkData.mobileSettings.getIN_SIDE_FASTEST_UPDATE_INTERVAL();
    }

    public long getGEOFENCE_CHECK_FREQUENCY() {
        return FBSdkData.mobileSettings.getGEOFENCE_CHECK_FREQUENCY();
    }

    public long getLOCATION_UPDATE_PING_FREQUENCY() {
        return FBSdkData.mobileSettings.getLOCATION_UPDATE_PING_FREQUENCY();
    }

    public long getBEACON_PING_FREQUENCY() {
        return FBSdkData.mobileSettings.getBEACON_PING_FREQUENCY();
    }

    public long getGEOFENCE_EXPIRY_TIME() {
        return FBSdkData.mobileSettings.getGEOFENCE_EXPIRY_TIME();
    }

    public long getIN_REGION_SLAB_TIME() {
        return FBSdkData.mobileSettings.getIN_REGION_SLAB_TIME();
    }

    public long getLOCATION_SERVICE_REFRESH_TIME() {
        return FBSdkData.mobileSettings.getLOCATION_SERVICE_REFRESH_TIME();
    }

    public long getIN_STORE_UPDATE_INTERVAL() {
        return FBSdkData.mobileSettings.getIN_STORE_UPDATE_INTERVAL();
    }

    public long getGEOFENCE_CHECK_DISTANCE_MOVED() {
        return FBSdkData.mobileSettings.getGEOFENCE_CHECK_DISTANCE_MOVED();
    }

    public long getBEACON_SLAB_TIME() {
        return FBSdkData.mobileSettings.getBEACON_SLAB_TIME();
    }

    public static String getDeviceType() {
        return FBConstant.DEVICE_TYPE;
    }

    public static String getDevice_os_ver() {
        return FBConstant.device_os_ver;
    }

    public void setFBSdkData(FBSdkData FBSdkData) {
        this.FBSdkData = FBSdkData;
    }
    public void setmCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }
    public String getAndroidOs(){
        return FBUtility.getAndroidOs();
    }

    public interface FBEventCallback
    {
        public void OnFBEventCallback(JSONArray response, Exception error);
    }



}