package com.fishbowl.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public class OloCategory
{
    private int id;
    private String name;
    private String description;
    private ArrayList<OloProduct> products;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public ArrayList<OloProduct> getProducts()
    {
        return products;
    }
}
