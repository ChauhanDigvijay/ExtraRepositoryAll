package com.wearehathway.apps.spendgo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoRewardSummary {
    private int point_total;
    private int spend_threshold;
    private int rewards_count;
    private ArrayList<SpendGoReward> rewards_list;

    public int getPoint_total()
    {
        return point_total;
    }

    public int getSpend_threshold()
    {
        return spend_threshold;
    }

    public int getRewards_count()
    {
        return rewards_count;
    }

    public ArrayList<SpendGoReward> getRewards_list()
    {
        return rewards_list;
    }
}
