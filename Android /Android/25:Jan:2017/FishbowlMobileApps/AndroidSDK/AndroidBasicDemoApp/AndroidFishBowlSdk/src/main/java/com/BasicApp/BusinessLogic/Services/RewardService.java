package com.BasicApp.BusinessLogic.Services;

/**
 * Created by digvijaychauhan on 21/09/16.
 */

import android.app.Activity;

import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryCallback;
import com.fishbowl.basicmodule.Services.FBUserOfferService;


import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class RewardService
{
    public static RewardSummary offerSummary = null;
    public static void getUserReward(Activity activity, final RewardSummaryCallback callback)
    {


        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBReward(data, " ", new FBUserOfferService.FBRewardCallback() {
            @Override
            public void OnFBRewardCallback(JSONObject response, Exception error) {

                offerSummary = new RewardSummary(response);
                callback.onRewardSummaryCallback(offerSummary, error);

            }
        });
    }
}
