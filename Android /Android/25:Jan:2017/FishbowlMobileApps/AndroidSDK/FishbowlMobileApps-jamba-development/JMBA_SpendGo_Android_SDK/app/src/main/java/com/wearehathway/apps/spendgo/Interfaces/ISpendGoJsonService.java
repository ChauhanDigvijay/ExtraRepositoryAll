package com.wearehathway.apps.spendgo.Interfaces;


import org.json.JSONObject;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface ISpendGoJsonService
{
    public void onServiceCallback(JSONObject response, Exception error);
}
