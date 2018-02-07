package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardPointSummary;

public interface RewardSummaryPointCallback {
    public void onRewardSummaryPointCallback(RewardPointSummary offerSummary, Exception error);
}

