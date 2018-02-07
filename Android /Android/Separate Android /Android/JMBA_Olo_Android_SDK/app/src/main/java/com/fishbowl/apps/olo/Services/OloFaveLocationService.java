package com.fishbowl.apps.olo.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fishbowl.apps.olo.Interfaces.OloAddUserFaveLocationCallback;
import com.fishbowl.apps.olo.Interfaces.OloDeleteUserFaveLocationCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserFaveLocationsCallback;
import com.fishbowl.apps.olo.Models.OloFaveLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public class OloFaveLocationService
{
    public static void getUserFaveLocations(final OloUserFaveLocationsCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserFaveLocationsCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/favelocations";
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                ArrayList<OloFaveLocation> list = null;
                if (response != null)
                {
                    try
                    {
                        Type listType = new TypeToken<ArrayList<OloFaveLocation>>()
                        {
                        }.getType();
                        JSONArray jsonArray = response.getJSONArray("favelocations");
                        Gson gson = new Gson();
                        list = gson.fromJson(jsonArray.toString(), listType);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    if (list == null)
                    {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onUserFaveLocationsCallback(list, error);

                }
            }
        });
    }

    public static void addUserFaveLocation(int vendorId, final OloAddUserFaveLocationCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onAddUserFaveLocationCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/favelocations/" + vendorId;
        OloService.getInstance().post(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                OloFaveLocation location = null;
                if (response != null)
                {
                    Gson gson = new Gson();
                    location = gson.fromJson(response.toString(), OloFaveLocation.class);
                    if (location == null)
                    {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onAddUserFaveLocationCallback(location, error);

                }
            }
        });
    }

    public static void deleteUserFaveLocation(int vendorId, final OloDeleteUserFaveLocationCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onDeleteUserFaveLocationCallback(new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/favelocations/" + vendorId;
        OloService.getInstance().delete(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    callback.onDeleteUserFaveLocationCallback(error);

                }
            }
        });
    }

}
