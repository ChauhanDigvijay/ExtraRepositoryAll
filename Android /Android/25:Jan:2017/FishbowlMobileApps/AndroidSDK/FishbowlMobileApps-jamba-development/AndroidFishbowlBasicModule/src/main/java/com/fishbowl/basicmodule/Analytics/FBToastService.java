package com.fishbowl.basicmodule.Analytics;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by digvijay(dj)
 */
public class FBToastService
{
    static FBToastService instance = null;
    Context context;
    static boolean onOff = false;

    public static boolean isOnOff() {
        return onOff;
    }

    public static void setOnOff(boolean onOff) {
        FBToastService.onOff = onOff;
    }

    public static FBToastService  sharedInstance() {

        if (instance == null) {
            instance = new FBToastService();
        }
        return instance;
    }

    public void initWithContext(Context _context) {
        this.context = _context;
    }

    public void show(String msg) {
        if (onOff) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

}
