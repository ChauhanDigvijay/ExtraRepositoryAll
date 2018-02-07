package com.wearehathway.apps.spendgo.Interfaces;

import com.wearehathway.apps.spendgo.Models.SpendGoRewardSummary;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface ISpendGoRewardSummary
{
    public void onRewardSummaryCallback(SpendGoRewardSummary rewards, Exception error);
}
