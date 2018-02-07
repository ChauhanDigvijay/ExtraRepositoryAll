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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static com.fishbowl.LoyaltyTabletApp.R.id.password;


public class FragmentLoginKey extends Fragment implements View.OnClickListener {
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bStar, bBack, bCheckin;
    EditText et, pass;
    TextView txt_signup;
    ProgressBarHandler progressBarHandler;
    String fbm;
    Timer myTimer;
    RelativeLayout button_parent;
    LinearLayout parentKey, parentEtText;
    String date;
    private NetworkImageView background;
    private ImageLoader mImageLoader;
    private NetworkImageView footer_imageurl;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_key_new, container, false);

        parentKey = (LinearLayout) v.findViewById(R.id.parent_key_layout);
        parentEtText = (LinearLayout) v.findViewById(R.id.parent_edittext);
        button_parent = (RelativeLayout) v.findViewById(R.id.button_parent);
        bCheckin = (Button) v.findViewById(R.id.bt_checkIn);
        background = (NetworkImageView) v.findViewById(R.id.right_side_image_url);
        footer_imageurl = (NetworkImageView) v.findViewById(R.id.footer_image_url);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        progressBarHandler = new ProgressBarHandler(getActivity());
        et = (EditText) v.findViewById(R.id.target);
        pass = (EditText) v.findViewById(password);
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

        b1 = (Button) v.findViewById(R.id.bt1);
        b2 = (Button) v.findViewById(R.id.bt2);
        b3 = (Button) v.findViewById(R.id.bt3);
        b4 = (Button) v.findViewById(R.id.bt4);
        b5 = (Button) v.findViewById(R.id.bt5);
        b6 = (Button) v.findViewById(R.id.bt6);
        b7 = (Button) v.findViewById(R.id.bt7);
        b8 = (Button) v.findViewById(R.id.bt8);
        b9 = (Button) v.findViewById(R.id.bt9);
        b0 = (Button) v.findViewById(R.id.bt0);
        bStar = (Button) v.findViewById(R.id.bt_star);
        bBack = (Button) v.findViewById(R.id.bt_back);
        txt_signup = (TextView) v.findViewById(R.id.txt_signup);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        bStar.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bCheckin.setOnClickListener(this);
        txt_signup.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Editable str = et.getText();
        switch (v.getId()) {
            case R.id.bt1:
                str = str.append(b1.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt2:
                str = str.append(b2.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt3:
                str = str.append(b3.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt4:
                str = str.append(b4.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt5:
                str = str.append(b5.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt6:
                str = str.append(b6.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt7:
                str = str.append(b7.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt8:
                str = str.append(b8.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt9:
                str = str.append(b9.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt0:
                str = str.append(b0.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt_star:
                str = str.append(bStar.getText());
                et.setText(str);
                et.setSelection(et.length());
                break;
            case R.id.bt_back:
                int length = et.getText().length();
                if (length > 0) {
                    et.getText().delete(length - 1, length);
                    et.setSelection(et.length());
                }
                break;
            case R.id.bt_checkIn:
                if (checkValidation()) {
                    loginMember();
                }
                break;
            case R.id.txt_signup:
                gettestToken();
                break;
        }
    }


    public void gettestToken() {
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

                } else {
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
                    Intent ii = new Intent(getContext(), SignUpActivity.class);
                    startActivity(ii);
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

        Date d = Calendar.getInstance().getTime();

        String format = null;
        if (format == null)
            format = "yyyy-MM-dd'T'hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        try {
            fbm = "false";
            ;

            if (fbm != null) {
                if (Integer.valueOf(fbm) == 0 || fbm == "false") {
                    object.put("username", email);
                    object.put("password", "password");
                    object.put("eventDateTime", currentData);
                } else if (Integer.valueOf(fbm) == 1 || fbm == "true") {
                    String password = pass.getText().toString();
                    object.put("username", email);
                    object.put("password", password);
                    object.put("eventDateTime", currentData);
                }
            }
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
        final String url1 = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralFooterImageUrl");
        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralFooterImageUrl");
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        } else {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }

        String buttoncolor = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));
            txt_signup.setTextColor(Color.parseColor(btncolor));
        }
    }

    public void passwordField() {
        fbm = "false";

        if (fbm != null) {
            if (Integer.valueOf(fbm) == 0 || fbm == "false") {
                parentKey.setVisibility(View.VISIBLE);
                et.setFocusable(false);
                parentEtText.setGravity(Gravity.BOTTOM);
                pass.setVisibility(View.GONE);
            } else if (Integer.valueOf(fbm) == 1 || fbm == "true") {
                pass.setVisibility(View.VISIBLE);
                parentKey.setVisibility(View.GONE);
                et.setFocusable(true);
                pass.setFocusable(true);
                parentEtText.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) parentEtText.getLayoutParams();
                params.weight = 75;
                parentEtText.setLayoutParams(params);
            }
        }

        final String url1 = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralFooterImageUrl");

        if (StringUtilities.isValidString(url1)) {
            final String url2 = "http://" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralFooterImageUrl");
            mImageLoader.get(url2, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));
            footer_imageurl.setImageUrl(url2, mImageLoader);
        } else {
            footer_imageurl.setBackgroundResource(R.drawable.bottom_bar);
        }
        String buttoncolor = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            bCheckin.setBackgroundColor(Color.parseColor(btncolor));
            bCheckin.setBackgroundColor(Color.parseColor("#" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor")));
            txt_signup.setTextColor(Color.parseColor(btncolor));


        }
    }

    public boolean checkValidation() {
        if (!(FBUtils.isValidString(et.getText().toString()) || FBUtils.isValidPhoneNumber(et.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty Mobile");
            return false;
        }

        return true;
    }
}
