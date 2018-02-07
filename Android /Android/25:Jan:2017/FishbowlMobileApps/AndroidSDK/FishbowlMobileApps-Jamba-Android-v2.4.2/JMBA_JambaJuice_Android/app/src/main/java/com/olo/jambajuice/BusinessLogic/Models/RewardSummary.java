package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.spendgo.Models.SpendGoReward;
import com.wearehathway.apps.spendgo.Models.SpendGoRewardSummary;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 22/06/15.
 */
public class RewardSummary
{
    private int points;
    private int threshold;
    private int rewardCount;
    private ArrayList<Reward> rewardsList;

    public RewardSummary(SpendGoRewardSummary spendGoRewardSummary)
    {
        this.points = spendGoRewardSummary.getPoint_total();
        this.threshold = spendGoRewardSummary.getSpend_threshold();
        this.rewardCount = spendGoRewardSummary.getRewards_count();
        //this.rewardCount = spendGoRewardSummary.getRewards_list().size();
        rewardsList = new ArrayList<>();
        if (spendGoRewardSummary.getRewards_list() != null)
        {
            for (SpendGoReward spendGoReward : spendGoRewardSummary.getRewards_list())
            {
                Reward reward = new Reward(spendGoReward);
                getRewardsList().add(reward);
            }
        }
    }

    public int getPoints()
    {
        return points;
    }

    public int getThreshold()
    {
        return threshold;
    }

    public ArrayList<Reward> getRewardsList()
    {
        return rewardsList;
    }

    public int getRewardCount()
    {
        return rewardCount;
    }
}
