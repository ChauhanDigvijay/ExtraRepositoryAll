package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.BasicApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.BasicApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by digvijaychauhan on 03/02/17.
 */

public class ActivityServiceSort {


    public static LoyaltyActivityList loyaltyactivitylist = null;

    public static void getActivity(Activity activity, JSONArray data, String activityType, final LoyaltyActivitySummaryCallback callback) {

        FBUserService.sharedInstance().getActivitySort(data, activityType, new FBUserService.FBGetActivity() {

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
