package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketChoice
{
    private int id;
    private int optionid;
    private int quantity;
    private String name;
    private int metric; //Display order metric for choices,
    private int indent; //Display indent-level for nested choices (e.g. Light/Regular/Heavy),
    private float cost;
    private ArrayList<OloChoiceCustomFieldValue> customfields; //Any custom fields that were applied to this choice

    public int getId()
    {
        return id;
    }

    public int getOptionId()
    {
        return optionid;
    }

    public String getName()
    {
        return name;
    }

    public int getMetric()
    {
        return metric;
    }

    public int getIndent()
    {
        return indent;
    }

    public float getCost()
    {
        return cost;
    }


    public void setId(int id)
    {
        this.id = id;
    }

    public void setOptionid(int optionid)
    {
        this.optionid = optionid;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMetric(int metric)
    {
        this.metric = metric;
    }

    public void setIndent(int indent)
    {
        this.indent = indent;
    }

    public void setCost(float cost)
    {
        this.cost = cost;
    }

    public ArrayList<OloChoiceCustomFieldValue> getCustomfields()
    {
        return customfields;
    }

    public void setCustomfields(ArrayList<OloChoiceCustomFieldValue> customfields)
    {
        this.customfields = customfields;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
