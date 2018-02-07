package com.BasicApp.BusinessLogic.Interfaces;

import com.BasicApp.BusinessLogic.Models.LoyaltyActivityList;

/**
 * Created by digvijaychauhan on 25/11/16.
 */


public interface LoyaltyActivitySummaryCallback {
    public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList offerSummary, Exception error);
}
