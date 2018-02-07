package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Interfaces.FBASessionServiceCallback;
import com.BasicApp.BusinessLogic.Interfaces.FBAUserServiceCallback;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;

import java.lang.ref.WeakReference;

import static com.fishbowl.basicmodule.Controllers.FBSdk.context;


/**
 * Created by digvijay(dj)
 */
public class FBASessionService {

    public static FBASessionService instance=null;
    public static Activity mContext;
    public static FBASessionService sharedInstance(Activity context){

        if(instance==null){
            instance=new FBASessionService(context);
        }

        return  instance;
    }
    public FBASessionService(Activity context)
    {
        if(context == null);
        mContext = context;
    }


    public static void signInUser(String email, String password, final FBASessionServiceCallback callback) {

        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBSessionService.loginMember(email, password, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem fbsessionitem, Exception error) {
                if (fbsessionitem != null) {

                    String secratekey = fbsessionitem.getAccessToken();
                    FBPreferences.sharedInstance(mContext).setAccessTokenforapp(secratekey);

                    FBPreferences.sharedInstance(mContext).setSignin(true);
                   // GetMember(callback);
                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(fbsessionitem, error);
                    }
                }else {

                    FBUtils.tryHandleTokenExpiry(mContext, error);
                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(fbsessionitem, error);
                    }

                }
            }
        });
    }

    public void createUser(FBMember user) {

        FBSessionService.createMember(user, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem fbsessionItem, Exception error) {
                if (fbsessionItem != null) {




                } else {

                }


            }


        });
    }



    public static void getToken() {

        FBSessionService.getTokenApi(new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (error!=null) {

                } else {

                    FBUtils.tryHandleTokenExpiry(mContext, error);

                }
            }
        });
    }

    public static void GetMember(final FBAUserServiceCallback callback,final FBASessionServiceCallback callback1) {
        final WeakReference<FBAUserServiceCallback> callbackWeakReference = new WeakReference<FBAUserServiceCallback>(callback);
        FBSessionService.getMember(new FBUserServiceCallback() {
            @Override
            public void onUserServiceCallback(FBMember user, Exception error) {


                if (user != null) {
                    String homeStoreID = user.getStoreCode();
                    Long customerID = user.getMemberId();
                    FBPreferences.sharedInstance(context).setUserMemberforAppId(String.valueOf(customerID));
                    FBPreferences.sharedInstance(mContext).setStoreCode(homeStoreID);
                    //hard code storecode
                    FBPreferences.sharedInstance(mContext).setStoreCode("168574");

//                    Intent ii = new Intent(mContext, DashboardModelActivity.class);
//                    mContext.startActivity(ii);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGIN);
                    FBAUserServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(user, error);
                    }
                    UpdateDevice(callback1);

                } else {

                    FBUtils.tryHandleTokenExpiry(mContext, error);
                    FBAUserServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(user, error);
                    }
                }

            }
        });
    }

    public static void UpdateDevice(FBASessionServiceCallback callback) {

        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBSessionService.deviceUpdate(new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(FBSessionItem fbsessionitem, Exception error) {
                if (fbsessionitem != null) {

//                    FBASessionServiceCallback cb = callbackWeakReference.get();
//                    if (cb != null && !((Activity) cb).isFinishing()) {
//                        cb.onUserServiceCallback(fbsessionitem, error);
//                    }

                } else {

                    FBUtils.tryHandleTokenExpiry(mContext, error);
                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(fbsessionitem, error);
                    }
                }
            }
        });
    }

    public void forgetPassword(String email, FBASessionServiceCallback callback) {
        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBSessionService.forgetPassword(email, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {

                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(spendGoSession, error);
                    }



                } else {

                    FBUtils.showErrorAlert(mContext, error);
                }
            }
        });
    }


    public void changePassword(String oldpassword, String newpassword, FBASessionServiceCallback callback) {
        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBSessionService.changePassword(oldpassword, newpassword, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {
                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onUserServiceCallback(spendGoSession, error);
                    }



                } else {

                    FBUtils.showErrorAlert(mContext, error);
                }
            }
        });
    }


    public void getUserLogout(String application) {

        FBSessionService.logout(application,new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {


                } else {

                }
            }
        });
    }
}
