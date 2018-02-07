package com.fishbowl.basicmodule.Services;

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
import com.fishbowl.basicmodule.Interfaces.FBLoginMemberCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceArrayCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBServicePassCallback;
import com.fishbowl.basicmodule.Miscellaneous.CustomJsonArrayRequest;
import com.fishbowl.basicmodule.Miscellaneous.CustomJsonObjectRequest;
import com.fishbowl.basicmodule.Miscellaneous.CustomJsonPassRequest;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBService {

	private String apiBaseURL;
	private String apiKey;
	Context context;
	private static FBService instance;
	private static final int TIMEOUT = 25000;
	private static final int MAX_RETRY_COUNT = -1;
	RequestQueue requestQueue;
	private FBService(Context context, String apiBaseUrl, String apiKey)
	{
	        /*Volley bug: Make 2 requests even after setting retry policy to 0. According to a suggestion in following link this also solves the problem.
	        http://stackoverflow.com/questions/26264942/android-volley-makes-2-requests-to-the-server-when-retry-policy-is-set-to-0 */
		System.setProperty("http.keepAlive", "false");
		requestQueue = Volley.newRequestQueue(context);
		this.apiBaseURL = apiBaseUrl;
		this.context=context;
		this.apiKey = apiKey;
	}
	public static FBService getInstance()
	{
		return instance;
	}
	public static void initialize(Context context, String apiBaseUrl, String apiKey)
	{
		if (instance == null)
		{
			instance = new FBService(context, apiBaseUrl, apiKey);
		}
	}

	// Public Methods
	public void get(String path, HashMap<String, Object> parameters, HashMap<String, String> _header, FBServiceCallback callback)
	{
		sendRequest(Request.Method.GET, path,null,  parameters,_header, callback);
	}


	// Public Methods
	public void get(String path, HashMap<String, Object> parameters, HashMap<String, String> _header,boolean dj, FBServiceCallback callback)
	{
		//sendRequest(Request.Method.GET, path,null,  parameters,_header, callback);
		makeRequest(Request.Method.GET, path, "",_header, callback, TIMEOUT);
	}

	public void post(String path, HashMap<String, Object> parameters, HashMap<String, String> _header, FBServiceCallback callback)
	{
		sendRequest(Request.Method.POST, path,null,  parameters,_header, callback);
	}

	public void makeCustomArrayRequestforbonus(String path, String JsonEntity, HashMap<String, String> _header, final FBServiceArrayCallback callback){

		String url = getCompleteServerUrl(path);

		makeArrayRequest(Request.Method.GET, url, JsonEntity,_header, callback, TIMEOUT);
	}



	// Special case when submitting basket.
	public void post(String path, HashMap<String, Object> parameters, HashMap<String, String> _header, FBServiceCallback callback, int requestTimeOut)
	{
		sendRequest(Request.Method.POST, path,null,  parameters,_header, callback, requestTimeOut);
	}

	public void post(String path, String authToken, HashMap<String, Object> parameters,HashMap<String, String> _header,   FBServiceCallback callback)
	{
		sendRequest(Request.Method.POST, path, authToken, parameters,_header, callback);
	}

	public void put(String path, HashMap<String, Object> parameters,HashMap<String, String> _header,  FBServiceCallback callback)
	{
		sendRequest(Request.Method.PUT, path,null, parameters,_header, callback);
	}
	public void put(String path, String JsonEntity,HashMap<String, String> _header, final FBServiceCallback callback){
		String url = getCompleteServerUrl(path);

		makeRequest(Request.Method.PUT, url, JsonEntity,_header, callback, TIMEOUT);
	}

	/**
	 * Created by digvijay(dj)
	 */
	public void delete(String path, HashMap<String, Object> parameters,HashMap<String, String> _header, FBServiceCallback callback){
		sendRequest(Request.Method.DELETE, path, null, parameters,_header, callback);
	}

	//Send Requests With Default Timeout
	private void sendRequest(int method, String path, String authToken, HashMap<String, Object> parameters,HashMap<String, String> _header, final FBServiceCallback callback){
		sendRequest(method, path, authToken, parameters,_header, callback, TIMEOUT);
	}

	public void post(String path, String JsonEntity,HashMap<String, String> _header, final FBServiceCallback callback){
		String url = getCompleteServerUrl(path);

		makeRequest(Request.Method.POST, url, JsonEntity,_header, callback, TIMEOUT);
	}

	public void post(String path, String JsonEntity,HashMap<String, String> _header,boolean dj,final FBServiceCallback callback){


		//makeRequestforstring(Request.Method.POST, path, JsonEntity,_header, callback, TIMEOUT);
	}

	public void postSecurity(String path, String JsonEntity,HashMap<String, String> _header, final FBServiceCallback callback){
		String url = getCompleteServerUrl(path);

		makeRequest(Request.Method.POST, url, JsonEntity,_header, callback, TIMEOUT);
	}

	private void sendRequest(int method, String path, String authToken, HashMap<String, Object> parameters, HashMap<String, String> _header, final FBServiceCallback callback, int requestTimeOut){

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
		makeRequest(method, url, params,_header, callback, requestTimeOut);
	}

	private void makeRequest(int method, String url, String params, HashMap<String, String> _header, final FBServiceCallback callback, int requestTimeOut){


		final CustomJsonObjectRequest request = new CustomJsonObjectRequest(method, url, params, _header, new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				callback.onServiceCallback(response, null,null);
			}

		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				NetworkResponse response = error.networkResponse;
			 String errorMessage = null;
				if (response != null && response.data != null)
				{
					String str = new String(response.data);

					if (error instanceof ServerError || error instanceof AuthFailureError)
					{
						try
						{
							JSONObject jsonStr = new JSONObject(str);
							NetworkResponse networkResponse=null;
							
							if (jsonStr.has("message"))
								errorMessage= (String) jsonStr.get("message");
								networkResponse = new NetworkResponse(1, jsonStr.getString("message").getBytes(), error.networkResponse.headers, false);
							if (jsonStr.has("error_message"))
								networkResponse = new NetworkResponse(1, jsonStr.getString("error_message").getBytes(), error.networkResponse.headers, false);

							error = new ServerError(networkResponse);

						} catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				}
				callback.onServiceCallback(null, error,errorMessage);
			}
		});
		request.apiKey=this.apiKey;
		request.setShouldCache(false);
		addRequestToQueue(request, requestTimeOut);
	}
	private void makePassRequest(int method, String url, String params, HashMap<String, String> _header, final FBServicePassCallback callback, int requestTimeOut){


		final CustomJsonPassRequest request = new CustomJsonPassRequest(method, url, params, _header, new Response.Listener<byte[]>()
		{
			@Override
			public void onResponse(byte[] response) {

				callback.onServicePassCallback(response, null,null);
			}




		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				NetworkResponse response = error.networkResponse;
				String errorMessage = null;
				if (response != null && response.data != null)
				{
					String str = new String(response.data);

					if (error instanceof ServerError || error instanceof AuthFailureError)
					{
						try
						{
							JSONObject jsonStr = new JSONObject(str);
							NetworkResponse networkResponse=null;

							if (jsonStr.has("message"))
								errorMessage= (String) jsonStr.get("message");
								networkResponse = new NetworkResponse(1, jsonStr.getString("message").getBytes(), error.networkResponse.headers, false);
							if (jsonStr.has("error_message"))
								networkResponse = new NetworkResponse(1, jsonStr.getString("error_message").getBytes(), error.networkResponse.headers, false);


							error = new ServerError(networkResponse);

						} catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				}
				callback.onServicePassCallback(null, error,errorMessage);
			}
		});
		request.apiKey=this.apiKey;
		request.setShouldCache(false);
		addRequestToQueue(request, requestTimeOut);
	}



	private void makeArrayRequest(int method, String url, String params, HashMap<String, String> _header, final FBServiceArrayCallback callback, int requestTimeOut){


		final CustomJsonArrayRequest request = new CustomJsonArrayRequest(method, url, params, _header, new Response.Listener<JSONArray>()
		{
			@Override
			public void onResponse(JSONArray response) {

				callback.onCLPServiceArrayCallback(response, null,null);
			}




		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				NetworkResponse response = error.networkResponse;
				String errorMessage = null;
				if (response != null && response.data != null)
				{
					String str = new String(response.data);

					if (error instanceof ServerError || error instanceof AuthFailureError)
					{
						try
						{
							JSONObject jsonStr = new JSONObject(str);
							NetworkResponse networkResponse=null;

							if (jsonStr.has("message"))
								errorMessage= (String) jsonStr.get("message");
							networkResponse = new NetworkResponse(1, jsonStr.getString("message").getBytes(), error.networkResponse.headers, false);
							if (jsonStr.has("error_message"))
								networkResponse = new NetworkResponse(1, jsonStr.getString("error_message").getBytes(), error.networkResponse.headers, false);


							error = new ServerError(networkResponse);

						} catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				}
				callback.onCLPServiceArrayCallback(null, error,errorMessage);
			}
		});
		request.apiKey=this.apiKey;
		request.setShouldCache(false);
		addRequestToQueue(request, requestTimeOut);
	}
	public void makeCustomPassRequest(String path, String JsonEntity, HashMap<String, String> _header, final FBServicePassCallback callback){

		String url = getCompleteServerUrl(path);

		makePassRequest(Request.Method.POST, url, JsonEntity,_header, callback, TIMEOUT);
	}

	public void makeCustomArrayRequest(String path, String JsonEntity, HashMap<String, String> _header, final FBServiceArrayCallback callback){

		String url = getCompleteServerUrl(path);

		makeArrayRequest(Request.Method.POST, url, JsonEntity,_header, callback, TIMEOUT);
	}


	public void makeCustomArrayRequest(String path, String JsonEntity, HashMap<String, String> _header,boolean dj, final FBServiceArrayCallback callback){

		String url = getCompleteServerUrl(path);

		makeArrayRequest(Request.Method.GET, url, JsonEntity,_header, callback, TIMEOUT);
	}
	private String getCompleteServerUrl(String path)
	{
		return apiBaseURL + "/" + path ;

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

	public void loginMember(JSONObject parameter,final FBLoginMemberCallback callback){

		String str=parameter.toString();
		FBService.getInstance().post(FBConstant.FBLoginApi, str,getHeader2(), new FBServiceCallback(){

			public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

				if(error==null&&response!=null){

					callback.onLoginMemberCallback(response,null);

				}else{

					callback.onLoginMemberCallback(null,error);
				}

			}

		});
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


	//Login
	HashMap<String,String> getHeader2(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application","mobile");
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("response_type","token");

		header.put("scope","read");
		header.put("state","stateless");
		return header;
	}

	//Login
	HashMap<String, String> getHeaderfortest() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Application", "mobilesdk");
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("response_type", "token");
		header.put("scope", "read");
		header.put("state", "stateless");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.context));
		//header.put("access_token", FBPreferences.sharedInstance(context).getAccessTokenforapp());
		return header;
	}

	HashMap<String,String> getHeader23(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("CLP-API-KEY","c6d1541a-b661-4423-a057-6391385a4ace");
		return header;
	}

	private <T> void addRequestToQueue(Request<T> req, int requestTimeOut)
	{
		req.setRetryPolicy(new DefaultRetryPolicy(requestTimeOut, MAX_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(req);
	}
}

 
 