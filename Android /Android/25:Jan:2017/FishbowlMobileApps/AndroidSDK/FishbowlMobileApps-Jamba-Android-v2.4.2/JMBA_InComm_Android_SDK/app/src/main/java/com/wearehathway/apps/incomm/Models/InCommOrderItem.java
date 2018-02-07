package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommOrderItem
{
    private String Id;
    private double Amount;
    private String BrandId;
    private String EmbossText;
    private int EmbossTextId;
    private Date ExpirationDate;
    private String ImageCode;
    private String MessageFrom;
    private String MessageText;
    private String MessageTo;
    private int ProductId;
    private String ProductName;
    private int Quantity;
    private Boolean HideAmount;

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

    public String getEmbossText()
    {
        return EmbossText;
    }

    public void setEmbossText(String embossText)
    {
        EmbossText = embossText;
    }

    public int getEmbossTextId()
    {
        return EmbossTextId;
    }

    public void setEmbossTextId(int embossTextId)
    {
        EmbossTextId = embossTextId;
    }

    public Date getExpirationDate()
    {
        return ExpirationDate;
    }

    public void setExpirationDate(Date expirationDate)
    {
        ExpirationDate = expirationDate;
    }

    public String getImageCode()
    {
        return ImageCode;
    }

    public void setImageCode(String imageCode)
    {
        ImageCode = imageCode;
    }

    public String getMessageFrom()
    {
        return MessageFrom;
    }

    public void setMessageFrom(String messageFrom)
    {
        MessageFrom = messageFrom;
    }

    public String getMessageText()
    {
        return MessageText;
    }

    public void setMessageText(String messageText)
    {
        MessageText = messageText;
    }

    public String getMessageTo()
    {
        return MessageTo;
    }

    public void setMessageTo(String messageTo)
    {
        MessageTo = messageTo;
    }

    public int getProductId()
    {
        return ProductId;
    }

    public void setProductId(int productId)
    {
        ProductId = productId;
    }

    public String getProductName()
    {
        return ProductName;
    }

    public void setProductName(String productName)
    {
        ProductName = productName;
    }

    public int getQuantity()
    {
        return Quantity;
    }

    public void setQuantity(int quantity)
    {
        Quantity = quantity;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public Boolean getHideAmount() {
        return HideAmount;
    }

    public void setHideAmount(Boolean hideAmount) {
        HideAmount = hideAmount;
    }
}
