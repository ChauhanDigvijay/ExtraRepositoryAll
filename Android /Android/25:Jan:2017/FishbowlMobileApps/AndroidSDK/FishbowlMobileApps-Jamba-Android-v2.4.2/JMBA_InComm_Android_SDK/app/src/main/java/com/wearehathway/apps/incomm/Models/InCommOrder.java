package com.wearehathway.apps.incomm.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommOrder
{
    private String ApplicationCode;
    private double CardBalance;
    private Date CardBalanceDate;
    private String ClientIpAddress;
    private Date CreatedOn;
    private String CustomerContactNumber;
    private String Description;
    private double DiscountAmount;
    private int DiscountId;
    private String Id;
    private boolean IsTestMode;
    private String OrderType;
    private String PaymentReferenceId;
    private String PurchaseOrderFilename;
    private String PurchaseOrderNumber;
    private String ResultDescription;
    private String TrackingNumber;
    private int TransactionId;
    //ValidationResults	ValidationResult Collection Validation results if the SubmitOrderResponse fails.
    private List<InCommOrderAmountModifier> AmountModifiers;
    private List<InCommSubmittedOrderItemGiftCards> SubmittedOrderItemGiftCards;
    private InCommOrderPayment Payment;
    private InCommOrderPurchaser Purchaser;
    private List<InCommOrderRecipientDetails> Recipients;
    private String Result; // Values are from enum OrderStatus.

    public String getApplicationCode()
    {
        return ApplicationCode;
    }

    public void setApplicationCode(String applicationCode)
    {
        ApplicationCode = applicationCode;
    }

    public double getCardBalance()
    {
        return CardBalance;
    }

    public void setCardBalance(double cardBalance)
    {
        CardBalance = cardBalance;
    }

    public Date getCardBalanceDate()
    {
        return CardBalanceDate;
    }

    public void setCardBalanceDate(Date cardBalanceDate)
    {
        CardBalanceDate = cardBalanceDate;
    }

    public String getClientIpAddress()
    {
        return ClientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress)
    {
        ClientIpAddress = clientIpAddress;
    }

    public Date getCreatedOn()
    {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        CreatedOn = createdOn;
    }

    public String getCustomerContactNumber()
    {
        return CustomerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber)
    {
        CustomerContactNumber = customerContactNumber;
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

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public boolean isTestMode()
    {
        return IsTestMode;
    }

    public void setIsTestMode(boolean isTestMode)
    {
        IsTestMode = isTestMode;
    }

    public String getOrderType()
    {
        return OrderType;
    }

    public void setOrderType(String orderType)
    {
        OrderType = orderType;
    }

    public String getPaymentReferenceId()
    {
        return PaymentReferenceId;
    }

    public void setPaymentReferenceId(String paymentReferenceId)
    {
        PaymentReferenceId = paymentReferenceId;
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

    public String getResultDescription()
    {
        return ResultDescription;
    }

    public void setResultDescription(String resultDescription)
    {
        ResultDescription = resultDescription;
    }

    public String getTrackingNumber()
    {
        return TrackingNumber;
    }

    public void setTrackingNumber(String trackingNumber)
    {
        TrackingNumber = trackingNumber;
    }

    public int getTransactionId()
    {
        return TransactionId;
    }

    public void setTransactionId(int transactionId)
    {
        TransactionId = transactionId;
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

    public String getResult()
    {
        return Result;
    }

    public void setResult(String result)
    {
        Result = result;
    }

    public List<InCommSubmittedOrderItemGiftCards> getSubmittedOrderItemGiftCards()
    {
        return SubmittedOrderItemGiftCards;
    }

    public void setSubmittedOrderItemGiftCards(List<InCommSubmittedOrderItemGiftCards> submittedOrderItemGiftCards)
    {
        SubmittedOrderItemGiftCards = submittedOrderItemGiftCards;
    }

    public InCommOrderPayment getPayment()
    {
        return Payment;
    }

    public void setPayment(InCommOrderPayment payment)
    {
        Payment = payment;
    }

    public List<InCommOrderAmountModifier> getAmountModifiers()
    {
        return AmountModifiers;
    }

    public void setAmountModifiers(List<InCommOrderAmountModifier> amountModifiers)
    {
        AmountModifiers = amountModifiers;
    }
}
