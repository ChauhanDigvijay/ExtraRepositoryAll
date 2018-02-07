package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloMenu;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public interface OloMenuServiceCallback
{
    public void onRestaurantMenuCallback(OloMenu menu, Exception exception);
}
