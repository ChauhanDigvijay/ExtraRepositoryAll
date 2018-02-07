package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloLoyaltyReward;
import com.wearehathway.apps.spendgo.Models.SpendGoReward;

import java.io.Serializable;

/**
 * Created by Ihsanulhaq on 5/27/2015.
 */
public class Reward implements Serializable
{
    // Shared fields (Olo & SpendGo)
    private String type;
    private String rewardTitle;
    private String description;
    int quantity;

    // Olo specific fields
    private int rewardId;
    private String reference;
    private boolean applied;
    private int membershipId;

    public Reward(SpendGoReward spendGoReward)
    {
        type = spendGoReward.getType();
        rewardTitle = spendGoReward.getReward_title();
        description = spendGoReward.getDescription();
        try
        {
            quantity = Integer.parseInt(spendGoReward.getQuantity());
        } catch (Exception e)
        {
        }

        applied = false;
    }

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
