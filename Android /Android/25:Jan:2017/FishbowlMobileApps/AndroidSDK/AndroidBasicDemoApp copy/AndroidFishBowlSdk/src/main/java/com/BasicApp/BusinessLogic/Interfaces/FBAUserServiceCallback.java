package com.BasicApp.BusinessLogic.Interfaces;


import com.fishbowl.basicmodule.Models.FBMember;

/**
 * Created by digvijay(dj)
 */
public interface FBAUserServiceCallback
{
    public void onUserServiceCallback(FBMember user, Exception error);
}
