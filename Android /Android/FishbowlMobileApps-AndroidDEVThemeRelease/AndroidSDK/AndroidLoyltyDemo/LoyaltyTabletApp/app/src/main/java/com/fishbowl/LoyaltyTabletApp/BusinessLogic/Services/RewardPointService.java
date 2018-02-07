package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardPointSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryPointCallback;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;

import org.json.JSONObject;

public class RewardPointService {
    public static RewardPointSummary offerSummary = null;

    public static void getUserRewardPoint(Activity activity, final RewardSummaryPointCallback callback) {
        JSONObject data = new JSONObject();
        FB_LY_UserOfferService.sharedInstance().getUserClypRewardPoint(data, " ", new FB_LY_UserOfferService.ClypRewardPointCallback() {
            @Override
            public void onClypRewardPointCallback(JSONObject response, Exception error) {
                offerSummary = new RewardPointSummary(response);
                callback.onRewardSummaryPointCallback(offerSummary, error);
            }
        });
    }
}
