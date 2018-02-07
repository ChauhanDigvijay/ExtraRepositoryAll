package com.wearehathway.apps.incomm.Services;

import com.google.gson.Gson;
import com.wearehathway.apps.incomm.Interfaces.InCommOrderServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommReloadOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */
public class InCommOrderService
{
    public static void submitOrder(InCommSubmitOrder order, final InCommOrderServiceCallback callback)
    {
        JSONObject jsonObject = InCommUtils.convertToJSON(order);
        InCommUtils.removeEmptyValues(jsonObject);
        InCommService.getInstance().post("Orders", jsonObject.toString(), new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommOrder orderResponse = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    orderResponse = gson.fromJson(response, InCommOrder.class);
                    if (orderResponse == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onOrderServiceCallback(orderResponse, error);
                }
            }
        });
    }

    public static void submitOrderReload(InCommReloadOrder reloadOrder, final InCommOrderServiceCallback callback)
    {
        JSONObject purchaserJson = InCommUtils.convertToJSON(reloadOrder.getPurchaser());
        JSONObject inCommSubmitPaymentJson = InCommUtils.convertToJSON(reloadOrder.getPayment());

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Amount", reloadOrder.getAmount());
        parameters.put("BrandId", InCommService.getInstance().getConfiguration().brandId);
        parameters.put("CardId", reloadOrder.getCardId());
        parameters.put("CardPin", reloadOrder.getCardPin());
        parameters.put("IsTestMode", reloadOrder.isTestMode());
        parameters.put("Purchaser", purchaserJson);
        parameters.put("Payment",inCommSubmitPaymentJson);

        InCommService.getInstance().post("Orders/SubmitReload", parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommOrder order = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    order = gson.fromJson(response.toString(), InCommOrder.class);
                    if (order == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onOrderServiceCallback(order, error);
                }
            }
        });
    }

}
