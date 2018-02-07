package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBOfferListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBOfferCallback
{
    public void onFBOfferCallback(FBOfferListItem response, Exception error);
}
