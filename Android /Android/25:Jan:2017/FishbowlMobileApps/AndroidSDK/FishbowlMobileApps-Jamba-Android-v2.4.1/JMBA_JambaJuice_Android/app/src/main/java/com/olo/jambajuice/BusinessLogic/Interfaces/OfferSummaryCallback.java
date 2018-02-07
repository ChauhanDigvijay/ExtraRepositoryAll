package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;

/**
 * Created by Digvijay Chauhan on 2/3/16.
 */
public interface OfferSummaryCallback {
    public void onOfferSummaryCallback(OfferSummary offerSummary, String error);
}

