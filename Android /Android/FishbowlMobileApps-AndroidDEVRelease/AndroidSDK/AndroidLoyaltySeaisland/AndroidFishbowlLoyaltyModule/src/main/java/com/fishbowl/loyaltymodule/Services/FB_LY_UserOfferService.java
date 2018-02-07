package com.fishbowl.loyaltymodule.Services;

import com.fishbowl.loyaltymodule.Controllers.FBSdk;
import com.fishbowl.loyaltymodule.Interfaces.FBServiceArrayCallback;
import com.fishbowl.loyaltymodule.Interfaces.FBServiceCallback;
import com.fishbowl.loyaltymodule.Interfaces.FBServicePassCallback;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.FBUtility;
import com.fishbowl.loyaltymodule.Utils.FBPreferences;
import com.fishbowl.loyaltymodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.loyaltymodule.Services.FB_LY_UserService.getHeader1;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */

public class FB_LY_UserOfferService {
    private FBSdk clpsdk;
    String TAG = "ClpOfferManager";
    public static FB_LY_UserOfferService instance;

    public static FB_LY_UserOfferService sharedInstance() {
        if (instance == null) {
            instance = new FB_LY_UserOfferService();
        }
        return instance;
    }

    public void init(FBSdk _clpsdk) {
        clpsdk = _clpsdk;
    }

    public void getUserClypOffer(JSONObject offer, String customId, final ClypOfferCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {


        {
            if (true) {

                String url = "mobile/getoffers" + "/" + FB_LY_UserService.sharedInstance().member.customerID+ "/" + "0";
                FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                                if (response != null) {
                                    callback.onClypOfferCallback(response, error);
                                }
                            else
                                {
                                    callback.onClypOfferCallback(null, error);
                                }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }
    }


    public void getClypOfferbyofferid(JSONObject offer, String offerId, final ClypOfferPushCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {





        String entity;


        if (StringUtilities.isValidString(FB_LY_UserService.sharedInstance().member.customerID)) {


            String url = "mobile/getOfferByOfferId" + "/" + offerId;
            FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                    try {


                            if (response != null) {
                                callback.onClypOfferPushCallback(response, error);
                            }
                        else
                            {
                                callback.onClypOfferPushCallback(null, error);
                            }


                    } catch (Exception e) {

                    }

                }
            });
        }


    }





//    public void getClypOfferPass(JSONObject offerpromo,String offerId,boolean isPMIntegrated, final ClypOfferPassCallback callback) {
//        CLPSdkData   clpSdkData=this.clpsdk.getClpSdkData();
//
//
//        String entity;
//
//        if (clpSdkData.currCustomer != null) {
//            if (StringUtilities.isValidString(FB_LY_UserService.sharedInstance().member.customerID)) {
//
//                entity = offerpromo.toString();
//
//                String url=  "mobile/getPass" +"/"+FB_LY_UserService.sharedInstance().member.customerID+"/" + offerId;
//
//
//                FBService.getInstance().makeCustomPassRequest(url, entity,getHeader(), new FBServicePassCallback(){
//                    public void onServicePassCallback(byte[] response, Exception error){
//                        try {
//
//                            if (callback != null) {
//                                callback.onClypOfferPassCallback(response,null);
//                            }
//
//                        }catch(Exception e){
//                            callback.onClypOfferPassCallback(null,e.getLocalizedMessage());
//                        }
//
//                    }
//                });
//            }
//        }
//
//    }


    public void getClypOfferPass(JSONObject offerpromo, String offerId, boolean isPMIntegrated, final ClypOfferPassCallback callback) {



        String entity = null;

        // if (clpSdkData.currCustomer != null) {
        if (StringUtilities.isValidString(FB_LY_UserService.sharedInstance().member.customerID)) {


            try {
                JSONObject object = new JSONObject();
                object.put("memberid", FB_LY_UserService.sharedInstance().member.customerID);
                object.put("campaignId", offerId);
                object.put("deviceName", "ANDROID");
                entity = object.toString();
            } catch (Exception e) {
            }


            //      String url=  "mobile/getPass" +"/"+FB_LY_UserService.sharedInstance().member.customerID+"/" + offerId;
            String url = "mobile/getPass/";

            FBService.getInstance().makeCustomPassRequest(url, entity, getHeaderforpass(), new FBServicePassCallback() {
                public void onServicePassCallback(byte[] response, Exception error,String errormeassage) {
                    try {

                        if(response !=null) {
                            callback.onClypOfferPassCallback(response, error);
                        }
                        else
                        {
                            callback.onClypOfferPassCallback(null, error);
                        }
                    } catch (Exception e) {
                        callback.onClypOfferPassCallback(null, null);
                    }

                }
            });
        }
        //  }

    }

