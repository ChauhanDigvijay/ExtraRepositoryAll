package com.fishbowl.basicmodule.Services;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBCommOrderIdCallback;
import com.fishbowl.basicmodule.Interfaces.FBCommTokenCallback;
import com.fishbowl.basicmodule.Interfaces.FBOrderValueCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUpdateInCommTransactionCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijaychauhan on 23/11/16.
 */

public class FBGiftService {
    public static FBGiftService instance;
    String TAG = "FBOfferService";
    private static FBSdk fbSdk;

    public static FBGiftService sharedInstance() {
        if (instance == null) {
            instance = new FBGiftService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk) {
        fbSdk = _fbsdk;
    }

    public void getFBCommToken(final JSONObject offer, final FBCommTokenCallback callback) {
        if(fbSdk!=null) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnFBCommTokenCallback(null, FBUtility.getNetworkError());
        } else {
            String url = "mobile/incomm/token";
            String str = offer.toString();
            FBService.getInstance().post(url, str, getHeaderwithsecurity(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    if (error == null && response != null) {
                        try {
                            if (callback != null) {
                                callback.OnFBCommTokenCallback(response, error);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        callback.OnFBCommTokenCallback(null, error);
                    }

                }
            });
        }

    }}


    public void getFBOrdervalue(final JSONObject offer, final FBOrderValueCallback callback) {
        if(fbSdk!=null) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnFBOrderValueCallback(null, FBUtility.getNetworkError());
        } else {
            String url = "mobile/incomm/rewards";
            String str = offer.toString();
            FBService.getInstance().post(url, str, getHeaderwithsecurity(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                    if (error == null && response != null) {
                        try {
                            if (callback != null) {
                                callback.OnFBOrderValueCallback(response, error);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        callback.OnFBOrderValueCallback(null, error);
                    }

                }
            });
        }

    }}

    public void getFBCommOrderId(final JSONObject offer, final FBCommOrderIdCallback callback) {
        if(fbSdk!=null) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnFBCommOrderIdCallback(null, FBUtility.getNetworkError());
        } else {
            String url = "mobile/incomm/orderId";
            String str = offer.toString();
            FBService.getInstance().post(url, str, getHeaderwithsecurity(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


                    if (error == null && response != null) {
                        try {
                            if (callback != null) {
                                callback.OnFBCommOrderIdCallback(response, error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        callback.OnFBCommOrderIdCallback(null, error);
                    }

                }
            });
        }
    }}

    public void FBupdateInCommTransaction(JSONObject parameter, final FBUpdateInCommTransactionCallback callback) {
        if(fbSdk!=null) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnFBUpdateInCommTransactionCallback(null, FBUtility.getNetworkError());
        } else {
            String str = parameter.toString();
            String url = "mobile/incomm/orderId";

            FBService.getInstance().put(url, str, getHeaderwithsecurity(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    if (error == null && response != null) {
                        callback.OnFBUpdateInCommTransactionCallback(response, null);
                    } else {
                        callback.OnFBUpdateInCommTransactionCallback(null, error);
                    }
                }
            });
        }

    }}


    public void getGiftAndroidSavePayJWT(JSONObject parameter, final FBGetSaveGiftJWTokenCallback callback) {
        if (fbSdk != null) {

            if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
                callback.onFBGetSaveGiftJWTokenCallback(null, FBUtility.getNetworkError());
            } else {
                String str = parameter.toString();
                String url = "mobile/getGiftAndroidPayJWT?memberId=" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId();
                FBService.getInstance().post(url, str, getGiftAndroidPayHeader(), new FBServiceCallback() {

                    @Override
                    public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                        if (error == null && response != null) {

                            callback.onFBGetSaveGiftJWTokenCallback(response, null);
                        } else {
                            callback.onFBGetSaveGiftJWTokenCallback(null, error);
                        }
                    }


                });
            }
        }
    }

    public void updateGiftAndroidSavePay(JSONObject parameter, final FBGetSaveGiftJWTokenCallback callback) {
        if(fbSdk!=null) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onFBGetSaveGiftJWTokenCallback(null, FBUtility.getNetworkError());
        } else {
            String str = parameter.toString();
            String url = "mobile/updateGiftAndroidPay?memberId=" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId();
            FBService.getInstance().post(url, str, getGiftAndroidPayHeader(), new FBServiceCallback() {

                @Override
                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    if (error == null && response != null) {

                        callback.onFBGetSaveGiftJWTokenCallback(response, null);
                    } else {
                        callback.onFBGetSaveGiftJWTokenCallback(null, error);
                    }
                }


            });
        }
    }}

    public interface FBGetSaveGiftJWTokenCallback {
        public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error);
    }

    HashMap<String, String> getHeaderwithsecurity() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("Application", FBConstant.client_Application);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));

        return header;
    }




    HashMap<String, String> getGiftAndroidPayHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

}
