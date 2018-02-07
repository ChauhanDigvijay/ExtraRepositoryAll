package com.fishbowl.loyaltymodule.Services;


import android.util.Log;

import com.fishbowl.loyaltymodule.Controllers.FBSdk;
import com.fishbowl.loyaltymodule.Interfaces.FBServiceCallback;
import com.fishbowl.loyaltymodule.Models.FBStoresItem;
import com.fishbowl.loyaltymodule.Models.Member;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.FBPreferences;
import com.fishbowl.loyaltymodule.Utils.FBUtility;
import com.fishbowl.loyaltymodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */

public class FB_LY_UserService {

    public Member member;
    private static FBSdk clpsdk;
    public static FB_LY_UserService instance;
    public Map<Integer, FBStoresItem> mapIdToStore = new HashMap<Integer, FBStoresItem>();//storesMap
    public Map<String, Integer> mapNumToId = new HashMap<String, Integer>();//storesMapforId

    public List<FBStoresItem> allStoreFromServer;

    public String access_token = null;


    public static FB_LY_UserService sharedInstance() {
        if (instance == null) {
            instance = new FB_LY_UserService();

        }
        return instance;
    }

    public void init(FBSdk _clpsdk) {
        clpsdk = _clpsdk;
        member = new Member();

    }

