package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBLoyaltyAreaTypeListItem {
    private String message;
    private boolean successFlag;
    private FBLoyaltyAreaTypeItem[] categories;

    public FBLoyaltyAreaTypeListItem(String message, boolean successFlag, FBLoyaltyAreaTypeItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBLoyaltyAreaTypeItem[] getCategories() {
        return categories;
    }


}