    public void getClypOfferPromo(JSONObject offerpromo, String itemId, Boolean isPMIntegrated, final ClypOfferPromoCallback callback) {



        String entity;

        if (StringUtilities.isValidString(FB_LY_UserService.sharedInstance().member.customerID)) {


            String url = "mobile/getPromo" + "/" + FB_LY_UserService.sharedInstance().member.customerID + "/" + itemId + "/" + isPMIntegrated;

            FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                    try {

                            if (callback != null) {
                                callback.onClypOfferPromoCallback(response, error);
                            }

                    } catch (Exception e) {

                    }

                }
            });

        }

    }



    //Interface for
    public interface ClypOfferCallback {
        public void onClypOfferCallback(JSONObject response, Exception error);
    }

    public interface ClypOfferPassCallback {
        public void onClypOfferPassCallback(byte[] response, Exception error);
    }

    public interface ClypOfferPromoCallback {
        public void onClypOfferPromoCallback(JSONObject response, Exception error);
    }

    public interface ClypOfferPushCallback {
        public void onClypOfferPushCallback(JSONObject response, Exception error);
    }



    //Interface for
    public interface ClypRewardCallback {
        public void onClypRewardCallback(JSONObject response, Exception error);
    }
    //Interface for
    public interface ClypRewardPointCallback {
        public void onClypRewardPointCallback(JSONObject response, Exception error);
    }
    public interface ClypSmsCallback {
        public void onClypSmsCallback(JSONObject response, Exception error);
    }
    public interface ClypOfferEventCallback {
        public void onClypOfferEventCallback(JSONObject response, Exception error);
    }

    public interface ClypRedeemedSummary {
        public void onClypRedeemedSummary(JSONObject response, Exception error);
    }



    HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));

        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");

        // header.put("tenantName", "fishbowl");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderforhtml() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


    HashMap<String, String> getHeaderrewards() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        //header.put("scope","READ");
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));

        // header.put("tenantName", "fishbowl");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderrewardsforpoint() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        //header.put("scope","READ");
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));

        // header.put("tenantName", "fishbowl");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }




    HashMap<String, String> getHeaderforsmssend() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("client_id", FBConstant.client_id);
       // header.put("client_secret",FBConstant.client_secret);
        header.put("Application", "ipad");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        return header;
    }



    public void getUserClypReward(JSONObject offer, String customId, final ClypRewardCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {


        {
            if (true) {

                String url = "mobile/getrewards" + "/" + FB_LY_UserService.sharedInstance().member.customerID + "/" + "0";
                FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                            if (response != null) {
                                callback.onClypRewardCallback(response, error);
                            }
                            else
                            {
                                callback.onClypRewardCallback(null, error);
                            }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }
    }


    public void getUserClypRewardPoint(JSONObject offer, String customId, final ClypRewardPointCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {


        {
            if (true) {

                String url = "mobile/getPointRewardInfo" + "/" + FB_LY_UserService.sharedInstance().member.customerID;
                FBService.getInstance().get(url, null, getHeaderrewardsforpoint(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                            if (response != null) {
                                callback.onClypRewardPointCallback(response, error);
                            }
                            else
                            {
                                callback.onClypRewardPointCallback(null, error);
                            }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }

    }



    public void sendOfferviaSms(JSONObject offer, String offerId, final ClypSmsCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {


        {
            if (true) {

                String url = "mobile/sendOfferviaSms"+ "/" + FB_LY_UserService.sharedInstance().member.customerID + "/" + offerId+"/"+"/" + FB_LY_UserService.sharedInstance().member.cellPhone;
                FBService.getInstance().get(url, null, getHeaderforsmssend(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                            if (response != null) {
                                callback.onClypSmsCallback(response, error);
                            }
                            else
                            {
                                callback.onClypSmsCallback(null, error);
                            }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }
    }





    public void getBonusRuleList(JSONArray parameter, final FBBonusRuleListCallback callback){
        String str  = parameter.toString();
        FBService.getInstance().makeCustomArrayRequestforbonus(FBConstant.FBBonusApi,null,getHeader1(), new FBServiceArrayCallback() {
            @Override
            public void onCLPServiceArrayCallback(JSONArray response, Exception error, String errorMessage) {
                if (error==null && response!=null){
                    callback.onBonusRuleListCallback(response,null);
                }
                else{
                    callback.onBonusRuleListCallback(null,error);
                }
            }
        });
    }

    public interface  FBBonusRuleListCallback{
        public void onBonusRuleListCallback(JSONArray response, Exception error);
    }

    //  https://qa-jamba.fishbowlcloud.com/clpapi/loyalty/getActivity


    HashMap<String, String> getHeaderforpass() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "ipad");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");
        header.put("deviceId", FBUtility.getAndroidDeviceID(clpsdk.context));
        // header.put("tenantName", "fishbowl");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("tenantName", "fishbowl");


        return header;
    }
}