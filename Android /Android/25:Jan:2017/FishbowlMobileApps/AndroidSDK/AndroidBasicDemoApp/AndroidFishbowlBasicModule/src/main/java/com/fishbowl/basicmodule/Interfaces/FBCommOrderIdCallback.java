package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 23/11/16.
 */


public interface FBCommOrderIdCallback
{
    public void OnFBCommOrderIdCallback(JSONObject response, Exception error);
}
