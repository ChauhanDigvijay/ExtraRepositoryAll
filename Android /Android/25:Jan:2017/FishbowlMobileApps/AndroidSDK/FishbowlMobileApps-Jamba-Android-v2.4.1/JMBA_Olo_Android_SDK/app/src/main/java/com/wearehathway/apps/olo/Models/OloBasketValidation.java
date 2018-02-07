package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketValidation
{
    private String basketid;
    private float tax;
    private float subtotal;
    private float total;
    private ArrayList<OloUpsellGroup> upsellgroups;

    public String getBasketid()
    {
        return basketid;
    }

    public float getTax()
    {
        return tax;
    }

    public float getSubtotal()
    {
        return subtotal;
    }

    public float getTotal()
    {
        return total;
    }

    public ArrayList<OloUpsellGroup> getUpsellgroups() {
        return upsellgroups;
    }

    public void setUpsellgroups(ArrayList<OloUpsellGroup> upsellgroups) {
        this.upsellgroups = upsellgroups;
    }
}
