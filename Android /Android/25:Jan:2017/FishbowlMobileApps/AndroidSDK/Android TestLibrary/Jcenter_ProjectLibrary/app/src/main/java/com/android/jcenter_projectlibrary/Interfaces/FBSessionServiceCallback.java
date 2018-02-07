package com.android.jcenter_projectlibrary.Interfaces;


import com.android.jcenter_projectlibrary.Models.FBSessionItem;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface FBSessionServiceCallback
{
    public void onSessionServiceCallback(FBSessionItem user, Exception error);
}
