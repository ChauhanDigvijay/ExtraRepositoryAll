package com.BasicApp.BusinessLogic.Interfaces;

import com.fishbowl.basicmodule.Models.FBOfferListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBAOfferCallback
{
    public void onFBOfferCallback(FBOfferListItem response, Exception error);
}
