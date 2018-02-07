//package com.fishbowl.LoyaltyTabletApp.Fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.ChangePasswordActivity;
//import com.fishbowl.LoyaltyTabletApp.R;
//import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
//import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
//import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
//import com.fishbowl.loyaltymodule.Models.Member;
//
//import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
//
//import org.json.JSONObject;
//
//public class FragmentUpdateProfile extends Fragment {
//    EditText fname, lname, email, phone, address;
//    Button registration_button;
//    ProgressBarHandler progressBarHandler;
//    ImageView imageView2;
//    Member member = null;
//    String memberFirstName, memberLastName, memberEmailId, memberPhone, memberAddress;
//    TextView passwordchange;
//    private ImageLoader mImageLoader;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);
//        progressBarHandler = new ProgressBarHandler(getActivity());
//        member = FB_LY_UserService.sharedInstance().member;
//
//        if (member.firstName != null) {
//            memberFirstName = member.firstName;
//        }
//
//        if (member.lastName != null) {
//            memberLastName = member.lastName;
//        }
//
//        if (member.emailID != null) {
//            memberEmailId = member.emailID;
//        }
//
//        if (member.cellPhone != null) {
//            memberPhone = member.cellPhone;
//        }
//
//        if (member.addressCity != null) {
//            memberAddress = member.addressCity;
//        }
//
//        fname = ((EditText) v.findViewById(R.id.txt_firstname));
//        lname = ((EditText) v.findViewById(R.id.txt_lastname));
//        email = ((EditText) v.findViewById(R.id.txt_email));
//        email.setFocusable(false);
//        phone = ((EditText) v.findViewById(R.id.txt_phoneno));
//        phone.setFocusable(false);
//        address = ((EditText) v.findViewById(R.id.txt_address));
//        passwordchange = (TextView) v.findViewById(R.id.text_change_pass);
//        passwordchange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
//                startActivity(i);
//            }
//        });
//        fname.setText(memberFirstName);
//        lname.setText(memberLastName);
//        email.setText(memberEmailId);
//        phone.setText(memberPhone);
//        address.setText(memberAddress);
//        registration_button = (Button) v.findViewById(R.id.registration_start_okbutton);
//        imageView2 = (ImageView) v.findViewById(R.id.imageView2);
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*Intent i = new Intent(getActivity(),HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);*/
//                getActivity().finish();
//            }
//        });
//        registration_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateMember();
//            }
//        });
//
//
//        phone.addTextChangedListener(new TextWatcher() {
//            private static final char space = ' ';
//
//            public void afterTextChanged(Editable s) {
//                phone.setSelection(phone.getText().length());
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    char c = s.charAt(0);
//                    if (c >= '0' && c <= '9') {
//                        FBUtils.setUsDashPhone(s, before, phone);
//                    }
//                }
//            }
//        });
//        return v;
//    }
//
//    public void updateMember() {
//        //  checkValidation();
//        progressBarHandler.show();
//        JSONObject object = new JSONObject();
//        try {
//            String firstname = fname.getText().toString();
//            String lastname = lname.getText().toString();
//            String emailid = email.getText().toString();
//            String phoneno = phone.getText().toString();
//            String fulladdress = address.getText().toString();
//            object.put("firstName", firstname);
//            object.put("lastName", lastname);
//            object.put("email", emailid);
//            object.put("birthDate", "01/01/1992");
//            object.put("addressCity", fulladdress);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        FB_LY_UserService.sharedInstance().memberUpdate(object, new FB_LY_UserService.FBMemberUpdateCallback() {
//            @Override
//            public void onMemberUpdateCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    FBUtils.showAlert(getContext(), "Member Update Successfully");
//                    progressBarHandler.dismiss();
//                    getMember();
//                } else {
//                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
//                    progressBarHandler.dismiss();
//                }
//            }
//        });
//    }
//
//    public void getMember() {
//        progressBarHandler.show();
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback() {
//            public void onGetMemberCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    progressBarHandler.dismiss();
//
//
//                } else {
//                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
//                    progressBarHandler.dismiss();
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
//        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
//
//
//        if (buttoncolor != null) {
//            String btncolor = "#" + buttoncolor;
//            registration_button.setBackgroundColor(Color.parseColor(btncolor));
//            registration_button.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
//
//
//            registration_button.setBackgroundResource(R.drawable.normal);
//            GradientDrawable gd = (GradientDrawable) registration_button.getBackground().getCurrent();
//            gd.setColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
//            gd.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
//            gd.setStroke(1, Color.parseColor("#444444"), 0, 0);
//
//        }
//    }
//
//}