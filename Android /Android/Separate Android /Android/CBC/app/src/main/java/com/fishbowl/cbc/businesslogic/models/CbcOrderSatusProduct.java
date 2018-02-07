package com.fishbowl.cbc.businesslogic.models;

import java.util.List;

/**
 * Created by VT027 on 5/20/2017.
 */

class CbcOrderSatusProduct {
    private String name;
    private int quantity;
    private float totalcost;
    private String specialinstructions;
    private List<BasketChoice> choices;

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalcost() {
        return totalcost;
    }

    public String getSpecialinstructions() {
        return specialinstructions;
    }

    public List<BasketChoice> getChoices() {
        return choices;
    }
}
