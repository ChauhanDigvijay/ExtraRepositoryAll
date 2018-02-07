package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */
public class FBMenuProductItem {
    private String message;
    private boolean successFlag;
    private FBMenuProductDetailItem[] categories;

    public FBMenuProductItem(String message, boolean successFlag, FBMenuProductDetailItem[] categories) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
    }

    public FBMenuProductDetailItem[] getCategories() {
        return categories;
    }


}
