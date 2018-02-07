package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */

public class FBRestaurantListDetailItem {

    private String message;
    private boolean successFlag;
    private FBRestaurantDetailItem[] categories;


    public FBRestaurantListDetailItem(String message, boolean successFlag, FBRestaurantDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }


    public FBRestaurantDetailItem[] getCategories() {
        return categories;
    }


}
