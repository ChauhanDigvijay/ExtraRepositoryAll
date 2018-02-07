package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBOfferSummaryCallback;
import com.Preferences.FBPreferences;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.JambaApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class OfferService {
    public static OfferSummary offerSummary = null;

    public static void getUserOffer(Activity activity, final OfferSummaryCallback callback) {

        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        if (FBPreferences.sharedInstance(_app).getUserMemberId() > 0) {
            _app.fbsdkObj.getFBOffer(data, " ", new FBOfferSummaryCallback() {
                @Override
                public void onClypOfferyCallback(JSONObject response, String error) {
                    if (response != null) {
                        offerSummary = new OfferSummary(response);
                        User user = UserService.getUser();
                        user.setTotalOffers(offerSummary.getOfferCount());
                        UserService.updateUserInformation();
                        callback.onOfferSummaryCallback(offerSummary, "");
                    } else {
                        callback.onOfferSummaryCallback(null, error);
                    }

                }
            });
        } else {
            callback.onOfferSummaryCallback(null, null);
        }
    }

    public static void clearOfferList(){
        if(offerSummary != null && offerSummary.getOfferList() != null) {
            offerSummary.getOfferList().clear();
        }
    }
}
