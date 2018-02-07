package com.BasicApp.Analytic;

import android.location.Location;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.fishbowl.basicmodule.Services.FBSessionService.fbSdk;

/**
 * Created by digvijay(dj)
 */
public class FBAnalyticsManager {

    static FBAnalyticsManager instance;
    public FBSdk _app;

    FBMember member;
    public FBAnalyticsManager() {
        this._app = FBSdk.instance;
        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(_app.context).mSharedPreferences.getString("FBUser", "");
        member = gson.fromJson(json, FBMember.class);


        if (_app != null) {
            FBAnalytic.sharedInstance().init(_app.context, _app.SERVER_URL, "91225258ddb5c8503dce33719c5deda7");
        } else {
          //   FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.), "91225258ddb5c8503dce33719c5deda7");

        }
    }

    public static FBAnalyticsManager sharedInstance() {
        if (instance == null) {
            instance = new FBAnalyticsManager();
        }
        return instance;
    }

    public void addCommonData(JSONObject data) {

        try {
            data.put("action", "AppEvent");
         //   FBCustomerItem customer = _app.getFBSdkData().currCustomer;
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("memberid",FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());
            data.put("lat", String.valueOf(_app.getmCurrentLocation().getLatitude()));
            data.put("lon", String.valueOf(_app.getmCurrentLocation().getLongitude()));
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("device_os_ver", _app.getAndroidOs());

        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }



    public void addCommonDataGuest(JSONObject data) {

        try {
            data.put("action", "AppEvent");
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("lat", String.valueOf(_app.getmCurrentLocation().getLatitude()));
            data.put("lon", String.valueOf(_app.getmCurrentLocation().getLongitude()));
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("device_os_ver", _app.getAndroidOs());

        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }

    public void recordErrorEvent() {
        if (_app.checkAppEventFlag()) {
            JSONObject data = new JSONObject();

            try {
                data.put("event_name", "AppError");
                addCommonData(data);
                FBAnalytic.sharedInstance().recordEvent(data);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void track_ItemWith(String id, String description, String eventName) {
        if (!checkValidCall())
            return;

        try {
            JSONObject data = new JSONObject();
            if (eventName != null && id != null && description != null) {
                data.put("item_id", id);
                data.put("item_name", description);
                data.put("event_name", eventName);
                addCommonData(data);
                if (_app.checkAppEventFlag())
                    FBAnalytic.sharedInstance().recordEvent(data);

            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }

    public void track_ItemWithGuest(String id, String description, String eventName) {
        if (!checkValidCall())
            return;

        try {
            JSONObject data = new JSONObject();
            if (eventName != null && id != null && description != null) {
                data.put("item_id", id);
                data.put("item_name", description);
                data.put("event_name", eventName);
                addCommonDataGuest(data);
                if (_app.checkAppEventFlag())
                    FBAnalytic.sharedInstance().recordEvent(data);

            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }



    public void track_OfferItemWith(String offerId, String clpId, String id, String description, String eventName) {
        if (!checkValidCall())
            return;

        try {
            JSONObject data = new JSONObject();
            if (eventName != null && offerId != null && clpId != null && id != null && description != null) {
                data.put("item_id", id);
                data.put("item_name", description);
                data.put("event_name", eventName);
                data.put("notifid", clpId);
                data.put("offerid", offerId);

            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }

    public void track_OfferItemWith(String id, String description, String eventName) {
        if (!checkValidCall())
            return;

        try {
            JSONObject data = new JSONObject();
            if (eventName != null && id != null && description != null) {
                data.put("item_id", id);
                data.put("item_name", description);
                data.put("event_name", eventName);

            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }

    public boolean checkValidCall() {
        if (_app == null)
            return false;


        if (member == null)
            return false;

        if (FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId() == null)
            return false;

        if (!FBUtility.isNetworkAvailable(_app.context)) {
            Log.d("fbSdk", "getCurrCustomer is null");
            return false;
        }
        if (_app.getmCurrentLocation() == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            _app.setmCurrentLocation(curLoc);
        }
        return true;
    }


    public boolean checkValidCallGuest() {
        if (_app == null)
            return false;


        if (!FBUtility.isNetworkAvailable(_app.context)) {
            Log.d("fbSdk", "getCurrCustomer is null");
            return false;
        }
        if (_app.getmCurrentLocation() == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            _app.setmCurrentLocation(curLoc);
        }
        return true;
    }


    public void track_EventbyName(String eventName) {
        if (!checkValidCall())
            return;
        try {
            JSONObject data = new JSONObject();
            data.put("event_name", eventName);
            addCommonData(data);
            if (_app.checkAppEventFlag())
                FBAnalytic.sharedInstance().recordEvent(data);

        } catch (Exception e) {
            recordErrorEvent();
        }
    }

    public void track_EvenforGuesttbyName(String eventName) {
        if (!checkValidCallGuest())
            return;
        try {
            JSONObject data = new JSONObject();
            data.put("event_name", eventName);
            addCommonDataGuest(data);
            if (_app.checkAppEventFlag())
                FBAnalytic.sharedInstance().recordEvent(data);

        } catch (Exception e) {
            recordErrorEvent();
        }
    }


    public void trackClypEvent(String category, String action, String label) {

    }

    public void trackClypEvent(String category, String action) {

    }

    public void trackClypScreen(String screenName) {

    }


}
