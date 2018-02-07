package com.identity.arx.general;

import android.content.Context;
import android.provider.Settings.Secure;

public class DeviceInfo {
    public static String getDeviceId(Context mContext) {
        return Secure.getString(mContext.getContentResolver(), "android_id");
    }
}
