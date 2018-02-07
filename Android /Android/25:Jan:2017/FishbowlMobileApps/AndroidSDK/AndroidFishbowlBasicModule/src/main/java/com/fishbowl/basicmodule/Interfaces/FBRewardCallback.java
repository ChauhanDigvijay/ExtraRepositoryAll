package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBRewardListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBRewardCallback {
    public void onFBOfferCallback(FBRewardListItem restaurants, Exception error);
}
