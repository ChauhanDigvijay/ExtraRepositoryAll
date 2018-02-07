package com.fishbowl.basicmodule.Interfaces;


import org.json.JSONArray;

/**
 * Created by digvijay(dj)
 */
public interface FBServiceArrayCallback {
    public void onFBServiceArrayCallback(JSONArray response, Exception error, String errorMessage);
}
