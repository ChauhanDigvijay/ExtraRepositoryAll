package com.BasicApp.BusinessLogic.Services;

/**
 * Created by digvijaychauhan on 21/09/16.
 */

import android.app.Activity;

import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryPointCallback;
import com.fishbowl.basicmodule.Services.FBUserOfferService;


import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class RewardPointService
{
    public static RewardPointSummary offerSummary = null;
    public static void getUserRewardPoint(Activity activity, final RewardSummaryPointCallback callback)
    {


        JSONObject data = new JSONObject();
        FBUserOfferService.sharedInstance().getUserFBRewardPoint(data, " ", new FBUserOfferService.FBRewardPointCallback() {
            @Override
            public void OnFBRewardPointCallback(JSONObject response, Exception error) {

                offerSummary = new RewardPointSummary(response);
                callback.onRewardSummaryPointCallback(offerSummary, error);

            }
        });
    }
}
