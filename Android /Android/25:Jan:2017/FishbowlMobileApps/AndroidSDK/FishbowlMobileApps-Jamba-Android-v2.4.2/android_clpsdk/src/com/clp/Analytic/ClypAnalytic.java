package com.clp.Analytic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.content.Context;
 

/**
Created By mohd Vaseem
*/
public class ClypAnalytic {

    public static final String TAG = "CLPAnalytic";
    
    public   int   EVENT_QUEUE_SIZE_THRESHOLD=10;
	public   int   TIMER_DELAY_IN_SECONDS=60;
 
 
    public static enum ClypAnalyticMessagingMode {
        TEST,
        PRODUCTION,
    }
 

    private CLPServiceQueue serviceQueue;
    
    private ScheduledExecutorService timerService;
    private CLPEventQueue clpEventQueue;
   // private ClypAnalytic.ClypAnalyticMessagingMode messagingMode;
    private Context context;


    static ClypAnalytic instance;
    private CLPEventSettings clpEventSettings;

    //Constructor
    public  ClypAnalytic() {
    	//1
       //Creation Of serviceQueue
    	serviceQueue = new CLPServiceQueue();
        timerService = Executors.newSingleThreadScheduledExecutor();
        timerService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                onTimer();
            }
        }, TIMER_DELAY_IN_SECONDS, TIMER_DELAY_IN_SECONDS, TimeUnit.SECONDS);
    	 
    }
    @SuppressWarnings("unused")
	private static class SingletonHolder {
        
    }
    
    public static ClypAnalytic sharedInstance() {
    	if (instance==null){
    		
    		instance=new ClypAnalytic();
 
    	}
   
        return instance;
    }
     
    public ClypAnalytic init( Context _context, final String serverURL, final String appKey) {
      	 if (_context == null) {
            throw new IllegalArgumentException("valid context is required");
        }
        if (!isValidURL(serverURL)) {
            throw new IllegalArgumentException("valid serverURL is required");
        }
     
        if (clpEventQueue != null && (!serviceQueue.getServerURL().equals(serverURL) )) {
            throw new IllegalStateException("ClypAnalytic cannot be reinitialized with different values");
        }
   
          //2 Creation Of Preference
           final CLPEventPref clpEventPref = new CLPEventPref(_context);
            serviceQueue.setServerURL(serverURL);
            serviceQueue.setAppKey(appKey);
            serviceQueue.setCLPEventPref(clpEventPref);
            
          //3 Creation Of Event Queue
            clpEventQueue = new CLPEventQueue(clpEventPref);
            context = _context;
            serviceQueue.setContext(context);

        return this;
  }

    public CLPEventSettings getClpEventSettings() {
		return clpEventSettings;
	}

	public void setClpEventSettings(CLPEventSettings clpEventSettings) {
		this.clpEventSettings = clpEventSettings;
	}

	

    public void initEventSettings(JSONObject jsonObject){
        clpEventSettings=new CLPEventSettings();
        clpEventSettings.initFromJson(jsonObject);
    }
  
   
 
    public synchronized boolean isInitialized() {
        return clpEventQueue != null;
    }


 
 
    public synchronized void recordEvent(JSONObject object) {
    	
    	try {
			
		
    	String eventName=object.getString("event_name");

    	if(clpEventSettings.isAvailableEvent(eventName)){

        if (!isInitialized()) {
            throw new IllegalStateException("ClypAnalytic.sharedInstance().init must be called before recordEvent");
        }
        clpEventQueue.recordEvent(object);
        sendEventsIfNeeded();

    	}
    	
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }
 


    void sendEventsIfNeeded() {
        if (clpEventQueue.size() >= EVENT_QUEUE_SIZE_THRESHOLD) {
            serviceQueue.recordEvents(clpEventQueue.clpEventsService());
        }
    }
 
    synchronized void onTimer() {
    

            if (clpEventQueue.size() > 0) {
                serviceQueue.recordEvents(clpEventQueue.clpEventsService());
            }
       
    }

   
    static int currentTimestamp() {
        return ((int)(System.currentTimeMillis() / 1000l));
    }

   
    static int currentHour(){return Calendar.getInstance().get(Calendar.HOUR_OF_DAY); }
 
    static int currentDayOfWeek(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
        }
        return 0;
    }

   
    static boolean isValidURL(final String urlStr) {
        boolean validURL = false;
        if (urlStr != null && urlStr.length() > 0) {
            try {
                new URL(urlStr);
                validURL = true;
            }
            catch (MalformedURLException e) {
                validURL = false;
            }
        }
        return validURL;
    }


    void setConnectionQueue (CLPServiceQueue _serviceQueue) { 
    	serviceQueue = _serviceQueue; 
    	
    }
    ExecutorService getTimerService() { return timerService; }
    CLPEventQueue getEventQueue() { return clpEventQueue; }
    void setEventQueue( CLPEventQueue _clpEventQueue) { clpEventQueue = _clpEventQueue; }
 

}
