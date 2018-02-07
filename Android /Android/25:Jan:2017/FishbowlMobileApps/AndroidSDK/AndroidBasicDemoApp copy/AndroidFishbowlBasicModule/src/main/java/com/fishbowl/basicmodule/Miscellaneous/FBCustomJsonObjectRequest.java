package com.fishbowl.basicmodule.Miscellaneous;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by digvijay(dj)
 */
public class FBCustomJsonObjectRequest extends JsonObjectRequest
{
    public FBCustomJsonObjectRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //If there is no content in body don't parse
            if (jsonString.length() == 0)
            {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        } catch (JSONException je)
        {
            return Response.error(new ParseError(je));
        }
    }
}
