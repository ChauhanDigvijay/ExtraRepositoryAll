package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 23/11/16.
 */


public interface FBCommTokenCallback
{
    public void OnFBCommTokenCallback(JSONObject response, Exception error);
}
