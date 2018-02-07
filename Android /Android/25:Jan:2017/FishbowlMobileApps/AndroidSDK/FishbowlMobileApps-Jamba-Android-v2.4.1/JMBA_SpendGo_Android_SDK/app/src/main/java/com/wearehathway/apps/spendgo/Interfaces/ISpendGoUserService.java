package com.wearehathway.apps.spendgo.Interfaces;

import com.wearehathway.apps.spendgo.Models.SpendGoUser;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface ISpendGoUserService
{
    public void onUserServiceCallback(SpendGoUser user, Exception error);
}
