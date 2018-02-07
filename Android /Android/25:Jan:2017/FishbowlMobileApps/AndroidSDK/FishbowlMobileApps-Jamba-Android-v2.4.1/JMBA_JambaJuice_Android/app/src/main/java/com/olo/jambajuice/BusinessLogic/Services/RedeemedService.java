package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBRedeemedCallback;
import com.fishbowl.basicmodule.Interfaces.FBRedeemedSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RedeemedServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.JambaApplication;

import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class RedeemedService {

    public static OfferSummary offerSummary = null;

//    public static void getRedeemedservices(Activity activity, String offerId, final RedeemedServiceCallback callback) {
//
//        JambaApplication _app = JambaApplication.getAppContext();
//        JSONObject data = new JSONObject();
//
//
//        _app.fbsdkObj.getredeemedservices(data, offerId, new FBRedeemedSummaryCallback() {
//            @Override
//            public void onClypRedeemedCallback(JSONObject response, String error) {
//                callback.onRedeemedServiceCallback(response, "");
//            }
//        });
//    }
}
