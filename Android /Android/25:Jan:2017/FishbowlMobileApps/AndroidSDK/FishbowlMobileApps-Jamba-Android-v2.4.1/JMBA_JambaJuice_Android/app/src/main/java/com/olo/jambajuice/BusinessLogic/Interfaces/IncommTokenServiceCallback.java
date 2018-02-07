package com.olo.jambajuice.BusinessLogic.Interfaces;

import org.json.JSONObject;

/**
 * Created by vthink on 24/10/16.
 */

public interface IncommTokenServiceCallback {
    public void onIncommTokenServiceCallback(String tokenSummary,Boolean successFlag, String error);
}
