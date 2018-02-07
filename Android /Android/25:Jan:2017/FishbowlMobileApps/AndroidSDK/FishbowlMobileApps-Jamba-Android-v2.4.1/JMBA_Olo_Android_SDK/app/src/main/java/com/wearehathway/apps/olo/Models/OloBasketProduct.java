package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketProduct
{
    private int id;
    private int productId;
    private String name;
    private String options;
    private int quantity;
    private float basecost;//Unit cost of the product without any modifiers,
    private float totalcost;//Line cost inclusive of quantity and modifiers,
    private String specialinstructions;
    private ArrayList<OloBasketChoice> choices;

    public int getId()
    {
        return id;
    }

    public int getProductId()
    {
        return productId;
    }

    public String getName()
    {
        return name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public float getBaseCost()
    {
        return basecost;
    }

    public float getTotalCost()
    {
        return totalcost;
    }

    public String getSpecialInstructions()
    {
        return specialinstructions;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setBasecost(float basecost)
    {
        this.basecost = basecost;
    }

    public void setTotalcost(float totalcost)
    {
        this.totalcost = totalcost;
    }

    public void setSpecialinstructions(String specialinstructions)
    {
        this.specialinstructions = specialinstructions;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public ArrayList<OloBasketChoice> getChoices()
    {
        return choices;
    }

    public void setChoices(ArrayList<OloBasketChoice> choices)
    {
        this.choices = choices;
    }
}
