package com.olo.jambajuice.BusinessLogic.Services;

import com.fishbowl.basicmodule.Interfaces.FBOrderIdCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.IncommOrderIdCallback;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vt010 on 11/15/16.
 */

public class OrderIdService {
    public static void getIncommOrderId(String customerId, final IncommOrderIdCallback callback) {
        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        try {
            data.put("customerId", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Utils.isNetworkAvailable(_app)) {
            _app.fbsdkObj.getInCommOrderId(data, new FBOrderIdCallback() {
                @Override
                public void OnFBOrderIdCallback(JSONObject jsonObject, Exception e) {
                    String orderId = null;
                    Boolean successFlag = false;
                    try {
                        if (jsonObject != null) {
                            successFlag = jsonObject.getBoolean("successFlag");
                            orderId = jsonObject.getString("inCommOrderId");
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    callback.onIncommOrderIdCallback(orderId, successFlag,e);
                }
            });

        } else {
            callback.onIncommOrderIdCallback(null, false, new Exception(Constants.NO_INTERNET_CONNECTION));
        }
    }
}
