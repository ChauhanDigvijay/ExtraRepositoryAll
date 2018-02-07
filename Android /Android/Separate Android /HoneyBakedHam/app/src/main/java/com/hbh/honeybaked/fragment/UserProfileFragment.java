package com.hbh.honeybaked.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.internal.ServerProtocol;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBChangePasswordCallback;
import com.fishbowl.basicmodule.Services.FBUserService.FBMemberUpdateCallback;
import com.google.firebase.auth.EmailAuthProvider;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserProfileFragment extends BaseFragment {
    LinearLayout Change_lay;
    LinearLayout Dob_lay;
    TextView Edit_mail;
    TextView Edit_profile_text;
    EditText FirstName_Text;
    LinearLayout First_name_lay;
    EditText LastName_Text;
    LinearLayout Last_name_lay;
    LinearLayout Phone_lay;
    private SimpleDateFormat dateFormatter;
    TextView dateOfBirth_Text;
    TextView email_Text;
    ToggleButton email_toggle_bt;
    private DatePickerDialog fromDatePickerDialog;
    private boolean isUpate = false;
    TextView phone_text;
    ToggleButton push_toggle_bt;
    LinearLayout sv_ll;
    RelativeLayout try_rl;
    LinearLayout user_pf_Lay;
    ScrollView user_sv;

    class C18021 implements OnDateSetListener {
        C18021() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            UserProfileFragment.this.dateOfBirth_Text.setText(UserProfileFragment.this.dateFormatter.format(newDate.getTime()));
        }
    }

    class C18032 implements FBMemberUpdateCallback {
        C18032() {
        }

        @RequiresApi(api = 19)
        public void onMemberUpdateCallback(JSONObject jsonObject, Exception e) {
            if (jsonObject != null) {
                if (jsonObject.has("successFlag")) {
                    if (jsonObject.optBoolean("successFlag")) {
                        UserProfileFragment.this.isUpate = false;
                        UserProfileFragment.this.Edit_profile_text.setText("Edit >");
                        UserProfileFragment.this.profileEdit();
                        Utility.showToast(UserProfileFragment.this.getActivity(), "Member updated Successfully");
                        UserProfileFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME, UserProfileFragment.this.FirstName_Text.getText().toString());
                        UserProfileFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LAST_NAME, UserProfileFragment.this.LastName_Text.getText().toString());
                        if (UserProfileFragment.this.Dob_lay.isShown()) {
                            UserProfileFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_DOB, UserProfileFragment.this.dateOfBirth_Text.getText().toString());
                        }
                        UserProfileFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EMAIL_OPTION, UserProfileFragment.this.email_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                        UserProfileFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_PUSH_OPTION, UserProfileFragment.this.push_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                    } else {
                        Utility.hideSoftKeyboard(UserProfileFragment.this.getActivity());
                        Utility.showToast(UserProfileFragment.this.getActivity(), jsonObject.optString("message"));
                    }
                }
                UserProfileFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                return;
            }
            Utility.hideSoftKeyboard(UserProfileFragment.this.getActivity());
            UserProfileFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            Utility.tryHandleTokenExpiry(UserProfileFragment.this.getActivity(), e);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile_page, container, false);
        this.user_sv = (ScrollView) v.findViewById(R.id.user_sv);
        this.try_rl = (RelativeLayout) v.findViewById(R.id.try_rl);
        this.user_pf_Lay = (LinearLayout) v.findViewById(R.id.user_pf_lay);
        this.sv_ll = (LinearLayout) v.findViewById(R.id.sv_ll);
        this.First_name_lay = (LinearLayout) v.findViewById(R.id.first_name_lay);
        this.Last_name_lay = (LinearLayout) v.findViewById(R.id.last_name_lay);
        this.Dob_lay = (LinearLayout) v.findViewById(R.id.dob_lay);
        this.Phone_lay = (LinearLayout) v.findViewById(R.id.phone_lay);
        this.Change_lay = (LinearLayout) v.findViewById(R.id.change_tab);
        this.FirstName_Text = (EditText) v.findViewById(R.id.first_name_txt);
        this.LastName_Text = (EditText) v.findViewById(R.id.last_name_txt);
        this.email_Text = (TextView) v.findViewById(R.id.email_txt);
        this.dateOfBirth_Text = (TextView) v.findViewById(R.id.dob_txt);
        this.phone_text = (TextView) v.findViewById(R.id.phone_txt);
        this.Edit_profile_text = (TextView) v.findViewById(R.id.edit_profile);
        this.Edit_mail = (TextView) v.findViewById(R.id.edit_mail);
        this.email_toggle_bt = (ToggleButton) v.findViewById(R.id.email_toggle_bt);
        this.push_toggle_bt = (ToggleButton) v.findViewById(R.id.push_toggle_bt);
        this.Edit_profile_text.setPaintFlags(this.Edit_profile_text.getPaintFlags() | 8);
        this.Edit_mail.setPaintFlags(this.Edit_mail.getPaintFlags() | 8);
        this.Edit_profile_text.setOnClickListener(this);
        this.Change_lay.setOnClickListener(this);
        this.Dob_lay.setOnClickListener(this);
        this.email_toggle_bt.setOnClickListener(this);
        this.push_toggle_bt.setOnClickListener(this);
        this.hb_dbHelper.openDb();
        initViews();
        if (this.cd.isConnectingToInternet()) {
            this.try_rl.setVisibility(View.VISIBLE);
            this.sv_ll.setVisibility(View.VISIBLE);
            this.FirstName_Text.setText(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME));
            this.LastName_Text.setText(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LAST_NAME));
            this.email_Text.setText(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_EMAIL_ID));
            this.email_toggle_bt.setChecked(Boolean.parseBoolean(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_EMAIL_OPTION)));
            this.push_toggle_bt.setChecked(Boolean.parseBoolean(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_PUSH_OPTION)));
            if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_DOB))) {
                this.Dob_lay.setVisibility(View.VISIBLE);
            } else {
                this.dateOfBirth_Text.setText(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_DOB));
            }
            this.phone_text.setText(Utility.convertToUsFormat(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_CELL_PHONE)));
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
            this.try_rl.setVisibility(View.VISIBLE);
            this.sv_ll.setVisibility(View.VISIBLE);
        }
        this.dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        this.dateOfBirth_Text.setInputType(0);
       // setDateField();
        profileEdit();
        return v;
    }

