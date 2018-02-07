package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public class OloLoyaltyScheme
{
    private int id;
    private String label; // Consumer-friendly identifier describing the membership # field,
    private String name;// Name of the loyalty program,
    private OloLoyaltyMembership membership;// (OloLoyaltyMembership, optional): membership details of the current user

    public int getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }

    public String getName()
    {
        return name;
    }

    public OloLoyaltyMembership getMembership()
    {
        return membership;
    }
}
