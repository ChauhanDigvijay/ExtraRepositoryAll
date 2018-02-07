package com.wearehathway.apps.incomm.Services;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommSDKConfiguration;
import com.wearehathway.apps.incomm.Models.InCommValidationErrors;
import com.wearehathway.apps.incomm.Utils.InCommUtils;
import com.wearehathway.apps.incomm.Utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nauman Afzaal on 06/08/15.
 */
public class InCommService
{
    private InCommSDKConfiguration configuration;

    private static final int TIMEOUT = 25000;
    /* Volley Bug: MAX_RETRY_COUNT set to -1 to avoid multiple postings by volley */
    private static final int MAX_RETRY_COUNT = -1;
    RequestQueue requestQueue;

    private static InCommService instance;

    public static void initialize(Context context, InCommSDKConfiguration configuration)
    {
        if (instance == null)
        {
            instance = new InCommService(context, configuration);
        }else{
            instance.getConfiguration().applicationToken = configuration.applicationToken;
        }
    }
    public static void clearSession(){
        instance=null;
    }

    public static InCommService getInstance()
    {
        return instance;
    }

    // Public Methods
    public void get(String path, HashMap<String, Object> parameters, InCommServiceCallback callback)
    {
        sendRequest(Request.Method.GET, path, parameters, callback);
    }

    public void post(String path, HashMap<String, Object> parameters, InCommServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path, parameters, callback);
    }

    public void post(String path, @Nullable String parameters, InCommServiceCallback callback)
    {
        sendRequest(Request.Method.POST, path, parameters, callback);
    }


    public void put(String path, HashMap<String, Object> parameters, InCommServiceCallback callback)
    {
        sendRequest(Request.Method.PUT, path, parameters, callback);
    }

    public void delete(String path, HashMap<String, Object> parameters, InCommServiceCallback callback)
    {
        sendRequest(Request.Method.DELETE, path, parameters, callback);
    }

    // Private Methods

    //Post, Put, Delete request with JSONObject string as parameter.
    private void sendRequest(int method, String path, String parameters, final InCommServiceCallback callback)
    {
        String url = getCompleteServerUrl(path);
        makeRequest(method, url, parameters, callback);
    }

    private void sendRequest(int method, String path, HashMap<String, Object> parameters, final InCommServiceCallback callback)
    {

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
        Logger.i("URL==> " + url);
        Logger.i("PARAMS==> " + params);
        makeRequest(method, url, params, callback);
    }

    private void makeRequest(int method, String url, final String params, final InCommServiceCallback callback)
    {
        final StringRequest request = new StringRequest(method, url, new Response.Listener<String>()
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
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null)
                {
                    String responseString = new String(response.data);
                    Logger.e("Error==> " + responseString);
                    try
                    {

                        if (error instanceof ServerError || error instanceof AuthFailureError)
                        {
                            try
                            {
                                JSONObject jsonObject=null;
                                int resultCode=0;
                                String resultDescription="";

                                try {
                                    jsonObject = new JSONObject(responseString);
                                    resultCode = jsonObject.getInt("Code");
                                    resultDescription = jsonObject.getString("Message");

                                    if (jsonObject.has("ValidationErrors")) {
                                        InCommValidationErrors validationError = new InCommValidationErrors();
                                        Gson gson = InCommUtils.getGsonForParsingDate();
                                        String validationErrorJson = jsonObject.getString("ValidationErrors");
                                        validationError = gson.fromJson(validationErrorJson, InCommValidationErrors.class);
                                        resultDescription = "";
                                        if (validationError.getCreditCardNumber() != null && validationError.getCreditCardNumber().length > 0) {
                                            if(resultDescription==null){
                                                resultDescription="";
                                            }

                                            resultDescription = resultDescription + "Please check the credit card details";
//                                            for (String errorDesc : validationError.getCreditCardNumber()) {
//                                                if(!resultDescription.contains(errorDesc)){
//                                                    resultDescription = resultDescription + errorDesc;
//                                                }
//                                            }
                                        }
                                        if(validationError.getFieldNotSpecified()!=null && validationError.getFieldNotSpecified().length>0){
                                            if(resultDescription==null){
                                                resultDescription="";
                                            }
                                            for(String errorDesc : validationError.getFieldNotSpecified()){
                                                if(!resultDescription.contains(errorDesc)) {
                                                    resultDescription = resultDescription + errorDesc;
                                                }
                                            }
                                        }

                                        if(validationError.getCreditCardExpirationMonth()!=null && validationError.getCreditCardExpirationMonth().length>0){
                                            if(resultDescription==null){
                                                resultDescription="";
                                            }
                                            for(String errorDesc : validationError.getCreditCardExpirationMonth()){
                                                if(!resultDescription.contains(errorDesc)) {
                                                    resultDescription = resultDescription + errorDesc;
                                                }
                                            }
                                        }
                                        if(validationError.getCreditCardExpirationYear()!=null && validationError.getCreditCardExpirationYear().length>0){
                                            if(resultDescription==null){
                                                resultDescription="";
                                            }
                                            for(String errorDesc : validationError.getCreditCardExpirationYear()){
                                                if(!resultDescription.contains(errorDesc)) {
                                                    resultDescription = resultDescription + errorDesc;
                                                }
                                            }
                                        }
//                            if(validationError.getOrderPaymentAmount()!=null && validationError.getOrderPaymentAmount().length>0){
//                                resultDescription ="";
//                                for(String errorDesc : validationError.getOrderPaymentAmount()){
//                                    resultDescription = resultDescription + errorDesc;
//                                }
//                            }
//                            if(validationError.getPayment()!=null && validationError.getPayment().length>0){
//                                resultDescription ="";
//                                for(String errorDesc : validationError.getPayment()){
//                                    resultDescription = resultDescription + errorDesc;
//                                }
//                            }



                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                                //Validation error has occurred.
//                        error = InCommUtils.getError(resultCode, resultDescription);

                                NetworkResponse networkResponse = new NetworkResponse(resultCode, resultDescription.getBytes(), error.networkResponse.headers, false);
                                error = new ServerError(networkResponse);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                callback.onServiceCallback(null, error);
            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError
            {
                return params.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                return getServiceHeders();
            }

            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        request.setShouldCache(false);
        addRequestToQueue(request);
    }

    private Map<String, String> getServiceHeders()
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "ClientAuth " + getConfiguration().applicationToken);
        headers.put("ClientId", getConfiguration().clientId);
        return headers;
    }

    private String getCompleteServerUrl(String path)
    {
        return getConfiguration().baseUrl + "/" + path;
    }

    private InCommService(Context context, InCommSDKConfiguration configuration)
    {
        /*Volley bug: Make 2 requests even after setting retry policy to 0. According to a suggestion in following link this also solves the problem.
        http://stackoverflow.com/questions/26264942/android-volley-makes-2-requests-to-the-server-when-retry-policy-is-set-to-0 */
        System.setProperty("http.keepAlive", "false");
        requestQueue = Volley.newRequestQueue(context);
        this.configuration = configuration;
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

    private <T> void addRequestToQueue(Request<T> req)
    {
        req.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, MAX_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }

    public InCommSDKConfiguration getConfiguration()
    {
        if (configuration == null)
        {
            configuration = new InCommSDKConfiguration();
        }
        return configuration;
    }
}
