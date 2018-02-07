package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloOption
{
    private int id;
    private String name;
    private boolean isdefault;
    private float cost;
    private boolean children;
    private ArrayList<OloModifier> modifiers;
    private ArrayList<OloCustomField> fields;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean isIsdefault()
    {
        return isdefault;
    }

    public float getCost()
    {
        return cost;
    }

    public boolean isChildren()
    {
        return children;
    }

    public ArrayList<OloModifier> getModifiers()
    {
        return modifiers;
    }

    public ArrayList<OloCustomField> getFields()
    {
        return fields;
    }
}
