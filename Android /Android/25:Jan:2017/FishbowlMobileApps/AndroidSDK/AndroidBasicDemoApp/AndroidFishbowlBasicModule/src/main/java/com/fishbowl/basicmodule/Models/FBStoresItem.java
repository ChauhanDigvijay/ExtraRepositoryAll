package com.fishbowl.basicmodule.Models;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */

public class FBStoresItem implements Serializable{
		
	private static final long serialVersionUID = 7358362607050904110L;
	public int storeID;
	public int companyID;//tenantId we are getting tenantId rather then companyID
	public String storeName;
	public String contactPerson;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String phone;
	public String mobile;
	public String email;
	public String latitude;
	public String longitude;
	public String storeNumber;
	public String enableGeoConquest;
	public String createDate;
	public String updateDate;
	public double _distanceFromLocation;  // not part of json content
	/**
	 * Created by digvijay(dj)
	 */
	public FBStoresItem(){}
	public FBStoresItem(JSONObject obj)
	{
		try
		{
			
			storeID = obj.has("storeID") ? obj.getInt("storeID") : 0;
			companyID = obj.has("tenantId") ? obj.getInt("tenantId") : 0;
			storeName = obj.has("storeName") ? obj.getString("storeName")  :null;
			contactPerson = obj.has("contactPerson") ? obj.getString("contactPerson") : null;
			address = obj.has("address") ? obj.getString("address") : null;
			city = obj.has("city") ? obj.getString("city"): null;
			state =obj.has("state") ? obj.getString("state") :null;
			zip = obj.has("zip")?obj.getString("zip"):null;
			country = obj.has("country")?obj.getString("country"):null;
			phone =obj.has("phone")? obj.getString("phone"):null;
			mobile = obj.has("mobile")?obj.getString("mobile"):null;
			email =obj.has("email")? obj.getString("email"):null;
			latitude = obj.has("latitude")?obj.getString("latitude"):null;
			longitude = obj.has("longitude")?obj.getString("longitude"):null;
			storeNumber =obj.has("storeNumber")? obj.getString("storeNumber"):null;
			enableGeoConquest = obj.has("enableGeoConquest")?obj.getString("enableGeoConquest"):null;
			createDate =obj.has("createDate")? obj.getString("createDate"):null;
			updateDate =obj.has("updateDate")? obj.getString("updateDate"):null;
			
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}

	public int getStoreID() {
		return storeID;
	}
	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	public int getCompanyID() {
		return companyID;
	}
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getEnableGeoConquest() {
		return enableGeoConquest;
	}
	public void setEnableGeoConquest(String enableGeoConquest) {
		this.enableGeoConquest = enableGeoConquest;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public double getDistanceFromCLocation() {
		return _distanceFromLocation;
	}
	public void setDistanceFromCLocation(double _distanceFromLocation) {
		this._distanceFromLocation = _distanceFromLocation;
	}
	@Override
	public String toString() {
		return "Stores [storeID=" + storeID + ", storeName=" + storeName + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", _distanceFromLocation=" + _distanceFromLocation
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + storeID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FBStoresItem other = (FBStoresItem) obj;
		if (storeID != other.storeID)
			return false;
		return true;
	}
	public Location getLocation() {
		Location storeLocation = new Location("ClpStores");
		storeLocation.setLatitude(Double.valueOf(this.getLatitude()));
		storeLocation.setLongitude(Double.valueOf(this.getLongitude()));
		return storeLocation;
	}


}
