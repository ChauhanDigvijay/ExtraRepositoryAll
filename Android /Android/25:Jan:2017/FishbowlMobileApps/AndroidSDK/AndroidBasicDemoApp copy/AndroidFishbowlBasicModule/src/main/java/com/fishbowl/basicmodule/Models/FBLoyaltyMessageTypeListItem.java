package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBLoyaltyMessageTypeListItem {
    private String message;
    private boolean successFlag;
    private FBLoyaltyMessageTypeItem[] categories;

    public FBLoyaltyMessageTypeListItem(String message, boolean successFlag, FBLoyaltyMessageTypeItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBLoyaltyMessageTypeItem[] getCategories() {
        return categories;
    }


}