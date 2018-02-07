package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Settings.PrivacyLink;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Settings.TermsConditionActivity;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
import com.fishbowl.loyaltymodule.Utils.FBPreferences;
import com.fishbowl.loyaltymodule.Utils.FBUtility;

import org.json.JSONObject;

public class FragmentSignup extends Fragment {
    EditText fname, lname, email, phone, address, password;
    Button registration_button;
    ImageView imageView2;
    ProgressBarHandler progressBarHandler;
    TextView terms_text, privacy_text;
    String fbm;
    CheckBox checkbox_terms;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_new, container, false);
        progressBarHandler = new ProgressBarHandler(getActivity());
        checkbox_terms = (CheckBox) v.findViewById(R.id.checkbox_terms);
        checkbox_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked() == true) {
                    registration_button.setEnabled(true);
                } else {
                    registration_button.setEnabled(false);
                }
            }
        });
        terms_text = (TextView) v.findViewById(R.id.terms_text);
        terms_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getContext(), TermsConditionActivity.class);
                startActivity(ii);
            }
        });
        privacy_text = (TextView) v.findViewById(R.id.privacy_text);
        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getContext(), PrivacyLink.class);
                startActivity(ii);
            }
        });
        fname = ((EditText) v.findViewById(R.id.txt_firstname));
        lname = ((EditText) v.findViewById(R.id.txt_lastname));
        email = ((EditText) v.findViewById(R.id.txt_email));
        phone = ((EditText) v.findViewById(R.id.txt_phoneno));
        address = ((EditText) v.findViewById(R.id.txt_address));
        password = ((EditText) v.findViewById(R.id.password));
        registration_button = (Button) v.findViewById(R.id.registration_start_okbutton);
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMember();
            }
        });
        imageView2 = (ImageView) v.findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {
                phone.setSelection(phone.getText().length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    char c = s.charAt(0);

                    if (c >= '0' && c <= '9') {
                        FBUtils.setUsDashPhone(s, before, phone);
                    }
                }
            }

        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
        }
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            registration_button.setBackgroundColor(Color.parseColor(btncolor));
            registration_button.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));


            registration_button.setBackgroundResource(R.drawable.normal);
            GradientDrawable gd = (GradientDrawable) registration_button.getBackground().getCurrent();
            gd.setColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            gd.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
            gd.setStroke(1, Color.parseColor("#444444"), 0, 0);

        }
    }

    public void createMember() {
        if (checkValidation()) {
            progressBarHandler.show();
            JSONObject object = new JSONObject();
            try {
                String firstname = fname.getText().toString();
                String lastname = lname.getText().toString();
                String emailid = email.getText().toString();
                String phoneno = phone.getText().toString();
                String fulladdress = address.getText().toString();
                String pass = password.getText().toString();
                object.put("firstName", firstname);
                object.put("lastName", lastname);
                object.put("email", emailid);
                object.put("phone", phoneno);
                object.put("emailOptIn", "true");
                object.put("smsOptIn", "true");
                object.put("addressStreet", fulladdress);
                object.put("addressState", "addressState");
                object.put("addressCity", "addressCity");
                object.put("password", pass);
                object.put("gender", "Male");
                object.put("birthDate", "01/01/1992");
                object.put("favoriteStore", "favoriteStore");
                object.put("sendWelcomeEmail", "sendWelcomeEmail");
                object.put("deviceId", FBUtility.getAndroidDeviceID(getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }


            FB_LY_UserService.sharedInstance().createMember(object, new FB_LY_UserService.FBCreateMemberCallback() {
                @Override
                public void onCreateMemberCallback(JSONObject response, Exception error) {
                    if (response != null) {
                        try {
                            loginMember();
                            progressBarHandler.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBarHandler.dismiss();
                        }

                    } else {
                        FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                        progressBarHandler.dismiss();
                    }
                }
            });
        }
    }


    public void loginMember() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        try {
            String email = phone.getText().toString();
            String pass = password.getText().toString();
            fbm = FB_LY_MobileSettingService.sharedInstance().passwordEnable;
            ;

            if (fbm != null) {
                if (Integer.valueOf(fbm) == 0 || fbm == "false") {
                    object.put("username", email);
                } else if (Integer.valueOf(fbm) == 1 || fbm == "true") {
                    object.put("username", email);
                    object.put("password", pass);
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
                    progressBarHandler.dismiss();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }


    public void getMember() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();

        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    Intent ii = new Intent(getContext(), HomeActivity.class);
                    startActivity(ii);
                    progressBarHandler.dismiss();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }

        });
    }

    public boolean checkValidation() {
        //  fname, lname, email, phone, address,password;
        if (!FBUtils.isValidString(fname.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty First Name");
            return false;
        }
        if (!FBUtils.isValidString(lname.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Last Name");
            return false;
        }
        if (!FBUtils.isValidString(email.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Email");
            return false;
        }
        if (!(FBUtils.isValidString(phone.getText().toString()) || FBUtils.isValidPhoneNumber(phone.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty Phone Number");
            return false;
        }

        if (!FBUtils.isValidString(address.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Address");
            return false;
        }
        if (!FBUtils.isValidString(password.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Password");
            return false;
        }

        return true;
    }

}
