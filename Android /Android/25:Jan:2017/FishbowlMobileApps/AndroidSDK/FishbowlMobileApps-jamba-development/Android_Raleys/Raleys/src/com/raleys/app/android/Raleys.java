package com.raleys.app.android;

import java.io.Serializable;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.Login;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;

public class Raleys implements Serializable {

	private static final long serialVersionUID = 8614970475455405873L;
	private static Raleys obj;
	private static RaleysApplication _app;
	public String pushDeviceToken;

	public transient Location latestLocation;
	public transient Context context;

	public static final String COMMON_ERROR_MSG = "Unable to process your request. Please try again";
	public static final int SLIDE_TRANSITION_DELAY = 400;

	private Raleys(Application application) {
		context = application.getApplicationContext();
		pushDeviceToken = "";
		latestLocation = new Location("");
		latestLocation.setLatitude(0);
		latestLocation.setLongitude(0);
	}

	public void reset() {
		obj = null;
	}

	public static Raleys shared(Application application) {
		if (obj == null) {
			_app = (RaleysApplication) application;
			obj = _app.getRaleys();

			if (obj == null) {
				obj = new Raleys(application);
				_app.storeRaleys();
			}
		}
		return obj;
	}

	public void userRegister() {
		if (_app.getPersistendDataAccount() == null
				|| _app.getPersistendDataAccount().email == null) {
			getAccountDetail();
		} else {
			_app.customerRegistration(_app.regid);
		}
	}

	public void getAccountDetail() {
		final Login login = _app.getLogin();
		if (login == null)
			return;

		try {
			final Thread accountInfoThread = new Thread() {
				@Override
				public void run() {
					HttpParams httpParamters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParamters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParamters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParamters);
					// http method
					HttpPost httpPost = new HttpPost(_app.ACCOUNT_GET_URL);
					httpPost.addHeader("Content-Type", "application/json");
					// request
					AccountRequest request = new AccountRequest();
					request.accountId = login.accountId;
					try {
						Gson requestGson = new Gson();
						String requestBody = requestGson.toJson(request);
						StringEntity stringEntity = new StringEntity(
								requestBody);
						stringEntity.setContentType("application/json");
						httpPost.setEntity(stringEntity);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();
						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							AccountRequest responseObject = gson.fromJson(
									responseString, AccountRequest.class);
							_app._currentAccountRequest = responseObject;
							if (_app._currentAccountRequest == null)
								return;
							_app.setPersistentDataAccount(_app._currentAccountRequest);
							_app.savePersistentData();
							_app.customerRegistration(_app.regid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			accountInfoThread.start();
			Thread userRegThread = new Thread() {
				@Override
				public void run() {
					try {
						accountInfoThread.join();
						_app.customerRegistration(_app.regid);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			userRegThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
