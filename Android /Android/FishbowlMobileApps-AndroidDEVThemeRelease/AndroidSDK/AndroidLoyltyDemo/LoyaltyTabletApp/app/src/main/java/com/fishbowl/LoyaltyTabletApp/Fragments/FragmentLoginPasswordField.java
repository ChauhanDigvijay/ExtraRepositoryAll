package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignUp.SignUpActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryPointCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardPointSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardPointService;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtility;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.FBPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class FragmentLoginPasswordField extends Fragment implements View.OnClickListener {
    Button bCheckin;
    EditText et, pass;
    TextView txt_signup;


    ProgressBarHandler progressBarHandler;
    String fbm;
    Timer myTimer;
    RelativeLayout button_parent;
    String date;
    private NetworkImageView background;
    private ImageLoader mImageLoader;
    private NetworkImageView footer_imageurl;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_password_field, container, false);


        bCheckin = (Button) v.findViewById(R.id.bt_checkIn);
        background = (NetworkImageView) v.findViewById(R.id.right_side_image_url);
        footer_imageurl = (NetworkImageView) v.findViewById(R.id.footer_image_url);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        progressBarHandler = new ProgressBarHandler(getActivity());
        et = (EditText) v.findViewById(R.id.target);
        pass = (EditText) v.findViewById(R.id.password);
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
        txt_signup = (TextView) v.findViewById(R.id.txt_signup);


        txt_signup.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.bt_checkIn) {
            if (checkValidation()) {
                loginMember();

            }
        }
        if (v.getId() == R.id.txt_signup) {
            gettestToken();

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
                    Intent ii = new Intent(getContext(), SignUpActivity.class);
                    startActivity(ii);
                    progressBarHandler.dismiss();

                }
                else
                {
                        progressBarHandler.dismiss();
                }
            }
        });
    }


    public void loginMemberfortest() {
        JSONObject object = new JSONObject();
        progressBarHandler.show();
        try {
            object.put("username", "testertesting@gmail.com");
            object.put("password", "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FB_LY_UserService.sharedInstance().loginMemberfortest(object, new FB_LY_UserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(getActivity()).setAccessTokenforapp(secratekey);
                        FB_LY_UserService.sharedInstance().access_token = response.getString("accessToken");

                        Intent ii = new Intent(getContext(), SignUpActivity.class);
                        startActivity(ii);
                        progressBarHandler.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    FBUtils.tryHandleTokenExpiry(getActivity(), error);
                    progressBarHandler.dismiss();
                }
            }

        });
    }


    public void signup() {
        Intent ii = new Intent(getContext(), SignUpActivity.class);
        startActivity(ii);
    }

    public void loginMember() {
        JSONObject object = new JSONObject();
        progressBarHandler.show();
        String email = et.getText().toString();
        String password = pass.getText().toString();

        Date d = Calendar.getInstance().getTime();

        String format = null;
        if (format == null)
            format = "yyyy-MM-dd'T'hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        try {
            object.put("username", email);
            object.put("password", password);
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
                    progressBarHandler.hide();
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
        final String url1 = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");
        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        }
        else if(FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor")!=null)
        {
            String  headercolor=FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
            footer_imageurl.setBackgroundColor(Color.parseColor(headercolor));
        }
        else {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }

        //signup button  color
        //String buttoncolor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        String generalButtonBGColorNormal =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonBGColorNormal");
        if (generalButtonBGColorNormal != null) {
            String btncolor =  generalButtonBGColorNormal;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));


        }

        //signup text  color
        String generalButtonStateFontColor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonStateFontColor");
        if (generalButtonBGColorNormal != null) {
            txt_signup.setTextColor(Color.parseColor(generalButtonStateFontColor));
        }
    }

    public void passwordField() {

        final String url1 = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");

        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        }
        else if(FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor")!=null)
        {
           String  headercolor=FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
            footer_imageurl.setBackgroundColor(Color.parseColor(headercolor));
        }
        else
        {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }

        //signup button  color
        //String buttoncolor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        String generalButtonBGColorNormal =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonBGColorNormal");
        if (generalButtonBGColorNormal != null) {
            String btncolor =  generalButtonBGColorNormal;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));


        }

        //signup text  color
        String generalButtonStateFontColor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonStateFontColor");
        if (generalButtonBGColorNormal != null) {
            txt_signup.setTextColor(Color.parseColor(generalButtonStateFontColor));
            bCheckin.setTextColor(Color.parseColor(generalButtonStateFontColor));
        }


    }

    public boolean checkValidation() {
        if (!(FBUtils.isValidString(et.getText().toString()) || FBUtils.isValidPhoneNumber(et.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty Phone number");
            return false;
        }


        if (!FBUtils.isValidString(pass.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Password");
            return false;
        }

        return true;
    }


}
