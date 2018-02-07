package com.wearehathway.apps.spendgo.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoStoreService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoStringService;
import com.wearehathway.apps.spendgo.Models.SpendGoStore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoStoreService
{
    public static void findNearestStores(double latitude, double longitude, double distance, final ISpendGoStoreService callback)
    {
        String path = "nearestStores";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);
        parameters.put("distance", distance);
        SpendGoService.getInstance().post(path, parameters, new ISpendGoStringService()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                parseRespponseAndNotifiy(response, error, callback);
            }
        });
    }

    public static void findNearestStores(String zipCode, double distance, final ISpendGoStoreService callback)
    {
        String path = "nearestStores";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("zip", zipCode);
        parameters.put("distance", distance);
        SpendGoService.getInstance().post(path, parameters, new ISpendGoStringService()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                parseRespponseAndNotifiy(response, error, callback);
            }
        });
    }

    private static void parseRespponseAndNotifiy(String response, Exception error, final ISpendGoStoreService callback)
    {
        ArrayList<SpendGoStore> stores = null;
        if (response != null)
        {
            try
            {
                Gson gson = new Gson();
                stores = gson.fromJson(response, new TypeToken<ArrayList<SpendGoStore>>()
                {
                }.getType());
            }
            catch (Exception ex)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onSpendGoStoreServiceCallback(stores, error);
        }
    }
}
