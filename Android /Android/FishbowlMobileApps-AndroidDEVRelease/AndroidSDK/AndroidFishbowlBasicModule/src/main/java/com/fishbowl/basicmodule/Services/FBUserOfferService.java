package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBServicePassCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.basicmodule.Utils.FBConstant.client_id;

/**
 * Created by digvijay(dj)
 */

/**
 * Created by digvijay(dj)
 */
public class FBUserOfferService {
    private FBSdk fbSdk;
    String TAG = "FBOfferService";
    public static FBUserOfferService instance;

    public static FBUserOfferService sharedInstance() {
        if (instance == null) {
            instance = new FBUserOfferService();
        }
        return instance;
    }

    public void init(FBSdk _clpsdk) {
        fbSdk = _clpsdk;
    }

    public void getUserFBOffer(JSONObject offer, String customId, final FBOfferCallback callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId())) {

            String url = "mobile/getoffers" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() + "/" + "0";
            FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    try {


                        if (response != null) {
                            callback.OnFBOfferCallback(response, error);
                        } else {
                            callback.OnFBOfferCallback(null, error);
                        }

                    } catch (Exception e) {

                    }

                }
            });
        }

    }

    public void getUserFBReward(JSONObject offer, String customId, final FBRewardCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId())) {
            String url = "mobile/getrewards" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() + "/" + "0";
            FBService.getInstance().get(url, null, getHeaderforhtml(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    try {


                        if (response != null) {
                            callback.OnFBRewardCallback(response, error);
                        } else {
                            callback.OnFBRewardCallback(null, error);
                        }

                    } catch (Exception e) {

                    }

                }
            });

        }
    }


    public void getFBOfferbyofferid(JSONObject offer, String offerId, final FBOfferPushCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId()))
        {

            String url = "mobile/getOfferByOfferId" + "/" + offerId;
            FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                    try {


                        if (response != null) {
                            callback.OnFBOfferPushCallback(response, error);
                        }
                        else
                        {
                            callback.OnFBOfferPushCallback(null, error);
                        }


                    } catch (Exception e) {

                    }

                }
            });
        }

    }

    public void getUserFBRewardPoint(JSONObject offer, String customId, final FBRewardPointCallback callback) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId())) {
            String url = "mobile/getPointRewardInfo" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId();
            FBService.getInstance().get(url, null, getHeaderrewardsforpoint(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    try {


                        if (response != null) {
                            callback.OnFBRewardPointCallback(response, error);
                        } else {
                            callback.OnFBRewardPointCallback(null, error);
                        }

                    } catch (Exception e) {

                    }

                }
            });
        }
    }

    //redeemedservices
    public void getredeemedservices(JSONObject offer, String itemId, final FBRedeemedSummary callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        FBSdkData FBSdkData = fbSdk.getFBSdkData();

        try {


            if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId())) {
                try {
                    offer.put("tenantid", FBSdkData.currCustomer.getCompanyId());
                    offer.put("memberid", FBSdkData.currCustomer.getCustomerID());
                    offer.put("lat", String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
                    offer.put("lon", String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
                    offer.put("device_type", FBConstant.DEVICE_TYPE);
                    offer.put("device_os_ver", FBUtility.getAndroidOs());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String json = offer.toString();

                String url = "mobile/redeemed" + "/" + FBUserService.sharedInstance().member.customerID + "/" + itemId;

                FBService.getInstance().post(url, json, null, new FBServiceCallback() {

                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {

                        if (response != null) {
                            callback.OnFBRedeemedSummary(response, error);
                        }
                        else
                        {
                            callback.OnFBRedeemedSummary(null, error);
                        }

                    }

                });
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }



    public void getClypOfferPass(JSONObject offerpromo, String offerId, boolean isPMIntegrated, final FBOfferPassCallback callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String entity = null;

        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId()))
        {


            try {
                JSONObject object = new JSONObject();
                object.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());
                object.put("campaignId", offerId);
                object.put("deviceName", "ANDROID");
                entity = object.toString();
            } catch (Exception e) {
            }

            String url = "mobile/getPass/";

            FBService.getInstance().makeCustomPassRequest(url, entity, getHeaderforpass(), new FBServicePassCallback() {
                public void onServicePassCallback(byte[] response, Exception error,String errormeassage) {
                    try {

                        if(response !=null) {
                            callback.OnFBOfferPassCallback(response, error);
                        }
                        else
                        {
                            callback.OnFBOfferPassCallback(null, error);
                        }
                    } catch (Exception e) {
                        callback.OnFBOfferPassCallback(null, null);
                    }

                }
            });
        }

    }

    public void getFBOfferPromo(JSONObject offerpromo, String itemId, Boolean isPMIntegrated, final FBOfferPromoCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId()))
        {

            String url = "mobile/getPromo" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() + "/" + itemId + "/" + isPMIntegrated;

            FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                    try {

                        if (callback != null) {
                            callback.OnFBOfferPromoCallback(response, error);
                        }

                    } catch (Exception e) {

                    }

                }
            });

        }

    }


    public void sendOfferEvent(JSONObject offerEvent) {
        FBSdkData FBSdkData = fbSdk.getFBSdkData();

        if (FBSdkData.currCustomer == null
                || FBSdkData.currCustomer.memberid <= 0
                || !FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        if (fbSdk.mCurrentLocation == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            fbSdk.mCurrentLocation = curLoc;
        }
        String entity;
        try {
            offerEvent.put("action", "CLPEvent");
            offerEvent.put("tenantid",
                    FBSdkData.currCustomer.getCompanyId());
            offerEvent.put("memberid",
                    FBSdkData.currCustomer.getCustomerID());
            offerEvent.put("lat",
                    String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
            offerEvent.put("lon",
                    String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
            offerEvent.put("device_type", FBConstant.DEVICE_TYPE);
            offerEvent.put("device_os_ver", FBUtility.getAndroidOs());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        entity = offerEvent.toString();


        String url = "mobile/submitclpevents";

        FBService.getInstance().post(url, entity, null, new FBServiceCallback() {
            public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {

                Log.d("sendOfferEvent", "SUCC + " + response + "");

            }
        });
    }

    public void getUserFBPointBankOffer(JSONObject offer, String customId, final FBPointBankOffer callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        if (StringUtilities.isValidString(FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId())) {

            String url = "mobile/getPointBankOffer" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId();
            FBService.getInstance().get(url, null, getHeaderforpoint(), new FBServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    try {


                        if (response != null) {
                            callback.OnFBPointBankOfferCallback(response, error);
                        } else {
                            callback.OnFBPointBankOfferCallback(null, error);
                        }

                    } catch (Exception e) {

                    }

                }
            });
        }

    }




    public void useOffer(JSONObject parameter,final FBUseOfferCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBUseOffer,str, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){

                    callback.onUseOfferCallback(response,null);
                }else {
                    callback.onUseOfferCallback(null,error);
                }
            }
        });

    }

    //Interface for
    public interface FBOfferCallback {
        public void OnFBOfferCallback(JSONObject response, Exception error);
    }

    public interface FBOfferPassCallback {
        public void OnFBOfferPassCallback(byte[] response, Exception error);
    }

    public interface FBOfferPromoCallback {
        public void OnFBOfferPromoCallback(JSONObject response, Exception error);
    }
    //Interface for
    public interface FBRewardPointCallback {
        public void OnFBRewardPointCallback(JSONObject response, Exception error);
    }

    public interface FBOfferPushCallback {
        public void OnFBOfferPushCallback(JSONObject response, Exception error);
    }

    public interface FBOfferEventCallback {
        public void OnFBOfferEventCallback(JSONObject response, Exception error);
    }

    public interface FBRedeemedSummary {
        public void OnFBRedeemedSummary(JSONObject response, Exception error);
    }


    //Interface for
    public interface FBRewardCallback {
        public void OnFBRewardCallback(JSONObject response, Exception error);
    }

    public interface FBPointBankOffer {
        public void OnFBPointBankOfferCallback(JSONObject response, Exception error);
    }

    public interface FBUseOfferCallback{
        public void onUseOfferCallback(JSONObject response, Exception error);
    }
    HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", client_id);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderforpoint() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", client_id);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderforhtml() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", client_id);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderforpass() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("tenantName", "fishbowl");


        return header;
    }
    HashMap<String, String> getHeaderrewardsforpoint() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    //Get Member Update
    HashMap<String,String> updateMemberHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


}