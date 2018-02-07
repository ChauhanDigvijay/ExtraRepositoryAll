package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.google.gson.Gson;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.FavoriteOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ForgotPasswordCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.JambaUserOrderServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.LookUpCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RecentOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserLogoutCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.SessionPersistenceManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.JambaOrderStatus;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloInvalidAuthCallback;
import com.wearehathway.apps.olo.Interfaces.OloServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloSessionAuthTokenCallback;
import com.wearehathway.apps.olo.Interfaces.OloUserLogoutCallback;
import com.wearehathway.apps.olo.Services.OloService;
import com.wearehathway.apps.olo.Services.OloSessionService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoForgotPasswordService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoInvalidAuthToken;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoLookUpService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSessionService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSignOffService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoUserService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoUserUpdateService;
import com.wearehathway.apps.spendgo.Models.SpendGoSession;
import com.wearehathway.apps.spendgo.Models.SpendGoUser;
import com.wearehathway.apps.spendgo.Services.SpendGoLookUpService;
import com.wearehathway.apps.spendgo.Services.SpendGoSessionService;
import com.wearehathway.apps.spendgo.Services.SpendGoUserService;
import com.wearehathway.apps.spendgo.Utils.SpendGoConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.BROADCAST_AUTH_TOKEN_FAILURE;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALID_OR_NOT_EXIST;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.INTERNAL_SERVER_ERROR;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.MEMBER_ALREADY_EXISTS;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.MEMBER_NOT_EXIST;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.USERID_PASSWORD_INVALID;

/**
 * Created by Nauman Afzaal on 07/05/15.
 */
public class UserService {

    public static JambaApplication _app;
    public static List<RecentOrder> recentOrder;
    public static List<FavoriteOrder> favOrder;
    private static User user = new User();

    public static User getUser() {
        if (user == null) {
            // Incase user object was destroyed and user was actually authenticated.
            SessionPersistenceManager.loadUserSession(new UserServiceCallback() {
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
        return (getUser() != null && getUser().getSpendGoAuthToken() != null && getUser().getOloAuthToken() != null);
    }

    private static void parseUser(User userObj) {
        if (userObj != null) {
            setInvalidAuthTokenHandlers();
            setUser(userObj);
            //AnalyticsManager.getInstance().setUserId(userObj.getEmailaddress());
            if (userObj.getEmailaddress() != null) {
                AnalyticsManager.getInstance().setUserId(userObj.getEmailaddress().substring(0, userObj.getEmailaddress().indexOf("@")));
            }
            SpendGoSessionService.setSessionParams(getUser().getSpendGoId(), getUser().getSpendGoAuthToken());
            OloSessionService.setSessionParams(getUser().getOloAuthToken());
        }
    }

    public static void loadSession() {
        //Before starting app load user session.
        SessionPersistenceManager.loadUserSession(new UserServiceCallback() {
            @Override
            public void onUserServiceCallback(User userObj, Exception exception) {
                if (userObj != null) {
                    parseUser(userObj);
                } else {
                    // If user is not logged in, flag as no purchases made (for logged-in users, rely on order history)
//                    NotificationManager.addTag(NoPurchase.value); //removed urbanairship
                }
            }
        });
    }

    // User Services
    public static void lookUpUserEmail(Activity activity, String email, final LookUpCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        SpendGoLookUpService.lookUp(email, "", new ISpendGoLookUpService() {
            @Override
            public void onLookUpServiceCallback(String status, Exception error) {
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onLookUpCallback(status, error);
                }
            }
        });
    }

    public static void lookUpUserPhone(Activity activity, String phone, final LookUpCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        SpendGoLookUpService.lookUp("", phone, new ISpendGoLookUpService() {
            @Override
            public void onLookUpServiceCallback(String status, Exception error) {
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onLookUpCallback(status, error);
                }
            }
        });
    }

    public static void getUserProfileInformation(Activity activity, final UserServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        SpendGoUserService.getMemberWithId(getUser().getSpendGoId(), new ISpendGoUserService() {
            @Override
            public void onUserServiceCallback(SpendGoUser spendGoUser, Exception error) {
                //We may need to update olo restaurant id of user if user updates his preferred store. For now we are only showing stores with order ahead so we dont need it.
                if (spendGoUser != null) {
                    getUser().updateUserWithSpendGoData(spendGoUser);
                    if (spendGoUser.getFavorite_store() != null) {
                        if (spendGoUser.getFavorite_store().getCode() != null) {
                            StoreService.getStoreInformation(spendGoUser.getFavorite_store().getCode(), new StoreDetailCallback() {
                                @Override
                                public void onStoreDetailCallback(Store store, Exception exception) {
                                    if (store != null) {
                                        getUser().setStoreName(store.getName());
                                        getUser().setStoreAddress(store.getCompleteAddress());
                                        getUser().setLatitude(store.getLatitude());
                                        getUser().setLongitude(store.getLongitude());
                                    }
                                }
                            });
                        }
                    }
                }
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onUserServiceCallback(getUser(), error);
                }
            }
        });
    }

