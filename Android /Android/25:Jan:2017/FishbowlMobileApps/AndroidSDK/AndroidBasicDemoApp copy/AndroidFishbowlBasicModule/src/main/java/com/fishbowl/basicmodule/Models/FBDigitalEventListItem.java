package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBDigitalEventListItem implements Serializable {
	private String message;
	private boolean successFlag;
	private FBDigitalEventItem[] categories;


	public FBDigitalEventListItem(String message, boolean successFlag, FBDigitalEventItem[] categories) {
		this.message = message;
		this.successFlag = successFlag;
		this.categories = categories;
	}

	public FBDigitalEventItem[] getCategories() {
		return categories;
	}
}
