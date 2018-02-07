package com.fishbowl.basicmodule.Interfaces;


import com.fishbowl.basicmodule.Models.FBMember;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface FBUserServiceCallback
{
    public void onUserServiceCallback(FBMember user, Exception error);
}
