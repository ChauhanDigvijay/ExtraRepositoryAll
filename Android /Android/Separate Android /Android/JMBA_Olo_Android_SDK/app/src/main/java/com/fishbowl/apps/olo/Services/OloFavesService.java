package com.fishbowl.apps.olo.Services;

import com.google.gson.Gson;
import com.fishbowl.apps.olo.Interfaces.OloDeleteUserFaveCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserFavesCallback;
import com.fishbowl.apps.olo.Models.OloFave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloFavesService
{
    public static void getUserFaves(final OloUserFavesCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserFavesCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves";
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    parseResponse(response, error, callback);
                }
            }
        });
    }

    public static void addUserFaves(String basketId, String description, final OloUserFavesCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserFavesCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("basketid", basketId);
        parameters.put("description", description);
        OloService.getInstance().post(path, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    parseResponse(response, error, callback);
                }
            }
        });
    }

    public static void deleteUserFaves(int faveId, final OloDeleteUserFaveCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onDeleteUserFaveCallback(new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves/" + faveId;
        OloService.getInstance().delete(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    callback.onDeleteUserFaveCallback(error);
                }
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception exception, OloUserFavesCallback callback)
    {
        OloFave[] faves = null;
        if (response != null)
        {
            Gson gson = new Gson();
            try
            {
                faves = gson.fromJson(response.getJSONArray("faves").toString(), OloFave[].class);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            if (faves == null)
            {
                exception = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onUserFavesCallback(faves, exception);
        }
    }
}
