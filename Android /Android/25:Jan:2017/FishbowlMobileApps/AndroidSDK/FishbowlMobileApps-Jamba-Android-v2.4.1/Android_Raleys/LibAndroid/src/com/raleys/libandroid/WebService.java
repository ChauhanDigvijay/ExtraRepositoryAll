package com.raleys.libandroid;

import java.io.ByteArrayOutputStream;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unchecked")
public class WebService extends AsyncTask<String, Void, String> {
	// service URL definitions
	public static final String KPI_URL = "/perspective/rest/kpi/immediateChildKpi";
	public static final String LOGIN_URL = "/rest/user/current";

	// error string definitions
	public static final String TIMED_OUT = "Timed Out";
	public static final String CONNECTION_LOST = "Connection Lost";
	public static final String AUTHORIZATION_FAILED = "Authorization Failed";

	// misc definitions
	public static final int SERVICE_TIMEOUT = 15000; // in milliseconds

	// members

	@SuppressWarnings("rawtypes")
	public Class _responseClass;
	private WebServiceListener _listener;
	private WebServiceError _error;
	private String _url;
	private String _auth;
	private Object _responseObject;
	private String _postParamsString;
	private String _responseData;
	private ByteArrayEntity _byteArrayEntity;
	private HttpResponse _response;
	private int _httpStatusCode;

	public boolean _isActive;

	@SuppressWarnings("rawtypes")
	public WebService(WebServiceListener listener, Class responseClass,
			String postParams, String auth) {
		_isActive = true;
		_listener = listener;
		_responseClass = responseClass;
		_postParamsString = postParams;
		_auth = auth;
	}

	@Override
	protected String doInBackground(String... urls) {
		return doRequest(urls);
	}

	@Override
	protected void onPostExecute(String result) {
		_isActive = false;
		processResponse();
		_listener.onServiceResponse(_responseObject);
	}

	private String doRequest(String... urls) {
		_url = urls[0];
		_httpStatusCode = 0;
		_responseData = null;
		Log.i(getClass().getSimpleName(), "URL:" + _url);

		try {
			if (_responseClass != null
					&& _responseClass != ByteArrayEntity.class) // ByteArrayEntity
																// needs to be
																// instantiated
																// with response
																// data
				_responseObject = _responseClass.newInstance();

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					SERVICE_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SERVICE_TIMEOUT);
			final DefaultHttpClient httpClient = new DefaultHttpClient(
					httpParameters);
			// final DefaultHttpClient httpClient =
			// Utils.getNewHttpClient(SERVICE_TIMEOUT); // use this to ignore
			// certificates and comment out the four lines above it
			_response = null;

			if (_postParamsString != null) // HTTP POST
			{
				HttpPost httpPost = new HttpPost(_url);

				if (_auth != null)
					httpPost.addHeader("authKey", _auth);

				httpPost.addHeader("Content-Type", "application/json");
				StringEntity stringEntity = new StringEntity(_postParamsString);
				stringEntity.setContentType("application/json");
				httpPost.setEntity(stringEntity);
				httpPost.getParams().setParameter(
						CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
				_response = httpClient.execute(httpPost);
			} else // HTTP GET
			{
				HttpGet httpGet = new HttpGet(_url);
				_response = httpClient.execute(httpGet);
			}

			StatusLine statusLine = _response.getStatusLine();
			_httpStatusCode = statusLine.getStatusCode();

			if (_responseClass == ByteArrayEntity.class) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				_response.getEntity().writeTo(baos);
				byte[] bytes = baos.toByteArray();
				_byteArrayEntity = (new ByteArrayEntity(bytes));
			} else if (_responseClass != null)
				_responseData = Utils.stringFromHttpEntity(_response
						.getEntity());
		} catch (ConnectTimeoutException cte) {
			_httpStatusCode = 422;
			_error = new WebServiceError(_httpStatusCode,
					"Server Request Timed Out");
		} catch (SocketTimeoutException ste) {
			_httpStatusCode = 422;
			_error = new WebServiceError(_httpStatusCode,
					"Server Request Timed Out");
		} catch (Exception e) {
			_httpStatusCode = 422;
			_error = new WebServiceError(_httpStatusCode, e.getMessage());
		}

		return _responseData;
	}

	private void processResponse() {
		synchronized (this) {
			try {
				Log.i(getClass().getSimpleName(), "HTTP status code = "
						+ _httpStatusCode);
				// Log.i(getClass().getSimpleName(), _responseData);
				// Log.i(getClass().getSimpleName(), "Response class = " +
				// _responseClass.getSimpleName());

				if (_httpStatusCode == 200) {
					if (_responseClass == ByteArrayEntity.class) {
						_responseObject = _byteArrayEntity;
					} else if (_responseObject != null) {
						Gson gson = new GsonBuilder().disableHtmlEscaping()
								.create();
						_responseObject = gson.fromJson(_responseData,
								_responseClass);
					}
				} else if (_httpStatusCode == 422) {
					if (_error == null) // might already be set by an http error
										// in doRequest()
					{
						Gson gson = new GsonBuilder().disableHtmlEscaping()
								.create();
						_error = new WebServiceError();
						_error = gson.fromJson(_responseData,
								WebServiceError.class);
					}
				}
			} catch (Exception e) {
				Log.e(getClass().getSimpleName(), e.toString());
			}
		}
	}

	public int getHttpStatusCode() {
		return _httpStatusCode;
	}

	public Object getResponseObject() {
		return _responseObject;
	}

	public String getResponseString() {
		return _responseData;
	}

	public WebServiceError getError() {
		return _error;
	}
}
