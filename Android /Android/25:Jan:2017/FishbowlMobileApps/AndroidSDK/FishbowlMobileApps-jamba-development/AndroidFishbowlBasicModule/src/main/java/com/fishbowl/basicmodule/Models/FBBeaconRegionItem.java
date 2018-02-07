package com.fishbowl.basicmodule.Models;

import com.estimote.sdk.Region;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBBeaconRegionItem implements Serializable{
	
	private static final long serialVersionUID = -2087298141312790573L;
	public Region region;
	public int StoreId;
	public FBBeaconRegionItem(Region region, int storeId) {
		this.region = region;
		StoreId = storeId;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public int getStoreId() {
		return StoreId;
	}
	public void setStoreId(int storeId) {
		StoreId = storeId;
	}
	

}
