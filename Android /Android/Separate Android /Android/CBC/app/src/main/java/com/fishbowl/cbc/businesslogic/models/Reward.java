package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloLoyaltyReward;

/**
 * Created by VT027 on 5/22/2017.
 */

public class Reward {
    private String type;
    private String rewardTitle;
    private String description;
    int quantity;

    // Olo specific fields
    private int rewardId;
    private String reference;
    private boolean applied;
    private int membershipId;

    public Reward(OloLoyaltyReward oloLoyaltyReward)
    {
        rewardId = oloLoyaltyReward.getRewardid();
        rewardTitle = oloLoyaltyReward.getLabel();
        description = "";
        type = oloLoyaltyReward.getType();
        applied = oloLoyaltyReward.isApplied();
        membershipId = oloLoyaltyReward.getMembershipid();
        reference = oloLoyaltyReward.getReference();
    }

    public String getDescription()
    {
        return description;
    }

    public String getRewardTitle()
    {
        return rewardTitle;
    }

    public String getType()
    {
        return type;
    }

    public int getRewardId()
    {
        return rewardId;
    }

    public String getReference()
    {
        return reference;
    }

    public boolean isApplied()
    {
        return applied;
    }

    public int getMembershipId()
    {
        return membershipId;
    }
}
