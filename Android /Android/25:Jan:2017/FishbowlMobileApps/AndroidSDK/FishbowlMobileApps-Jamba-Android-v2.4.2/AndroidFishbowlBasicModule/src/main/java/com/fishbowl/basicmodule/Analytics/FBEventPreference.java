package com.fishbowl.basicmodule.Analytics;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by digvijay(dj)
 */
public class FBEventPreference {
	    private static final String PREFERENCES = "CLP_PREF";
	    private static final String DELIMITER = ":::";
	    private static final String CONNECTIONS_PREFERENCE = "CONNECTIONS";
	    private static final String EVENTS_PREFERENCE = "EVENTS";
	   // private static final String LOCATION_PREFERENCE = "LOCATION";

	    private final SharedPreferences preferences_;

	   
	    FBEventPreference(Context context) {
	        if (context == null) {
	            throw new IllegalArgumentException("must provide valid context");
	        }
	        preferences_ = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	    }

	   
	    public String[] connectionsFromPref() {

	        final String joinedConnStr = preferences_.getString(CONNECTIONS_PREFERENCE, "");
	        return joinedConnStr.length() == 0 ? new String[0] : joinedConnStr.split(DELIMITER);
	    }

	    
	    public String[] clpEventsFromPref() {
	        final String joinedEventsStr = preferences_.getString(EVENTS_PREFERENCE, "");
	        return joinedEventsStr.length() == 0 ? new String[0] : joinedEventsStr.split(DELIMITER);
	    }
	   
 
	    public boolean isEmptyConnections() {
	        return preferences_.getString(CONNECTIONS_PREFERENCE, "").length() == 0;
	    }

	    
	    public synchronized void addConnection(final String str) {

	        if (str != null && str.length() > 0) {
	            final List<String> connections = new ArrayList<String>(Arrays.asList(connectionsFromPref()));
	            connections.add(str);
	            preferences_.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
	        }
	    }
 
	    public synchronized void removeConnection(final String str) {
	        if (str != null && str.length() > 0) {
	            final List<String> connections = new ArrayList<String>(Arrays.asList(connectionsFromPref()));
	            if (connections.remove(str)) {
	                preferences_.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
	            }
	        }
	    }

	public synchronized void removeEvents(String []  eventsToRemove) {
		if (eventsToRemove != null && eventsToRemove.length > 0) {

   			final List<String> evnets = new ArrayList<String>(Arrays.asList(clpEventsFromPref()));

			if (evnets.removeAll(new ArrayList<String>(Arrays.asList(eventsToRemove)))) {

				preferences_.edit().putString(EVENTS_PREFERENCE, join(evnets, DELIMITER)).commit();
			}

		}
	}



	  void addEvent(JSONObject object) {
	    	final String[] array = clpEventsFromPref();

	        preferences_.edit().putString(EVENTS_PREFERENCE,joinEvents(array, object)).commit();
	    }

	    
 
	   // Convert CLPEvent to string and then append all in one string
	    static String joinEvents(String[] array, JSONObject object) {
	    	
	        final List<String> strings = new ArrayList<String>();
	        
	        for (int i = 0; i < array.length; i++) {
	        	strings.add(array[i]);
			}
	        
	        strings.add(object.toString());
	        
	        return join(strings, DELIMITER);
	    }

	    /**
	     * Joins all the strings in the specified collection into a single string with the specified delimiter.
	     */
	    static String join(List<String> events, final String delimiter) {
	        final StringBuilder builder = new StringBuilder();

	        int i = 0;
	        for (String s : events) {
	            builder.append(s);
	            if (++i < events.size()) {
	                builder.append(delimiter);
	            }
	        }

	        return builder.toString();
	    }

	    
	    public synchronized String getPreference(final String key) {
	        return preferences_.getString(key, null);
	    }

	  
	    public synchronized void setPreference(final String key, final String value) {
	        if (value == null) {
	            preferences_.edit().remove(key).commit();
	        } else {
	            preferences_.edit().putString(key, value).commit();
	        }
	    }

	    // for unit testing
	    synchronized void clear() {
	        final SharedPreferences.Editor prefsEditor = preferences_.edit();
	        prefsEditor.remove(EVENTS_PREFERENCE);
	        prefsEditor.remove(CONNECTIONS_PREFERENCE);
	        prefsEditor.commit();
	    }
}
