package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloBasketChoice;

import java.io.Serializable;

/**
 * Created by Nauman Afzaal on 29/05/15.
 */
public class BasketChoice implements Serializable
{

    private int id;
    private int optionid;
    private String name;
    private float cost;
    private int quantity;

    public BasketChoice(){

    }
    public BasketChoice(OloBasketChoice oloBasketChoice)
    {
        id = oloBasketChoice.getId();
        optionid = oloBasketChoice.getOptionId();
        name = oloBasketChoice.getName();
        cost = oloBasketChoice.getCost();
        quantity=oloBasketChoice.getQuantity();
    }

    public int getId()
    {
        return id;
    }

    public int getOptionid()
    {
        return optionid;
    }

    public String getName()
    {
        return name;
    }

    public float getCost()
    {
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
