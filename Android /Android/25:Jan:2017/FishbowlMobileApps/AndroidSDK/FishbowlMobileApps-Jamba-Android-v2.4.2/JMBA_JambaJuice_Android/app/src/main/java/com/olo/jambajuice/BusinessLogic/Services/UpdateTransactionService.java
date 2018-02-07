package com.olo.jambajuice.BusinessLogic.Services;

import com.fishbowl.basicmodule.Interfaces.FBUpdateTransactionCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.IncommUpdateTransaction;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vt010 on 11/15/16.
 */

public class UpdateTransactionService {
    public static void updateTransactionOrder(String orderId,int status,String customerId,String errorReason, final IncommUpdateTransaction callback) {
        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        try {
            data.put("inCommOrderId",orderId);
            data.put("orderStatus",status);
            data.put("errorReason",errorReason);
            data.put("customerId", customerId);
            data.put("orderDateTime",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Utils.isNetworkAvailable(_app)) {
            _app.fbsdkObj.updateInCommTransaction(data, new FBUpdateTransactionCallback() {
                @Override
                public void OnFBUpdateTransactionCallback(JSONObject jsonObject, Exception e) {
                    Boolean successFlag = false;
                    try {
                        if(jsonObject != null) {
                            successFlag = jsonObject.getBoolean("successFlag");
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    callback.onIncommUpdateTransactionCallback(successFlag,Utils.getErrorDescription(e));
                }
            });

        }else{
            callback.onIncommUpdateTransactionCallback(false, Constants.NO_INTERNET_CONNECTION);
        }
    }
}
