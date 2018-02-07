package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public interface OloUserOrderServiceCallback
{
    public void onUserOrderServiceCallback(OloOrderStatus[] orderStatuses, Exception exception);
}
