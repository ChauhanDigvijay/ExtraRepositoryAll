package com.fishbowl.apps.olo.Services;

import com.google.gson.Gson;
import com.fishbowl.apps.olo.Interfaces.OloOrderServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloServiceCallback;
import com.fishbowl.apps.olo.Models.OloOrderStatus;

import org.json.JSONObject;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public class OloOrderService
{
    public static void getOrderById(String orderId, final OloOrderServiceCallback callback)
    {
        String path = "orders/" + orderId;
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseOrderResponse(response, error, callback);
            }
        });
    }

    public static void getOrderByRef(String orderRef, final OloOrderServiceCallback callback)
    {
        String path = "orders/byref" + orderRef;
        OloService.getInstance().get(path, null, new OloServiceCallback()
        {
            @Override
            public void onServiceCallback(JSONObject response, Exception error)
            {
                parseOrderResponse(response, error, callback);
            }
        });
    }

    private static void parseOrderResponse(JSONObject response, Exception error, OloOrderServiceCallback callback)
    {
        OloOrderStatus orderStatus = null;
        if (response != null)
        {
            Gson gson = new Gson();
            orderStatus = gson.fromJson(response.toString(), OloOrderStatus.class);
            if (orderStatus == null)
            {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null)
        {
            callback.onOrderServiceCallback(orderStatus, error);
        }
    }
}
