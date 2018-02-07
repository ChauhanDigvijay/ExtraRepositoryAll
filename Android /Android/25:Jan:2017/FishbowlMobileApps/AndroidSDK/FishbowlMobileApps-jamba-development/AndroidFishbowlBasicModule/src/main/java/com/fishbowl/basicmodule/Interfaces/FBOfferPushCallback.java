

package com.fishbowl.basicmodule.Interfaces;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)  on 03/03/16.
 */
public interface FBOfferPushCallback
{
    public void onCLypOfferPush(JSONObject response, String error);
}
