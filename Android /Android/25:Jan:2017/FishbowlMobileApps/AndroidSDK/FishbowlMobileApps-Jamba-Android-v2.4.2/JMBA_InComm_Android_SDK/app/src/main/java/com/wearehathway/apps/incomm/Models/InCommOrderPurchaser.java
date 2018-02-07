package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommOrderPurchaser
{
    private String City;
    private String CompanyName;
    private String Country;
    private String EmailAddress;
    private String FirstName;
    private String LastName;
    private String MobilePhoneNumber;
    private String PhoneNumber;
    private boolean SendDeliveryEmailAlert; //Valid values are "true" and "false." If true, the purchaser will receive an email once the gift card is delivered to the recipient.
    private boolean SendDeliveryTextAlert; //Valid values are "true" and "false." If true, the purchaser will receive a text once the gift card is delivered to the recipient.
    private boolean SendViewEmailAlert; //Valid values are "true" and "false." If true, the purchaser will receive an email once the gift card is viewed by the recipient.
    private boolean SendViewTextAlert; //Valid values are "true" and "false." If true, the purchaser will receive a text message once the gift card is viewed by the recipient.
    private String StateProvince;
    private String StreetAddress1;
    private String StreetAddress2;
    private boolean SuppressReceiptEmail;//Valid values are "true" and "false." If true, the purchaser will receive an email receipt.
    private String ZipPostalCode;

    public String getCity()
    {
        return City;
    }

    public void setCity(String city)
    {
        City = city;
    }

    public String getCompanyName()
    {
        return CompanyName;
    }

    public void setCompanyName(String companyName)
    {
        CompanyName = companyName;
    }

    public String getCountry()
    {
        return Country;
    }

    public void setCountry(String country)
    {
        Country = country;
    }

    public String getEmailAddress()
    {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        EmailAddress = emailAddress;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public void setFirstName(String firstName)
    {
        FirstName = firstName;
    }

    public String getLastName()
    {
        return LastName;
    }

    public void setLastName(String lastName)
    {
        LastName = lastName;
    }

    public String getMobilePhoneNumber()
    {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber)
    {
        MobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPhoneNumber()
    {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        PhoneNumber = phoneNumber;
    }

    public boolean isSendDeliveryEmailAlert()
    {
        return SendDeliveryEmailAlert;
    }

    public void setSendDeliveryEmailAlert(boolean sendDeliveryEmailAlert)
    {
        SendDeliveryEmailAlert = sendDeliveryEmailAlert;
    }

    public boolean isSendDeliveryTextAlert()
    {
        return SendDeliveryTextAlert;
    }

    public void setSendDeliveryTextAlert(boolean sendDeliveryTextAlert)
    {
        SendDeliveryTextAlert = sendDeliveryTextAlert;
    }

    public boolean isSendViewEmailAlert()
    {
        return SendViewEmailAlert;
    }

    public void setSendViewEmailAlert(boolean sendViewEmailAlert)
    {
        SendViewEmailAlert = sendViewEmailAlert;
    }

    public boolean isSendViewTextAlert()
    {
        return SendViewTextAlert;
    }

    public void setSendViewTextAlert(boolean sendViewTextAlert)
    {
        SendViewTextAlert = sendViewTextAlert;
    }

    public String getStateProvince()
    {
        return StateProvince;
    }

    public void setStateProvince(String stateProvince)
    {
        StateProvince = stateProvince;
    }

    public String getStreetAddress1()
    {
        return StreetAddress1;
    }

    public void setStreetAddress1(String streetAddress1)
    {
        StreetAddress1 = streetAddress1;
    }

    public String getStreetAddress2()
    {
        return StreetAddress2;
    }

    public void setStreetAddress2(String streetAddress2)
    {
        StreetAddress2 = streetAddress2;
    }

    public boolean isSuppressReceiptEmail()
    {
        return SuppressReceiptEmail;
    }

    public void setSuppressReceiptEmail(boolean suppressReceiptEmail)
    {
        SuppressReceiptEmail = suppressReceiptEmail;
    }

    public String getZipPostalCode()
    {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode)
    {
        ZipPostalCode = zipPostalCode;
    }
}
