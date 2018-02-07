package com.wearehathway.apps.incomm.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.incomm.Interfaces.InCommCardDeleteServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommSaveUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */

public class InCommUserPaymentAccountService
{
    public static void getAllUserPaymentAccount(String userId, InCommUserPaymentAccountCallback callback)
    {
        String path = "Users/" + userId +"/PaymentAccounts";
        InCommUserPaymentListAccountService(path, callback);
    }
    public static void saveUserPaymentAccount(String userId, InCommUserPaymentAccount newPaymentAccount, final InCommSaveUserPaymentAccountCallback callback)
    {
        String path = "Users/" + userId +"/PaymentAccounts";
        JSONObject jsonObject = InCommUtils.convertToJSON(newPaymentAccount);
        InCommUtils.removeEmptyValues(jsonObject);
        InCommService.getInstance().post(path, jsonObject.toString(), new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommUserPaymentAccount paymentAccountResponse = null;
                if (response != null) {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    paymentAccountResponse = gson.fromJson(response.toString(), InCommUserPaymentAccount.class);
                    if (paymentAccountResponse == null) {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null) {
                    callback.onPaymentAccountSaveServiceCallback(paymentAccountResponse, error);
                }
            }
        });
    }

    private static void InCommUserPaymentListAccountService(String path, final InCommUserPaymentAccountCallback callback)
    {
        InCommService.getInstance().get(path, null, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                ArrayList<InCommUserPaymentAccount> accountList = null;
                if (response != null) {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    accountList = gson.fromJson(response.toString(),  new TypeToken<ArrayList<InCommUserPaymentAccount>>()
                    {
                    }.getType());
                    if (accountList == null) {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null) {
                    callback.onPaymentAccountListServiceCallback(accountList, error);
                }
            }
        });
    }
    public static void deleteCard(String userId,String creditCardId, InCommCardDeleteServiceCallback callback){
        String path= "Users/"+userId+"/PaymentAccounts/"+creditCardId;
        deleteCardService(path, callback);
    }
    private static void deleteCardService(String path, final InCommCardDeleteServiceCallback callback){
        HashMap<String, Object> parameters = new HashMap<>();

        InCommService.getInstance().delete(path, parameters, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                if (callback != null) {
                    callback.onCardDeleteServiceCallback(error);
                }
            }
        });
    }
}
