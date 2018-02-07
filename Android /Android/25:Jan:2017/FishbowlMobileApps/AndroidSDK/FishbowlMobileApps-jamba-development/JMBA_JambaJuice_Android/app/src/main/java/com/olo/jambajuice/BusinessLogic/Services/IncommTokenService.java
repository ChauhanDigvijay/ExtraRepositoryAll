package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;
import android.content.ContextWrapper;
import android.util.Base64;

import com.fishbowl.basicmodule.Interfaces.FBIncommResponseCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPassServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by vthink on 24/10/16.
 */

public class IncommTokenService {

    public static void getIncommTokenServices(final IncommTokenServiceCallback callback) {

        JambaApplication _app = JambaApplication.getAppContext();
        JSONObject data = new JSONObject();
        try {
            data.put("key", "jambamobile");
            data.put("authorization", UserService.getUser().getSpendGoAuthToken());
            data.put("spendgoId", UserService.getUser().getSpendGoId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isNetworkAvailable(_app)) {
            _app.fbsdkObj.getInCommToken(data, new FBIncommResponseCallback() {
                        @Override
                        public void OnFBIncommResponseCallback(JSONObject jsonObject, Exception e) {
                            String inCommToken = null;
                            Boolean successFlag = false;
                            try {
                                if (jsonObject != null) {
                                    successFlag = jsonObject.getBoolean("successFlag");
                                    inCommToken = jsonObject.getString("inCommToken");
                                }
                            } catch (JSONException e1) {
                                e.printStackTrace();
                            }

                            if (inCommToken != null) {
                                callback.onIncommTokenServiceCallback(inCommToken, successFlag, null);
                            } else {
                                callback.onIncommTokenServiceCallback(inCommToken, successFlag, "error");
                            }
                        }
                    }
            );

            //String incommToken = authTokenGenration();
//            if(inCommToken != null) {
//                callback.onIncommTokenServiceCallback(inCommToken, true, null);
//            }else{
//                callback.onIncommTokenServiceCallback(inCommToken, false, "error");
//            }


        } else {
            callback.onIncommTokenServiceCallback(null, false, Constants.NO_INTERNET_CONNECTION);
        }

    }

    private static String authTokenGenration() {
        String time = (System.currentTimeMillis() / 1000) + "";
        int expires = Integer.parseInt(time) + (10 * 24 * 60 * 60);
        String secretkey = JambaApplication.getAppContext().getString(R.string.incomm_secret_key);
        String customerid = UserService.getUser().getSpendGoId();

        String message = "id=" + customerid + "&expires=" + expires;
        String authToken = null;
        try {

            String hashhamc = calculateHash(secretkey, message);

            String base64Hash = message + "&Signature=" + hashhamc;

            byte[] data = base64Hash.getBytes("UTF-8");
            authToken = Base64.encodeToString(data, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }

    private static String calculateHash(String secretKey, String finalMessage) {
        try {
            String type = "HmacSHA256";
            byte[] keyBase64 = Base64.decode(secretKey, Base64.NO_WRAP);
            SecretKeySpec secret = new SecretKeySpec(keyBase64, type);
            Mac mac = Mac.getInstance(type);
            mac.init(secret);
            byte[] bytes = mac.doFinal(finalMessage.getBytes());
            String hash = Base64.encodeToString(bytes, Base64.NO_WRAP);
            return hash;
        } catch (Exception ex) {
        }
        return null;
    }
}
