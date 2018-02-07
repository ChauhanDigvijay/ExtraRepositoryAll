package com.fishbowl.loyaltymodule.Interfaces;

import org.json.JSONObject;
/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */

public interface FBServiceCallback {
	    
	public void onServiceCallback(JSONObject response, Exception error, String errorMessage);
} 
 