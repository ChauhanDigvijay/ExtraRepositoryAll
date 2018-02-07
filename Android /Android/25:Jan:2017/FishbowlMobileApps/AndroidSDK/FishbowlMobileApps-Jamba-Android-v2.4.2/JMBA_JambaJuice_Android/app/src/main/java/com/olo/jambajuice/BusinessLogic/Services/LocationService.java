package com.olo.jambajuice.BusinessLogic.Services;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationSearchCallback;
import com.olo.jambajuice.JambaApplication;
import com.wearehathway.apps.spendgo.Utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Nauman Afzaal on 20/05/15.
 */
public class LocationService {
    private static JsonObjectRequest geoCodingRequest;

    public static void searchZipForLocation(double latitude, double longitude, LocationSearchCallback callback) {
        String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";
        getLocationInfo(url, callback);
    }

    public static void searchLocationForString(String location, LocationSearchCallback callback) {
        try {
            location = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://maps.google.com/maps/api/geocode/json?address=" + location + "&sensor=true";
        getLocationInfo(url, callback);
    }

    private static void getLocationInfo(String url, final LocationSearchCallback callback) {
        if (geoCodingRequest != null) {
            geoCodingRequest.cancel();
            geoCodingRequest = null;
        }
        RequestQueue queue = Volley.newRequestQueue(JambaApplication.getAppContext());
        geoCodingRequest = new JsonObjectRequest(Method.GET, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Logger.i("Response: " + response);
                JSONObject address = null;
                try {
                    JSONArray results = (JSONArray) response.get("results");
                    if (results != null && results.length() > 0) {
                        address = (JSONObject) results.get(0);
                    }

                } catch (Exception ex) {
                }
                callback.onSearchLocationCallback(address);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.i("Error: " + error);
                callback.onSearchLocationCallback(null);
            }
        });
        queue.add(geoCodingRequest);
    }
}
