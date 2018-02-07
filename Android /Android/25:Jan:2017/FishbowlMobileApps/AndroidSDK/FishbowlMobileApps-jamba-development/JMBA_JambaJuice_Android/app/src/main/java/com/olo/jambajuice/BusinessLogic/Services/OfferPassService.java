package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBOfferPassCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPassServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.JambaApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class OfferPassService {
    public static OfferSummary offerSummary = null;

    public static void getOfferservices(Activity activity, String offerId, Boolean isPMIntegrated, final OfferPassServiceCallback callback) {

        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        try {
            data.put("offerId", offerId);
            data.put("isPMIntegrated", isPMIntegrated);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(_app.fbsdkObj != null) {
            _app.fbsdkObj.getFBOfferPass(data, offerId, isPMIntegrated, new FBOfferPassCallback()

            {
                @Override
                public void onClypOfferPassCallback(byte[] response, String error)

                {
                    if (response != null) {

                        callback.onOfferServiceCallback(response, "");
                    }
                }
            });
        }


    }
}
