package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloFave
{
    private int id;
    private String name;
    private int vendorid;
    private String vendorname;
    private boolean disabled; //if true, this fave cannot be ordered because the menu has changed
    private boolean online;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getVendorId()
    {
        return vendorid;
    }

    public String getVendorName()
    {
        return vendorname;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public boolean isOnline()
    {
        return online;
    }
}
