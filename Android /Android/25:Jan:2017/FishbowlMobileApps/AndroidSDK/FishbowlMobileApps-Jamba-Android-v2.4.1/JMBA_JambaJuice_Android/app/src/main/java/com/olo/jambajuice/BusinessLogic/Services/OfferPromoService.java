package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBOfferPromoCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPromoCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.JambaApplication;

import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class OfferPromoService extends Activity {
    public static OfferSummary offerSummary = null;

//    public static void getUserOfferPromo(Activity activity, String offerId, Boolean isPMIntegrated, final OfferPromoCallback callback) {
//
//        JambaApplication _app = JambaApplication.getAppContext();
//        JSONObject data = new JSONObject();
//
//        _app.fbsdkObj.getFBOfferPromo(data, offerId, isPMIntegrated, new FBOfferPromoCallback() {
//            @Override
//            public void onClypOfferyCallback(JSONObject response, String error) {
//                if (response != null) {
//                    callback.onOfferPromoCallback(response, "");
//                }else{
//                    callback.onOfferPromoCallback(response,error);
//                }
//            }
//        });
//    }
}
