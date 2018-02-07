package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList;

/**
 * Created by digvijaychauhan on 25/11/16.
 */


public interface LoyaltyActivitySummaryCallback {
    public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList offerSummary, Exception error);
}
