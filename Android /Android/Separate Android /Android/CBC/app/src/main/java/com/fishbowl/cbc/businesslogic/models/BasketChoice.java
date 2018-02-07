package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloBasketChoice;

import java.io.Serializable;

/**
 * Created by VT027 on 5/20/2017.
 */

public class BasketChoice implements Serializable {

    private int id;
    private int optionid;
    private String name;
    private float cost;
    private int quantity;

    public BasketChoice() {

    }

    public BasketChoice(OloBasketChoice oloBasketChoice) {
        id = oloBasketChoice.getId();
        optionid = oloBasketChoice.getOptionId();
        name = oloBasketChoice.getName();
        cost = oloBasketChoice.getCost();
        quantity = oloBasketChoice.getQuantity();
    }

    public int getId() {
        return id;
    }

    public int getOptionid() {
        return optionid;
    }

    public String getName() {
        return name;
    }

    public float getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
