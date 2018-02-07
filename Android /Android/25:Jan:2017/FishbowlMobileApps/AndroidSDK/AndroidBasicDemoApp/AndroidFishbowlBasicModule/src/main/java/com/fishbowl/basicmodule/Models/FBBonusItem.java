package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBBonusItem {
    private String message;
    private boolean successFlag;
    private FBBonusDetailItem[] categories;

    public FBBonusItem(String message, boolean successFlag, FBBonusDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBBonusDetailItem[] getCategories() {
        return categories;
    }


}
