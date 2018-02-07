package com.hbh.honeybaked.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.Preferences.FBPreferences;
import com.facebook.internal.ServerProtocol;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBStoreService.FBStoreDetailCallback;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBDeviceUpdateCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBLoginMemberCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBforgetPasswordCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.firebase.auth.EmailAuthProvider;
import com.hbh.honeybaked.BuildConfig;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.activity.MainActivity;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.Constants;
import com.hbh.honeybaked.constants.PreferenceConstants;

import com.hbh.honeybaked.supportingfiles.JsonParser;
import com.hbh.honeybaked.supportingfiles.Utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends BaseFragment {
    TextView create_txt;
    TextView disclaimer_tv;
    private EditText email_et;
    String email_id;
    TextView forget_pw_tv;
    String forgot_edit;
    InputMethodManager imm = null;
    RelativeLayout login_rl;
    String pass_word;
    private EditText pass_word_et;
    CheckBox remember_chk_bx;

    class C17431 implements OnClickListener {
        C17431() {
        }

        public void onClick(View v) {
            LoginFragment.this.email_id = LoginFragment.this.email_et.getText().toString();
            LoginFragment.this.pass_word = LoginFragment.this.pass_word_et.getText().toString();
            if (Utility.isEmptyString(LoginFragment.this.email_et.getText().toString())) {
                LoginFragment.this.email_et.requestFocus();
                LoginFragment.this.email_et.setError("Phone number is required!");
            } else if (LoginFragment.this.email_et.getText().toString().length() < 12) {
                LoginFragment.this.email_et.requestFocus();
                LoginFragment.this.email_et.setError("Please Enter the valid phone number");
            } else if (Utility.isEmptyString(LoginFragment.this.pass_word_et.getText().toString())) {
                LoginFragment.this.pass_word_et.requestFocus();
                LoginFragment.this.pass_word_et.setError("Please Enter the password");
            } else {
                Utility.hideSoftKeyboard(LoginFragment.this.getActivity());
                if (LoginFragment.this.cd.isConnectingToInternet()) {
                    LoginFragment.this.loginMember(LoginFragment.this.email_id, LoginFragment.this.pass_word);
                } else {
                    Utility.showToast(LoginFragment.this.getActivity(), AppConstants.NO_CONNECTION_TEXT);
                }
            }
        }
    }

    class C17442 implements TextWatcher {
        C17442() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginFragment.this.pass_word_et.setError(null);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            LoginFragment.this.pass_word_et.setError(null);
        }
    }

    class C17453 extends PhoneNumberFormattingTextWatcher {
        private boolean backspacingFlag = false;
        private int cursorComplement;
        private boolean editedFlag = false;

        C17453() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.cursorComplement = s.length() - LoginFragment.this.email_et.getSelectionStart();
            if (count > after) {
                this.backspacingFlag = true;
            } else {
                this.backspacingFlag = false;
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginFragment.this.email_et.setError(null);
        }

        public void afterTextChanged(Editable s) {
            LoginFragment.this.email_et.setError(null);
            String phone = s.toString().replaceAll("[^\\d]", "");
            if (this.editedFlag) {
                this.editedFlag = false;
            } else if (phone.length() >= 6 && !this.backspacingFlag) {
                this.editedFlag = true;
                LoginFragment.this.email_et.setText(phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6));
                LoginFragment.this.email_et.setSelection(LoginFragment.this.email_et.getText().length() - this.cursorComplement);
            } else if (phone.length() >= 3 && !this.backspacingFlag) {
                this.editedFlag = true;
                LoginFragment.this.email_et.setText(phone.substring(0, 3) + "-" + phone.substring(3));
                LoginFragment.this.email_et.setSelection(LoginFragment.this.email_et.getText().length() - this.cursorComplement);
            }
        }
    }

    class C17475 implements FBDeviceUpdateCallback {
        C17475() {
        }

        public void onDeviceUpdateCallback(JSONObject response, Exception error) {
            Intent i = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
            LoginFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            LoginFragment.this.startActivity(i);
            LoginFragment.this.getActivity().finish();
        }
    }

    class C17519 implements FBforgetPasswordCallback {
        C17519() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onFBforgetPasswordCallback(JSONObject r7, Exception r8) {
            /*
            r6 = this;
            r5 = 0;
            if (r7 == 0) goto L_0x005c;
        L_0x0003:
            r2 = "successFlag";
            r2 = r7.has(r2);	 Catch:{ Exception -> 0x003f }
            if (r2 == 0) goto L_0x0023;
        L_0x000b:
            r2 = "successFlag";
            r1 = r7.optBoolean(r2);	 Catch:{ Exception -> 0x003f }
            r2 = 1;
            if (r1 != r2) goto L_0x002f;
        L_0x0014:
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;	 Catch:{ Exception -> 0x003f }
            r2 = r2.getActivity();	 Catch:{ Exception -> 0x003f }
            r3 = "message";
            r3 = r7.optString(r3);	 Catch:{ Exception -> 0x003f }
            com.hbh.honeybaked.supportingfiles.Utility.showToast(r2, r3);	 Catch:{ Exception -> 0x003f }
        L_0x0023:
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r5);
            r2.performDialogAction(r3, r4);
        L_0x002e:
            return;
        L_0x002f:
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;	 Catch:{ Exception -> 0x003f }
            r2 = r2.getActivity();	 Catch:{ Exception -> 0x003f }
            r3 = "message";
            r3 = r7.optString(r3);	 Catch:{ Exception -> 0x003f }
            com.hbh.honeybaked.supportingfiles.Utility.showToast(r2, r3);	 Catch:{ Exception -> 0x003f }
            goto L_0x0023;
        L_0x003f:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ all -> 0x004f }
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r5);
            r2.performDialogAction(r3, r4);
            goto L_0x002e;
        L_0x004f:
            r2 = move-exception;
            r3 = com.hbh.honeybaked.fragment.LoginFragment.this;
            r4 = "hide_progress_dialog";
            r5 = java.lang.Boolean.valueOf(r5);
            r3.performDialogAction(r4, r5);
            throw r2;
        L_0x005c:
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r5);
            r2.performDialogAction(r3, r4);
            r2 = com.hbh.honeybaked.fragment.LoginFragment.this;
            r2 = r2.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r2, r8);
            goto L_0x002e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.LoginFragment.9.onFBforgetPasswordCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    private class getMemberProfileAsyncTask extends AsyncTask<String, Void, String> {
        private getMemberProfileAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            LoginFragment.this.performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        }

        protected String doInBackground(String... params) {
            return new JsonParser().getStringFromUrl(Constants.sdkPointingUrl(10) + "/member/getMemberProfileForSDK", FBUtility.getAndroidDeviceID(LoginFragment.this.getActivity()), FBPreferences.sharedInstance(LoginFragment.this.getActivity()).getAccessTokenforapp());
        }

        protected void onPostExecute(String s) {
            if (Utility.isEmptyString(s)) {
                LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject == null) {
                    LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                } else if (jsonObject.optBoolean("successFlag")) {
                    String str = "";
                    FBUserService.sharedInstance().member.initWithJson(jsonObject, LoginFragment.this.getActivity());
                    FBPreferences.sharedInstance(LoginFragment.this.getActivity()).setUserMemberforAppId(Utility.getStringValue(jsonObject, "customerID"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID, Utility.getStringValue(jsonObject, "customerID"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME, Utility.getStringValue(jsonObject, "firstName"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LAST_NAME, Utility.getStringValue(jsonObject, "lastName"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_ID, Utility.getStringValue(jsonObject, "emailID"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO, Utility.getStringValue(jsonObject, "loyaltyNo"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_OPTION, String.valueOf(jsonObject.optBoolean("emailOpted") ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_PUSH_OPTION, String.valueOf(jsonObject.optBoolean("pushOpted") ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false"));
                    String formattedDate = "";
                    String str2 = Utility.getStringValue(jsonObject, "dateOfBirth");
                    if (!Utility.isEmptyString(str2)) {
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(str2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            if (Utility.isEmptyString(str)) {
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store_id", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store_code", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("lat", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("lang", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("city", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store_ex_hrs_head", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store_ex_hrs", "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue("store_image", "");
                                Utility.showToast(LoginFragment.this.getActivity(), "Login Successful");
                                LoginFragment.this.hbha_pref_helper.saveIntValue("login_flag", 1);
                                LoginFragment.this.deviceUpdate();
                            } else {
                               // LoginFragment.this.getStoreDetails(str);
                            }
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_fnm", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_lnm", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_mail", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_pw", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_dob", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_ph_no", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_conf_pw", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_conf_ph_no", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_id", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_code", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_nm", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_add", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_ph", "");
                            LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_city", "");
                            LoginFragment.this.hbha_pref_helper.saveIntValue("rule_id", 0);
                            LoginFragment.this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
                            LoginFragment.this.hbha_pref_helper.saveStringValue("customerID", jsonObject.optString("customerID"));
                        } finally {
                            LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                        }
                    }
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_DOB, formattedDate);
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CELL_PHONE, Utility.getStringValue(jsonObject, "cellPhone"));
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_GENDER, Utility.getStringValue(jsonObject, "customerGender"));
                    String formattedDate1 = "";
                    if (!Utility.isEmptyString(Utility.getStringValue(jsonObject, "previousLoginTime"))) {
                        try {
                            formattedDate1 = new SimpleDateFormat("MM/dd/yy").format(new SimpleDateFormat("yyyy-MM-dd").parse(Utility.getStringValue(jsonObject, "previousLoginTime").split("\\s+")[0]));
                        } catch (org.apache.http.ParseException e3) {
                            e3.printStackTrace();
                        } catch (ParseException e4) {
                            e4.printStackTrace();
                        }
                    }
                    LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME, formattedDate1);
                    JSONObject storeJsonObject = jsonObject.optJSONObject("store");
                    if (!Utility.isEmpty(storeJsonObject)) {
                        str = storeJsonObject.getString("storeId");
                    }
                    if (Utility.isEmptyString(str)) {
                       // LoginFragment.this.getStoreDetails(str);
                    } else {
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store_id", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store_code", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("lat", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("lang", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("city", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store_ex_hrs_head", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store_ex_hrs", "");
                        LoginFragment.this.hbha_pref_helper.saveStringValue("store_image", "");
                        Utility.showToast(LoginFragment.this.getActivity(), "Login Successful");
                        LoginFragment.this.hbha_pref_helper.saveIntValue("login_flag", 1);
                        LoginFragment.this.deviceUpdate();
                    }
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_fnm", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_lnm", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_mail", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_pw", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_dob", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_ph_no", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_conf_pw", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_conf_ph_no", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_id", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_code", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_nm", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_add", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_ph", "");
                    LoginFragment.this.hbha_pref_helper.saveStringValue("reg_store_city", "");
                    LoginFragment.this.hbha_pref_helper.saveIntValue("rule_id", 0);
                    LoginFragment.this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
                    LoginFragment.this.hbha_pref_helper.saveStringValue("customerID", jsonObject.optString("customerID"));
                } else {
                    LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                    if (jsonObject.has("message")) {
                        Utility.showToast(LoginFragment.this.getActivity(), jsonObject.optString("message"));
                    } else {
                        Utility.showToast(LoginFragment.this.getActivity(), "Error in Login, Please try again later!");
                    }
                }
            } catch (Exception e22) {
                e22.printStackTrace();
                LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        this.hb_dbHelper.openDb();
        this.email_et = (EditText) v.findViewById(R.id.email_et);
        this.pass_word_et = (EditText) v.findViewById(R.id.password_et);
        this.login_rl = (RelativeLayout) v.findViewById(R.id.login_rl);
        this.create_txt = (TextView) v.findViewById(R.id.create);
        this.forget_pw_tv = (TextView) v.findViewById(R.id.forget_pw_tv);
        this.disclaimer_tv = (TextView) v.findViewById(R.id.disclaimer_tv);
        this.remember_chk_bx = (CheckBox) v.findViewById(R.id.remember_chk_bx);
        if (this.hbha_pref_helper.getBooleanValue("remind_me_value")) {
          //  this.email_et.setText(Utility.decryptIt(this.hbha_pref_helper.getStringValue("remind_me_user_name"), getString(R.string.app_name) + "^%$#entappia"));
          //  this.pass_word_et.setText(Utility.decryptIt(this.hbha_pref_helper.getStringValue("remind_me_user_pass"), getString(R.string.app_name) + "^%$#entappia"));
            this.remember_chk_bx.setChecked(true);
        } else {
            this.remember_chk_bx.setChecked(false);
            this.email_et.setText("");
            this.pass_word_et.setText("");
        }
        this.forget_pw_tv.setPaintFlags(this.forget_pw_tv.getPaintFlags() | 8);
        this.create_txt.setPaintFlags(this.create_txt.getPaintFlags() | 8);
        this.imm = (InputMethodManager) getActivity().getSystemService("input_method");
        this.create_txt.setOnClickListener(this);
        this.forget_pw_tv.setOnClickListener(this);
        Spannable wordtoSpan = new SpannableString("Disclaimer: App not available for use in California locations.");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ham_burg_new)), 0, 11, 33);
        this.disclaimer_tv.setText(wordtoSpan);
        this.login_rl.setOnClickListener(new C17431());
        this.pass_word_et.addTextChangedListener(new C17442());
        this.email_et.addTextChangedListener(new C17453());
        return v;
    }

    public void loginMember(final String ph_no, String ps_wd) {
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
                                LoginFragment.this.hbha_pref_helper.saveStringValue("current_reg_ph_no", ph_no);
                                if (LoginFragment.this.hbha_pref_helper.getBooleanValue("last_reg_ph_no_flg")) {
                                    LoginFragment.this.hbha_pref_helper.saveBooleanValue("last_reg_ph_no_flg", false);
                                } else {
                                    LoginFragment.this.hbha_pref_helper.saveStringValue("last_reg_ph_no", "");
                                }
                                if (LoginFragment.this.hbha_pref_helper.getStringValue("last_reg_ph_no").equals(ph_no)) {
                                    LoginFragment.this.hbha_pref_helper.saveBooleanValue(PreferenceConstants.PREFERENCE_SIGNUP_FIRSTTIME, false);
                                }
                                LoginFragment.this.hbha_pref_helper.saveBooleanValue("remind_me_value", LoginFragment.this.remember_chk_bx.isChecked());
                             //   LoginFragment.this.hbha_pref_helper.saveStringValue("remind_me_user_name", Utility.encryptIt(LoginFragment.this.email_id, LoginFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                             //   LoginFragment.this.hbha_pref_helper.saveStringValue("remind_me_user_pass", Utility.encryptIt(LoginFragment.this.pass_word, LoginFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                                FBPreferences.sharedInstance(LoginFragment.this.getActivity()).setAccessTokenforapp(response.optString("accessToken"));
                                FBUserService.sharedInstance().access_token = response.optString("accessToken");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LAST_NAME, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_ID, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_OPTION, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_PUSH_OPTION, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_DOB, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_CELL_PHONE, "");
                                LoginFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_GENDER, "");
                                new getMemberProfileAsyncTask().execute(new String[0]);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                        Utility.tryHandleTokenExpiry(LoginFragment.this.getActivity(), error);
                        return;
                    }
                    LoginFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                    Utility.tryHandleTokenExpiry(LoginFragment.this.getActivity(), error);
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
        FBUserService.sharedInstance().deviceUpdate(object, new C17475());
    }

    private void showDialog(String content) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.custom_alert_dialog_layout);
        final EditText forgot_edit_text = (EditText) dialog.findViewById(R.id.new_pw_tv);
        forgot_edit_text.setHint("email address");
        TextView text_head = (TextView) dialog.findViewById(R.id.forgot_head_text);
        TextView text_sub_head = (TextView) dialog.findViewById(R.id.head_text_dialog);
        text_sub_head.setVisibility(View.VISIBLE);
        text_sub_head.setText("Please enter your email address");
        Button fpw_cancel = (Button) dialog.findViewById(R.id.fpw_cancel);
        final Button fpw_ok = (Button) dialog.findViewById(R.id.fpw_ok);
        text_head.setText(content);
        fpw_ok.setEnabled(false);
        fpw_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (LoginFragment.this.cd.isConnectingToInternet()) {
                    LoginFragment.this.forgot_edit = forgot_edit_text.getText().toString();
                    LoginFragment.this.forgotPassword(LoginFragment.this.forgot_edit);
                } else {
                    Utility.showToast(LoginFragment.this.getActivity(), AppConstants.NO_CONNECTION_TEXT);
                }
                LoginFragment.this.imm.hideSoftInputFromWindow(forgot_edit_text.getWindowToken(), 0);
            }
        });
        forgot_edit_text.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utility.isValidEmail(forgot_edit_text.getText().toString())) {
                    fpw_ok.setEnabled(true);
                    fpw_ok.setBackgroundResource(R.drawable.my_button_border);
                    return;
                }
                fpw_ok.setEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        fpw_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void forgotPassword(String forgot_edit) {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        try {
            JSONObject forgot_pwd_object = new JSONObject();
            forgot_pwd_object.put("email", forgot_edit);
            FBUserService.sharedInstance().forgetPassword(forgot_pwd_object, new C17519());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_pw_tv:
                showDialog(AppConstants.FORGOT_PASSWORD_TEXT);
                return;
            case R.id.create:
                this.hbha_pref_helper.saveIntValue("rule_id", 0);
                this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
                this.hbha_pref_helper.saveBooleanValue("reg_email_option_check", false);
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SIGNUP_PAGE, Boolean.valueOf(false));
                return;
            case R.id.head_back_img_vw:
                this.hbha_pref_helper.saveIntValue("login_flag", 0);
                return;
            default:
                return;
        }
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void performDialogAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }


}
