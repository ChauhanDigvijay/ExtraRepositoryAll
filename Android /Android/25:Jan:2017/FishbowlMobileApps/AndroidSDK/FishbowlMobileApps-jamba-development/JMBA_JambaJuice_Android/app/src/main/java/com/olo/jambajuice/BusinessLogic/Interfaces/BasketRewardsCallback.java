package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Reward;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public interface BasketRewardsCallback {
    public void onBasketRewardsCallback(ArrayList<Reward> reward, Exception error);
}
