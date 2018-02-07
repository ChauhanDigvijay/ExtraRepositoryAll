//dj initial
package com.BasicApp.BusinessLogic.Models;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
public class UserItem {
	private long id;

	private String userID;
	private String fullname;
	private String firstname;
	private String lastname;
	private String password;
	private String mobile;
	private String email;
	private String country;
	private boolean isConfirmed;
	private String pushToken;
	
	public UserItem(){
		userID="";
		fullname="";
		firstname="";
		lastname="";
		password="";
		mobile="";
		email="";
		country="";
		pushToken="";
		isConfirmed=true;   
		
	}


	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	public boolean isConfirmed() {
		return isConfirmed;
	}
	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}
