package com.fishbowl.basicmodule.Analytics;

import android.content.Context;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;

import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by digvijay(dj)
 */
public class FBAnalytic
{

    public static final String TAG = "CLPAnalytic";
    public   int   EVENT_QUEUE_SIZE_THRESHOLD=10;
    public   int   TIMER_DELAY_IN_SECONDS=2;

    public static enum ClypAnalyticMessagingMode
    {
        TEST,
        PRODUCTION,
    }

    private FBServiceQueue serviceQueue;
    private ScheduledExecutorService timerService;
    private FBEventQueue FBEventQueue;
    // private FBAnalytic.ClypAnalyticMessagingMode messagingMode;
    private Context context;
    static FBAnalytic instance;
    private FBEventSettings FBEventSettings;

    //Constructor
    public FBAnalytic()
    {
        //1
        //Creation Of serviceQueue
        serviceQueue = new FBServiceQueue();
        timerService = Executors.newSingleThreadScheduledExecutor();
        timerService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                onTimer();
            }
        }, TIMER_DELAY_IN_SECONDS, TIMER_DELAY_IN_SECONDS, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unused")
    private static class SingletonHolder
    {

    }

    public static FBAnalytic sharedInstance()
    {
        if (instance==null){

            instance=new FBAnalytic();

        }
        return instance;
    }

    public FBAnalytic init(Context _context, final String serverURL, final String appKey) {

        if (_context == null) {
            throw new IllegalArgumentException("valid context is required");
        }
        if (!isValidURL(serverURL)) {
            throw new IllegalArgumentException("valid serverURL is required");
        }

        if (FBEventQueue != null && (!serviceQueue.getServerURL().equals(serverURL) )) {
            throw new IllegalStateException("FBAnalytic cannot be reinitialized with different values");
        }

        //2 Creation Of Preference
        final FBEventPreference clpEventPref = new FBEventPreference(_context);
        serviceQueue.setServerURL(serverURL);
        serviceQueue.setAppKey(appKey);
        serviceQueue.setCLPEventPref(clpEventPref);

        //3 Creation Of Event Queue
        FBEventQueue = new FBEventQueue(clpEventPref);
        context = _context;
        serviceQueue.setContext(context);


        return this;
    }

    public FBEventSettings getFBEventSettings()
    {
        return FBEventSettings;
    }

    public void setFBEventSettings(FBEventSettings FBEventSettings)
    {
        this.FBEventSettings = FBEventSettings;
    }

    public void initEventSettings(JSONObject jsonObject){
        FBEventSettings =new FBEventSettings();
        FBEventSettings.initFromJson(jsonObject);
    }

    public synchronized boolean isInitialized()
    {
        return FBEventQueue != null;
    }

    public synchronized void recordEvent(JSONObject object) {

        try {
            String eventName=object.getString("event_name");
            FBSdk fbSdk = FBSdk.sharedInstance(context);
            final FBSdkData FBSdkData = fbSdk.getFBSdkData();
            if(FBEventSettings.isAvailableEvent(eventName)){
                if(FBMobileSettingService.sharedInstance().mobileSettings.TRIGGER_APP_EVENTS == 1) {
                    if (!isInitialized()) {
                        throw new IllegalStateException("FBAnalytic.sharedInstance().init must be called before recordEvent");
                    }
                    FBEventQueue.recordEvent(object);
                    sendEventsIfNeeded();
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    void sendEventsIfNeeded() {
        if (FBEventQueue.size() >= EVENT_QUEUE_SIZE_THRESHOLD) {
            serviceQueue.recordEvents(FBEventQueue.clpEventsService());
        }
    }

    public synchronized void onTimer()
    {

        if (FBEventQueue.size() > 0) {
            serviceQueue.recordEvents(FBEventQueue.clpEventsService());
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


    static boolean isValidURL(final String urlStr)
    {
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


    void setConnectionQueue (FBServiceQueue _serviceQueue)
    {
        serviceQueue = _serviceQueue;

    }
    ExecutorService getTimerService() { return timerService; }
    FBEventQueue getEventQueue() { return FBEventQueue; }
    void setEventQueue( FBEventQueue _FBEventQueue) { FBEventQueue = _FBEventQueue; }

    ////////////////////////////

}
