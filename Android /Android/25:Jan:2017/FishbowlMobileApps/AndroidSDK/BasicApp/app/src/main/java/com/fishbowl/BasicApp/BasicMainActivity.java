package com.fishbowl.BasicApp;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBGiftCardCreateRequestItem;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.SavePay.SaveGiftWidget;
import com.fishbowl.basicmodule.SavePay.SaveUpdateGiftWidget;
import com.fishbowl.basicmodule.SavePay.UpdateGiftWidget;
import com.fishbowl.basicmodule.Services.FBGiftService;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBOfferService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
import com.fishbowl.basicmodule.Utils.Config;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.FBUtils;
import com.fishbowl.basicmodule.Utils.NotificationUtils;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.fishbowl.basicmodule.Controllers.FBSdk.context;


/**
 * Created by digvijay(dj)
 */

public class BasicMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
    public static final int SAVE_TO_ANDROID = 888;
    private static final String DEFAULT_STORE_CODE = "0145";//Emeryville - 0145 : Merced - 0620
    private static final String DEFAULT_STORE_SEARCH_KEYWORD = "Emeryville";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = BasicMainActivity.class.getSimpleName();
    public AuthApplication _app;
    Button bbl;
    Bundle extras;
    String offerid = null;
    String clpnid = null;
    String ntype = "";
    JSONObject json_object;
    int diifer;
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer pmPromotionID;
    Integer offerID;
    Integer channelID;
    String validityEndDateTime;
    private String mPassDataUrl = "";
    private String passPreviewImageURL = "";
    private String e = "";
    private ArrayList<FBOfferItem> offerList;
    private int offerCount;
    private NotificationUtils notificationUtils;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_main);

        String token = FirebaseInstanceId.getInstance().getToken();
        pushNotificationRecieve();
        UpdatePay();
        SavePay();


       //1.Save Button Initialisation Option
        SaveGiftWidget savegift = (SaveGiftWidget) findViewById(R.id.savegift);
        savegift.initSaveGiftWidget(this, collectGiftCardCreateRequest());

        //2.Update Button Initialisation Option
        UpdateGiftWidget updategift = (UpdateGiftWidget) findViewById(R.id.updategift);
        updategift.initUpdateGiftWidget(this, collectGiftCardCreateRequest());

        //3.Save and Update both Button Initialisation Option
        SaveUpdateGiftWidget saveupdategift = (SaveUpdateGiftWidget) findViewById(R.id.saveupdategift);
        saveupdategift.initSaveUpdateGiftWidget(this, collectGiftCardCreateRequest());

        this._app = AuthApplication.getAppContext();

        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        if (_app.fbsdkObj != null) {
            FBAnalytic.sharedInstance().init(this._app, _app.fbsdkObj.SERVER_URL, "91225258ddb5c8503dce33719c5deda7");
        } else {
            //  FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.SERVER_URL), "91225258ddb5c8503dce33719c5deda7");

        }

        Intent i = getIntent();
        extras = i.getExtras();
        {
            if (extras != null) {

                if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);
                        Log.d(TAG, "Key: " + key + " Value: " + value);
                    }
                }

