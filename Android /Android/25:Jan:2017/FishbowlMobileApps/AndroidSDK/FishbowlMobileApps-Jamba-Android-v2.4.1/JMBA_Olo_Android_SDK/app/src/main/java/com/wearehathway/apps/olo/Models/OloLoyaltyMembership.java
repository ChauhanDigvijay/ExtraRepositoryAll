package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public class OloLoyaltyMembership
{
    private int id;//Unique identifier for the loyalty scheme membership,
    private String description; //Consumer-friendly description of the membership (e.g. MyLoyalty Card x-1234),
    private String membershipnumber;// The full membership number

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public String getMembershipnumber()
    {
        return membershipnumber;
    }
}
