package com.hbh.honeybaked.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.facebook.internal.ServerProtocol;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBBonusRuleListCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBCreateMemberCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBDeviceUpdateCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBGetTokenCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBLoginMemberCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.firebase.auth.EmailAuthProvider;
import com.hbh.honeybaked.BuildConfig;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.activity.MainActivity;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.module.RuleModule;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SignUpFragment extends BaseFragment {
    EditText Signup_conf_phone;
    EditText Signup_conf_pwd;
    TextView Signup_dob;
    EditText Signup_fname;
    EditText Signup_lname;
    EditText Signup_mail;
    EditText Signup_phone_no;
    EditText Signup_pwd;
    String android_device_id;
    Button button_offer;
    Button button_points;
    RelativeLayout change_store_rl;
    TextView change_store_tv;
    private SimpleDateFormat dateFormatter;
    TextView disclaimer_tv;
    CheckBox email_option_chk_bx;
    private DatePickerDialog fromDatePickerDialog;
    boolean isFirstTime;
    CheckBox privacy_chk_bx;
    TextView privacy_policy_textView;
    ArrayList<RuleModule> rule_list_data = new ArrayList();
    LinearLayout select_signup_point_layout;
    TextView select_signup_rule_text;
    TextView select_store_add_tv;
    LinearLayout select_store_details_ll;
    LinearLayout select_store_details_rg;
    TextView select_store_nm_tv;
    TextView select_store_ph_tv;
    RelativeLayout select_store_rl;
    TextView select_store_tv;
    LinearLayout signup_lay;

    class C17791 extends PhoneNumberFormattingTextWatcher {
        private boolean backspacingFlag = false;
        private int cursorComplement;
        private boolean editedFlag = false;

        C17791() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.cursorComplement = s.length() - SignUpFragment.this.Signup_phone_no.getSelectionStart();
            if (count > after) {
                this.backspacingFlag = true;
            } else {
                this.backspacingFlag = false;
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_phone_no.setError(null);
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_phone_no.setError(null);
            String phone = s.toString().replaceAll("[^\\d]", "");
            if (this.editedFlag) {
                this.editedFlag = false;
            } else if (phone.length() >= 6 && !this.backspacingFlag) {
                this.editedFlag = true;
                SignUpFragment.this.Signup_phone_no.setText(phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6));
                SignUpFragment.this.Signup_phone_no.setSelection(SignUpFragment.this.Signup_phone_no.getText().length() - this.cursorComplement);
            } else if (phone.length() >= 3 && !this.backspacingFlag) {
                this.editedFlag = true;
                SignUpFragment.this.Signup_phone_no.setText(phone.substring(0, 3) + "-" + phone.substring(3));
                SignUpFragment.this.Signup_phone_no.setSelection(SignUpFragment.this.Signup_phone_no.getText().length() - this.cursorComplement);
            }
        }
    }

    class C17802 extends PhoneNumberFormattingTextWatcher {
        private boolean backspacingFlag1 = false;
        private int cursorComplement1;
        private boolean editedFlag1 = false;

        C17802() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.cursorComplement1 = s.length() - SignUpFragment.this.Signup_conf_phone.getSelectionStart();
            if (count > after) {
                this.backspacingFlag1 = true;
            } else {
                this.backspacingFlag1 = false;
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_conf_phone.setError(null);
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_conf_phone.setError(null);
            String phone1 = s.toString().replaceAll("[^\\d]", "");
            if (this.editedFlag1) {
                this.editedFlag1 = false;
            } else if (phone1.length() >= 6 && !this.backspacingFlag1) {
                this.editedFlag1 = true;
                SignUpFragment.this.Signup_conf_phone.setText(phone1.substring(0, 3) + "-" + phone1.substring(3, 6) + "-" + phone1.substring(6));
                SignUpFragment.this.Signup_conf_phone.setSelection(SignUpFragment.this.Signup_conf_phone.getText().length() - this.cursorComplement1);
            } else if (phone1.length() >= 3 && !this.backspacingFlag1) {
                this.editedFlag1 = true;
                SignUpFragment.this.Signup_conf_phone.setText(phone1.substring(0, 3) + "-" + phone1.substring(3));
                SignUpFragment.this.Signup_conf_phone.setSelection(SignUpFragment.this.Signup_conf_phone.getText().length() - this.cursorComplement1);
            }
        }
    }

    class C17813 implements TextWatcher {
        C17813() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_fname.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_fname.setError(null);
        }
    }

    class C17824 implements TextWatcher {
        C17824() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_lname.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_lname.setError(null);
        }
    }

    class C17835 implements TextWatcher {
        C17835() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_mail.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_mail.setError(null);
        }
    }

    class C17846 implements TextWatcher {
        C17846() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_pwd.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_pwd.setError(null);
        }
    }

    class C17857 implements TextWatcher {
        C17857() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_conf_pwd.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_conf_pwd.setError(null);
        }
    }

    class C17868 implements TextWatcher {
        C17868() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SignUpFragment.this.Signup_dob.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            SignUpFragment.this.Signup_dob.setError(null);
        }
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        this.hb_dbHelper.openDb();
        initView(v);
        this.dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        this.Signup_dob.setInputType(0);
        this.Signup_dob.requestFocus();
        setPrefValues();
       // setDateField();
        if (this.cd.isConnectingToInternet()) {
            Utility.hideSoftKeyboard(getActivity());
            getBonusRuleList();
        } else {
            this.select_store_details_rg.setVisibility(View.VISIBLE);
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
        this.android_device_id = Secure.getString(getActivity().getContentResolver(), "android_id");
        this.change_store_rl.setOnClickListener(this);
        this.select_store_rl.setOnClickListener(this);
        this.button_offer.setOnClickListener(this);
        this.button_points.setOnClickListener(this);
        this.Signup_phone_no.addTextChangedListener(new C17791());
        this.Signup_conf_phone.addTextChangedListener(new C17802());
        this.Signup_fname.addTextChangedListener(new C17813());
        this.Signup_lname.addTextChangedListener(new C17824());
        this.Signup_mail.addTextChangedListener(new C17835());
        this.Signup_pwd.addTextChangedListener(new C17846());
        this.Signup_conf_pwd.addTextChangedListener(new C17857());
        this.Signup_dob.addTextChangedListener(new C17868());
        return v;
    }

    private void storeEvent(boolean isChangeStore) {
        if (!this.cd.isConnectingToInternet()) {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        } else if (Utility.isEmptyString(this.Signup_fname.getText().toString())) {
            this.Signup_fname.requestFocus();
            this.Signup_fname.setError("First name is required!");
        } else if (Utility.isEmptyString(this.Signup_lname.getText().toString())) {
            this.Signup_lname.requestFocus();
            this.Signup_lname.setError("Last name is required!");
        } else if (Utility.isEmptyString(this.Signup_mail.getText().toString())) {
            this.Signup_mail.requestFocus();
            this.Signup_mail.setError("Email id is required!");
        } else if (!Utility.isValidEmail(this.Signup_mail.getText().toString())) {
            this.Signup_mail.requestFocus();
            this.Signup_mail.setError("Please enter valid email address");
        } else if (Utility.isEmptyString(this.Signup_pwd.getText().toString())) {
            this.Signup_pwd.requestFocus();
            this.Signup_pwd.setError("Password is required!");
        } else if (this.Signup_pwd.getText().toString().length() < 6) {
            this.Signup_pwd.requestFocus();
            this.Signup_pwd.setError("Please Enter minimum 6 characters");
        } else if (this.Signup_pwd.getText().toString().length() > 20) {
            this.Signup_pwd.requestFocus();
            this.Signup_pwd.setError("Please Enter maximum 20 characters");
        } else if (Utility.isEmptyString(this.Signup_conf_pwd.getText().toString())) {
            this.Signup_conf_pwd.requestFocus();
            this.Signup_conf_pwd.setError("Confirm Password is required!");
        } else if (Utility.isEmptyString(this.Signup_phone_no.getText().toString())) {
            this.Signup_phone_no.requestFocus();
            this.Signup_phone_no.setError("Phone number is required!");
        } else if (Utility.isEmptyString(this.Signup_conf_phone.getText().toString())) {
            this.Signup_conf_phone.requestFocus();
            this.Signup_conf_phone.setError("Confirm Phone number is required!");
        } else if (this.Signup_phone_no.getText().toString().replaceAll("-", "").length() != 10) {
            this.Signup_phone_no.requestFocus();
            this.Signup_phone_no.setError("Please enter valid Phone number");
        } else if (!this.Signup_pwd.getText().toString().equals(this.Signup_conf_pwd.getText().toString())) {
            this.Signup_conf_pwd.requestFocus();
            this.Signup_conf_pwd.setError("Passowrds do not match!");
        } else if (!this.Signup_phone_no.getText().toString().equals(this.Signup_conf_phone.getText().toString())) {
            this.Signup_conf_phone.requestFocus();
            this.Signup_conf_phone.setError("Phone numbers do not match!");
        } else if (isChangeStore) {
            saveEditFieldValues();
            this.fragmentActivityListener.performFragmentActivityAction(AppConstants.GETSTOREID_PAGE, Boolean.valueOf(false));
        } else {
            Utility.hideSoftKeyboard(getActivity());
            if (!this.privacy_chk_bx.isChecked()) {
                Utility.showToast(getActivity(), "Please confirm you have read the Terms Of Use & Privacy Policy by selecting the appropriate box.");
            } else if (this.cd.isConnectingToInternet()) {
                JSONObject create_member_object = new JSONObject();
                try {
                    create_member_object.put("firstName", this.Signup_fname.getText().toString());
                    create_member_object.put("lastName", this.Signup_lname.getText().toString());
                    create_member_object.put("email", this.Signup_mail.getText().toString());
                    create_member_object.put("emailOptIn", this.email_option_chk_bx.isChecked());
                    create_member_object.put("phone", this.Signup_phone_no.getText().toString());
                    create_member_object.put("pushOptIn", true);
                    create_member_object.put("addressStreet", "");
                    create_member_object.put("addressCity", "");
                    create_member_object.put("addressState", "");
                    create_member_object.put("addressZipCode", "");
                    create_member_object.put(PreferenceConstants.PREFERENCE_GENDER, "");
                    create_member_object.put("storeCode", this.hbha_pref_helper.getStringValue("reg_store_code"));
                    if (Utility.isEmptyString(this.Signup_dob.getText().toString())) {
                        create_member_object.put("birthDate", "");
                    } else {
                        create_member_object.put("birthDate", this.Signup_dob.getText().toString());
                    }
                    create_member_object.put("username", this.Signup_phone_no.getText().toString());
                    create_member_object.put(EmailAuthProvider.PROVIDER_ID, this.Signup_pwd.getText().toString());
                    create_member_object.put("sendWelcomeEmail", "demo");
                    create_member_object.put("ruleId", String.valueOf(this.hbha_pref_helper.getIntValue("rule_id")));
                    if (this.hbha_pref_helper.getBooleanValue("reward_rule")) {
                        create_member_object.put("rewardRule", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                    } else {
                        create_member_object.put("rewardRule", "false");
                    }
                    create_member_object.put("smsOptIn", false);
                    create_member_object.put("deviceId", FBUtility.getAndroidDeviceID(getActivity()));
                    create_member_object.put("deviceOsVersion", FBConstant.device_os_ver);
                    create_member_object.put("deviceType", FBConstant.DEVICE_TYPE);
                    create_member_object.put("pushToken", FBPreferences.sharedInstance(getActivity()).getPushToken());
                    create_member_object.put("appId", BuildConfig.APPLICATION_ID);
                    registerMember(create_member_object);
                } catch (JSONException e) {
                }
            } else {
                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
            }
        }
    }



    private void saveEditFieldValues() {
        RuleModule ruleModule;
        this.hbha_pref_helper.saveStringValue("reg_fnm", this.Signup_fname.getText().toString());
        this.hbha_pref_helper.saveStringValue("reg_lnm", this.Signup_lname.getText().toString());
        this.hbha_pref_helper.saveStringValue("reg_mail", this.Signup_mail.getText().toString());
        this.hbha_pref_helper.saveStringValue("reg_pw", this.Signup_pwd.getText().toString());
        this.hbha_pref_helper.saveStringValue("reg_dob", this.Signup_dob.getText().toString());
        this.hbha_pref_helper.saveStringValue("reg_ph_no", this.Signup_phone_no.getText().toString().toString());
        this.hbha_pref_helper.saveStringValue("last_reg_ph_no", this.Signup_phone_no.getText().toString().toString());
        this.hbha_pref_helper.saveStringValue("reg_conf_pw", this.Signup_conf_pwd.getText().toString().toString());
        this.hbha_pref_helper.saveStringValue("reg_conf_ph_no", this.Signup_conf_phone.getText().toString().toString());
        this.hbha_pref_helper.saveBooleanValue("reg_email_option_check", this.email_option_chk_bx.isChecked());
        this.hbha_pref_helper.saveBooleanValue("reg_privacy_terms_check", this.privacy_chk_bx.isChecked());
        if (this.hbha_pref_helper.getIntValue("rule_button_id") == 0) {
            ruleModule = (RuleModule) this.button_points.getTag();
        } else {
            ruleModule = (RuleModule) this.button_offer.getTag();
        }
        if (ruleModule != null) {
            this.hbha_pref_helper.saveIntValue("rule_id", ruleModule.getId());
            this.hbha_pref_helper.saveBooleanValue("reward_rule", ruleModule.isRewardRule());
            return;
        }
        this.hbha_pref_helper.saveIntValue("rule_id", 0);
        this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
    }

    private void setPrefValues() {
        this.Signup_fname.setText(this.hbha_pref_helper.getStringValue("reg_fnm"));
        this.Signup_lname.setText(this.hbha_pref_helper.getStringValue("reg_lnm"));
        this.Signup_mail.setText(this.hbha_pref_helper.getStringValue("reg_mail"));
        this.Signup_pwd.setText(this.hbha_pref_helper.getStringValue("reg_pw"));
        this.Signup_dob.setText(this.hbha_pref_helper.getStringValue("reg_dob"));
        this.Signup_conf_pwd.setText(this.hbha_pref_helper.getStringValue("reg_conf_pw"));
        this.Signup_phone_no.setText(this.hbha_pref_helper.getStringValue("reg_ph_no"));
        this.Signup_conf_phone.setText(this.hbha_pref_helper.getStringValue("reg_conf_ph_no"));
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("reg_fnm"))) {
            this.isFirstTime = true;
        } else {
            this.isFirstTime = false;
        }
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("reg_store_id"))) {
            this.email_option_chk_bx.setChecked(true);
            this.select_store_rl.setVisibility(View.VISIBLE);
            this.change_store_tv.setText("SELECT STORE");
            this.select_store_nm_tv.setVisibility(View.VISIBLE);
            this.select_store_details_ll.setVisibility(View.VISIBLE);
        } else {
            this.email_option_chk_bx.setChecked(this.hbha_pref_helper.getBooleanValue("reg_email_option_check"));
            this.privacy_chk_bx.setChecked(this.hbha_pref_helper.getBooleanValue("reg_privacy_terms_check"));
            this.select_store_nm_tv.setText(this.hbha_pref_helper.getStringValue("reg_store_city"));
            this.select_store_add_tv.setText(this.hbha_pref_helper.getStringValue("reg_store_add"));
            this.select_store_ph_tv.setText("Phone: " + this.hbha_pref_helper.getStringValue("reg_store_ph"));
            if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("reg_store_ph"))) {
                this.select_store_ph_tv.setVisibility(View.VISIBLE);
            } else {
                this.select_store_ph_tv.setVisibility(View.VISIBLE);
            }
            this.select_store_rl.setVisibility(View.VISIBLE);
            this.select_store_nm_tv.setVisibility(View.VISIBLE);
            this.change_store_tv.setText("CHANGE STORE");
            this.select_store_details_ll.setVisibility(View.VISIBLE);
        }
        Spannable wordtoSpan = new SpannableString("Disclaimer: This phone number will become your loyalty ID number");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ham_burg_new)), 0, 11, 33);
        this.disclaimer_tv.setText(wordtoSpan);
    }

    private void initView(View v) {
        this.Signup_fname = (EditText) v.findViewById(R.id.signup_fname);
        this.Signup_lname = (EditText) v.findViewById(R.id.signup_lname);
        this.Signup_mail = (EditText) v.findViewById(R.id.signup_email);
        this.Signup_pwd = (EditText) v.findViewById(R.id.signup_pwd);
        this.Signup_conf_pwd = (EditText) v.findViewById(R.id.signup_conf_pwd);
        this.Signup_conf_phone = (EditText) v.findViewById(R.id.signup_conf_phone);
        this.Signup_dob = (TextView) v.findViewById(R.id.signup_dob);
        this.Signup_phone_no = (EditText) v.findViewById(R.id.signup_phone);
        this.signup_lay = (LinearLayout) v.findViewById(R.id.signup_lay);
        this.select_store_details_ll = (LinearLayout) v.findViewById(R.id.select_store_details_ll);
        this.select_signup_point_layout = (LinearLayout) v.findViewById(R.id.select_signup_point_layout);
        this.change_store_rl = (RelativeLayout) v.findViewById(R.id.change_store_rl);
        this.select_store_rl = (RelativeLayout) v.findViewById(R.id.select_store_rl);
        this.change_store_tv = (TextView) v.findViewById(R.id.change_store_tv);
        this.select_store_tv = (TextView) v.findViewById(R.id.select_store_tv);
        this.select_store_nm_tv = (TextView) v.findViewById(R.id.select_store_nm_tv);
        this.select_store_add_tv = (TextView) v.findViewById(R.id.select_store_add_tv);
        this.select_store_ph_tv = (TextView) v.findViewById(R.id.select_store_ph_tv);
        this.select_signup_rule_text = (TextView) v.findViewById(R.id.select_signup_rule_text);
        this.select_store_details_rg = (LinearLayout) v.findViewById(R.id.select_store_details_rg);
        this.email_option_chk_bx = (CheckBox) v.findViewById(R.id.email_option_chk_bx);
        this.privacy_chk_bx = (CheckBox) v.findViewById(R.id.privacy_chk_bx);
        this.disclaimer_tv = (TextView) v.findViewById(R.id.disclaimer_tv);
        this.button_offer = (Button) v.findViewById(R.id.button_offer);
        this.button_points = (Button) v.findViewById(R.id.button_points);
        this.privacy_policy_textView = (TextView) v.findViewById(R.id.privacy_policy_textView);
        setPrivacyPolicy();
    }

    private void setPrivacyPolicy() {
        this.privacy_policy_textView.setClickable(true);
        String sFirstPart = "By clicking Signup, you agree to our ";
        String sSecondPart = "Terms of Use";
        String sThirdPart = " and have read our ";
        String sFourthPart = "Privacy Policy";
        int firstLength = sFirstPart.length();
        int secondLength = firstLength + sSecondPart.length();
        int thirdLength = secondLength + sThirdPart.length();
        int fourhength = thirdLength + sFourthPart.length();
        SpannableString span = new SpannableString(sFirstPart + sSecondPart + sThirdPart + sFourthPart);
        span.setSpan(new URLSpan("http://d7be05wqyprgy.cloudfront.net/hbh/loyalty/TermsAndConditions/220a8af7648d480ab0ab93494f193239.html"), firstLength, secondLength, 33);
        span.setSpan(new URLSpan("http://d7be05wqyprgy.cloudfront.net/hbh/loyalty/PrivacyPolicy/e90378f5468a4c60b4c4d969ba15c901.html"), thirdLength, fourhength, 33);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ham_burg_new)), firstLength, secondLength, 33);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ham_burg_new)), thirdLength, fourhength, 33);
        this.privacy_policy_textView.setText(span);
        this.privacy_policy_textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void getBonusRuleList() {
        if (FBUtility.isNetworkAvailable(getActivity())) {
            performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
            FBUserService.sharedInstance().getBonusRuleList(new JSONArray(), new FBBonusRuleListCallback() {
                public void onBonusRuleListCallback(JSONArray response, Exception error) {
                    if (response != null) {
                        try {
                            if (response.length() > 0) {
                                for (int r = 0; r < response.length(); r++) {
                                    JSONObject rule_obj = response.optJSONObject(r);
                                    SignUpFragment.this.rule_list_data.add(new RuleModule(rule_obj.optString("criteriaName"), rule_obj.optInt("criteriaValue"), rule_obj.optString("description"), rule_obj.optBoolean("pointRule"), rule_obj.optInt("id"), rule_obj.optBoolean("rewardRule")));
                                }
                            }
                        } catch (Exception e) {
                            SignUpFragment.this.addRuleButton();
                            SignUpFragment.this.getToken();
                            return;
                        } catch (Throwable th) {
                            SignUpFragment.this.addRuleButton();
                            SignUpFragment.this.getToken();
                        }
                    } else {
                        Utility.tryHandleTokenExpiry(SignUpFragment.this.getActivity(), error);
                    }
                    SignUpFragment.this.addRuleButton();
                    SignUpFragment.this.getToken();
                }
            });
            return;
        }
        Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
    }

    private void getToken() {
        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(getActivity()));
            object.put("tenantId", FBConstant.client_tenantid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FBUserService.sharedInstance().getTokenApi(object, new FBGetTokenCallback() {

            public void onGetTokencallback(JSONObject r7, Exception r8) {
                performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(true));
            }
        });
    }

    private void addRuleButton() {
        if (this.rule_list_data == null || this.rule_list_data.size() <= 0 || getActivity() == null) {
            this.select_store_details_rg.setVisibility(View.VISIBLE);
            return;
        }
        this.select_store_details_rg.setVisibility(View.VISIBLE);
        if (this.rule_list_data.size() == 0) {
            this.select_store_details_rg.setVisibility(View.VISIBLE);
        } else if (this.rule_list_data.size() == 1) {
            this.button_points.setText(((RuleModule) this.rule_list_data.get(0)).getDescription());
            this.button_points.setTag(this.rule_list_data.get(0));
            this.button_offer.setVisibility(View.VISIBLE);
        } else {
            this.button_points.setText(((RuleModule) this.rule_list_data.get(0)).getDescription());
            this.button_points.setTag(this.rule_list_data.get(0));
            this.button_offer.setText(((RuleModule) this.rule_list_data.get(1)).getDescription());
            this.button_offer.setTag(this.rule_list_data.get(1));
        }
        if (this.isFirstTime) {
            this.isFirstTime = false;
            setClickedButton(R.id.button_points);
        } else if (this.hbha_pref_helper.getIntValue("rule_button_id") == 0) {
            setClickedButton(R.id.button_points);
        } else {
            setClickedButton(R.id.button_offer);
        }
    }

    public void registerMember(JSONObject create_member_object) {
        if (FBUtility.isNetworkAvailable(getActivity())) {
            setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
            FBUserService.sharedInstance().createMember(create_member_object, new FBCreateMemberCallback() {
                public void onCreateMemberCallback(JSONObject response, Exception error) {
                    if (response == null) {
                        Utility.tryHandleTokenExpiry(SignUpFragment.this.getActivity(), error);
                    } else if (response.has("successFlag")) {
                        if (response.optBoolean("successFlag")) {
                            Utility.showToast(SignUpFragment.this.getActivity(), "Registered Successfully");
                            SignUpFragment.this.hbha_pref_helper.saveBooleanValue(PreferenceConstants.PREFERENCE_SIGNUP_FIRSTTIME, true);
                            SignUpFragment.this.loginMember(SignUpFragment.this.hbha_pref_helper.getStringValue("reg_ph_no"), SignUpFragment.this.hbha_pref_helper.getStringValue("reg_pw"));
                        } else {
                            Utility.showToast(SignUpFragment.this.getActivity(), response.optString("message"));
                        }
                    }
                    SignUpFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                }
            });
            return;
        }
        Utility.showToast(getActivity(), "No/Poor Internet connection");
    }

