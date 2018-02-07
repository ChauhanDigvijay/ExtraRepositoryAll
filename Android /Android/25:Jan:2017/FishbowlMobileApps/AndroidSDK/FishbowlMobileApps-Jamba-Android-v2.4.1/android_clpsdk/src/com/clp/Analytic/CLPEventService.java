package com.clp.Analytic;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
Created By mohd Vaseem
*/
public class CLPEventService implements Runnable {
	    private static int CONNECTION_TIMEOUT = 120000;

	    private final CLPEventPref clpEventPref;
	    private final String serverURL_;
	    private  String appKey;
	    private Context context;
	    public static AsyncHttpClient aClient = new AsyncHttpClient();
	    public static AsyncHttpClient sClient = new SyncHttpClient();

	    CLPEventService(Context _context,String serverURL,String _appKey, final CLPEventPref store) {
	        serverURL_ = serverURL;
	        clpEventPref = store;
	        context=_context;
	        appKey=_appKey;

	    }

	  
	    @Override
	    public void run() {
	    	
	     final String[] storedEvents = clpEventPref.connectionsFromPref();
       if(storedEvents.length>0){
    	   
	       final String eventData = storedEvents[0];
	       getClient().addHeader("CLP-API-KEY",appKey);
           StringEntity entity = null;
            Log.i("CLPEVENT SERVICE", eventData);
		    try {
				entity = new StringEntity(eventData);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			 entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json")); 
			 
			 //mobile/submitappevents
			 getClient().post(context, serverURL_ + "mobile/submitallappevents",   entity, "application/json", new JsonHttpResponseHandler() {
			            @Override
			            public void onSuccess(int statusCode, Header[] headers,
			                                  JSONObject response) {
			            	
			            ToastManager.sharedInstance().show("Event List send to server");
			            
			                  //localLog("sendAppEvent SUCCESS", "Response + " + response + "");
			            }

			            @Override
			            public void onFailure(int statusCode, Header[] headers,
			                                  Throwable throwable, JSONObject errorResponse) {
			                if (errorResponse != null){}
			                    //localLog("onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",errorResponse.toString());
			            }
			        });
			 
			 
			 //Remove That event has been sent
			 clpEventPref.removeConnection(eventData);
			 
	    }//end of if(storedEvents.length>0){
	    	
	    }
	        

	    /*
	     
	     */
	    String getServerURL() { 
	    	return serverURL_; 
	    }
	    
	    /*
	     
	     */
	    CLPEventPref getCLPEventPref() {
	    	return clpEventPref; 
	    	
	    }
	    /*
	     
	     */
	    private static AsyncHttpClient getClient() {
	        sClient.setTimeout(CONNECTION_TIMEOUT);
	        aClient.setTimeout(CONNECTION_TIMEOUT);
	        // Return the synchronous HTTP client when the thread is not prepared
	        if (Looper.myLooper() == null)
	            return sClient;
	        return aClient;
	    }
	   
}
