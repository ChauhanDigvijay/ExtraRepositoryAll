package com.olo.jambajuice.Utils;

import org.json.JSONObject;

/**
 * Created by mikemaxwell on 9/19/15.
 */
public class ParseUtils {
    public static Object sanitizeValue(Object obj) {
        if (obj == null)
            return JSONObject.NULL;
        else
            return obj;
    }
}
