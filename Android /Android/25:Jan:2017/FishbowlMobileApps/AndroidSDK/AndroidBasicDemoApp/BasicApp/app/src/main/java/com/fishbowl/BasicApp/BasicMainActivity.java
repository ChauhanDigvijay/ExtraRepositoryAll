package com.fishbowl.BasicApp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardActivity;
import com.BasicApp.Activites.NonGeneric.Offer.PassDetail_Activity;
import com.BasicApp.Activites.NonGeneric.Offer.PushDetail_Activity;
import com.BasicApp.ActivityModel.Authentication.SignIn.SignInModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBOfferPushCallback;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

  //  ProgressBarHandler progressBarHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_main);
     //   progressBarHandler = new ProgressBarHandler(this);
        FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.APP_OPEN);
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
                            //String apsStr1 =  i.getExtras().getString("offerid");
                            //String clpnid2 = i.getExtras().getString("clpnid");
                            //FBAnalyticsManager.sharedInstance().track_OfferItemWith(apsStr1,clpnid2,apsStr1,clpnid2,FBEventSettings.PUSH_OPEN);
                            //It has been implemented in fbSdk processPushMessage SO I have Remove It

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

       // mobileSettings();
      //  trackFirstLaunch();
    }

    private void trackFirstLaunch() {
        boolean isFirstLaunch =  FBPreferences.sharedInstance(BasicMainActivity.this).getFirsttime_launch();
        if (!isFirstLaunch) {
            for(int i=0;i<=5;i++) {
                FBAnalyticsManager.sharedInstance().track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);
            }
            FBPreferences.sharedInstance(BasicMainActivity.this).setFirsttime_launch(true);
        }


    }
//    public void mobileSettings() {
//        progressBarHandler.show();
//        String cusId = "0";
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if(state) {
//                    Log.d("Mobile Settings  Api", "Success");
//                    trackFirstLaunch();
//                    progressBarHandler.dismiss();
//                }
//                else{
//
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
//                    progressBarHandler.dismiss();
//                }
//            }
//        });
//    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (extras != null && (StringUtilities.isValidString(e))) {

            checkPushandPass();

        }else {
            getMobileSettings();
           // getThemeMobileSettingsServiceSettings();

        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, SignInActivity.class);
        this.startActivity(i);
    }

//    public void getThemeMobileSettingsServiceSettings() {
//
//        final JSONObject object = new JSONObject();
//
//        FBThemeMobileSettingsService.sharedInstance().getThemeSettings(object, new FBThemeMobileSettingsService.FBThemeSettingsCallback() {
//            @Override
//            public void onThemeSettingsCallback(JSONObject response, final Exception error) {
//
//
//                if(response !=null) {
//
//
//                    FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
//                        @Override
//                        public void OnAllStoreCallback(JSONObject response, Exception error) {
//
//                            if(response!=null){
//
//
//                            }
//
//                        }
//                    });
//
//                    if(  FBPreferences.sharedInstance(AuthApplication.instance).IsSignin())  {
//
//                        Timer timer = new Timer();
//                        timer.schedule(new TimerTask(){
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                getMember();
//                            }}, 1500);
//
//
//                    }
//                    else
//                    if(true) {
//                        Timer timer = new Timer();
//                        timer.schedule(new TimerTask(){
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                Intent i = new Intent(BasicMainActivity.this,SignInActivity.class);
//                                startActivity(i);
//                                BasicMainActivity.this.finish();
//                            }}, 1500);
//
//                    }
//                }else {
//
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
//                }
//            }
//        });
//    }


    public void getMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(FBViewMobileSettingsService instance , final Exception error) {

                if(instance !=null) {


//                    FBRestaurantService.getAllRestaurants(new FBRestaurantServiceCallback() {
//                        @Override
//                        public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
//                            if (error != null)
//
//                            {
//                            //    Constants.alertDialogShow(BasicMainActivity.this, "getAllStores Success Message" + response);
//
//
//                            } else {
//                            //    Constants.alertDialogShow(BasicMainActivity.this, "getAllStores Error Message");
//                            //    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
//                            }
//                        }
//                    });

                    if(  FBPreferences.sharedInstance(AuthApplication.instance).IsSignin())  {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask(){
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                             //   GetMember();

                                Intent i = new Intent(BasicMainActivity.this, SignInModelActivity.class);
                                startActivity(i);
                                BasicMainActivity.this.finish();

                            }}, 1500);


                    }
                    else
                    {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask(){
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Intent i = new Intent(BasicMainActivity.this,SignInModelActivity.class);
                                startActivity(i);
                                BasicMainActivity.this.finish();
                            }}, 1500);

                    }
                }else {

                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
                }
            }
        });
    }



//    public void getMember(){
//
//        JSONObject object=new JSONObject();
//
//        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback(){
//            public void onGetMemberCallback(JSONObject response, Exception error){
//
//                if (response!=null) {
//                    Intent i = new Intent(BasicMainActivity.this, DashboardActivity.class);
//                    startActivity(i);
//                    deviceUpdate();
//                    BasicMainActivity.this.finish();
//                }else {
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
//                }
//            }
//
//        });
//    }

