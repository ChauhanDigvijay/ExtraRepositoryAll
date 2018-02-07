package com.olo.jambajuice.Activites.NonGeneric.Home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp.SignUpJambaInsiderActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard.GiftCardHomeListActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.NewCardActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliesActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.MyRewardsActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.MyRewardsAndOfferActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderHistoryActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.ForceUpdateActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.SettingsActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.AllStoreMenuCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.FBSDKLoginServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.PreferredStoreCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdDetailsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RecentOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StartOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Models.RewardSummary;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.BusinessLogic.Services.OfferService;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.RewardService;
import com.olo.jambajuice.BusinessLogic.Services.StoreLocatorService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.FontsManager;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.incomm.Interfaces.InCommBrandsCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommGetAllCardServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommStates;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Services.InCommBrandService;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommUserService;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.olo.jambajuice.BusinessLogic.Services.ProductService.TwentyFourHourInMiliSeconds;
import static com.olo.jambajuice.Utils.SharedPreferenceHandler.LastProductUpdate;

/**
 * *
 * modified by dj on 18/3/16.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, RecentOrderCallback, LocationUpdatesCallback, StoreServiceCallback {
    public static final String MyPREFERENCES = "MyData";
    private static final String APP_ID = "0e307238823b47f08ed72ffb66a203aa";
    private static final String DEFAULT_STORE_SEARCH_KEYWORD = "Emeryville";
    private static final String DEFAULT_STORE_CODE = "0145";//Emeryville - 0145 : Merced - 0620
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    public static int frequency = 2;
    public static String versioncode;
    public static String version;
    public static String information;
    public static int FREQUENCY = 2;
    public static String forceupdate;
    public static int checkversion = 0;
    public static boolean check_offer = false;
    public static int checkoffer = 0;
    public static HomeActivity homeActivity;
    public JambaApplication _app;
    public int Trigger_Beacon = 0;
    public String title;
    View signedView;
    boolean recentOrdersExist;
    AlertDialog.Builder blueToothAlertBuilder;
    BluetoothAdapter mBluetoothAdapter;
    AlertDialog.Builder gpsAlertBuilder;
    Location currentLocation;
    LinearLayout llMenu;
    Button btnSignIn;
    Activity context;
    SharedPreferences keyValues;
    SharedPreferences sharefequency;
    Bundle extras;
    private boolean isLocationBasedSearch = false;
    private boolean updateHeader = false;
    private int availableRewardCount = 0;
    private String offerId;
    private String custId;
    private String previewURl;
    private SharedPreferences sharedPreferences;
    private StartOrderCallback globalCallBack;
    private int logincount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        homeActivity = this;

        setContentView(R.layout.activity_home);
        setUpToolBar(true);

        // Get intent information to see if we were launched from the app's custom URI
        Intent intent = getIntent();

        setUpView();

        checkPermissionsforlocation(context);

        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_UPDATE_HOME_ACTIVITY));


        // check if this intent is started via custom scheme link
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            // Parse the parameters sent to the intent
        }
        //Check bluetooth is enabled otherwise show alert
        //
        //   checkBluetoothEnabled();

        //Check gps is enabled otherwise show alert
        if ((int) Build.VERSION.SDK_INT < 23) {
            checkGPSEnabled(context);
            return;
        }

        //Hocke App update
        //  checkForUpdates();

        //checkMobileVersion();

    }


    public boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    public void checkPermissionsforlocation(Activity context) {
        if ((int) Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            checkGPSEnabled(context);
            return;
        } else {
            if (Utils.checkLocationPermission(context)) {
                // As of now, we dont use location service from fbsdk
//                if (JambaApplication.getAppContext().fbsdkObj != null) {
//                    JambaApplication.getAppContext().fbsdkObj.isLocationService = true;
//                }
                checkGPSEnabled(context);
            } else {
                // ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                Utils.requestPermissionGps(context);
            }
        }


    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }

    private void checkForUpdates() {
        // Remove this for store / production builds!
        UpdateManager.register(this, APP_ID);
    }

    public void checkBluetoothEnabled() {
        _app = JambaApplication.getAppContext();
        if (_app.fbsdkObj != null) {
            if (_app.fbsdkObj.getFBSdkData() != null) {
                Trigger_Beacon = _app.fbsdkObj.getFBSdkData().getMobileSettings().TRIGGER_BEACON;
                if (Trigger_Beacon == 1) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                        enableBluetoothIfDisabled();
                    }
                }
            }
        }
    }

    public boolean isBLESupportedDevice() {

        // GEt current API level of device
        int currentapiVersion = Build.VERSION.SDK_INT;

        // Check API level is > than 18
        if ((currentapiVersion > 18)
                && (getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))) {
            return true;
        }

        return false;
    }

    // Ask permission from user before enabling bluetooth
    public void enableBluetoothIfDisabled() {
        if (!isBLESupportedDevice())
            return;
        if (blueToothAlertBuilder != null)
            return;
        blueToothAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
                this, android.R.style.Theme_DeviceDefault_Light_Dialog));

        blueToothAlertBuilder.setTitle("Jamba");
        blueToothAlertBuilder
                .setMessage("Please turn on Bluetooth if you wish to receive offer notifications.");
        blueToothAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothAdapter.enable();
                blueToothAlertBuilder = null;
            }
        });
        blueToothAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Show Some toast Message
                dialog.dismiss();
                blueToothAlertBuilder = null;
            }
        });
        blueToothAlertBuilder.show();
    }


    public void checkGPSEnabled(Context context) {
        // ask user to enable gps
        if (gpsAlertBuilder != null) {
            gpsAlertBuilder = null;
        }
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean providerAvailable = false;
        final List<String> providers = manager.getProviders(true);
        for (final String provider : providers) {
            if (manager.isProviderEnabled(provider)
                    && provider.equals(LocationManager.GPS_PROVIDER)) {
                providerAvailable = true;
            }
        }
        if (Utils.checkEnableGPS(context)) {
            return;
        }

        gpsAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
                context, android.R.style.Theme_DeviceDefault_Light_Dialog));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (JambaApplication.getAppContext().fbsdkObj != null) {
                        JambaApplication.getAppContext().fbsdkObj.isLocationService = true;
                    }
                } else {
                    if (JambaApplication.getAppContext().fbsdkObj != null) {
                        JambaApplication.getAppContext().fbsdkObj.isLocationService = false;
                    }
                }
                break;
        }
    }

    public boolean checkLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void turnGPSOn(DialogInterface dialog) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (dialog != null) {
            dialog.dismiss();
        }


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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_signup) {
            trackButtonWithName("Sign Up");
            TransitionManager.slideUp(HomeActivity.this, SignUpJambaInsiderActivity.class);
        } else if (id == R.id.btn_signIn) {
            trackButtonWithName("Log In");
            TransitionManager.slideUp(HomeActivity.this, SignInActivity.class);
        } else if (id == R.id.menuStoreChangeHeaderInnerLeft) {
            TransitionManager.slideUp(HomeActivity.this, StoreLocatorActivity.class);
        } else if (id == R.id.imgHeaderProductSearch) {
            DataManager manager = DataManager.getInstance();
            if (manager.getCurrentSelectedStore() != null && manager.getAllProductFamily() != null && manager.getAllProductFamily().size() > 0) {
                TransitionManager.transitFrom(HomeActivity.this, ProductSearchActivity.class);
            } else if (manager.getCurrentSelectedStore() != null) {
                enableScreen(false);
                selectNewOrder(manager.getCurrentSelectedStore(), "product search");
            } else {
                if (UserService.isUserAuthenticated()) {
                    enableScreen(false);
                    String code = UserService.getUser().getFavoriteStoreCode();
                    if (UserService.getUser().getFavoriteStoreCode() != null) {
                        StoreService.getStoreInformation(UserService.getUser().getFavoriteStoreCode(), new StoreDetailCallback() {
                            @Override
                            public void onStoreDetailCallback(Store store, Exception exception) {
                                if (store != null) {
                                    if (store.isSupportsOrderAhead()) {
                                        selectNewOrder(store, "product search");
                                    } else {
                                        enableScreen(true);
                                        preferredStoredDoesnotSupportAlert();
                                    }
                                } else if (store == null || exception != null) {
                                    enableScreen(true);
                                    preferredStoredDoesnotSupportAlert();
                                }
                            }
                        });
                    } else {
                        enableScreen(true);
                        NotHavePreferredStoreAlert();
                    }
                }
            }
        } else if (id == R.id.menuView) {
            //Menu button in Signed in screen
            DataManager manager = DataManager.getInstance();
            if (manager.getCurrentSelectedStore() != null && manager.getAllProductFamily() != null && manager.getAllProductFamily().size() > 0) {
                isLocationBasedSearch = false;
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.MENU_CLICK);
                openMenu();//Open menu if already ready
            } else if (manager.getCurrentSelectedStore() != null) {
                enableScreen(false);
                selectNewOrder(manager.getCurrentSelectedStore(), "order now");
            } else {
                if (UserService.isUserAuthenticated()) {
                    enableScreen(false);
                    String code = UserService.getUser().getFavoriteStoreCode();
                    if (UserService.getUser().getFavoriteStoreCode() != null) {
                        StoreService.getStoreInformation(UserService.getUser().getFavoriteStoreCode(), new StoreDetailCallback() {
                            @Override
                            public void onStoreDetailCallback(Store store, Exception exception) {
                                if (store != null) {
                                    if (store.isSupportsOrderAhead()) {
                                        selectNewOrder(store, "order now");
                                    } else {
                                        enableScreen(true);
                                        preferredStoredDoesnotSupportAlert();
                                    }
                                } else if (store == null || exception != null) {
                                    enableScreen(true);
                                    preferredStoredDoesnotSupportAlert();
                                }
                            }
                        });
                    } else {
                        enableScreen(true);
                        NotHavePreferredStoreAlert();
                    }
                    //   if(UserService.isUserAuthenticated() && manager.getCurrentSelectedStore()!=null && manager.getCurrentSelectedStore().getName()!=null && manager.getCurrentSelectedStore().isSupportsOrderAhead()){
//                    selectNewOrder(manager.getCurrentSelectedStore());//download store menu for already exist store
//                    return;
//                }
//                if (currentLocation != null) {
//                    //Show menu for current location
//                    com.olo.jambajuice.Location.LocationManager.getInstance(this).stopLocationUpdates();
//                    double lat = currentLocation.getLatitude();
//                    double lng = currentLocation.getLongitude();
//                    isLocationBasedSearch=true;
//                   StoreLocatorService.findStoresNearLocation(lat, lng, this, "user_location");
//                } else {
//                    isLocationBasedSearch=false;
//                    createNewOrderForDefaultStore();
//                    StoreLocatorService.findStoresByLocationName(DEFAULT_STORE_SEARCH_KEYWORD, this);
                }
            }
        } else if (id == R.id.ll_menu) {
            //Menu button in Non Signed in Screen
            JambaApplication _app = ((JambaApplication) this.getApplicationContext());
            DataManager manager = DataManager.getInstance();

            if (manager.getCurrentSelectedStore() != null) {
                isLocationBasedSearch = false;
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.MENU_CLICK);
                openMenu();
            } else {
//                enableScreen(false);
//                if (currentLocation != null) {
//                    //Show menu for current location
//                    com.olo.jambajuice.Location.LocationManager.getInstance(this).stopLocationUpdates();
//                    double lat = currentLocation.getLatitude();
//                    double lng = currentLocation.getLongitude();
//                    isLocationBasedSearch = true;
//                    StoreLocatorService.findStoresNearLocation(lat, lng, this, "user_location");
//                } else {
//                    isLocationBasedSearch = false;
////                    createNewOrderForDefaultStore();
////                    StoreLocatorService.findStoresByLocationName(DEFAULT_STORE_SEARCH_KEYWORD, this);
//                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN, true);
                TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
                // createNewOrderBySelectingNewStore();

            }
        }
//        else if (id == R.id.storeBtn)
//        {
//            trackButtonWithName("Stores");
//            TransitionManager.slideUp(HomeActivity.this, StoreLocatorActivity.class);
//        }
//        else if (id == R.id.menuBtn)
//        {
//            openMenu();
//        }
        else if (id == R.id.termsAndPrivacy) {
            trackButtonWithName("Settings");
            TransitionManager.slideUp(HomeActivity.this, SettingsActivity.class);
        } else if (id == R.id.userInfoView) {
            trackButtonWithName("Account & Settings");
            TransitionManager.slideUp(HomeActivity.this, SettingsActivity.class);
        } else if (id == R.id.rewardsLayer) {
            trackButtonWithName("Rewards");
            if (check_offer) {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsAndOfferActivity.class);
            } else {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsActivity.class);

            }

        } else if (id == R.id.welcomeNameAndSettingsView1) {
            trackButtonWithName("Offers");
            if (check_offer) {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsAndOfferActivity.class);
            } else {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsActivity.class);
            }
        } else if (id == R.id.jambaCup) {
            trackButtonWithName("Rewards");
            if (check_offer) {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsAndOfferActivity.class);
            } else {
                TransitionManager.slideUp(HomeActivity.this, MyRewardsActivity.class);

            }

        } else if (id == R.id.recentOrderView) {
            if (recentOrdersExist) {
                trackButtonWithName("Order History");
                Bundle bundle = new Bundle();
                bundle.putString("from", "Main");
                TransitionManager.slideUp(HomeActivity.this, OrderHistoryActivity.class, bundle);
                recentOrdersExist = false;
            } else {
//                if (DataManager.getInstance().isBasketPresent()) {
//                    TransitionManager.transitFrom(HomeActivity.this, BasketActivity.class);
//                } else {
//                    TransitionManager.slideUp(HomeActivity.this, MenuActivity.class);
//                }
            }
        } else if (id == R.id.giftCardView) {
            giftCardOnClick();
        }
    }

    private void giftCardOnClick() {
        enableScreen(false);
        //Check for spendgo user profile has both first name and last name
        User user = UserService.getUser();
        if (user.getFirstname() != null && user.getFirstname().length() != 0 && user.getLastname() != null && user.getLastname().length() != 0) {
            if (DataManager.getInstance().getInCommToken() != null) {
                logincount = 0;
                giftCardNavigation();
            } else {
                if (_app.fbsdkObj != null) {
                    //Commented on 22-nov-2016
                    //if (_app.clpsdkObj.getFBSdkData().getCurrCustomer() != null && JambaApplication.getAppContext().clpsdkObj.getFBSdkData().getCurrCustomer().getCustomerID() > 0) {
                    if (UserService.getUser().getSpendGoId() != null) {
                        IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                            @Override
                            public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                                enableScreen(true);
                                if (successFlag) {
                                    DataManager.getInstance().setInCommToken(tokenSummary);
                                    ((JambaApplication) context.getApplication()).initializeInCommSDK();

                                    giftCardNavigation();
                                } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                    Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                                } else {
                                    // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                    logincount++;
                                    if (logincount < 3) {
                                        enableScreen(false);
                                        JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                            @Override
                                            public void fbSdkLoginCallBack(Boolean success) {
                                                enableScreen(true);
                                                if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                    giftCardOnClick();
                                                } else {
                                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                                }
                                            }
                                        });
                                    } else {
                                        enableScreen(true);
                                        logincount = 0;
                                        Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                    }
                                }
                            }
                        });
                    } else {
                        logincount = 0;
                        enableScreen(true);
                        Utils.alert(context, "Failure", "Server error in saving customer info");
                        _app.updateCustomerInfo(UserService.getUser());
                    }
                } else {
                    logincount = 0;
                    enableScreen(true);
                    Utils.alert(context, "Failure", "Server error in init clp sdk");
                    _app.initializeFBSDK();

                }

            }
        } else {
            String message = "Please take a moment to update your profile information (first and last name) before purchasing a Jamba Card.";
            alertWithYesNo(context, "ProfileUpdate", message, "Attention", "Update Profile", "Cancel");
        }
    }

    public void orderNowClicked(StartOrderCallback startOrderCallback) {
        globalCallBack = startOrderCallback;
        DataManager manager = DataManager.getInstance();
        if (manager.getCurrentSelectedStore() != null && manager.getAllProductFamily() != null && manager.getAllProductFamily().size() > 0) {
            isLocationBasedSearch = false;
            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.MENU_CLICK);
            openMenu();//Open menu if already ready
        } else if (manager.getCurrentSelectedStore() != null) {
            enableScreen(false);
            selectNewOrder(manager.getCurrentSelectedStore(), "order now");
        } else {
            if (UserService.isUserAuthenticated()) {
                enableScreen(false);
                String code = UserService.getUser().getFavoriteStoreCode();
                if (UserService.getUser().getFavoriteStoreCode() != null) {
                    StoreService.getStoreInformation(UserService.getUser().getFavoriteStoreCode(), new StoreDetailCallback() {
                        @Override
                        public void onStoreDetailCallback(Store store, Exception exception) {
                            globalCallBack.onStartOrderCallback(null);
                            if (store != null) {
                                if (store.isSupportsOrderAhead()) {
                                    selectNewOrder(store, "order now");
                                } else {
                                    enableScreen(true);
                                    preferredStoredDoesnotSupportAlert();
                                }
                            } else if (store == null || exception != null) {
                                enableScreen(true);
                                preferredStoredDoesnotSupportAlert();
                            }
                        }
                    });
                } else {
                    enableScreen(true);
                    globalCallBack.onStartOrderCallback(null);
                    NotHavePreferredStoreAlert();
                }
            }
        }
    }

    private void alertWithYesNo(Context context, final String type, String Message, String title, String yesButtonText, String noButtonText) {
        enableScreen(true);
        if (Message == null) {
            Message = "Error";
        }
        if (title == null || title.length() == 0) {
            title = "Failure";
        }
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type.equalsIgnoreCase("ProfileUpdate")) {
                    enableScreen(false);
                    trackButtonWithName("Account & Settings");
                    TransitionManager.slideUp(HomeActivity.this, SettingsActivity.class);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(noButtonText, null);
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void giftCardNavigation() {
        if (GiftCardDataManager.getInstance().getInCommUser() != null) {
            if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                if (GiftCardDataManager.getInstance().getBrands() != null) {
                    enableScreen(true);
                    if (GiftCardDataManager.getInstance().getUserAllCards().size() > 0) {
                        TransitionManager.slideUp(HomeActivity.this, GiftCardHomeListActivityGiftCard.class);
                    } else {
                        TransitionManager.slideUp(HomeActivity.this, NewCardActivityGiftCard.class);
                    }
                } else {
                    getAllBrandsAndNavigate();
                }
            } else {
                getGiftCardsNavigate(GiftCardDataManager.getInstance().getInCommUser().getUserId());
            }
        } else {
            getGiftCardUserIdNavigate();
        }
    }

    private void getAllBrandsAndNavigate() {
        InCommBrandService.getAllBrands(new InCommBrandsCallback() {
            @Override
            public void onAllBrandsCallback(List<InCommBrand> brands, List<InCommCountry> countries, List<InCommStates> states, Exception error) {
                enableScreen(true);
                if (brands != null && countries != null && states != null) {
                    logincount = 0;
                    GiftCardDataManager.getInstance().setBrands(brands);
                    GiftCardDataManager.getInstance().setInCommCountries(countries);
                    GiftCardDataManager.getInstance().setInCommStates(states);
                    GiftCardDataManager.getInstance().setCreditCardTypes(brands.get(0).getCreditCardTypes());
                    if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                        if (GiftCardDataManager.getInstance().getUserAllCards().size() > 0) {
                            TransitionManager.slideUp(HomeActivity.this, GiftCardHomeListActivityGiftCard.class);
                        } else {
                            TransitionManager.slideUp(HomeActivity.this, NewCardActivityGiftCard.class);
                        }
                    } else {
                        TransitionManager.slideUp(HomeActivity.this, NewCardActivityGiftCard.class);
                    }

                } else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(error).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplication()).initializeInCommSDK();
                                enableScreen(false);
                                getAllBrandsAndNavigate();
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                logincount++;
                                if (logincount < 3) {
                                    enableScreen(false);
                                    JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                        @Override
                                        public void fbSdkLoginCallBack(Boolean success) {
                                            enableScreen(true);
                                            if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                getAllBrandsAndNavigate();
                                            } else {
                                                Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                            }
                                        }
                                    });
                                } else {
                                    enableScreen(true);
                                    logincount = 0;
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        }
                    });
                } else if (error != null) {
                    logincount = 0;
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Brands", Utils.getErrorDescription(error));
                    } else {
                        alertWithTryAgain(context, "Brands", "Unexpected error occurred while processing your request. Please try again.");
                    }
                } else {
                    logincount = 0;
                    alertWithTryAgain(context, "Brands", "Unexpected error occurred while processing your request. Please try again.");
                }
            }
        });
    }

    private void alertWithTryAgain(Context context, final String type, String Message) {
        enableScreen(true);
        if(!((Activity) context).isFinishing()) {
            if (Message == null) {
                Message = "Error";
            }
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Failure");
            alertDialogBuilder.setMessage(Message);
            alertDialogBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (type.equalsIgnoreCase("Brands")) {
                        enableScreen(false);
                        getAllBrandsAndNavigate();
                    } else if (type.equalsIgnoreCase("GiftCard")) {
                        enableScreen(false);
                        getGiftCardsNavigate(GiftCardDataManager.getInstance().getInCommUser().getUserId());
                    } else if (type.equalsIgnoreCase("userID")) {
                        enableScreen(false);
                        getGiftCardUserIdNavigate();
                    } else if (type.equalsIgnoreCase("incommToken")) {
                        enableScreen(false);
                        getIncommToken();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void preferredStoredDoesnotSupportAlert() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN_NO_PREFERRED_STORE, true);
        TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
    }

    private void NotHavePreferredStoreAlert() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN, true);
        TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
        //createNewOrderBySelectingNewStore();
    }

//    private void createNewOrderBySelectingNewStore() {
//
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Store Search");
//        alertDialogBuilder.setMessage("Please select search and select a store where your order will be fulfilled ");
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN, true);
//                TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", null);
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }

    //Private Methods
    private void setUpView() {
//        Button store = (Button) findViewById(R.id.storeBtn);
//        Button menu = (Button) findViewById(R.id.menuBtn);
//        store.setOnClickListener(this);
//        menu.setOnClickListener(this);
        if (UserService.isUserAuthenticated()) {
            updateHeader = true;
            getStoreAndSetUpSignedInView(updateHeader);
        } else {
            setUpNonSignedInView();
        }
    }

    private void setUpNonSignedInView() {
        try {
            AnalyticsManager.getInstance().trackScreen("HomeNonSignedView");
            recentOrdersExist = false;
            signedView = null;
            LayoutInflater inflater = LayoutInflater.from(this);
            RelativeLayout contentView = (RelativeLayout) findViewById(R.id.contentView);
            contentView.removeAllViews();
            View view = inflater.inflate(R.layout.home_view_non_signed_in, contentView, false);
            //ImageView glow = (ImageView) findViewById(R.id.background_glow_layout);
            //BitmapUtils.loadBitmapResourceWithViewSize(glow, R.drawable.dashboard_bg, false);
            //  ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            //  scrollView.setBackgroundResource(R.drawable.dashboard_bg);
            //  ImageView jambaLogo = (ImageView) view.findViewById(R.id.jambaLogo);
            //  jambaLogo.setImageResource(R.drawable.jamba_juice_logo);
//        BitmapUtils.loadBitmapResourceWithViewSize(jambaLogo, R.drawable.splash_jamba_juice_logo, false);

            TextView lblMenu = (TextView) view.findViewById(R.id.lbl_menu);
            TextView lblBrowse = (TextView) view.findViewById(R.id.lbl_browse);
            TextView lblSigningup = (TextView) view.findViewById(R.id.lbl_signingup);
            btnSignIn = (Button) view.findViewById(R.id.btn_signIn);
            Button btnSignup = (Button) view.findViewById(R.id.btn_signup);
            llMenu = (LinearLayout) view.findViewById(R.id.ll_menu);
            Button termsAndPrivacy = (Button) view.findViewById(R.id.termsAndPrivacy);
            termsAndPrivacy.setOnClickListener(this);
            ViewTreeObserver vto = llMenu.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        llMenu.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        llMenu.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int width = llMenu.getMeasuredWidth();
                    int height = llMenu.getMeasuredHeight();
                    //   RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
                    //   lp.height = height;
                    //   btnSignIn.setLayoutParams(lp);
                }
            });


//        ImageView jambaStartGuest=(ImageView)view.findViewById(R.id.jambaStartGuest);
//        jambaStartGuest.setImageResource(R.drawable.guest_start);
            lblSigningup.setTypeface(FontsManager.getInstance().getArcherSemiBoldFont());
            lblBrowse.setTypeface(FontsManager.getInstance().getArcherSemiBoldFont());
            lblMenu.setTypeface(FontsManager.getInstance().getArcherSemiBoldFont());
            btnSignIn.setTypeface(FontsManager.getInstance().getArcherSemiBoldFont());
            btnSignup.setTypeface(FontsManager.getInstance().getArcherBoldFont());

            TextView donthaveaccountText = (TextView) view.findViewById(R.id.donthaveaccountText);
            donthaveaccountText.setTypeface(FontsManager.getInstance().getArcherSemiBoldFont());

            //TextView termsAndPrivacy = (TextView) view.findViewById(R.id.termsAndPrivacy);
            //termsAndPrivacy.setOnClickListener(this);
            llMenu.setOnClickListener(this);
            btnSignIn.setOnClickListener(this);
            btnSignup.setOnClickListener(this);
            contentView.addView(view);
        } catch (Exception e) {
            e.printStackTrace();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();

        }


    }// Authenticated user functions

    private void setUpSignedInView() {
        try {
            if (signedView == null) {
                AnalyticsManager.getInstance().trackScreen("HomeSignedInView");


                LayoutInflater inflater = LayoutInflater.from(this);
                RelativeLayout contentView = (RelativeLayout) findViewById(R.id.contentView);
                contentView.removeAllViews();
                {
                    _app = JambaApplication.getAppContext();
                    if (_app.fbsdkObj != null) {
                        if (_app.fbsdkObj.getFBSdkData() != null) {
                            checkoffer = _app.fbsdkObj.getFBSdkData().getMobileSettings().getTRIGGER_INAPPOFFER_ENABLED();
                            check_offer = checkoffer == 1 ? true : false;

                            if (check_offer) {

                                signedView = inflater.inflate(R.layout.home_view_signed_in, contentView, false);
                                RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
                                rewardsView.setOnClickListener(this);
                            } else {

                                signedView = inflater.inflate(R.layout.home_view_signed_reward_in, contentView, false);
                                RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
                                rewardsView.setOnClickListener(this);
                            }

                        } else {

                            signedView = inflater.inflate(R.layout.home_view_signed_reward_in, contentView, false);
                            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
                            rewardsView.setOnClickListener(this);
                        }

                    } else {

                        signedView = inflater.inflate(R.layout.home_view_signed_reward_in, contentView, false);
                        RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
                        rewardsView.setOnClickListener(this);
                    }
                }
                //TextView settings = (TextView) signedView.findViewById(R.id.settingsBtn);
                //  settings.setOnClickListener(this);
                contentView.addView(signedView);
                // ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
                // scrollView.setBackgroundResource(R.drawable.dashboard_signed_in);
                FrameLayout userInfoView = (FrameLayout) signedView.findViewById(R.id.userInfoView);
                userInfoView.setOnClickListener(this);
                LinearLayout menuView = (LinearLayout) signedView.findViewById(R.id.menuView);
                menuView.setOnClickListener(this);
                LinearLayout giftCardView = (LinearLayout) signedView.findViewById(R.id.giftCardView);
                giftCardView.setOnClickListener(this);

                RelativeLayout recentOrderView = (RelativeLayout) signedView.findViewById(R.id.recentOrderView);
                recentOrderView.setOnClickListener(this);
                //Store header block
                LinearLayout menuStoreChangeHeaderInnerLeft = (LinearLayout) findViewById(R.id.menuStoreChangeHeaderInnerLeft);
                menuStoreChangeHeaderInnerLeft.setOnClickListener(this);
                LinearLayout offer = (LinearLayout) findViewById(R.id.welcomeNameAndSettingsView1);
                offer.setOnClickListener(this);


                ImageView jambaCup = (ImageView) findViewById(R.id.jambaCup);
                jambaCup.setOnClickListener(this);

                ImageView imgHeaderProductSearch = (ImageView) findViewById(R.id.imgHeaderProductSearch);
                imgHeaderProductSearch.setOnClickListener(this);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();

        }

    }

    // Authenticated user functions
    private void setUpOrUpdateSignedInView(Store store) {
        enableScreen(true);
        if (store != null) {
            DataManager manager = DataManager.getInstance();
            manager.setCurrentSelectedStore(store);
        }
        if (signedView == null) {
            setUpSignedInView();
        }
        User user = UserService.getUser();
        TextView welcomeMessage = (TextView) signedView.findViewById(R.id.welcomeMessage);
        TextView welcomeMessageName = (TextView) signedView.findViewById(R.id.welcomeMessageName);
        welcomeMessageName.setText(user.getFirstname());
        ImageView avatarImage = (ImageView) signedView.findViewById(R.id.userImage);
        avatarImage.setImageResource(Constants.AVATAR_ICONS[user.getAvatarId()]);
        //Button orderButton = (Button) signedView.findViewById(R.id.orderBtn);
        //orderButton.setOnClickListener(this);

        updateRewardInfo();
        fetchUserRewards();
        fetchRecentOrder();
        //fetchDataOffer();
        updateGiftCardInfo();//get gift card info
        setStoreHeaderText();
        //Store header block
    }

    private void setStoreHeaderText() {
        try {
            DataManager manager = DataManager.getInstance();
            Store store = manager.getCurrentSelectedStore();
            if (!UserService.isUserAuthenticated() || store == null || store.getName() == null || store.getCompleteAddress() == null)
                return;
            com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreTitle = (SemiBoldTextView) findViewById(R.id.headerStoreTitle);
            com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreLocation = (SemiBoldTextView) findViewById(R.id.headerStoreLocation);
            if (DataManager.getInstance().isDebug) {
                headerStoreTitle.setText(Utils.setDemoStoreName(store).getName().replace("Jamba Juice ", ""));
            } else {
                headerStoreTitle.setText(store.getName().replace("Jamba Juice ", ""));
            }
            String location = store.getCompleteAddress();
            headerStoreLocation.setText(location);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchRecentOrder() {
        if (UserService.isUserAuthenticated()) {
            UserService.getRecentOrder(this);
        }
    }

    private void fetchUserRewards() {
        if (UserService.isUserAuthenticated()) // In case user logout and we get response of this call
        {
            RewardService.getUserRewards(this, new RewardSummaryCallback() {
                @Override
                public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception exception) {
                    if (rewardSummary != null) {
                        updateRewardInfo();
                        int size = rewardSummary.getRewardsList().size();
                        Iterator<Reward> it = rewardSummary.getRewardsList().iterator();
                        int count = 0;
                        //Display rewards of type offer only
                        while (it.hasNext()) {
                            Reward reward = it.next();
                            if (reward.getType() == null || (!reward.getType().equals("offer") && !reward.getType().equals("reward"))) {
                                count++;
                                it.remove();
                            }
                        }
                        availableRewardCount = size - count;
                        fetchDataOffer();
                    } else {
                        Utils.tryHandlingAuthTokenExpiry(HomeActivity.this, exception);// We don't need to show error alert to user but handle auth token expiry.
                        fetchDataOffer();
                    }
                }
            });
        }
    }

//    private void fetchAllStore() {
//
//        if (keyValues == null) {
//            int storecode = 0;
//            User user = UserService.getUser();
//            keyValues = HomeActivity.this.getSharedPreferences("HomeActivity", Context.MODE_PRIVATE);
//            if (StringUtilities.isValidString(user.getFavoriteStoreCode())) {
//                storecode = Integer.parseInt(user.getFavoriteStoreCode());
//            } else {
//                storecode = 0;
//            }
//            if (keyValues.getInt(String.valueOf(storecode), 0) <= 0) {
//                //   enableScreen(false);
//                StoreClypService.getStoreClyp(this, new StoreSummaryCallback() {
//                    @Override
//                    public void onStoreSummaryCallback(Map<Integer, Integer> storesMapforId, String error) {
//
//
//                        if (storesMapforId != null) {
//                            //      updateOfferInfo();
//
//
//                            SharedPreferences.Editor keyValuesEditor = keyValues.edit();
//
//                            for (Integer s : storesMapforId.keySet()) {
//                                keyValuesEditor.putInt(String.valueOf(s), storesMapforId.get(s));
//                            }
//
//                            keyValuesEditor.commit();
//                        } else {
//                            //  enableScreen(true);
//                            //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
//                        }
//                    }
//                });
//            }
//        }
//    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//
//
//
//
//        if(Utils.getVersionNameOnly().equalsIgnoreCase(version)&&forceupdate.equalsIgnoreCase("1"))
//        {
//
//        }
//        else if(!Utils.getVersionNameOnly().equalsIgnoreCase(version)&&forceupdate.equalsIgnoreCase("1"))
//        {
//            return;
//        }
//        else
//        {
//            super.onWindowFocusChanged(hasFocus);
//        }
//
//    }


    //
//    public void CheckMobileversion() {
//        _app = JambaApplication.getAppContext();
//        if (_app.fbsdkObj != null) {
//
//            if (_app.fbsdkObj.getFBSdkData() != null) {
//                version = _app.fbsdkObj.getFBSdkData().getMobileSettings().VERSION;
//                information = _app.fbsdkObj.getFBSdkData().getMobileSettings().INFORMATION;
//                forceupdate = _app.fbsdkObj.getFBSdkData().getMobileSettings().FORCEUPDATE;
//                frequency = _app.fbsdkObj.getFBSdkData().getMobileSettings().FREQUENCY;
//
//                _app.setFREQUENCY(frequency);
//                _app.setVersion(version);
//                _app.setForceupdate(forceupdate);
//                sharefequency = _app.getSharedPreferences("JambaApplication", Context.MODE_PRIVATE);
//                // checkversion =forceupdate.equalsIgnoreCase("true") ? 1 :0;
//                if (Utils.getVersionNameOnly().equalsIgnoreCase(version) && forceupdate.equalsIgnoreCase("1")) {
//                    boolean IsFirstSplash = SharedPreferenceHandler.getBoolean(SharedPreferenceHandler.IsFirstSplash, true);
//                    if (sharefequency.getInt("Frequency", 0) == frequency) {
//                        SharedPreferences.Editor keyValuesEditor = sharefequency.edit();
//                        keyValuesEditor.putInt("Frequency", 0);
//                        keyValuesEditor.commit();
//                        Utils.customVersionAlertDialog(this, information);
//                        SharedPreferenceHandler.put(SharedPreferenceHandler.IsFirstSplash, false);
//                    } else if (IsFirstSplash) {
//                        SharedPreferences.Editor keyValuesEditor = sharefequency.edit();
//                        keyValuesEditor.putInt("Frequency", 0);
//                        keyValuesEditor.commit();
//                        Utils.customVersionAlertDialog(this, information);
//                        SharedPreferenceHandler.put(SharedPreferenceHandler.IsFirstSplash, false);
//                    }
//                } else if (!Utils.getVersionNameOnly().equalsIgnoreCase(version) && forceupdate.equalsIgnoreCase("1")) {
//                    Utils.customVersionAlertDialogOne(this, information);
//                    SharedPreferenceHandler.put(SharedPreferenceHandler.IsFirstSplash, false);
//                } else {
//                    SharedPreferenceHandler.put(SharedPreferenceHandler.IsFirstSplash, false);
//
//                }
//            }
//        }
//    }

    public void checkMobileVersion() {
        FBSdk fbSdk = FBSdk.sharedInstance(context);

        String cusId = "1";//Not sure why does need to hard code.
        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback() {
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                Log.d("APP_UPDATE_CHECK", "checkMobileVersion running....");
                Log.d("APP_UPDATE_CHECK", "_app is" + _app);
                Log.d("APP_UPDATE_CHECK", "mobileSettings: " + FBMobileSettingService.sharedInstance().mobileSettings);

                if(DataManager.isDebug) {
                    FBMobileSettingService.sharedInstance().mobileSettings.TRIGGER_APP_EVENTS = 0;
                }

                _app = JambaApplication.getAppContext();
                if (_app != null) {

                    if (FBMobileSettingService.sharedInstance().mobileSettings != null) {
                        version = FBMobileSettingService.sharedInstance().mobileSettings.VERSION;
                        information = FBMobileSettingService.sharedInstance().mobileSettings.INFORMATION;
                        forceupdate = FBMobileSettingService.sharedInstance().mobileSettings.FORCEUPDATE;
                        versioncode = FBMobileSettingService.sharedInstance().mobileSettings.VERSIONCODE;
                        if (StringUtilities.isValidString(version)
                                && StringUtilities.isValidString(forceupdate)
                                && StringUtilities.isValidString(versioncode)) {
                            int appVersion = Integer.parseInt(Utils.getVersionNameOnly().replace(".", ""));
                            int mobileSettingsAppVersion = Integer.parseInt(version.replace(".", ""));
                            int appVersionCode = Integer.parseInt(Utils.getVersionCode());
                            int mobileSettingsAppVersionCode = Integer.parseInt(versioncode);
                            int dbLatestBuildCode = (int) FBPreferences.sharedInstance(context).getLatestBuildCode();
                            if (mobileSettingsAppVersionCode > dbLatestBuildCode) {
                                FBPreferences.sharedInstance(context).setUserDismissedAppUpdate(false);
                            }

                            if (!FBPreferences.sharedInstance(context).isUserDismissedAppUpdate()) {
                                Boolean isAlreadyShown = DataManager.getInstance().getAlreadyShownUpdateScreen();
                                if ((mobileSettingsAppVersion > appVersion)
                                        || (mobileSettingsAppVersionCode > appVersionCode)) {
                                    if (!isAlreadyShown) {
                                        TransitionManager.slideUp(HomeActivity.this, ForceUpdateActivity.class);
                                        DataManager.getInstance().setAlreadyShownUpdateScreen(true);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });

    }

    //dj UserOffer
    private void fetchDataOffer() {

        if (UserService.isUserAuthenticated() && check_offer) // In case user logout and we get response of this call
        {
            //enableScreen(false);
            if (UserService.getUser().getTotalOffers() == -1) {
                final TextView offerdetails = (TextView) signedView.findViewById(R.id.offerdetails);
                offerdetails.setText("Offers Loading...");
                offerdetails.setTextSize(12);
                OfferService.getUserOffer(this, new OfferSummaryCallback() {
                    @Override
                    public void onOfferSummaryCallback(OfferSummary offerSummary, String error) {
                        //enableScreen(true);
                        offerdetails.setText("Rewards & Offers");
                        if (offerSummary != null) {
                            if (check_offer) {
                                updateOfferInfo();
                                offerdetails.setTextSize(12);
                            } else {
                                updateOfferInfo();
                                offerdetails.setTextSize(12);
                            }
                        } else {
                            updateOfferInfo();
                            offerdetails.setTextSize(12);
                            //  enableScreen(true);
                            //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
                        }
                    }
                });
            } else {
                updateOfferInfo();
            }
        }
    }

    private void updateOfferInfo() {
        //   enableScreen(true);
        if (UserService.isUserAuthenticated()) {
            if (signedView == null)
                return;
            User user = UserService.getUser();
            int offerCount = user.getTotalOffers() == -1 ? 0 : user.getTotalOffers();
            int rewardCount = availableRewardCount;
            int value = offerCount + rewardCount;
            if (value > 0) {
                TextView rewardsValue = (TextView) signedView.findViewById(R.id.welc);
                TextView offerdetails = (TextView) signedView.findViewById(R.id.offerdetails);
//                rewardsValue.setText("You have" + " " + String.valueOf(value) + " exciting offers");
//                offerdetails.setText("Your offers are available in the  Rewards and Promotion Section and can be redeemed when you checkout");
                rewardsValue.setText(String.valueOf(value));
            } else {
                TextView rewardsValue = (TextView) signedView.findViewById(R.id.welc);
                TextView offerdetails = (TextView) signedView.findViewById(R.id.offerdetails);
//                offerdetails.setText("Your do not have any offers for now and will soon be getting exciting offers");
//                rewardsValue.setText("You have no offers available");
                rewardsValue.setText("0");
            }
        }
    }


    private void update_RewardInfo() {
        //   enableScreen(true);
        if (UserService.isUserAuthenticated()) {
            if (signedView == null)
                return;

            TextView rewardsValue = (TextView) signedView.findViewById(R.id.welc);
            rewardsValue.setVisibility(View.GONE);
            TextView offerdetails = (TextView) signedView.findViewById(R.id.offerdetails);
            offerdetails.setText("Good things come to those who blend.");
            User user = UserService.getUser();
            int value = user.getTotalRewards();
            if (value > 0) {
                TextView reward_count = (TextView) signedView.findViewById(R.id.reward_count);
                reward_count.setText(String.valueOf(value));
            }
        }
    }

    private void updateRewardInfo() {
        if (UserService.isUserAuthenticated()) {
            if (signedView == null)
                return;
            User user = UserService.getUser();
            int maxPts = user.getThreshold();
            int progress = user.getTotalPoints();
            int value = user.getTotalRewards();
            if (!check_offer) {
                update_RewardInfo();
            }
            TextView rewardsValue = (TextView) signedView.findViewById(R.id.rewardsValue);
            rewardsValue.setText(String.valueOf(progress));
            TextView MaxPts = (TextView) signedView.findViewById(R.id.rewardsValue1);
            MaxPts.setText("/" + String.valueOf(maxPts));

            ImageView jambaCup = (ImageView) findViewById(R.id.jambaCup);
            if (progress <= 0) {
                jambaCup.setImageResource(R.drawable.cup_0);
            } else if (progress <= 5) {
                jambaCup.setImageResource(R.drawable.cup_1);
            } else if (progress <= 10) {
                jambaCup.setImageResource(R.drawable.cup_2);
            } else if (progress <= 15) {
                jambaCup.setImageResource(R.drawable.cup_3);
            } else if (progress <= 20) {
                jambaCup.setImageResource(R.drawable.cup_4);
            } else if (progress <= 25) {
                jambaCup.setImageResource(R.drawable.cup_5);
            } else if (progress <= 30) {
                jambaCup.setImageResource(R.drawable.cup_6);
            } else if (progress <= 35) {
                jambaCup.setImageResource(R.drawable.cup_7);
            }
            /*TextView myRewardCount = (TextView) findViewById(R.id.myRewardCount);
            TextView minPoints = (TextView) findViewById(R.id.minPoints);
            TextView maxPoints = (TextView) findViewById(R.id.maxPoints);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.rewardProgress);

            int minPts = 0;
            int maxPts = user.getThreshold();
            progressBar.setMax(maxPts);
            progressBar.setProgress(user.getTotalPoints());

            myRewardCount.setText(user.getTotalRewards() + "");
            minPoints.setText(minPts + "");
            maxPoints.setText(maxPts + " pts");

            setMidPoints(user.getTotalPoints(), user.getThreshold());*/
        }
    }

    /*private void setMidPoints(int points, int total) {
        float value = (float) points / total;

        ProgressBar layout = (ProgressBar) findViewById(R.id.rewardProgress);
        final TextView midPoints = (TextView) findViewById(R.id.midPoints);
        int width = layout.getWidth();
        final float percentageDisplacement = width * value;

        Utils.addOnGlobalLayoutListener(midPoints, new Runnable() {
            @Override
            public void run() {
                update(midPoints.getMeasuredWidth() / 2, percentageDisplacement, midPoints);
            }
        });
        midPoints.setText(points + "");
    }*/

    private void update(final int difference, final float percentageDisplacement, final TextView midPoints) {
        //final int difference = (right - left)/2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) midPoints.getLayoutParams();
        int resultantMargin = (int) percentageDisplacement - difference;
        params.leftMargin = resultantMargin;
        midPoints.setLayoutParams(params);
    }

    //Update recent orders.
    @Override
    public void onOrderCallback(List<RecentOrder> status, Exception exception) {
        if (UserService.isUserAuthenticated()) // In case user logout and we get response of this call
        {
            updateRecentOrders();
            if (exception != null) {
                Utils.tryHandlingAuthTokenExpiry(HomeActivity.this, exception);// We don't need to show error alert to user but handle auth token expiry.
            }
        }
    }

    private void updateGiftCardInfo() {
        if (UserService.isUserAuthenticated()) {
            if (signedView == null)
                return;

            int totalCards = 0;
            if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                totalCards = GiftCardDataManager.getInstance().getUserAllCards().size();
            }

            TextView jamba_cards_count = (TextView) signedView.findViewById(R.id.jamba_cards_count);
            if (totalCards > 0) {
                jamba_cards_count.setVisibility(View.VISIBLE);
                jamba_cards_count.setText(String.valueOf(totalCards));
            } else {
                jamba_cards_count.setVisibility(View.GONE);
            }
        }
    }


    private void updateRecentOrders() {
        if (UserService.isUserAuthenticated()) {
            if (signedView == null)
                return;
            List<RecentOrder> status = UserService.recentOrder;

            TextView txtOrderTitle1 = (TextView) signedView.findViewById(R.id.txtOrderTitle1);
            TextView txtOrderPrice1 = (TextView) signedView.findViewById(R.id.txtOrderPrice1);
            TextView txtOrderTime1 = (TextView) signedView.findViewById(R.id.txtOrderTime1);
            TextView orderhistory_count = (TextView) signedView.findViewById(R.id.orderhistory_count);
            TextView txtOrderDesc1 = (TextView) signedView.findViewById(R.id.txtOrderDesc1);
            if (status != null && status.size() > 0) {
                signedView.findViewById(R.id.recentOrderView).setVisibility(View.VISIBLE);
                signedView.findViewById(R.id.recentOrderNewView).setVisibility(View.GONE);

                signedView.findViewById(R.id.ll_recent1).setVisibility(View.VISIBLE);
                txtOrderTitle1.setText(status.get(0).getFofpName());
                txtOrderTime1.setText(status.get(0).getOrderTimeStatement());
                txtOrderPrice1.setText("$" + String.format("%.02f", status.get(0).getTotal()));
                if (orderhistory_count != null) {
                    orderhistory_count.setText(String.valueOf(status.size()));
                }
                if (txtOrderDesc1 != null) {
                    txtOrderDesc1.setText("Struggling to eat enough greens? Start drinking them! A bend of apple and strawberry juices...");
                }
                recentOrdersExist = true;
            } else {
                recentOrdersExist = false;
                signedView.findViewById(R.id.recentOrderView).setVisibility(View.GONE);
                signedView.findViewById(R.id.recentOrderNewView).setVisibility(View.VISIBLE);
                signedView.findViewById(R.id.ll_recent1).setVisibility(View.GONE);
//                if (DataManager.getInstance().isBasketPresent()) {
//                    detail.setText("ORDER IN PROGRESS");
//                } else if (status == null || status.size() == 0) {
//                    detail.setText("START AN ORDER");
//                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Handle any Google Play Services errors
        //Removed urbanairship
//        if (PlayServicesUtils.isGooglePlayStoreAvailable()) {
//            PlayServicesUtils.handleAnyPlayServicesError(this);
//        }
    }

    private void checkGooglePlayServices() {
        if (checkPlayServices()) {
            //checkAccessToken();
//            fetchUserRewards();
//            fetchRecentOrder();
//            //   fetchAllStore();
//            //fetchDataOffer();
//            startLocationUpdate();
//            checkForCrashes();
//            checkMobileVersion();
//            //checkBluetoothEnabled();
//            if (UserService.isUserAuthenticated()) {
//                setStoreHeaderText();
//            }

            fetchUserRewards();
            fetchRecentOrder();
            startLocationUpdate();
            checkForCrashes();
            checkMobileVersion();
            //checkBluetoothEnabled();
            if (UserService.isUserAuthenticated()) {
                setStoreHeaderText();
                //initializeInCommSdk and get giftcardinfo
                enableScreen(false);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());
                try {
                    if (sdf.parse(formattedDate).before(sdf.parse("2017-01-01"))) {
                        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        int flag = sharedPreferences.getInt("flag", 0);
                        boolean isAlreadyShown = sharedPreferences.getBoolean("isAlreadyShown", true);
                        if (flag == 1) {
                            if (!isAlreadyShown) {
                                showFreeSmoothieAlert();
                            }
                        }
                    }
                } catch (ParseException e) {

                }
                //testing
                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();

                if (DataManager.getInstance().getInCommToken() == null) {
                    if (_app.fbsdkObj != null) {
                        if (_app.fbsdkObj.getFBSdkData().getCurrCustomer() != null && JambaApplication.getAppContext().fbsdkObj.getFBSdkData().getCurrCustomer().getCustomerID() > 0) {
                            getIncommToken();
                        } else {
                            enableScreen(true);
                            //Commented on 22-nov-2016
                            //Utils.alert(context, "Failure", "Unexpected error occured. Please notify us.");
                            _app.updateCustomerInfo(UserService.getUser());
                        }
                    } else {
                        enableScreen(true);
                        //Commented on 22-nov-2016
                        //Utils.alert(context, "Failure", "Unexpected error occured. Please notify us.");
                        _app.initializeFBSDK();
                    }
                } else {
                    enableScreen(true);
                    if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                        updateGiftCardInfo();
                    } else {
                        fetchGiftCardData();
                    }

                }
            }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGooglePlayServices();
    }

    private void checkAccessToken() {
        if (FBPreferences.sharedInstance(context).getAccessTokenforapp() == null) {
            JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                @Override
                public void fbSdkLoginCallBack(Boolean success) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearPopUpForOnDestroy();
    }

    private void clearPopUpForOnDestroy() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int flag = sharedPreferences.getInt("flag", 0);
        if (flag == 1) {
            editor.putBoolean("isAlreadyShown", false);
        } else {
            editor.putBoolean("isAlreadyShown", true);
        }
        editor.commit();
    }

    private void clearPopUpForOnPause() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int flag = sharedPreferences.getInt("flag", 0);
        if (flag == 1) {
            editor.putBoolean("isAlreadyShown", true);
        } else {
            editor.putBoolean("isAlreadyShown", false);
        }
        editor.commit();
    }

    private void getIncommToken() {
        IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
            @Override
            public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                enableScreen(true);
                if (successFlag) {
                    logincount = 0;
                    DataManager.getInstance().setInCommToken(tokenSummary);
                    ((JambaApplication) context.getApplication()).initializeInCommSDK();

                    if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                        updateGiftCardInfo();
                    } else {
                        fetchGiftCardData();
                    }
                } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                    logincount = 0;
                    Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                } else {
                    // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                    logincount++;
                    if (logincount < 3) {
                        enableScreen(false);
                        JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                            @Override
                            public void fbSdkLoginCallBack(Boolean success) {
                                enableScreen(true);
                                if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                    getIncommToken();
                                } else {
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        });
                    } else {
                        enableScreen(true);
                        logincount = 0;
                        Utils.alert(context, "Failure", "Unable to proceed gift card service");
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
//        com.olo.jambajuice.Location.LocationManager.getInstance(this).stopLocationUpdates();
        UpdateManager.unregister();
        clearPopUpForOnPause();
    }

    private void fetchGiftCardData() {

        if (GiftCardDataManager.getInstance().getInCommUser() != null) {
            getGiftCards(GiftCardDataManager.getInstance().getInCommUser().getUserId());
        } else {
            getGiftCardUserId();
        }

    }

    private void getGiftCardUserId() {
        enableScreen(false);
        InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
            @Override
            public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {
                enableScreen(true);
                if (inCommUser != null) {
                    logincount = 0;
                    GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                    String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
                    getGiftCards(userId);

                } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(exception).equals(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(exception).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getGiftCardUserId();
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                logincount = 0;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                logincount++;
                                if (logincount < 3) {
                                    enableScreen(false);
                                    JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                        @Override
                                        public void fbSdkLoginCallBack(Boolean success) {
                                            enableScreen(true);
                                            if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                getGiftCardUserId();
                                            } else {
                                                Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                            }
                                        }
                                    });
                                } else {
                                    enableScreen(true);
                                    logincount = 0;
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        }
                    });
                } else {
                    enableScreen(true);
                    logincount = 0;
                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                }
            }
        });
    }

    private String convertDate(Date dateString) {
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = output.format(dateString);
        return formattedDate;
    }

    private String convertTime(Date dateString) {
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
        String formattedTime = output.format(dateString);
        return formattedTime;
    }

    private void getGiftCards(final String userId) {
        enableScreen(false);
        InCommCardService.getAllCard(userId, new InCommGetAllCardServiceCallBack() {
            @Override
            public void onGetAllCardServiceCallback(List<InCommCard> allCards, Exception exception) {
                GiftCardDataManager.getInstance().setInCommCards(allCards);//array
                if (allCards != null) {
                    logincount = 0;
                    enableScreen(true);
                    int size = allCards.size();
                    HashMap<Integer, InCommCard> inCommCardHashMap = new HashMap<Integer, InCommCard>();
                    for (int i = 0; i < size; i++) {
                        if (allCards.get(i) != null) {
                            inCommCardHashMap.put(allCards.get(i).getCardId(), allCards.get(i));
                        }
                    }

                    GiftCardDataManager.getInstance().setUserAllCards(inCommCardHashMap);//hashmap
                    Calendar calendar = Calendar.getInstance();
                    String date = convertDate(calendar.getTime());
                    String time = convertTime(calendar.getTime());
                    DataManager.getInstance().setLastCheckedDateAndtime(date + " at " + time);
                    updateGiftCardInfo();//ui

                } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(exception).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getGiftCards(userId);
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                logincount = 0;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                logincount++;
                                if (logincount < 3) {
                                    enableScreen(false);
                                    JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                        @Override
                                        public void fbSdkLoginCallBack(Boolean success) {
                                            enableScreen(true);
                                            if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                getGiftCards(userId);
                                            } else {
                                                Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                            }
                                        }
                                    });
                                } else {
                                    enableScreen(true);
                                    logincount = 0;
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        }
                    });
                } else {
                    enableScreen(true);
                    logincount = 0;
                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                }
            }
        });
    }

    private void getGiftCardUserIdNavigate() {
        InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
            @Override
            public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {
                if (inCommUser != null) {
                    logincount = 0;
                    GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                    String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
                    getGiftCardsNavigate(userId);
                } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(exception).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplication()).initializeInCommSDK();
                                enableScreen(false);
                                getGiftCardUserIdNavigate();
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                logincount = 0;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                logincount++;
                                if (logincount < 3) {
                                    enableScreen(false);
                                    JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                        @Override
                                        public void fbSdkLoginCallBack(Boolean success) {
                                            enableScreen(true);
                                            if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                getGiftCardUserIdNavigate();
                                            } else {
                                                Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                            }
                                        }
                                    });
                                } else {
                                    enableScreen(true);
                                    logincount = 0;
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        }
                    });
                } else if (exception != null) {
                    logincount = 0;
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "userID", Utils.getErrorDescription(exception));
                    } else {
                        alertWithTryAgain(context, "userID", "Unexpected error occurred while processing your request. Please try again.");
                    }
                } else {
                    logincount = 0;
                    alertWithTryAgain(context, "userID", "Unexpected error occurred while processing your request. Please try again.");
                }
            }
        });
    }


    private void getGiftCardsNavigate(String userId) {
        enableScreen(false);
        InCommCardService.getAllCard(userId, new InCommGetAllCardServiceCallBack() {
            @Override
            public void onGetAllCardServiceCallback(List<InCommCard> allCards, Exception exception) {
                enableScreen(true);
                GiftCardDataManager.getInstance().setInCommCards(allCards);
                if (allCards != null) {
                    logincount = 0;
                    int size = allCards.size();
                    HashMap<Integer, InCommCard> inCommCardHashMap = new HashMap<Integer, InCommCard>();
                    for (int i = 0; i < size; i++) {
                        if (allCards.get(i) != null) {
                            inCommCardHashMap.put(allCards.get(i).getCardId(), allCards.get(i));
                        }
                    }

                    GiftCardDataManager.getInstance().setUserAllCards(inCommCardHashMap);
                    Calendar calendar = Calendar.getInstance();
                    String date = convertDate(calendar.getTime());
                    String time = convertTime(calendar.getTime());
                    DataManager.getInstance().setLastCheckedDateAndtime(date + " at " + time);
                    updateGiftCardInfo();//ui
                    getAllBrandsAndNavigate();

                } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(exception).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplication()).initializeInCommSDK();
                                enableScreen(false);
                                getGiftCardsNavigate(GiftCardDataManager.getInstance().getInCommUser().getUserId());
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                logincount = 0;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                logincount++;
                                if (logincount < 3) {
                                    enableScreen(false);
                                    JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                        @Override
                                        public void fbSdkLoginCallBack(Boolean success) {
                                            enableScreen(true);
                                            if (FBPreferences.sharedInstance(context).getAccessTokenforapp() != null) {
                                                getGiftCardsNavigate(GiftCardDataManager.getInstance().getInCommUser().getUserId());
                                            } else {
                                                Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                            }
                                        }
                                    });
                                } else {
                                    enableScreen(true);
                                    logincount = 0;
                                    Utils.alert(context, "Failure", "Unable to proceed gift card service");
                                }
                            }
                        }
                    });
                } else if (exception != null) {
                    logincount = 0;
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "GiftCard", Utils.getErrorDescription(exception));
                    } else {
                        alertWithTryAgain(context, "GiftCard", "Unexpected error occurred while processing your request. Please try again.");
                    }
                } else {
                    logincount = 0;
                    alertWithTryAgain(context, "GiftCard", "Unexpected error occurred while processing your request. Please try again.");
                    GiftCardDataManager.getInstance().setInCommCards(null);
                    GiftCardDataManager.getInstance().setUserAllCards(null);
                }

            }
        });
    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        updateSettingMenuTitle();
        Boolean isItFromSettingScreen = false;
        if (UserService.isUserAuthenticated()) {
            if (intent.getExtras() != null) {
                isItFromSettingScreen = intent.getBooleanExtra(Constants.B_IS_IT_FROM_SETTINGS_SCREEN, false);
                if (isItFromSettingScreen) {
                    updateHeader = false;//this is from setting activity so no need to update header if preferred store is order ahead or not oreder ahead
                } else {
                    updateHeader = true;//if it is from login screen or some other screens except setting screen
                }
            } else {
                updateHeader = true;//if it is from login screen or some other screens except setting screen
            }


            if (!isItFromSettingScreen) {
                //initializeInCommSdk and get giftcardinfo
//                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                int flag = sharedPreferences.getInt("flag",0);
//                if(flag == 1){
//                    showFreeSmoothieAlert();
//                }
//                enableScreen(false);
//                if (DataManager.getInstance().getInCommToken() == null) {
//                    getIncommToken();
//                } else {
//                    enableScreen(true);
//                    if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
//                        updateGiftCardInfo();
//                    } else {
//                        fetchGiftCardData();
//                    }
//                }
//                ((JambaApplication) this.getApplication()).initializeInCommSDK();
//                if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
//                    updateGiftCardInfo();
//                } else {
//                    fetchGiftCardData();
//                }
            }
            getStoreAndSetUpSignedInView(updateHeader);
        } else {
            setUpNonSignedInView();
        }
    }

    private void showFreeSmoothieAlert() {
        //Commented on Dec-5-2016
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
//        alertDialogBuilder.setTitle("Gift Card Promotion");
//        alertDialogBuilder.setMessage("Now through Dec 31, buy a Jamba Card of $25 or more and receive a free smoothie coupon by email. Free smoothie must be redeemed in-store by 1/11/17.");
//        alertDialogBuilder.setPositiveButton("Not Right Now", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("flag", 1);
//                editor.putBoolean("isAlreadyShown", false);
//                editor.commit();
//            }
//        });
//        alertDialogBuilder.setNegativeButton("I'm Not Interested", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("flag", 0);
//                editor.putBoolean("isAlreadyShown", true);
//                editor.commit();
//            }
//        });
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
    }

    private void getStoreAndSetUpSignedInView(final Boolean updateHeader) {


        setUpSignedInView();
        DataManager manager = DataManager.getInstance();
        if (manager.getCurrentSelectedStore() == null || manager.getCurrentSelectedStore().getName() == null) {
            enableScreen(false);
            Gson gson = new Gson();
            String lastSelectedStore = SharedPreferenceHandler.getString(SharedPreferenceHandler.LastSelectedStore, "");
            Store store = gson.fromJson(lastSelectedStore, Store.class);
            if (store != null && store.getName() != null) {
                setUpOrUpdateSignedInView(store);//set session store in store header
            } else {
                StoreService.getPreferredStore(this, new PreferredStoreCallBack() {
                    @Override
                    public void onPreferredStoreCallback(Exception exception, Store store) {
                        if (exception != null) {
//                                enableScreen(true);
//                                Utils.showErrorAlert(context, exception);
                            // setNearestStoreForSignedInView();//Try to download nearest store
                            setNoStoreForSignedInView();
                            return;
                        }
                        if (store != null && store.getName() != null) {
                            if (store.isSupportsOrderAhead()) {
                                // store.setName(store.getName().replace("Jamba Juice ", ""));
                                if (updateHeader) {
                                    setUpOrUpdateSignedInView(store);//select store in store header and update storename in header
                                } else {
                                    setNoStoreForSignedInView(); //don't update storename in header
                                }

                            } else {
                                setNoStoreForSignedInView();
                            }
                        } else {
                            //setNearestStoreForSignedInView();//set nearest store in store header
                            setNoStoreForSignedInView();
                        }
                    }

                    @Override
                    public void onPreferredStoreErrorCallback(Exception exception) {
//                            if (exception != null)
//                            {
//                                enableScreen(true);
//                                Utils.showErrorAlert(context, exception);
//                                return;
//                            }
                        //setNearestStoreForSignedInView();//set nearest store in store header
                        setNoStoreForSignedInView();
                    }
                });
            }
        } else {
            setUpOrUpdateSignedInView(manager.getCurrentSelectedStore());//set session store in store header
        }

    }


    private Store getNearestStoreFromList(ArrayList<Store> stores) {
        if (stores == null)
            return null;
        if (stores != null) {
            for (Store newStore : stores) {
                if (newStore.isSupportsOrderAhead()) {
                    return newStore;
                }
            }
        }
        return null;
    }

    private void setNearestStoreForSignedInView() {
        if (currentLocation != null) {
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();
            StoreLocatorService.findStoresNearLocation(lat, lng, new StoreServiceCallback() {
                @Override
                public void onStoreServiceCallback(ArrayList<Store> stores, Exception exception) {
                    if (exception != null) {
                        //  enableScreen(true);
                        // Utils.showErrorAlert(context, exception);
                        setNoStoreForSignedInView();//Try to download default store
                        return;
                    }
                    Store selectedStore = getNearestStoreFromList(stores);
                    if (selectedStore != null) {
                        setUpOrUpdateSignedInView(selectedStore);//set nearest store as selected store in header
                    } else {
                        setNoStoreForSignedInView();
                    }
                }
            }, "user_location");
        } else {
            setNoStoreForSignedInView();
        }
    }

    private void setNoStoreForSignedInView() {
        enableScreen(true);
        if (signedView == null) {
            setUpSignedInView();
        }

        User user = UserService.getUser();

        TextView welcomeMessageName = (TextView) signedView.findViewById(R.id.welcomeMessageName);
        ImageView avatarImage = (ImageView) signedView.findViewById(R.id.userImage);
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreTitle = (SemiBoldTextView) findViewById(R.id.headerStoreTitle);
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreLocation = (SemiBoldTextView) findViewById(R.id.headerStoreLocation);

        welcomeMessageName.setText(user.getFirstname());
        avatarImage.setImageResource(Constants.AVATAR_ICONS[user.getAvatarId()]);
        headerStoreTitle.setText("Store not saved?");
        headerStoreLocation.setText("Tap to find your nearest store");

        updateRewardInfo();
        fetchUserRewards();
        fetchRecentOrder();
        updateGiftCardInfo();
//        StoreService.getStoreInformation(String.valueOf(DEFAULT_STORE_CODE), new StoreDetailCallback() {
//            @Override
//            public void onStoreDetailCallback(Store store, Exception exception) {
//                if (exception != null) {
//                    enableScreen(true);
//                    Utils.showErrorAlert(context, exception);
//                    return;
//                }
//                if (store != null) {
//                    if (store.getName() != null) {
//                        store.setName(store.getName().replace("Jamba Juice ", ""));
//                    }
//                    setUpOrUpdateSignedInView(store);//set nearest store as selected store in header
//                } else {
//                    enableScreen(true);
//                    Utils.showErrorAlert(context, new Exception("Could not find a store for you. Please try again"));
//
//                }
//            }
//        });
    }

    private void openMenu() {

        long lastPullTime = SharedPreferenceHandler.getInstance().getLong(LastProductUpdate, -1);
        if (System.currentTimeMillis() - lastPullTime > TwentyFourHourInMiliSeconds || lastPullTime == -1) {
            enableScreen(false);
        }
        loadAdConfig();
    }

    //load ad configuartion
    private void loadAdConfig() {
        ProductService.loadAdsConfig(this, new ProductAdsServiceCallback() {
            @Override
            public void onProductAdsCallback(ArrayList<ProductAd> productAds, Exception exception) {
                DataManager manager = DataManager.getInstance();
                manager.setSelectedStoreProductAd(productAds);// set filtered ads
                loadAdDetails();
            }
        });
    }

    //load ads details
    private void loadAdDetails() {
        ProductService.loadAllAdDetails(this, new ProductAdDetailsServiceCallback() {
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
        if (globalCallBack != null) {
            globalCallBack.onStartOrderCallback(null);
        }
        if (DataManager.getInstance().getCurrentSelectedStore() != null && DataManager.getInstance().getCurrentSelectedStore().getName() != null) {
            trackButtonWithName("OrderNow");
            if ((((DataManager.getInstance().getSelectedStoreProductAdDetail() != null
                    && DataManager.getInstance().getSelectedStoreProductAdDetail().size() > 0)
                    || (DataManager.getInstance().getSelectedStoreFeaturedProducts() != null
                    && DataManager.getInstance().getSelectedStoreFeaturedProducts().size() > 0)
                    || (DataManager.getInstance().getRecentOrderList() != null
                    && DataManager.getInstance().getRecentOrderList().size() > 0)))) {
                TransitionManager.slideUp(HomeActivity.this, MenuActivity.class);

            } else if (DataManager.getInstance().getSelectedStoreProductFamily() != null
                    && DataManager.getInstance().getSelectedStoreProductFamily().size() > 0) {
                TransitionManager.slideUp(HomeActivity.this, ProductFamiliesActivity.class);
            }

        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN, true);
            TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
//            Utils.showErrorAlert(this, new Exception("Not able to find a store for you. Please try again!"));
            // createNewOrderBySelectingNewStore();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getBoolean(Constants.B_OPEN_MENU)) {
                Log.e("Error scenario", "Error scenario");
                openMenu();
            }
        }
    }

    @Override
    public void onLocationCallback(Location location) {
        currentLocation = location;
    }

    @Override
    public void onConnectionFailedCallback() {
        com.olo.jambajuice.Location.LocationManager.getInstance(this).stopLocationUpdates();
    }

    private void startLocationUpdate() {
        com.olo.jambajuice.Location.LocationManager locationManager = com.olo.jambajuice.Location.LocationManager.getInstance(this);
        if (locationManager.isLocationServicesEnabled()) {
            AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.SEARCH.value, "store_search", "user_location");
            com.olo.jambajuice.Location.LocationManager.getInstance(this).startLocationUpdates(this);
        } else {
            //Notify user that location service is not enabled
            locationManager.showLocationServiceNotAvailableAlert();
        }
    }

    @Override
    public void onStoreServiceCallback(ArrayList<Store> stores, Exception exception) {
        Store selectedStore = null;
        if (stores != null) {
            for (Store newStore : stores) {
                if (newStore.isSupportsOrderAhead()) {
                    selectedStore = newStore;
                    break;
                }
            }
        }
        if (selectedStore != null) {
            selectNewOrder(selectedStore, "order now");
        } else {
            if (isLocationBasedSearch) {
                isLocationBasedSearch = false;
//                StoreLocatorService.findStoresByLocationName(DEFAULT_STORE_SEARCH_KEYWORD, this);
                // createNewOrderBySelectingNewStore();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN, true);
                TransitionManager.transitFrom(HomeActivity.this, StoreLocatorActivity.class, bundle);
            } else {
                if (exception != null) {
                    enableScreen(true);
                    Utils.showErrorAlert(this, exception);
                    return;
                }
                enableScreen(true);
                Utils.showErrorAlert(this, new Exception("Not able to find a store for you. Please try again!"));
            }
        }
    }

    private void selectNewOrder(Store selectedStore, final String from) {
        DataManager.getInstance().resetDataManager();
        DataManager.getInstance().setCurrentSelectedStore(selectedStore);
        ProductService.startNewOrderForStore(this, selectedStore, new AllStoreMenuCallBack() {
            @Override
            public void onAllStoreMenuCallback(Exception exception) {
                if (exception != null) {
                    if (globalCallBack != null) {
                        globalCallBack.onStartOrderCallback(null);
                    }
                    enableScreen(true);
                    Utils.showErrorAlert(context, exception);
                    return;
                }
                Log.i("Callback", "Success");
                enableScreen(true);
                if (from.equals("product search")) {
                    if (globalCallBack != null) {
                        globalCallBack.onStartOrderCallback(null);
                    }
                    TransitionManager.transitFrom(HomeActivity.this, ProductSearchActivity.class);
                }
                if (from.equals("order now")) {
                    openMenu();
                }

            }

            @Override
            public void onAllStoreMenuErrorCallback(Exception exception) {
                if (exception != null) {
                    enableScreen(true);
                    Utils.showErrorAlert(context, exception);
                    return;
                }
                enableScreen(true);
                Log.i("Callback", "Error");
            }
        });

    }

}