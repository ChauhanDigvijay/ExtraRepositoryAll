package com.fishbowl.apps.olo.Models;

/**
 * Created by Ihsanulhaq on 6/1/2015.
 */
public class OloOrderInfo {

    private String basketId;
    private String billingMethod;
    private int billingAccountId;
    private String userType;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String reference;
    private String cardnumber;
    private int expiryyear;
    private int expirymonth;
    private String cvv;
    private String zip;
    private String saveonfile;
    private String orderref;

    public OloOrderInfo()
    {

    }
    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }

    public int getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(int billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public int getExpiryyear() {
        return expiryyear;
    }

    public void setExpiryyear(int expiryyear) {
        this.expiryyear = expiryyear;
    }

    public int getExpirymonth() {
        return expirymonth;
    }

    public void setExpirymonth(int expirymonth) {
        this.expirymonth = expirymonth;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getSaveonfile() {
        return saveonfile;
    }

    public void setSaveonfile(String saveonfile) {
        this.saveonfile = saveonfile;
    }

    public String getOrderref() {
        return orderref;
    }

    public void setOrderref(String orderref) {
        this.orderref = orderref;
    }
}
