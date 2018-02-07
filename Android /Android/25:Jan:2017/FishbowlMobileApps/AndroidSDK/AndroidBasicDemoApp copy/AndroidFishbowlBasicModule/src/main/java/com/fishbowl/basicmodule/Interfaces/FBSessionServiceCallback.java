package com.fishbowl.basicmodule.Interfaces;


import com.fishbowl.basicmodule.Models.FBSessionItem;

/**
 * Created by digvijay(dj)
 */
public interface FBSessionServiceCallback
{
    public void onSessionServiceCallback(FBSessionItem user, Exception error);
}
