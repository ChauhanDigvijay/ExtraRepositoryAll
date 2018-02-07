package com.wearehathway.apps.incomm.Models;

/**
 * Created by danda@Fishbowl on 11/19/16.
 */

public class InCommUserProfile {

    private long Id;
    private String FirstName;
    private String LastName;
    private String EmailAddress;
    private String MobilePhoneNumber;
    private String PhoneNumber;
    private String Credentials;
    private boolean IsActive;
    private boolean CorporateAccountId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getMobilePhoneNumber() {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        MobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCredentials() {
        return Credentials;
    }

    public void setCredentials(String credentials) {
        Credentials = credentials;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isCorporateAccountId() {
        return CorporateAccountId;
    }

    public void setCorporateAccountId(boolean corporateAccountId) {
        CorporateAccountId = corporateAccountId;
    }
}
