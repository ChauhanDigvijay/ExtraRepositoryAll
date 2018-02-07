package com.BasicApp.BusinessLogic.Interfaces;

import com.fishbowl.basicmodule.Models.FBRewardPointDetailItem;

/**
 * Created by digvijaychauhan on 27/10/17.
 */


public interface FBARewardPointCallback
{
    public void onFBRewardPointCallback(FBRewardPointDetailItem response, Exception error);
}
