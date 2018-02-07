package com.fishbowl.loyaltymodule.Miscellaneous;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class CustomJsonArrayRequest extends JsonArrayRequest {


    public String apiKey=null;
    HashMap<String, String> header=null;
    public CustomJsonArrayRequest(int method, String url, String requestBody, HashMap<String, String> _header , Response.Listener<JSONArray> listener, Response.ErrorListener errorListener)
    {
        super(method, url, requestBody, listener, errorListener);
        header=_header;
    }

    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, "UTF-8");
            //If there is no content in body don't parse
            if (jsonString.length() == 0)
            {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        } catch (JSONException je)
        {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }
}
