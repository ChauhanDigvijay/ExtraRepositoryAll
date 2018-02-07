package com.raleys.app.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCategory implements Serializable {
	private static final long serialVersionUID = -7253555533257212178L;

	public int productCategoryId;
	public int parentCategoryId;
	public int grandParentCategoryId;
	public String parentCategoryName;
	public String grandParentCategoryName;
	public String name;
	public String type;
	public int level;
	public int priority;
	public ArrayList<ProductCategory> subCategoryList; // will be empty if this
														// is the lowest level
														// and there are
														// products in
														// productList
	public ArrayList<ProductCategory> categoryList;
}
