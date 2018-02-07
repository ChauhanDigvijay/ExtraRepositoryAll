package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 06/08/15.
 */
public class InCommSubmitPayment
{
    private double Amount;
    private String City;
    private String Country;
    private int CreditCardExpirationMonth;
    private int CreditCardExpirationYear;
    private String CreditCardNumber;
    private String CreditCardVerificationCode;
    private String FirstName;
    private String LastName;
    private int PaymentAccountId; //The optional UserPaymentAccount to use for paying for an Order if a User has stored their credit card info with their User account. Use this to create a new Order charging their payment account. The SubmitOrder request must be authenticated using the User credentials of the associated User.
    private boolean PaymentReceived;//If true, marks this order as paid at time of submission. No payment is taken and approval is not required.
    private String StateProvince;
    private String StreetAddress1;
    private String StreetAddress2;
    private String VestaOrgId;
    private String VestaWebSessionId;
    private String ZipPostalCode;
    private InCommOrderPaymentMobileDevice MobileDevice;
    private String CreditCardType;
    private String OrderPaymentMethod;

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

    public String getZipPostalCode()
    {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode)
    {
        ZipPostalCode = zipPostalCode;
    }

    public double getAmount()
    {
        return Amount;
    }

    public void setAmount(double amount)
    {
        Amount = amount;
    }

    public String getCity()
    {
        return City;
    }

    public void setCity(String city)
    {
        City = city;
    }

    public String getCountry()
    {
        return Country;
    }

    public void setCountry(String country)
    {
        Country = country;
    }

    public int getCreditCardExpirationMonth()
    {
        return CreditCardExpirationMonth;
    }

    public void setCreditCardExpirationMonth(int creditCardExpirationMonth)
    {
        CreditCardExpirationMonth = creditCardExpirationMonth;
    }

    public int getCreditCardExpirationYear()
    {
        return CreditCardExpirationYear;
    }

    public void setCreditCardExpirationYear(int creditCardExpirationYear)
    {
        CreditCardExpirationYear = creditCardExpirationYear;
    }

    public String getCreditCardNumber()
    {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber)
    {
        CreditCardNumber = creditCardNumber;
    }

    public String getCreditCardVerificationCode()
    {
        return CreditCardVerificationCode;
    }

    public void setCreditCardVerificationCode(String creditCardVerificationCode)
    {
        CreditCardVerificationCode = creditCardVerificationCode;
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

    public int getPaymentAccountId()
    {
        return PaymentAccountId;
    }

    public void setPaymentAccountId(int paymentAccountId)
    {
        PaymentAccountId = paymentAccountId;
    }

    public boolean isPaymentReceived()
    {
        return PaymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived)
    {
        PaymentReceived = paymentReceived;
    }

    public String getStateProvince()
    {
        return StateProvince;
    }

    public void setStateProvince(String stateProvince)
    {
        StateProvince = stateProvince;
    }

    public String getVestaOrgId()
    {
        return VestaOrgId;
    }

    public void setVestaOrgId(String vestaOrgId)
    {
        VestaOrgId = vestaOrgId;
    }

    public String getVestaWebSessionId()
    {
        return VestaWebSessionId;
    }

    public void setVestaWebSessionId(String vestaWebSessionId)
    {
        VestaWebSessionId = vestaWebSessionId;
    }

    public InCommOrderPaymentMobileDevice getMobileDevice()
    {
        return MobileDevice;
    }

    public void setMobileDevice(InCommOrderPaymentMobileDevice mobileDevice)
    {
        MobileDevice = mobileDevice;
    }

    public String getCreditCardType()
    {
        return CreditCardType;
    }

    public void setCreditCardType(String creditCardType)
    {
        CreditCardType = creditCardType;
    }

    public String getOrderPaymentMethod()
    {
        return OrderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod)
    {
        OrderPaymentMethod = orderPaymentMethod;
    }
}
