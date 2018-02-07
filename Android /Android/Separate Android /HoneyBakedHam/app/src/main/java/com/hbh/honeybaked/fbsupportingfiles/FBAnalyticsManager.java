package com.hbh.honeybaked.fbsupportingfiles;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.facebook.internal.NativeProtocol;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;

import bolts.MeasurementEvent;

public class FBAnalyticsManager {
    static FBAnalyticsManager instance;
    public FBSdk _app = FBSdk.instance;
    Member member = FBUserService.sharedInstance().member;

    public FBAnalyticsManager() {
        if (this._app != null) {
            FBAnalytic sharedInstance = FBAnalytic.sharedInstance();
            Context context = this._app.context;
            FBSdk fBSdk = this._app;
            sharedInstance.init(context, FBSdk.SERVER_URL, "91225258ddb5c8503dce33719c5deda7");
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
            data.put(NativeProtocol.WEB_DIALOG_ACTION, "AppEvent");
            FBCustomerItem customer = this._app.getFBSdkData().currCustomer;
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("memberid", FBPreferences.sharedInstance(this._app.context).getUserMemberforAppId());
            data.put("lat", 28.6154469d);
            data.put("lon", 77.3906964d);
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("event_source_id", FBUtility.getAndroidDeviceID(this._app.context));
            data.put("device_os_ver", this._app.getAndroidOs());
        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }

    public void addCommonDataGuest(JSONObject data) {
        try {
            data.put(NativeProtocol.WEB_DIALOG_ACTION, "AppEvent");
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("lat", 28.6154469d);
            data.put("lon", 77.3906964d);
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("event_source_id", FBUtility.getAndroidDeviceID(this._app.context));
            data.put("device_os_ver", this._app.getAndroidOs());
        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }

    public void recordErrorEvent() {
        if (this._app.checkAppEventFlag()) {
            JSONObject data = new JSONObject();
            try {
                data.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, "AppError");
                addCommonData(data);
                FBAnalytic.sharedInstance().recordEvent(data);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void track_ItemWith(String id, String description, String eventName) {
        if (checkValidCall()) {
            try {
                JSONObject data = new JSONObject();
                if (eventName != null && id != null && description != null) {
                 //   data.put(Param.ITEM_ID, id);
                  //  data.put(Param.ITEM_NAME, description);
                    data.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, eventName);
                    addCommonData(data);
                    if (this._app.checkAppEventFlag()) {
                        FBAnalytic.sharedInstance().recordEvent(data);
                    }
                }
            } catch (Exception e) {
                recordErrorEvent();
            }
        }
    }

    public boolean checkValidCall() {
        if (this._app == null || this._app.getFBSdkData() == null) {
            return false;
        }
        if (FBUtility.isNetworkAvailable(this._app.context)) {
            if (this._app.getmCurrentLocation() == null) {
                Location curLoc = new Location("");
                curLoc.setLatitude(0.0d);
                curLoc.setLongitude(0.0d);
                this._app.setmCurrentLocation(curLoc);
            }
            return true;
        }
        Log.d("fbSdk", "getCurrCustomer is null");
        return false;
    }

    public boolean checkValidCallGuest() {
        if (this._app == null) {
            return false;
        }
        if (FBUtility.isNetworkAvailable(this._app.context)) {
            if (this._app.getmCurrentLocation() == null) {
                Location curLoc = new Location("");
                curLoc.setLatitude(0.0d);
                curLoc.setLongitude(0.0d);
                this._app.setmCurrentLocation(curLoc);
            }
            return true;
        }
        Log.d("fbSdk", "getCurrCustomer is null");
        return false;
    }

    public void track_EventbyName(String eventName) {
        if (checkValidCall()) {
            try {
                JSONObject data = new JSONObject();
                data.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, eventName);
                addCommonData(data);
                if (this._app.checkAppEventFlag()) {
                    FBAnalytic.sharedInstance().recordEvent(data);
                }
            } catch (Exception e) {
                recordErrorEvent();
            }
        }
    }

    public void track_EvenforGuesttbyName(String eventName) {
        if (checkValidCallGuest()) {
            try {
                JSONObject data = new JSONObject();
                data.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, eventName);
                addCommonDataGuest(data);
                if (this._app.checkAppEventFlag()) {
                    FBAnalytic.sharedInstance().recordEvent(data);
                }
            } catch (Exception e) {
                recordErrorEvent();
            }
        }
    }
}
