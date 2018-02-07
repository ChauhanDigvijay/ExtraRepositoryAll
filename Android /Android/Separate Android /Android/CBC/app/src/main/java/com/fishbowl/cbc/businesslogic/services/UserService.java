package com.fishbowl.cbc.businesslogic.services;

import android.content.Context;

import com.fishbowl.apps.olo.Interfaces.OloInvalidAuthCallback;
import com.fishbowl.apps.olo.Services.OloSessionService;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.cbc.CbcApplication;
import com.fishbowl.cbc.businesslogic.analytics.AnalyticsManager;
import com.fishbowl.cbc.businesslogic.analytics.CbcAnalyticsManager;
import com.fishbowl.cbc.businesslogic.interfaces.UserServiceCallback;
import com.fishbowl.cbc.businesslogic.managers.SessionMaintainenceManager;
import com.fishbowl.cbc.businesslogic.models.User;
import com.fishbowl.cbc.utils.Constants;
import com.fishbowl.cbc.utils.Utils;

import static com.fishbowl.cbc.utils.Constants.BROADCAST_AUTH_TOKEN_FAILURE;

/**
 * Created by VT027 on 5/20/2017.
 */

public class UserService {

    public static CbcApplication _app;
    //public static List<RecentOrder> recentOrder;
    //public static List<FavoriteOrder> favOrder;
    private static User user = new User();


    public static User getUser() {
        if (user == null) {
            // Incase user object was destroyed and user was actually authenticated.
            SessionMaintainenceManager.loadUserSession(new UserServiceCallback() {
                @Override
                public void onUserServiceCallback(User savedUser, Exception exception) {
                    if (savedUser != null) {
                        parseUser(savedUser);
                    } else {
                        user = new User();
                    }
                }
            });
        }
        return user;
    }

    public static void setUser(User user) {
        UserService.user = user;
    }

    public static boolean isUserAuthenticated() {
        return (getUser() != null && getUser().getOloAuthToken() != null);
    }

    private static void parseUser(User userObj) {
        if (userObj != null) {
            setInvalidAuthTokenHandlers();
            setUser(userObj);
            //AnalyticsManager.getInstance().setUserId(userObj.getEmailaddress());
            if (userObj.getEmailaddress() != null) {
                AnalyticsManager.getInstance().setUserId(userObj.getEmailaddress().substring(0, userObj.getEmailaddress().indexOf("@")));
            }
            //SpendGoSessionService.setSessionParams(getUser().getSpendGoId(), getUser().getSpendGoAuthToken());
            OloSessionService.setSessionParams(getUser().getOloAuthToken());
        }
    }

    private static void setInvalidAuthTokenHandlers() {
        OloSessionService.invalidAuthTokenCallback = new OloInvalidAuthCallback() {
            @Override
            public void onInvalidAuthTokenCallback() {
                handleInvalidAuthToken();
            }
        };
//        SpendGoSessionService.invalidAuthTokenCallback = new ISpendGoInvalidAuthToken() {
//            @Override
//            public void onInvalidAuthTokenCallback() {
//                handleInvalidAuthToken();
//            }
//        };
    }

    private static void clearSession() {
        //Bug: When guest user has basket active and tries to login but enters wrong password, basket gets deleted
        /**
         * TODO: uncomment when rewardservice is available
         */
        //RewardService.rewardSummary = null;
        OloSessionService.clearSession();
        SessionMaintainenceManager.clearSession();
        RecentOrdersService.clearAllData();
        AnalyticsManager.getInstance().setUserId(null);

        setUser(new User());
    }

    public static void updateUserInformation() {
        SessionMaintainenceManager.persistUserData();
    }

    public static void updateAvatarId(int id) {
        CbcAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE_IMAGE);
        CbcAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE);
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_profile_image", "Image::" + id);
        getUser().setAvatarId(id);
        updateUserInformation();
    }

