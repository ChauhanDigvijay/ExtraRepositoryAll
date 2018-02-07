package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces;


import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public interface OfferSummaryCallback {
    public void onOfferSummaryCallback(OfferSummary offerSummary, Exception error);
}

