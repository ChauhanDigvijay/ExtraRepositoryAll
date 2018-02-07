package com.fishbowl.basicmodule.Services;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceDetailCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBRestaurantDetailItem;
import com.fishbowl.basicmodule.Models.FBRestaurantItem;
import com.fishbowl.basicmodule.Models.FBRestaurantListDetailItem;
import com.fishbowl.basicmodule.Models.FBRestaurantListItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.basicmodule.Services.FBSessionService.fbSdk;


/**
 * Created by digvijay(dj)  on 02/03/16.
 */
public class FBRestaurantService {

    public static void getAllRestaurants(final FBRestaurantServiceCallback callback) {
        String path = "mobile/stores/getstores";

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponsestore(response, error, callback);
            }
        });
    }


    public static void getAllRestaurantsNear(String storeId, String radius, String limit, final FBRestaurantServiceCallback callback) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("query", storeId);
        parameters.put("radius", radius);
        parameters.put("count", limit);


        FBMainService.getInstance().post(FBConstant.StoreSearchApi, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponsestoresearch(response, error, callback);
            }
        });
    }


    public static void getRestaurantById(String storeId, final FBRestaurantServiceDetailCallback callback) {
        String path = "/mobile/stores/getStoreDetails/" + storeId;
        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {

                {
                    parseResponsestoredetail(response, error, callback);
                }
            }
        });
    }


    public static void favourteStoreUpdate(String storeId, final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = FBConstant.FBMStoreUpdateApi;
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                parameters.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());
                parameters.put("storeCode", storeId);


            } catch (Exception e) {
                e.printStackTrace();
            }

            FBMainService.getInstance().put(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {

                    try {
                        String message = response.getString("message");
                        Boolean successFlag = response.getBoolean("successFlag");


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }
            });
        }
    }


    private static void parseResponsestore(JSONObject response, Exception error, FBRestaurantServiceCallback callback) {
        FBRestaurantListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("stores");
                FBRestaurantItem[] categories = gson.fromJson(jsonCategories.toString(), FBRestaurantItem[].class);
                restaurants = new FBRestaurantListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onRestaurantServiceCallback(restaurants, error);
        }
    }

    private static void parseResponsestoresearch(JSONObject response, Exception error, FBRestaurantServiceCallback callback) {
        FBRestaurantListItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONArray jsonCategories = response.getJSONArray("storeList");
                FBRestaurantItem[] categories = gson.fromJson(jsonCategories.toString(), FBRestaurantItem[].class);
                restaurants = new FBRestaurantListItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onRestaurantServiceCallback(restaurants, error);
        }
    }


    private static void parseResponsestoredetail(JSONObject response, Exception error, FBRestaurantServiceDetailCallback callback) {
        FBRestaurantListDetailItem restaurants = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");
                JSONObject responseobj=response.getJSONObject("mobileStores");
                JSONArray jsonCategories = responseobj.getJSONArray("storeHourList");
                FBRestaurantDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBRestaurantDetailItem[].class);
                restaurants = new FBRestaurantListDetailItem(message, successFlag, categories);
            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onRestaurantServiceDetailCallback(restaurants, error);
        }
    }

}
