package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBRewardPointDetailItem;

/**
 * Created by digvijaychauhan on 27/10/17.
 */


public interface FBRewardPointCallback
{
    public void onFBRewardPointCallback(FBRewardPointDetailItem response, Exception error);
}
