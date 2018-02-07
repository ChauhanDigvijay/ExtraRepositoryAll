package com.fishbowl.basicmodule.Interfaces;


import com.fishbowl.basicmodule.Models.FBSessionItem;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface FBSessionServiceCallback
{
    public void onSessionServiceCallback(FBSessionItem user, Exception error);
}
