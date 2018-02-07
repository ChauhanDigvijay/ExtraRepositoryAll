package com.wearehathway.apps.spendgo.Services;

import android.text.TextUtils;

import com.wearehathway.apps.spendgo.Interfaces.ISpendGoJsonService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoLookUpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 12/05/15.
 */
public class SpendGoLookUpService
{
    public static void lookUp(String email, String phone, final ISpendGoLookUpService callback)
    {
        String path = "lookup";
        HashMap<String, Object> parameters = new HashMap<>();

        if (!TextUtils.isEmpty(email))
        {
            parameters.put("email", email);
        }
        else if (!TextUtils.isEmpty(phone))
        {
            parameters.put("phone", phone);
        }
        SpendGoService.getInstance().post(path, parameters, new ISpendGoJsonService()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                String status = null;
                if (response != null)
                {
                    try
                    {
                        status = response.getString("status");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (callback != null)
                {
                    callback.onLookUpServiceCallback(status, error);
                }

            }
        });
    }
}
