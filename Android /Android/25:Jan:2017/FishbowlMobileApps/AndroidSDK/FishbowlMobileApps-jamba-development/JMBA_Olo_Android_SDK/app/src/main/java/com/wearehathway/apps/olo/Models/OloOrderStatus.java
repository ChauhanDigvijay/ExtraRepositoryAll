package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloOrderStatus
{
    private String id;
    private String timeplaced;
    private String vendorname;
    private String status;
    private float total;
    private float subtotal;
    private float salestax;
    private String orderref;
    private String readytime;
    private String vendorextref;
    private String deliverymode;
    private ArrayList<OloOrdersSatusProduct> products;
    private boolean iseditable;

    public String getId()
    {
        return id;
    }

    public String getTimeplaced()
    {
        return timeplaced;
    }

    public String getVendorname()
    {
        return vendorname;
    }

    public String getStatus()
    {
        return status;
    }

    public float getTotal()
    {
        return total;
    }

    public float getSubtotal()
    {
        return subtotal;
    }

    public float getSalestax()
    {
        return salestax;
    }

    public String getOrderref()
    {
        return orderref;
    }

    public String getReadytime()
    {
        return readytime;
    }

    public String getVendorextref()
    {
        return vendorextref;
    }

    public String getDeliverymode()
    {
        return deliverymode;
    }

    public ArrayList<OloOrdersSatusProduct> getProducts()
    {
        return products;
    }

    public boolean iseditable() {
        return iseditable;
    }

    public void setIseditable(boolean iseditable) {
        this.iseditable = iseditable;
    }
}
