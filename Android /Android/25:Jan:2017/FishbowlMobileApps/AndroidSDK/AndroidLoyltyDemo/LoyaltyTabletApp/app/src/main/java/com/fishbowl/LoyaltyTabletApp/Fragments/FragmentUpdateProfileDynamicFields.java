//package com.fishbowl.LoyaltyTabletApp.Fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ProfileMenu.UpdateProfile;
//import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.State.StateActivity;
//import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Store.SearchStoreActivity;
//import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.States;
//import com.fishbowl.LoyaltyTabletApp.R;
//import com.fishbowl.LoyaltyTabletApp.Utils.FBUtility;
//import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
//import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
//import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
//import com.fishbowl.loyaltymodule.Models.Field;
//import com.fishbowl.loyaltymodule.Models.Member;
//import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
//import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static android.app.Activity.RESULT_OK;
//import static com.fishbowl.LoyaltyTabletApp.Fragments.FragmentSignUpDynamic.PICK_STORE_REQUEST;
//import static com.fishbowl.LoyaltyTabletApp.Fragments.FragmentSignUpDynamic.PICK_State_REQUEST;
//
///**
// * Created by schaudhary_ic on 24-Nov-16.
// */
//
//public class FragmentUpdateProfileDynamicFields extends Fragment {
//    public static String statecode;
//    public EditText FirstName;
//    public EditText LastName;
//    public EditText Address;
//    public EditText ZipCode;
//    public EditText FavoriteStore;
//    public EditText City;
//    public RadioGroup Gender;
//    public EditText EmailAddress;
//    public CheckBox SMSOptIn;
//    public CheckBox EmailOptIn;
//    public EditText DOB;
//    public EditText State;
//    public EditText PhoneNumber;
//    ProgressBarHandler progressBarHandler;
//    LinearLayout scrollLinearLayout;
//    Member member = null;
//    Button bt_save, bt_edit;
//    com.fishbowl.loyaltymodule.Models.FBStoresItem selectedStore;
//    LinearLayout logout_layout;
//    Button registration_button;
//    TextView registxt_name;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_update_profile_new, container, false);
//        progressBarHandler = new ProgressBarHandler(getContext());
//        registration_button = (Button) v.findViewById(R.id.registration_start_okbutton);
//        registration_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateMember();
//            }
//        });
//        registxt_name = (TextView) v.findViewById(R.id.registxt_name);
//        registxt_name.setText("My Profile");
//        scrollLinearLayout = (LinearLayout) v.findViewById(R.id.scrollLinearLayout);
//        member = FB_LY_UserService.sharedInstance().member;
//        bt_edit = (Button) v.findViewById(R.id.bt_edit);
//        bt_save = (Button) v.findViewById(R.id.bt_save);
//        configureField();
//        return v;
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//        bt_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (StringUtilities.isValidString(member.firstName)) {
//                    FirstName.setFocusableInTouchMode(true);
//                    FirstName.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    FirstName.requestFocus();
//                }
//                if (StringUtilities.isValidString(member.lastName)) {
//
//                    LastName.setFocusableInTouchMode(true);
//                    LastName.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    LastName.requestFocus();
//                }
//                if (StringUtilities.isValidString(member.addressCity)) {
//
//                    City.setFocusableInTouchMode(true);
//                    City.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    City.requestFocus();
//                }
//                if (StringUtilities.isValidString(member.addressZip)) {
//
//                    ZipCode.setFocusableInTouchMode(true);
//                    ZipCode.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    ZipCode.requestFocus();
//                    ZipCode.setEnabled(true);
//
//                }
//                if (StringUtilities.isValidString(member.addressState)) {
//
//                    State.setFocusableInTouchMode(false);
//
//                    State.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    State.requestFocus();
//                    State.setEnabled(true);
//                    State.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(getContext(), StateActivity.class);
//                            startActivityForResult(i, PICK_State_REQUEST);
//                        }
//                    });
//
//                }
//
//                if (StringUtilities.isValidString(member.customerGender)) {
//
//                    int radioButtonID = Gender.getCheckedRadioButtonId();
//                    RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
//                    for (int i = 0; i < Gender.getChildCount(); i++) {
//                        Gender.getChildAt(i).setEnabled(true);
//
//                    }
//
//                }
//                if (StringUtilities.isValidString(member.homeStoreID)) {
//
//
//                    FavoriteStore.setFocusableInTouchMode(true);
//                    FavoriteStore.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
//                    FavoriteStore.requestFocus();
//                    FavoriteStore.setEnabled(true);
//                    FavoriteStore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(getContext(), SearchStoreActivity.class);
//                            startActivityForResult(i, PICK_STORE_REQUEST);
//                        }
//                    });
//
//                }
//                if (StringUtilities.isValidString(member.emailOpted)) {
//
//                    EmailOptIn.setEnabled(true);
//                }
//                bt_edit.setVisibility(View.GONE);
//                bt_save.setVisibility(View.VISIBLE);
//                registxt_name.setText("Update Profile");
//
//            }
//        });
//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (checkValidation()) {
//                    updateMember();
//                    if (StringUtilities.isValidString(member.firstName)) {
//                        FirstName.setFocusable(false);
//                        FirstName.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//                    }
//                    if (StringUtilities.isValidString(member.lastName)) {
//                        LastName.setFocusable(false);
//                        LastName.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//                    }
//                    if (StringUtilities.isValidString(member.addressCity)) {
//                        City.setFocusable(false);
//                        City.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//                    }
//                    if (StringUtilities.isValidString(member.addressZip)) {
//                        ZipCode.setFocusableInTouchMode(false);
//                        ZipCode.setEnabled(false);
//                        ZipCode.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//
//
//                    }
//                    if (StringUtilities.isValidString(member.addressState)) {
//                        State.setFocusableInTouchMode(false);
//                        State.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//                        State.requestFocus();
//                        State.setEnabled(false);
//                    }
//                    if (StringUtilities.isValidString(member.homeStoreID)) {
//
//                        FavoriteStore.setFocusableInTouchMode(false);
//                        FavoriteStore.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
//                        FavoriteStore.setFocusable(false);
//                        FavoriteStore.setEnabled(false);
//
//
//                    }
////
//                    if (StringUtilities.isValidString(member.customerGender)) {
//                        for (int i = 0; i < Gender.getChildCount(); i++) {
//                            Gender.getChildAt(i).setEnabled(false);
//                        }
//                    }
//                    if (StringUtilities.isValidString(member.emailOpted)) {
//                        EmailOptIn.setEnabled(false);
//                    }
////
//                    bt_save.setVisibility(View.GONE);
//                    bt_edit.setVisibility(View.VISIBLE);
//                    registxt_name.setText("My Profile");
//                }
//            }
//        });
//    }
//
//    public void configureField() {
//
//
//        if (StringUtilities.isValidString(member.firstName))
//            creatTextField("firstName");
//
//
//        if (StringUtilities.isValidString(member.lastName))
//            creatTextField("lastName");
//
//
//        if (StringUtilities.isValidString(member.addressZip))
//            creatTextField("addressZip");
//
//        if (StringUtilities.isValidString(member.addressCity))
//            creatTextField("addressCity");
//        /*if (member.addressLine1 != null)
//            creatTextField(member.addressLine1);
//*/
//        if (StringUtilities.isValidString(member.emailID))
//            creatTextField("emailID");
//
//        if (StringUtilities.isValidString(member.dateOfBirth))
//            creatTextField("dateOfBirth");
//
//        if (StringUtilities.isValidString(member.addressState)) {
//            //  String statename= allmapstorecode.get(member.addressState);
//            creatTextField("addressState");
//        }
//
//
//        if (StringUtilities.isValidString(member.homeStoreID))
//            creatTextField("homeStoreID");
//
//        if (StringUtilities.isValidString(member.customerGender))
//            creatRadioGroup(member.customerGender);
//        if (StringUtilities.isValidString(member.cellPhone)) {
//            final EditText editText = creatTextField("cellPhone");
//
//
//            editText.addTextChangedListener(new TextWatcher() {
//                private static final char space = ' ';
//
//                public void afterTextChanged(Editable s) {
//                    PhoneNumber.setSelection(PhoneNumber.getText().length());
//                }
//
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    FBUtils.setUsDashPhone(s, before, PhoneNumber);
//                }
//            });
//
//        }
////        if (StringUtilities.isValidString(member.smsOpted ))
////            creatRadioButton("SMSOptIn");
//
//        if (StringUtilities.isValidString(member.emailOpted))
//            creatRadioButton("Opt-in to receive VIP email offers");
//
//
//    }
//
//
//    public void creatRadioGroup(String name) {
//
//        LinearLayout view;
//        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//        view = (LinearLayout) mInflater.inflate(R.layout.radio_buttons, null, false);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
//        view.setLayoutParams(layoutParams);
//
//        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
//
//        RadioButton radioMale = (RadioButton) view.findViewById(R.id.maleRadio);
//        RadioButton radiofemale = (RadioButton) view.findViewById(R.id.femaleRadio);
//
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.d("", "");
//            }
//        });
//
//
//        TextView editText = (TextView) view.findViewById(R.id.textView);
//        editText.setText(name);
//
//        if (name.equals("M")) {
//            Gender = radioGroup;
//            editText.setText("Gender");
//            radioGroup.check(radioMale.getId());
//
//        } else if (name.equals("F")) {
//            Gender = radioGroup;
//            editText.setText("Gender");
//            radioGroup.check(radiofemale.getId());
//
//        }
//        for (int i = 0; i < Gender.getChildCount(); i++) {
//            Gender.getChildAt(i).setEnabled(false);
//        }
//
//        scrollLinearLayout.addView(view);
//    }
//
//    public void creatRadioButton(String name) {
//
//        LinearLayout view;
//        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//        view = (LinearLayout) mInflater.inflate(R.layout.checkbox_buttons, null, false);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
//        view.setLayoutParams(layoutParams);
//
//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
//
//
//        if (name.equals("Opt-in to receive VIP email offers")) {
//            EmailOptIn = checkBox;
//            TextView editText = (TextView) view.findViewById(R.id.textView);
//            editText.setText("Opt-in to receive VIP email offers");
//            EmailOptIn.setChecked(Boolean.valueOf(member.emailOpted));
//            EmailOptIn.setEnabled(false);
//        }
//
//
//        scrollLinearLayout.addView(view);
//    }
//
//    public EditText creatTextField(String name) {
//
//        LinearLayout view;
//        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//        view = (LinearLayout) mInflater.inflate(R.layout.edittext_field, null, false);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
//        view.setLayoutParams(layoutParams);
//
//
//        EditText editText = (EditText) view.findViewById(R.id.editText);
//        TextView textview = (TextView) view.findViewById(R.id.field_text);
//
//
//        if (name.equals("firstName")) {
//
//
//            textview.setText("FirstName");
//            editText.setTag(member.firstName);
//            editText.setText(member.firstName);
//            FirstName = editText;
//            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
//            FirstName.setMaxLines(1);
//            FirstName.setFocusable(false);
//
//            //imageView.setBackgroundResource(R.drawable.logo);
//        }
//        if (name.equals("lastName")) {
//            textview.setText("LastName");
//            editText.setTag(member.lastName);
//            editText.setText(member.lastName);
//            LastName = editText;
//            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
//            LastName.setMaxLines(1);
//            LastName.setFocusable(false);
//
//        }
//
//        if (name.equals("addressCity")) {
//            textview.setText("City");
//            editText.setTag(member.addressCity);
//            editText.setText(member.addressCity);
//            City = editText;
//            City.setInputType(InputType.TYPE_CLASS_TEXT);
//            City.setMaxLines(1);
//            City.setFocusable(false);
//
//        }
//        if (name.equals("emailID")) {
//            textview.setText("EmailAddress");
//            editText.setTag(member.emailID);
//            editText.setText(member.emailID);
//            EmailAddress = editText;
//            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            EmailAddress.setMaxLines(1);
//            EmailAddress.setFocusable(false);
//        }
//
//
//        if (name.equals("cellPhone")) {
//
//            textview.setText("PhoneNumber");
//            editText.setTag(member.cellPhone);
//            editText.setText(member.cellPhone);
//            PhoneNumber = editText;
//            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
//            PhoneNumber.setMaxLines(1);
//            PhoneNumber.setEnabled(false);
//
//
//        }
//        if (name.equals("addressZip")) {
//            textview.setText("ZipCode");
//            editText.setTag(member.addressZip);
//            editText.setText(member.addressZip);
//            ZipCode = editText;
//            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
//            ZipCode.setMaxLines(1);
//            ZipCode.setFocusable(false);
//            ZipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//
//        }
//
//
//        if (name.equals("homeStoreID")) {
//            textview.setText("FavoriteStore");
//            editText.setTag(member.homeStoreID);
//            editText.setText(member.homeStoreID);
//            FavoriteStore = editText;
//            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
//            FavoriteStore.setMaxLines(1);
//            FavoriteStore.setFocusable(false);
//
//            selectedStore = FB_LY_UserService.sharedInstance().mapIdToStore.get(Integer.valueOf(member.homeStoreID));
//            FavoriteStore.setText(selectedStore.getStoreName());
//
//        }
//
//
//        if (name.equals("dateOfBirth")) {
//            if (member.dateOfBirth != null && FBUtils.isValidString(member.dateOfBirth)) {
//                textview.setText("BirthDay");
//
//                try {
//                    String currentDate = member.dateOfBirth;
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date tempDate = simpleDateFormat.parse(currentDate);
//                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                    //System.out.println("Output date is = "+outputDateFormat.format(tempDate));
//                    String output = outputDateFormat.format(tempDate);
//                    if (output != null)
//                        editText.setText(output);
//                    else
//                        editText.setText(currentDate);
//                } catch (ParseException ex) {
//                    System.out.println("Parse Exception");
//                }
//                editText.setTag(member.dateOfBirth);
//
//                DOB = editText;
//                DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
//                DOB.setFocusable(false);
//                DOB.setMaxLines(1);
//                DOB.setFocusable(false);
//            }
//
//        }
//
//        if (name.equals("addressState")) {
//
//            textview.setText("State");
//            editText.setTag(member.addressState);
//            editText.setText(member.addressState);
//            State = editText;
//            State.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            State.setMaxLines(1);
//            State.setFocusable(false);
//            State.setFocusable(false);
//
//        }
//
//        scrollLinearLayout.addView(view);
//
//        return editText;
//    }
//
//    public void updateMember() {
//
//        enableScreen(false);
//        JSONObject object = new JSONObject();
//        try {
//            Field activeFields = FB_LY_MobileSettingService.sharedInstance().activeFields;
//
//            if (StringUtilities.isValidString(member.firstName))
//                object.put("firstName", FirstName.getText().toString());
//
//            if (StringUtilities.isValidString(member.lastName))
//                object.put("lastName", LastName.getText().toString());
//
//            if (StringUtilities.isValidString(member.emailID))
//                object.put("email", EmailAddress.getText().toString());
//
//            if (StringUtilities.isValidString(member.emailOpted))
//                object.put("emailOptIn", EmailOptIn.isChecked());
//
//            if (StringUtilities.isValidString(member.cellPhone))
//                object.put("phone", PhoneNumber.getText().toString());
//
//
//            if (StringUtilities.isValidString(member.addressState))
//                object.put("addressState", statecode);
//
//            if (StringUtilities.isValidString(member.addressCity))
//                object.put("addressCity", City.getText().toString());
//
//            if (StringUtilities.isValidString(member.addressZip))
//                object.put("addressZipCode", ZipCode.getText().toString());
//
//            if (StringUtilities.isValidString(member.dateOfBirth))
//                object.put("dob", DOB.getText().toString());
//
//            if (StringUtilities.isValidString(member.customerGender)) {
//                int radioButtonID = Gender.getCheckedRadioButtonId();
//                RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
//                object.put("gender", radioButton.getText());
//
//
//            }
//            if (StringUtilities.isValidString(member.homeStoreID))
//                object.put("favoriteStore", selectedStore.getStoreNumber());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        FB_LY_UserService.sharedInstance().memberUpdate(object, new FB_LY_UserService.FBMemberUpdateCallback() {
//            @Override
//            public void onMemberUpdateCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    FBUtils.showAlert(getContext(), "Member Update Successfully");
//                    //progressBarHandler.hide();
//                    enableScreen(true);
//                    getMember();
//                } else {
//                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
//                    //progressBarHandler.hide();
//                    enableScreen(true);
//                }
//            }
//        });
//    }
//
//    public boolean checkValidation() {
//        //  fname, lname, email, phone, address,password;
//        if (StringUtilities.isValidString(member.firstName)) {
//            if (!FBUtils.isValidString(FirstName.getText().toString())) {
//                FBUtils.showAlert(getActivity(), "Empty First Name");
//                return false;
//            }
//        }
//        if (StringUtilities.isValidString(member.lastName)) {
//            if (!FBUtils.isValidString(LastName.getText().toString())) {
//                FBUtils.showAlert(getActivity(), "Empty Last Name");
//                return false;
//            }
//        }
//
//
//        return true;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//
//        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
//        if (buttoncolor != null) {
//            String btncolor = "#" + buttoncolor;
//            bt_edit.setBackgroundColor(Color.parseColor(btncolor));
//            bt_save.setBackgroundColor(Color.parseColor(btncolor));
//            //  registxt_name.setTextColor(Color.parseColor(btncolor));
//
//        }
//    }
//
//    public void getMember() {
//        progressBarHandler.show();
//        JSONObject object = new JSONObject();
//        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback() {
//            public void onGetMemberCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    progressBarHandler.dismiss();
//                    RelativeLayout toolbarTextView = (RelativeLayout) ((UpdateProfile) getActivity()).findViewById(R.id.tool_bar);
//                    if (toolbarTextView != null) {
//                        TextView title = (TextView) toolbarTextView.findViewById(R.id.title_textb);
//                        String firstName = FB_LY_UserService.sharedInstance().member.firstName;
//                        title.setText(firstName);
//
//                    }
//                } else {
//                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
//                    progressBarHandler.dismiss();
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode == PICK_STORE_REQUEST) {
//            if (resultCode == RESULT_OK) {
//
//                selectedStore = (com.fishbowl.loyaltymodule.Models.FBStoresItem) data.getSerializableExtra("CLPSTORE");
//                FavoriteStore.setText(selectedStore.getStoreName());
//
//            }
//
//        }
//        if (requestCode == PICK_State_REQUEST) {
//            if (resultCode == RESULT_OK) {
//
//                String statename = (String) data.getSerializableExtra("State");
//                statecode = (String) data.getSerializableExtra("StateCode");
//                State.setText(statename);
//
//            }
//        }
//
//    }
//
//    public void enableScreen(boolean isEnabled) {
//        RelativeLayout screenDisableView = (RelativeLayout) getActivity().findViewById(R.id.screenDisableView);
//        if (screenDisableView != null) {
//            if (!isEnabled) {
//                screenDisableView.setVisibility(View.VISIBLE);
//            } else {
//                screenDisableView.setVisibility(View.GONE);
//            }
//        }
//    }
//
//
//    public void getState() {
//
//
//        if (States.allmapstorecode.size() > 0) {
//            progressBarHandler.show();
//            JSONObject object = new JSONObject();
//            FB_LY_UserService.sharedInstance().getState(object, new FB_LY_UserService.FBStateCallback() {
//                @Override
//                public void onStateCallback(JSONObject response, Exception error) {
//
//                    try {
//                        if (error == null && response != null) {
//                            if (!response.has("stateList"))
//                                return;
//
//                            JSONArray getArrayStates = response.getJSONArray("stateList");
//                            if (getArrayStates != null) {
//
//                                for (int i = 0; i < getArrayStates.length(); i++) {
//                                    JSONObject myStoresObj = getArrayStates.getJSONObject(i);
//                                    States statesListObj = new States(myStoresObj);
//
//
//                                }
//                            }
//                            progressBarHandler.dismiss();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//
//            });
//        }
//
//    }
//
//}
