package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBCustomerItem implements Serializable{
	

	private static final long serialVersionUID = -2379605260674479252L;
	public long tenantid; //we rae parsing with name Tenate Id in database
    public long memberid; //we rae parsing with name Member Id in database
	public String firstName;
	public String lastName;
	public String emailID;
	public String loginID;
	public String loginPassword;
	public String loyalityNo;
	public String loyalityLevel;
	public String homePhone;
	public String cellPhone;
	public String additionalPhone;
	public String addressLine1;
	public String addressLine2;
	public String addressCity;
	public String addressState;
	public String addressZip;
	public String addressCountry;
	public String customerTenantID;
	public String customerStatus;
	public String lastActivtiy;
	public String lastActivityTime;
	public String lastLoginTime;
	public int statusCode;
	public String registeredDate;
	public String registeredIP;
	public String invitationDate;
	public String customerGender;
	public String dateOfBirth;
	public String customerAge;
	public String homeStore;
	public String homeStoreID;  //dj
	public String favoriteDepartment;
	public String pushOpted;
	public String smsOpted;
	public String emailOpted;
	public String phoneOpted;
	public String adOpted;
	public String loyalityRewards;
	public String createdBy;
	public String createdDate;
	public String modifiedBy;
	public String modifedDate;
	public int customerDeviceID;
	public String deviceId;
	public String pushToken;
	public String deviceType;
	public String deviceOsVersion;
	public String deviceVendor;
	public String modifiedDate;
	public String enabledFlag;
	public String locationEnabled;
	public String appid;

	public FBCustomerItem(){}

	public String getappid() {
		return appid;
	}

	public void setTenantid(long tenantid) {
		this.tenantid = tenantid;
	}

	public FBCustomerItem(int customerID, int companyId, String firstName,
						  String lastName, String emailID, String loginID,
						  String loginPassword, String loyalityNo, String loyalityLevel,
						  String homePhone, String cellPhone, String additionalPhone,
						  String addressLine1, String addressLine2, String addressCity,
						  String addressState, String addressZip, String addressCountry,
						  String customerTenantID, String customerStatus,
						  String lastActivtiy, String lastActivityTime, String lastLoginTime,
						  int statusCode, String registeredDate, String registeredIP,
						  String invitationDate, String customerGender, String dateOfBirth,
						  String customerAge, String homeStore, String homeStoreID, String favoriteDepartment,
						  String pushOpted, String smsOpted, String emailOpted,
						  String phoneOpted, String adOpted, String loyalityRewards,
						  String createdBy, String createdDate, String modifiedBy,
						  String modifedDate, int customerDeviceID, String deviceId,
						  String pushToken, String deviceType, String deviceOsVersion,
						  String deviceVendor, String modifiedDate, String enabledFlag,
						  String locationEnabled, String _appid) {
		super();
		this.memberid = customerID;
		this.tenantid = companyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailID = emailID;
		this.loginID = loginID;
		this.loginPassword = loginPassword;
		this.loyalityNo = loyalityNo;
		this.loyalityLevel = loyalityLevel;
		this.homePhone = homePhone;
		this.cellPhone = cellPhone;
		this.additionalPhone = additionalPhone;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressCity = addressCity;
		this.addressState = addressState;
		this.addressZip = addressZip;
		this.addressCountry = addressCountry;
		this.customerTenantID = customerTenantID;
		this.customerStatus = customerStatus;
		this.lastActivtiy = lastActivtiy;
		this.lastActivityTime = lastActivityTime;
		this.lastLoginTime = lastLoginTime;
		this.statusCode = statusCode;
		this.registeredDate = registeredDate;
		this.registeredIP = registeredIP;
		this.invitationDate = invitationDate;
		this.customerGender = customerGender;
		this.dateOfBirth = dateOfBirth;
		this.customerAge = customerAge;
		this.homeStore = homeStore;
		this.homeStoreID=homeStoreID;
		this.favoriteDepartment = favoriteDepartment;
		this.pushOpted = pushOpted;
		this.smsOpted = smsOpted;
		this.emailOpted = emailOpted;
		this.phoneOpted = phoneOpted;
		this.adOpted = adOpted;
		this.loyalityRewards = loyalityRewards;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifedDate = modifedDate;
		this.customerDeviceID = customerDeviceID;
		this.deviceId = deviceId;
		this.pushToken = pushToken;
		this.deviceType = deviceType;
		this.deviceOsVersion = deviceOsVersion;
		this.deviceVendor = deviceVendor;
		this.modifiedDate = modifiedDate;
		this.enabledFlag = enabledFlag;
		this.locationEnabled = locationEnabled;
		this.appid=_appid;
	}

	public long getCustomerID() {
		return memberid;
	}
	public void setCustomerID(int customerID) {
		this.memberid = customerID;
	}
	public long getCompanyId() {
		return tenantid;
	}
	public void setCompanyId(int companyId) {
		this.tenantid = companyId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getLoyalityNo() {
		return loyalityNo;
	}
	public void setLoyalityNo(String loyalityNo) {
		this.loyalityNo = loyalityNo;
	}
	public String getLoyalityLevel() {
		return loyalityLevel;
	}
	public void setLoyalityLevel(String loyalityLevel) {
		this.loyalityLevel = loyalityLevel;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getAdditionalPhone() {
		return additionalPhone;
	}
	public void setAdditionalPhone(String additionalPhone) {
		this.additionalPhone = additionalPhone;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	public String getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	public String getAddressCountry() {
		return addressCountry;
	}
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	public String getCustomerTenantID() {
		return customerTenantID;
	}
	public void setCustomerTenantID(String customerTenantID) {
		this.customerTenantID = customerTenantID;
	}
	public String getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}
	public String getLastActivtiy() {
		return lastActivtiy;
	}
	public void setLastActivtiy(String lastActivtiy) {
		this.lastActivtiy = lastActivtiy;
	}
	public String getLastActivityTime() {
		return lastActivityTime;
	}
	public void setLastActivityTime(String lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getRegisteredIP() {
		return registeredIP;
	}
	public void setRegisteredIP(String registeredIP) {
		this.registeredIP = registeredIP;
	}
	public String getInvitationDate() {
		return invitationDate;
	}
	public void setInvitationDate(String invitationDate) {
		this.invitationDate = invitationDate;
	}
	public String getCustomerGender() {
		return customerGender;
	}
	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCustomerAge() {
		return customerAge;
	}
	public void setCustomerAge(String customerAge) {
		this.customerAge = customerAge;
	}
	public String getHomeStore() {
		return homeStore;
	}
	public void setHomeStore(String homeStore) {
		this.homeStore = homeStore;
	}


	public String getHomeStoreID() {
		return homeStoreID;
	}

	public void setHomeStoreID(String homeStoreID) {
		this.homeStoreID = homeStoreID;
	}
	public String getFavoriteDepartment() {
		return favoriteDepartment;
	}
	public void setFavoriteDepartment(String favoriteDepartment) {
		this.favoriteDepartment = favoriteDepartment;
	}
	public String getPushOpted() {
		return pushOpted;
	}
	public void setPushOpted(String pushOpted) {
		this.pushOpted = pushOpted;
	}
	public String getSmsOpted() {
		return smsOpted;
	}
	public void setSmsOpted(String smsOpted) {
		this.smsOpted = smsOpted;
	}
	public String getEmailOpted() {
		return emailOpted;
	}
	public void setEmailOpted(String emailOpted) {
		this.emailOpted = emailOpted;
	}
	public String getPhoneOpted() {
		return phoneOpted;
	}
	public void setPhoneOpted(String phoneOpted) {
		this.phoneOpted = phoneOpted;
	}
	public String getAdOpted() {
		return adOpted;
	}
	public void setAdOpted(String adOpted) {
		this.adOpted = adOpted;
	}
	public String getLoyalityRewards() {
		return loyalityRewards;
	}
	public void setLoyalityRewards(String loyalityRewards) {
		this.loyalityRewards = loyalityRewards;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifedDate() {
		return modifedDate;
	}
	public void setModifedDate(String modifedDate) {
		this.modifedDate = modifedDate;
	}
	public int getCustomerDeviceID() {
		return customerDeviceID;
	}
	public void setCustomerDeviceID(int customerDeviceID) {
		this.customerDeviceID = customerDeviceID;
	}
	public String getDeviceID() {
		return deviceId;
	}
	public void setDeviceID(String deviceID) {
		this.deviceId = deviceID;
	}
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceOsVersion() {
		return deviceOsVersion;
	}
	public void setDeviceOsVersion(String deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public String getLocationEnabled() {
		return locationEnabled;
	}
	public void setLocationEnabled(String locationEnabled) {
		this.locationEnabled = locationEnabled;
	}

}
