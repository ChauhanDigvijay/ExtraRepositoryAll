package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloRestaurant;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public interface OloRestaurantServiceCallback
{
    public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception);
}
