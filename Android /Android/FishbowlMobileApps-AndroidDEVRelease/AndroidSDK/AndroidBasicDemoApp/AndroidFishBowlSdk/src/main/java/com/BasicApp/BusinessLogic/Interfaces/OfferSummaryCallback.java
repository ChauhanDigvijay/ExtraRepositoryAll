package com.BasicApp.BusinessLogic.Interfaces;


import com.BasicApp.BusinessLogic.Models.OfferSummary;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public interface OfferSummaryCallback {
    public void onOfferSummaryCallback(OfferSummary offerSummary, Exception error);
}

