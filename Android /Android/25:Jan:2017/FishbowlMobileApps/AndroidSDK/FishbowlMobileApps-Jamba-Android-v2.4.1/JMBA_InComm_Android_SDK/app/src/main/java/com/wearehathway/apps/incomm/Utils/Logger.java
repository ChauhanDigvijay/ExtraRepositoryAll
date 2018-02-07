package com.wearehathway.apps.incomm.Utils;


import android.util.Log;

public class Logger
{
    private static String LOG_TAG = "LOGGER";

    private enum LoggerState
    {
        DEBUGABLE(1), NON_DEBUGABLE(2);
        int currentVal = 1;

        private LoggerState(int val)
        {
            currentVal = val;
        }

        public int value()
        {
            return currentVal;
        }
    }

    private static LoggerState state = LoggerState.DEBUGABLE;

    public static void i(String msg)
    {
        if (Logger.isLogable())
        {
            Log.i(LOG_TAG, msg);
        }
    }

    public static void d(String msg)
    {
        if (Logger.isLogable())
        {
            Log.d(LOG_TAG, msg);
        }
    }

    public static void v(String msg)
    {
        if (Logger.isLogable())
        {
            Log.v(LOG_TAG, msg);
        }
    }

    public static void w(String msg)
    {
        if (Logger.isLogable())
        {
            Log.w(LOG_TAG, msg);
        }
    }

    public static void e(String msg)
    {
        if (Logger.isLogable())
        {
            Log.e(LOG_TAG, msg);
        }
    }

    public static void println(int priority, String msg)
    {
        if (Logger.isLogable())
        {
            Log.println(priority, LOG_TAG, msg);
        }
    }

    private static boolean isLogable()
    {
        boolean isLogable = true;
        if (LoggerState.NON_DEBUGABLE.value() == Logger.state.value())
        {
            isLogable = false;
        }
        return isLogable;
    }
}
