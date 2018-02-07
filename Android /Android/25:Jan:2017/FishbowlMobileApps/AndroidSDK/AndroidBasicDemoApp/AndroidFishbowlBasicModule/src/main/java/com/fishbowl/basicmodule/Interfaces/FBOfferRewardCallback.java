package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBOfferRewardItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBOfferRewardCallback
{
    public void onFBOfferRewardCallback(FBOfferRewardItem response, Exception error);
}
