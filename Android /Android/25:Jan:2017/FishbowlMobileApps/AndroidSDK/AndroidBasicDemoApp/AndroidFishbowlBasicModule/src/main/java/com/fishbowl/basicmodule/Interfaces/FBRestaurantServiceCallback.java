package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBRestaurantListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBRestaurantServiceCallback{
    public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error);
}