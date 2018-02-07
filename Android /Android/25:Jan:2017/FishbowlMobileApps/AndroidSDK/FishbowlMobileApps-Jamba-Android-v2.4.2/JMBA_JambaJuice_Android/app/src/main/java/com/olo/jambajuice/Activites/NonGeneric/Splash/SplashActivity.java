package com.olo.jambajuice.Activites.NonGeneric.Splash;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Analytics.FBToastService;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliesActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PkpassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.PushNotificationActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.AllStoreMenuCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdDetailsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.StoreLocatorService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Location.LocationManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.BusinessLogic.Services.ProductService.TwentyFourHourInMiliSeconds;
import static com.olo.jambajuice.Utils.Constants.SPLASH_TIME_OUT;
import static com.olo.jambajuice.Utils.SharedPreferenceHandler.LastProductUpdate;

/**
 * *
 * modified by Digvijay Chauhan on 29/3/16.
 */

public class SplashActivity extends AppCompatActivity implements LocationUpdatesCallback {
    private static final String DEFAULT_STORE_CODE = "0145";//Emeryville - 0145 : Merced - 0620
    private static final String DEFAULT_STORE_SEARCH_KEYWORD = "Emeryville";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    Bundle extras;
    String offerid = null;
    String clpnid = null;
    String custId = null;
    String promoCode;
    String ntype = "";
    int diifer;
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer pmPromotionID;
    Integer offerID;
    Integer channelID;
    String validityEndDateTime;
    AlertDialog.Builder gpsAlertBuilder;
    private String mPassDataUrl = "";
    private String passPreviewImageURL = "";
    private String e = "";
    private ArrayList<OfferItem> offerList;
    private int offerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initializeCrashlytics();
        setContentView(R.layout.activity_splash);
        checkGooglePlayService();
    }


    private void checkGooglePlayService() {
        // Getting status
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (resultCode == ConnectionResult.SUCCESS) {
            checkPermissionsforlocation();

            if (Utils.checkEnableGPS(this)) {
                checkLocationServicesEnabled();
            }

            int constants = getIntent().getFlags();

            Intent i = getIntent();
            // Bundle eee = i.getExtras();
            extras = i.getExtras();

//            if (getIntent().getFlags() == (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)) {
//                extras = i.getExtras();
//            } else {
//                extras = null;
//            }

//            if (extras != null) {
//                this.mPassDataUrl = i.getStringExtra("url");
//                String ntype = i.getStringExtra("ntype");
//                String apsStr = i.getExtras().getString("aps");
//                passPreviewImageURL = i.getExtras().getString("previewimage");
//                try {
//                    if (apsStr != null) {
//                        enableScreen(false);
//                        JSONObject apsJson = new JSONObject(apsStr);
//                        if (apsJson.has("alert")) {
//                            e = apsJson.getString("alert");
//                            if (extras.getString("offerid") != null) {
//                                offerid = extras.getString("offerid");
//                            }
//
//                            if (extras.getString("clpnid") != null) {
//                                clpnid = extras.getString("clpnid");
//                            }
//
//                            if (extras.getString("custid") != null) {
//                                custId = extras.getString("custid");
//                            }
//
//                            if (extras.getString("pc") != null) {
//                                promoCode = extras.getString("pc");
//                            }
//
//                            long currCustId = FBPreferences.sharedInstance(this).getUserMemberId();
//                            long offerCustId = Long.parseLong(custId);
//
//                            // navigateScreen();
//                            if (JambaApplication.getAppContext().isApplicationVisible()) {
//                                if (StringUtilities.isValidString(e)) {
//                                    if (StringUtilities.isValidString(custId)) {
//                                        if (currCustId == offerCustId) {
//                                            Intent intent1 = new Intent(SplashActivity.this, PushNotificationActivity.class);
//                                            Bundle exs = new Bundle();
//                                            exs.putString("Title", e);
//                                            exs.putString("offerId", offerid);
//                                            exs.putString("custId", custId);
//                                            exs.putString("promoCode", promoCode);
//                                            intent1.putExtras(exs);
//                                            startActivity(intent1);
//                                            finish();
//                                        }
//                                    }
//                                }
//                                finish();
//                            } else {
//                                TransitionManager.transitFrom(SplashActivity.this, HomeActivity.class);
//
//                                if (StringUtilities.isValidString(e)) {
//                                    if (StringUtilities.isValidString(custId)) {
//                                        if (currCustId == offerCustId) {
//
//                                            Intent intent1 = new Intent(SplashActivity.this, PushNotificationActivity.class);
//                                            Bundle exs = new Bundle();
//                                            exs.putString("Title", e);
//                                            exs.putString("offerId", offerid);
//                                            exs.putString("custId", custId);
//                                            exs.putString("promoCode", promoCode);
//                                            intent1.putExtras(exs);
//                                            startActivity(intent1);
//                                            finish();
//                                        }
//                                    }
//                                }
//                                finish();
//                            }
//                        }
//                    }
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//            }
            if (extras != null) {
//                String[] keysets = new String[getIntent().getExtras().keySet().size()];
//                String[] value = new String[getIntent().getExtras().keySet().size()];
//                int k = 0;
                if (getIntent().getExtras().keySet() != null) {

                    String title = "";
                    if (getIntent().getExtras().keySet().contains("datapayloadjson")) {
                        this.mPassDataUrl = i.getStringExtra("url");
                        String data = i.getExtras().getString("datapayloadjson");
                        title = i.getExtras().getString("message");
                        if (data != null) {
                            enableScreen(false);
                            try {
                                JSONObject dataJson = new JSONObject(data);
                                if (dataJson.getString("offerid") != null) {
                                    offerid = dataJson.getString("offerid");
                                }


                                if (dataJson.getString("custid") != null) {
                                    custId = dataJson.getString("custid");
                                }

                                if (dataJson.getString("pc") != null) {
                                    promoCode = dataJson.getString("pc");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        for (String key : getIntent().getExtras().keySet()) {
                            if (key.equalsIgnoreCase("offerid")) {
                                offerid = getIntent().getExtras().getString(key);
                            }
                            if (key.equalsIgnoreCase("custid")) {
                                custId = getIntent().getExtras().getString(key);
                            }
                            if (key.equalsIgnoreCase("pc")) {
                                promoCode = getIntent().getExtras().getString(key);
                            }
                            if (key.equalsIgnoreCase("noti_body")) {
                                title = getIntent().getExtras().getString(key);
                            }

                        }
                    }

                    if (StringUtilities.isValidString(custId)
                            && StringUtilities.isValidString(title)) {
                        long currCustId = FBPreferences.sharedInstance(this).getUserMemberId();
                        long offerCustId = Long.parseLong(custId);

                        // navigateScreen();
                        if (JambaApplication.getAppContext().isApplicationVisible()) {
                            if (StringUtilities.isValidString(title)) {
                                if (StringUtilities.isValidString(custId)) {
                                    if (currCustId == offerCustId) {
                                        Intent intent1 = new Intent(SplashActivity.this, PushNotificationActivity.class);
                                        Bundle exs = new Bundle();
                                        exs.putString("Title", title);
                                        exs.putString("offerId", offerid);
                                        exs.putString("custId", custId);
                                        exs.putString("promoCode", promoCode);
                                        intent1.putExtras(exs);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }
                            }
                            finish();
                        } else {
                            TransitionManager.transitFrom(SplashActivity.this, HomeActivity.class);

                            if (StringUtilities.isValidString(title)) {
                                if (StringUtilities.isValidString(custId)) {
                                    if (currCustId == offerCustId) {
                                        Intent intent1 = new Intent(SplashActivity.this, PushNotificationActivity.class);
                                        Bundle exs = new Bundle();
                                        exs.putString("Title", title);
                                        exs.putString("offerId", offerid);
                                        exs.putString("custId", custId);
                                        exs.putString("promoCode", promoCode);
                                        intent1.putExtras(exs);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }
                            }
                            finish();
                        }
                    } else {
                        loadImages();
                        startAnimationAfterDelay();
                    }
                }else {
                    loadImages();
                    startAnimationAfterDelay();
                }
            } else {
                loadImages();
                startAnimationAfterDelay();
            }


//            try {
//                pushNotificationRecieve();
//            } catch (Exception e) {
//                Crashlytics.logException(e);
//
//            }
        } else {
            alert(this, "Get Google Play Services", "An update to Google Play services is required to use the Jamba Juice app", "Update Now");
        }
    }

    public void alert(Context context, String Title, String Message, String str) {
        if (Message == null) {
            Message = "Error";
        }
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        alertDialogBuilder.setNegativeButton(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {

                    if (checkPlayStore()) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE)));

                    } else {

                        startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en")), "Choose Browser"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private boolean checkPlayStore() {
        try {
            if (this.getPackageManager().getApplicationInfo("com.android.vending", 0).enabled
                    || this.getPackageManager().getApplicationInfo("com.google.market", 0).enabled)
                return true;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    private void checkPermissionsforlocation() {
        FBToastService.sharedInstance().initWithContext(this);
        FBToastService.sharedInstance().setOnOff(true);

        if ((int) Build.VERSION.SDK_INT < 23) {
            return;
        } else {
            if (checkPermission()) {

                //             FBToastService.sharedInstance().show("Permission already granted");


            } else {
                //       FBToastService.sharedInstance().show("Please request permission");

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    //    FBToastService.sharedInstance().show("Permission already granted.");

                }
            }
        }


    }


    private boolean checkPermission() {


        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            if (JambaApplication.getAppContext().fbsdkObj != null) {
                JambaApplication.getAppContext().fbsdkObj.isLocationService = true;
            }
            return true;
        } else {
            if (JambaApplication.getAppContext().fbsdkObj != null) {
                JambaApplication.getAppContext().fbsdkObj.isLocationService = false;
            }
            return false;
        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            //    Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //    FBToastService.sharedInstance().show("Permission Granted, Now you can access location data.");
                    Toast.makeText(this, "Permission Granted, Now you can access location data.", Toast.LENGTH_SHORT).show();

                } else {
                    //   FBToastService.sharedInstance().show("Permission Denied, You cannot access location data.");
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private void checkLocationServicesEnabled() {
        LocationManager locationManager = LocationManager.getInstance(this);
        if (locationManager.isLocationServicesEnabled()) {
            LocationManager.getInstance(this).startLocationUpdates(this);
        } else {
            //Notify user that location service is not enabled
            showLocationServiceNotAvailableAlert();
        }
    }

    public void showLocationServiceNotAvailableAlert() {
        gpsAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
                this, android.R.style.Theme_DeviceDefault_Light_Dialog));
        gpsAlertBuilder.setTitle("Jamba");
        gpsAlertBuilder
                .setMessage("Your location will be used to find stores near you and location based store offers.");
        gpsAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Turn On GPS if Yes Clicked
                    gpsAlertBuilder = null;
                    turnGPSOn(dialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gpsAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Show Some toast Message
                gpsAlertBuilder = null;
                dialog.dismiss();
            }
        });
        gpsAlertBuilder.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void turnGPSOn(DialogInterface dialog) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        dialog.dismiss();

        if (currentapiVersion > 18) {

            Intent mIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle mBundle = new Bundle();
            startActivity(mIntent, mBundle);
        } else {

            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            this.sendBroadcast(intent);
            String provider = Settings.Secure.getString(
                    this.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                this.sendBroadcast(poke);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        if (checkPermission()) {
            User user = UserService.getUser();

            final FBSdk sdk = JambaApplication.getAppContext().fbsdkObj;
            if (user != null && sdk != null) {
                Location location = sdk.getmCurrentLocation();
                String loc = "Lat=" + location.getLatitude() + " Long=" + location.getLongitude();
                FBMobileSettingService.sharedInstance().getMobileSetting("0", new FBMobileSettingService.FBMobileSettingCallback() {
                    @Override
                    public void OnFBMobileSettingCallback(boolean b, Exception e) {

                        if(DataManager.disableEvents) {
                            FBMobileSettingService.sharedInstance().mobileSettings.TRIGGER_APP_EVENTS = 0;
                        }

                        if (FBPreferences.sharedInstance(getApplicationContext()).getAccessToken() != null) {
                            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_OPEN);
                        } else {
                            Utils.getTokenAndSendEventsByName(getApplicationContext(), FBEventSettings.APP_OPEN);
                        }

                    }
                });

            }
        } else {
            FBMobileSettingService.sharedInstance().getMobileSetting("0", new FBMobileSettingService.FBMobileSettingCallback() {
                @Override
                public void OnFBMobileSettingCallback(boolean b, Exception e) {

                    if(DataManager.disableEvents) {
                        FBMobileSettingService.sharedInstance().mobileSettings.TRIGGER_APP_EVENTS = 0;
                    }

                    if (FBPreferences.sharedInstance(getApplicationContext()).getAccessToken() != null) {
                        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_OPEN);
                    } else {
                        Utils.getTokenAndSendEventsByName(getApplicationContext(), FBEventSettings.APP_OPEN);
                    }

                }
            });
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (checkPermission()) {
//            User user = UserService.getUser();
//
//            FBSdk sdk = JambaApplication.getAppContext().fbsdkObj;
//            if (user != null && sdk != null) {
//                Location location = sdk.getmCurrentLocation();
//                String loc = "Lat=" + location.getLatitude() + " Long=" + location.getLongitude();
//                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_OPEN);
//
//            }
//        }
//    }

    private void pushNotificationRecieve() {
        // Broadcast Receiver for receiving token To get BroadCast
        BroadcastReceiver mMessageReceived = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle extra = intent.getExtras();
                if (extra != null) {

                    try {

                        if (extra.getString("offerid") != null) {
                            offerid = extra.getString("offerid");
                        }

                        if (extra.getString("clpnid") != null) {
                            clpnid = extra.getString("clpnid");
                        }

                        if (extra.getString("custid") != null) {
                            custId = extra.getString("custid");

                        }
                    } catch (Exception e) {

                    }
                }
            }
        };

        // Broadcast Receiver for receiving message
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceived,
                new IntentFilter(FBSdk.CLP_PUSH_MESSAGE_RECEIVED));
    }


    public void enableScreen(boolean isEnabled) {

        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    private void loadImages() {
        ImageView view = (ImageView) findViewById(R.id.splash_bg_layout);
        ImageView topLeft = (ImageView) findViewById(R.id.topLeft);
        ImageView topRight = (ImageView) findViewById(R.id.topRight);
        ImageView bottomLeft = (ImageView) findViewById(R.id.bottomLeft);
        ImageView bottomRight = (ImageView) findViewById(R.id.bottomRight);
        ImageView blendInGood = (ImageView) findViewById(R.id.blendInGood);
        ImageView jambaLogo = (ImageView) findViewById(R.id.jambaLogo);

        BitmapUtils.loadBitmapResource(view, R.drawable.splash_bg);
        BitmapUtils.loadBitmapResource(topLeft, R.drawable.splash_top_left);
        BitmapUtils.loadBitmapResource(topRight, R.drawable.splash_top_right);
        BitmapUtils.loadBitmapResource(bottomLeft, R.drawable.splash_bottom_left);
        BitmapUtils.loadBitmapResource(bottomRight, R.drawable.splash_bottom_right);
        BitmapUtils.loadBitmapResource(blendInGood, R.drawable.splash_blend_good);
        BitmapUtils.loadBitmapResource(jambaLogo, R.drawable.splash_jamba_juice_logo);
    }

    private void startAnimationAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 500);
    }

    private void startAnimation() {
        ImageView topLeft = (ImageView) findViewById(R.id.topLeft);
        ImageView topRight = (ImageView) findViewById(R.id.topRight);
        ImageView bottomLeft = (ImageView) findViewById(R.id.bottomLeft);
        ImageView bottomRight = (ImageView) findViewById(R.id.bottomRight);
        RelativeLayout logoView = (RelativeLayout) findViewById(R.id.logoView);

        JambaApplication appContext = JambaApplication.getAppContext();
        Animation animCenter = AnimationUtils.loadAnimation(appContext, R.anim.splash_center);
        Animation animTopLeft = AnimationUtils.loadAnimation(appContext, R.anim.splash_top_left);
        Animation animTopRight = AnimationUtils.loadAnimation(appContext, R.anim.splash_top_right);
        Animation animBottomLeft = AnimationUtils.loadAnimation(appContext, R.anim.splash_bottom_left);
        Animation animBottomRight = AnimationUtils.loadAnimation(appContext, R.anim.splash_bottom_right);
        animTopLeft.setFillAfter(true);
        animTopRight.setFillAfter(true);
        animBottomLeft.setFillAfter(true);
        animBottomRight.setFillAfter(true);

        animCenter.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                navigateToNextScreen();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topLeft.setVisibility(View.VISIBLE);
        topRight.setVisibility(View.VISIBLE);
        bottomLeft.setVisibility(View.VISIBLE);
        bottomRight.setVisibility(View.VISIBLE);
        logoView.setVisibility(View.VISIBLE);

        logoView.startAnimation(animCenter);
        topLeft.startAnimation(animTopLeft);
        topRight.startAnimation(animTopRight);
        bottomLeft.startAnimation(animBottomLeft);
        bottomRight.startAnimation(animBottomRight);
    }


    private void createNewOrderForDefaultStore() {
        StoreService.getStoreInformation(String.valueOf(DEFAULT_STORE_CODE), new StoreDetailCallback() {
            @Override
            public void onStoreDetailCallback(Store store, Exception exception) {
                if (exception != null) {

                    Utils.showErrorAlert(SplashActivity.this, exception);
                    return;
                }
                if (store != null) {
                    if (store.getName() != null) {
                        if (DataManager.getInstance().isDebug) {
                            store.setName(Utils.setDemoStoreName(store).getName().replace("Jamba Juice ", ""));
                        } else {
                            store.setName(store.getName().replace("Jamba Juice ", ""));
                        }
                    }
                    selectNewOrder(store);
                } else {
                    StoreLocatorService.findStoresByLocationName(DEFAULT_STORE_SEARCH_KEYWORD, new StoreServiceCallback() {
                        @Override
                        public void onStoreServiceCallback(ArrayList<Store> stores, Exception exception) {
                            if (exception != null) {
                                enableScreen(true);
                                Utils.showErrorAlert(SplashActivity.this, exception);
                                return;
                            }
                            if (stores != null && stores.size() > 0) {
                                Store store = stores.get(0);
                                if (store.getName() != null) {
                                    if (DataManager.getInstance().isDebug) {
                                        store.setName(Utils.setDemoStoreName(store).getName().replace("Jamba Juice ", ""));
                                    } else {
                                        store.setName(store.getName().replace("Jamba Juice ", ""));
                                    }
                                }
                                selectNewOrder(store);
                            } else {
                                enableScreen(true);
                                Utils.showErrorAlert(SplashActivity.this, new Exception("Could not find a store for you. Please try again"));
                            }
                        }
                    });
                }
            }
        });
    }

    private void selectNewOrder(Store selectedStore) {
        DataManager.getInstance().resetDataManager();
        ProductService.startNewOrderForStore(this, selectedStore, new AllStoreMenuCallBack() {
            @Override
            public void onAllStoreMenuCallback(Exception exception) {
                if (exception != null) {
                    Utils.showErrorAlert(SplashActivity.this, exception);
                    return;
                }
                Log.i("Callback", "Success");
                openMenu();
            }

            @Override
            public void onAllStoreMenuErrorCallback(Exception exception) {
                if (exception != null) {

                    Utils.showErrorAlert(SplashActivity.this, exception);
                    return;
                }

                Log.i("Callback", "Error");
            }
        });

    }

    //load ad configuartion
    private void loadAdConfig() {
        if (DataManager.getInstance().getSelectedStoreProductAd() != null
                && DataManager.getInstance().getSelectedStoreProductAd().size() > 0) {
            for (ProductAd ad : DataManager.getInstance().getSelectedStoreProductAd()) {
                loadAdDetails(ad);
            }
        } else {
            ProductService.loadAdsConfig(this, new ProductAdsServiceCallback() {
                @Override
                public void onProductAdsCallback(ArrayList<ProductAd> productAds, Exception exception) {
                    DataManager manager = DataManager.getInstance();
                    manager.setSelectedStoreProductAd(productAds);// set filtered ads
                    for (ProductAd ad : productAds) {
                        loadAdDetails(ad);
                    }
                }
            });
        }
    }

    private void openMenu() {
        long lastPullTime = SharedPreferenceHandler.getInstance().getLong(LastProductUpdate, -1);
        if (System.currentTimeMillis() - lastPullTime > TwentyFourHourInMiliSeconds || lastPullTime == -1) {
            enableScreen(false);
        }
        loadAdConfig();
    }

    //load ads details
    private void loadAdDetails(ProductAd productAd) {
        ProductService.loadAllAdDetails(this, productAd, new ProductAdDetailsServiceCallback() {
            @Override
            public void onProductAdDetailsCallback(ArrayList<ProductAdDetail> productAdDetailss, Exception exception) {
                DataManager.getInstance().setSelectedStoreProductAdDetail(productAdDetailss);
                List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
                DataManager.getInstance().setRecentOrderList(userProds);
                navigateScreen();
            }
        });
    }

    private void navigateScreen() {
        enableScreen(true);
        if (UserService.isUserAuthenticated()) {

            if (DataManager.getInstance().getCurrentSelectedStore() != null
                    && DataManager.getInstance().getCurrentSelectedStore().getName() != null) {
                //Get User recent order product
//            List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
//            if (userProds == null) {
//                userProds = new ArrayList<>();
//            }
//            List<Product> tempProductList = new ArrayList<>();//temp copy for validation
//            tempProductList.addAll(userProds);
//            //Remove recent products do not in current store menu
//            for (Product product : tempProductList) {
//                StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
//                if (storeMenuProduct == null) {
//                    userProds.remove(product);
//                }
//            }

                if ((((DataManager.getInstance().getSelectedStoreProductAdDetail() != null
                        && DataManager.getInstance().getSelectedStoreProductAdDetail().size() > 0)
                        || (DataManager.getInstance().getSelectedStoreFeaturedProducts() != null
                        && DataManager.getInstance().getSelectedStoreFeaturedProducts().size() > 0)
                        || (DataManager.getInstance().getRecentOrderList() != null
                        && DataManager.getInstance().getRecentOrderList().size() > 0)))) {
                    if (StringUtilities.isValidString(e)) {
                        if (StringUtilities.isValidString(custId)) {
                            if (FBSdk.sharedInstance(this).getFBSdkData() != null
                                    && FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer() != null) {
                                if (FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer().getCustomerID() == Long.parseLong(custId)) {
                                    enableScreen(true);
                                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Title", e);
                                    // extras.putString("Url", passPreviewImageURL);
                                    extras.putString("offerId", offerid);
                                    extras.putString("custId", custId);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                    finish();

                } else if (DataManager.getInstance().getAllProductFamily() != null && DataManager.getInstance().getAllProductFamily().size() > 0) {
                    enableScreen(true);
                    if (StringUtilities.isValidString(e)) {
                        if (StringUtilities.isValidString(custId)) {
                            if (FBSdk.sharedInstance(this).getFBSdkData() != null
                                    && FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer() != null) {
                                if (FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer().getCustomerID() == Long.parseLong(custId)) {
                                    Intent intent = new Intent(SplashActivity.this, ProductFamiliesActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Title", e);
                                    // extras.putString("Url", passPreviewImageURL);
                                    extras.putString("offerId", offerid);
                                    extras.putString("custId", custId);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                    finish();

                } else {

                }

            } else {

                enableScreen(true);
                createNewOrderForDefaultStore();
                //Utils.showErrorAlert(this, new Exception("Not able to find a store for you. Please try again!"));
            }
        } else {
            TransitionManager.transitFrom(SplashActivity.this, HomeActivity.class);

            if (StringUtilities.isValidString(e)) {
                if (StringUtilities.isValidString(custId)) {
                    if (FBSdk.sharedInstance(this).getFBSdkData() != null
                            && FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer() != null) {
                        if (FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer().getCustomerID() == Long.parseLong(custId)) {

                            Intent intent1 = new Intent(SplashActivity.this, PushNotificationActivity.class);
                            Bundle exs = new Bundle();
                            exs.putString("Title", e);
                            exs.putString("offerId", offerid);
                            exs.putString("custId", custId);
                            intent1.putExtras(exs);
                            startActivity(intent1);
                            finish();
                        }
                    }
                }
            }
            finish();

        }
//        else {
//            Toast.makeText(SplashActivity.this, "Please login before viewing the offer", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

    private void navigateToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean IsFirstSplash = SharedPreferenceHandler.getBoolean(SharedPreferenceHandler.IsFirstSplash, true);
                if (extras != null && (StringUtilities.isValidString(e))) {
                    DataManager manager = DataManager.getInstance();
                    if (mPassDataUrl.equalsIgnoreCase("pass")) {

                        Intent intent = new Intent(SplashActivity.this, PkpassdetailActivity.class);
                        Bundle extras = new Bundle();
                        //extras.putString("Url", passPreviewImageURL);
                        extras.putString("Title", e);
                        extras.putString("offerId", offerid);
                        intent.putExtras(extras);
                        startActivity(intent);
                        finish();
                    } else if (manager.getCurrentSelectedStore() != null && manager.getAllProductFamily() != null && manager.getAllProductFamily().size() > 0) {

                        openMenu();


                    } else {

                        if (UserService.isUserAuthenticated() && manager.getCurrentSelectedStore() != null && manager.getCurrentSelectedStore().getName() != null) {
                            selectNewOrder(manager.getCurrentSelectedStore());//download store menu for already exist store
                            return;
                        } else {
                            createNewOrderForDefaultStore();
                        }

                    }
                } else if (IsFirstSplash) {
                    TransitionManager.transitFrom(SplashActivity.this, WelcomeActivity.class);
                    finish();
                } else {
                    TransitionManager.transitFrom(SplashActivity.this, HomeActivity.class);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onLocationCallback(Location location) {
        JambaApplication.getAppContext().setmCurrentLocation(location);
    }

    @Override
    public void onConnectionFailedCallback() {

    }
}

