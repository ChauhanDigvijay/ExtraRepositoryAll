package com.identity.arx.general;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPrefrence {
    public static SharedPreferences getSharedPrefrence(Context mContext) {
        return mContext.getSharedPreferences("MyApp_Settings", 0);
    }
}
