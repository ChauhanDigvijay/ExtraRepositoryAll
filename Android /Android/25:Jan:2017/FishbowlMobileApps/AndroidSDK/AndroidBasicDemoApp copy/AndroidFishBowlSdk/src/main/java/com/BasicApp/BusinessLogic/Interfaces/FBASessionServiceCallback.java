package com.BasicApp.BusinessLogic.Interfaces;


import com.fishbowl.basicmodule.Models.FBSessionItem;

/**
 * Created by digvijay(dj)
 */
public interface FBASessionServiceCallback {
    public void onUserServiceCallback(FBSessionItem spendGoSession, Exception exception);
}