    public void createMember(JSONObject parameter, final FBCreateMemberCallback callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }

        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBCreateMemberApi, str, getHeader1(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onCreateMemberCallback(response, null);

                } else {
                    callback.onCreateMemberCallback(null, error);
                }
            }

        });

    }


    public void loginMember(JSONObject parameter, final FBLoginMemberCallback callback) {


        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.FBLoginApi, str, getHeader2(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    callback.onLoginMemberCallback(response, null);

                } else {

                    callback.onLoginMemberCallback(null, error);
                }

            }

        });
    }


    public void loginMemberfortest(JSONObject parameter, final FBLoginMemberCallback callback) {


        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.FBLoginApi, str, getHeaderfortest(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    callback.onLoginMemberCallback(response, null);

                } else {

                    callback.onLoginMemberCallback(null, error);
                }

            }

        });
    }

    public void getMember(JSONObject parameter, final FBGetMemberCallback callback) {

        String str = parameter.toString();

        FBService.getInstance().get(FBConstant.FBGetMemberApi, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    member.initWithJson(response);

                    callback.onGetMemberCallback(response, null);
                } else {
                    callback.onGetMemberCallback(null, error);
                }

            }
        });

    }


    public void memberUpdate(JSONObject parameter, final FBMemberUpdateCallback callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str = parameter.toString();
        FBService.getInstance().put(FBConstant.FBMemberUpdateApi, str, updateMemberHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {
                    // member.initWithJson(response);
                    callback.onMemberUpdateCallback(response, null);
                } else {
                    callback.onMemberUpdateCallback(null, error);
                }
            }
        });

    }


    public void deviceUpdate(JSONObject parameter, final FBDeviceUpdateCallback callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.DeviceUpdateApi, str, deviceUpdateHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {
                    //member.initWithJson(response);
                    callback.onDeviceUpdateCallback(response, null);
                } else {
                    callback.onDeviceUpdateCallback(null, error);
                }
            }
        });

    }


    public void changePassword(JSONObject parameter, final FBChangePasswordCallback callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }

        String str = parameter.toString();
        FBService.getInstance().put(FBConstant.FBChangePasswordApi, str, changePassHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error == null && response != null) {
                    callback.onChangePasswordCallback(response, null);
                } else {
                    callback.onChangePasswordCallback(null, error);
                }
            }
        });

    }


    public void logout(JSONObject parameter, final FBLogoutCallback callback) {

        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.FBLogoutApi, str, logoutHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {
                    callback.onLogoutCallback(response, null);
                } else {
                    callback.onLogoutCallback(null, error);
                }
            }

        });
    }





    public void contactUs(JSONObject parameter, final FBContactUsCallback callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }

        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.FBContactUsApi, str, contactUsHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error == null && response != null) {
                    Log.d(errorMessage,response.toString());
                    callback.onContactUsCallback(response, null);
                } else {
                    callback.onContactUsCallback(null, error);
                }
            }
        });

    }

    public void getLoyaltyAreaType(JSONObject parameter, final FBLoyaltyAreaTypeCallback callback) {

        String str = parameter.toString();

        FBService.getInstance().get(FBConstant.FBLoyaltyAreaType, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    //     areaType.areaJson(response);

                    callback.onLoyaltyAreaTypeCallback(response, null);
                } else {
                    callback.onLoyaltyAreaTypeCallback(null, error);
                }

            }
        });

    }

    public void getLoyaltyMessageType(JSONObject parameter, final FBLoyaltyMessageTypeCallback callback) {

        String str = parameter.toString();

        FBService.getInstance().get(FBConstant.FBLoyaltyMessageType, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    //     areaType.areaJson(response);

                    callback.onLoyaltyMessageTypeCallback(response, null);
                } else {
                    callback.onLoyaltyMessageTypeCallback(null, error);
                }

            }
        });

    }



    public void getLoyaltyMessage(JSONObject parameter, final FBLoyaltyMessageCallback callback) {

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }

        try {

            parameter.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
           // parameter.put("activityType",parameter.get("areaType") );
            parameter.put("startIndex", "0");
            parameter.put("count", "10");

        } catch (Exception e) {
        }



        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBLoyaltyMessage, str, getHeader1(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onLoyaltyMessageCallback(response, null);

                } else {
                    callback.onLoyaltyMessageCallback(null, error);
                }
            }

        });

    }




    public static void getLoyaltyMessageStatus(JSONObject parameter,   JSONArray jsArray, final FBLoyaltyMessageCallback callback) {




        try {

            parameter.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
            // parameter.put("activityType",parameter.get("areaType") );
            parameter.put("deletedMessage", jsArray);


        } catch (Exception e) {
        }



        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBLoyaltyMessageStatus, str, getHeaderformessagestatus(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onLoyaltyMessageCallback(response, null);

                } else {
                    callback.onLoyaltyMessageCallback(null, error);
                }
            }

        });

    }


    public static void getLoyaltyMarkMessageStatus(JSONObject parameter,   JSONArray jsArray, final FBLoyaltyMessageCallback callback) {




        try {

            parameter.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
            // parameter.put("activityType",parameter.get("areaType") );
            parameter.put("markedMessage", jsArray);


        } catch (Exception e) {
        }



        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBLoyaltyMessageStatus, str, getHeaderformessagestatus(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onLoyaltyMessageCallback(response, null);

                } else {
                    callback.onLoyaltyMessageCallback(null, error);
                }
            }

        });

    }

    public static void getLoyaltyUnMarkMessageStatus(JSONObject parameter,   JSONArray jsArray, final FBLoyaltyMessageCallback callback) {




        try {

            parameter.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
            parameter.put("unMarkedMessage", jsArray);


        } catch (Exception e) {
        }



        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBLoyaltyMessageStatus, str, getHeaderformessagestatus(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onLoyaltyMessageCallback(response, null);

                } else {
                    callback.onLoyaltyMessageCallback(null, error);
                }
            }

        });

    }



    public void getEmailOfferDetail(JSONObject offer, String Channelid,final FBLoyaltyEmailOfferDetail callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {


        {
            if (true) {

                String url = "mobile/getEmailOfferDetail" + "/" + Channelid;
                FBService.getInstance().get(url, null, getHeaderforemail(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                            if (response != null) {
                                callback.onFBLoyaltyEmailOfferDetailCallback(response, error);
                            }
                            else
                            {
                                callback.onFBLoyaltyEmailOfferDetailCallback(null, error);
                            }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }
    }



    public void getActivity(JSONObject parameter, final FBGetActivity callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }

        try {

            parameter.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
            parameter.put("activityType",parameter.get("areaType") );
            parameter.put("startIndex", "0");
            parameter.put("count", "10");

        } catch (Exception e) {
        }



        String str = parameter.toString();

        FBService.getInstance().post(FBConstant.FBGetActivityApi, str, getHeader1(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onFBGetActivity(response, null);

                } else {
                    callback.onFBGetActivity(null, error);
                }
            }

        });

    }



    public void getActivitySort(JSONArray parameter,String activityType,  final FBGetActivity callback) {


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        JSONObject obj = new JSONObject();
        try {

            obj.put("memberId", FB_LY_UserService.sharedInstance().member.customerID );
            obj.put("activityType",activityType);
            obj.put("startIndex", "0");
            obj.put("count", "10");
            obj.put("sort",parameter);


        } catch (Exception e) {
        }



        String str = obj.toString();

        FBService.getInstance().post(FBConstant.FBGetActivityApi, str, getHeader1(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                if (error == null && response != null) {

                    callback.onFBGetActivity(response, null);

                } else {
                    callback.onFBGetActivity(null, error);
                }
            }

        });

    }

    public void getCountry(JSONObject parameter,final FBCountryCallback callback){
        //  final FBSdkData FBSdkData = clpsdk.getClpSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBCountry,null, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null)
                {

                    callback.onCountryCallback(response,null);
                }
                else
                {
                    callback.onCountryCallback(null,error);
                }

            }
        });

    }

    public void getState(JSONObject parameter,final FBStateCallback callback){
        //  final FBSdkData FBSdkData = clpsdk.getClpSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBStates,null, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    //   member.initWithJson(response);

                    callback.onStateCallback(response,null);
                }else{
                    callback.onStateCallback(null,error);
                }

            }
        });

    }

    public  void getAllStore(final FBAllStoreCallback callback){

        FBService.getInstance().get(FBConstant.StoreApi, null, getHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {

                try {
                    if (error == null && response != null) {
                        if (!response.has("stores"))
                            return;

                        JSONArray getArrayStores = response.getJSONArray("stores");
                        if (getArrayStores != null) {

                            for (int i = 0; i < getArrayStores.length(); i++) {
                                JSONObject myStoresObj = getArrayStores.getJSONObject(i);
                                FBStoresItem getStoresObj = new FBStoresItem(myStoresObj);
                                if (StringUtilities.isValidString(getStoresObj.getStoreNumber())) {

                                    mapIdToStore.put(getStoresObj.getStoreID(), getStoresObj);
                                    mapNumToId.put(getStoresObj.getStoreNumber(), getStoresObj.getStoreID());
                                }
                            }
                            allStoreFromServer = new ArrayList<FBStoresItem>(mapIdToStore.values());
                            callback.OnAllStoreCallback(response, null);

                        }
                        else
                        {
                            Log.d("allstore error", "Download error");
                        }
                    }

                } catch (JSONException e) {
                    //	callback.OnAllStoreCallback(response,error.getLocalizedMessage());
                    e.printStackTrace();
                }


            }

        });

    }

    public void getTokenApi(JSONObject parameter, final FBGetTokenCallback callback){


        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBGetToken,str, getTokenHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error==null && response!=null){

                    callback.onGetTokencallback(response,null);
                }else {
                    callback.onGetTokencallback(null,error);
                }
            }


        });

    }

    public void getsendPassEmail(JSONObject parameter,final FBSendPassEmail callback){
        //  final FBSdkData FBSdkData = clpsdk.getClpSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();
        String url = "mobile/sendPassEmail" + "/" + FB_LY_UserService.sharedInstance().member.customerID ;

        FBService.getInstance().get(url,null, sendemailheader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    //   member.initWithJson(response);

                    callback.onSendPassEmaiCallback(response,null);
                }else{
                    callback.onSendPassEmaiCallback(null,error);
                }

            }
        });

    }



    public void postsendPassEmail(JSONObject parameter, final FBSendPassEmail callback) {



        String str = parameter.toString();
        String url = "mobile/sendEmailWithPassCouponUrl" + "/" + FB_LY_UserService.sharedInstance().member.customerID ;
        FBService.getInstance().post(url, str, sendemailheader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    callback.onSendPassEmaiCallback(response, null);

                } else {

                    callback.onSendPassEmaiCallback(null, error);
                }

            }

        });
    }


    public void postsendPassLoyalty(JSONObject parameter, final FBSendPassEmail callback) {



        String str = parameter.toString();
        String url = "mobile/sendPassEmail" + "/" + FB_LY_UserService.sharedInstance().member.customerID ;
        FBService.getInstance().post(url, str, sendemailheader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    callback.onSendPassEmaiCallback(response, null);

                } else {

                    callback.onSendPassEmaiCallback(null, error);
                }

            }

        });
    }


    public void getMemberLoyaltyCard(JSONObject parameter,final FBGetMemberLoyaltyCardCallback callback){
        //  final FBSdkData FBSdkData = clpsdk.getClpSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();
        String url = FBConstant.FBMemberLoyaltyCard+"?memberId="+member.customerID;
        FBService.getInstance().get(url,null, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    //   member.initWithJson(response);

                    callback.onGetMemberLoyaltyCardCallback(response,null);
                }else{
                    callback.onGetMemberLoyaltyCardCallback(null,error);
                }

            }
        });

    }



    static HashMap<String, String> getHeader1() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());

        return header;
    }
    static HashMap<String, String> getHeader1error() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobile");
        header.put("client_id", FBConstant.client_id);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());

        return header;
    }

    static HashMap<String, String> getHeaderformessagestatus() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobile");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());

        return header;
    }


    static HashMap<String, String> getHeaderforemail() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobile");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        return header;
    }


    HashMap<String, String> getHeadersignup() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));


        return header;
    }

    //Create Member preet
    HashMap<String, String> getHeader1preet() {

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }

    //Create  Device Update
    HashMap<String, String> deviceUpdateHeader() {

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }


    //Login
    HashMap<String, String> getHeader2() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("response_type", "token");
        header.put("scope", "read");
        header.put("state", "stateless");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
       // header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        return header;
    }





    //Login
    HashMap<String, String> getHeaderfortest() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("response_type", "token");
        header.put("scope", "read");
        header.put("state", "stateless");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        //  header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        return header;
    }

    HashMap<String,String> getHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        //header.put("X-XSRF-TOKEN","1234");
        //header.put("REDIS","true");
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        //header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


    //Get Member
    HashMap<String, String> getHeader3() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }

    //Get Member Update
    HashMap<String, String> updateMemberHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }


    HashMap<String, String> sendemailheader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("tenantName", "fishbowl");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }

    //
    HashMap<String, String> changePassHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }

    //Get Member Update
    HashMap<String, String> logoutHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }

    //Contact Us Headers
    HashMap<String, String> contactUsHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "kiosk");
        header.put("client_secret", FBConstant.client_secret);
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String,String> getTokenHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Accept","application/json");
        return header;
    }


    //Login

    /*HashMap<String,String> getHeaderNAT(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        return header;
    }

    HashMap<String,String> getHeaderAT(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("access_token",access_token);
        return header;
    }
*/

