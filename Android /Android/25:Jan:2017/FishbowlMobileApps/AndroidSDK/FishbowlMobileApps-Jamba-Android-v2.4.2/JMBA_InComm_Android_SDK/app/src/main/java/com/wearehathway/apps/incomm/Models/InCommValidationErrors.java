package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 06/08/15.
 */
public class InCommValidationErrors {
    private String[] Payment;
    private String[] CreditCardNumber;
    private String[] OrderPaymentAmount;
    private String[] FieldNotSpecified;
    private String[] CreditCardExpirationYear;
    private String[] CreditCardExpirationMonth;


    public String[] getPayment() {
        return Payment;
    }

    public void setPayment(String[] payment) {
        Payment = payment;
    }

    public String[] getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String[] creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String[] getOrderPaymentAmount() {
        return OrderPaymentAmount;
    }

    public void setOrderPaymentAmount(String[] orderPaymentAmount) {
        OrderPaymentAmount = orderPaymentAmount;
    }

    public String[] getFieldNotSpecified() {
        return FieldNotSpecified;
    }

    public void setFieldNotSpecified(String[] fieldNotSpecified) {
        FieldNotSpecified = fieldNotSpecified;
    }

    public String[] getCreditCardExpirationYear() {
        return CreditCardExpirationYear;
    }

    public void setCreditCardExpirationYear(String[] creditCardExpirationYear) {
        CreditCardExpirationYear = creditCardExpirationYear;
    }

    public String[] getCreditCardExpirationMonth() {
        return CreditCardExpirationMonth;
    }

    public void setCreditCardExpirationMonth(String[] creditCardExpirationMonth) {
        CreditCardExpirationMonth = creditCardExpirationMonth;
    }
}
