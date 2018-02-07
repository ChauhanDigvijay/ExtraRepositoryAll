package com.hbh.honeybaked.connector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) this._context.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo result = connectivity.getActiveNetworkInfo();
            if (result != null && result.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
