//package com.fishbowl.BasicApp;
//
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.Preferences.FBPreferences;
//import com.fishbowl.basicmodule.Analytics.FBAnalytic;
//import com.fishbowl.basicmodule.Controllers.FBSdk;
//import com.fishbowl.basicmodule.Models.FBCustomerItem;
//import com.fishbowl.basicmodule.Models.FBOfferItem;
//import com.fishbowl.basicmodule.Services.FBMobileSettingService;
//import com.fishbowl.basicmodule.Services.FBStoreService;
//import com.fishbowl.basicmodule.Services.FBUserService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
//import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService.FBViewSettingsCallback;
//import com.fishbowl.basicmodule.Services.FB_LY_UserOfferService;
//import com.fishbowl.basicmodule.Services.FB_LY_UserService;
//import com.fishbowl.basicmodule.Utils.FBConstant;
//import com.fishbowl.basicmodule.Utils.FBUtility;
//import com.fishbowl.basicmodule.Utils.FBUtils;
//import com.fishbowl.basicmodule.Utils.StringUtilities;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashSet;
//
//
///**
// * Created by digvijay(dj)
// */
//
//public class LoyaltyMainActivity extends Activity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {
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
//    HashSet<Integer> checkedValue = new HashSet<Integer>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//
//
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
//        gettestToken();
//
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
//        if (extras != null && (StringUtilities.isValidString(e)))
//        {
//
//        }
//        else
//        {
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
//
//    public void gettestToken(){
//        JSONObject object=new JSONObject();
//        try
//        {
//            object.put("clientId", FBConstant.client_id);
//            object.put("clientSecret",FBConstant.client_secret);
//            object.put("deviceId", FBUtility.getAndroidDeviceID(LoyaltyMainActivity.this));
//            object.put("tenantId",FBConstant.client_tenantid);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        FB_LY_UserService.sharedInstance().getTokenApi(object, new FB_LY_UserService.FBGetTokenCallback()
//        {
//            @Override
//            public void onGetTokencallback(JSONObject response, Exception error) {
//                if(response!=null){
//                    String secratekey = null;
//                    mobileSettings();
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"GetToken Success Message");
//
//                }else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"GetToken Error Message");
//                }
//            }
//        });
//    }
//
//
//    public void mobileSettings() {
//        String cusId = "0";
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if(state) {
//                    Log.d("Mobile Settings  Api", "Success");
//                    Constants.alertDialogShow(LoyaltyMainActivity.this, "MobileSettingsService Success Message" + state);
//                    getViewMobileSettingsServiceSettings();
//                }
//                else{
//                    Constants.alertDialogShow(LoyaltyMainActivity.this, "MobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this, error);
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
//                if(error==null&&response!=null){
//
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"MobileSettingsService Success Message"+response);
//
//                    loginMember();
//
//                }else {
//
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"MobileSettingsService Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//        });
//    }
//    public void loginMember()
//    {
//        JSONObject object = new JSONObject();
//
//        Date d = Calendar.getInstance().getTime();
//
//        String format = null;
//        if (format == null)
//            format = "yyyy-MM-dd'T'hh:mm:ss";
//
//        SimpleDateFormat formatter = new SimpleDateFormat(format);
//        String currentData = formatter.format(d);
//
//        try
//        {   object.put("username", "111-333-3333");
//            object.put("password", "password");
//            object.put("eventDateTime",currentData);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        FB_LY_UserService.sharedInstance().loginMember(object, new FB_LY_UserService.FBLoginMemberCallback()
//        {
//            public void onLoginMemberCallback(JSONObject response, Exception error)
//            {
//                if (response != null)
//                {
//                    try
//                    {
//                        String secratekey = response.getString("accessToken");
//                        FBPreferences.sharedInstance(LoyaltyMainActivity.this).setAccessTokenforapp(secratekey);
//                        FB_LY_UserService.sharedInstance().access_token = response.getString("accessToken");
//                        FBPreferences.sharedInstance(LoyaltyMainActivity.this).setSignin(true);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                    getMember();
//                }
//                else
//                {
//
//                }
//            }
//
//        });
//    }
//
//
//
//
//    public void getMember()
//    {
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback()
//        {
//            public void onGetMemberCallback(JSONObject response, Exception error)
//            {
//                if (response != null)
//                {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getMember Success Message"+ response);
//                    getAllStores();
//
//                }
//                else
//                {
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this, error);
//
//                }
//            }
//        });
//    }
//
//    public void getAllStores() {
//        final JSONObject object = new JSONObject();
//        FBStoreService.sharedInstance().getAllStore(new FBStoreService.FBAllStoreCallback() {
//            @Override
//            public void OnAllStoreCallback(JSONObject response, Exception error) {
//
//                if(error==null&&response!=null){
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getAllStore Success Message");
//                    getUserRewardPoint();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getAllStore Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
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
//    public void getUserRewardPoint()
//    {
//        JSONObject data = new JSONObject();
//        FB_LY_UserOfferService.sharedInstance().getUserClypRewardPoint(data, " ", new FB_LY_UserOfferService.ClypRewardPointCallback()
//        {
//            @Override
//            public void onClypRewardPointCallback(JSONObject response, Exception error)
//            { if(error==null&&response!=null){
//                Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserRewardPoint Success Message");
//                getUserOffer();
//            }
//            else {
//                Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserRewardPoint Error Message");
//                FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//            }
//
//            }
//        });
//    }
//
//
//    public  void getUserOffer()
//    {
//        JSONObject data = new JSONObject();
//        FB_LY_UserOfferService.sharedInstance().getUserClypOffer(data, " ", new FB_LY_UserOfferService.ClypOfferCallback()
//        {
//            @Override
//            public void onClypOfferCallback(JSONObject response, Exception error)
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserFBOffer Success Message"+response);
//                    getUserReward();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserFBOffer Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//        });
//    }
//
//    public  void getUserReward()
//    {
//        JSONObject data = new JSONObject();
//        FB_LY_UserOfferService.sharedInstance().getUserClypReward(data, " ", new FB_LY_UserOfferService.ClypRewardCallback()
//        {
//            @Override
//            public void onClypRewardCallback(JSONObject response, Exception error)
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserFBReward Success Message"+response);
//                    getBonusRuleList();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getUserFBReward Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//        });
//    }
//    public void getBonusRuleList() {
//        final JSONArray object = new JSONArray();
//        FB_LY_UserOfferService.sharedInstance().getBonusRuleList(object, new FB_LY_UserOfferService.FBBonusRuleListCallback() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            public void onBonusRuleListCallback(JSONArray response, Exception error) {
//                try {
//                    if(error==null&&response!=null) {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getBonusRuleList Success Message"+response);
//                        getState();
//                    }
//                    else {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getBonusRuleList Error Message");
//                        FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//    }
//
//    public void getState() {
//
//        JSONObject object = new JSONObject();
//        // progressBarHandler.show();
//        //   pd.show();
//        FB_LY_UserService.sharedInstance().getState(object, new FB_LY_UserService.FBStateCallback() {
//            @Override
//            public void onStateCallback(JSONObject response, Exception error) {
//
//                try {
//                    if(error==null&&response!=null) {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getState Success Message"+response);
//                        getLoyaltyMessageType();
//                    }
//                    else {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getState Error Message");
//                        FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//
//        });
//    }
//
//    public void getLoyaltyMessageType(){
//
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getLoyaltyMessageType(object, new FB_LY_UserService.FBLoyaltyMessageTypeCallback()
//        {
//            public void onLoyaltyMessageTypeCallback(JSONObject response, Exception error)
//            {try
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyMessageType Success Message"+response);
//                    getLoyaltyAreaType();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyMessageType Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//            catch (Exception e){}
//            }
//        });
//
//    }
//    public void getLoyaltyAreaType(){
//
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getLoyaltyAreaType(object, new FB_LY_UserService.FBLoyaltyAreaTypeCallback()
//        {
//            public void onLoyaltyAreaTypeCallback(JSONObject response, Exception error)
//            {try
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyAreaType Success Message"+response);
//                    getActivity();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyAreaType Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//            catch (Exception e){}
//            }
//        });
//    }
//
//
//
//    public  void getActivity()
//    {
//
//        JSONObject obj = new JSONObject();
//        try
//        {
//            obj.put("areaType","");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        FB_LY_UserService.sharedInstance().getActivity(obj, new FB_LY_UserService.FBGetActivity()
//        {
//
//            @Override
//            public void onFBGetActivity(JSONObject response, Exception error)
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getActivity Success Message"+response);
//                    getActivitySort();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getActivity Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//        });
//    }
//
//
//    public  void getActivitySort()
//    {
//        String activityType="POINT_RULE";
//        org.json.JSONArray jsonarray = new  org.json.JSONArray();
//        try
//        {
//            JSONObject obj = new JSONObject();
//            obj.put("fieldName","eventDate");
//            obj.put("direction","DESC");
//            jsonarray.put(obj);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//
//        FB_LY_UserService.sharedInstance().getActivitySort(jsonarray,activityType, new FB_LY_UserService.FBGetActivity()
//        {
//
//            @Override
//            public void onFBGetActivity(JSONObject response, Exception error)
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getActivitySort Success Message"+response);
//                    getLoyaltyMessages();
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"getActivitySort Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//            }
//        });
//    }
//
//    public void getLoyaltyMessages(){
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getLoyaltyMessage(object, new FB_LY_UserService.FBLoyaltyMessageCallback()
//        {
//            public void onLoyaltyMessageCallback(JSONObject response, Exception error)
//            {
//                try
//                {
//                    if(error==null&&response!=null) {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyMessages Success Message"+response);
//
//                        Memberupdate();
//                    }
//                    else {
//                        Constants.alertDialogShow(LoyaltyMainActivity.this,"getLoyaltyMessages Error Message");
//                        FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                    }
//                }
//                catch (Exception e){}
//            }
//        });
//
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
//        customer.cellPhone = "111-333-3333";
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
//    public  void Memberupdate()
//    {
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
//        FB_LY_UserService.sharedInstance().memberUpdate(object, new FB_LY_UserService.FBMemberUpdateCallback()
//        {
//            @Override
//            public void onMemberUpdateCallback(JSONObject response, Exception error)
//            {
//                if(error==null&&response!=null) {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"memberUpdate Success Message"+response);
//                    getUserLogout();
//
//                }
//                else {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"memberUpdate Error Message"+" --"+error);
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//                }
//
//            }
//        });
//    }
//
//
//    public void getUserLogout()  {
//        final JSONObject object = new JSONObject();
//        try {
//            object.put("Application", "mobilesdk");
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
//            @Override
//            public void onLogoutCallback(JSONObject response, Exception error) {
//
//                if (response != null && error==null)
//                {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this, "Congratution logout Success Message"+response);
//
//                }
//                else
//                {
//                    Constants.alertDialogShow(LoyaltyMainActivity.this,"logout Error Message");
//                    FBUtils.tryHandleTokenExpiry(LoyaltyMainActivity.this,error);
//
//                }
//
//            }
//        });
//    }
//
//
//
//
//
//    public void contactUs()
//    {
//
//        String sub = "Vivek";
//        String msg= "kumar" ;
//        String area= null ;
//        String msgType= null;
//        JSONObject obj = new JSONObject();
//        try
//        {
//            obj.put("subject",sub);
//            obj.put("description",msg);
//            obj.put("messageType",msgType);
//            obj.put("areaType",area);
//            obj.put("memberId", FB_LY_UserService.sharedInstance().member.customerID);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        // progressBar..Handler.show();
//
//        FB_LY_UserService.sharedInstance().contactUs(obj, new FB_LY_UserService.FBContactUsCallback()
//        {
//            @Override
//            public void onContactUsCallback(JSONObject response, Exception error)
//            {
//                // progressBarHandler.hide();
//                if (response != null)
//                {
//                    if(response.has("successFlag")) {
//                        try {
//                            if(response.getString("successFlag").equalsIgnoreCase("true")) {
//
//                                FBUtils.showAlert(LoyaltyMainActivity.this,"Message successfully sent");
//                                ;
//                            }
//                            else
//                            {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                else
//                {
//
//                    FBUtils.showErrorAlert(LoyaltyMainActivity.this, error);
//                }
//            }
//        });
//    }
//
//
//
//    public void  getLoyaltyDeleteMessageStatus()
//    {
//        if (checkedValue != null) {
//            if (checkedValue.size() > 0) {
//                //  p.show();
//                JSONObject obj = new JSONObject();
//
//                JSONArray jsArray = new JSONArray(checkedValue);
//
//                FB_LY_UserService.getLoyaltyMessageStatus(obj, jsArray,new FB_LY_UserService.FBLoyaltyMessageCallback() {
//
//
//                    @Override
//                    public void onLoyaltyMessageCallback(JSONObject response, Exception error)
//                    {
//
//                        if (response != null)
//                        {
//                        }
//                        else
//                        {
//                        }
//                    }
//                });}}}
//
//    public void  getLoyaltyMarkMessageStatus()
//    {
//        if (checkedValue != null) {
//            if (checkedValue.size() > 0) {
//                //   p.show();
//
//                JSONObject obj = new JSONObject();
//                JSONArray jsArray = new JSONArray(checkedValue);
//                FB_LY_UserService.getLoyaltyMarkMessageStatus(obj, jsArray,new FB_LY_UserService.FBLoyaltyMessageCallback() {
//
//
//                    @Override
//                    public void onLoyaltyMessageCallback(JSONObject response, Exception error)
//                    {
//
//                        if (response != null)
//                        {
//                            getLoyaltyMessages();
//                        }
//                        else
//                        {
//
//                        }
//                    }
//                });
//            }}}
//
//
//
//    public void  getLoyaltyUnMarkedMessageStatus()
//    {
//
//        if (checkedValue != null) {
//            if (checkedValue.size() > 0) {
//                JSONObject obj = new JSONObject();
//
//                JSONArray jsArray = new JSONArray(checkedValue);
//
//                FB_LY_UserService.getLoyaltyUnMarkMessageStatus(obj, jsArray, new FB_LY_UserService.FBLoyaltyMessageCallback() {
//
//                    @Override
//                    public void onLoyaltyMessageCallback(JSONObject response, Exception error) {
//
//                        if (response != null) {
//                            getLoyaltyMessages();
//                        } else {
//
//                        }
//                    }
//                });
//            }
//        }}
//
//
//
//
//
//
//    public void getCountry() {
//
//        JSONObject object = new JSONObject();
//        // progressBarHandler.show();
//        //   pd.show();
//        FB_LY_UserService.sharedInstance().getCountry(object, new FB_LY_UserService.FBCountryCallback() {
//            @Override
//            public void onCountryCallback(JSONObject response, Exception error) {
//
//                try {
//                    if (error == null && response != null)
//                    {
//                        if (!response.has("countryList"))
//                            return;
//
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//
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
