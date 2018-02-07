package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public class OloBillingScheme
{
    private int id;
    private String name;
    private String type;
    private ArrayList<OloBillingAccount> accounts;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public ArrayList<OloBillingAccount> getAccounts()
    {
        return accounts;
    }
}
