package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBLoyaltyMessageTypeListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBLoyaltyMessageTypeCallback {
    public void onFBLoyaltyMessageTypeCallback(FBLoyaltyMessageTypeListItem loyaltytype, Exception error);
}
