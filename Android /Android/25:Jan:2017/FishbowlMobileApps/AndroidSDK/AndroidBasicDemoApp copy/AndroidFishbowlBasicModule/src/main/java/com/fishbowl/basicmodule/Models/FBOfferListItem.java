package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBOfferListItem {
    private String message;
    private boolean successFlag;
    private FBOfferDetailItem[] categories;

    public FBOfferListItem(String message, boolean successFlag, FBOfferDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBOfferDetailItem[] getCategories() {
        return categories;
    }


}