//    private void setDateField() {
//        this.Signup_dob.setOnClickListener(this);
//        Calendar newCalendar = Calendar.getInstance();
//        this.fromDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                SignUpFragment.this.Signup_dob.setText(SignUpFragment.this.dateFormatter.format(newDate.getTime()));
//            }
//        }, newCalendar.get(1) - 27, newCalendar.get(2), newCalendar.get(5));
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_dob:
                Utility.hideSoftKeyboard(getActivity());
                this.fromDatePickerDialog.show();
                return;
            case R.id.button_points:
                setClickedButton(R.id.button_points);
                return;
            case R.id.button_offer:
                setClickedButton(R.id.button_offer);
                return;
            case R.id.change_store_rl:
                storeEvent(true);
                return;
            case R.id.select_store_rl:
                storeEvent(false);
                return;
            case R.id.head_back_img_vw:
                this.hbha_pref_helper.saveIntValue("login_flag", 0);
                return;
            default:
                return;
        }
    }

    private void setClickedButton(int button) {
        if (button == R.id.button_points) {
            this.button_points.setBackgroundResource(R.drawable.grey_border_with_burgundy);
            this.button_points.setTextColor(getActivity().getResources().getColor(R.color.white));
            this.button_offer.setBackgroundResource(R.drawable.grey_border);
            this.button_offer.setTextColor(getActivity().getResources().getColor(R.color.black));
            this.hbha_pref_helper.saveIntValue("rule_button_id", 0);
            return;
        }
        this.button_offer.setBackgroundResource(R.drawable.grey_border_with_burgundy);
        this.button_offer.setTextColor(getActivity().getResources().getColor(R.color.white));
        this.button_points.setBackgroundResource(R.drawable.grey_border);
        this.button_points.setTextColor(getActivity().getResources().getColor(R.color.black));
        this.hbha_pref_helper.saveIntValue("rule_button_id", 1);
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void performDialogAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void loginMember(final String ph_no, final String ps_wd) {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        try {
            JSONObject login_object = new JSONObject();
            login_object.put("username", ph_no);
            login_object.put(EmailAuthProvider.PROVIDER_ID, ps_wd);
            FBUserService.sharedInstance().loginMember(login_object, new FBLoginMemberCallback() {
                public void onLoginMemberCallback(JSONObject response, Exception error) {
                    if (response != null) {
                        String _message = response.optString("message");
                        if (!response.has("successFlag")) {
                            return;
                        }
                        if (response.optBoolean("successFlag")) {
                            try {
                                SignUpFragment.this.hbha_pref_helper.saveStringValue("current_reg_ph_no", ph_no);
                                if (SignUpFragment.this.hbha_pref_helper.getBooleanValue("last_reg_ph_no_flg")) {
                                    SignUpFragment.this.hbha_pref_helper.saveBooleanValue("last_reg_ph_no_flg", false);
                                } else {
                                    SignUpFragment.this.hbha_pref_helper.saveStringValue("last_reg_ph_no", "");
                                }
                                SignUpFragment.this.hbha_pref_helper.saveBooleanValue("remind_me_value", true);
                              //  SignUpFragment.this.hbha_pref_helper.saveStringValue("remind_me_user_name", Utility.encryptIt(ph_no, SignUpFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                             //   SignUpFragment.this.hbha_pref_helper.saveStringValue("remind_me_user_pass", Utility.encryptIt(ps_wd, SignUpFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                                FBPreferences.sharedInstance(SignUpFragment.this.getActivity()).setAccessTokenforapp(response.optString("accessToken"));
                                FBUserService.sharedInstance().access_token = response.optString("accessToken");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LAST_NAME, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_ID, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_OPTION, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_PUSH_OPTION, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_DOB, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CELL_PHONE, "");
                                SignUpFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_GENDER, "");
                              //  new getMemberProfileAsyncTask().execute(new String[0]);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        SignUpFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                        Utility.tryHandleTokenExpiry(SignUpFragment.this.getActivity(), error);
                        return;
                    }
                    SignUpFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                    Utility.tryHandleTokenExpiry(SignUpFragment.this.getActivity(), error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void deviceUpdate() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", FBPreferences.sharedInstance(getActivity()).getUserMemberforAppId());
            object.put("deviceId", FBUtility.getAndroidDeviceID(getActivity()));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken", FBPreferences.sharedInstance(getActivity()).getPushToken());
            object.put("appId", BuildConfig.APPLICATION_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FBUserService.sharedInstance().deviceUpdate(object, new FBDeviceUpdateCallback() {
            public void onDeviceUpdateCallback(JSONObject response, Exception error) {
                Intent i = new Intent(SignUpFragment.this.getActivity(), MainActivity.class);
                SignUpFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                SignUpFragment.this.startActivity(i);
                SignUpFragment.this.getActivity().finish();
            }
        });
    }
}
