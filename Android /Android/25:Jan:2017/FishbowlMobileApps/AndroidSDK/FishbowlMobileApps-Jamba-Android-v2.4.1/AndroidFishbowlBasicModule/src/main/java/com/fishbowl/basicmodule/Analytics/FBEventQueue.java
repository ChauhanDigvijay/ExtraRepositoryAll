package com.fishbowl.basicmodule.Analytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by digvijay(dj)
 */

public class FBEventQueue {
	 private final FBEventPreference clpEventPref;

	    /**
	     * Constructs an FBEventQueue.
	     */
	 FBEventQueue(final FBEventPreference _clpEventPref) {
		 clpEventPref = _clpEventPref;
	    }

	    /**
	     * Returns the number of events in the local event queue.
	     * @return the number of events in the local event queue
	     */

	    int size() {
	        return clpEventPref.clpEventsFromPref().length;
	    }

	    String clpEventsService() {
	        String result;

	         String[] clpEvents = clpEventPref.clpEventsFromPref();

	         JSONArray eventArray = new JSONArray();
	        
	        for (String e : clpEvents) {
	            try {
					eventArray.put(new JSONObject(e));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
	        }

			clpEventPref.removeEvents(clpEvents);
	        final JSONObject finalObject=new JSONObject();

			try {
				finalObject.put("mobileAppEvent", eventArray);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			result = finalObject.toString();
			return result;
	    }

	    void recordEvent(JSONObject object) {
	        clpEventPref.addEvent(object);
	    }

	    // for unit tests
	    FBEventPreference getClpEventPref() {
	        return clpEventPref;
	    }
}
