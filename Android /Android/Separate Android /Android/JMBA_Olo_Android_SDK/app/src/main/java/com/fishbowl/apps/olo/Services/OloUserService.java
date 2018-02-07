package com.fishbowl.apps.olo.Services;

import com.google.gson.Gson;
import com.fishbowl.apps.olo.Interfaces.OloBillingAccountCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserContactDetailCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserOrderServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserPostServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserServiceCallback;
import com.fishbowl.apps.olo.Models.OloBillingAccount;
import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Models.OloUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.apps.olo.Utils.Constants.FORGOT_PASSWORD;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloUserService
{
    public static void changePassword(String currentPassword, String newPassword, final OloUserPostServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserPostCallback(false, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/password";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("currentpassword", currentPassword);
        parameters.put("newpassword", newPassword);

        OloService.getInstance().post(path, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                boolean isPasswordChanged = error == null;
                if (callback != null)
                {
                    callback.onUserPostCallback(isPasswordChanged, error);
                }
            }
        });
    }

    public static void forgotPassword(String email, final OloUserPostServiceCallback callback)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("emailaddress", email);

        OloService.getInstance().post(FORGOT_PASSWORD, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                boolean isPasswordChanged = error == null;
                if (callback != null)
                {
                    callback.onUserPostCallback(isPasswordChanged, error);
                }
            }
        });
    }

    public static void getUserInformation(final OloUserServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserServiceCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken;
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                if (callback != null) {
                    parseResponse(response, error, callback);
                }
            }
        });
    }

    public static void updateUserInformation(OloUser user, final OloUserServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserServiceCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", user.getFirstname());
        parameters.put("lastname", user.getLastname());
        parameters.put("cardsuffix", user.getCardsuffix());
        parameters.put("emailaddress", user.getEmailaddress());

        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                if (callback != null) {
                    parseResponse(response, error, callback);
                }
            }
        });
    }

    public static void addCreditCard(String cardNumber, int expiryMonth, int expiryYear, String cvv, String zip, final OloUserPostServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserPostCallback(false, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/creditcard";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("cardnumber", cardNumber);
        parameters.put("expirymonth", expiryMonth);
        parameters.put("expiryyear", expiryYear);
        parameters.put("cvv", cvv);
        parameters.put("zip", zip);
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                boolean isSucces = error == null;
                if (callback != null) {
                    callback.onUserPostCallback(isSucces, error);
                }
            }
        });
    }

    public static void deleteCreditCard(String cardNumber, final OloUserPostServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserPostCallback(false, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/creditcard";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("path", cardNumber);
        OloService.getInstance().delete(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                boolean isSucces = error == null;
                if (callback != null) {
                    callback.onUserPostCallback(isSucces, error);
                }
            }
        });
    }

    public static void getRecentOrders(final OloUserOrderServiceCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserOrderServiceCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/recentorders";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                OloOrderStatus[] orderStatuses = null;
                if (response != null) {
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("orders");
                        orderStatuses = gson.fromJson(jsonArray.toString(), OloOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserOrderServiceCallback(orderStatuses, error);
                }
            }
        });
    }

    public static void getUserContactDetail(final OloUserContactDetailCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserContactDetailCallback(null, new Exception("User is not authenticated."));
            return;
        }

        String path = "users/" + authToken + "/contactdetails";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                String contactdetails = null;
                if (response != null) {
                    try {
                        contactdetails = response.getString("contactdetails");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserContactDetailCallback(contactdetails, error);
                }
            }
        });
    }

    public static void updateUserContactDetail(String contactDetail, final OloUserContactDetailCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null)
        {
            callback.onUserContactDetailCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/contactdetails";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("contactdetails", contactDetail);
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                String contactdetails = null;
                if (response != null) {
                    try {
                        contactdetails = response.getString("contactdetails");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserContactDetailCallback(contactdetails, error);
                }
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, OloUserServiceCallback callback)
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
            if (user == null)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onUserServiceCallback(user, error);
        }
    }

    public static void getBillingAccount(final OloBillingAccountCallback callback)
    {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if(authToken == null)
        {
            callback.onBillingAccountCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/billingaccounts";
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                OloBillingAccount[] accounts = null;
                if (response != null)
                {
                    Gson gson = new Gson();
                    try
                    {
                        JSONArray jsonArray = response.getJSONArray("userbillingaccounts");
                        accounts = gson.fromJson(jsonArray.toString(), OloBillingAccount[].class);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onBillingAccountCallback(accounts, error);
                }
            }
        });
    }
}
