package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBRestaurantListItem {
    private String message;
    private boolean successFlag;
    private FBRestaurantItem[] categories;
    private FBRestaurantDetailItem[] restaurantDetail ;

    public FBRestaurantListItem(String message, boolean successFlag, FBRestaurantItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }
    public FBRestaurantListItem(String message, boolean successFlag, FBRestaurantDetailItem[] restaurantDetail) {
        this.message = message;
        this.successFlag = successFlag;
        this.restaurantDetail = restaurantDetail;
    }

    public FBRestaurantItem[] getCategories() {
        return categories;
    }


}