//    private static void parseResponseAndNotify(Exception exception, WeakReference<UserServiceCallback> callbackWeakReference) {
//        User newUser = null;
//        if (exception != null) {
//            clearSession();
//            exception = getSpendGoErrorWithDescription(exception);
//        } else {
//            //User Logged In successfully
//
//
//            newUser = getUser();
//            newUser.setAvatarId(Utils.getRandomAvatarID());
//            UserService.updateUserInformation();
//            setInvalidAuthTokenHandlers();
//
//
//        }
//        UserServiceCallback cb = callbackWeakReference.get();
//        if (cb != null && !((Activity) cb).isFinishing()) {
//            cb.onUserServiceCallback(newUser, exception);
//        }
//    }

    private static void handleInvalidAuthToken() {
        UserService.clearSession();
        Context context = CbcApplication.getInstance();
        Utils.notify(context, BROADCAST_AUTH_TOKEN_FAILURE);
        Utils.notifyRemoveBasketUI(context);
        Utils.notifyHomeScreenUpdate(context);
    }

//    private static Exception getSpendGoErrorWithDescription(Exception exception) {
//        if (exception != null && exception instanceof VolleyError) {
//            int errorCode = Utils.getErrorCode(exception);
//            String details = "";
//            if (errorCode == MEMBER_NOT_EXIST.value) {
//                details = "This email/phone number is not registered. Please verify your credentials."; //SignIn with email address that is not registered.
//            } else if (errorCode == USERID_PASSWORD_INVALID.value) {
//                details = "User id or password is incorrect."; //SignUp with invalid credentials
//            } else if (errorCode == EMAIL_NOT_VALID_OR_NOT_EXIST.value) {
//                details = "This email is not registered. Please enter a valid email address."; //Forgot password with invalid email address.
//            } else if (errorCode == EMAIL_NOT_VALIDATED.value) {
//                details = "Please validate your email address.";
//            } else if (errorCode == MEMBER_ALREADY_EXISTS.value) {
//                details = "Email address already exists.";
//            } else if (errorCode == INTERNAL_SERVER_ERROR.value) {
//                details = "Some internal server error occurred. Please try again.";
//            }
//
//            if (!details.equals("")) {
//                VolleyError serverError = (VolleyError) exception;
//                NetworkResponse networkResponse = new NetworkResponse(errorCode, details.getBytes(), serverError.networkResponse.headers, false);
//                exception = new ServerError(networkResponse);
//            }
//        }
//        return exception;
//    }

   /* public static void getRecentOrder(RecentOrderCallback callback) {
        final WeakReference<RecentOrderCallback> callbackWeakReference = new WeakReference<RecentOrderCallback>(callback);
        getRecentOrders(new JambaUserOrderServiceCallBack() {
            @Override
            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
                RecentOrderCallback cb = callbackWeakReference.get();
                if (cb != null && !((Activity) cb).isFinishing()) {
                    recentOrder = new ArrayList<RecentOrder>();
                    if (jambaOrderStatuses != null) {
                        for (JambaOrderStatus status : jambaOrderStatuses) {
                            recentOrder.add(new RecentOrder(status));
                        }
                        if (jambaOrderStatuses.length > 0) {
//                            NotificationManager.removeTag(NoPurchase.value); //removed urbanairship
                        } else {
//                            NotificationManager.addTag(NoPurchase.value); //removed urbanairship
                        }
                    }
                    cb.onOrderCallback(recentOrder, exception);
                }
            }
        });
    }

    public static void getRecentOrders(final JambaUserOrderServiceCallBack callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onUserOrderServiceCallBack(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/recentorders";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                if (response != null) {
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("orders");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserOrderServiceCallBack(jorderStatuses, error);
                }
            }
        });
    }

    public static void getFavoriteOrderDetail(String id, RecentOrderCallback callback) {
        final WeakReference<RecentOrderCallback> callbackWeakReference = new WeakReference<RecentOrderCallback>(callback);
        getRecentOrdersDetail(id, new JambaUserOrderServiceCallBack() {
            @Override
            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
                RecentOrderCallback cb = callbackWeakReference.get();
                if (cb != null) {
                    recentOrder = new ArrayList<RecentOrder>();
                    if (jambaOrderStatuses != null) {
                        for (JambaOrderStatus status : jambaOrderStatuses) {
                            recentOrder.add(new RecentOrder(status));
                        }
                        if (jambaOrderStatuses.length > 0) {
//                            NotificationManager.removeTag(NoPurchase.value); //removed urbanairship
                        } else {
//                            NotificationManager.addTag(NoPurchase.value); //removed urbanairship
                        }
                    }
                    cb.onOrderCallback(recentOrder, exception);
                }
            }
        });
    }

    private static void getRecentOrdersDetail(String id, final JambaUserOrderServiceCallBack callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onUserOrderServiceCallBack(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "/baskets/createfromfave";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("faveid", id);
        OloService.getInstance().post(path, authToken, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                if (response != null) {
                    Gson gson = new Gson();
                    try {
                        JSONArray jArrayObject = new JSONArray();
                        jArrayObject.put(response);

                        jorderStatuses = gson.fromJson(jArrayObject.toString(), JambaOrderStatus[].class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserOrderServiceCallBack(jorderStatuses, error);
                }
            }
        });
    }

    public static void getFavoriteOrder(FavoriteOrderCallback callback) {
        final WeakReference<FavoriteOrderCallback> callbackWeakReference = new WeakReference<FavoriteOrderCallback>(callback);
        getFavoriteOrders(new JambaUserOrderServiceCallBack() {
            @Override
            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
                FavoriteOrderCallback cb = callbackWeakReference.get();
                if (cb != null && !((Activity) cb).isFinishing()) {
                    favOrder = new ArrayList<FavoriteOrder>();
                    if (jambaOrderStatuses != null) {
                        for (JambaOrderStatus status : jambaOrderStatuses) {
                            favOrder.add(new FavoriteOrder(status));
                        }
                        if (jambaOrderStatuses.length > 0) {
//                            NotificationManager.removeTag(NoPurchase.value); //removed urbanairship
                        } else {
//                            NotificationManager.addTag(NoPurchase.value); //removed urbanairship
                        }
                    }
                    cb.onFavoriteCallback(favOrder, exception);
                }
            }
        });
    }

    public static void getFavoriteOrders(final JambaUserOrderServiceCallBack callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onUserOrderServiceCallBack(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                if (response != null) {
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("faves");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onUserOrderServiceCallBack(jorderStatuses, error);
                }
            }
        });
    }
//    public static void addFavoriteOrder(int id,String name,FavoriteOrderCallback callback) {
//        final WeakReference<FavoriteOrderCallback> callbackWeakReference = new WeakReference<FavoriteOrderCallback>(callback);
//        addFavoriteOrders(id,name, new JambaUserOrderServiceCallBack() {
//            @Override
//            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
//                FavoriteOrderCallback cb = callbackWeakReference.get();
//                if (cb != null && !((Activity) cb).isFinishing()) {
//                    favOrder = new ArrayList<FavoriteOrder>();
//                    if (jambaOrderStatuses != null) {
//                        for (JambaOrderStatus status : jambaOrderStatuses) {
//                            favOrder.add(new FavoriteOrder(status));
//                        }
//                        if (jambaOrderStatuses.length > 0) {
////                            NotificationManager.removeTag(NoPurchase.value); //removed urbanairship
//                        } else {
////                            NotificationManager.addTag(NoPurchase.value); //removed urbanairship
//                        }
//                    }
//                    cb.onFavoriteCallback(favOrder, exception);
//                }
//            }
//        });
//    }

    public static void addFavoriteOrders(String id, String name, final FavoriteOrderCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onFavoriteCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("basketid", id);
        parameters.put("description", name);
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                if (response != null) {
                    Log.e("response:s", String.valueOf(response));
                   *//* Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("faves");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }*//*
                }
                if (callback != null) {
                    callback.onFavoriteCallback(null, error);
                }
            }
        });
    }

    public static void removeFavoriteOrders(String id, final FavoriteOrderCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onFavoriteCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "users/" + authToken + "/faves/" + id;
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                if (response != null) {
                    Log.e("response:s", String.valueOf(response));
                   *//* Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("faves");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }*//*
                }
                if (callback != null) {
                    callback.onFavoriteCallback(null, error);
                }
            }
        });
    }

    public static void getDeliveryStatus(String id, final RecentOrderCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onOrderCallback(null, new Exception("User is not authenticated."));
            return;
        }
        String path = "orders/" + id + "/deliverystatus";
        OloService.getInstance().get(path, null, authToken, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                JambaOrderStatus[] jorderStatuses = null;
                List<RecentOrder> neworder = null;
                if (response != null) {
                    neworder = new ArrayList<RecentOrder>();
                    Log.e("response:s", String.valueOf(response));
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("deliveries");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                        neworder.add(new RecentOrder(jorderStatuses[0]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onOrderCallback(neworder, error);
                }
            }
        });
    }
*/
}
