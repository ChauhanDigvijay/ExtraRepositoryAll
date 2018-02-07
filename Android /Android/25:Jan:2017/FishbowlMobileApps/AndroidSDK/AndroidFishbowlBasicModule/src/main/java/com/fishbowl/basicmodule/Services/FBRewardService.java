package com.fishbowl.basicmodule.Services;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Interfaces.FBBonusRuleListCallback;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Interfaces.FBOfferRewardCallback;
import com.fishbowl.basicmodule.Interfaces.FBRewardCallback;
import com.fishbowl.basicmodule.Models.FBBonusDetailItem;
import com.fishbowl.basicmodule.Models.FBBonusItem;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Models.FBOfferRewardDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferRewardItem;
import com.fishbowl.basicmodule.Models.FBRewardDetailItem;
import com.fishbowl.basicmodule.Models.FBRewardListItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.basicmodule.Services.FBSessionService.fbSdk;


/**
 * Created by digvijay(dj)  on 02/03/16.
 */
public class FBRewardService {



    public static void getUserFBOffer(final FBOfferCallback callback) {
        String path = "mobile/getoffers" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() + "/" + "0";

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponsestore(response, error, callback);
            }
        });
    }



    public static void getUserFBReward(final FBOfferCallback callback) {
        String path = "mobile/getrewards" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() + "/" + "0";

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponsestore(response, error, callback);
            }
        });
    }





    public static void getBasicOfferRewardInfo(final FBOfferRewardCallback callback) {
        String path = "mobile/getBasicOfferRewardInfo" + "/" + "2477390372" ;

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponseofferreward(response, error, callback);
            }
        });
    }

    public static void getUserFBPointBankOffer(final FBRewardCallback callback) {
        String path = "mobile/getPointBankOffer" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId();

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }



    public static void useOffer(String offerId, String claimPoints, final FBRewardCallback callback) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("memberId", FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());
        parameters.put("offerId", offerId);
        parameters.put("tenantId", FBConstant.client_tenantid);
        parameters.put("claimPoints", claimPoints);

        FBMainService.getInstance().get(FBConstant.FBUseOffer, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }



    public static void getBonusRuleList(final FBBonusRuleListCallback callback) {
        HashMap<String, Object> parameters = new HashMap<>();
        FBMainService.getInstance().get(FBConstant.FBBonusApi, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponsestoresearch(response, error, callback);
            }
        });
    }


    private static void parseResponse(JSONObject response, Exception error, FBRewardCallback callback) {
        FBRewardListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("inAppOfferList");
                FBRewardDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBRewardDetailItem[].class);
                restaurants = new FBRewardListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBOfferCallback(restaurants, error);
        }
    }

    private static void parseResponsestore(JSONObject response, Exception error, FBOfferCallback callback) {
        FBOfferListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("inAppOfferList");
                FBOfferDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBOfferDetailItem[].class);
                restaurants = new FBOfferListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBOfferCallback(restaurants, error);
        }
    }

    private static void parseResponseofferreward(JSONObject response, Exception error, FBOfferRewardCallback callback) {
        FBOfferRewardItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                int numberOffers = response.getInt("numberOffers");
                int numberReward = response.getInt("numberReward");
                JSONArray jsonCategories = response.getJSONArray("offerList");
                FBOfferRewardDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBOfferRewardDetailItem[].class);
                restaurants = new FBOfferRewardItem(message, successFlag, categories,numberOffers,numberReward);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBOfferRewardCallback(restaurants, error);
        }
    }


    private static void parseResponsestoresearch(JSONObject response, Exception error, FBBonusRuleListCallback callback) {
        FBBonusItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("inBonusList");
                FBBonusDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBBonusDetailItem[].class);
                restaurants = new FBBonusItem(message, successFlag, categories);


            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onBonusRuleListCallback(restaurants, error);
        }
    }



}
