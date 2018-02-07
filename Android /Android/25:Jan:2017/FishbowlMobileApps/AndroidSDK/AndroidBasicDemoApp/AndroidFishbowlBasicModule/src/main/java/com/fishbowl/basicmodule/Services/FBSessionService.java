package com.fishbowl.basicmodule.Services;

/**
 * Created by digvijaychauhan on 30/08/17.
 */


import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBInvalidAuthTokenCallback;
import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBParseMember;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijaychauhan on 23/11/16.
 */
public class FBSessionService {
    public final static FBSessionItem currentSession = new FBSessionItem();
    public static FBInvalidAuthTokenCallback invalidAuthTokenCallback;
    public static FBSdk fbSdk;
    private static FBSessionService instance;
    public static FBParseMember member;

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

    public static void clearCurrentSession() {
        currentSession.clearSession();
    }


    public static void getTokenApi( final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
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

            FBMainService.getInstance().put(path, parameters,true, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(fbSdk.context).setAccessTokenforapp(secratekey);
                    parseResponse(response, error, callback);


                }
            });
        }
    }

    public static void getTokenApi(final FBMember user, final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
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

            FBMainService.getInstance().put(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(fbSdk.context).setAccessTokenforapp(secratekey);
                    createMember(user, callback);

                }
            });
        }
    }



    public static void loginMember(String phoneorusername, String password, final FBSessionServiceCallback callback) {
        String path = "member/login";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", phoneorusername);
        parameters.put("password", password);


        FBMainService.sharedInstance().post(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void createMember(FBMember user, final FBSessionServiceCallback callback) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = "v1/fbmember/create";
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


                parameters.put("emailOptIn", user.getEmailOptIn());


                parameters.put("sMSOptIn", user.getsMSOptIn());


                parameters.put("pushOptIn", user.getsMSOptIn());


            if (StringUtilities.isValidString(user.getProfileImageUrl())) {
                parameters.put("profileImageUrl", user.getProfileImageUrl());
            }


                parameters.put("requestFromJoinPage", user.getRequestFromJoinPage());

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


            FBMainService.getInstance().post(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    parseResponse(response, error, callback);
                }
            });
        }
    }

    public static void memberUpdate(FBMember user, final FBSessionServiceCallback callback) {
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


            parameters.put("emailOptIn", user.getEmailOptIn());


            parameters.put("sMSOptIn", user.getsMSOptIn());


            parameters.put("pushOptIn", user.getsMSOptIn());


        if (StringUtilities.isValidString(user.getProfileImageUrl())) {
            parameters.put("profileImageUrl", user.getProfileImageUrl());
        }


            parameters.put("requestFromJoinPage", user.getRequestFromJoinPage());

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


        FBMainService.sharedInstance().post(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }



    public static void deviceUpdate( final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = FBConstant.DeviceUpdateApi;
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                parameters.put("memberid",FBPreferences.sharedInstance(fbSdk.context).getUserMemberforAppId());
                parameters.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
                parameters.put("deviceOsVersion", FBConstant.device_os_ver);
                parameters.put("deviceType", FBConstant.DEVICE_TYPE);
                parameters.put("pushToken", FBPreferences.sharedInstance(FBSdk.context).getPushToken());
                parameters.put("pushToken", "com.fishbowl.BasicApp");

            } catch (Exception e) {
                e.printStackTrace();
            }

            FBMainService.getInstance().post(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    parseResponse(response, error, callback);

                }
            });
        }
        
    }


    public static void forgetPassword( String newemail,final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = FBConstant.FBforgetPasswordApi;
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                parameters.put("email", newemail);

            } catch (Exception e) {
                e.printStackTrace();
            }

            FBMainService.getInstance().post(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {

                    parseResponse(response, error, callback);


                }
            });
        }
    }


    public static void changePassword( String oldpassword,String newpassword,final FBSessionServiceCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = FBConstant.FBChangePasswordApi;
            HashMap<String, Object> parameters = new HashMap<>();
            try {

                parameters.put("oldPassword", oldpassword);
                parameters.put("password", newpassword);

            } catch (Exception e) {
                e.printStackTrace();
            }

            FBMainService.getInstance().put(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {

                    parseResponse(response, error, callback);


                }
            });
        }
    }



    public static void logout(String application ,final FBSessionServiceCallback callback) {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onSessionServiceCallback(null, FBUtility.getNetworkError());
        } else {
            String path = FBConstant.FBLogoutApi;
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("application", application);


            FBMainService.sharedInstance().post(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    parseResponse(response, error, callback);
                }
            });
        }
    }


    public static void getMember(final FBUserServiceCallback callback) {
        String path = "v1/fbmember/getMember";
        HashMap<String, Object> parameters = new HashMap<>();

        FBMainService.sharedInstance().get(path, parameters, new FBJsonServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                member = new FBParseMember();
                member.initWithJson(response, fbSdk.context);
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




    public void init(FBSdk _fbsdk) {
        fbSdk = _fbsdk;
    }


}
