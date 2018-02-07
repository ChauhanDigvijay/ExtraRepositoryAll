package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBSettingListItem {
    private String message;
    private boolean successFlag;
    private FBMobileSettingListItem fbmobilesettinglistitem;
    private FBDigitalEventListItem digitalEventList;

    public FBSettingListItem(String message, boolean successFlag, FBMobileSettingListItem categories,FBDigitalEventListItem digitalEventList) {
        this.message = message;
        this.successFlag = successFlag;
        this.fbmobilesettinglistitem = fbmobilesettinglistitem;
        this.digitalEventList = digitalEventList;
    }

    public FBMobileSettingListItem getCategories() {
        return fbmobilesettinglistitem;
    }

    public FBDigitalEventListItem getSubCategories() {
        return digitalEventList;
    }


}