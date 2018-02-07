package com.fishbowl.basicmodule.Interfaces;


import com.fishbowl.basicmodule.Models.FBMember;

/**
 * Created by digvijay(dj)
 */
public interface FBUserServiceCallback
{
    public void onUserServiceCallback(FBMember user, Exception error);
}
