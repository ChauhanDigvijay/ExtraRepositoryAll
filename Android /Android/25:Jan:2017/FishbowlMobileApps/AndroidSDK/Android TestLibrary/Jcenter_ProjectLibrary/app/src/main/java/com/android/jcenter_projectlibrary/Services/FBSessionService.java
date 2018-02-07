package com.android.jcenter_projectlibrary.Services;

/**
 * Created by digvijaychauhan on 30/08/17.
 */


import com.android.jcenter_projectlibrary.Controllers.FBSdk;
import com.android.jcenter_projectlibrary.Interfaces.FBInvalidAuthTokenCallback;
import com.android.jcenter_projectlibrary.Interfaces.FBJsonServiceCallback;
import com.android.jcenter_projectlibrary.Interfaces.FBSessionServiceCallback;
import com.android.jcenter_projectlibrary.Interfaces.FBUserServiceCallback;
import com.android.jcenter_projectlibrary.Models.FBMember;
import com.android.jcenter_projectlibrary.Models.FBSessionItem;
import com.android.jcenter_projectlibrary.Utils.FBConstant;
import com.android.jcenter_projectlibrary.Utils.FBPreferences;
import com.android.jcenter_projectlibrary.Utils.FBUtility;
import com.android.jcenter_projectlibrary.Utils.StringUtilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class FBSessionService {
    public final static FBSessionItem currentSession = new FBSessionItem();
    public static FBInvalidAuthTokenCallback invalidAuthTokenCallback;
    public static FBSdk fbSdk;
    private static FBSessionService instance;

    public static void notifyInvalidAuthTokenCallback() {
        clearCurrentSession();
        if (invalidAuthTokenCallback != null) {
            invalidAuthTokenCallback.onInvalidAuthTokenCallback();
        }
    }

    public static FBSessionService sharedInstance() {
        if (instance == null) {
            instance = new FBSessionService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk) {
        fbSdk = _fbsdk;
    }

    //    public static void setSessionParams(String spendGoId, String authToken)
//    {
//        currentSession.setSpendgo_id(spendGoId);
//        currentSession.setAuthToken(authToken);
//    }
    public static void clearCurrentSession() {
        currentSession.clearSession();
    }

    public static void updateUser(FBMember user, final FBSessionServiceCallback callback) {
        String path = "v1/fbmember/updateMemberNotify";
        HashMap<String, Object> parameters = new HashMap<>();
        if (StringUtilities.isValidString(user.getFirstName())) {
            parameters.put("firstName", user.getFirstName());
        }
        if (StringUtilities.isValidString(user.getLastName())) {
            parameters.put("lastName", user.getLastName());
        }
        if (StringUtilities.isValidString(user.getEmailAddress())) {
            parameters.put("emailAddress", user.getEmailAddress());

        }
        if (StringUtilities.isValidString(user.getAddressStreet())) {
            parameters.put("addressStreet", user.getAddressStreet());
        }
        if (StringUtilities.isValidString(user.getAddressState())) {
            parameters.put("addressState", user.getAddressState());
        }
        if (StringUtilities.isValidString(user.getAddressCity())) {
            parameters.put("addressCity", user.getAddressCity());
        }

        if (StringUtilities.isValidString(user.getPhoneNumber())) {
            parameters.put("phoneNumber", user.getPhoneNumber());
        }
        if (StringUtilities.isValidString(user.getAddressZipCode())) {
            parameters.put("addressZipCode", user.getAddressZipCode());
        }

        if (StringUtilities.isValidString(user.getDate())) {
            parameters.put("date", user.getDate());// (optional ­ “M” | “F”)
        }
        if (StringUtilities.isValidString(user.getMonth())) {
            parameters.put("month", user.getMonth());// (optional ­ “M” | “F”)
        }
        if (StringUtilities.isValidString(user.getYear())) {
            parameters.put("year", user.getYear());//(optional ­ “Single” | “Married” | “Divorced” | “Domestic Partner”)
        }

        if (StringUtilities.isValidString(user.getGender())) {
            parameters.put("gender", user.getGender());// (optional ­ “M” | “F”)
        }

        if (StringUtilities.isValidString(user.getFavoriteStore())) {
            parameters.put("favoriteStore", user.getFavoriteStore());
        }

        if (StringUtilities.isValidString(user.getEmailOptIn())) {
            parameters.put("emailOptIn", user.getEmailOptIn());
        }
        if (StringUtilities.isValidString(user.getsMSOptIn())) {
            parameters.put("sMSOptIn", user.getsMSOptIn());
        }
        if (StringUtilities.isValidString(user.getsMSOptIn())) {
            parameters.put("pushOptIn", user.getsMSOptIn());
        }

        if (StringUtilities.isValidString(user.getProfileImageUrl())) {
            parameters.put("profileImageUrl", user.getProfileImageUrl());
        }

        if (StringUtilities.isValidString(user.getRequestFromJoinPage())) {
            parameters.put("requestFromJoinPage", user.getRequestFromJoinPage());
        }
        if (StringUtilities.isValidString(user.getCreated())) {
            parameters.put("created", user.getCreated());
        }


        if (StringUtilities.isValidString(user.getdOB())) {
            parameters.put("dOB", user.getdOB());// YYYYMMDD
        }


        if (StringUtilities.isValidString(user.getSendWelcomeEmail())) {
            parameters.put("sendWelcomeEmail", user.getSendWelcomeEmail());
        }

        if (StringUtilities.isValidString(user.getDeviceId())) {
            parameters.put("deviceId", user.getDeviceId());
        }
        if (StringUtilities.isValidString(user.getStoreCode())) {
            parameters.put("storeCode", user.getStoreCode());
        }
        if (StringUtilities.isValidString(user.getCountry())) {
            parameters.put("country", user.getCountry());
        }

        if (StringUtilities.isValidString(user.getPassword())) {
            parameters.put("password", user.getPassword());
        }


        FBService.sharedInstance().post(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void addUser(FBMember user, final FBSessionServiceCallback callback) {
        String path = "v1/fbmember/create?themeId=150";
        HashMap<String, Object> parameters = new HashMap<>();
        if (StringUtilities.isValidString(user.getFirstName())) {
            parameters.put("firstName", user.getFirstName());
        }
        if (StringUtilities.isValidString(user.getLastName())) {
            parameters.put("lastName", user.getLastName());
        }
        if (StringUtilities.isValidString(user.getEmailAddress())) {
            parameters.put("emailAddress", user.getEmailAddress());

        }
        if (StringUtilities.isValidString(user.getAddressStreet())) {
            parameters.put("addressStreet", user.getAddressStreet());
        }
        if (StringUtilities.isValidString(user.getAddressState())) {
            parameters.put("addressState", user.getAddressState());
        }
        if (StringUtilities.isValidString(user.getAddressCity())) {
            parameters.put("addressCity", user.getAddressCity());
        }

        if (StringUtilities.isValidString(user.getPhoneNumber())) {
            parameters.put("phoneNumber", user.getPhoneNumber());
        }
        if (StringUtilities.isValidString(user.getAddressZipCode())) {
            parameters.put("addressZipCode", user.getAddressZipCode());
        }

        if (StringUtilities.isValidString(user.getDate())) {
            parameters.put("date", user.getDate());// (optional ­ “M” | “F”)
        }
        if (StringUtilities.isValidString(user.getMonth())) {
            parameters.put("month", user.getMonth());// (optional ­ “M” | “F”)
        }
        if (StringUtilities.isValidString(user.getYear())) {
            parameters.put("year", user.getYear());//(optional ­ “Single” | “Married” | “Divorced” | “Domestic Partner”)
        }

        if (StringUtilities.isValidString(user.getGender())) {
            parameters.put("gender", user.getGender());// (optional ­ “M” | “F”)
        }

        if (StringUtilities.isValidString(user.getFavoriteStore())) {
            parameters.put("favoriteStore", user.getFavoriteStore());
        }

        if (StringUtilities.isValidString(user.getEmailOptIn())) {
            parameters.put("emailOptIn", user.getEmailOptIn());
        }
        if (StringUtilities.isValidString(user.getsMSOptIn())) {
            parameters.put("sMSOptIn", user.getsMSOptIn());
        }
        if (StringUtilities.isValidString(user.getsMSOptIn())) {
            parameters.put("pushOptIn", user.getsMSOptIn());
        }

        if (StringUtilities.isValidString(user.getProfileImageUrl())) {
            parameters.put("profileImageUrl", user.getProfileImageUrl());
        }

        if (StringUtilities.isValidString(user.getRequestFromJoinPage())) {
            parameters.put("requestFromJoinPage", user.getRequestFromJoinPage());
        }
        if (StringUtilities.isValidString(user.getCreated())) {
            parameters.put("created", user.getCreated());
        }


        if (StringUtilities.isValidString(user.getdOB())) {
            parameters.put("dOB", user.getdOB());// YYYYMMDD
        }


        if (StringUtilities.isValidString(user.getSendWelcomeEmail())) {
            parameters.put("sendWelcomeEmail", user.getSendWelcomeEmail());
        }

        if (StringUtilities.isValidString(user.getDeviceId())) {
            parameters.put("deviceId", user.getDeviceId());
        }
        if (StringUtilities.isValidString(user.getStoreCode())) {
            parameters.put("storeCode", user.getStoreCode());
        }
        if (StringUtilities.isValidString(user.getCountry())) {
            parameters.put("country", user.getCountry());
        }

        if (StringUtilities.isValidString(user.getPassword())) {
            parameters.put("password", user.getPassword());
        }


        FBService.getInstance().post(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getRegisterToken(final FBMember user, final FBSessionServiceCallback callback) {

        String path = "mobile/getToken";
        HashMap<String, Object> parameters = new HashMap<>();
        try {
            parameters.put("clientId", FBConstant.client_id);
            parameters.put("clientSecret", FBConstant.client_secret);
            parameters.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
            parameters.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBService.getInstance().put(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                String secratekey = null;
                try {
                    secratekey = response.getString("message");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                FBPreferences.sharedInstance(fbSdk.context).setAccessToken(secratekey);
                addUser(user, callback);

            }
        });
    }

    public static void signIn(String phoneorusername, String password, final FBSessionServiceCallback callback) {
        String path = "member/login";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", phoneorusername);
        parameters.put("password", password);

        FBService.sharedInstance().post(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getmember(final FBUserServiceCallback callback) {
        String path = "v1/fbmember/getMember";
        HashMap<String, Object> parameters = new HashMap<>();

        FBService.sharedInstance().get(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parsegetMemberResponse(response, error, callback);
            }
        });
    }

    private static void parseResponse(JSONObject response, Exception error, FBSessionServiceCallback callback) {
        FBSessionItem user = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                user = gson.fromJson(response.toString(), FBSessionItem.class);
            } catch (Exception ex) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onSessionServiceCallback(user, error);
        }
    }


//    public static void signOff(String authToken, final ISpendGoSignOffService callback)
//    {
//        String path = "signoff";
//        HashMap<String, Object> parameters = new HashMap<>();
//        parameters.put("auth_token", authToken);
//        FBService.getInstance().post(path, parameters, new FBJsonServiceCallback()
//        {
//            @Override
//            public void onServiceCallback(JSONObject response, Exception error)
//            {
//                callback.onSignOffCallback(error);
//            }
//        });
//    }

//    private static void parseResponse(JSONObject response, Exception error, FBSessionServiceCallback callback)
//    {
//        if (response == null && error == null)
//        {
//            error = new VolleyError(new NetworkResponse(FBSessionConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED.value, null, null, false));
//        }
//        else if (response != null)
//        {
//            try
//            {
//                String spendgo_id = response.getString("spendgo_id");
//                String auth_token = response.getString("auth_token");
//                currentSession.setSpendgo_id(spendgo_id);
//                currentSession.setAuthToken(auth_token);
//            } catch (Exception ex)
//            {
//                error = new Exception("Error occurred while parsing data.");
//            }
//        }
//        if (callback != null)
//        {
//            callback.onSessionServiceCallback(currentSession, error);
//        }
//    }

    private static void parsegetMemberResponse(JSONObject response, Exception error, FBUserServiceCallback callback) {
        FBMember user = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                user = gson.fromJson(response.toString(), FBMember.class);
            } catch (Exception ex) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onUserServiceCallback(user, error);
        }
    }



}
