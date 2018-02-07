package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;

public interface RewardSummaryCallback {
    public void onRewardSummaryCallback(RewardSummary offerSummary, Exception error);
}
