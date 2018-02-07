package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public interface OloBasketSubmitServiceCallback
{
    public void onOloBasketSubmitServiceCallback(OloOrderStatus orderStatus, Exception exception);
}
