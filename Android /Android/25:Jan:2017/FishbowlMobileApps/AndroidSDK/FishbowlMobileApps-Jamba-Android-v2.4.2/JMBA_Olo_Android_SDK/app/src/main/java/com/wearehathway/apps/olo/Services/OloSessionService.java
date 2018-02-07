package com.wearehathway.apps.olo.Services;

import com.google.gson.Gson;
import com.wearehathway.apps.olo.Interfaces.OloInvalidAuthCallback;
import com.wearehathway.apps.olo.Interfaces.OloServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloSessionAuthTokenCallback;
import com.wearehathway.apps.olo.Interfaces.OloSessionServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloUserLogoutCallback;
import com.wearehathway.apps.olo.Models.OloSession;
import com.wearehathway.apps.olo.Models.OloUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.wearehathway.apps.olo.Utils.Constants.AUTHENTICATE_USER;
import static com.wearehathway.apps.olo.Utils.Constants.CREATE_USER;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloSessionService
{
    public static OloInvalidAuthCallback invalidAuthTokenCallback;

    public static void notifyInValidAuthTokenCalback()
    {
        clearSession();
        if(invalidAuthTokenCallback != null)
        {
            invalidAuthTokenCallback.onInvalidAuthTokenCallback();
        }
    }

    public final static OloSession currentSesstion = new OloSession();

    public static void setSessionParams(String authToken)
    {
        currentSesstion.setAuthToken(authToken);
    }

    public static void clearSession()
    {
        currentSesstion.clearSession();
    }

    public static void createUser(String firstName, String lastName, String contactNumber, String emailAddress, String password, final OloSessionServiceCallback callback)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", firstName);
        parameters.put("lastname", lastName);
        parameters.put("emailaddress", emailAddress);
        parameters.put("contactnumber", contactNumber);
        parameters.put("password", password);

        OloService.getInstance().post(CREATE_USER, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void authenticateUser(String email, String password, final OloSessionServiceCallback callback)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("login", email);
        parameters.put("password", password);

        OloService.getInstance().post(AUTHENTICATE_USER, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }


    public static void logOutUser(final OloUserLogoutCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserLogoutCallback(new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + currentSesstion.getAuthToken();
        OloService.getInstance().delete(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (error == null)
                {
                    currentSesstion.clearSession();
                }
                if (callback != null)
                {
                    callback.onUserLogoutCallback(error);
                }
            }
        });

    }

    public static void getOrCreateUser(String provider, String providerToken, String contactNumber, String basketId, final OloSessionAuthTokenCallback callback)
    {
        String path = "/users/getorcreate";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("provider", provider);
        parameters.put("providertoken", providerToken);
        parameters.put("contactnumber", contactNumber);
        parameters.put("basketid", basketId);
        OloService.getInstance().post(path, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                String authToken = "";
                try
                {
                    if (response != null)
                    {
                        authToken = response.getString("authtoken");
                        currentSesstion.setAuthToken(authToken);
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    error = new Exception("Error occurred while parsing data.");
                }
                if (callback != null)
                {
                    callback.onOloSessionAuthTokenCallback(authToken, error);
                }
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, OloSessionServiceCallback callback)
    {
        OloUser user = null;
        if (response != null)
        {
            Gson gson = new Gson();
            if (response.has("user"))
            {
                try
                {
                    response = response.getJSONObject("user");
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            user = gson.fromJson(response.toString(), OloUser.class);
            currentSesstion.setAuthToken(user.getAuthtoken());
            if (user == null)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onSessionServiceCallback(user, error);
        }
    }
}
