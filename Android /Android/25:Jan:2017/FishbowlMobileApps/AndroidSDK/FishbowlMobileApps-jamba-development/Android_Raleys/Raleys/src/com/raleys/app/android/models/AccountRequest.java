package com.raleys.app.android.models;

import java.io.Serializable;

public class AccountRequest implements Serializable {
	private static final long serialVersionUID = 8714970475455405873L;
	public String crmNumber;
	public String accountId;
	public String password;
	public String favoriteDept;
	public String linkSource;
	public String modifiedBy;
	public String email;
	public String firstName;
	public String lastName;
	public String employee;
	public String employeeID;
	public String cardStatus;
	public String phone;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String loyaltyNumber;
	public String middleName;
	public String customerStatus;
	public String mobilePhone;
	public String prefix;
	public String suffix;
	public String voucherMethod;
	public boolean sendEmailsFlag;
	public boolean sendTextsFlag;
	public boolean issueCardFlag;
	public boolean termsAcceptedFlag;
	public int storeNumber;
	public long dateOfBirth;
	public long enrollmentDate;
	public long dateCreated;
	public long dateModified;
}
