package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)
 */
public interface FBServiceCallback {

	public void onServiceCallback(JSONObject response, Exception error, String errorMessage);
}
