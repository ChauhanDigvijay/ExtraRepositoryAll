package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 17/08/15.
 */
public class InCommReloadOrder
{
    private double Amount;
    private String BrandId;
    private int CardId;
    private String CardPin;
    private boolean IsTestMode;
    private InCommOrderPurchaser Purchaser;
    private InCommSubmitPayment Payment;

    public double getAmount()
    {
        return Amount;
    }

    public void setAmount(double amount)
    {
        Amount = amount;
    }

    public String getBrandId()
    {
        return BrandId;
    }

    public void setBrandId(String brandId)
    {
        BrandId = brandId;
    }

    public int getCardId()
    {
        return CardId;
    }

    public void setCardId(int cardId)
    {
        CardId = cardId;
    }

    public String getCardPin()
    {
        return CardPin;
    }

    public void setCardPin(String cardPin)
    {
        CardPin = cardPin;
    }

    public boolean isTestMode()
    {
        return IsTestMode;
    }

    public void setIsTestMode(boolean isTestMode)
    {
        IsTestMode = isTestMode;
    }

    public InCommOrderPurchaser getPurchaser()
    {
        return Purchaser;
    }

    public void setPurchaser(InCommOrderPurchaser purchaser)
    {
        Purchaser = purchaser;
    }
    public  void resetPurchaser(){
        Purchaser=null;
    }

    public InCommSubmitPayment getPayment()
    {
        return Payment;
    }

    public void setPayment(InCommSubmitPayment payment)
    {
        Payment = payment;
    }
}
