package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBMenuSubCategoryItem {
    private String message;
    private boolean successFlag;
    private FBMenuSubCategoryDetailItem[] subcategories;

    public FBMenuSubCategoryItem(String message, boolean successFlag, FBMenuSubCategoryDetailItem[] subcategories) {
        this.message = message;
        this.successFlag = successFlag;
        this.subcategories = subcategories;
    }

    public FBMenuSubCategoryDetailItem[] getCategories() {
        return subcategories;
    }


}
