package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloLoyaltyReward;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public interface OloBasketLoyaltyRewardsCallback
{
    public void onBasketLoyaltyRewardsCallback(ArrayList<OloLoyaltyReward> oloLoyaltyReward, Exception error);
}
