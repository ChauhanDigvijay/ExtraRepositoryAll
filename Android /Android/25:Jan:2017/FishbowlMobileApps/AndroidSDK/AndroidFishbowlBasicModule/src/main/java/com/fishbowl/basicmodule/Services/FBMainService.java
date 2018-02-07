package com.fishbowl.basicmodule.Services;

import android.util.Base64;

import com.Preferences.FBPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBStringServiceCallback;
import com.fishbowl.basicmodule.Miscellaneous.FBCustomJsonObjectRequest;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBSessionConstants;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.FBUtils;
import com.fishbowl.basicmodule.Utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by digvijaychauhan on 23/11/16.
 */
public class FBMainService
{
    private String mobileUrl;

    private String xClassKey;
    private String secretKey;

    RequestQueue requestQueue;
    private static FBSdk fbSdk;
    private String apiBaseUrl= "https://stg-hbh.fishbowlcloud.com/clpapi/";

    private static FBMainService instance;

    public static FBMainService sharedInstance(){
        if(instance==null){
            instance=new FBMainService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk){
        fbSdk =_fbsdk;
        requestQueue = Volley.newRequestQueue(fbSdk.context);
    }



    public static FBMainService getInstance()
    {
        return instance;
    }

    // Public Methods
    public void get(String path, HashMap<String, Object> parameters, FBJsonServiceCallback callback)
    {
        sendRequest(Request.Method.GET, path, parameters, callback);
    }

    public void post(String path, HashMap<String, Object> parameters, FBStringServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path, parameters, callback);
    }

    public void post(String path, HashMap<String, Object> parameters, FBJsonServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path, parameters, callback);
    }
    public void put(String path, HashMap<String, Object> parameters, FBJsonServiceCallback callback)
    {
        sendRequest(Request.Method.PUT, path, parameters, callback);
    }

    // Private Methods
    private void sendRequest(int method, String path, HashMap<String, Object> parameters, final FBJsonServiceCallback callback)
    {
        String url = getCompleteServerUrl(path);

        String params = "";
        if (method == Request.Method.GET && parameters != null)
        {
            url +=  getUrlString(parameters);
        }
        else
        {
            params = FBUtils.getJsonObject(parameters).toString();
        }
        final String finalUrl = apiBaseUrl + path;
        FBCustomJsonObjectRequest request = new FBCustomJsonObjectRequest(method, url, params, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Logger.i("Response==> " + response);
                callback.onServiceCallback(response, null);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                parseError(error, callback, null);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                return getServiceHeders(finalUrl, new String(getBody()));
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
            {
                if (response == null || response.statusCode != 200) // If there is no response or server is sending error.
                {
                    return Response.error(new VolleyError(response));
                }
                else
                {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        request.setShouldCache(false);
        addRequestToQueue(request);
    }

    private void sendRequest(int method, String path, HashMap<String, Object> parameters, final FBStringServiceCallback callback)
    {
        String url = getCompleteServerUrl(path);

        String params = "";
        if (method == Request.Method.GET && parameters != null)
        {
            url += "&" + getUrlString(parameters);
        }
        else
        {
            params = FBUtils.getJsonObject(parameters).toString();
        }
        final String finalUrl = apiBaseUrl + path;
        final String finalParams = params;
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Logger.i("Response==> " + response);
                callback.onServiceCallback(response, null);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                parseError(error, null, callback);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                return getServiceHeders(finalUrl, new String(getBody()));
            }

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                return finalParams.getBytes();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response)
            {
                //                parseResponse(response);
                if (response == null || response.statusCode != 200) // If there is no response or server is sending error.
                {
                    return Response.error(new VolleyError(response));
                }
                else
                {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        request.setShouldCache(false);
        addRequestToQueue(request);
    }

    public String getSignature(String spendgo_id){
        String path = "get";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("spendgo_id", spendgo_id);
        String params = FBUtils.getJsonObject(parameters).toString();
        final String finalUrl = mobileUrl + path;
        final String finalParams = params;
        final String body = new String(finalParams.getBytes());
        return calculateHash(finalUrl, body);
    }

    private void parseError(VolleyError error, FBJsonServiceCallback jsonCallback, FBStringServiceCallback stringCallback)
    {
        NetworkResponse response = error.networkResponse;
        if (error.getMessage() != null && error.getMessage().contains("authentication"))
        {
            // Server sends a 401 (Unauthorized) but does not add a WWW-Authenticate header. In this case volley try to parse this header and fails.
            NetworkResponse networkResponse = new NetworkResponse(401, "".getBytes(), null, false);
            error = new ServerError(networkResponse);
        }
        else if (response != null && response.data != null)
        {
            String str = new String(response.data);
            int code = response.statusCode;
            String details = "";
            try
            {
                JSONObject jsonStr = new JSONObject(str);
                // We don't need spendGo JSON error code as it is always 2000. Instead https error code is required. Error codes are defined in FBSessionConstants.
                int serverStatusCode = 0;
                if (jsonStr.has("code"))
                {
                    serverStatusCode = jsonStr.getInt("code");
                }
                if (serverStatusCode == FBSessionConstants.SERVER_ERROR.INVALID_AUTH_TOKEN.value)
                {
                    FBSessionService.notifyInvalidAuthTokenCallback();
                    code = serverStatusCode; // Update status code to auth token status code. In all other cases ignore it.
                }
                if (jsonStr.has("details"))
                {
                    details = jsonStr.getString("details");
                }

                NetworkResponse networkResponse = new NetworkResponse(code, details.getBytes(), response.headers, false);
                error = new ServerError(networkResponse);

            } catch (JSONException e)
            {
            }
            Logger.e("Error==> " + code + " Details: " + details);
        }

        if (jsonCallback != null)
        {
            jsonCallback.onServiceCallback(null, error);
        }
        else if (stringCallback != null)
        {
            stringCallback.onServiceCallback(null, error);
        }
    }

    private Map<String, String> getServiceHeders(String finalUrl, String body)
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Application", "mobilesdk");
        headers.put("client_id", FBConstant.client_id);
        headers.put("client_secret", FBConstant.client_secret);
        headers.put("response_type", "token");
        headers.put("scope", "read");
        headers.put("state", "stateless");
        headers.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        headers.put("tenantid", FBConstant.client_tenantid);
        if (FBSessionService.currentSession != null)
        {
            headers.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        }
        return headers;
    }




    private String getCompleteServerUrl(String path)
    {
        return apiBaseUrl  + path ;
    }



    private String getUrlString(HashMap<String, Object> parameters)
    {
        String urlParams = "";
        if (parameters != null)
        {
            for (String key : parameters.keySet())
            {
                Object obj = parameters.get(key);
                if (urlParams.equals(""))
                {
                    urlParams = key + "=" + obj;
                }
                else
                {
                    urlParams += "&" + key + "=" + obj;
                }
            }
        }
        return urlParams;
    }

    private <T> void addRequestToQueue(Request<T> req)
    {
        requestQueue.add(req);
    }

    private String calculateHash(String url, String body)
    {
        try
        {
            String finalMessage = url.concat(body);
            String type = "HmacSHA256";
            byte[] keyBase64 = Base64.decode(secretKey, Base64.DEFAULT);
            SecretKeySpec secret = new SecretKeySpec(keyBase64, type);
            Mac mac = Mac.getInstance(type);
            mac.init(secret);
            byte[] bytes = mac.doFinal(finalMessage.getBytes());
            String hash = Base64.encodeToString(bytes, Base64.DEFAULT);
            Logger.i("HASH: " + hash);
            return hash;
        } catch (Exception ex)
        {
        }
        return null;
    }
}
