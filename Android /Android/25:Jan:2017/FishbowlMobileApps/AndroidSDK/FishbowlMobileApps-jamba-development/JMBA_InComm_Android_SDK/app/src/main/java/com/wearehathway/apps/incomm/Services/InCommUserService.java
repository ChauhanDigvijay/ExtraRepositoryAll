package com.wearehathway.apps.incomm.Services;

import com.google.gson.Gson;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommUserProfileServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Models.InCommUserProfile;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by vthink on 18/08/16.
 */
public class InCommUserService {

    public static void getAccessTokenWithUserId(InCommUserServiceCallBack callback){
        String path= "accesstokens/";
        userIdService(path, callback);
    }

    private static void userIdService(String path, final InCommUserServiceCallBack callback){
        HashMap<String, Object> parameters = new HashMap<>();

        InCommService.getInstance().post(path, parameters, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommUser inCommUser = null;
                if (response != null)
                {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    try {
                        inCommUser = gson.fromJson(response, InCommUser.class);
                    }catch (Exception e){
                       e.printStackTrace();
                    }
                    if (inCommUser == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null)
                {
                    callback.onUserServiceCallback(inCommUser, error);
                }
            }
        });
    }

    public static void updateUserProfileWithUserId(HashMap<String, Object> parameters, InCommUserProfileServiceCallBack callBack){
        String path = "Users/" + parameters.get("Id");
        updateUserProfileService(path,parameters,callBack);
    }

    private static void updateUserProfileService(String path, HashMap<String, Object> parameters, final InCommUserProfileServiceCallBack callBack){
        InCommService.getInstance().put(path, parameters, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommUserProfile inCommUserProfile = null;
                if(response != null){
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    try {
                        inCommUserProfile = gson.fromJson(response, InCommUserProfile.class);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(inCommUserProfile == null)
                    {
                        error = InCommUtils.getParsingError();
                    }
                }
                if(callBack != null)
                {
                    callBack.onUserProfileServiceCallback(inCommUserProfile, error);
                }
            }
        });
    }
}
