package com.raleys.app.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable
{
	private static final long serialVersionUID = -7353555533257212178L;
	
	public String sku;
	public String upc;
	public String brand;
	public int    storeNumber;
	public String description;
	public String extendedDisplay;
	public int    packSize;
	public String unitOfMeasure;
	public int    regForQty;
	public float  regPrice;
	public float  salesTaxRate;
	public String promoPriceText;
	public float  promoPrice;
	public int    promoForQty;
	public int    promoType;
	public String promoStart;
	public String promoEnd;
	public int    aisleNumber;
	public String aisleSide;
	public String imagePath;
	public int    qty;
	public float  weight;
	public String sellByCode;
	public String customerComment;
	public float  approxAvgWgt;
	public String mainCategory;
	public float  crvAmount;
	public String ingredients;
	public ArrayList<Product> productList;
	
	public static final int PRODUCT_ADD = 1;
	public static final int PRODUCT_MODIFY = 2;
	public static final int PRODUCT_DELETE = 3;
}
