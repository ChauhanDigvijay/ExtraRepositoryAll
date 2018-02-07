package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by Nauman Afzaal on 18/08/15.
 */
public class InCommOrderPayment
{
    private double Amount;
    private String City;
    private String Country;
    private int CreditCardExpirationMonth;
    private int CreditCardExpirationYear;
    private String FirstName;
    private String LastFourDigitsOfCreditCardNumber;
    private String LastName;
    private String PartnerTransactionId;
    private int PaymentAccountId;
    private String PaymentGatewayAvsCode;
    private String PaymentGatewayBankApprovalCode;
    private String PaymentGatewayBankTransactionId;
    private int PaymentGatewayId;
    private String PaymentGatewayIsoCode;
    private String PaymentGatewayOrderId;
    private String PaymentGatewayReferenceId;
    private String PaymentGatewayToken;
    private String StateProvince;
    private String StreetAddress1;
    private String StreetAddress2;
    private Date TimeStamp;
    private String ZipPostalCode;
    private InCommOrderPaymentMobileDevice MobileDevice;
    private String CreditCardType; //Enum CreditCardType
    private String OrderPaymentMethod; //Enum OrderPaymentMethod
    private String PaymentStatus; //Enum TransactionPaymentStatus

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastFourDigitsOfCreditCardNumber() {
        return LastFourDigitsOfCreditCardNumber;
    }

    public void setLastFourDigitsOfCreditCardNumber(String lastFourDigitsOfCreditCardNumber) {
        LastFourDigitsOfCreditCardNumber = lastFourDigitsOfCreditCardNumber;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPartnerTransactionId() {
        return PartnerTransactionId;
    }

    public void setPartnerTransactionId(String partnerTransactionId) {
        PartnerTransactionId = partnerTransactionId;
    }

    public int getPaymentAccountId() {
        return PaymentAccountId;
    }

    public void setPaymentAccountId(int paymentAccountId) {
        PaymentAccountId = paymentAccountId;
    }

    public String getPaymentGatewayAvsCode() {
        return PaymentGatewayAvsCode;
    }

    public void setPaymentGatewayAvsCode(String paymentGatewayAvsCode) {
        PaymentGatewayAvsCode = paymentGatewayAvsCode;
    }

    public String getPaymentGatewayBankApprovalCode() {
        return PaymentGatewayBankApprovalCode;
    }

    public void setPaymentGatewayBankApprovalCode(String paymentGatewayBankApprovalCode) {
        PaymentGatewayBankApprovalCode = paymentGatewayBankApprovalCode;
    }

    public String getPaymentGatewayBankTransactionId() {
        return PaymentGatewayBankTransactionId;
    }

    public void setPaymentGatewayBankTransactionId(String paymentGatewayBankTransactionId) {
        PaymentGatewayBankTransactionId = paymentGatewayBankTransactionId;
    }

    public int getPaymentGatewayId() {
        return PaymentGatewayId;
    }

    public void setPaymentGatewayId(int paymentGatewayId) {
        PaymentGatewayId = paymentGatewayId;
    }

    public String getPaymentGatewayIsoCode() {
        return PaymentGatewayIsoCode;
    }

    public void setPaymentGatewayIsoCode(String paymentGatewayIsoCode) {
        PaymentGatewayIsoCode = paymentGatewayIsoCode;
    }

    public String getPaymentGatewayOrderId() {
        return PaymentGatewayOrderId;
    }

    public void setPaymentGatewayOrderId(String paymentGatewayOrderId) {
        PaymentGatewayOrderId = paymentGatewayOrderId;
    }

    public String getPaymentGatewayReferenceId() {
        return PaymentGatewayReferenceId;
    }

    public void setPaymentGatewayReferenceId(String paymentGatewayReferenceId) {
        PaymentGatewayReferenceId = paymentGatewayReferenceId;
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

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getZipPostalCode() {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        ZipPostalCode = zipPostalCode;
    }

    public InCommOrderPaymentMobileDevice getMobileDevice() {
        return MobileDevice;
    }

    public void setMobileDevice(InCommOrderPaymentMobileDevice mobileDevice) {
        MobileDevice = mobileDevice;
    }

    public String getCreditCardType() {
        return CreditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        CreditCardType = creditCardType;
    }

    public String getOrderPaymentMethod() {
        return OrderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        OrderPaymentMethod = orderPaymentMethod;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
