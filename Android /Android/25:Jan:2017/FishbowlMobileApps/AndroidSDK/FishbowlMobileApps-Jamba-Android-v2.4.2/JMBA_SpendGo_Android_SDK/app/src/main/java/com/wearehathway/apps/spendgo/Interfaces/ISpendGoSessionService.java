package com.wearehathway.apps.spendgo.Interfaces;

import com.wearehathway.apps.spendgo.Models.SpendGoSession;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface ISpendGoSessionService
{
    public void onSessionServiceCallback(SpendGoSession user, Exception error);
}