    public static void signUpUser(String fName, String lName, final String email, String dob, boolean email_opt_in, boolean sms_opt_in, String contactNumber, final String password, String favoriteStoreCode, final UserServiceCallback callback) {
        SpendGoUser user = new SpendGoUser();
        user.setPhone(contactNumber);
        user.setSms_opt_in(sms_opt_in);
        user.setEmail(email);
        user.setEmail_opt_in(email_opt_in);
        user.setFirst_name(fName);
        user.setLast_name(lName);
        user.setDob(dob);

        final WeakReference<UserServiceCallback> callbackWeakReference = new WeakReference<UserServiceCallback>(callback);
        SpendGoSessionService.addUser(user, password, favoriteStoreCode, true, false, new ISpendGoSessionService() {
            @Override
            public void onSessionServiceCallback(SpendGoSession spendGoSession, Exception exception) {
                if (spendGoSession.getAuthToken() != null) {
                    getOloTokenFromSpendGoToken(false, spendGoSession, callbackWeakReference);
                } else {
                    if (Utils.getErrorCode(exception) == SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED.value) {
                        // User has successfully registered.
                        AnalyticsManager.getInstance().trackUserRegistration();
                    }
                    UserServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(null, exception);
                    }
                }
            }
        });
    }

    /* During SignUp Fecth following information
    * SpendGo AUth Token.
    * Olo Auth Token.
    * SpendGo Member Information.
    * OloRestaurant Information.
    * */
    public static void signInUser(String email, String password, UserServiceCallback callback) {
        final WeakReference<UserServiceCallback> callbackWeakReference = new WeakReference<UserServiceCallback>(callback);
        SpendGoSessionService.signIn(email, password, new ISpendGoSessionService() {
            @Override
            public void onSessionServiceCallback(final SpendGoSession spendGoSession, Exception error) {
                if (error != null)

                {
                    parseResponseAndNotify(error, callbackWeakReference);
                } else {
                    getOloTokenFromSpendGoToken(true, spendGoSession, callbackWeakReference);
                }
            }
        });
    }

    private static void getOloTokenFromSpendGoToken(final boolean isSigningIn, final SpendGoSession spendGoSession, final WeakReference<UserServiceCallback> callbackWeakReference) {
        getUser().setSpendGoAuthToken(spendGoSession.getAuthToken());
        Basket basket = DataManager.getInstance().getCurrentBasket();
        String basketId = null;
        if (basket != null) {
            basketId = basket.getId();
        }
        OloSessionService.getOrCreateUser("spendgo", spendGoSession.getAuthToken(), "", basketId, new OloSessionAuthTokenCallback() {
            @Override
            public void onOloSessionAuthTokenCallback(String authToken, Exception error) {
                if (!authToken.equals("")) {
                    getUser().setOloAuthToken(authToken);
                    getMemeberWithId(spendGoSession, callbackWeakReference);
                } else {
                    if (isSigningIn) {
                        parseResponseAndNotify(error, callbackWeakReference);
                    } else {
                        parseResponseAndNotify(null, callbackWeakReference); // Incase user during sign up exchange token call fails notify user that account has been created now sign in.
                    }
                }
            }
        });
    }

    private static void getMemeberWithId(final SpendGoSession spendGoSession, final WeakReference<UserServiceCallback> callbackWeakReference) {
        SpendGoUserService.getMemberWithId(spendGoSession.getSpendGoId(), new ISpendGoUserService() {
            @Override
            public void onUserServiceCallback(SpendGoUser spendGoUser, Exception error) {
                if (spendGoUser != null) {
                    getUser().updateUserWithSpendGoData(spendGoUser);
                    AuditService.trackUserAccess(getUser(), "login");
                    AnalyticsManager.getInstance().trackUserLogin();
                    getOloRestaurantInfo(getUser().getFavoriteStoreCode(), callbackWeakReference);
                } else {
                    parseResponseAndNotify(error, callbackWeakReference);
                }
            }
        });
    }

    private static void getOloRestaurantInfo(String storeCode, final WeakReference<UserServiceCallback> callbackWeakReference) {
        StoreService.getStoreInformation(storeCode, new StoreDetailCallback() {
            @Override
            public void onStoreDetailCallback(Store store, Exception error) {
                if (error != null) {
                    int errorCode = Utils.getErrorCode(error);
                    if (errorCode == 200) {
                        // Restaurant with this store code does not exists. No need to notify this error
                        error = null;
                    }
                } else if (store != null) {
                    getUser().setOloFavoriteRestaurantId(store.getRestaurantId());
                    getUser().setLatitude(store.getLatitude());
                    getUser().setLongitude(store.getLongitude());
                }
                parseResponseAndNotify(error, callbackWeakReference);
            }
        });
    }

    public static void forgotPassword(final String email, ForgotPasswordCallback callback) {
        final WeakReference<ForgotPasswordCallback> callbackWeakReference = new WeakReference<ForgotPasswordCallback>(callback);
        SpendGoUserService.forgotPassword(email, new ISpendGoForgotPasswordService() {
            @Override
            public void onForgotPasswordCallback(Exception error) {
                if (error != null) {
                    error = getSpendGoErrorWithDescription(error);
                } else {
                    AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "forgot_password");
                }
                ForgotPasswordCallback cb = callbackWeakReference.get();
                if (cb != null && !((Activity) cb).isFinishing()) {
                    cb.onForgotPasswordCallback(error);
                }
            }
        });
    }

    public static void logout(UserLogoutCallback callback) {
        final WeakReference<UserLogoutCallback> callbackWeakReference = new WeakReference<UserLogoutCallback>(callback);
        if (UserService.isUserAuthenticated()) {
            OloSessionService.logOutUser(new OloUserLogoutCallback() {
                @Override
                public void onUserLogoutCallback(Exception exception) {
                    SpendGoSessionService.signOff(getUser().getSpendGoAuthToken(), new ISpendGoSignOffService() {
                        @Override
                        public void onSignOffCallback(Exception exception) {
                            final User user1 = user;
                            clearSession();// Clear user session in all cases.
                            UserLogoutCallback cb = callbackWeakReference.get();
                            if (cb != null && !((Activity) cb).isFinishing()) {
                                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "logout");
                                cb.onUserLogoutCallback(null);
                            }
                        }
                    });
                }
            });
        } else {
            clearSession();//Just safety check to make sure everything is deleted.
            UserLogoutCallback cb = callbackWeakReference.get();
            if (cb != null && !((Activity) cb).isFinishing()) {
                cb.onUserLogoutCallback(null);
            }
        }
    }

    public static void updateUserInformation(final String firstName, final String lastName, boolean isSmsEnabled, boolean isEmailEnabled, final String dob, UserUpdateCallback callback) {
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE);
        final WeakReference<UserUpdateCallback> callbackWeakReference = new WeakReference<UserUpdateCallback>(callback);
        final SpendGoUser spendGoUser = getSpendGoUserWithBasicValues();
        spendGoUser.setFirst_name(firstName);
        spendGoUser.setLast_name(lastName);
        spendGoUser.setEmail_opt_in(isEmailEnabled);
        spendGoUser.setSms_opt_in(isSmsEnabled);
        spendGoUser.setDob(dob);
        sendUpdateCall(spendGoUser, "", "", new UserUpdateCallback() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                if (error == null) {
                    User user = getUser();
                    if (!firstName.equals("")) {
                        user.setFirstname(firstName);
                    }
                    if (!lastName.equals("")) {
                        user.setLastname(lastName);
                    }
                    if (!dob.equals("")) {
                        user.setDob(dob);
                    }
                    user.setEnableEmailOpt(spendGoUser.isEmail_opt_in());
                    user.setEnableSmsOpt(spendGoUser.isSms_opt_in());
                    updateUserInformation();
                }
                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_profile");
                // if user info has been updated notify user.
                notifyUserUpdateCallback(callbackWeakReference, error);
            }
        });
    }

    public static void changeEmail(final String email, UserUpdateCallback callback) {
        final WeakReference<UserUpdateCallback> callbackWeakReference = new WeakReference<UserUpdateCallback>(callback);
        SpendGoUser spendGoUser = getSpendGoUserWithBasicValues();
        spendGoUser.setEmail(email);
        final String id = spendGoUser.getSpendgo_id();
        sendUpdateCall(spendGoUser, "", "", new UserUpdateCallback() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                //Do not update user email locally We need to update user email once user verify link sent to new email address.
                //                int errorCode = Utils.getErrorCode(error);
                //                if (error != null && errorCode == EMAIL_NOT_VALIDATED.value)
                //                {
                //                    getUser().setEmailaddress(email);
                //                    updateUserInformation();
                //                }

                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_email");
                notifyUserUpdateCallback(callbackWeakReference, error);
            }
        });
    }

    public static void changeContactNumber(final String phoneNumber, UserUpdateCallback callback) {
        final WeakReference<UserUpdateCallback> callbackWeakReference = new WeakReference<UserUpdateCallback>(callback);
        SpendGoUser spendGoUser = getSpendGoUserWithBasicValues();
        spendGoUser.setPhone(phoneNumber);
        sendUpdateCall(spendGoUser, "", "", new UserUpdateCallback() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                if (error == null) {
                    getUser().setContactnumber(phoneNumber);
                    updateUserInformation();
                }

                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_phone");
                notifyUserUpdateCallback(callbackWeakReference, error);
            }
        });
    }

    public static void changeUserPassword(String newPassword, UserUpdateCallback callback) {
        final WeakReference<UserUpdateCallback> callbackWeakReference = new WeakReference<UserUpdateCallback>(callback);
        SpendGoUser spendGoUser = getSpendGoUserWithBasicValues();
        sendUpdateCall(spendGoUser, newPassword, "", new UserUpdateCallback() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_password");
                notifyUserUpdateCallback(callbackWeakReference, error);
            }
        });
    }

    public static void updateFavoriteStore(final Store store, final UserUpdateCallback callback) {
        final WeakReference<UserUpdateCallback> callbackWeakReference = new WeakReference<UserUpdateCallback>(callback);
        SpendGoUser spendGoUser = getSpendGoUserWithBasicValues();
        sendUpdateCall(spendGoUser, "", store.getStoreCode(), new UserUpdateCallback() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                if (error == null) {
                    getUser().setSpendGoFavoriteStoreId(store.getId());
                    getUser().setFavoriteStoreCode(store.getStoreCode());
                    getUser().setOloFavoriteRestaurantId(store.getRestaurantId());
                    getUser().setStoreName(store.getName());
                    getUser().setStoreAddress(store.getCompleteAddress());
                    getUser().setLongitude(store.getLongitude());
                    getUser().setLatitude(store.getLatitude());
                    getUser().setStoreTelephoneNumber(store.getTelephone());
                    updateUserInformation();

                    StoreService.getStoreInformation(store.getStoreCode(), new StoreDetailCallback() {
                        @Override
                        public void onStoreDetailCallback(Store oloStore, Exception exception) {
                            if (oloStore != null) {
                                getUser().setLongitude(oloStore.getLongitude());
                                getUser().setLatitude(oloStore.getLatitude());
                                updateUserInformation();
                            }
                        }
                    });
                }
                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_favorite_store", store.getName());
                notifyUserUpdateCallback(callbackWeakReference, error);
            }
        });
    }

    private static void sendUpdateCall(SpendGoUser spendGoUser, String newPassword, String favoriteStoreId, final UserUpdateCallback callback) {
        SpendGoUserService.updateUser(spendGoUser, newPassword, favoriteStoreId, new ISpendGoUserUpdateService() {
            @Override
            public void onUserUpdateCallback(Exception error) {
                callback.onUserUpdateCallback(error);
            }
        });
    }

    private static SpendGoUser getSpendGoUserWithBasicValues() {
        SpendGoUser spendGoUser = new SpendGoUser();
        spendGoUser.setSms_opt_in(getUser().isEnableSmsOpt());
        spendGoUser.setEmail_opt_in(getUser().isEnableEmailOpt());
        return spendGoUser;
    }

    private static void notifyUserUpdateCallback(WeakReference<UserUpdateCallback> callbackWeakReference, Exception error) {
        if (error != null) {
            error = getSpendGoErrorWithDescription(error);
        }
        UserUpdateCallback cb = callbackWeakReference.get();
        if (cb != null && (cb instanceof Activity && !((Activity) cb).isFinishing()) || (cb != null && !(cb instanceof Activity))) {
            cb.onUserUpdateCallback(error);
        }
    }

    private static void clearSession() {
        //Bug: When guest user has basket active and tries to login but enters wrong password, basket gets deleted
        //DataManager.getInstance().resetDataManager();
        RewardService.rewardSummary = null;
        UserService.recentOrder = null;
        UserService.favOrder = null;
        SpendGoSessionService.clearCurrentSession();
        OloSessionService.clearSession();
        SessionPersistenceManager.clearSession();
        RecentOrdersService.clearAllData();
        AnalyticsManager.getInstance().setUserId(null);
//        NotificationManager.addTag(Constants.NotificationTag.NoPurchase.value); //Add user to non purchase made as he logged out now. // removed urbanairship
        setUser(new User());
    }

    public static void updateUserInformation() {
        SessionPersistenceManager.persistUserData();
    }

    public static void updateAvatarId(int id) {
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE_IMAGE);
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE);
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "update_profile_image", "Image::" + id);
        getUser().setAvatarId(id);
        updateUserInformation();
    }

    private static void parseResponseAndNotify(Exception exception, WeakReference<UserServiceCallback> callbackWeakReference) {
        User newUser = null;
        if (exception != null) {
            clearSession();
            exception = getSpendGoErrorWithDescription(exception);
        } else {
            //User Logged In successfully


            newUser = getUser();
            newUser.setAvatarId(Utils.getRandomAvatarID());
            UserService.updateUserInformation();
            setInvalidAuthTokenHandlers();


        }
        UserServiceCallback cb = callbackWeakReference.get();
        if (cb != null && !((Activity) cb).isFinishing()) {
            cb.onUserServiceCallback(newUser, exception);
        }
    }

    private static void setInvalidAuthTokenHandlers() {
        OloSessionService.invalidAuthTokenCallback = new OloInvalidAuthCallback() {
            @Override
            public void onInvalidAuthTokenCallback() {
                handleInvalidAuthToken();
            }
        };
        SpendGoSessionService.invalidAuthTokenCallback = new ISpendGoInvalidAuthToken() {
            @Override
            public void onInvalidAuthTokenCallback() {
                handleInvalidAuthToken();
            }
        };
    }

    private static void handleInvalidAuthToken() {
        UserService.clearSession();
        Context context = JambaApplication.getAppContext();
        Utils.notify(context, BROADCAST_AUTH_TOKEN_FAILURE);
        Utils.notifyRemoveBasketUI(context);
        Utils.notifyHomeScreenUpdate(context);
    }

    private static Exception getSpendGoErrorWithDescription(Exception exception) {
        if (exception != null && exception instanceof VolleyError) {
            int errorCode = Utils.getErrorCode(exception);
            String details = "";
            if (errorCode == MEMBER_NOT_EXIST.value) {
                details = "This email/phone number is not registered. Please verify your credentials."; //SignIn with email address that is not registered.
            } else if (errorCode == USERID_PASSWORD_INVALID.value) {
                details = "User id or password is incorrect."; //SignUp with invalid credentials
            } else if (errorCode == EMAIL_NOT_VALID_OR_NOT_EXIST.value) {
                details = "This email is not registered. Please enter a valid email address."; //Forgot password with invalid email address.
            } else if (errorCode == EMAIL_NOT_VALIDATED.value) {
                details = "Please validate your email address.";
            } else if (errorCode == MEMBER_ALREADY_EXISTS.value) {
                details = "Email address already exists.";
            } else if (errorCode == INTERNAL_SERVER_ERROR.value) {
                details = "Some internal server error occurred. Please try again.";
            }

            if (!details.equals("")) {
                VolleyError serverError = (VolleyError) exception;
                NetworkResponse networkResponse = new NetworkResponse(errorCode, details.getBytes(), serverError.networkResponse.headers, false);
                exception = new ServerError(networkResponse);
            }
        }
        return exception;
    }

