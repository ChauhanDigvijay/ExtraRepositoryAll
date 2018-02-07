package com.wearehathway.apps.incomm.Utils;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.wearehathway.apps.incomm.Utils.InCommConstants.TimeFormat;


/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class InCommUtils
{
    public static Gson getGsonForParsingDate()
    {
        Gson gson = new GsonBuilder().setDateFormat(TimeFormat).create();
        return gson;
    }

    public static Exception getParsingError()
    {
        return new Exception("Error occurred while parsing data.");
    }

    public static VolleyError getError(int statusCode, String description)
    {
        NetworkResponse networkResponse = new NetworkResponse(statusCode, description.getBytes(), null, false);
        return new VolleyError(networkResponse);
    }

    public static JSONObject convertToJSON(Object object)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(getGsonForParsingDate().toJson(object));
            removeEmptyValues(jsonObject);
        } catch (JSONException e)
        {
            jsonObject = new JSONObject();
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void removeEmptyValues(JSONObject jsonString)
    {
        Iterator<String> keys = jsonString.keys();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try
            {
                Object value = jsonString.get(key);
                if(value == null)
                {
                    keys.remove();
                }
                else if ( value instanceof JSONObject ) {
                    removeEmptyValues((JSONObject) value);
                }
                else if(value.getClass().equals(Double.class))
                {
                    Double actualVal = (Double) value;
                    if(actualVal == 0)
                    {
                        keys.remove();
                    }
                }
                else if(value.getClass().equals(Integer.class))
                {
                    Integer actualVal = (Integer) value;
                    if(actualVal == 0)
                    {
                        keys.remove();
                    }
                }
                else if(value instanceof JSONArray)
                {
                    JSONArray array = (JSONArray) value;
                    for(int i = 0; i<array.length(); i++)
                    {
                        Object arrayObj = array.get(i);
                        if(arrayObj instanceof JSONObject)
                        {
                            removeEmptyValues((JSONObject) arrayObj);
                        }
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}