package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.supportingfiles.Utility;

public class LandingPageFragment extends BaseFragment {
    int EMAIL_RESPONSE_CODE = 101;
    TextView call_us_tv2;
    String ph_no = "appsupport@hbham.com";
    TableRow signin_bt;
    RelativeLayout signin_rl;
    RelativeLayout signup_rl;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_landing_page_layout, container, false);
        this.signup_rl = (RelativeLayout) v.findViewById(R.id.signup_rl);
        this.signin_rl = (RelativeLayout) v.findViewById(R.id.signin_rl);
        this.signup_rl.setOnClickListener(this);
        this.signin_rl.setOnClickListener(this);
        this.hb_dbHelper.openDb();
        this.call_us_tv2 = (TextView) v.findViewById(R.id.call_us_tv2);
        SpannableString content = new SpannableString(this.ph_no);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        this.call_us_tv2.setText(content);
        this.call_us_tv2.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_rl:
              //  clearRegPref();
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SIGNUP_PAGE, Boolean.valueOf(false));
                return;
            case R.id.signin_rl:
              //  clearRegPref();
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SIGNIN_PAGE, Boolean.valueOf(false));
                return;
            case R.id.call_us_tv2:
                if (this.cd.isConnectingToInternet()) {
                    sendMail(this.ph_no);
                    return;
                } else {
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
            default:
                return;
        }
    }

    public void sendMail(String email_id) {
        startActivityForResult(Intent.createChooser(new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", email_id, null)), "Send email..."), this.EMAIL_RESPONSE_CODE);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode != this.EMAIL_RESPONSE_CODE) {
        }
    }

    private void callFunction() {
        if (VERSION.SDK_INT < 23) {
            goToCall();
        } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CALL_PHONE") == 0) {
            goToCall();
        } else if (shouldShowRequestPermissionRationale("android.permission.CALL_PHONE")) {
            Builder builder = new Builder(getActivity());
            builder.setTitle("This app needs call access");
            builder.setMessage("Go to Settings and enable the phone permission");
            builder.show();
        } else {
            requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
        }
    }

    private void goToCall() {
        if (((TelephonyManager) getActivity().getSystemService("phone")).getSimState() != 1) {
            try {
                Intent in = new Intent("android.intent.action.DIAL");
                in.setData(Uri.parse("tel:" + this.ph_no));
                startActivity(in);
                return;
            } catch (ActivityNotFoundException e) {
                Utility.showToast(getActivity(), "No Sim Available");
                return;
            }
        }
        Utility.showToast(getActivity(), "No Sim Available");
    }

    public void clearRegPref() {
        this.hbha_pref_helper.saveStringValue("reg_fnm", "");
        this.hbha_pref_helper.saveStringValue("reg_lnm", "");
        this.hbha_pref_helper.saveStringValue("reg_mail", "");
        this.hbha_pref_helper.saveStringValue("reg_pw", "");
        this.hbha_pref_helper.saveStringValue("reg_dob", "");
        this.hbha_pref_helper.saveStringValue("reg_ph_no", "");
        this.hbha_pref_helper.saveStringValue("reg_conf_pw", "");
        this.hbha_pref_helper.saveStringValue("reg_conf_ph_no", "");
        this.hbha_pref_helper.saveStringValue("reg_store_id", "");
        this.hbha_pref_helper.saveStringValue("reg_store_nm", "");
        this.hbha_pref_helper.saveStringValue("reg_store_add", "");
        this.hbha_pref_helper.saveStringValue("reg_store_city", "");
        this.hbha_pref_helper.saveStringValue("reg_store_ph", "");
        this.hbha_pref_helper.saveIntValue("rule_id", 0);
        this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
        this.hbha_pref_helper.saveBooleanValue("reg_email_option_check", false);
        this.hbha_pref_helper.saveBooleanValue("reg_privacy_terms_check", false);
    }

    private void showStoreAlert() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.logout_alert_dialog_layout);
        Button fpw_cancel = (Button) dialog.findViewById(R.id.fpw_cancel);
        Button fpw_ok = (Button) dialog.findViewById(R.id.fpw_ok);
        ((TextView) dialog.findViewById(R.id.head_text)).setText("For new Registration with HoneyBaked Ham, you need to select your store. Please click on OK for select store.");
        fpw_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LandingPageFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.GETSTOREID_PAGE, Boolean.valueOf(false));
                dialog.dismiss();
            }
        });
        fpw_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
