package com.fishbowl.basicmodule.Analytics;


import android.content.Context;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBTokenService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBEventService implements Runnable {
	    private static int CONNECTION_TIMEOUT = 120000;
//	private static Producer<String, String> producer;
	private static final String topic = "test-kafka";
	    private final FBEventPreference clpEventPref;
	    private final String serverURL_;
	    private  String appKey;
	    private Context context;
 

	    FBEventService(Context _context, String serverURL, String _appKey, final FBEventPreference store) {
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
           String  entity = null;
            Log.i("CLPEVENT SERVICE", eventData);
		    entity =  eventData;


			FBService.getInstance().post(FBConstant.ClpAnalyticApi, entity,getHeader(), new FBServiceCallback(){
				   public void onServiceCallback(JSONObject response, Exception error, String errorMessage){
					  if(error==null&&response!=null){
						  FBToastService.sharedInstance().show("Event List send to server");
					  }
					  else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN))){
						   FBTokenService.sharedInstance(FBSdk.sharedInstance(FBEventService.this.context)).getTokenClpAnalytic();
					   }

					   else{

					  }
			}
			});
			 //Remove That event has been sent
			 clpEventPref.removeConnection(eventData);

	    }//end of if(storedEvents.length>0){


			//initialize();
	    }

	    /*
	     
	    */
	    String getServerURL() { 
	    	return serverURL_; 
	    }

	    /*
	     
	    */

	    FBEventPreference getCLPEventPref() {
	    	return clpEventPref; 
		}

		HashMap<String,String> getHeader(){
			HashMap<String,String> header=new HashMap<String, String>();
			header.put("Content-Type","application/json");
			header.put("Application","mobilesdk");
			header.put("client_id", FBConstant.client_id);
			header.put("client_secret", FBConstant.client_secret);
			header.put("access_token", FBPreferences.sharedInstance(context).getAccessTokenforapp());
			header.put("tenantName","fishbowl");
			header.put("tenantid","1173");
			header.put("deviceId", FBUtility.getAndroidDeviceID(context));
			return header;
		}
//	public static void initialize() {
//		Properties props = new Properties();
//		props.put("bootstrap.servers", "10.80.0.60:9092");
//		props.put("acks", "all");
//		props.put("retries", 0);
//		props.put("batch.size", 16384);
//		props.put("linger.ms", 1);
//		// props.put("buffer.memory", 33554432);
//		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		producer = new KafkaProducer<String, String>(props);
//		// producer = new KafkaProducer<>(props);
//		List<PartitionInfo> partitionsFor = producer.partitionsFor(topic);
//		Map<MetricName, ? extends Metric> metrics = producer.metrics();
//		System.out.println(metrics);
//
//
//		for(int i = 0; i < 100; i++) {
////
//			producer.send(new ProducerRecord<String, String>(topic, Integer.toString(i), Integer.toString(i)));
//			System.out.println("Send " + i) ;
//		}
////
//		producer.close();
//
//	}
}