//    public static void getRecentOrder(RecentOrderCallback callback)
//    {
//        final WeakReference<RecentOrderCallback> callbackWeakReference = new WeakReference<RecentOrderCallback>(callback);
//       OloUserService.getRecentOrders(new OloUserOrderServiceCallback()
//        {
//            @Override
//            public void onUserOrderServiceCallback(OloOrderStatus[] orderStatuses, Exception exception)
//            {
//                RecentOrderCallback cb = callbackWeakReference.get();
//                if (cb != null && !((Activity) cb).isFinishing())
//                {
//                    recentOrder = new ArrayList<RecentOrder>();
//                    if (orderStatuses != null)
//                    {
//                        for (OloOrderStatus status : orderStatuses)
//                        {
//                            recentOrder.add(new RecentOrder(status));
//                        }
//                        if(orderStatuses.length > 0)
//                        {
////                            NotificationManager.removeTag(NoPurchase.value); //removed urbanairship
//                        }
//                        else
//                        {
////                            NotificationManager.addTag(NoPurchase.value); //removed urbanairship
//                        }
//                    }
//                    cb.onOrderCallback(recentOrder, exception);
//                }
//            }
//        });
//    }

    public static void getRecentOrder(final RecentOrderCallback callback) {
        //final WeakReference<RecentOrderCallback> callbackWeakReference = new WeakReference<RecentOrderCallback>(callback);
        getRecentOrders(new JambaUserOrderServiceCallBack() {
            @Override
            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
               // RecentOrderCallback cb = callbackWeakReference.get();
               // if (cb != null && !((Activity) cb).isFinishing()) {
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
                    callback.onOrderCallback(recentOrder, exception);
             //   }
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

    public static void getFavoriteOrderDetail(String id, final RecentOrderCallback callback) {
        //final WeakReference<RecentOrderCallback> callbackWeakReference = new WeakReference<RecentOrderCallback>(callback);
        getRecentOrdersDetail(id, new JambaUserOrderServiceCallBack() {
            @Override
            public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception exception) {
               // RecentOrderCallback cb = callbackWeakReference.get();
                if (callback != null) {
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
                    callback.onOrderCallback(recentOrder, exception);
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
                   /* Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("faves");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }*/
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
                   /* Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("faves");
                        jorderStatuses = gson.fromJson(jsonArray.toString(), JambaOrderStatus[].class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = new Exception("Error occurred while parsing data.");
                    }*/
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
}
