package com.fishbowl.apps.olo.Services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.fishbowl.apps.olo.Interfaces.OloDownloadServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Misc.CustomJsonObjectRequest;
import com.fishbowl.apps.olo.Utils.Constants;
import com.fishbowl.apps.olo.Utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class OloService
{

    private String apiBaseURL;
    private String apiKey;

    private static final int TIMEOUT = 25000;
    /* Volley Bug: MAX_RETRY_COUNT set to -1 to avoid multiple postings by volley */
    private static final int MAX_RETRY_COUNT = -1;
    RequestQueue requestQueue;

    private static OloService instance;

    public static void initialize(Context context, String apiBaseUrl, String apiKey)
    {
        if (instance == null)
        {
            instance = new OloService(context, apiBaseUrl, apiKey);
        }
    }

    public static OloService getInstance()
    {
        return instance;
    }

    // Public Methods
    public void get(String path, HashMap<String, Object> parameters, OloServiceCallback callback)
    {
        sendRequest(Request.Method.GET, path,null,  parameters, callback);
    }

    public void post(String path, HashMap<String, Object> parameters, OloServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path,null,  parameters, callback);
    }

    // Special case when submitting basket.
    public void post(String path, HashMap<String, Object> parameters, OloServiceCallback callback, int requestTimeOut)
    {
        sendRequest(Request.Method.POST, path,null,  parameters, callback, requestTimeOut);
    }

    public void post(String path, String authToken, HashMap<String, Object> parameters, OloServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path, authToken, parameters, callback);
    }

    public void put(String path, HashMap<String, Object> parameters, OloServiceCallback callback)
    {
        sendRequest(Request.Method.PUT, path,null, parameters, callback);
    }

    public void delete(String path, HashMap<String, Object> parameters, OloServiceCallback callback)
    {
        sendRequest(Request.Method.DELETE, path, null, parameters, callback);
    }

    public void download(Context context, String path, String location, ProgressCallback progressCallback, final OloDownloadServiceCallback callback)
    {
        String url = getCompleteServerUrl(path);
        Ion.with(context).load(url).progress(progressCallback).write(new File(location)).withResponse().setCallback(new FutureCallback<com.koushikdutta.ion.Response<File>>()
        {
            @Override
            public void onCompleted(Exception exception, com.koushikdutta.ion.Response<File> result)
            {
                File file = null;
                if (result != null)
                {
                    file = result.getResult();
                }
                if (result != null && result.getHeaders().code() < 200 || result.getHeaders().code() > 300 && exception == null)
                {
                    exception = new Exception("Unable to download file.");
                    if (file != null)
                    {
                        file.delete(); // Delete any file if created.
                        file = null;
                    }
                }
                if (callback != null)
                {
                    callback.onDownloadCompetedCallback(file, exception);
                }
            }
        });
    }

    // Private Methods

    //Send Requests With Default Timeout
    private void sendRequest(int method, String path, String authToken, HashMap<String, Object> parameters, final OloServiceCallback callback)
    {
        sendRequest(method, path, authToken, parameters, callback, TIMEOUT);
    }

    private void sendRequest(int method, String path, String authToken, HashMap<String, Object> parameters, final OloServiceCallback callback, int requestTimeOut){

        String url = getCompleteServerUrl(path);

        String params = "";
        if (method == Request.Method.GET && parameters != null)
        {
            url += "&" + getUrlString(parameters);
        }
        else
        {
            params = getJsonObject(parameters).toString();
        }
        if(authToken != null){
            url += "&authtoken=" + authToken;
        }
        makeRequest(method, url, params, callback, requestTimeOut);
    }

    private void makeRequest(int method, String url, String params, final OloServiceCallback callback, int requestTimeOut){
        Logger.i("URL==> " + url);
        Logger.i("PARAMS==> " + params);
        final CustomJsonObjectRequest request = new CustomJsonObjectRequest(method, url, params, new Response.Listener<JSONObject>()
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
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null)
                {
                    String str = new String(response.data);

                    if (error instanceof ServerError || error instanceof AuthFailureError)
                    {
                        try
                        {
                            JSONObject jsonStr = new JSONObject(str);
                            NetworkResponse networkResponse = new NetworkResponse(jsonStr.getInt("code"), jsonStr.getString("message").getBytes(), error.networkResponse.headers, false);
                            error = new ServerError(networkResponse);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Logger.e("Error==> " + str);
                }
                if(error.networkResponse != null && error.networkResponse.statusCode == Constants.OLO_INVALID_AUTH_STATUS_CODE)
                {
                    OloSessionService.notifyInValidAuthTokenCalback();
                }
                callback.onServiceCallback(null, error);
            }
        });
        request.setShouldCache(false);
        addRequestToQueue(request, requestTimeOut);
    }
    private String getCompleteServerUrl(String path)
    {
        return apiBaseURL + "/" + path + "?key=" + apiKey;
    }

    private OloService(Context context, String apiBaseUrl, String apiKey)
    {
        /*Volley bug: Make 2 requests even after setting retry policy to 0. According to a suggestion in following link this also solves the problem.
        http://stackoverflow.com/questions/26264942/android-volley-makes-2-requests-to-the-server-when-retry-policy-is-set-to-0 */
        System.setProperty("http.keepAlive", "false");
        requestQueue = Volley.newRequestQueue(context);
        this.apiBaseURL = apiBaseUrl;
        this.apiKey = apiKey;
    }

    private JSONObject getJsonObject(HashMap<String, Object> parameters)
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

    private <T> void addRequestToQueue(Request<T> req, int requestTimeOut)
    {
        req.setRetryPolicy(new DefaultRetryPolicy(requestTimeOut, MAX_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }
}
