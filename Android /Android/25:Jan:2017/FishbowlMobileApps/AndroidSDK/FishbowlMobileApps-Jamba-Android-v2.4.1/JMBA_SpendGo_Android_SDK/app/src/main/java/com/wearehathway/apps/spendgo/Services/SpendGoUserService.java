package com.wearehathway.apps.spendgo.Services;

import com.google.gson.Gson;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoForgotPasswordService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoJsonService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoResetPasswordService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoStringService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoUserService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoUserUpdateService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoVerifyTokenService;
import com.wearehathway.apps.spendgo.Models.SpendGoOptional;
import com.wearehathway.apps.spendgo.Models.SpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Models.SpendGoUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoUserService
{
    public static void updateUser(SpendGoUser user, String password, String favoriteStoreCode, final ISpendGoUserUpdateService callback)
    {
        String path = "update";
        HashMap<String, Object> parameters = new HashMap<>();
        if(isValid(user.getPhone()))
        {
            parameters.put("phone", user.getPhone());
        }
        if(isValid(user.getEmail()))
        {
            parameters.put("email", user.getEmail());
        }
        if(isValid(user.getFirst_name()))
        {
            parameters.put("first_name", user.getFirst_name());
        }
        if(isValid(user.getLast_name()))
        {
            parameters.put("last_name", user.getLast_name());
        }
        if(isValid(user.getDob()))
        {
            parameters.put("dob", user.getDob());// YYYYMMDD
        }
        if(isValid(user.getGender()))
        {
            parameters.put("gender", user.getGender());// (optional ­ “M” | “F”)
        }
        if(isValid(user.getMarital_status()))
        {
            parameters.put("marital_status", user.getMarital_status());//(optional ­ “Single” | “Married” | “Divorced” | “Domestic Partner”)
        }
        if(isValid(user.getStreet()))
        {
            parameters.put("street", user.getStreet());
        }
        if(isValid(user.getState()))
        {
            parameters.put("state", user.getState());
        }
        if(isValid(user.getCity()))
        {
            parameters.put("city", user.getCity());
        }
        if(isValid(user.getZip()))
        {
            parameters.put("zip", user.getZip());
        }
        if(isValid(password))
        {
            parameters.put("password", password);
        }
        if(isValid(favoriteStoreCode))
        {
            parameters.put("favorite_store_code", favoriteStoreCode);
        }
        parameters.put("email_opt_in", user.isEmail_opt_in());
        parameters.put("sms_opt_in", user.isSms_opt_in());
        parameters.put("spendgo_id", SpendGoSessionService.currentSession.getSpendGoId());
        parameters.put("email_validated", true);
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

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    callback.onUserUpdateCallback(error);
                }
            }
        });
    }

    private static boolean isValid(String val)
    {
        return val != null && !val.equals("");
    }
    public static void getMemberWithId(String spendgo_id, final ISpendGoUserService callback)
    {
        String path = "get";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("spendgo_id", spendgo_id);
        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
                {
                    @Override
                    public void onServiceCallback(JSONObject response, Exception error)
                    {
                        {
                            parseResponse(response, error, callback);
                        }
                    }
                });
    }

    public static void forgotPassword(String email, final ISpendGoForgotPasswordService callback)
    {
        String path = "forgotPassword";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);

        SpendGoService.getInstance().post(path, parameters, new ISpendGoStringService()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                if (callback != null)
                {
                    callback.onForgotPasswordCallback(error);
                }
            }
        });
    }

    public static void verifyResetToken(String token, final ISpendGoVerifyTokenService callback)
    {
        String path = "member/verifyResetToken";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("token", token);

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                if (callback != null)
                {
                    callback.onVerifyTokenCallback(error);
                }
            }
        });
    }

    public static void resetPassword(String token, String password, final ISpendGoResetPasswordService callback)
    {
        String path = "member/resetPassword";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("token", token);
        parameters.put("password", password);

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {

                String username = null;
                if (response != null)
                {
                    try
                    {
                        username = response.getString("username");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (callback != null)
                {
                    callback.onResetPasswordCallback(username, error);
                }
            }
        });
    }

    public static void getUserRewardSummary(String spendGoId, String accountId, final ISpendGoRewardSummary callback)
    {
        String path = "rewardsAndOffers";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("spendgo_id", spendGoId);
        parameters.put("account_id", accountId);

        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                SpendGoRewardSummary rewards = null;
                if (response != null)
                {
                    try
                    {
                        Gson gson = new Gson();
                        rewards = gson.fromJson(response.toString(), SpendGoRewardSummary.class);
                    } catch (Exception ex)
                    {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onRewardSummaryCallback(rewards, error);
                }
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, ISpendGoUserService callback)
    {
        SpendGoUser user = null;
        if (response != null)
        {
            try
            {
                Gson gson = new Gson();
                user = gson.fromJson(response.toString(), SpendGoUser.class);
            } catch (Exception ex)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onUserServiceCallback(user, error);
        }
    }

}
