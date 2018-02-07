package com.fishbowl.basicmodule.Services;


import android.content.SharedPreferences;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBSettingCallback;
import com.fishbowl.basicmodule.Models.FBDigitalEventItem;
import com.fishbowl.basicmodule.Models.FBDigitalEventListItem;
import com.fishbowl.basicmodule.Models.FBMobileSettingItem;
import com.fishbowl.basicmodule.Models.FBMobileSettingListItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by digvijay(dj)
 */
public class FBSettingService {

    public static FBSettingService instance;
    public static FBSdk fbSdk;


    public static FBSettingService sharedInstance() {
        if (instance == null) {
            instance = new FBSettingService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk) {
        fbSdk = _fbsdk;
    }



    public static void getMobileSetting(final FBSettingCallback callback) {
        String path = "mobile/settings/getmobilesettings";

        FBMainService.getInstance().get(path, null, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }


    private static void parseResponse(JSONObject response, Exception error, FBSettingCallback callback) {
        FBMobileSettingListItem mobilesetting = null;

        FBDigitalEventListItem digitalevent = null;
        if (response != null) {
            Gson gson = new Gson();
            try {
                String message = response.getString("message");
                Boolean successFlag = response.getBoolean("successFlag");

                if (response.has("mobSettingList")) {
                    JSONObject mobSettingList = response.getJSONObject("mobSettingList");
                    try {
                        String mobSettingmessage = mobSettingList.getString("message");
                        Boolean smobSettingsuccessFlag = mobSettingList.getBoolean("successFlag");
                        JSONArray mobileSettings = mobSettingList.getJSONArray("mobileSettings");

                        FBMobileSettingItem[] categories = gson.fromJson(mobileSettings.toString(), FBMobileSettingItem[].class);
                        mobilesetting = new FBMobileSettingListItem(mobSettingmessage, smobSettingsuccessFlag, categories);
                        String json = gson.toJson(mobilesetting);
                        SharedPreferences.Editor prefsEditor = FBPreferences.sharedInstance(fbSdk.context).mSharedPreferences.edit();
                        prefsEditor.putString("FBMobileSetting", json);
                        prefsEditor.commit();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }

                if (response.has("digitalEventList")) {
                    JSONObject digitalEventList = response.getJSONObject("digitalEventList");
                    try {
                        String mobSettingmessage = digitalEventList.getString("message");
                        Boolean smobSettingsuccessFlag = digitalEventList.getBoolean("successFlag");
                        JSONArray digitalEventListarray = digitalEventList.getJSONArray("digitalEventList");
                        FBDigitalEventItem[] digitevent = gson.fromJson(digitalEventListarray.toString(), FBDigitalEventItem[].class);
                        digitalevent = new FBDigitalEventListItem(mobSettingmessage, smobSettingsuccessFlag, digitevent);
                        String json = gson.toJson(digitalevent);
                        SharedPreferences.Editor prefsEditor = FBPreferences.sharedInstance(fbSdk.context).mSharedPreferences.edit();
                        prefsEditor.putString("FBDigitalEvent", json);
                        prefsEditor.commit();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onFBSettingCallback(mobilesetting,digitalevent, error);
        }
    }


}
