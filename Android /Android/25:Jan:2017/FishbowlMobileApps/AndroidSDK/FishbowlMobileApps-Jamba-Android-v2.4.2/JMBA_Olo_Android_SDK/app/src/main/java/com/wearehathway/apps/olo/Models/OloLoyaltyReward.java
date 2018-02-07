package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public class OloLoyaltyReward
{
    private String type;
    private boolean applied;
    private String label;
    private double value;
    private String reference;
    private int membershipid;
    private int rewardid;

    public String getType()
    {
        return type;
    }

    public boolean isApplied()
    {
        return applied;
    }

    public String getLabel()
    {
        return label;
    }

    public double getValue()
    {
        return value;
    }

    public String getReference()
    {
        return reference;
    }

    public int getMembershipid()
    {
        return membershipid;
    }

    public int getRewardid()
    {
        return rewardid;
    }
}
