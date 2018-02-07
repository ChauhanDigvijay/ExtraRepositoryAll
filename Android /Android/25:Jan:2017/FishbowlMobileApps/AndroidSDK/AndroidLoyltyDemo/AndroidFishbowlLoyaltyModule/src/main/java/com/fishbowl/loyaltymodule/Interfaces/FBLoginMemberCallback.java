package com.fishbowl.loyaltymodule.Interfaces;

/**
 * Created by digvijaychauhan on 11/08/16.
 */

import org.json.JSONObject;


public interface FBLoginMemberCallback {

    public void onLoginMemberCallback(JSONObject response, Exception error);
}
