package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public interface OloOrderServiceCallback
{
    public void onOrderServiceCallback(OloOrderStatus orderStatus, Exception error);
}
