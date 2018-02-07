package com.raleys.app.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable
{
	private static final long serialVersionUID = -7353555533257212177L;
	public int    storeNumber;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String longitude;
	public String latitude;
	public String groceryPhoneNo;
	public String chain;
	public String ecart;
	public ArrayList<Store> storeList;
	public double _distanceFromLocation;  // not part of json content
}
