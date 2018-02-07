package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloOrdersSatusProduct
{
    private String name;
    private int quantity;
    private float totalcost;
    private String specialinstructions;

    public String getName()
    {
        return name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public float getTotalcost()
    {
        return totalcost;
    }

    public String getSpecialinstructions()
    {
        return specialinstructions;
    }
}
