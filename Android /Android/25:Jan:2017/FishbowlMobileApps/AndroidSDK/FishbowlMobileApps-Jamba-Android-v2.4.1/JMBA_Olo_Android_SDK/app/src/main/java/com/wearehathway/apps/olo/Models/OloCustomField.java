package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloCustomField
{
    private int id;
    private boolean isrequired;
    private String label;
    private int maxlength;

    public int getId()
    {
        return id;
    }

    public boolean isIsrequired()
    {
        return isrequired;
    }

    public String getLabel()
    {
        return label;
    }

    public int getMaxlength()
    {
        return maxlength;
    }
}
