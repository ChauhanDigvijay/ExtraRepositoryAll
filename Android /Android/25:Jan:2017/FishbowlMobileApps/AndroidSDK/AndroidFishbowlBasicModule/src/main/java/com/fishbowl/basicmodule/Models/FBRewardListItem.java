package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBRewardListItem {
    private String message;
    private boolean successFlag;
    private FBRewardDetailItem[] categories;

    public FBRewardListItem(String message, boolean successFlag, FBRewardDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBRewardDetailItem[] getCategories() {
        return categories;
    }


}