package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloLoyaltyScheme;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public interface OloBasketLoyaltySchemesCallback
{
    public void onLoyaltySchemesCallback(ArrayList<OloLoyaltyScheme> oloLoyaltySchemes, Exception error);
}
