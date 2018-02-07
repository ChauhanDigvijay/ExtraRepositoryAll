package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by digvijaychauhan on 03/02/17.
 */

public class ActivityServiceSort {


    public static LoyaltyActivityList loyaltyactivitylist = null;

    public static void getActivity(Activity activity, JSONArray data, String activityType, final LoyaltyActivitySummaryCallback callback) {

        FB_LY_UserService.sharedInstance().getActivitySort(data, activityType, new FB_LY_UserService.FBGetActivity() {

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
