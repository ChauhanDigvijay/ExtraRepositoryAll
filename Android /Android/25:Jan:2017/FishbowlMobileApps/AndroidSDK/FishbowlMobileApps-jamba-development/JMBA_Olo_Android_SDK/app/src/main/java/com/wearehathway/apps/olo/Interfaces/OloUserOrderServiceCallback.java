package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public interface OloUserOrderServiceCallback
{
    public void onUserOrderServiceCallback(OloOrderStatus[] orderStatuses, Exception exception);
}
