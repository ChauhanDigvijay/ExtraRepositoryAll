package com.fishbowl.BasicApp;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBOfferService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.FBUtils;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by digvijay(dj)
 */

public class BasicMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
    Button bbl;

    Bundle extras;
    private String mPassDataUrl = "";
    private String passPreviewImageURL = "";
    private   String e = "";
    private static final String DEFAULT_STORE_CODE="0145";//Emeryville - 0145 : Merced - 0620
    private static final String DEFAULT_STORE_SEARCH_KEYWORD="Emeryville";
    String offerid=null;
    String clpnid=null;
    String ntype="";
    private static final int PERMISSION_REQUEST_CODE = 1;
    int diifer;

    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer   pmPromotionID ;
    Integer offerID;
    Integer channelID;
    private ArrayList<FBOfferItem> offerList;
    private int offerCount;
    String validityEndDateTime;
    public AuthApplication _app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_main);
        this._app = AuthApplication.getAppContext();


        if (_app.fbsdkObj != null) {
            FBAnalytic.sharedInstance().init(this._app, _app.fbsdkObj.SERVER_URL, "91225258ddb5c8503dce33719c5deda7");
        } else {
            //  FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.SERVER_URL), "91225258ddb5c8503dce33719c5deda7");

        }

        Intent i = getIntent();
        extras = i.getExtras();
        {
            if (extras != null) {
                this.mPassDataUrl = i.getStringExtra("url");
                ntype = i.getStringExtra("ntype");
                String apsStr = i.getExtras().getString("aps");
                passPreviewImageURL = i.getExtras().getString("previewimage");
                try {
                    if(apsStr!=null) {
                        //  enableScreen(false);
                        JSONObject apsJson = new JSONObject(apsStr);
                        if (apsJson.has("alert")) {
                            e = apsJson.getString("alert");
                            if (extras.getString("offerid") != null && (extras.getString("clpnid") != null)) {
                                offerid = extras.getString("offerid");
                                clpnid = extras.getString("clpnid");

                            }
                            String apsStr1 =  i.getExtras().getString("offerid");
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
        getToken();
    }




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (extras != null && (StringUtilities.isValidString(e)))
        {

        }
        else
        {

        }
    }

    @Override
    public void onClick(View v) {

    }

    public void loginMember() {
        final JSONObject object = new JSONObject();



        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback(){
            public void onGetTokencallback(JSONObject response, Exception error){
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



    public void getToken(){

        JSONObject object=new JSONObject();
        try
        {
            object.put("clientId",FBConstant.client_id);
            object.put("clientSecret",FBConstant.client_secret);
            object.put("deviceId",FBUtility.getAndroidDeviceID(this));
            object.put("tenantId",FBConstant.client_tenantid);

        }catch (Exception e){
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback()
        {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if(error==null&&response!=null){

                 //   Constants.alertDialogShow(BasicMainActivity.this,"GetToken Success Message");
                    String secratekey= null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(BasicMainActivity.this).setAccessToken(secratekey);
                   // mobileSettings();

                }else {
                  //  Constants.alertDialogShow(BasicMainActivity.this,"GetToken Error Message");

                }

            }
        });
    }


    public void mobileSettings() {
        String cusId = "0";

        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                if(state) {
                    Log.d("Mobile Settings  Api", "Success");
                    Constants.alertDialogShow(BasicMainActivity.this, "MobileSettingsService Success Message" + state);
                    getViewMobileSettingsServiceSettings();
                    EventCheckGuest();

                }
                else{
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

                if(error==null&&response!=null){

                    Constants.alertDialogShow(BasicMainActivity.this,"MobileSettingsService Success Message"+response);
                    ChecktwentyloginMember();


                }else {

                    Constants.alertDialogShow(BasicMainActivity.this,"MobileSettingsService Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }

    public void ChecktwentyloginMember(){

        JSONObject object=new JSONObject();
        try {

            object.put("username","325-285-2565");
            object.put("password","test123");
        }catch (Exception e){
            e.printStackTrace();
        }
        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback(){
            public void onLoginMemberCallback(JSONObject response, Exception error)
            {
                if(error==null&&response!=null){
                    try {
                        Constants.alertDialogShow(BasicMainActivity.this,"loginMember Success Message"+response);
                        String secratekey=response.getString("accessToken");
                        FBPreferences.sharedInstance(BasicMainActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(BasicMainActivity.this).setSignin(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CheckTwentyApi();

                }else {
                    Constants.alertDialogShow(BasicMainActivity.this,"loginMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }

        });
    }


    public void CheckTwentyApi() {


        JSONObject object=new JSONObject();
        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback(){
            public void onGetMemberCallback(JSONObject response, Exception error){

                if(error==null&&response!=null){


                    Constants.alertDialogShow(BasicMainActivity.this,"getMember Success Message"+response.toString());
                    deviceUpdate();
                }else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }

        });
    }




    public void deviceUpdate(){


        JSONObject object=new JSONObject();

        Member member= FBUserService.sharedInstance().member;

        try {
            object.put("memberid",FBPreferences.sharedInstance(this).getUserMemberforAppId());
            Constants.alertDialogShow(BasicMainActivity.this,"MemberID of user "+FBPreferences.sharedInstance(this).getUserMemberforAppId());
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken",   FBPreferences.sharedInstance(this).getPushToken());
            Constants.alertDialogShow(BasicMainActivity.this,"GCM Push token of user "+FBPreferences.sharedInstance(this).getPushToken());
            object.put("appId",   "com.fishbowl.BasicApp");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().deviceUpdate(object, new FBUserService.FBDeviceUpdateCallback(){
            @Override
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {

                if(error==null&&response!=null){
                    try {
                        Constants.alertDialogShow(BasicMainActivity.this,"deviceUpdate Success Message"+response);
                        EventCheck();
                        //  memberUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"deviceUpdate Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }




    private FBCustomerItem collectCustomerData() {

        FBCustomerItem customer = new FBCustomerItem();
        customer.firstName = "Dj";
        customer.lastName ="Kumar";
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
        customer.smsOpted =  "N";
        customer.emailOpted = "N";
        customer.phoneOpted = "N";
        customer.adOpted = "N";
        customer.loyalityRewards = "150";
        return customer;
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
                if(error==null&&response!=null) {
                    Constants.alertDialogShow(BasicMainActivity.this,"memberUpdate Success Message"+response);

                    GetMember();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"memberUpdate Error Message"+" --"+error);
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }



    public void GetMember() {



        JSONObject object=new JSONObject();
        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback(){
            public void onGetMemberCallback(JSONObject response, Exception error){

                if(error==null&&response!=null){
                    Constants.alertDialogShow(BasicMainActivity.this,"getMember Success Message"+ response);
                    getAllStores();
                }else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }

        });
    }



    public void getAllStores() {
        final JSONObject object = new JSONObject();
        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
            @Override
            public void OnAllStoreCallback(JSONObject response, Exception error) {

                if(error==null&&response!=null){
                    Constants.alertDialogShow(BasicMainActivity.this,"getAllStore Success Message");
                    StoreDetails();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getAllStore Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }


    public void StoreDetails(){

        FBStoreService.sharedInstance().getStoreDetails("168874",new FBStoreService.FBStoreDetailCallback() {
            @Override
            public void OnFBStoreDetailCallback( JSONObject response, Exception error) {

                if(error==null&&response!=null){
                    Constants.alertDialogShow(BasicMainActivity.this,"StoreDetails Success Message"+response);
                    favouriteStoreUpdate();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"StoreDetails Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }

            }



        });
    }

    public void getSearchStore() {
        JSONObject object = new JSONObject();
        try {
            object.put("query","168874");
            object.put("radius","1000");
            object.put("count","10");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        FBStoreService.sharedInstance().getSearchAllStore1(object,"",new FBStoreService.FBAllSearchStorejsonCallback() {
                    @Override
                    public void OnAllSearchStorejsonCallback(JSONObject response, Exception error) {

                        if (response != null)
                        {
                        }
                    }
                }
        );
    }


    public void favouriteStoreUpdate(){

        JSONObject object=new JSONObject();
        try
        {
            object.put("memberid",FBPreferences.sharedInstance(BasicMainActivity.this).getUserMemberforAppId());
            object.put("storeCode","168874");

        }catch (Exception e){
            e.printStackTrace();
        }


        FBUserService.sharedInstance().favourteStoreUpdate(object, new FBUserService.FBFavouriteStoreUpdateCallback()
        {
            @Override
            public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error) {
                if(error==null&&response!=null){

                    Constants.alertDialogShow(BasicMainActivity.this,"favourteStoreUpdate Success Message"+response);
                    getMenuCategory();

                }else {

                    Constants.alertDialogShow(BasicMainActivity.this,"favourteStoreUpdate Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
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
                            if(error==null&&response!=null){

                                Constants.alertDialogShow(BasicMainActivity.this,"getMenuCategory Success Message"+response);
                                getMenuSubCategory();
                            }
                            else {
                                Constants.alertDialogShow(BasicMainActivity.this,"getMenuCategory Error Message");
                                FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
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
                    if(error==null&&response!=null){

                        Constants.alertDialogShow(BasicMainActivity.this,"getMenuSubCategory Success Message"+response);
                        getMenuDrawerListProduct();
                    }
                    else {
                        Constants.alertDialogShow(BasicMainActivity.this,"getMenuSubCategory Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                    }
                } catch (Exception e) {

                }
            }

        });
    }



    public void getMenuDrawerListProduct() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getDrawerProductList(object, String.valueOf(168589), String.valueOf(22),String.valueOf(223), new FBUserService.FBMenuDrawerCallback() {

            public void onMenuDrawerCallback(JSONObject response, Exception error) {
                try {
                    if(error==null&&response!=null){
                        Constants.alertDialogShow(BasicMainActivity.this,"getDrawerProductList Success Message"+response);
                        getMenuProductDetail();
                    }
                    else {
                        Constants.alertDialogShow(BasicMainActivity.this,"getDrawerProductList Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                    }
                } catch (Exception e) {

                }
            }

        });
    }


    public void getMenuProductDetail() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuProduct(object, String.valueOf(168589), String.valueOf(22),String.valueOf(223), String.valueOf(1017), new FBUserService.FBMenuProductCallback() {

            public void onMenuProductCallback(JSONObject response, Exception error) {
                try {
                    if(error==null&&response!=null){

                        Constants.alertDialogShow(BasicMainActivity.this,"getMenuProduct Success Message"+response);
                        getUserOffers();
                    }
                    else {
                        Constants.alertDialogShow(BasicMainActivity.this,"getMenuProduct Error Message");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
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
                if(error==null&&response!=null) {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBOffer Success Message"+response);
                    getUserReward();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }

    public void getUserReward() {
        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBReward(data, " ", new FBUserOfferService.FBRewardCallback() {
            @Override
            public void OnFBRewardCallback(JSONObject response, Exception error) {
                if(error==null&&response!=null) {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBReward Success Message"+response);
                    getUserFBPointBankOffer();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBReward Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }


    public void getUserFBPointBankOffer() {
        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBPointBankOffer(data, " ", new FBUserOfferService.FBPointBankOffer() {
            public void OnFBPointBankOfferCallback(JSONObject response, Exception error) {
                if(error==null&&response!=null) {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBPointBankOffer Success Message"+response);
                    useOffer();
                }
                else {
                    Constants.alertDialogShow(BasicMainActivity.this,"getUserFBPointBankOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }


    public void useOffer(){
        JSONObject object=new JSONObject();
        try {
            object.put("memberId","81");
            object.put("offerId","328829");
            object.put("tenantId","581");
            object.put("claimPoints","13");


        }catch (Exception e){

        }
        FBUserOfferService.sharedInstance().useOffer(object, new FBUserOfferService.FBUseOfferCallback() {
            @Override
            public void onUseOfferCallback(JSONObject response, Exception error) {
                if(error==null&&response!=null) {

                    Constants.alertDialogShow(BasicMainActivity.this,"useOffer Success Message"+response);
                    getBonusRuleList();
                }else {
                    Constants.alertDialogShow(BasicMainActivity.this,"useOffer Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                    getBonusRuleList();
                }
            }
        });
    }

    public void getBonusRuleList(){

        final JSONArray object = new JSONArray();
        FBUserService.sharedInstance().getBonusRuleList(object, new FBUserService.FBBonusRuleListCallback()
        {

            public void onBonusRuleListCallback(JSONArray response, Exception error)
            {try
            {
                if (response != null && error==null)
                {
                    Constants.alertDialogShow(BasicMainActivity.this,"getBonusRuleList Success Message"+response);

                    getUserLogout();
                }
                else
                {
                    Constants.alertDialogShow(BasicMainActivity.this,"getBonusRuleList Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);

                }
            }
            catch (Exception e){

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

                if (response != null && error==null)
                {
                    Constants.alertDialogShow(BasicMainActivity.this, "Congratution logout Success Message"+response);

                }
                else
                {
                    Constants.alertDialogShow(BasicMainActivity.this,"logout Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);

                }

            }
        });
    }
    public void getOfferById(){

        JSONObject object=new JSONObject();

        FBOfferService.sharedInstance().getFBOfferByOfferId(object, offerid,new FBOfferService.FBOfferByIdCallback()
        {
            @Override
            public void onFBOfferByIdCallback(JSONObject response, Exception error) {
                if(response!=null){

                }else {
                }


            }
        });
    }

    public void getAllRewardsOffer(){

        JSONObject object=new JSONObject();

        FBUserService.sharedInstance().getAllRewardOfferApi(object, new FBUserService.FBAllRewardOfferCallback()
        {
            @Override
            public void onAllRewardOfferCallback(JSONObject response, Exception error) {
                if(response!=null){

                }else {
                }

            }
        });
    }


    public void EventCheck()
    {

            track_ItemWith("12345", "Lunch", FBEventSettings.ACCEPT_REWARD);

    }

    public void EventCheckGuest()
    {

            // track_ItemWith("12345", "Lunch", FBEventSettings.FIRST_TIME_LAUNCH);
            track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);

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
            data.put("memberid", FBPreferences.sharedInstance(_app.fbsdkObj.context).getUserMemberforAppId());
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
            object.put("oldPassword","password");
            object.put("password","password");

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
            object.put("email","vkumar_ic@fishbowl.com");
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
    public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem) {

    }

    @Override
    public void onFBRegistrationError(String error) {

    }
}
