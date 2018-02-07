package com.wearehathway.apps.incomm.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by Nauman Afzaal on 06/08/15.
 */
public class InCommOrderRecipientDetails
{
    private String Id;
    private String City;
    private String CompanyName;
    private String Country;
    private Date DeliverOn;
    private String DeliveryLanguage;
    private boolean DisableEmailDelivery; //Valid values are "true" and "false." If true, this recipient will NOT receive an email message notification for this order.
    private String EmailAddress;
    private String FirstName;
    private String LastName;
    private String MobilePhoneNumber;
    private String PhoneNumber;
    private String StateProvince;
    private String StreetAddress1;
    private String StreetAddress2;
    private int UserId;
    private String ZipPostalCode;
    private List<InCommOrderItem> Items	;
    private String ShippingMethod;

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

    public Date getDeliverOn()
    {
        return DeliverOn;
    }

    public void setDeliverOn(Date deliverOn)
    {
        DeliverOn = deliverOn;
    }

    public String getDeliveryLanguage()
    {
        return DeliveryLanguage;
    }

    public void setDeliveryLanguage(String deliveryLanguage)
    {
        DeliveryLanguage = deliveryLanguage;
    }

    public boolean isDisableEmailDelivery()
    {
        return DisableEmailDelivery;
    }

    public void setDisableEmailDelivery(boolean disableEmailDelivery)
    {
        DisableEmailDelivery = disableEmailDelivery;
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

    public int getUserId()
    {
        return UserId;
    }

    public void setUserId(int userId)
    {
        UserId = userId;
    }

    public String getZipPostalCode()
    {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode)
    {
        ZipPostalCode = zipPostalCode;
    }

    public List<InCommOrderItem> getItems()
    {
        return Items;
    }

    public void setItems(List<InCommOrderItem> items)
    {
        Items = items;
    }

    public String getShippingMethod()
    {
        return ShippingMethod;
    }

    public void setShippingMethod(String shippingMethod)
    {
        ShippingMethod = shippingMethod;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }
}
