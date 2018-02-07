package com.clp.model;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)  on 27/03/16.
 */
public interface IClypOfferSummary
{
    public void onClypOfferyCallback(JSONObject response, String error);
}
