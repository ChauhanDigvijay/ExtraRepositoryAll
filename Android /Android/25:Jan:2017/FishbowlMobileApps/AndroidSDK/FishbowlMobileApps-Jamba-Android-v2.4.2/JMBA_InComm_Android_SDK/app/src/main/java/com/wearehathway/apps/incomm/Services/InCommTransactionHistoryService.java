package com.wearehathway.apps.incomm.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommTransactionHistoryCallback;
import com.wearehathway.apps.incomm.Models.InCommCardTransactionHistory;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

/**
 * Created by vt010 on 9/16/16.
 */
public class InCommTransactionHistoryService {
    public static void getAllTransactionHistories(String userId, String cardId,InCommTransactionHistoryCallback callback)
    {
        String path = "Users/" + userId +"/Cards/"+cardId+"/GetTransactionHistory";
        InCommTransactionHistoryService(path, callback);
    }

    private static void InCommTransactionHistoryService(String path, final InCommTransactionHistoryCallback callback)
    {
        InCommService.getInstance().get(path, null, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommCardTransactionHistory transactionHistoriesList = null;
                if (response != null) {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    transactionHistoriesList = gson.fromJson(response.toString(),  new TypeToken<InCommCardTransactionHistory>()
                    {
                    }.getType());
                    if (transactionHistoriesList == null) {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null) {
                    callback.onTransactionHistoryServiceCallback(transactionHistoriesList, error);
                }
            }
        });
    }
}
