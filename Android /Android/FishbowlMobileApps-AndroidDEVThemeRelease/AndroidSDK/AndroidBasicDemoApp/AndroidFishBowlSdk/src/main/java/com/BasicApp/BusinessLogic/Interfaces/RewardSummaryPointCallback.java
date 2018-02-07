package com.BasicApp.BusinessLogic.Interfaces;


import com.BasicApp.BusinessLogic.Models.RewardPointSummary;

public interface RewardSummaryPointCallback {
    public void onRewardSummaryPointCallback(RewardPointSummary offerSummary, Exception error);
}

