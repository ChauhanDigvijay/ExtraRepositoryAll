package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBMenuDrawerItem {
    private String message;
    private boolean successFlag;
    private FBMenuDrawerDetailItem[] categories;

    public FBMenuDrawerItem(String message, boolean successFlag, FBMenuDrawerDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBMenuDrawerDetailItem[] getCategories() {
        return categories;
    }


}
