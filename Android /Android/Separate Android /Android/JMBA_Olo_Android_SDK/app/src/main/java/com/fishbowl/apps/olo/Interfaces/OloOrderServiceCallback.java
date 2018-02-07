package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloOrderStatus;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public interface OloOrderServiceCallback
{
    public void onOrderServiceCallback(OloOrderStatus orderStatus, Exception error);
}
