package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;

import org.json.JSONObject;

public class OfferService {
    public static OfferSummary offerSummary = null;

    public static void getUserOffer(Activity activity, final OfferSummaryCallback callback) {
        JSONObject data = new JSONObject();
        FB_LY_UserOfferService.sharedInstance().getUserClypOffer(data, " ", new FB_LY_UserOfferService.ClypOfferCallback() {
            @Override
            public void onClypOfferCallback(JSONObject response, Exception error) {
                offerSummary = new OfferSummary(response);
                callback.onOfferSummaryCallback(offerSummary, error);
            }
        });
    }
}
