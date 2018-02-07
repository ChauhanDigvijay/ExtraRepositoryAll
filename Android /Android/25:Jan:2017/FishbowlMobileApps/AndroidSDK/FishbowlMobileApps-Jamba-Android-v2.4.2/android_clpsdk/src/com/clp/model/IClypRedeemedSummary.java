package com.clp.model;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)  on 02/03/16.
 */
public interface IClypRedeemedSummary
{
    public void onClypRedeemedCallback(JSONObject response, String error);
}
