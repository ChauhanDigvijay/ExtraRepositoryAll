package com.wearehathway.apps.incomm.Services;

import com.google.gson.Gson;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommVestaWebSessionDataCallback;
import com.wearehathway.apps.incomm.Models.InCommVestaWebSessionData;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

/**
 * Created by Nauman Afzaal on 17/09/15.
 */
public class InCommVestaWebSessionService
{
    public static void getVestaWebSessionData(final InCommVestaWebSessionDataCallback callback)
    {
        String path = "VestaWebSessionId";
        InCommService.getInstance().get(path, null, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommVestaWebSessionData inCommVestaWebSessionData = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    inCommVestaWebSessionData = gson.fromJson(response.toString(), InCommVestaWebSessionData.class);
                    if (inCommVestaWebSessionData == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onVestaWebSessionDataCallback(inCommVestaWebSessionData, error);
                }
            }
        });
    }
}
