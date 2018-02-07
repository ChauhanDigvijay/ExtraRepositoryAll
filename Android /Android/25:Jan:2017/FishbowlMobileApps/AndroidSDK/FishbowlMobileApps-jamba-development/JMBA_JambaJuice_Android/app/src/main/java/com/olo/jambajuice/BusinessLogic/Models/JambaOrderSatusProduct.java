package com.olo.jambajuice.BusinessLogic.Models;

import java.util.List;

/**
 * Created by kalai on 22-04-2016.
 */
public class JambaOrderSatusProduct {
    private String name;
    private int quantity;
    private float totalcost;
    private String specialinstructions;
    private List<BasketChoice> choices;

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

    public List<BasketChoice> getChoices() {
        return choices;
    }
}
