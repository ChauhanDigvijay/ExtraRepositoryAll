package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBMenuCategoryItem {
    private String message;
    private boolean successFlag;
    private FBMenuCategoryDetailItem[] categories;

    public FBMenuCategoryItem(String message, boolean successFlag, FBMenuCategoryDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBMenuCategoryDetailItem[] getCategories() {
        return categories;
    }


}
