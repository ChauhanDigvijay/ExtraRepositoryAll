package com.clp.model;

import java.io.Serializable;

import com.estimote.sdk.Region;

public class CLPBeaconRegion implements Serializable{
	
	private static final long serialVersionUID = -2087298141312790573L;
	public Region region;
	public int StoreId;
	public CLPBeaconRegion(Region region, int storeId) {
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
