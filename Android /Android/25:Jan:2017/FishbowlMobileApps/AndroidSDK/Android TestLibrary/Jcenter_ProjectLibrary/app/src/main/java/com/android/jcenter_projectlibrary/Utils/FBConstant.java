package com.android.jcenter_projectlibrary.Utils;

//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.

import com.android.jcenter_projectlibrary.Models.FBConfig;

/**
 * *
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBConstant {


    //GENERAL CONSTANT
    public static String DEVICE_TYPE = "Android";
    public static String device_os_ver = DEVICE_TYPE.concat(" ").concat(android.os.Build.VERSION.RELEASE);

    public static String gcm_sender_id_dev = "";

    public static String client_id = FBConfig.getClient_id();

    public static String client_secret = FBConfig.getClient_secret();;

    public static String client_tenantid = "1173";


}
