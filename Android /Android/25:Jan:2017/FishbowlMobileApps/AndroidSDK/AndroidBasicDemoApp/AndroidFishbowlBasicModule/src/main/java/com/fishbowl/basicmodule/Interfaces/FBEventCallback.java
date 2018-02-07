package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 14/09/17.
 */

public interface FBEventCallback
{
    public void OnFBEventCallback(JSONObject response, Exception error);
}
