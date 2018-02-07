package com.fishbowl.basicmodule.Interfaces;


import org.json.JSONObject;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface FBJsonServiceCallback
{
    public void onServiceCallback(JSONObject response, Exception error);
}
