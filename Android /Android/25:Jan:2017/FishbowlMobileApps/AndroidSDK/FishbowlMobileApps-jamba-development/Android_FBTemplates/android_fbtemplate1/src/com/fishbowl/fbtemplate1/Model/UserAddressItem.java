package com.fishbowl.fbtemplate1.Model;

import java.io.Serializable;

/**
 **
 * Created by Digvijay Chauhan on 9/1/16.
 */
public class UserAddressItem  implements Serializable {

	private long id;	
	private String userID;
	private String useraddressType;
	private String userStreet;
	private String usertownCity;
	private String userstateRegion;
	private String userpostZip;
	private String userCountry;
	private boolean userPreferred;
	private Double usergeoLoc;
	private String userlocationID;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUseraddressType() {
		return useraddressType;
	}
	public void setUseraddressType(String useraddressType) {
		this.useraddressType = useraddressType;
	}
	public String getUserStreet() {
		return userStreet;
	}
	public void setUserStreet(String userStreet) {
		this.userStreet = userStreet;
	}
	public String getUsertownCity() {
		return usertownCity;
	}
	public void setUsertownCity(String usertownCity) {
		this.usertownCity = usertownCity;
	}
	public String getUserstateRegion() {
		return userstateRegion;
	}
	public void setUserstateRegion(String userstateRegion) {
		this.userstateRegion = userstateRegion;
	}
	public Double getUsergeoLoc() {
		return usergeoLoc;
	}
	public void setUsergeoLoc(Double usergeoLoc) {
		this.usergeoLoc = usergeoLoc;
	}
	public String getUserpostZip() {
		return userpostZip;
	}
	public void setUserpostZip(String userpostZip) {
		this.userpostZip = userpostZip;
	}
	public String getUserCountry() {
		return userCountry;
	}
	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}
	public boolean isUserPreferred() {
		return userPreferred;
	}
	public void setUserPreferred(boolean userPreferred) {
		this.userPreferred = userPreferred;
	}
	
	public String getUserlocationID() {
		return userlocationID;
	}
	public void setUserlocationID(String userlocationID) {
		this.userlocationID = userlocationID;
	}
			
}
