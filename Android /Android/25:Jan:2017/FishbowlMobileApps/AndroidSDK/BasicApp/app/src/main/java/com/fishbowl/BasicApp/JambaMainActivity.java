//package com.fishbowl.BasicApp;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.Preferences.FBPreferences;
//import com.fishbowl.basicmodule.Analytics.FBAnalytic;
//import com.fishbowl.basicmodule.Analytics.FBEventSettings;
//import com.fishbowl.basicmodule.Controllers.FBSdk;
//import com.fishbowl.basicmodule.Controllers.FBSdkData;
//import com.fishbowl.basicmodule.Models.FBCustomerItem;
//import com.fishbowl.basicmodule.Models.FBOfferItem;
//import com.fishbowl.basicmodule.Models.Member;
//import com.fishbowl.basicmodule.Services.FBMobileSettingService;
//import com.fishbowl.basicmodule.Services.FBUserService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
//import com.fishbowl.basicmodule.Utils.FBConstant;
//import com.fishbowl.basicmodule.Utils.FBUtility;
//import com.fishbowl.basicmodule.Utils.FBUtils;
//import com.fishbowl.basicmodule.Utils.StringUtilities;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import static com.fishbowl.BasicApp.AuthApplication.getAppContext;
//import static com.fishbowl.BasicApp.AuthApplication.instance;
//
//
///**
// * Created by digvijay(dj)
// */
//
//public class JambaMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
//    Button bbl;
//
//    Bundle extras;
//    private String mPassDataUrl = "";
//    private String passPreviewImageURL = "";
//    private   String e = "";
//    private static final String DEFAULT_STORE_CODE="0145";//Emeryville - 0145 : Merced - 0620
//    private static final String DEFAULT_STORE_SEARCH_KEYWORD="Emeryville";
//    String offerid=null;
//    String clpnid=null;
//    String ntype="";
//    private static final int PERMISSION_REQUEST_CODE = 1;
//    int diifer;
//
//    String offerDescription;
//    String offerTitle;
//    Boolean ispmOffer;
//    Integer   pmPromotionID ;
//    Integer offerID;
//    Integer channelID;
//    private ArrayList<FBOfferItem> offerList;
//    private int offerCount;
//    String validityEndDateTime;
//    public AuthApplication _app;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_basic_main);
//        this._app = AuthApplication.getAppContext();
//
//
//        if (_app.fbsdkObj != null) {
//            FBAnalytic.sharedInstance().init(this._app, _app.fbsdkObj.SERVER_URL, "91225258ddb5c8503dce33719c5deda7");
//        } else {
//            //  FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.SERVER_URL), "91225258ddb5c8503dce33719c5deda7");
//
//        }
//        Intent i = getIntent();
//        extras = i.getExtras();
//        {
//            if (extras != null) {
//                this.mPassDataUrl = i.getStringExtra("url");
//                ntype = i.getStringExtra("ntype");
//                String apsStr = i.getExtras().getString("aps");
//                passPreviewImageURL = i.getExtras().getString("previewimage");
//                try {
//                    if(apsStr!=null) {
//                        //  enableScreen(false);
//                        JSONObject apsJson = new JSONObject(apsStr);
//                        if (apsJson.has("alert")) {
//                            e = apsJson.getString("alert");
//                            if (extras.getString("offerid") != null && (extras.getString("clpnid") != null)) {
//                                offerid = extras.getString("offerid");
//                                clpnid = extras.getString("clpnid");
//
//                            }
//                            String apsStr1 =  i.getExtras().getString("offerid");
//                            String clpnid2 = i.getExtras().getString("clpnid");
//                            //  JambaAnalyticsManager.sharedInstance().track_OfferItemWith(apsStr1,clpnid2,apsStr1,clpnid2, FBEventSettings.PUSH_OPEN);
//                            // It has been implemented in fbSdk processPushMessage SO I have Remove It
//
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (extras != null && (StringUtilities.isValidString(e))) {
//
//
//        }else {
//           getToken();
//
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    public void loginMember() {
//        final JSONObject object = new JSONObject();
//
//
//
//        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback(){
//            public void onGetTokencallback(JSONObject response, Exception error){
//                //  getMobileSettings();
//                //  getToken();
//
//            }
//        });
//    }
//
//
//    public void registerMember() {
//        final JSONObject object = new JSONObject();
//        FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
//            @Override
//            public void onCreateMemberCallback(JSONObject response, Exception error) {
//
//            }
//        });
//    }
//
//
//    public void getToken(){
//        JSONObject object=new JSONObject();
//        try
//        {
//            object.put("clientId",FBConstant.client_id);
//            object.put("clientSecret",FBConstant.client_secret);
//            object.put("deviceId",FBUtility.getAndroidDeviceID(this));
//            object.put("tenantId",FBConstant.client_tenantid);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback()
//        {
//
//
//            @Override
//            public void onGetTokencallback(JSONObject response, Exception error) {
//                if(error==null&&response!=null){
//
//                    Constants.alertDialogShow(JambaMainActivity.this,"GetToken Success Message");
//
//                    mobileSettings();
//
//
//                }else {
//                    Constants.alertDialogShow(JambaMainActivity.this,"GetToken Error Message");
//
//                }
//
//            }
//        });
//    }
//
//    public void mobileSettings() {
//        String cusId = "0";
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if(state) {
//                    getViewMobileSettings();
//                    Log.d("Mobile Settings  Api", "Success");
//                    Constants.alertDialogShow(JambaMainActivity.this, "MobileSettingsService Success Message" + state);
//                }
//                else{
//                    Constants.alertDialogShow(JambaMainActivity.this, "MobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//
//    public void getViewMobileSettings() {
//
//        final JSONObject object = new JSONObject();
//
//        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewSettingsCallback() {
//            @Override
//            public void onViewSettingsCallback(JSONObject response, final Exception error) {
//
//                if(error==null&&response!=null){
//
//                    Constants.alertDialogShow(JambaMainActivity.this,"FBViewMobileSettingsService Success Message"+response);
//                    loginFb();
//
//
//                }else {
//
//                    Constants.alertDialogShow(JambaMainActivity.this,"FBViewMobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                    loginFb();
//                }
//            }
//        });
//    }
//
//    private FBCustomerItem collectCustomerData() {
//
//        FBCustomerItem customer = new FBCustomerItem();
//        customer.firstName = "Dj";
//        customer.lastName ="Kumar";
//        customer.emailID = "vkumar_ic@fishbowl.com";
//        customer.loginID = "";
//        customer.customerAge = "";
//        customer.customerGender = "Male";
//        customer.cellPhone = "956-071-7227";
//        customer.loyalityNo = "";
//        customer.addressLine1 = "";
//        customer.addressLine2 = "";
//        customer.addressCity = "";
//        customer.addressState = "";
//        customer.addressZip = "";
//        customer.favoriteDepartment = "";
//        customer.customerTenantID = "581";
//        customer.statusCode = 1;
//        customer.deviceId = "3ae347ca6e2a2d6c";
//        int storecode = 145; //Default store - Emeryville
//        customer.homeStore = Integer.toString(storecode);
//        customer.pushOpted = "N";
//        customer.smsOpted =  "N";
//        customer.emailOpted = "N";
//        customer.phoneOpted = "N";
//        customer.adOpted = "N";
//        customer.loyalityRewards = "150";
//        return customer;
//    }
//
//    public void loginFb() {
//
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("username", "kumaresan@vthink.co.in");
//            object.put("deviceId", FBUtility.getAndroidDeviceID(getAppContext()));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        FBUserService.sharedInstance().loginMemberforjambatest(object,  new FBUserService.FBLoginMemberCallback() {
//            public void onLoginMemberCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    try {
//                        boolean successFlag = response.getBoolean("successFlag");
//                        if (successFlag) {
//                            String accessTokenForApp = response.getString("accessToken");
//                            FBPreferences.sharedInstance(instance).setAccessTokenforapp(accessTokenForApp);
//                           getMember();
//                            getUserLogout();
//
//                            Constants.alertDialogShow(JambaMainActivity.this,"loginMemberforjambatest Success Message"+response);
//                        } else {
//                            Constants.alertDialogShow(JambaMainActivity.this,"loginMemberforjambatest Error Message"+" --"+error);
//                            FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    Constants.alertDialogShow(JambaMainActivity.this,"loginMemberforjambatest Error Message"+" --"+error);
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                }
//
//            }
//        });
//    }
//
//        public void getMember() {
//            FBSdk fbSdk = FBSdk.sharedInstance(getAppContext());
//            final FBSdkData FBSdkData = fbSdk.getFBSdkData();
//            final JSONObject object = new JSONObject();
//            final Member member = FBUserService.sharedInstance().member;
//            FBUserService.sharedInstance().getMemberforjamba(object, FBPreferences.sharedInstance(this).getExternalCustomerIdforapp(), new FBUserService.FBGetMemberCallback() {
//                public void onGetMemberCallback(JSONObject response, Exception error) {
//                    if (response != null) {
//                        try {
//                            boolean successFlag = response.has("successFlag") ? response.getBoolean("successFlag") : false;
//                            if (successFlag) {
//                                member.initWithJson(response, getAppContext());
//                                String pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
//                                String deviceId = response.has("deviceId") ? response.getString("deviceId") : "";
//
//                                if (com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(pushToken) && com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(deviceId)) {
//                                    FBPreferences.sharedInstance(getAppContext()).setDeviceId(deviceId);
//                                    FBPreferences.sharedInstance(getAppContext()).setPushToken(pushToken);
//                                }
//                                Long memberId = response.has("customerID") ? response.getLong("customerID") : 0;
//
//                                FBSdkData.currCustomer = new FBCustomerItem();
//                                FBSdkData.currCustomer.tenantid = response.has("customerTenantID") ? Long.parseLong(response.getString("customerTenantID")) : 0;
//                                FBSdkData.currCustomer.memberid = response.has("customerID") ? Long.parseLong(response.getString("customerID")) : 0;
//                                FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
//                                FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
//                                FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
//                                FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
//                                FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
//                                FBSdkData.currCustomer.pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
//                                FBSdkData.currCustomer.pushOpted = response.has("pushOpted") ? response.getString("pushOpted") : "";
//
//                                FBSdkData.setCustomer(FBSdkData.currCustomer);
//                                Constants.alertDialogShow(JambaMainActivity.this,"getMember Success Message"+response);
//                                deviceUpdate();
//                            } else {
//                                Constants.alertDialogShow(JambaMainActivity.this,"getMember Error Message"+" --"+error);
//                                FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
//                              //  FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Constants.alertDialogShow(JambaMainActivity.this,"getMember Error Message"+" --"+error);
//                        FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                      //  FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
//
//                    }
//
//                }
//            });
//        }
//
//
//
//        public void deviceUpdate() {
//
//
//            JSONObject object = new JSONObject();
//
//            try {
//                object.put("memberid", FBPreferences.sharedInstance(this).getUserMemberforAppId());
//                object.put("deviceId", FBUtility.getAndroidDeviceID(this));
//                object.put("deviceOSVersion", FBConstant.device_os_ver);
//                object.put("deviceType", FBConstant.DEVICE_TYPE);
//                object.put("appId", BuildConfig.APPLICATION_ID);
//                object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            FBUserService.sharedInstance().deviceUpdateffojamba(object, new FBUserService.FBDeviceUpdateCallback() {
//                @Override
//                public void onDeviceUpdateCallback(JSONObject response, Exception error) {
//
//                    if (response != null) {
//                        try {
//                            JSONObject data = new JSONObject();
//                            Constants.alertDialogShow(JambaMainActivity.this,"deviceUpdateffojamba Success Message"+response);
//                          //  memberUpdate();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    } else {
//                        Constants.alertDialogShow(JambaMainActivity.this,"deviceUpdateffojamba Error Message"+" --"+error);
//                        FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                    }
//                }
//            });
//        }
//
//
//    public void memberUpdate() {
//
//        FBCustomerItem customer = collectCustomerData();
//
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("firstName", customer.getFirstName());
//            object.put("lastName", customer.getLastName());
//            object.put("email", customer.getEmailID());
//            object.put("phone", customer.getCellPhone());
//            object.put("pushOptIn", true);
//            object.put("addressState", customer.getAddressState());
//            object.put("addressStreet", customer.getAddressLine1());
//            object.put("addressCity", customer.getAddressCity());
//            object.put("addressZipCode", customer.getAddressZip());
//            object.put("storeCode", customer.getHomeStore());
//            object.put("birthDate", customer.getDateOfBirth());
//            object.put("gender", customer.getCustomerGender());
//            object.put("username", customer.getEmailID());
//            object.put("password", customer.getLoginPassword());
//            object.put("deviceId", customer.getDeviceID());
//            object.put("sendWelcomeEmail", "ss");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        FBUserService.sharedInstance().memberUpdate(object, new FBUserService.FBMemberUpdateCallback() {
//            @Override
//            public void onMemberUpdateCallback(JSONObject response, Exception error) {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(JambaMainActivity.this,"memberUpdate Success Message"+response);
//
//                    GetMember();
//                }
//                else {
//                    Constants.alertDialogShow(JambaMainActivity.this,"memberUpdate Error Message"+" --"+error);
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                }
//            }
//        });
//    }
//
//
//
//
//
//    public void GetMember() {
//        FBSdk fbSdk = FBSdk.sharedInstance(getAppContext());
//        final FBSdkData FBSdkData = fbSdk.getFBSdkData();
//        final JSONObject object = new JSONObject();
//        final Member member = FBUserService.sharedInstance().member;
//        FBUserService.sharedInstance().getMemberforjamba(object, FBPreferences.sharedInstance(this).getExternalCustomerIdforapp(), new FBUserService.FBGetMemberCallback() {
//            public void onGetMemberCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    try {
//                        boolean successFlag = response.has("successFlag") ? response.getBoolean("successFlag") : false;
//                        if (successFlag) {
//
//                            member.initWithJson(response, getAppContext());
//
//                            String pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
//                            String deviceId = response.has("deviceId") ? response.getString("deviceId") : "";
//
//                            if (com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(pushToken) && com.fishbowl.basicmodule.Utils.StringUtilities.isValidString(deviceId)) {
//                                FBPreferences.sharedInstance(getAppContext()).setDeviceId(deviceId);
//                                FBPreferences.sharedInstance(getAppContext()).setPushToken(pushToken);
//                            }
//                            Long memberId = response.has("customerID") ? response.getLong("customerID") : 0;
//
//                            FBSdkData.currCustomer = new FBCustomerItem();
//                            FBSdkData.currCustomer.tenantid = response.has("customerTenantID") ? Long.parseLong(response.getString("customerTenantID")) : 0;
//                            FBSdkData.currCustomer.memberid = response.has("customerID") ? Long.parseLong(response.getString("customerID")) : 0;
//                            FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
//                            FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
//                            FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
//                            FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
//                            FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
//                            FBSdkData.currCustomer.pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
//                            FBSdkData.currCustomer.pushOpted = response.has("pushOpted") ? response.getString("pushOpted") : "";
//
//                            FBSdkData.setCustomer(FBSdkData.currCustomer);
//                            Constants.alertDialogShow(JambaMainActivity.this,"GetMember Success Message"+response);
//                            favouriteStoreUpdate();
//                        } else {
//                          //  FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
//                            Constants.alertDialogShow(JambaMainActivity.this,"GetMember Error Message"+" --"+error);
//                            FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                   // FBPreferences.sharedInstance(getApplicationContext()).setUserMemberId(0);
//                    Constants.alertDialogShow(JambaMainActivity.this,"GetMember Error Message"+" --"+error);
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//
//                }
//
//            }
//        });
//    }
////    public void getAllStores() {
////        final JSONObject object = new JSONObject();
////        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
////            @Override
////            public void OnAllStoreCallback(JSONObject response, Exception error) {
////
////                if(error==null&&response!=null){
////                    Constants.alertDialogShow(JambaMainActivity.this,"getAllStore Success Message");
////                    StoreDetails();
////                }
////                else {
////                    Constants.alertDialogShow(JambaMainActivity.this,"getAllStore Error Message");
////                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
////                }
////            }
////        });
////    }
//
//
//
//    public void favouriteStoreUpdate(){
//
//        JSONObject object=new JSONObject();
//        try
//        {
//            object.put("memberid",FBPreferences.sharedInstance(JambaMainActivity.this).getUserMemberforAppId());
//            object.put("storeCode","168874");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        FBUserService.sharedInstance().favourteStoreUpdate(object, new FBUserService.FBFavouriteStoreUpdateCallback()
//        {
//            @Override
//            public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error) {
//                if(error==null&&response!=null){
//
//                    Constants.alertDialogShow(JambaMainActivity.this,"favourteStoreUpdate Success Message"+response);
//                    getUserLogout();
//
//                }else {
//
//                    Constants.alertDialogShow(JambaMainActivity.this,"favourteStoreUpdate Error Message");
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                }
//
//            }
//        });
//    }
//
//
//
//    public void getUserLogout() {
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("mobile", "Application");
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
//            @Override
//            public void onLogoutCallback(JSONObject response, Exception error) {
//
//                if (response != null && error==null)
//                {
//                    Constants.alertDialogShow(JambaMainActivity.this, "Congratution logout Success Message");
//                }
//                else
//                {
//                    Constants.alertDialogShow(JambaMainActivity.this,"logout Error Message");
//                    FBUtils.tryHandleTokenExpiry(JambaMainActivity.this,error);
//                }
//
//            }
//        });
//    }
//
//
//
//    public void EventCheck()
//    {
//        for(int i=0;i<=5;i++)
//        {
//            track_ItemWith("12345", "Lunch", FBEventSettings.CATEGORY_CLICK);
//        }
//    }
//
//
//
//    public boolean checkValidCall() {
//        if (_app.fbsdkObj == null)
//            return false;
//
//        if (_app.fbsdkObj.getFBSdkData() == null)
//            return false;
//
//
//        if (_app.fbsdkObj.getmCurrentLocation() == null) {
//            Location curLoc = new Location("");
//            curLoc.setLatitude(0);
//            curLoc.setLongitude(0);
//            _app.fbsdkObj.setmCurrentLocation(curLoc);
//        }
//        return true;
//    }
//
//
//
//    public void track_ItemWith(String id, String description, String eventName) {
//        if (!checkValidCall())
//            return;
//
//        try {
//            JSONObject data = new JSONObject();
//            if (eventName != null && id != null && description != null) {
//                data.put("item_id", id);
//                data.put("item_name", description);
//                data.put("event_name", eventName);
//                addCommonData(data);
//                if (_app.fbsdkObj.checkAppEventFlag())
//                    FBAnalytic.sharedInstance().recordEvent(data);
//            }
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }
//
//
//
//    public void addCommonData(JSONObject data) {
//
//        try {
//            data.put("action", "AppEvent");
//            data.put("event_id", generateUniqueEventId());
//            data.put("memberid", FBPreferences.sharedInstance(_app.fbsdkObj.context).getUserMemberId());
//            data.put("lat", _app.fbsdkObj.getmCurrentLocation().getLatitude());
//            data.put("lon", _app.fbsdkObj.getmCurrentLocation().getLongitude());
//            data.put("device_type", FBConstant.DEVICE_TYPE);
//            data.put("tenantid", FBConstant.client_tenantid);
//            data.put("device_os_ver", _app.fbsdkObj.getAndroidOs());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            recordErrorEvent();
//        }
//    }
//
//
//
//    private String generateUniqueEventId() {
//        return UUID.randomUUID().toString();
//    }
//
//
//    public void recordErrorEvent() {
//        if (_app.fbsdkObj.checkAppEventFlag()) {
//            JSONObject data = new JSONObject();
//
//            try {
//                data.put("event_name", "AppError");
//                addCommonData(data);
//                FBAnalytic.sharedInstance().recordEvent(data);
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//
//
//
//
//
//
//
//    @Override
//    public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem) {
//
//    }
//
//    @Override
//    public void onFBRegistrationError(String error) {
//
//    }
//}
