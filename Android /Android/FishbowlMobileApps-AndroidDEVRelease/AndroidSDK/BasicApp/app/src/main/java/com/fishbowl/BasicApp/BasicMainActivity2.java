//package com.fishbowl.BasicApp;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.Preferences.FBPreferences;
//import com.fishbowl.basicmodule.Controllers.FBSdk;
//import com.fishbowl.basicmodule.Models.FBCustomerItem;
//import com.fishbowl.basicmodule.Models.FBOfferItem;
//import com.fishbowl.basicmodule.Models.Member;
//import com.fishbowl.basicmodule.Services.FBStoreService;
//import com.fishbowl.basicmodule.Services.FBUserOfferService;
//import com.fishbowl.basicmodule.Services.FBUserService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
//import com.fishbowl.basicmodule.Utils.FBConstant;
//import com.fishbowl.basicmodule.Utils.FBUtility;
//import com.fishbowl.basicmodule.Utils.FBUtils;
//import com.fishbowl.basicmodule.Utils.StringUtilities;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//
///**
// * Created by digvijay(dj)
// */
//
//public class BasicMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
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
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_basic_main);
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
//            getMobileSettings();
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
//        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback(){
//            public void onLoginMemberCallback(JSONObject response, Exception error){
//
//            }
//        });
//    }
//
//
//    public void getMobileSettings() {
//
//        final JSONObject object = new JSONObject();
//
//        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewSettingsCallback() {
//            @Override
//            public void onViewSettingsCallback(JSONObject response, final Exception error) {
//
//                if(response !=null) {
//
////
////                    FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
////                        @Override
////                        public void OnAllStoreCallback(JSONObject response, Exception error) {
////
////                           if(response!=null){
////                                // getBonusRuleList();
////                                ChecktwentyloginMember();
////
////                            }
////
////                        }
////                    });
//
//                    ChecktwentyloginMember();
//
//
//                }else {
//
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
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
//
//    public void ChecktwentyloginMember(){
//
//        JSONObject object=new JSONObject();
//        try {
//
//
//            object.put("username","703-216-3403");
//            object.put("password","password1");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback(){
//            public void onLoginMemberCallback(JSONObject response, Exception error){
//
//                if(response!=null) {
//                    try {
//                        String secratekey=response.getString("accessToken");
//                        FBPreferences.sharedInstance(BasicMainActivity.this).setAccessTokenforapp(secratekey);
//                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
//                        FBPreferences.sharedInstance(BasicMainActivity.this).setSignin(true);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    CheckTwentyApi();
//
//                }else {
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
//                }
//            }
//
//        });
//    }
//
//
//    public void CheckTwentyApi() {
//
//
//
//        JSONObject object=new JSONObject();
//        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback(){
//            public void onGetMemberCallback(JSONObject response, Exception error){
//
//                if (response!=null) {
//
//                    deviceUpdate();
//                }else {
//
//                }
//            }
//
//        });
//    }
//
//
//
//    public void deviceUpdate(){
//
//
//        JSONObject object=new JSONObject();
//
//        Member member= FBUserService.sharedInstance().member;
//
//        try {
//            object.put("memberid",FBPreferences.sharedInstance(this).getUserMemberforAppId());
//            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
//            object.put("deviceOsVersion", FBConstant.device_os_ver);
//            object.put("deviceType", FBConstant.DEVICE_TYPE);
//            object.put("pushToken",   FBPreferences.sharedInstance(this).getPushToken());
//            object.put("appId",   "com.fishbowl.BasicApp");
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
//                        JSONObject data = new JSONObject();
////                        FBStoreService.sharedInstance().getSearchAllStore(data,"Las",new FBStoreService.FBAllSearchStoreCallback() {
////                                    @Override
////                                    public void OnAllSearchStoreCallback(List<FBStoresSearchItem> response, Exception error) {
////
////                                        if (response != null) {
////                                            favouriteStoreUpdate();
////                                        }
////
////                                    }
////                                }
//
//
//                        FBStoreService.sharedInstance().getStoreDetails("1502",new FBStoreService.FBStoreDetailCallback() {
//                                    @Override
//                                    public void OnFBStoreDetailCallback( JSONObject response, Exception error) {
//
//                                        if (response != null) {
//                                            favouriteStoreUpdate();
//                                        }
//
//                                    }
//                                }
//
//
//                        );
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//                else {
//
//                }
//            }
//        });
//    }
//
//
//
//
//
////    public void StoreDetails(){
////
////        FBStoreService.sharedInstance().getStoreDetails(storeid,new FBStoreService.FBStoreDetailCallback() {
////            @Override
////            public void OnFBStoreDetailCallback( JSONObject response, Exception error) {
////
////                if (response != null) {
////
////                }
////
////            }
////
////
////
////        });
////    }
//
//    public void getUserOffers() {
//        JSONObject data = new JSONObject();
//        FBUserOfferService.sharedInstance().getUserFBOffer(data, " ", new FBUserOfferService.FBOfferCallback() {
//            @Override
//            public void OnFBOfferCallback(JSONObject response, Exception error) {
//
//                getUserReward();
//            }
//        });
//    }
//
//    public void getUserReward() {
//        JSONObject data = new JSONObject();
//        FBUserOfferService.sharedInstance().getUserFBReward(data, " ", new FBUserOfferService.FBRewardCallback() {
//            @Override
//            public void OnFBRewardCallback(JSONObject response, Exception error) {
//
//                getUserFBPointBankOffer();
//            }
//        });
//    }
//
//
//    public void getUserFBPointBankOffer() {
//        JSONObject data = new JSONObject();
//        FBUserOfferService.sharedInstance().getUserFBPointBankOffer(data, " ", new FBUserOfferService.FBPointBankOffer() {
//            public void OnFBPointBankOfferCallback(JSONObject response, Exception error) {
//                useOffer();
//            }
//        });
//    }
//
//
//    public void useOffer(){
//        JSONObject object=new JSONObject();
//        try {
//            object.put("memberId","81");
//            object.put("offerId","328829");
//            object.put("tenantId","581");
//            object.put("claimPoints","13");
//
//
//        }catch (Exception e){
//
//        }
//        FBUserOfferService.sharedInstance().useOffer(object, new FBUserOfferService.FBUseOfferCallback() {
//            @Override
//            public void onUseOfferCallback(JSONObject response, Exception error) {
//                if (response!=null) {
//
//
//
//                }else {
//                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this,error);
//                    getBonusRuleList();
//                }
//            }
//        });
//    }
//
//
//    public void getBonusRuleList(){
//
//        final JSONArray object = new JSONArray();
//        FBUserService.sharedInstance().getBonusRuleList(object, new FBUserService.FBBonusRuleListCallback()
//        {
//
//            public void onBonusRuleListCallback(JSONArray response, Exception error)
//            {try
//            {
//                if (response != null && error==null)
//                {
//                    getUserLogout();
//                }
//                else
//                {
//
//                }
//            }
//            catch (Exception e){
//
//            }
//            }
//        });
//    }
//
//
//
//    public void favouriteStoreUpdate(){
//
//        JSONObject object=new JSONObject();
//        try
//        {
//            object.put("memberid",FBPreferences.sharedInstance(BasicMainActivity.this).getUserMemberforAppId());
//            object.put("storeCode","202");
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
//                if(response!=null){
//
//                    getUserOffers();
//
//
//                }else {
//
//
//                }
//
//            }
//        });
//    }
//
//
//    public void getUserLogout() {
//        final JSONObject object = new JSONObject();
//        FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
//            @Override
//            public void onLogoutCallback(JSONObject response, Exception error) {
//
//
//            }
//        });
//    }
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
