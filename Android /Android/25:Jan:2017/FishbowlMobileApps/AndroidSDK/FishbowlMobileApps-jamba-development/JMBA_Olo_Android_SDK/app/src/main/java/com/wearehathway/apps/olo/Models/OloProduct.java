package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public class OloProduct
{
    private int id;
    private int chainproductid;
    private String name;
    private String description;
    private float cost;
    private String basecalories;
    private String maxcalories;
    private String displayid;
    private float starthour;
    private float endhour;
    private String imagefilename;

    public int getId()
    {
        return id;
    }

    public int getChainproductid()
    {
        return chainproductid;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public float getCost()
    {
        return cost;
    }

    public String getBasecalories()
    {
        return basecalories;
    }

    public String getMaxcalories()
    {
        return maxcalories;
    }

    public String getDisplayid()
    {
        return displayid;
    }

    public float getStarthour()
    {
        return starthour;
    }

    public float getEndhour()
    {
        return endhour;
    }

    public String getImagefilename()
    {
        return imagefilename;
    }
}
