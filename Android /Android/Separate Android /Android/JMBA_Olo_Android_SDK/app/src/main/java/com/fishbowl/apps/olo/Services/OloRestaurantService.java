package com.fishbowl.apps.olo.Services;

import com.google.gson.Gson;
import com.fishbowl.apps.olo.Interfaces.OloRestaurantCalendarCallback;
import com.fishbowl.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Models.OloRestaurant;
import com.fishbowl.apps.olo.Models.OloRestaurantCalenders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.fishbowl.apps.olo.Utils.Constants.RESTAURANTS;
import static com.fishbowl.apps.olo.Utils.Constants.RESTAURANTS_NEAR;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class OloRestaurantService
{

    public static void getAllRestaurants(final OloRestaurantServiceCallback callback)
    {
        OloService.getInstance().get(RESTAURANTS, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getAllRestaurantsNear(double latitude, double longitude, double radius, int limit, final OloRestaurantServiceCallback callback)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("lat", latitude);
        parameters.put("long", longitude);
        parameters.put("radius", radius);
        parameters.put("limit", limit);

        OloService.getInstance().get(RESTAURANTS_NEAR, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getRestaurantByRef(String storeCode, final OloRestaurantServiceCallback callback)
    {
        String path = "restaurants/byref/" + storeCode;
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getRestaurantById(int storeId, final OloRestaurantServiceCallback callback)
    {
        String path = "restaurants/" + storeId;
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                OloRestaurant restaurants = null;
                if (response != null) {
                    Gson gson = new Gson();
                    restaurants = gson.fromJson(response.toString(), OloRestaurant.class);
                }
                if (callback != null)
                {
                    OloRestaurant [] restaurantsArray = new OloRestaurant[]{restaurants};
                    callback.onRestaurantServiceCallback(restaurantsArray, error);
                }
            }
        });
    }

    public static void getRestaurantCalendar(int restaurantId, Date fromDate, Date toDate, final OloRestaurantCalendarCallback callback)
    {
        String path = "restaurants/" + restaurantId + "/calendars";
        HashMap<String, Object> parameters = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String from = simpleDateFormat.format(fromDate).toString();
        String to = simpleDateFormat.format(toDate).toString();
        parameters.put("from", from);
        parameters.put("to", to);
        OloService.getInstance().get(path, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                OloRestaurantCalenders calendars = null;
                if (response != null)
                {
                    Gson gson = new Gson();
                    try
                    {
                        calendars = gson.fromJson(response.toString(), OloRestaurantCalenders.class);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onCalendarCallback(calendars, error);
                }
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, OloRestaurantServiceCallback callback)
    {
        OloRestaurant[] restaurants = null;
        if (response != null)
        {
            Gson gson = new Gson();
            try
            {
                JSONArray jsonRestaurantsArray = response.getJSONArray("restaurants");
                restaurants = gson.fromJson(jsonRestaurantsArray.toString(), OloRestaurant[].class);
            } catch (JSONException e)
            {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onRestaurantServiceCallback(restaurants, error);
        }
    }
}
