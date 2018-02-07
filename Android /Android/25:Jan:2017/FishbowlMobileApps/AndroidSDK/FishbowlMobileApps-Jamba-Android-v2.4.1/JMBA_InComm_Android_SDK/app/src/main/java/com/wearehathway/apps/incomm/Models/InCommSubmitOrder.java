package com.wearehathway.apps.incomm.Models;

import java.util.List;

/**
 * Created by Nauman Afzaal on 06/08/15.
 */
public class InCommSubmitOrder
{
    private boolean DelayActivation; //If true, PlatformServices will prevent automatic activation and require manual activation later.
    private String Description;
    private double DiscountAmount;
    private int DiscountId;
    private boolean FulfillImmediately;
    private String Id;
    private String PurchaseOrderFilename;
    private String PurchaseOrderNumber;
    private InCommOrderPurchaser Purchaser;
    private List<InCommOrderRecipientDetails> Recipients;
    private InCommSubmitPayment Payment;

    public boolean isDelayActivation()
    {
        return DelayActivation;
    }

    public void setDelayActivation(boolean delayActivation)
    {
        DelayActivation = delayActivation;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

    public double getDiscountAmount()
    {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount)
    {
        DiscountAmount = discountAmount;
    }

    public int getDiscountId()
    {
        return DiscountId;
    }

    public void setDiscountId(int discountId)
    {
        DiscountId = discountId;
    }

    public boolean isFulfillImmediately()
    {
        return FulfillImmediately;
    }

    public void setFulfillImmediately(boolean fulfillImmediately)
    {
        FulfillImmediately = fulfillImmediately;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getPurchaseOrderFilename()
    {
        return PurchaseOrderFilename;
    }

    public void setPurchaseOrderFilename(String purchaseOrderFilename)
    {
        PurchaseOrderFilename = purchaseOrderFilename;
    }

    public String getPurchaseOrderNumber()
    {
        return PurchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        PurchaseOrderNumber = purchaseOrderNumber;
    }

    public InCommOrderPurchaser getPurchaser()
    {
        return Purchaser;
    }

    public void setPurchaser(InCommOrderPurchaser purchaser)
    {
        Purchaser = purchaser;
    }

    public List<InCommOrderRecipientDetails> getRecipients()
    {
        return Recipients;
    }

    public void setRecipients(List<InCommOrderRecipientDetails> recipients)
    {
        Recipients = recipients;
    }

    public InCommSubmitPayment getPayment()
    {
        return Payment;
    }

    public void setPayment(InCommSubmitPayment payment)
    {
        Payment = payment;
    }

    public  void resetPurchaser(){
        Purchaser=null;
    }
}
