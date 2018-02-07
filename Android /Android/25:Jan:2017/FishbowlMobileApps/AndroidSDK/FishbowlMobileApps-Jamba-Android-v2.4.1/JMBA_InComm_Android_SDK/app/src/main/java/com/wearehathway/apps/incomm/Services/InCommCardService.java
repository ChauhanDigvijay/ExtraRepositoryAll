package com.wearehathway.apps.incomm.Services;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.incomm.Interfaces.InCommCardAutoReloadServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommCardDeleteServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommCardUpdateServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommGetAllCardServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */

public class InCommCardService
{

    public final static String VolleyFailure_UnAuthorizedMessage = "No authentication challenges found";

    public static void getCardById(int cardId, boolean getLatestBalance, InCommCardServiceCallback callback)
    {
        String path = "Cards/" + cardId + "?getLatestBalance=" + getLatestBalance;
        cardService(path, callback);
    }

    public static void getCardByNumber(String cardNumber, String cardPin, boolean getLatestBalance, InCommCardServiceCallback callback)
    {
        if(InCommService.getInstance() != null) {
            String path = "Cards/" + InCommService.getInstance().getConfiguration().brandId + "/" + cardNumber + "?pin=" + cardPin + "&getLatestBalance=" + getLatestBalance;
            cardService(path, callback);
        }else{
            callback.onCardServiceCallback(null,new Exception(VolleyFailure_UnAuthorizedMessage));
        }
    }

