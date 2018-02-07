package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.Reward;

import java.util.ArrayList;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface BasketRewardsCallback {
    public void onBasketRewardsCallback(ArrayList<Reward> reward, Exception error);
}
