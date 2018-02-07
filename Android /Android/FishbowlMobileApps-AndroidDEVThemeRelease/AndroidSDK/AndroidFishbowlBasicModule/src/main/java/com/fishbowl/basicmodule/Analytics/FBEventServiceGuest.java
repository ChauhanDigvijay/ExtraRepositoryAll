package com.fishbowl.basicmodule.Analytics;

import android.content.Context;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBServiceArrayCallback;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBTokenService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by digvijay(dj)
 */
public class FBEventServiceGuest implements Runnable {
    private static int CONNECTION_TIMEOUT = 120000;
    private static final String topic = "test-kafka";
    private final FBEventPreference clpEventPref;
    private final String serverURL_;
    private  String appKey;
    private Context context;


    FBEventServiceGuest(Context _context, String serverURL, String _appKey, final FBEventPreference store) {
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
            Log.i("FBEVENT SERVICE", eventData);
            entity =  eventData;

            FBService.getInstance().makeCustomArrayRequest(FBConstant.FBGuestAnalyticApi,entity,getHeader(), new FBServiceArrayCallback()
            {
                @Override
                public void onFBServiceArrayCallback(JSONArray response, Exception error, String errorMessage)
                {

                    if (error == null && response != null)
                    {

                        try {
                            int i=0;
                            if (response.length() > 0)
                            {
                                JSONObject invalidDetails = (JSONObject)response.get(i);
                                if (invalidDetails.has("successFlag") && invalidDetails.getBoolean("successFlag") == false &&invalidDetails.has("message") )
                                {
                                    if (invalidDetails.getString("message").equalsIgnoreCase(FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN))
                                    {
                                        FBTokenService.sharedInstance(FBSdk.sharedInstance(FBEventServiceGuest.this.context)).getTokenFBAnalytic(eventData);
                                    }
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }
            });


            clpEventPref.removeConnection(eventData);
        }

    }



    String getServerURL() {
        return serverURL_;
    }


    FBEventPreference getCLPEventPref() {
        return clpEventPref;
    }

    HashMap<String,String> getHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        //   header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(context).getAccessToken());
        // header.put("tenantName","fishbowl");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(context));
        return header;
    }
}
