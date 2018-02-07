package com.android.jcenter_projectlibrary.Utils;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class FBUtils
{

    public static String getUniquePsuedoID()
    {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) +
                (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception)
        {
            // String needs to be initialized
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static JSONObject getJsonObject(HashMap<String, Object> parameters)
    {
        JSONObject object = new JSONObject();
        if (parameters != null)
        {
            for (String key : parameters.keySet())
            {
                Object obj = parameters.get(key);
                try
                {
                    object.put(key, obj);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}