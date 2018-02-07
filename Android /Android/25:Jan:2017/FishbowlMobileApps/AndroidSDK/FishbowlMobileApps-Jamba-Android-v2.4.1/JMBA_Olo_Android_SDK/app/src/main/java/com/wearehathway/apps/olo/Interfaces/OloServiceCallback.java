package com.wearehathway.apps.olo.Interfaces;

import org.json.JSONObject;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public interface OloServiceCallback
{
    public void onServiceCallback(JSONObject response, Exception error);
}
