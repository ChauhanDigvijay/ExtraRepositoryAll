package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommBrandDeliveryMethod
{
    private boolean IsShippingCostPerItem;
    private double ShippingCost;
    private String ShippingMethod;
    private int SortOrder;

    public boolean isShippingCostPerItem()
    {
        return IsShippingCostPerItem;
    }

    public double getShippingCost()
    {
        return ShippingCost;
    }

    public String getShippingMethod()
    {
        return ShippingMethod;
    }

    public int getSortOrder()
    {
        return SortOrder;
    }
}
