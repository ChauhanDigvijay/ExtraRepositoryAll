package com.fishbowl.basicmodule.Interfaces;


import org.json.JSONObject;
/**
 * Created by digvijay(dj)
 */

public interface FBJsonServiceCallback
{
    public void onServiceCallback(JSONObject response, Exception error);
}
