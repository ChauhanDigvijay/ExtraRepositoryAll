package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBLoyaltyAreaTypeListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBLoyaltyAreaTypeCallback {
    public void onFBLoyaltyTypeCallback(FBLoyaltyAreaTypeListItem loyaltytype, Exception error);
}
