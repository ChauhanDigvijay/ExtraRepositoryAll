package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBRestaurantListDetailItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBRestaurantServiceDetailCallback{
    public void onRestaurantServiceDetailCallback(FBRestaurantListDetailItem response, Exception error);
}