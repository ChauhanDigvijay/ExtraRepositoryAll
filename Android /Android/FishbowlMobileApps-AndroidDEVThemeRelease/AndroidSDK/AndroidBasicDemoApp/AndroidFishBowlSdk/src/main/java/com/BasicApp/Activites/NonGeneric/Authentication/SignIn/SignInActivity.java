package com.BasicApp.Activites.NonGeneric.Authentication.SignIn;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Authentication.SignUp.SignUpThemeActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardActivity;
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
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.fishbowl.basicmodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Timer;

/**
 * Created by digvijay(dj)
 */
public class SignInActivity extends Activity implements View.OnClickListener {
    ImageView imbackground;
    EditText epass, eemail;
    LoginButton loginButton;
    Button bt1;
    Timer myTimer;
    RelativeLayout mtoolbar;
    TextView newUser, forgotPassword, companyName, btnforpassword;
    LinearLayout fblogin;
    ProgressBarHandler progressBarHandler;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private NetworkImageView loginBackground, imlogo, imforward, headerImage, leftImage, imbottomtask;
    //ImageView imlogo;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_sign_in);
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
        getThemeMobileSettingsServiceSettings();
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

    public void onCustomBackPressed() {
        SignInActivity.this.finish();
    }


    public boolean checkValidation() {

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


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bt_checkin) {

            if (checkValidation()) {

                loginMember();


            }
        }
        if (v.getId() == R.id.fblogin) {
            Intent ii = new Intent(SignInActivity.this, FBLoginActivity.class);
            startActivity(ii);
        }
        if (v.getId() == R.id.user_signup) {
            {
                gettestToken();
            }
        }
        if (v.getId() == R.id.btn_forpassword) {

            Intent ii = new Intent(this, ForgotPasswordActivity.class);
            startActivity(ii);

        }
    }


    public void switchToSignUp() {

        progressBarHandler.dismiss();
        Intent ii = new Intent(this, SignUpThemeActivity.class);
        startActivity(ii);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void loginMember() {
        JSONObject object = new JSONObject();
        try {
            String email = eemail.getText().toString();

            object.put("username", eemail.getText().toString());
            object.put("password", epass.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBarHandler.show();

        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {

                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignInActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignInActivity.this).setSignin(true);

                        //     FBAnalyticsManager.sharedInstance().track_ItemWith(user.getSpendGoId(),user.getFirstname()+" "+user.getLastname(), FBEventSettings.LOGIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getMember();

                } else {

                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);
                }
            }

        });
    }

    public void mobileSettings() {
        progressBarHandler.show();
        String cusId = "0";

        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback(){
            @Override
            public void OnFBMobileSettingCallback(boolean state, Exception error) {
                if(state) {
                    Log.d("Mobile Settings  Api", "Success");
                    trackFirstLaunch();
                    progressBarHandler.dismiss();
                }
                else{

                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }

    private void trackFirstLaunch() {
        boolean isFirstLaunch =  FBPreferences.sharedInstance(SignInActivity.this).getFirsttime_launch();
        if (!isFirstLaunch) {
            FBAnalyticsManager.sharedInstance().track_EvenforGuesttbyName(FBEventSettings.FIRST_TIME_LAUNCH);
            FBPreferences.sharedInstance(SignInActivity.this).setFirsttime_launch(true);
        }


    }


    public void getMember() {

        JSONObject object = new JSONObject();

        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {

                progressBarHandler.dismiss();
                if (response != null) {
                    String homeStoreID = null;
                    try {
                        homeStoreID = response.getString("homeStoreID");
                        FBPreferences.sharedInstance(SignInActivity.this).setStoreCode(homeStoreID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent ii = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(ii);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGIN);
                    deviceUpdate();
                } else {
                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);
                }
            }

        });
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
                        FBPreferences.sharedInstance(SignInActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignInActivity.this).setSignin(true);
                        getMember();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);
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
                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);
                    loginFacebookMember(object);
                }

            }
        });
    }

    public void deviceUpdate() {


        JSONObject object = new JSONObject();

        Member member = FBUserService.sharedInstance().member;

        try {
            object.put("memberid", member.customerID);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
            object.put("appId", "com.fishbowl.BasicApp");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().deviceUpdate(object, new FBUserService.FBDeviceUpdateCallback() {
            @Override
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {

                if (response != null) {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    FBUtils.tryHandleTokenExpiry(SignInActivity.this, error);

                }
            }
        });
    }

    public void getMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(JSONObject response, final Exception error) {

                if (response != null) {

                }
            }
        });
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



    public void getThemeMobileSettingsServiceSettings() {

        final JSONObject object = new JSONObject();

        FBThemeMobileSettingsService.sharedInstance().getThemeSettings(object, new FBThemeMobileSettingsService.FBThemeSettingsCallback() {
            @Override
            public void onThemeSettingsCallback(JSONObject response, final Exception error) {


                if(response !=null) {

                  //  FBThemeMobileSettingsService.sharedInstance().initFromJson(response);
                }else {

                    FBUtils.tryHandleTokenExpiry(SignInActivity.this,error);
                }
            }
        });
    }

    public void gettestToken() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {
            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (response != null) {
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                        FBPreferences.sharedInstance(SignInActivity.this).setAccessTokenforapp(secratekey);
                        FBUserService.sharedInstance().access_token = response.getString("message");
                        switchToSignUp();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });
    }

}




