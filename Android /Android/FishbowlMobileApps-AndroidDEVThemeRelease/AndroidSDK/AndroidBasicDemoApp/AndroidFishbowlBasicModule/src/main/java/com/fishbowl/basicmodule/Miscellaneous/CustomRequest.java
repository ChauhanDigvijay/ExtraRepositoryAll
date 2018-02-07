package com.fishbowl.basicmodule.Miscellaneous;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by digvijaychauhan on 21/11/16.
 */

public class CustomRequest extends Request {
    // the response listener
    private Response.Listener listener;

    public CustomRequest(int requestMethod, String url, Response.Listener responseListener, Response.ErrorListener errorListener) {
        super(requestMethod, url, errorListener); // Call parent constructor
        this.listener = responseListener;
    }



    // Same as JsonObjectRequest#parseNetworkResponse
    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public int compareTo(Request other) {
        return 0;
    }

    @Override
    protected void deliverResponse(Object response) {
        if (listener!=null)
            listener.onResponse(response);
    }
}