//    private void setDateField() {
//        Calendar newCalendar = Calendar.getInstance();
//        this.fromDatePickerDialog = new DatePickerDialog(getActivity(), new C18021(), newCalendar.get(1) - 13, newCalendar.get(2), newCalendar.get(5));
//        Calendar newCalendar1 = Calendar.getInstance();
//        newCalendar1.add(1, -13);
//        this.fromDatePickerDialog.getDatePicker().setMaxDate(newCalendar1.getTimeInMillis());
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                this.isUpate = !this.isUpate;
                if (this.isUpate) {
                    profileEdit();
                    this.Edit_profile_text.setText("Update >");
                    return;
                }
                this.Edit_profile_text.setText("Edit >");
                if (Utility.isEmptyString(this.FirstName_Text.getText().toString())) {
                    this.FirstName_Text.requestFocus();
                    this.FirstName_Text.setError("First name is required!");
                    return;
                } else if (Utility.isEmptyString(this.LastName_Text.getText().toString())) {
                    this.LastName_Text.requestFocus();
                    this.LastName_Text.setError("Last name is required!");
                    return;
                } else {
                    profileEdit();
                    update_Dialog();
                    return;
                }
            case R.id.dob_lay:
                Utility.hideSoftKeyboard(getActivity());
                this.fromDatePickerDialog.show();
                return;
            case R.id.change_tab:
                if (Utility.isEmptyString(this.FirstName_Text.getText().toString())) {
                    this.FirstName_Text.requestFocus();
                    this.FirstName_Text.setError("First name is required!");
                    return;
                } else if (Utility.isEmptyString(this.LastName_Text.getText().toString())) {
                    this.LastName_Text.requestFocus();
                    this.LastName_Text.setError("Last name is required!");
                    return;
                } else {
                    shoDialog(AppConstants.CHANGE_PASSWORD_TEXT);
                    return;
                }
            case R.id.email_toggle_bt:
                update_Dialog();
                return;
            case R.id.push_toggle_bt:
                update_Dialog();
                return;
            default:
                return;
        }
    }

    private void profileEdit() {
        int i = R.drawable.profile_border;
        this.FirstName_Text.setEnabled(this.isUpate);
        this.LastName_Text.setEnabled(this.isUpate);
        this.Dob_lay.setEnabled(false);
        this.FirstName_Text.setBackgroundResource(this.isUpate ? R.drawable.profile_border : R.color.transparent);
        EditText editText = this.LastName_Text;
        if (!this.isUpate) {
            i = R.color.transparent;
        }
        editText.setBackgroundResource(i);
        LayoutParams firstNameParams = (LayoutParams) this.FirstName_Text.getLayoutParams();
        if (this.isUpate) {
            firstNameParams.topMargin = 5;
        } else {
            firstNameParams.topMargin = 0;
        }
        this.FirstName_Text.setLayoutParams(firstNameParams);
        LayoutParams lastNameParams = (LayoutParams) this.LastName_Text.getLayoutParams();
        if (this.isUpate) {
            lastNameParams.topMargin = 5;
        } else {
            lastNameParams.topMargin = 0;
        }
        this.LastName_Text.setLayoutParams(lastNameParams);
    }

    private void update_Dialog() {
        if (this.cd.isConnectingToInternet()) {
            JSONObject update_object = new JSONObject();
            try {
                update_object.put("firstName", this.FirstName_Text.getText().toString());
                update_object.put("lastName", this.LastName_Text.getText().toString());
                update_object.put("email", this.hbha_pref_helper.getStringValue("emailID"));
                update_object.put("emailOptIn", this.email_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                update_object.put("smsOptIn", this.hbha_pref_helper.getStringValue("smsOpted"));
                update_object.put("addressStreet", this.hbha_pref_helper.getStringValue("addressLine1"));
                update_object.put("addressState", this.hbha_pref_helper.getStringValue("addressState"));
                update_object.put("addressCity", this.hbha_pref_helper.getStringValue("addressCity"));
                update_object.put("addressZipCode", this.hbha_pref_helper.getStringValue("addressZip"));
                update_object.put("storeCode", this.hbha_pref_helper.getStringValue("store_code"));
                if (this.Dob_lay.isShown()) {
                    update_object.put("birthDate", this.dateOfBirth_Text.getText().toString());
                }
                update_object.put(PreferenceConstants.PREFERENCE_GENDER, this.hbha_pref_helper.getStringValue("customerGender"));
//                update_object.put("username", Utility.decryptIt(this.hbha_pref_helper.getStringValue("remind_me_user_name"), getString(R.string.app_name) + "^%$#entappia"));
//                update_object.put(EmailAuthProvider.PROVIDER_ID, Utility.decryptIt(this.hbha_pref_helper.getStringValue("remind_me_user_pass"), getString(R.string.app_name) + "^%$#entappia"));
                update_object.put("deviceId", this.hbha_pref_helper.getStringValue("deviceID"));
                update_object.put("pushOptIn", this.push_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                update_object.put("sendWelcomeEmail", "ss");
                if (this.cd.isConnectingToInternet()) {
                    updateMember(update_object);
                    return;
                } else {
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
            } catch (JSONException e) {
                return;
            }
        }
        Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
    }

    private void updateMember(JSONObject update_object) {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserService.sharedInstance().memberUpdate(update_object, new C18032());
    }

    private void shoDialog(String content) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.change_pwd_dialog);
        final EditText curnt_pw_tv = (EditText) dialog.findViewById(R.id.cur_pw_tv);
        final EditText new_pw_tv = (EditText) dialog.findViewById(R.id.new_pw_tv);
        final EditText confrm_pw_tv = (EditText) dialog.findViewById(R.id.confrm_pw_tv);
        ((TextView) dialog.findViewById(R.id.change_head_text)).setText(content);
        final Button Chng_pw_ok = (Button) dialog.findViewById(R.id.chng_pw_ok);
        Button Chng_pw_cancel = (Button) dialog.findViewById(R.id.chng_pw_cancel);
        Chng_pw_ok.setEnabled(false);
        Chng_pw_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str_curnt_Pwd = curnt_pw_tv.getText().toString();
                String strPwd = new_pw_tv.getText().toString();
                String strConfPwd = confrm_pw_tv.getText().toString();
                if (new_pw_tv.getText().toString().length() < 6) {
                    new_pw_tv.requestFocus();
                    new_pw_tv.setError("Please Enter minimum 6 characters");
                } else if (new_pw_tv.getText().toString().length() > 20) {
                    new_pw_tv.requestFocus();
                    new_pw_tv.setError("Please Enter maximum 20 characters");
//                } else if (!str_curnt_Pwd.equals(Utility.decryptIt(UserProfileFragment.this.hbha_pref_helper.getStringValue("remind_me_user_pass"), UserProfileFragment.this.getString(R.string.app_name) + "^%$#entappia"))) {
//                    curnt_pw_tv.requestFocus();
//                    curnt_pw_tv.setError("Invalid current password!");
//                } else if (strPwd.equals(Utility.decryptIt(UserProfileFragment.this.hbha_pref_helper.getStringValue("remind_me_user_pass"), UserProfileFragment.this.getString(R.string.app_name) + "^%$#entappia"))) {
//                    new_pw_tv.requestFocus();
//                    new_pw_tv.setError("New password should not be same as old password!");
//                } else if (!strPwd.equals(strConfPwd)) {
                    confrm_pw_tv.requestFocus();
                    confrm_pw_tv.setError("Passwords do not match!");
                } else if (UserProfileFragment.this.cd.isConnectingToInternet()) {
                    dialog.dismiss();
                    JSONObject change_pwd_object = new JSONObject();
                    try {
                        change_pwd_object.put("firstName", UserProfileFragment.this.FirstName_Text.getText().toString());
                        change_pwd_object.put("lastName", UserProfileFragment.this.LastName_Text.getText().toString());
                        change_pwd_object.put("email", UserProfileFragment.this.email_Text.getText().toString());
                        change_pwd_object.put("emailOptIn", UserProfileFragment.this.email_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                        change_pwd_object.put("pushoptin", UserProfileFragment.this.push_toggle_bt.isChecked() ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false");
                        change_pwd_object.put("storeCode", UserProfileFragment.this.hbha_pref_helper.getStringValue("store_code"));
                        if (UserProfileFragment.this.Dob_lay.isShown()) {
                            change_pwd_object.put("birthDate", UserProfileFragment.this.dateOfBirth_Text.getText().toString());
                        } else {
                            change_pwd_object.put("birthDate", "");
                        }
                        change_pwd_object.put("phone", UserProfileFragment.this.phone_text.getText().toString());
                        change_pwd_object.put(PreferenceConstants.PREFERENCE_GENDER, "");
                       // change_pwd_object.put("oldPassword", Utility.decryptIt(UserProfileFragment.this.hbha_pref_helper.getStringValue("remind_me_user_pass"), UserProfileFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                        change_pwd_object.put(EmailAuthProvider.PROVIDER_ID, new_pw_tv.getText().toString());
                        UserProfileFragment.this.changePassword(change_pwd_object, new_pw_tv.getText().toString());
                    } catch (JSONException e) {
                    }
                } else {
                    Utility.showToast(UserProfileFragment.this.getActivity(), AppConstants.NO_CONNECTION_TEXT);
                }
            }
        });
        final EditText editText = new_pw_tv;
        final EditText editText2 = curnt_pw_tv;
        final EditText editText3 = confrm_pw_tv;
        curnt_pw_tv.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() == 0 || editText2.getText().toString().length() == 0 || editText3.getText().toString().length() == 0) {
                    Chng_pw_ok.setEnabled(false);
                    Chng_pw_ok.setBackgroundResource(R.drawable.my_button_unclickable_border);
                    return;
                }
                Chng_pw_ok.setEnabled(true);
                Chng_pw_ok.setBackgroundResource(R.drawable.my_button_border);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        new_pw_tv.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() == 0 || editText2.getText().toString().length() == 0 || editText3.getText().toString().length() == 0) {
                    Chng_pw_ok.setEnabled(false);
                    Chng_pw_ok.setBackgroundResource(R.drawable.my_button_unclickable_border);
                    return;
                }
                Chng_pw_ok.setEnabled(true);
                Chng_pw_ok.setBackgroundResource(R.drawable.my_button_border);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        confrm_pw_tv.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() == 0 || editText2.getText().toString().length() == 0 || editText3.getText().toString().length() == 0) {
                    Chng_pw_ok.setEnabled(false);
                    Chng_pw_ok.setBackgroundResource(R.drawable.my_button_unclickable_border);
                    return;
                }
                Chng_pw_ok.setEnabled(true);
                Chng_pw_ok.setBackgroundResource(R.drawable.my_button_border);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        Chng_pw_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void changePassword(JSONObject change_pwd_object, final String new_password) {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserService.sharedInstance().changePassword(change_pwd_object, new FBChangePasswordCallback() {
            public void onChangePasswordCallback(JSONObject jsonObject, Exception e) {
                if (jsonObject == null) {
                    return;
                }
                if (!jsonObject.has("successFlag")) {
                    UserProfileFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                    Utility.hideSoftKeyboard(UserProfileFragment.this.getActivity());
                    Utility.tryHandleTokenExpiry(UserProfileFragment.this.getActivity(), e);
                } else if (jsonObject.optBoolean("successFlag")) {
                    Utility.showToast(UserProfileFragment.this.getActivity(), "Password Changed Successfully");
                  //  UserProfileFragment.this.hbha_pref_helper.saveStringValue("remind_me_user_pass", Utility.encryptIt(new_password, UserProfileFragment.this.getString(R.string.app_name) + "^%$#entappia"));
                    UserProfileFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                } else {
                    Utility.hideSoftKeyboard(UserProfileFragment.this.getActivity());
                    Utility.showToast(UserProfileFragment.this.getActivity(), jsonObject.optString("message"));
                }
            }
        });
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }
}
