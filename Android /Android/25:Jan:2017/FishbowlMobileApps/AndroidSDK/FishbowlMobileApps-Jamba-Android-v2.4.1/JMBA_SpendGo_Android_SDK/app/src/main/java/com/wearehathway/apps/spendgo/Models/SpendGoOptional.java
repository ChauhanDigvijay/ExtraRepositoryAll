package com.wearehathway.apps.spendgo.Models;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoOptional
{
    private String name;
    private String value;
    public SpendGoOptional(String name, String value)
    {
        this.setName(name);
        this.setValue(value);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
