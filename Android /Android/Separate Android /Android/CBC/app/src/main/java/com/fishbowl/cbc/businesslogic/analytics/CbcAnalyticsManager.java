package com.fishbowl.cbc.businesslogic.analytics;

import android.location.Location;
import android.util.Log;

import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.cbc.CbcApplication;
import com.fishbowl.cbc.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VT027 on 5/22/2017.
 */

public class CbcAnalyticsManager {
    static CbcAnalyticsManager sInstance;
    public CbcApplication _app;

    public CbcAnalyticsManager() {
        this._app = CbcApplication.getInstance();
//        if (_app.fbsdkObj != null) {
//            FBAnalytic.sharedInstance().init(this._app, _app.fbsdkObj.SERVER_URL, Constants.FBApiKey);
//        } else {
//            FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.MainProduction), Constants.FBApiKey);
//        }
        FBAnalytic.sharedInstance().init(this._app, Constants.sdkPointingUrl(Constants.getEnvironment()), Constants.FBApiKey);
    }

    public static CbcAnalyticsManager sharedInstance() {
        if (sInstance == null) {
            sInstance = new CbcAnalyticsManager();
        }
        return sInstance;
    }

//    private String generateUniqueEventId() {
//        return UUID.randomUUID().toString();
//    }

    public void addCommonData(JSONObject data) {

        try {
            data.put("action", "AppEvent");
            //data.put("event_id", generateUniqueEventId());
            data.put("memberid", _app.fbsdkObj.getFBSdkData().currCustomer.getCustomerID());
            data.put("lat", _app.fbsdkObj.getmCurrentLocation().getLatitude());
            data.put("lon", _app.fbsdkObj.getmCurrentLocation().getLongitude());
            data.put("device_type", FBConstant.DEVICE_TYPE);
            data.put("tenantid", FBConstant.client_tenantid);
            data.put("device_os_ver", _app.fbsdkObj.getAndroidOs());
            data.put("event_source_id", FBUtility.getAndroidDeviceID(this._app));

        } catch (JSONException e) {
            e.printStackTrace();
            recordErrorEvent();
        } catch (Exception e) {
            e.printStackTrace();
            recordErrorEvent();
        }
    }

    public void recordErrorEvent() {
        if (_app.fbsdkObj.checkAppEventFlag()) {
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
                //Clyp Analytic
                if (_app.fbsdkObj.checkAppEventFlag())
                    FBAnalytic.sharedInstance().recordEvent(data);
                //_app.fbsdkObj.updateAppEvent(data);
            }

        } catch (Exception e) {
            recordErrorEvent();
        }
    }
//
//    public void track_OfferItemWith(String offerId, String clpId, String id, String description, String eventName) {
//        if (!checkValidCall())
//            return;
//
//        try {
//            JSONObject data = new JSONObject();
//            if (eventName != null && offerId != null && clpId != null && id != null && description != null) {
//                data.put("item_id", id);
//                data.put("item_name", description);
//
//                data.put("event_name", eventName);
//                data.put("notifid", clpId);
//                data.put("offerid", offerId);
//
//                //if (_app.fbsdkObj.checkAppEventFlag())
//                    //_app.fbsdkObj.sendOfferEvent(data);
//                //_app.fbsdkObj.updateAppEvent(data);
//            }
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }
//
//    public void track_OfferItemWith(String id, String description, String eventName) {
//        if (!checkValidCall())
//            return;
//
//        try {
//            JSONObject data = new JSONObject();
//            if (eventName != null && id != null && description != null) {
//                data.put("item_id", id);
//                data.put("item_name", description);
//                data.put("event_name", eventName);
//
//
//                //if (_app.fbsdkObj.checkAppEventFlag())
//                    //_app.fbsdkObj.sendOfferEvent(data);
//                //_app.fbsdkObj.updateAppEvent(data);
//            }
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }
//
//    //Track Product Viewed
//    public void track_Product(Product product, String eventName) {
//        if (!checkValidCall())
//            return;
//
//        try {
//            JSONObject data = new JSONObject();
//            if (product != null && product.getName() != null && eventName != null) {
//                data.put("item_id", product.getProductId());
//                data.put("item_name", product.getName());
//                data.put("event_name", eventName);
//                addCommonData(data);
//                //Clyp Analytic
//                if (_app.fbsdkObj.checkAppEventFlag())
//                    FBAnalytic.sharedInstance().recordEvent(data);
//
//            }
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }
//
//    //Track Product Viewed
//    public void track_Favourate_Store() {
//        if (!checkValidCall())
//            return;
//
//        try {
//            JSONObject data = new JSONObject();
//            data.put("store", Constants.B_STORE);
//            data.put("event_name", FBEventSettings.FAV_STORE);
//            data.put("event_time", FBUtility.formatedCurrentDate());
//
//            //Clyp Analytic
//            addCommonData(data);
//
//            if (_app.fbsdkObj.checkAppEventFlag())
//                FBAnalytic.sharedInstance().recordEvent(data);
//
//
//        } catch (Exception e) {
//            recordErrorEvent();
//        }
//    }

    public boolean checkValidCall() {
        if (_app.fbsdkObj == null)
            return false;

        if (_app.fbsdkObj.getFBSdkData() == null)
            return false;

        if (_app.fbsdkObj.getFBSdkData().getCurrCustomer() == null)
            return false;


        if (_app.fbsdkObj.getFBSdkData().getCurrCustomer().memberid <= 0
                || !FBUtility.isNetworkAvailable(_app)) {

            Log.d("fbSdk", "getCurrCustomer is null");
            //localLog("fbSdk : ", "getCurrCustomer is null");
            return false;
        }

        //localLog("fbSdk : ", "sendAppEvent");

        if (_app.fbsdkObj.getmCurrentLocation() == null) {
            Location curLoc = new Location("");
            curLoc.setLatitude(0);
            curLoc.setLongitude(0);
            _app.fbsdkObj.setmCurrentLocation(curLoc);
        }
        return true;
    }

    public void track_EventbyName(String eventName) {
        if (!checkValidCall())
            return;

        try {

            JSONObject data = new JSONObject();
            data.put("event_name", eventName);
            // data.put("event_time", _app.fbsdkObj.formatedCurrentDate());
            addCommonData(data);
            //Clyp Analytic
            if (_app.fbsdkObj.checkAppEventFlag())
                FBAnalytic.sharedInstance().recordEvent(data);


        } catch (Exception e) {
            recordErrorEvent();
        }
    }


//    public void trackClypEvent(String category, String action, String label) {
//
//    }
//
//    public void trackClypEvent(String category, String action) {
//
//    }
//
//    //All screens are automatically tracked except fragments and home signedIn and nonSignedIn View.
//    public void trackClypScreen(String screenName) {
//
//    }


}