    private static void cardService(String path, final InCommCardServiceCallback callback)
    {
        InCommService.getInstance().get(path, null, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommCard card = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    card = gson.fromJson(response.toString(), InCommCard.class);
                    if (card == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onCardServiceCallback(card, error);
                }
            }
        });
    }

    public static void deleteCard(String userId,long cardId, InCommCardDeleteServiceCallback callback){
        String path= "Users/"+userId+"/Cards/"+String.valueOf(cardId);
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

    public static void getAllCard(String userId,InCommGetAllCardServiceCallBack callback){
        String path= "Users/"+userId+"/Cards";
        getAllCardService(path, callback);
    }

    private static void getAllCardService(String path, final InCommGetAllCardServiceCallBack callback){
        HashMap<String, Object> parameters = null;

        if(InCommService.getInstance() != null) {
            InCommService.getInstance().get(path, parameters, new InCommServiceCallback() {
                @Override
                public void onServiceCallback(String response, Exception error) {
                    List<InCommCard> allCards = null;
                    if (response != null) {
                        Gson gson = InCommUtils.getGsonForParsingDate();
                        allCards = gson.fromJson(response, new TypeToken<ArrayList<InCommCard>>() {
                        }.getType());
                        if (allCards == null) {
                            error = InCommUtils.getParsingError();
                        } else {
                            Collections.reverse(allCards);
                        }
                    }
                    if (callback != null) {
                        callback.onGetAllCardServiceCallback(allCards, error);
                    }
                }
            });
        }else{
            callback.onGetAllCardServiceCallback(null,new Exception(VolleyFailure_UnAuthorizedMessage));
        }
    }

    public static void addCardToList(String userId,String token, final InCommCardServiceCallback callback)
    {
        String path= "Users/"+userId+"/Cards";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("CardToken", token);


        InCommService.getInstance().post(path, parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommCard card = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    card = gson.fromJson(response.toString(), InCommCard.class);
                    if (card == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onCardServiceCallback(card, error);
                }
            }
        });
    }

    public static void addCardToList(String userId,String cardNumber,String cardPin, final InCommCardServiceCallback callback)
    {
        String path= "Users/"+userId+"/Cards";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("CardNumber", cardNumber);
        parameters.put("CardPin", cardPin);
        parameters.put("BrandId", InCommService.getInstance().getConfiguration().brandId);


        InCommService.getInstance().post(path, parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommCard card = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    card = gson.fromJson(response.toString(), InCommCard.class);
                    if (card == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onCardServiceCallback(card, error);
                }
            }
        });
    }

    public static void updateCard(String userId, int cardId,String cardName, InCommCardUpdateServiceCallback callback){
        String path= "Users/"+userId+"/Cards/"+String.valueOf(cardId);
        updateCardService(path, cardName,callback);
    }
    public static void getCardInfo(String userId, int cardId, InCommCardServiceCallback callback){
        String path= "Users/"+userId+"/Cards/"+String.valueOf(cardId)+"/GetBalance";
        cardService(path,callback);
    }

    private static void updateCardService(String path,String cardName, final InCommCardUpdateServiceCallback callback){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("CardName",cardName);

        InCommService.getInstance().put(path, parameters, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommCard card = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    card = gson.fromJson(response.toString(), InCommCard.class);
                    if (card == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onCardUpdateServiceCallback(card,error);
                }
            }
        });
    }


    //AutoReload services
    //Submit Auto Reload
    public static void autoReload(String userId, String cardId, InCommAutoReloadSubmitOrder inCommAutoReloadSubmitOrder, final InCommCardAutoReloadServiceCallBack callBack){
        String path = "Users/" + userId +"/Cards/"+cardId+"/AutoReloads";
        JSONObject jsonObject = InCommUtils.convertToJSON(inCommAutoReloadSubmitOrder);
        InCommUtils.removeEmptyValues(jsonObject);
        InCommService.getInstance().post(path, jsonObject.toString(), new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommAutoReloadSavable inCommAutoReloadResponse = null;
                if (response != null) {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    inCommAutoReloadResponse = gson.fromJson(response.toString(), InCommAutoReloadSavable.class);
                    if (inCommAutoReloadResponse == null) {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callBack != null) {
                    callBack.onCardAutoReloadServiceCallback(inCommAutoReloadResponse, error);
                }
            }
        });
    }


    //Updating AutoReload State
    public static void updateAutoReloadState(String userId,int cardId,int autoReloadId,Boolean active,final InCommCardAutoReloadServiceCallBack callBack){
        String path = "Users/" + userId +"/Cards/"+cardId+"/AutoReloads/"+autoReloadId+"/UpdateState?active="+active;

        HashMap<String, Object> parameters = null;


        InCommService.getInstance().put(path, parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommAutoReloadSavable inCommAutoReloadResponse = null;
                if(error == null) {
                    if (response.equalsIgnoreCase("")) {
                        inCommAutoReloadResponse = null;
                    }
                }else{
                    inCommAutoReloadResponse = null;
                }
                if (callBack != null)
                {
                    callBack.onCardAutoReloadServiceCallback(inCommAutoReloadResponse, error);
                }
            }
        });
    }


    //Delete AutoReload
    public static void deleteAutoReload(String userId,int cardId,int autoReloadId,final InCommCardAutoReloadServiceCallBack callBack){
        String path = "Users/" + userId +"/Cards/"+cardId+"/AutoReloads/"+autoReloadId;

        HashMap<String, Object> parameters = null;

        InCommService.getInstance().delete(path, parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommAutoReloadSavable inCommAutoReloadResponse = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    inCommAutoReloadResponse = gson.fromJson(response.toString(), InCommAutoReloadSavable.class);
                }
                if (callBack != null)
                {
                    callBack.onCardAutoReloadServiceCallback(inCommAutoReloadResponse, error);
                }
            }
        });
    }

    //Get AutoReload Info
    public static void getAutoReloadInfo(String userId,int cardId,int autoReloadId,final InCommCardAutoReloadServiceCallBack callBack){
        String path = "Users/" + userId +"/Cards/"+cardId+"/AutoReloads/"+autoReloadId;
        HashMap<String, Object> parameters = null;

        InCommService.getInstance().get(path, parameters, new InCommServiceCallback()
        {
            @Override
            public void onServiceCallback(String response, Exception error)
            {
                InCommAutoReloadSavable inCommAutoReloadResponse = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    inCommAutoReloadResponse = gson.fromJson(response.toString(), InCommAutoReloadSavable.class);
                    if (inCommAutoReloadResponse == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callBack != null)
                {
                    callBack.onCardAutoReloadServiceCallback(inCommAutoReloadResponse, error);
                }
            }
        });
    }
}
