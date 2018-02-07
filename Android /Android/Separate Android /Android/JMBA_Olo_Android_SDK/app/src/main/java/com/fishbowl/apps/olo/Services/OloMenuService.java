package com.fishbowl.apps.olo.Services;

import com.fishbowl.apps.olo.Interfaces.OloMenuServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloProductModifierCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Models.OloCategory;
import com.fishbowl.apps.olo.Models.OloMenu;
import com.fishbowl.apps.olo.Models.OloModifier;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public class OloMenuService
{
    public static void getRestaurantMenu(int restaurantId, final OloMenuServiceCallback callback)
    {
        String path = "/restaurants/" + restaurantId + "/menu";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("restaurant_id", restaurantId);

        OloService.getInstance().get(path, parameters, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                OloMenu menu = null;
                if (response != null)
                {
                    Gson gson = new Gson();
                    try
                    {
                        String imagePath = response.getString("imagepath");
                        JSONArray jsonCategories = response.getJSONArray("categories");
                        OloCategory[] categories = gson.fromJson(jsonCategories.toString(), OloCategory[].class);
                        menu = new OloMenu(imagePath, categories);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null)
                {
                    callback.onRestaurantMenuCallback(menu, error);
                }
            }
        });

    }

    public static void getProductModifiers(int productId, final OloProductModifierCallback callback)
    {
        getProductOptionGroups("modifiers", productId, callback);
    }

    public static void getProductOptions(int productId, final OloProductModifierCallback callback)
    {
        getProductOptionGroups("options", productId, callback);
    }

    private static void getProductOptionGroups(String path, int productId, final OloProductModifierCallback callback)
    {
        OloService.getInstance().get("products/" + productId + "/" + path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseResponse(response, error, callback);
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, OloProductModifierCallback callback)
    {
        OloModifier[] optionGroups = null;
        if (response != null)
        {
            Gson gson = new Gson();
            try
            {
                JSONArray jsonArray = response.getJSONArray("optiongroups");
                optionGroups = gson.fromJson(jsonArray.toString(), OloModifier[].class);
            } catch (JSONException e)
            {
                e.printStackTrace();
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onProductModifierCallback(optionGroups, error);
        }
    }
}
