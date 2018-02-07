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
//import com.fishbowl.basicmodule.Interfaces.FBBonusRuleListCallback;
//import com.fishbowl.basicmodule.Interfaces.FBMenuCategoryCallback;
//import com.fishbowl.basicmodule.Interfaces.FBMenuDrawerCallback;
//import com.fishbowl.basicmodule.Interfaces.FBMenuProductCallback;
//import com.fishbowl.basicmodule.Interfaces.FBMenuSubCategoryCallback;
//import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
//import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceCallback;
//import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceDetailCallback;
//import com.fishbowl.basicmodule.Interfaces.FBRewardCallback;
//import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
//import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
//import com.fishbowl.basicmodule.Models.FBBonusItem;
//import com.fishbowl.basicmodule.Models.FBCustomerItem;
//import com.fishbowl.basicmodule.Models.FBMember;
//import com.fishbowl.basicmodule.Models.FBMenuCategoryItem;
//import com.fishbowl.basicmodule.Models.FBMenuDrawerItem;
//import com.fishbowl.basicmodule.Models.FBMenuProductItem;
//import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;
//import com.fishbowl.basicmodule.Models.FBOfferItem;
//import com.fishbowl.basicmodule.Models.FBOfferListItem;
//import com.fishbowl.basicmodule.Models.FBRestaurantListDetailItem;
//import com.fishbowl.basicmodule.Models.FBRestaurantListItem;
//import com.fishbowl.basicmodule.Models.FBRewardListItem;
//import com.fishbowl.basicmodule.Models.FBSessionItem;
//import com.fishbowl.basicmodule.Services.FBMenuService;
//import com.fishbowl.basicmodule.Services.FBMobileSettingService;
//import com.fishbowl.basicmodule.Services.FBRestaurantService;
//import com.fishbowl.basicmodule.Services.FBRewardService;
//import com.fishbowl.basicmodule.Services.FBSessionService;
//import com.fishbowl.basicmodule.Services.FBUserService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
//import com.fishbowl.basicmodule.Utils.FBConstant;
//import com.fishbowl.basicmodule.Utils.FBUtils;
//import com.fishbowl.basicmodule.Utils.StringUtilities;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import static com.facebook.login.widget.ProfilePictureView.TAG;
//
//
///**
// * Created by digvijay(dj)
// */
//
//public class DuplicateNewBasicMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
//    private static final String DEFAULT_STORE_CODE = "0145";//Emeryville - 0145 : Merced - 0620
//    private static final String DEFAULT_STORE_SEARCH_KEYWORD = "Emeryville";
//    private static final int PERMISSION_REQUEST_CODE = 1;
//    public AuthApplication _app;
//    Button bbl;
//    Bundle extras;
//    String offerid = null;
//    String clpnid = null;
//    String ntype = "";
//    JSONObject json_object;
//    int diifer;
//    String offerDescription;
//    String offerTitle;
//    Boolean ispmOffer;
//    Integer pmPromotionID;
//    Integer offerID;
//    Integer channelID;
//    String validityEndDateTime;
//    private String mPassDataUrl = "";
//    private String passPreviewImageURL = "";
//    private String e = "";
//    private ArrayList<FBOfferItem> offerList;
//    private int offerCount;
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
//
//        Intent i = getIntent();
//        extras = i.getExtras();
//        {
//            if (extras != null) {
//
//                if (getIntent().getExtras() != null) {
//                    for (String key : getIntent().getExtras().keySet()) {
//                        String value = getIntent().getExtras().getString(key);
//                        Log.d(TAG, "Key: " + key + " Value: " + value);
//                    }
//                }
//
////                try {
////                    json_object = new JSONObject(i.getStringExtra("datapayloadjson"));
////
////                    Log.e(TAG, "Example Item: " + json_object.getString("KEY"));
////
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//                this.mPassDataUrl = i.getStringExtra("data");
//                ntype = i.getStringExtra("ntype");
//                String apsStr = i.getExtras().getString("aps");
//                passPreviewImageURL = i.getExtras().getString("previewimage");
//                try {
//                    if (apsStr != null) {
//                        //  enableScreen(false);
//                        JSONObject apsJson = new JSONObject(apsStr);
//                        if (apsJson.has("alert")) {
//                            e = apsJson.getString("alert");
//                            if (extras.getString("offerid") != null && (extras.getString("clpnid") != null)) {
//                                offerid = extras.getString("offerid");
//                                clpnid = extras.getString("clpnid");
//
//                            }
//                            String apsStr1 = i.getExtras().getString("offerid");
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
//        mobileSettings();
//        //   loginMember("959-595-9596", "password");
//        // EventCheck();
//    }
//
//
//    public void mobileSettings() {
//        String cusId = "0";
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback() {
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if (state) {
//                    Log.d("Mobile Settings  Api", "Success");
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "MobileSettingsService Success Message" + state);
//
//                    // EventCheckGuest();
//                    getViewMobileSettingsServiceSettings();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "MobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getViewMobileSettingsServiceSettings() {
//
//        final JSONObject object = new JSONObject();
//
//        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewSettingsCallback() {
//            @Override
//            public void onViewSettingsCallback(JSONObject response, final Exception error) {
//
//                if (error == null && response != null) {
//
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "MobileSettingsService Success Message" + response);
//
//                    loginMember("959-595-9596", "password");
//
//                } else {
//
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "MobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void loginMember(String name, String password) {
//
//        FBSessionService.loginMember(name, password, new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "loginMember Success Message" + spendGoSession);
//                    String secratekey = spendGoSession.getAccessToken();
//                    FBPreferences.sharedInstance(DuplicateNewBasicMainActivity.this).setAccessTokenforapp(secratekey);
//                    FBUserService.sharedInstance().access_token = spendGoSession.getAccessToken();
//                    FBPreferences.sharedInstance(DuplicateNewBasicMainActivity.this).setSignin(true);
//                    createUser(collectCustomerData());
//
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "loginMember Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    private FBMember collectCustomerData() {
//
//        FBMember customer = new FBMember();
//        customer.setFirstName("Dj");
//        customer.setLastName("Kumar");
//        customer.setEmailAddress("vkumar_ic@fishbowl.com");
//        customer.setAddressStreet("East Avenue");
//        customer.setAddressState("Noida");
//        customer.setAddressCity("Noida");
//        customer.setPhoneNumber("956-071-7227");
//        customer.setAddressZipCode("201301");
//        customer.setDate("08");
//        customer.setMonth("08");
//        customer.setYear("2017");
//        customer.setGender("M");
//        customer.setFavoriteStore("0");
//        customer.setEmailOptIn(true);
//        customer.setPushOptIn(true);
//        customer.setsMSOptIn(true);
//        customer.setProfileImageUrl("");
//        customer.setRequestFromJoinPage(true);
//        customer.setCreated("2017-08-08 08:22:00.0");
//        customer.setdOB("08/08/2017");
//        customer.setSendWelcomeEmail("ss");
//        customer.setDeviceId("12345");
//        customer.setStoreCode("0");
//        customer.setCountry("India");
//        customer.setPassword("password");
//
//        return customer;
//    }
//
//    public void createUser(FBMember user) {
//
//        FBSessionService.createMember(user, new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "createUser Success Message" + spendGoSession);
//                    UpdateMember(collectCustomerData());
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "createUser Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//
//                }
//            }
//        });
//    }
//
//    public void UpdateMember(FBMember user) {
//
//        FBSessionService.memberUpdate(user, new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "memberUpdate Success Message" + spendGoSession);
//                    GetMember();
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "memberUpdate Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//
//                }
//            }
//        });
//    }
//
//    public void GetMember() {
//
//        FBSessionService.getMember(new FBUserServiceCallback() {
//            @Override
//            public void onUserServiceCallback(FBMember user, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "GetMember Success Message" + user);
//                    UpdateDevice();
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "GetMember Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//
//                }
//            }
//        });
//    }
//
//    public void UpdateDevice() {
//
//        FBSessionService.deviceUpdate(new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback( FBSessionItem spendGoSession, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "UpdateDevice Success Message" + spendGoSession);
//                    getAllStores();
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "UpdateDevice Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//
//                }
//            }
//        });
//    }
//
//
//    public void getAllStores() {
//
//        FBRestaurantService.getAllRestaurants(new FBRestaurantServiceCallback() {
//            @Override
//            public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getAllStores Success Message" + response);
//                    getStoresDetail();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getAllStores Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getStoresDetail() {
//
//        FBRestaurantService.getRestaurantById("168874", new FBRestaurantServiceDetailCallback() {
//            @Override
//            public void onRestaurantServiceDetailCallback(FBRestaurantListDetailItem response, Exception error) {
//                if (error != null) {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getStoresDetail Success Message" + response);
//                    getStoresSearch();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getStoresDetail Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//    public void getStoresSearch() {
//
//        FBRestaurantService.getAllRestaurantsNear("168874", "1000", "10", new FBRestaurantServiceCallback() {
//            @Override
//            public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
//                if (error != null) {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getStoresSearch Success Message" + response);
//                    favouriteStoreUpdate();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getStoresSearch Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//    public void favouriteStoreUpdate() {
//
//        FBRestaurantService.favourteStoreUpdate("168874", new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback(FBSessionItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "favouriteStoreUpdate Success Message" + response);
//                    getMenuCategory();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "favouriteStoreUpdate Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getMenuCategory() {
//
//        FBMenuService.getMenuCategory(String.valueOf(168589), new FBMenuCategoryCallback() {
//            @Override
//            public void onMenuCategoryCallback(FBMenuCategoryItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuCategory Success Message" + response);
//                    getSubMenuCategory();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuCategory Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getSubMenuCategory() {
//
//        FBMenuService.getMenuSubCategory(String.valueOf(168589), String.valueOf(22), new FBMenuSubCategoryCallback() {
//            @Override
//            public void onMenuSubCategoryCallback(FBMenuSubCategoryItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getSubMenuCategory Success Message" + response);
//                    getMenuDrawerListProduct();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getSubMenuCategory Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//    public void getMenuDrawerListProduct() {
//
//        FBMenuService.getDrawerProductList(String.valueOf(168589), String.valueOf(22), String.valueOf(223), new FBMenuDrawerCallback() {
//            @Override
//            public void onMenuDrawerCallback(FBMenuDrawerItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuDrawerListProduct Success Message" + response);
//                    getMenuProductDetail();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuDrawerListProduct Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getMenuProductDetail() {
//
//        FBMenuService.getMenuProduct(String.valueOf(168589), String.valueOf(22), String.valueOf(223), String.valueOf(1017), new FBMenuProductCallback() {
//            @Override
//            public void onMenuProductCallback(FBMenuProductItem category, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuProductDetail Success Message" + category);
//                    getUserOffers();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getMenuProductDetail Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//
//
//
//    public void getUserOffers() {
//
//        FBRewardService.getUserFBOffer(new FBOfferCallback() {
//            @Override
//            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Success Message" + response);
//                    getUserReward();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getUserReward() {
//
//        FBRewardService.getUserFBReward(new FBOfferCallback() {
//            @Override
//            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Success Message" + response);
//                    getUserFBPointBankOffer();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//
//    public void getUserFBPointBankOffer() {
//
//        FBRewardService.getUserFBPointBankOffer(new FBRewardCallback() {
//            @Override
//            public void onFBOfferCallback(FBRewardListItem restaurants, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Success Message" +restaurants);
//                    useOffer("328829","13");
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserOffers Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//    public void useOffer(String offerId, String claimPoints ) {
//
//        FBRewardService.useOffer(offerId,claimPoints,new FBRewardCallback() {
//            @Override
//            public void onFBOfferCallback(FBRewardListItem restaurants, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "useOffer Success Message" + restaurants);
//                    getBonusRuleList();
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "useOffer Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//    public void getBonusRuleList() {
//
//        FBRewardService.getBonusRuleList(new FBBonusRuleListCallback() {
//            @Override
//            public void onBonusRuleListCallback(FBBonusItem restaurants , Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getBonusRuleList Success Message" + restaurants);
//                    getUserLogout("mobilesdk");
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getBonusRuleList Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//    public void getUserLogout(String application) {
//
//        FBSessionService.logout(application,new FBSessionServiceCallback() {
//            @Override
//            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
//                if (error != null)
//
//                {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserLogout Success Message" + spendGoSession);
//
//                } else {
//                    Constants.alertDialogShow(DuplicateNewBasicMainActivity.this, "getUserLogout Error Message");
//                    FBUtils.tryHandleTokenExpiry(DuplicateNewBasicMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//
//
//
//
//
//    public void EventCheck() {
//
//        track_ItemWith("12345", "Lunch", FBEventSettings.GIFT_CARD_ADDED);
//
//    }
//
//    public void EventCheckGuest() {
//        FBPreferences.sharedInstance(this).delete("user_id_forapp");
//
//
//        // track_ItemWith("12345", "Lunch", FBEventSettings.FIRST_TIME_LAUNCH);
//        track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);
//
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (extras != null && (StringUtilities.isValidString(e))) {
//
//        } else {
//
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
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
//    public void track_EvenforGuesttbyName(String eventName) {
//        if (!checkValidCallGuest())
//            return;
//        try {
//            JSONObject data = new JSONObject();
//            data.put("event_name", eventName);
//            addCommonDataGuest(data);
//            if (_app.fbsdkObj.checkAppEventFlag())
//                FBAnalytic.sharedInstance().recordEvent(data);
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }
//
//    public boolean checkValidCallGuest() {
//        if (_app == null)
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
//    public void addCommonDataGuest(JSONObject data) {
//
//        try {
//            data.put("action", "AppEvent");
//            data.put("tenantid", FBConstant.client_tenantid);
//            data.put("lat", _app.fbsdkObj.getmCurrentLocation().getLatitude());
//            data.put("lon", _app.fbsdkObj.getmCurrentLocation().getLongitude());
//            data.put("device_type", FBConstant.DEVICE_TYPE);
//            data.put("device_os_ver", _app.fbsdkObj.getAndroidOs());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            recordErrorEvent();
//        }
//    }
//
//
//    public void addCommonData(JSONObject data) {
//
//        try {
//            data.put("action", "AppEvent");
//            data.put("event_id", generateUniqueEventId());
//            data.put("memberid", FBPreferences.sharedInstance(_app.fbsdkObj.context).getUserMemberforAppId());
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
//    public void changePassword() {
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("oldPassword", "password");
//            object.put("password", "password");
//
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        FBUserService.sharedInstance().changePassword(object, new FBUserService.FBChangePasswordCallback() {
//            @Override
//            public void onChangePasswordCallback(JSONObject response, Exception error) {
//
//            }
//        });
//    }
//
//
//    public void forgetPassword() {
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("email", "vkumar_ic@fishbowl.com");
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//
//        FBUserService.sharedInstance().forgetPassword(object, new FBUserService.FBforgetPasswordCallback() {
//            @Override
//            public void onFBforgetPasswordCallback(JSONObject response, Exception error) {
//
//            }
//        });
//    }
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
