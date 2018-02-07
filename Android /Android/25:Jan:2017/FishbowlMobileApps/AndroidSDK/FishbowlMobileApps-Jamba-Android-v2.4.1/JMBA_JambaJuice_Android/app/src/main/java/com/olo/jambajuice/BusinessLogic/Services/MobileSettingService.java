package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBOfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.JambaApplication;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 20/06/16.
 */
public class MobileSettingService {

    public static OfferSummary offerSummary = null;

    public static void getUserOffer(Activity activity, final OfferSummaryCallback callback) {

        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        _app.fbsdkObj.getFBOffer(data, " ", new FBOfferSummaryCallback()


        {
            @Override
            public void onClypOfferyCallback(JSONObject response, String error) {

                offerSummary = new OfferSummary(response);
                User user = UserService.getUser();
                user.setTotalOffers(offerSummary.getOfferCount());
                UserService.updateUserInformation();
                callback.onOfferSummaryCallback(offerSummary, "");

            }
        });
    }
}
