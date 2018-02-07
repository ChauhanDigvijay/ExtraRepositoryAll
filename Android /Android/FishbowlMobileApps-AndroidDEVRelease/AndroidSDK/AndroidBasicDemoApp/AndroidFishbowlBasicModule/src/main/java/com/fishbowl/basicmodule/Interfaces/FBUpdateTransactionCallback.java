package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by vt010 on 11/15/16.
 */

public interface FBUpdateTransactionCallback {
    public void OnFBUpdateTransactionCallback(JSONObject response, Exception error);
}
