package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.BasicApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.BasicApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 25/11/16.
 */

public class ActivityService {

    public static LoyaltyActivityList loyaltyactivitylist = null;

    public static void getActivity(Activity activity, JSONObject data, final LoyaltyActivitySummaryCallback callback) {

        FBUserService.sharedInstance().getActivity(data, new FBUserService.FBGetActivity() {

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
