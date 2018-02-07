package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public interface OloBasketSubmitServiceCallback
{
    public void onOloBasketSubmitServiceCallback(OloOrderStatus orderStatus, Exception exception);
}
