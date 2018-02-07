package com.wearehathway.apps.spendgo.Services;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoInvalidAuthToken;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoJsonService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSessionService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSignOffService;
import com.wearehathway.apps.spendgo.Models.SpendGoOptional;
import com.wearehathway.apps.spendgo.Models.SpendGoSession;
import com.wearehathway.apps.spendgo.Models.SpendGoUser;
import com.wearehathway.apps.spendgo.Utils.SpendGoConstants;
import com.wearehathway.apps.spendgo.Utils.SpendGoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoSessionService
{
    public static ISpendGoInvalidAuthToken invalidAuthTokenCallback;

    public static void notifyInvalidAuthTokenCallback()
    {
        clearCurrentSession();
        if(invalidAuthTokenCallback != null)
        {
            invalidAuthTokenCallback.onInvalidAuthTokenCallback();
        }
    }

    public final static SpendGoSession currentSession = new SpendGoSession();

    public static void setSessionParams(String spendGoId, String authToken)
    {
        currentSession.setSpendgo_id(spendGoId);
        currentSession.setAuthToken(authToken);
    }
    public static void clearCurrentSession()
    {
        currentSession.clearSession();
    }

    public static void addUser(SpendGoUser user, String password, String favoriteStoreCode, boolean sendWelcomeEmail, boolean emailValidated, final ISpendGoSessionService callback)
    {
        String path = "add";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("phone", user.getPhone());
        parameters.put("sms_opt_in", user.isSms_opt_in());
        parameters.put("password", password);
        parameters.put("email", user.getEmail());
        parameters.put("email_opt_in", user.isEmail_opt_in());
        parameters.put("first_name", user.getFirst_name());
        parameters.put("last_name", user.getLast_name());
        parameters.put("dob", user.getDob());// YYYYMMDD
        parameters.put("gender", user.getGender());// (optional ­ “M” | “F”)
        parameters.put("marital_status", user.getMarital_status());//(optional ­ “Single” | “Married” | “Divorced” | “Domestic Partner”)
        parameters.put("street", user.getStreet());
        parameters.put("state", user.getState());
        parameters.put("city", user.getCity());
        parameters.put("zip", user.getZip());
        parameters.put("favorite_store_code", favoriteStoreCode);
        JSONArray additionalInfoArray = new JSONArray();
        ArrayList<SpendGoOptional> userAdditionalInfo = user.getAddtl_info();
        if (userAdditionalInfo != null)
        {
            for (SpendGoOptional optional : userAdditionalInfo)
            {
                try
                {
                    JSONObject additionalInfo = new JSONObject();
                    additionalInfo.put("name", optional.getName());
                    additionalInfo.put("value", optional.getValue());
                    additionalInfoArray.put(additionalInfo);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        parameters.put("addtl_info", additionalInfoArray);
        parameters.put("send_welcome_email", sendWelcomeEmail);
        parameters.put("email_validated", emailValidated);

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void signIn(String phoneorusername, String password, final ISpendGoSessionService callback)
    {
        String path = "signin";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("value", phoneorusername);
        parameters.put("password", password);
        parameters.put("device_id", SpendGoUtils.getUniquePsuedoID());

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void signOff(String authToken, final ISpendGoSignOffService callback)
    {
        String path = "signoff";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("auth_token", authToken);
        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                callback.onSignOffCallback(error);
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, ISpendGoSessionService callback)
    {
        if (response == null && error == null)
        {
            error = new VolleyError(new NetworkResponse(SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED.value, null, null, false));
        }
        else if (response != null)
        {
            try
            {
                String spendgo_id = response.getString("spendgo_id");
                String auth_token = response.getString("auth_token");
                currentSession.setSpendgo_id(spendgo_id);
                currentSession.setAuthToken(auth_token);
            } catch (Exception ex)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onSessionServiceCallback(currentSession, error);
        }
    }
}
