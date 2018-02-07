package com.android.jcenter_projectlibrary.Services;


import com.android.jcenter_projectlibrary.Controllers.FBSdk;

import java.util.HashMap;
import java.util.Map;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */

public class FB_LY_UserService {


    private static FBSdk clpsdk;
    public static FB_LY_UserService instance;

    public Map<String, Integer> mapNumToId = new HashMap<String, Integer>();//storesMapforId

    public Map<String, String> mapStateNumToId = new HashMap<String, String>();//storesMapforId
    public String access_token = null;


    public static FB_LY_UserService sharedInstance() {
        if (instance == null) {
            instance = new FB_LY_UserService();

        }
        return instance;
    }

    public void init(FBSdk _clpsdk) {
        clpsdk = _clpsdk;


    }


}
