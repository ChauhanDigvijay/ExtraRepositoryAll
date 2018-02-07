package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 18/08/15.
 */
public class InCommSubmittedOrderItemGiftCards
{
    private double Amount;
    private String BarcodeImageUrl;
    private String BarcodeValue;
    private String BrandId;
    private String BrandName;
    private String ExternalCertificateData;
    private int GiftCardId;
    private String GiftCardImageUrl;
    private String GiftCardNumber;
    private String GiftCardUrl;
    private String Pin;
    private String Token;
    private String GiftCardStatus; // Can have value from GiftCardStatus enum.

    public double getAmount()
    {
        return Amount;
    }

    public String getBarcodeImageUrl()
    {
        return BarcodeImageUrl;
    }

    public String getBrandId()
    {
        return BrandId;
    }

    public String getBrandName()
    {
        return BrandName;
    }

    public String getExternalCertificateData()
    {
        return ExternalCertificateData;
    }

    public int getGiftCardId()
    {
        return GiftCardId;
    }

    public String getGiftCardImageUrl()
    {
        return GiftCardImageUrl;
    }

    public String getGiftCardNumber()
    {
        return GiftCardNumber;
    }

    public String getGiftCardUrl()
    {
        return GiftCardUrl;
    }

    public String getPin()
    {
        return Pin;
    }

    public String getToken()
    {
        return Token;
    }

    public String getGiftCardStatus()
    {
        return GiftCardStatus;
    }

    public String getBarcodeValue()
    {
        return BarcodeValue;
    }
}
