package com.fishbowl.loyaltymodule.Interfaces;


import org.json.JSONArray;

/**
 * Created by digvijaychauhan on 03/10/16.
 */

//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.

public interface FBServiceArrayCallback {
    public void onCLPServiceArrayCallback(JSONArray response, Exception error, String errorMessage);
}
