package com.fishbowl.basicmodule.Miscellaneous;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by digvijay(dj)
 */
public class CustomJsonObjectRequest extends JsonObjectRequest {
	
	
	 public String apiKey=null;
	HashMap<String, String> header=null;
	public CustomJsonObjectRequest(int method, String url, String requestBody,HashMap<String, String> _header , Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
	    {
	        super(method, url, requestBody, listener, errorListener);
			header=_header;
	    }

	    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	    {
	        try
	        {
	            String jsonString = new String(response.data, "UTF-8");
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

	    @Override
		public Map<String, String> getHeaders() throws AuthFailureError {
                return header;
		} 
}
