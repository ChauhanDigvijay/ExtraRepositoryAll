package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBMobileSettingListItem implements Serializable {
	private String message;
	private boolean successFlag;
	private FBMobileSettingItem[] categories;


	public FBMobileSettingListItem(String message, boolean successFlag, FBMobileSettingItem[] categories) {
		this.message = message;
		this.successFlag = successFlag;
		this.categories = categories;
	}

	public FBMobileSettingItem[] getCategories() {
		return categories;
	}
}
