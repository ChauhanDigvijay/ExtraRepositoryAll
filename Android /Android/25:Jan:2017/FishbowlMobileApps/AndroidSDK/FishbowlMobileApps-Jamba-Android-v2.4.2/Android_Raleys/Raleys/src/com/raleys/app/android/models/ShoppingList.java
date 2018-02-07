package com.raleys.app.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable
{
	private static final long serialVersionUID = -7353555533257212179L;
	public String  listId;
	public int     storeNumber;
	public String  name;
	public String  accountId;
	public String  listType;
	public String  modifiedBy;
	public float   totalPrice;
	public float   totalCrv;
	public long    serverUpdateTime;
	public boolean productListReturned;
	public ArrayList<Product> productList;
}
