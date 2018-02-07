package com.wearehathway.apps.olo.Utils;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class Utils
{
    public static String stripExtension(String str)
    {
        // Handle null case specially.
        if (str == null)
        {
            return null;
        }
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1)
        {
            return str;
        }
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }
}