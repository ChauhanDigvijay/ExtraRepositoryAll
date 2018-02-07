package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;

import org.json.JSONObject;

public class RewardService {
    public static RewardSummary offerSummary = null;

    public static void getUserReward(Activity activity, final RewardSummaryCallback callback) {
        JSONObject data = new JSONObject();
        FB_LY_UserOfferService.sharedInstance().getUserClypReward(data, " ", new FB_LY_UserOfferService.ClypRewardCallback() {
            @Override
            public void onClypRewardCallback(JSONObject response, Exception error) {
                offerSummary = new RewardSummary(response);
                callback.onRewardSummaryCallback(offerSummary, error);
            }
        });
    }
}