//                try {
//                    json_object = new JSONObject(i.getStringExtra("datapayloadjson"));
//
//                    Log.e(TAG, "Example Item: " + json_object.getString("KEY"));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                this.mPassDataUrl = i.getStringExtra("data");
                ntype = i.getStringExtra("ntype");
                String apsStr = i.getExtras().getString("aps");
                passPreviewImageURL = i.getExtras().getString("previewimage");
                try {
                    if (apsStr != null) {
                        //  enableScreen(false);
                        JSONObject apsJson = new JSONObject(apsStr);
                        if (apsJson.has("alert")) {
                            e = apsJson.getString("alert");
                            if (extras.getString("offerid") != null && (extras.getString("clpnid") != null)) {
                                offerid = extras.getString("offerid");
                                clpnid = extras.getString("clpnid");

                            }
                            String apsStr1 = i.getExtras().getString("offerid");
                            String clpnid2 = i.getExtras().getString("clpnid");
                            //  JambaAnalyticsManager.sharedInstance().track_OfferItemWith(apsStr1,clpnid2,apsStr1,clpnid2, FBEventSettings.PUSH_OPEN);
                            // It has been implemented in fbSdk processPushMessage SO I have Remove It

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //  GiftCardCreateObjectRequest();
        getToken();
        // EventCheck();

//
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                    txtMessage.setText(message);
//                }
//            }
//        };
//
//        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }


    @Override
    protected void onResume() {
        super.onResume();
//        SaveBottomActivity b = (SaveBottomActivity) findViewById(R.id.bottom_toolbar);
//        b.initBottomToolbar(this,collectGiftCardCreateRequest());

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (extras != null && (StringUtilities.isValidString(e))) {

        } else {

        }
    }

    @Override
    public void onClick(View v) {

    }

    public void loginMember() {
        final JSONObject object = new JSONObject();


        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {
            public void onGetTokencallback(JSONObject response, Exception error) {
                //  getMobileSettings();
                //  getToken();

            }
        });
    }


    public void registerMember() {
        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {

            }
        });
    }


    public void registerMemberUpdatebyEmailApi() {

        final JSONObject object = new JSONObject();
        try {

            object.put("email", "vkumarsss_ic@fishbowl.com");// your email id
            object.put("appId", "com.fishbowl.BasicApp"); //  your package id
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
            object.put("pushOptIn", true);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOSVersion", FBConstant.device_os_ver);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().createMemberUpdatebyEmail(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "createMemberUpdatebyEmail Success Message" + response);

                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "createMemberUpdatebyEmail Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }



    public void getToken() {

        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    //   Constants.alertDialogShow(BasicMainActivity.this,"GetToken Success Message");
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(BasicMainActivity.this).setAccessToken(secratekey);
                    Constants.alertDialogShow(BasicMainActivity.this, "getTokenApi Success Message" + response);
                 //  mobileSettings();
                    registerMemberUpdatebyEmailApi();

                } else {

//                    String errormessage = FBUtility.getErrorDescription(error);
//                    Constants.alertDialogShow(BasicMainActivity.this, errormessage);

                    Constants.alertDialogShow(BasicMainActivity.this, "getTokenApi Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);

                }

            }
        });
    }


    public void mobileSettings() {
        String cusId = "0";

        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback() {
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                if (state) {
                    Log.d("Mobile Settings  Api", "Success");
                    Constants.alertDialogShow(BasicMainActivity.this, "MobileSettingsService Success Message" + state);

                    //EventCheckGuest();
                    track_ItemWith("12345", "Lunch", FBEventSettings.FORGOT_PASSWORD);
                   // getViewMobileSettingsServiceSettings();

                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "MobileSettingsService Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    public void getViewMobileSettingsServiceSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(JSONObject response, final Exception error) {

                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "MobileSettingsService Success Message" + response);
                    ChecktwentyloginMember();
                    //EventCheckGuest();

                } else {

                    Constants.alertDialogShow(BasicMainActivity.this, "MobileSettingsService Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }

    public void ChecktwentyloginMember() {

        JSONObject object = new JSONObject();
        try {
//
//            object.put("username", "989-720-7580");
//            object.put("password", "password");

            object.put("username", "987-308-7785");
            object.put("password", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {
                    try {
                        Constants.alertDialogShow(BasicMainActivity.this, "loginMember Success Message" + response);
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(BasicMainActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(BasicMainActivity.this).setSignin(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CheckTwentyApi();

                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "loginMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }

        });
    }




    public void CheckTwentyApi() {


        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {


                    Constants.alertDialogShow(BasicMainActivity.this, "getMember Success Message" + response.toString());
                   // deviceUpdate();
                    track_ItemWith("12345", "Lunch", FBEventSettings.APPLY_COUPON);
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "getMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }

        });
    }


    public void deviceUpdate() {


        JSONObject object = new JSONObject();

        Member member = FBUserService.sharedInstance().member;

        try {
            object.put("memberid", FBPreferences.sharedInstance(this).getUserMemberforAppId());
            Constants.alertDialogShow(BasicMainActivity.this, "MemberID of user " + FBPreferences.sharedInstance(this).getUserMemberforAppId());
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
            Constants.alertDialogShow(BasicMainActivity.this, "GCM Push token of user " + FBPreferences.sharedInstance(this).getPushToken());
            object.put("appId", "com.fishbowl.BasicApp");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().deviceUpdate(object, new FBUserService.FBDeviceUpdateCallback() {
            @Override
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    try {
                        Constants.alertDialogShow(BasicMainActivity.this, "deviceUpdate Success Message" + response);
                        //  EventCheck();
                        //  memberUpdate();
                        // getRefreshToken();
                        track_ItemWith("12345", "Lunch", FBEventSettings.APPLY_COUPON);
                      //  updateGiftAndroidSavePay(collectGiftCardCreateRequest());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "deviceUpdate Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }




    private FBCustomerItem collectCustomerData() {

        FBCustomerItem customer = new FBCustomerItem();
        customer.firstName = "Dj";
        customer.lastName = "Kumar";
        customer.emailID = "vkumar_ic@fishbowl.com";
        customer.loginID = "";
        customer.customerAge = "";
        customer.customerGender = "Male";
        customer.cellPhone = "956-071-7227";
        customer.loyalityNo = "";
        customer.addressLine1 = "";
        customer.addressLine2 = "";
        customer.addressCity = "";
        customer.addressState = "";
        customer.addressZip = "";
        customer.favoriteDepartment = "";
        customer.customerTenantID = "581";
        customer.statusCode = 1;
        customer.deviceId = "3ae347ca6e2a2d6c";
        int storecode = 145; //Default store - Emeryville
        customer.homeStore = Integer.toString(storecode);
        customer.pushOpted = "N";
        customer.smsOpted = "N";
        customer.emailOpted = "N";
        customer.phoneOpted = "N";
        customer.adOpted = "N";
        customer.loyalityRewards = "150";
        return customer;
    }

    private FBCustomerItem collectCustomerDataMemberUpdatebyEmailApi() {

        FBCustomerItem customer = new FBCustomerItem();
        customer.firstName = "Dj";
        customer.lastName = "Kumar";
        customer.emailID = "vkumarss_ic@fishbowl.com";
        customer.loginID = "";
        customer.customerAge = "";
        customer.customerGender = "Male";
        customer.cellPhone = "956-071-7227";
        customer.loyalityNo = "";
        customer.addressLine1 = "";
        customer.addressLine2 = "";
        customer.addressCity = "";
        customer.addressState = "";
        customer.addressZip = "";
        customer.favoriteDepartment = "";
        customer.customerTenantID = "581";
        customer.statusCode = 1;
        customer.deviceId = "3ae347ca6e2a2d6c";
        int storecode = 145; //Default store - Emeryville
        customer.homeStore = Integer.toString(storecode);
        customer.pushOpted = "N";
        customer.smsOpted = "N";
        customer.emailOpted = "N";
        customer.phoneOpted = "N";
        customer.adOpted = "N";
        customer.loyalityRewards = "150";
        return customer;
    }

    private FBGiftCardCreateRequestItem GiftCardCreateObjectRequest() {

        FBGiftCardCreateRequestItem customer = new FBGiftCardCreateRequestItem();
        customer.BrandId = "711763";
        customer.CardId = 70621227;
        customer.CardName = "JambaJuice";
        customer.CardNumber = "89";
        customer.Balance = 15000000;
        customer.InitialBalance = 5000000;
        customer.CardPin = "5441";
        customer.BrandName = "JambaJuice";
        customer.BrandCustomerNumber = "5441";
        customer.BrandWebsiteUrl = "https://www.jambajuice.com/more-jamba/jambacard";
        customer.BarcodeValue = "46079SJ6416";
        customer.CardBackgroundColor = "#E0EDF7";
        customer.LastModifiedDate = "2017-09-22T14:44:36";
        customer.BarcodeType = "code128";
        customer.BrandLogoURL = "https:\\/\\/api.giftango.com\\/imageservice\\/Images\\/150x95\\/CIR_000735_00.png";
        customer.BrandMainImageURL = "https:\\/\\/api.giftango.com\\/imageservice\\/BarcodeImages\\/7YOoGyYbVJivd3qwlvU1Sg.png?BCTID=1&ShowBarcodeReadableText=0";
        customer.MemberName = "Sridhar Rajendran Deva";
        customer.GiftCardHeaderDetail = "gifts.Enjoy your gift for being a loyal customer.";
        return customer;
    }

    private JSONObject collectGiftCardCreateRequest() {
        final JSONObject object = new JSONObject();
        try {

//            object.put("Balance", 15000000);
//            object.put("BrandId", "711763");
//            object.put("BrandName", "JambaJuice");
//            object.put("BrandCustomerNumber", "5441");
//            object.put("BrandWebsiteUrl", "https://www.jambajuice.com/more-jamba/jambacard");
//            object.put("BarcodeValue", "46079SJ6416");
//            object.put("BarcodeType", "code128");
//            object.put("BrandLogoURL", "https:\\/\\/api.giftango.com\\/imageservice\\/Images\\/150x95\\/CIR_000735_00.png");
//            object.put("BrandMainImageURL", "https:\\/\\/api.giftango.com\\/imageservice\\/Images\\/300x190\\/CIR_000735_00.png");
//            object.put("CardName", "JambaJuice");
//            object.put("CardNumber", "46079SJ6416");
//            object.put("CardId", 70621227);
//            object.put("CardPin", "5441");
//            object.put("CardBackgroundColor", "#E0EDF7");
//            object.put("GiftCardHeaderDetail", "gifts.Enjoy your gift for being a loyal customer.");
//            object.put("InitialBalance", 5000000);
//            object.put("LastModifiedDate", "2017-09-22T14:44:36");
//            object.put("MessageTo", "gifts.Enjoy your gift for being a loyal customer.");
//            object.put("MemberName", "Sridhar Rajendran Deva");


            object.put("Balance", 19000000);//should be in Long
            object.put("BrandId", "711763");
            object.put("BrandName", "JambaJuice");
            object.put("BarcodeValue", "46079SJ6416");
            object.put("CardId", 97);
            object.put("CardNumber", "46079SJ6416");
            object.put("CardName", "JambaJuice");
            object.put("CardPin", "5441");
            object.put("CardBackgroundColor", "#E0EDF7");
            object.put("MessageTo", "Sridhar");
            object.put("InitialBalance", 15000000);
            object.put("ImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");
            object.put("LastModifiedDate", "2017-11-11T14:44:36"); //format should be like yyyy-MM-dd'T'HH:mm:ss
            object.put("ThumbnailImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");

//
//            object.put("Balance", 15000000); //
//            object.put("LastModifiedDate", "2017-09-22T14:44:36"); //format should be like yyyy-MM-dd'T'HH:mm:ss
//            object.put("CardNumber", "46079SJ6416");
//            object.put("CardPin", "5441");
//            object.put("ThumbnailImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");
//            object.put("BrandName", "JambaJuice");
//            object.put("BarcodeValue", "46079SJ6416");
//            object.put("CardId", 70621227);
//            object.put("MessageTo", "Sridhar");
//            object.put("ImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");
//


            return object;
        } catch (JSONException e) {
            e.printStackTrace();


        }
        return object;
    }


    public void getGiftJWT(final JSONObject expectedjson) {
        FBGiftService.sharedInstance().getGiftAndroidSavePayJWT(expectedjson, new FBGiftService.FBGetSaveGiftJWTokenCallback() {
            @Override
            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "getGiftJWT Success Message" + response);
                    updateGiftAndroidSavePay(expectedjson);
                } else {

                    Constants.alertDialogShow(BasicMainActivity.this, "getGiftJWT Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }

    public void updateGiftAndroidSavePay(final JSONObject expectedjson) {
        FBGiftService.sharedInstance().updateGiftAndroidSavePay(expectedjson, new FBGiftService.FBGetSaveGiftJWTokenCallback() {
            @Override
            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "updateGiftAndroidSavePay Success Message" + response);
                } else {

                    Constants.alertDialogShow(BasicMainActivity.this, "updateGiftAndroidSavePay Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }

//    public void saveToAndroidPay(View view) {
//
//        FBGiftService.sharedInstance().getGiftAndroidPayJWT(collectGiftCardCreateRequest(), new FBGiftService.FBGetSaveGiftJWTokenCallback() {
//            @Override
//            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {
//
//                if (error == null && response != null) {
//                    try {
//                        boolean successFlag = response.getBoolean("successFlag");
//                        String message = response.getString("message");
//                        if (successFlag) {
//                            String jwtToken = response.getString("jwtToken");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/payapp/savetoandroidpay/" + jwtToken));
//                            startActivity(intent);
//                        } else {
//                            if (StringUtilities.isValidString(message)) {
//                               // Utils.alert(context, "Error", message);
//                            } else {
//                               // Utils.alert(context, "Error", "Unable to save this card to android pay");
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //Toast.makeText(context, "Unable to save to android pay " + Utils.getErrorDescription(error), Toast.LENGTH_SHORT).show();
//                   // Utils.alert(context, "Error", Utils.getErrorDescription(error));
//                }
//            }
//        });
//    }

    public void getRefreshToken() {

        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getRefreshToken(object, new FBUserService.FBGetRefreshTokenCallback() {


            @Override
            public void onGetRefreshTokencallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    //   Constants.alertDialogShow(BasicMainActivity.this,"GetToken Success Message");
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(BasicMainActivity.this).setAccessToken(secratekey);
                    mobileSettings();

                } else {

                    String errormessage = FBUtility.getErrorDescription(error);
                    Constants.alertDialogShow(BasicMainActivity.this, errormessage);

                }

            }
        });
    }

    //
    public void getRefresahToken() {

        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getRefreshToken(object, new FBUserService.FBGetRefreshTokenCallback() {
            @Override
            public void onGetRefreshTokencallback(JSONObject response, Exception error) {
                if (error == null && response != null) {


                } else {

                }

            }
        });
    }


    private void pushNotificationRecieve() {
        // Broadcast Receiver for receiving token To get BroadCast
        BroadcastReceiver mMessageReceived = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Intent i = intent;
                Bundle extras = i.getExtras();
                {
                    if (extras != null) {

                        String apsStr = i.getExtras().getString("aps");
                        try {
                            if (apsStr != null) {
                                JSONObject apsJson = new JSONObject(apsStr);
                                if (apsJson.has("alert")) {
                                    String title = apsJson.getString("alert");
                                    String offerid = "";
                                    String custId = "";
                                    if (extras.getString("offerid") != null) {
                                        offerid = extras.getString("offerid");
                                    }


                                    if (extras.getString("custid") != null) {
                                        custId = extras.getString("custid");
                                    }

                                    long currCustId = FBPreferences.sharedInstance(context).getUserMemberId();
                                    long offerCustId = Long.parseLong(custId);
                                    if (currCustId == offerCustId) {
                                        System.out.println("equals");
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


    public void memberUpdate() {

        FBCustomerItem customer = collectCustomerData();

        final JSONObject object = new JSONObject();
        try {
            object.put("firstName", customer.getFirstName());
            object.put("lastName", customer.getLastName());
            object.put("email", customer.getEmailID());
            object.put("phone", customer.getCellPhone());
            object.put("pushOptIn", true);
            object.put("addressState", customer.getAddressState());
            object.put("addressStreet", customer.getAddressLine1());
            object.put("addressCity", customer.getAddressCity());
            object.put("addressZipCode", customer.getAddressZip());
            object.put("storeCode", customer.getHomeStore());
            object.put("birthDate", customer.getDateOfBirth());
            object.put("gender", customer.getCustomerGender());
            object.put("username", customer.getEmailID());
            object.put("password", customer.getLoginPassword());
            object.put("deviceId", customer.getDeviceID());
            object.put("sendWelcomeEmail", "ss");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().memberUpdate(object, new FBUserService.FBMemberUpdateCallback() {
            @Override
            public void onMemberUpdateCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "memberUpdate Success Message" + response);

                    GetMember();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "memberUpdate Error Message" + " --" + error);
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    GetMember();
                }
            }
        });
    }


    public void GetMember() {


        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "getMember Success Message" + response);
                    getAllStores();
                    // getSearchStore();
                    //  EventCheck();

                    //    getGiftJWT(collectGiftCardCreateRequest());
                }
                {
                    Constants.alertDialogShow(BasicMainActivity.this, "getMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }

        });
    }


    public void getAllStores() {
        final JSONObject object = new JSONObject();
        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
            @Override
            public void OnAllStoreCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "getAllStore Success Message");
                    StoreDetails();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "getAllStore Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    public void StoreDetails() {

        FBStoreService.sharedInstance().getStoreDetails("168874", new FBStoreService.FBStoreDetailCallback() {
            @Override
            public void OnFBStoreDetailCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "StoreDetails Success Message" + response);
                    favouriteStoreUpdate();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "StoreDetails Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }

            }


        });
    }

    public void getSearchStore() {
        JSONObject object = new JSONObject();
        try {
            object.put("query", "168874");
            object.put("radius", "1000");
            object.put("count", "10");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        FBStoreService.sharedInstance().getSearchAllStore1(object, "", new FBStoreService.FBAllSearchStorejsonCallback() {
                    @Override
                    public void OnAllSearchStorejsonCallback(JSONObject response, Exception error) {

                        if (response != null) {
                        }
                    }
                }
        );
    }


    public void favouriteStoreUpdate() {

        JSONObject object = new JSONObject();
        try {
            object.put("memberid", FBPreferences.sharedInstance(BasicMainActivity.this).getUserMemberforAppId());
            object.put("storeCode", "168874");

        } catch (Exception e) {
            e.printStackTrace();
        }


        FBUserService.sharedInstance().favourteStoreUpdate(object, new FBUserService.FBFavouriteStoreUpdateCallback() {
            @Override
            public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "favourteStoreUpdate Success Message" + response);
                    getMenuCategory();

                } else {

                    Constants.alertDialogShow(BasicMainActivity.this, "favourteStoreUpdate Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    getMenuCategory();
                }

            }
        });
    }


    public void getMenuCategory() {
        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuCategory(object, String.valueOf(168589),
                new FBUserService.FBMenuCategoryCallback() {

                    public void onMenuCategoryCallback(JSONObject response, Exception error) {
                        try {
                            if (error == null && response != null) {

                                Constants.alertDialogShow(BasicMainActivity.this, "getMenuCategory Success Message" + response);
                                getMenuSubCategory();
                            } else {
                                Constants.alertDialogShow(BasicMainActivity.this, "getMenuCategory Error Message");
                                FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                            }
                        } catch (Exception e) {

                        }
                    }

                });
    }


    public void getMenuSubCategory() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuSubCategory(object, String.valueOf(168589), String.valueOf(22), new FBUserService.FBMenuSubCategoryCallback() {

            public void onMenuSubCategoryCallback(JSONObject response, Exception error) {
                try {
                    if (error == null && response != null) {

                        Constants.alertDialogShow(BasicMainActivity.this, "getMenuSubCategory Success Message" + response);
                        getMenuDrawerListProduct();
                    } else {
                        Constants.alertDialogShow(BasicMainActivity.this, "getMenuSubCategory Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    }
                } catch (Exception e) {

                }
            }

        });
    }


    public void getMenuDrawerListProduct() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getDrawerProductList(object, String.valueOf(168589), String.valueOf(22), String.valueOf(223), new FBUserService.FBMenuDrawerCallback() {

            public void onMenuDrawerCallback(JSONObject response, Exception error) {
                try {
                    if (error == null && response != null) {
                        Constants.alertDialogShow(BasicMainActivity.this, "getDrawerProductList Success Message" + response);
                        getMenuProductDetail();
                    } else {
                        Constants.alertDialogShow(BasicMainActivity.this, "getDrawerProductList Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    }
                } catch (Exception e) {

                }
            }

        });
    }


    public void getMenuProductDetail() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuProduct(object, String.valueOf(168589), String.valueOf(22), String.valueOf(223), String.valueOf(1017), new FBUserService.FBMenuProductCallback() {

            public void onMenuProductCallback(JSONObject response, Exception error) {
                try {
                    if (error == null && response != null) {

                        Constants.alertDialogShow(BasicMainActivity.this, "getMenuProduct Success Message" + response);
                        getUserOffers();
                    } else {
                        Constants.alertDialogShow(BasicMainActivity.this, "getMenuProduct Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    }
                } catch (Exception e) {

                }
            }

        });
    }


    public void getUserOffers() {
        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBOffer(data, " ", new FBUserOfferService.FBOfferCallback() {
            @Override
            public void OnFBOfferCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBOffer Success Message" + response);
                    getUserReward();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }

    public void getUserReward() {
        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBReward(data, " ", new FBUserOfferService.FBRewardCallback() {
            @Override
            public void OnFBRewardCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBReward Success Message" + response);
                    getUserFBPointBankOffer();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBReward Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    public void getUserFBPointBankOffer() {
        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBPointBankOffer(data, " ", new FBUserOfferService.FBPointBankOffer() {
            public void OnFBPointBankOfferCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBPointBankOffer Success Message" + response);
                    useOffer();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "getUserFBPointBankOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    public void useOffer() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberId", "81");
            object.put("offerId", "328829");
            object.put("tenantId", "581");
            object.put("claimPoints", "13");


        } catch (Exception e) {

        }
        FBUserOfferService.sharedInstance().useOffer(object, new FBUserOfferService.FBUseOfferCallback() {
            @Override
            public void onUseOfferCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(BasicMainActivity.this, "useOffer Success Message" + response);
                    getBonusRuleList();
                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "useOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                    getBonusRuleList();
                }
            }
        });
    }

    public void getBonusRuleList() {

        final JSONArray object = new JSONArray();
        FBUserService.sharedInstance().getBonusRuleList(object, new FBUserService.FBBonusRuleListCallback() {

            public void onBonusRuleListCallback(JSONArray response, Exception error) {
                try {
                    if (response != null && error == null) {
                        Constants.alertDialogShow(BasicMainActivity.this, "getBonusRuleList Success Message" + response);

                        getUserLogout();
                    } else {
                        Constants.alertDialogShow(BasicMainActivity.this, "getBonusRuleList Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);

                    }
                } catch (Exception e) {

                }
            }
        });
    }

    public void getUserLogout() throws JSONException {
        final JSONObject object = new JSONObject();
        object.put("Application", "mobilesdk");
        FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
            @Override
            public void onLogoutCallback(JSONObject response, Exception error) {

                if (response != null && error == null) {
                    Constants.alertDialogShow(BasicMainActivity.this, "Congratution logout Success Message" + response);

                } else {
                    Constants.alertDialogShow(BasicMainActivity.this, "logout Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);

                }

            }
        });
    }

    public void getOfferById() {

        JSONObject object = new JSONObject();

        FBOfferService.sharedInstance().getFBOfferByOfferId(object, offerid, new FBOfferService.FBOfferByIdCallback() {
            @Override
            public void onFBOfferByIdCallback(JSONObject response, Exception error) {
                if (response != null) {

                } else {
                }


            }
        });
    }

    public void getAllRewardsOffer() {

        JSONObject object = new JSONObject();

        FBUserService.sharedInstance().getAllRewardOfferApi(object, new FBUserService.FBAllRewardOfferCallback() {
            @Override
            public void onAllRewardOfferCallback(JSONObject response, Exception error) {
                if (response != null) {

                } else {
                }

            }
        });
    }


    public void EventCheck() {

        track_ItemWith("12345", "Lunch", FBEventSettings.MENU_CLICK);


    }

    public void EventCheckGuest() {
        FBPreferences.sharedInstance(this).delete("user_id_forapp");


        // track_ItemWith("12345", "Lunch", FBEventSettings.FIRST_TIME_LAUNCH);

        for (int i = 0; i < 5; i++) {
            track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);
        }

    }


    public boolean checkValidCall() {
        if (_app.fbsdkObj == null)
            return false;

        if (_app.fbsdkObj.getFBSdkData() == null)
            return false;


        if (_app.fbsdkObj.getmCurrentLocation() == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            _app.fbsdkObj.setmCurrentLocation(curLoc);
        }
        return true;
    }


    public void track_ItemWith(String id, String description, String eventName) {
        if (!checkValidCall())
            return;

        try {
            JSONObject data = new JSONObject();
            if (eventName != null && id != null && description != null) {
                data.put("item_id", id);
                data.put("item_name", description);
                data.put("event_name", eventName);
                addCommonData(data);
                if (_app.fbsdkObj.checkAppEventFlag())
                    FBAnalytic.sharedInstance().recordEvent(data);
            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }


    public void track_EvenforGuesttbyName(String eventName) {
        if (!checkValidCallGuest())
            return;
        try {
            JSONObject data = new JSONObject();
            data.put("event_name", eventName);
            addCommonDataGuest(data);
            if (_app.fbsdkObj.checkAppEventFlag())
                FBAnalytic.sharedInstance().recordEvent(data);

        } catch (Exception e) {
            recordErrorEvent();
        }
    }

    public boolean checkValidCallGuest() {
        if (_app == null)
            return false;


        if (_app.fbsdkObj.getmCurrentLocation() == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            _app.fbsdkObj.setmCurrentLocation(curLoc);
        }
        return true;
    }


    public void addCommonDataGuest(JSONObject data) {

        try {
            data.put("action", "AppEvent");
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("lat", _app.fbsdkObj.getmCurrentLocation().getLatitude());
            data.put("lon", _app.fbsdkObj.getmCurrentLocation().getLongitude());
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("device_os_ver", _app.fbsdkObj.getAndroidOs());

        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }


    public void addCommonData(JSONObject data) {

        try {
            data.put("action", "AppEvent");
            data.put("event_id", generateUniqueEventId());
            data.put("memberid", FBPreferences.sharedInstance(context).getUserMemberforAppId());
            data.put("lat", _app.fbsdkObj.getmCurrentLocation().getLatitude());
            data.put("lon", _app.fbsdkObj.getmCurrentLocation().getLongitude());
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("device_os_ver", _app.fbsdkObj.getAndroidOs());

        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }


    private String generateUniqueEventId() {
        return UUID.randomUUID().toString();
    }


    public void recordErrorEvent() {
        if (_app.fbsdkObj.checkAppEventFlag()) {
            JSONObject data = new JSONObject();

            try {
                data.put("event_name", "AppError");
                addCommonData(data);
                FBAnalytic.sharedInstance().recordEvent(data);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }


    public void changePassword() {
        final JSONObject object = new JSONObject();
        try {
            object.put("oldPassword", "password");
            object.put("password", "password");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        FBUserService.sharedInstance().changePassword(object, new FBUserService.FBChangePasswordCallback() {
            @Override
            public void onChangePasswordCallback(JSONObject response, Exception error) {

            }
        });
    }


    public void forgetPassword() {
        final JSONObject object = new JSONObject();
        try {
            object.put("email", "vkumar_ic@fishbowl.com");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        FBUserService.sharedInstance().forgetPassword(object, new FBUserService.FBforgetPasswordCallback() {
            @Override
            public void onFBforgetPasswordCallback(JSONObject response, Exception error) {

            }
        });
    }


    @Override
    public void onFBRegistrationSuccess(String FBCustomerItem) {
        deviceUpdate();

    }

    @Override
    public void onFBRegistrationError(String error) {

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText textBox = (EditText) findViewById(R.id.s2wResponse);
        switch (requestCode) {
            case SAVE_TO_ANDROID:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        textBox.setText("SUCCESS_RESPONSE_TEXT");
                        break;
                    case Activity.RESULT_CANCELED:
                        textBox.setText("CANCELED_RESPONSE_TEXT");
                        break;
                    default:
                        int errorCode =
                                data.getIntExtra(
                                        WalletConstants.EXTRA_ERROR_CODE, -1);
                        textBox.setText("ERROR_PREFIX_TEXT" + errorCode);
                        break;
                }
        }
    }

    private void UpdatePay() {

        BroadcastReceiver updatepayresponse = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Intent i = intent;
                Bundle extras = i.getExtras();
                {
                    if (extras != null) {
                        String message = i.getExtras().getString("message");
                        Boolean successflag = i.getExtras().getBoolean("successFlag");
                        Constants.alertDialogShow(BasicMainActivity.this, successflag + "-------" + message);

                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(
                updatepayresponse,
                new IntentFilter(Config.UPDATESAVEPAY));
    }

    private void SavePay() {

        BroadcastReceiver savepayresponse = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Intent i = intent;
                Bundle extras = i.getExtras();
                {
                    if (extras != null) {
                        String message = i.getExtras().getString("message");
                        Boolean successflag = i.getExtras().getBoolean("successFlag");
                        Constants.alertDialogShow(BasicMainActivity.this, successflag + "-------" + message);

                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(
                savepayresponse,
                new IntentFilter(Config.SAVEPAY));
    }
}