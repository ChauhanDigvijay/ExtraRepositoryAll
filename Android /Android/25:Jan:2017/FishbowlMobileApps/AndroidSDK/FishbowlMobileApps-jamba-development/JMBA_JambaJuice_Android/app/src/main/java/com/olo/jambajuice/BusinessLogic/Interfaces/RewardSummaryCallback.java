package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.RewardSummary;

/**
 * Created by Nauman Afzaal on 22/06/15.
 */
public interface RewardSummaryCallback {
    public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception exception);
}
