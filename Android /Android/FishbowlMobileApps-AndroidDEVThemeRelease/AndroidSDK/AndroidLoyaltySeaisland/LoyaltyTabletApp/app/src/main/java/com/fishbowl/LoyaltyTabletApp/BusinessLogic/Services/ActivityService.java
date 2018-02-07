package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 25/11/16.
 */

public class ActivityService {

    public static LoyaltyActivityList loyaltyactivitylist = null;

    public static void getActivity(Activity activity, JSONObject data, final LoyaltyActivitySummaryCallback callback) {

        FB_LY_UserService.sharedInstance().getActivity(data, new FB_LY_UserService.FBGetActivity() {

            @Override
            public void onFBGetActivity(JSONObject response, Exception error) {
                if (response != null) {
                    loyaltyactivitylist = new LoyaltyActivityList(response);
                    callback.onLoyaltyActivitySummaryCallback(loyaltyactivitylist, error);
                } else {
                    callback.onLoyaltyActivitySummaryCallback(null, error);
                }
            }
        });
    }
}
