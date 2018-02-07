package com.fishbowl.basicmodule.Services;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBLoyaltyAreaTypeCallback;
import com.fishbowl.basicmodule.Interfaces.FBLoyaltyMessageTypeCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBLoyaltyAreaTypeItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyAreaTypeListItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyMessageTypeItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyMessageTypeListItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
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
public class FBLoyaltyService {



    public static void getLoyaltyAreaType(final FBLoyaltyAreaTypeCallback callback) {


        FBMainService.getInstance().get(FBConstant.FBLoyaltyAreaType, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponseAreaType(response, error, callback);
            }
        });
    }



    public static void getLoyaltyMessageType(final FBLoyaltyMessageTypeCallback callback) {


        FBMainService.getInstance().get(FBConstant.FBLoyaltyMessageType, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponseMessagetype(response, error, callback);
            }
        });
    }


    public static void contactUs( String subject, String description, String messageType ,String areaType,final FBSessionServiceCallback callback) {


            String path = FBConstant.FBContactUsApi;
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                parameters.put("subject", subject);
                parameters.put("description", description);
                parameters.put("messageType", messageType);
                parameters.put("areaType", areaType);
                parameters.put("memberId", FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());

            } catch (Exception e) {
                e.printStackTrace();
            }

            FBMainService.getInstance().post(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {

                    parseResponsestore(response, error, callback);


                }
            });
        }




    private static void parseResponseAreaType(JSONObject response, Exception error, FBLoyaltyAreaTypeCallback callback) {
        FBLoyaltyAreaTypeListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("loyaltyAreaType");
                FBLoyaltyAreaTypeItem[] categories = gson.fromJson(jsonCategories.toString(), FBLoyaltyAreaTypeItem[].class);
                restaurants = new FBLoyaltyAreaTypeListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBLoyaltyTypeCallback(restaurants, error);
        }
    }
    private static void parseResponseMessagetype(JSONObject response, Exception error, FBLoyaltyMessageTypeCallback callback) {
        FBLoyaltyMessageTypeListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("loyaltyMessageType");
                FBLoyaltyMessageTypeItem[] categories = gson.fromJson(jsonCategories.toString(), FBLoyaltyMessageTypeItem[].class);
                restaurants = new FBLoyaltyMessageTypeListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBLoyaltyMessageTypeCallback(restaurants, error);
        }
    }

    private static void parseResponsestore(JSONObject response, Exception error, FBSessionServiceCallback callback) {
        FBSessionItem user = null;
        if (response != null) {


                Gson gson = new Gson();
                user = gson.fromJson(response.toString(), FBSessionItem.class);

        }
        if (callback != null) {
            callback.onSessionServiceCallback(user, error);
        }
    }


}
