package com.hbh.honeybaked.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.internal.ShareConstants;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseActivity;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.fragment.GetStoreIdFragment;
import com.hbh.honeybaked.fragment.LandingPageFragment;
import com.hbh.honeybaked.fragment.LoginFragment;
import com.hbh.honeybaked.fragment.SignUpFragment;
import com.hbh.honeybaked.supportingfiles.Utility;

public class LoginMainActivity extends BaseActivity {
    String current = "";
    private DrawerLayout mDrawerLayout;
    String previous = "";
    String previouscurrent = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initViews();
        performFragmentActivityAction(AppConstants.LANDING_PAGE, Boolean.valueOf(true));
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerLayout.setDrawerLockMode(1);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_bottom_left_img_vw:
                goBack();
                return;
            default:
                return;
        }
    }

    private void goBack() {
        Utility.hideSoftKeyboard(this);
        if (this.current.equalsIgnoreCase(AppConstants.SIGNUP_PAGE) && this.previous.equalsIgnoreCase(AppConstants.GETSTOREID_PAGE)) {
            performFragmentActivityAction(AppConstants.SIGNUP_PAGE, Boolean.valueOf(true));
        } else if (this.current.equalsIgnoreCase(AppConstants.SIGNIN_PAGE) || this.current.equalsIgnoreCase(AppConstants.SIGNUP_PAGE)) {
            if (this.current.equalsIgnoreCase(AppConstants.SIGNUP_PAGE) && this.previouscurrent.equalsIgnoreCase(AppConstants.SIGNIN_PAGE)) {
                performFragmentActivityAction(AppConstants.SIGNIN_PAGE, Boolean.valueOf(true));
            } else {
                performFragmentActivityAction(AppConstants.LANDING_PAGE, Boolean.valueOf(false));
            }
        } else if (this.current.equalsIgnoreCase(AppConstants.LANDING_PAGE)) {
            shoDialog(AppConstants.EXIT_TEXT, false);
        }
    }

    private void shoDialog(String exitText, final boolean b) {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.logout_alert_dialog_layout);
        Button fpw_cancel = (Button) dialog.findViewById(R.id.fpw_cancel);
        Button fpw_ok = (Button) dialog.findViewById(R.id.fpw_ok);
        ((TextView) dialog.findViewById(R.id.head_text)).setText(exitText);
        fpw_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (b) {
                    LoginMainActivity.this.hbha_pref_helper.saveIntValue("login_flag", 0);
                    LoginMainActivity.this.startActivity(new Intent(LoginMainActivity.this, LoginMainActivity.class));
                    LoginMainActivity.this.finish();
                    return;
                }
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(268435456);
                //intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                intent.putExtra("EXIT", true);
                LoginMainActivity.this.startActivity(intent);
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

    public void onBackPressed() {
        goBack();
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    private void setHeaderView() {
    }

    public void performFragmentActivityAction(String tagName, Object data) {
        if (this.current.equalsIgnoreCase(AppConstants.SIGNIN_PAGE) && tagName.equalsIgnoreCase(AppConstants.SIGNUP_PAGE)) {
            this.previouscurrent = AppConstants.SIGNIN_PAGE;
        }
        if (!tagName.equalsIgnoreCase(AppConstants.GETSTOREID_PAGE)) {
            this.current = tagName;
        }
        if (tagName.equals(AppConstants.SET_HEADER_VIEW)) {
            setHeaderView();
        } else if (tagName.equals(AppConstants.LANDING_PAGE)) {
            this.header_top_layout.setVisibility(View.VISIBLE);
            this.header_bottom_layout.setVisibility(View.VISIBLE);
            LandingPageFragment landingPageFragment = new LandingPageFragment();
            if (((Boolean) data).booleanValue()) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, landingPageFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, landingPageFragment).commit();
            }
        } else if (tagName.equals(AppConstants.SIGNIN_PAGE)) {
            this.head_user_set_img_vw.setVisibility(View.VISIBLE);
            this.head_nav_img_vw.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, new LoginFragment()).commit();
            this.header_top_layout.setVisibility(View.VISIBLE);
            this.header_bottom_layout.setVisibility(View.VISIBLE);
            this.header_bottom_right_tv.setVisibility(View.VISIBLE);
            this.header_bottom_title_tv.setText("SIGN IN");
        } else if (tagName.equals(AppConstants.SIGNUP_PAGE)) {
            if (this.previous != "") {
                this.current = AppConstants.SIGNUP_PAGE;
                this.previous = "";
            }
            this.head_user_set_img_vw.setVisibility(View.VISIBLE);
            this.head_nav_img_vw.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, new SignUpFragment()).commit();
            this.header_top_layout.setVisibility(View.VISIBLE);
            this.header_bottom_layout.setVisibility(View.VISIBLE);
            this.header_bottom_right_tv.setVisibility(View.VISIBLE);
            this.header_bottom_title_tv.setText("SIGN UP");
        } else if (tagName.equals(AppConstants.GETSTOREID_PAGE)) {
            this.previous = tagName;
            this.header_top_layout.setVisibility(View.VISIBLE);
            this.header_bottom_layout.setVisibility(View.VISIBLE);
            this.header_bottom_right_tv.setVisibility(View.VISIBLE);
            this.header_bottom_title_tv.setText("SELECT STORE");
            this.head_user_set_img_vw.setVisibility(View.VISIBLE);
            this.head_nav_img_vw.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, new GetStoreIdFragment()).commit();
        }
    }

    protected void onNewIntent(Intent intent) {
        if (!intent.getScheme().equalsIgnoreCase("terms") && !intent.getScheme().equalsIgnoreCase(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)) {
            super.onNewIntent(intent);
        }
    }
}
