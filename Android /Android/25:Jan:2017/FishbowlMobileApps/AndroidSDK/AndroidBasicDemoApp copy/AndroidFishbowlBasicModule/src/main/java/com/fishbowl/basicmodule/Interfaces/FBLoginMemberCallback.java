package com.fishbowl.basicmodule.Interfaces;


/**
 * Created by digvijay(dj)
 */

import org.json.JSONObject;


public interface FBLoginMemberCallback {

    public void onLoginMemberCallback(JSONObject response, Exception error);
}
