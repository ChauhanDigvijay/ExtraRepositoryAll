package com.identity.arx.utils;

import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.util.Log;

public class CameraErrorCallback implements ErrorCallback {
    private static final String TAG = "CameraErrorCallback";

    public void onError(int error, Camera camera) {
        Log.e(TAG, "Encountered an unexpected camera error: " + error);
    }
}