/*
Jamba:
ClientID: 201969E1BFD242E189FE7B6297B1B5A5
ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB8

Deltaco:
ClientID: 201969E1BFD242E189FE7B6297B1B5A6
ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB7
*/


    //Interface for
    public interface FBCreateMemberCallback {
        public void onCreateMemberCallback(JSONObject response, Exception error);
    }

    public interface FBLoginMemberCallback {
        public void onLoginMemberCallback(JSONObject response, Exception error);
    }

    public interface FBGetMemberCallback {
        public void onGetMemberCallback(JSONObject response, Exception error);
    }

    public interface FBMemberUpdateCallback {
        public void onMemberUpdateCallback(JSONObject response, Exception error);
    }

    public interface FBDeviceUpdateCallback {
        public void onDeviceUpdateCallback(JSONObject response, Exception error);
    }

    public interface FBChangePasswordCallback {
        public void onChangePasswordCallback(JSONObject response, Exception error);
    }

    public interface FBLogoutCallback {
        public void onLogoutCallback(JSONObject response, Exception error);
    }
    public interface FBContactUsCallback{
        public void onContactUsCallback(JSONObject response, Exception error);
    }
    public interface  FBLoyaltyAreaType{
        public void onLoyaltyAreaType(JSONObject response, Exception error);
    }
    public  interface FBGetActivity {
        public void onFBGetActivity(JSONObject response, Exception error);
    }

    public interface  FBLoyaltyAreaTypeCallback{
        public void onLoyaltyAreaTypeCallback(JSONObject response, Exception error);
    }

    public interface  FBLoyaltyMessageTypeCallback{
        public void onLoyaltyMessageTypeCallback(JSONObject response, Exception error);
    }


    public interface  FBLoyaltyMessageCallback{
        public void onLoyaltyMessageCallback(JSONObject response, Exception error);
    }


    public interface  FBLoyaltyEmailOfferDetail{
        public void onFBLoyaltyEmailOfferDetailCallback(JSONObject response, Exception error);
    }
    public interface FBCountryCallback{
        public void onCountryCallback(JSONObject response, Exception error);
    }
    public interface FBStateCallback{
        public void onStateCallback(JSONObject response, Exception error);
    }


    public interface FBSendPassEmail{
        public void onSendPassEmaiCallback(JSONObject response, Exception error);
    }


    //Interface for
    public interface FBAllStoreCallback {
        public void OnAllStoreCallback(JSONObject response, String error);
    }

    public interface FBGetTokenCallback{
        public void onGetTokencallback(JSONObject response,Exception error);
    }

    public interface FBGetMemberLoyaltyCardCallback{
        public void onGetMemberLoyaltyCardCallback(JSONObject response,Exception error);
    }
}
