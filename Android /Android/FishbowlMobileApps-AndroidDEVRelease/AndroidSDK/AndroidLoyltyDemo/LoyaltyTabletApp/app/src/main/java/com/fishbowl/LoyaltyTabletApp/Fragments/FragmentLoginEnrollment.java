package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignUp.SignUpActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardPointSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryPointCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardPointService;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtility;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.FBPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by schaudhary_ic on 24-Jan-17.
 */

public class FragmentLoginEnrollment extends Fragment implements View.OnClickListener {
    Button bCheckin, button_signup;
    EditText et;
    ProgressBarHandler progressBarHandler;
    String fbm;
    private NetworkImageView background;
    private ImageLoader mImageLoader;
    private NetworkImageView footer_imageurl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_enrollment, container, false);
        bCheckin = (Button) v.findViewById(R.id.bt_checkIn);
        button_signup = (Button) v.findViewById(R.id.button_signup);
        background = (NetworkImageView) v.findViewById(R.id.right_side_image_url);
        footer_imageurl = (NetworkImageView) v.findViewById(R.id.footer_image_url);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        progressBarHandler = new ProgressBarHandler(getActivity());
        et = (EditText) v.findViewById(R.id.target);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        et.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {
                et.setSelection(et.getText().length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    char c = s.charAt(0);

                    if (c >= '0' && c <= '9') {
                        FBUtils.setUsDashPhone(s, before, et);
                    }
                }
            }

        });
        bCheckin.setOnClickListener(this);
        button_signup.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Editable str = et.getText();
        switch (v.getId()) {

            case R.id.bt_checkIn:
                if (checkValidation()) {

                    loginMember();

                }
                break;
            case R.id.button_signup:

                gettestToken();
                break;
        }
    }

    public void gettestToken() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(getActivity()));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FB_LY_UserService.sharedInstance().getTokenApi(object, new FB_LY_UserService.FBGetTokenCallback() {
            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (response != null) {
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                        FBPreferences.sharedInstance(getActivity()).setAccessTokenforapp(secratekey);
                        FB_LY_UserService.sharedInstance().access_token = response.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //	FBPreferences.sharedInstance(SignInActivity.this).setSignin(true);

                    Intent ii = new Intent(getContext(), SignUpActivity.class);
                    startActivity(ii);
                    progressBarHandler.dismiss();

                } else {
                }
            }
        });
    }

    public void signup() {
        Intent ii = new Intent(getContext(), SignUpActivity.class);
        startActivity(ii);
    }


    public void getToken() {


        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(getActivity()));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FB_LY_UserService.sharedInstance().getTokenApi(object, new FB_LY_UserService.FBGetTokenCallback() {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (response != null) {

                    try {
                        String secratekey = response.getString("message");
                        FBPreferences.sharedInstance(getActivity()).setAccessTokenforapp(secratekey);
                        loginMember();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }
        });
    }


    public void loginMember() {
        JSONObject object = new JSONObject();
        progressBarHandler.show();
        String email = et.getText().toString();
        Date d = Calendar.getInstance().getTime();

        String format = null;
        if (format == null)
            format = "yyyy-MM-dd'T'hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        try {
            object.put("username", email);
            object.put("password", "password");
            object.put("eventDateTime", currentData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FB_LY_UserService.sharedInstance().loginMember(object, new FB_LY_UserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(getContext()).setAccessTokenforapp(secratekey);
                        FB_LY_UserService.sharedInstance().access_token = response.getString("accessToken");
                        FBPreferences.sharedInstance(getContext()).setSignin(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getMember();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }

        });
    }

    private void fetchRewardPoint() {

        RewardPointService.getUserRewardPoint((Activity) getContext(), new RewardSummaryPointCallback() {
            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {
                if (rewardSummary != null) {
                    progressBarHandler.dismiss();
                    Intent ii = new Intent(getContext(), HomeActivity.class);
                    startActivity(ii);
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }

    public void getMember() {
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    fetchRewardPoint();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String url1 = FB_LY_MobileSettingService.sharedInstance().loginFooterImageUrl;
        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginFooterImageUrl;
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        } else {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }

        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));
            button_signup.setBackgroundColor(Color.parseColor(btncolor));
        }
    }

    public void passwordField() {

        final String url1 = FB_LY_MobileSettingService.sharedInstance().loginFooterImageUrl;

        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginFooterImageUrl;
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        } else {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }
        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));
            bCheckin.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            button_signup.setBackgroundColor(Color.parseColor(btncolor));

        }
    }

    public boolean checkValidation() {
        if (!(FBUtils.isValidString(et.getText().toString()) || FBUtils.isValidPhoneNumber(et.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty Phone Number");
            return false;
        }

        return true;
    }
}
