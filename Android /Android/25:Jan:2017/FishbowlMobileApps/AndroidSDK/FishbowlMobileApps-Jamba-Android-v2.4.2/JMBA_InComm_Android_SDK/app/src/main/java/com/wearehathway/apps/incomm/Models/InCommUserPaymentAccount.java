package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 18/08/15.
 */
public class InCommUserPaymentAccount
{
    private String City;
    private String ClientIpAddress;
    private String Country;
    private int CreditCardExpirationMonth;
    private int CreditCardExpirationYear;
    private String CreditCardNumber;
    private String CreditCardTypeCode;
    private String CreditCardVerificationCode;
    private String FirstName;
    private int Id;
    private String LastName;
    private String MiddleName;
    private String Name;
    private String PaymentGatewayToken;
    private String StateProvince;
    private String StreetAddress1;
    private String StreetAddress2;
    private int UserId;
    private String ZipPostalCode;


    public InCommUserPaymentAccount(InCommSubmitPayment inCommSubmitPayment,String userId) {
        Country = inCommSubmitPayment.getCountry();
        City = inCommSubmitPayment.getCity();
        ZipPostalCode = inCommSubmitPayment.getZipPostalCode();
        StreetAddress2 = inCommSubmitPayment.getStreetAddress2();
        StreetAddress1 = inCommSubmitPayment.getStreetAddress1();
        StateProvince = inCommSubmitPayment.getStateProvince();
        CreditCardExpirationMonth = inCommSubmitPayment.getCreditCardExpirationMonth();
        CreditCardExpirationYear = inCommSubmitPayment.getCreditCardExpirationYear();
        CreditCardNumber = inCommSubmitPayment.getCreditCardNumber();
        CreditCardTypeCode = inCommSubmitPayment.getCreditCardType();
        CreditCardVerificationCode = inCommSubmitPayment.getCreditCardVerificationCode();
        FirstName = inCommSubmitPayment.getFirstName();
        LastName = inCommSubmitPayment.getLastName();
        UserId = Integer.parseInt(userId);
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getClientIpAddress() {
        return ClientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        ClientIpAddress = clientIpAddress;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public int getCreditCardExpirationMonth() {
        return CreditCardExpirationMonth;
    }

    public void setCreditCardExpirationMonth(int creditCardExpirationMonth) {
        CreditCardExpirationMonth = creditCardExpirationMonth;
    }

    public int getCreditCardExpirationYear() {
        return CreditCardExpirationYear;
    }

    public void setCreditCardExpirationYear(int creditCardExpirationYear) {
        CreditCardExpirationYear = creditCardExpirationYear;
    }

    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String getCreditCardTypeCode() {
        return CreditCardTypeCode;
    }

    public void setCreditCardTypeCode(String creditCardTypeCode) {
        CreditCardTypeCode = creditCardTypeCode;
    }

    public String getCreditCardVerificationCode() {
        return CreditCardVerificationCode;
    }

    public void setCreditCardVerificationCode(String creditCardVerificationCode) {
        CreditCardVerificationCode = creditCardVerificationCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPaymentGatewayToken() {
        return PaymentGatewayToken;
    }

    public void setPaymentGatewayToken(String paymentGatewayToken) {
        PaymentGatewayToken = paymentGatewayToken;
    }

    public String getStateProvince() {
        return StateProvince;
    }

    public void setStateProvince(String stateProvince) {
        StateProvince = stateProvince;
    }

    public String getStreetAddress1() {
        return StreetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        StreetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return StreetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        StreetAddress2 = streetAddress2;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getZipPostalCode() {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        ZipPostalCode = zipPostalCode;
    }
}
