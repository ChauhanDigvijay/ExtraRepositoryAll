package com.BasicApp.ActivityModel.Authentication.SignIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.ActivityModel.Authentication.SignUp.SignUpNewModelActivity;
import com.BasicApp.ActivityModel.Home.DashboardModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.fishbowl.basicmodule.Controllers.FBSdk.context;

/**
 * Created by digvijay(dj)
 */
public class SignInModelActivity extends Activity implements View.OnClickListener {

    EditText epass, eemail;
    LoginButton loginButton;
    Button bt1;
    RelativeLayout mtoolbar;
    TextView newUser, btnforpassword;
    ProgressBarHandler progressBarHandler;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private NetworkImageView loginBackground, imlogo;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_sign_in);
        //Save Android Pay
//        UpdatePay();
//        SavePay();
//        SaveUpdateGiftWidget b = (SaveUpdateGiftWidget) findViewById(R.id.bottom_toolbar);
//        b.initSaveUpdateGiftWidget(this,collectGiftCardCreateRequest());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBarHandler = new ProgressBarHandler(this);
        imlogo = (NetworkImageView) findViewById(R.id.im_logo);
        loginBackground = (NetworkImageView) findViewById(R.id.loginBackground);
        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });
        newUser = (TextView) findViewById(R.id.user_signup);
        newUser.setOnClickListener(this);
        btnforpassword = (TextView) findViewById(R.id.btn_forpassword);
        btnforpassword.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {


                                try {
                                    String email = object.getString("email");
                                    //  String birthday = object.getString("birthday");
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    //   tv_profile_name.setText(name);
                                    String imageurl = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    createMember(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday,location,locale,link");
                request.setParameters(parameters);
                request.executeAsync();
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        eemail = (EditText) findViewById(R.id.et_mail);
        eemail.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {
                eemail.setSelection(eemail.getText().length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    char c = s.charAt(0);
                    eemail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
                    if (c >= '0' && c <= '9') {
                        FBUtils.setUsDashPhone(s, before, eemail);

                    }
                }
            }

        });

        epass = (EditText) findViewById(R.id.et_pass);

        bt1 = (Button) findViewById(R.id.bt_checkin);
        bt1.setOnClickListener(this);
        mobileSettings();
        getMobileSettings();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getImageLoader();
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().loginBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(loginBackground, R.drawable.bgimage, R.drawable.bgimage));
            loginBackground.setImageUrl(url, mImageLoader);
            final String url2 = null;

            if (StringUtilities.isValidString(url2)) {
                mImageLoader.get(url2, ImageLoader.getImageListener(imlogo, R.drawable.welcomebanner, R.drawable
                        .welcomebanner));

                imlogo.setImageUrl(url2, mImageLoader);
            } else {
                imlogo.setBackgroundResource(R.drawable.welcomebanner);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bt_checkin) {

            if (checkValidation()) {

                loginMember();


            }
        }
        if (v.getId() == R.id.fblogin) {
            Intent ii = new Intent(SignInModelActivity.this, FBLoginActivity.class);
            startActivity(ii);
        }
        if (v.getId() == R.id.user_signup) {
            {
                getToken();
            }
        }
        if (v.getId() == R.id.btn_forpassword) {

            Intent ii = new Intent(this, ForgotPasswordModelActivity.class);
            startActivity(ii);

        }
    }


    public void onCustomBackPressed() {
        SignInModelActivity.this.finish();
    }


    public boolean checkValidation()
    {

        if (!(FBUtils.isValidString(eemail.getText().toString()) || FBUtils.isValidPhoneNumber(eemail.getText().toString()))) {
            FBUtils.showAlert(this, "Empty Email/Mobile");
            return false;
        }
        char c = eemail.getText().toString().charAt(0);
        if (!FBUtils.isValidString(epass.getText().toString())) {
            FBUtils.showAlert(this, "Empty Password");
            return false;
        }
        return true;
    }


    public void getToken()
    {

        FBSessionService.getTokenApi(new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (true) {
                    switchToSignUp();
                } else {
                    switchToSignUp();
                }
            }
        });
    }


    public void switchToSignUp()
    {

        progressBarHandler.dismiss();
        Intent ii = new Intent(this, SignUpNewModelActivity.class);
        startActivity(ii);
    }


    public void loginMember(String name, String password) {

        FBSessionService.loginMember(name, password, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null) {
                    //  Constants.alertDialogShow(SignInModelActivity.this, "loginMember Success Message" + spendGoSession);
                    String secratekey = spendGoSession.getAccessToken();
                    FBPreferences.sharedInstance(SignInModelActivity.this).setAccessTokenforapp(secratekey);
                    FBUserService.sharedInstance().access_token = spendGoSession.getAccessToken();
                    FBPreferences.sharedInstance(SignInModelActivity.this).setSignin(true);
                    GetMember();

                } else {
                    //    Constants.alertDialogShow(SignInModelActivity.this, "loginMember Error Message");
                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }


    public void GetMember() {

        FBSessionService.getMember(new FBUserServiceCallback() {
            @Override
            public void onUserServiceCallback(FBMember user, Exception error) {


                if (user != null) {
                    String homeStoreID = user.getStoreCode();
                    Long customerID = user.getMemberId();
                    FBPreferences.sharedInstance(context).setUserMemberforAppId(String.valueOf(customerID));
                    FBPreferences.sharedInstance(SignInModelActivity.this).setStoreCode(homeStoreID);
                    //hard code storecode
                    FBPreferences.sharedInstance(SignInModelActivity.this).setStoreCode("168574");

                    Intent ii = new Intent(SignInModelActivity.this, DashboardModelActivity.class);
                    startActivity(ii);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGIN);
                    UpdateDevice();

                }
                else {
                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                    progressBarHandler.dismiss();
                }

            }
        });
    }


    public void UpdateDevice() {

        FBSessionService.deviceUpdate(new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(FBSessionItem spendGoSession, Exception error) {
                if (error != null)

                {
                    progressBarHandler.dismiss();
                }
                else {

                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }



    public void loginMember() {
        JSONObject object = new JSONObject();
        try {

            object.put("username", eemail.getText().toString());
            object.put("password", epass.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBarHandler.show();
        loginMember(eemail.getText().toString(), epass.getText().toString());

    }

    public void mobileSettings() {
        progressBarHandler.show();
        String cusId = "0";

        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback() {
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                if (state) {
                    Log.d("Mobile Settings  Api", "Success");
                    trackFirstLaunch();
                    progressBarHandler.dismiss();
                } else {

                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }


    public void getMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(FBViewMobileSettingsService instance, final Exception error) {

                if (instance != null) {

                }
            }
        });
    }

    private void trackFirstLaunch() {
        boolean isFirstLaunch = FBPreferences.sharedInstance(SignInModelActivity.this).getFirsttime_launch();
        if (!isFirstLaunch) {
            FBAnalyticsManager.sharedInstance().track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);
            FBPreferences.sharedInstance(SignInModelActivity.this).setFirsttime_launch(true);
        }


    }


    public void loginFacebookMember(final JSONObject object1) {

        JSONObject object = new JSONObject();
        try {
            String email = (String) object1.get("email");

            object.put("username", email);
            object.put("password", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {

                if (response != null) {
                    try {
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignInModelActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignInModelActivity.this).setSignin(true);
                        //  getMember();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                }
            }

        });

    }

    public void createMember(final JSONObject object) {
        try {
            if (object.has("first_name"))
                object.put("firstName", object.get("first_name"));
            if (object.has("last_name"))
                object.put("lastName", object.get("last_name"));
            if (object.has("email"))
                object.put("email", object.get("email"));
            if (object.has("locale"))
                object.put("addressStreet", object.get("locale"));
            object.put("storeCode", "474");
            if (object.has("birthday"))
                object.put("birthDate", object.get("birthday"));
            if (object.has("gender")) {
                object.put("gender", object.get("gender"));
            }
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {

                if (response != null) {
                    try {


                        loginFacebookMember(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    FBUtils.tryHandleTokenExpiry(SignInModelActivity.this, error);
                    loginFacebookMember(object);
                }

            }
        });
    }



    //Android Save Pay

//    private JSONObject collectGiftCardCreateRequest() {
//        final JSONObject object = new JSONObject();
//        try {
//
//            object.put("Balance", 22000000);//should be in Long
//            object.put("BrandId", "711763");
//            object.put("BrandName", "JambaJuice");
//            object.put("BarcodeValue", "46079SJ6416");
//            object.put("CardId", 82);
//            object.put("CardNumber", "46079SJ6416");
//            object.put("CardName", "JambaJuice");
//            object.put("CardPin", "5441");
//            object.put("CardBackgroundColor", "#E0EDF7");
//            object.put("MessageTo", "Sridhar");
//            object.put("InitialBalance", 22000000);
//            object.put("ImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");
//            object.put("LastModifiedDate", "2017-09-15T14:44:36"); //format should be like yyyy-MM-dd'T'HH:mm:ss
//            object.put("ThumbnailImageUrl", "https://api.giftango.com/imageservice/Images/300x190/CIR_000735_00.png");
//
//
//            return object;
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//
//        }
//        return object;
//    }
//
//
//    private void UpdatePay() {
//        // Broadcast Receiver for receiving token To get BroadCast
//        BroadcastReceiver mMessageReceived = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                Intent i = intent;
//                Bundle extras = i.getExtras();
//                {
//                    if (extras != null) {
//                        String message = i.getExtras().getString("message");
//                        Boolean successflag = i.getExtras().getBoolean("successFlag");
//                        Constants.alertDialogShow(SignInModelActivity.this, successflag + "-------" + message);
//
//                    }
//                }
//            }
//        };
//        // Broadcast Receiver for receiving message
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mMessageReceived,
//                new IntentFilter(Config.UPDATESAVEPAY));
//    }
//
//    private void SavePay() {
//        // Broadcast Receiver for receiving token To get BroadCast
//        BroadcastReceiver mMessageReceived = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                Intent i = intent;
//                Bundle extras = i.getExtras();
//                {
//                    if (extras != null) {
//                        String message = i.getExtras().getString("message");
//                        Boolean successflag = i.getExtras().getBoolean("successFlag");
//                        Constants.alertDialogShow(SignInModelActivity.this, successflag + "-------" + message);
//
//                    }
//                }
//            }
//        };
//        // Broadcast Receiver for receiving message
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mMessageReceived,
//                new IntentFilter(Config.SAVEPAY));
//    }


}




