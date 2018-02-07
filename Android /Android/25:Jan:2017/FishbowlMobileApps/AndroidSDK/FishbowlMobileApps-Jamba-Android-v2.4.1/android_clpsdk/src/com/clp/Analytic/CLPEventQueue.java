package com.clp.Analytic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
Created By mohd Vaseem
*/
public class CLPEventQueue {
	 private final CLPEventPref clpEventPref;

	    /**
	     * Constructs an CLPEventQueue.

	     */
	 CLPEventQueue(final CLPEventPref _clpEventPref) {
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }

			clpEventPref.removeEvents(clpEvents);

	        final JSONObject finalObject=new JSONObject();
			try {
				finalObject.put("mobileAppEvent", eventArray);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


	        result = finalObject.toString();

             return result;
	    }
	    
	   
	    void recordEvent(JSONObject object) {
	    
	        clpEventPref.addEvent(object);
	    }

	    // for unit tests
	    CLPEventPref getClpEventPref() {
	        return clpEventPref;
	    }
}