//
//    public void deviceUpdate(){
//
//
//        JSONObject object=new JSONObject();
//
//        Member member= FBUserService.sharedInstance().member;
//
//        try {
//            object.put("memberid",member.customerID);
//            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
//            object.put("deviceOsVersion", FBConstant.device_os_ver);
//            object.put("deviceType", FBConstant.DEVICE_TYPE);
//            object.put("pushToken",   FBPreferences.sharedInstance(this).getPushToken());
//            //object.put("appId",   "com.olo.jambajuiceapp");
//            object.put("appId",   "com.fishbowl.mybistro");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        FBUserService.sharedInstance().deviceUpdate(object, new FBUserService.FBDeviceUpdateCallback(){
//            @Override
//            public void onDeviceUpdateCallback(JSONObject response, Exception error) {
//
//                if (response != null) {
//                    try {
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//                else {
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
//
//                }
//
//            }
//        });
//    }


    public void checkPushandPass(){
        if (extras != null && (StringUtilities.isValidString(e)))

        {

            if (mPassDataUrl.equalsIgnoreCase("pass")) {

                Intent intent = new Intent(BasicMainActivity.this, PassDetail_Activity.class);
                Bundle extras = new Bundle();
                extras.putString("Url", passPreviewImageURL);
                extras.putString("Title", e);
                extras.putString("offerId", offerid);
                intent.putExtras(extras);
                startActivity(intent);

                finish();
            } else if (mPassDataUrl.equalsIgnoreCase("")) {

                fetchDataOffer();


            }

        }
    }


    //dj UserOffer
    private void fetchDataOffer() {

        JSONObject object = new JSONObject();
        //   enableScreen(false);
        FBUserOfferService.sharedInstance().getFBOfferbyofferid(object,offerid, new FBOfferPushCallback() {
            @Override
            public void OnFBOfferPushCallback(JSONObject response, Exception error) {

                try {
                    if (response.has("inAppOfferList")) {

                        JSONArray jsonArray = response.getJSONArray("inAppOfferList");

                        if (jsonArray != null && jsonArray.length() > 0) {

                            offerCount = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject wallObj = jsonArray.getJSONObject(i);
                                if (wallObj != null) {

                                    if (wallObj.has("campaignTitle")) {
                                        offerTitle = (String) wallObj.get("campaignTitle");

                                    }
                                    if (wallObj.has("campaignDescription")) {
                                        offerDescription = (String) wallObj.get("campaignDescription");

                                    }
                                    if (wallObj.has("validityEndDateTime")) {
                                        validityEndDateTime = (String) wallObj.get("validityEndDateTime");

                                    }
                                    if (wallObj.has("isPMOffer")) {
                                        ispmOffer = wallObj.getBoolean("isPMOffer");

                                    }

                                    if (wallObj.has("passPreviewImageURL")) {
                                        passPreviewImageURL = (String) wallObj.get("passPreviewImageURL");

                                    }
                                    if (wallObj.has("campaignId")) {
                                        offerID = (Integer) wallObj.get("campaignId");

                                    }
                                    if (wallObj.has("channelID")) {
                                        channelID = (Integer) wallObj.get("channelID");

                                    }

                                    if (wallObj.has("promotionID")) {
                                        pmPromotionID = (Integer) wallObj.get("promotionID");


                                    }

                                }


                            }

                        }


                        if (StringUtilities.isValidString(validityEndDateTime)) {
                            Date d2 = FBUtils.getDateFromString(validityEndDateTime, null);
                            diifer = FBUtils.daysBetween(new Date(), d2);
                        }
                        Intent intent = new Intent(BasicMainActivity.this, PushDetail_Activity.class);
                        Bundle extras = new Bundle();
                        extras.putString("Title", offerTitle);
                        extras.putInt("Expire", diifer);
                        extras.putInt("promotionId", pmPromotionID);
                        extras.putString("offerId", String.valueOf(offerID));
                        extras.putBoolean("isPMOffer", false);
                        extras.putString("desc", offerDescription);
                        intent.putExtras(extras);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //  Utils.showErrorAlert(PassDetail_Activity.this, "Empty Response");
                        FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);



                    }

                }
                catch(Exception ex){
                    ex.printStackTrace();

                }
            }


        });

    }


    public void loginMember(String name, String password) {

        FBSessionService.loginMember(name, password, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {
                    //  Constants.alertDialogShow(SignInActivity.this, "loginMember Success Message" + spendGoSession);
                    String secratekey = spendGoSession.getAccessToken();
                    FBPreferences.sharedInstance(BasicMainActivity.this).setAccessTokenforapp(secratekey);
                    FBUserService.sharedInstance().access_token = spendGoSession.getAccessToken();
                    FBPreferences.sharedInstance(BasicMainActivity.this).setSignin(true);
                    GetMember();

                } else {
                    //    Constants.alertDialogShow(SignInActivity.this, "loginMember Error Message");
                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    public void GetMember() {

        FBSessionService.getMember(new FBUserServiceCallback() {
            @Override
            public void onUserServiceCallback(FBMember user, Exception error) {


                if (user != null) {
                    Intent i = new Intent(BasicMainActivity.this, DashboardActivity.class);
                    startActivity(i);
                    UpdateDevice();
                    BasicMainActivity.this.finish();
                    UpdateDevice();

                }
            }
        });
    }

    public void UpdateDevice() {

        FBSessionService.deviceUpdate(new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback( FBSessionItem spendGoSession, Exception error) {
                if (error != null)

                {

                } else {


                }
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
