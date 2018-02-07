package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloMenu;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public interface OloMenuServiceCallback
{
    public void onRestaurantMenuCallback(OloMenu menu, Exception exception);
}
