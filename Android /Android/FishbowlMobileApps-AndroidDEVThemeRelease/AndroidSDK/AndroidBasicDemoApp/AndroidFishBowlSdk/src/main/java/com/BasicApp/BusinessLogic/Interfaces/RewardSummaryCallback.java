package com.BasicApp.BusinessLogic.Interfaces;


import com.BasicApp.BusinessLogic.Models.RewardSummary;

public interface RewardSummaryCallback {
    public void onRewardSummaryCallback(RewardSummary offerSummary, Exception error);
}
