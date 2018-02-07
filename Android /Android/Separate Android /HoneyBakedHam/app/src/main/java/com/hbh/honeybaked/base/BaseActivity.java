package com.hbh.honeybaked.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.applications.Application;
import com.hbh.honeybaked.connector.ConnectionDetector;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.helper.HBDBHelper;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.listener.FragmentActivityListener;
import com.hbh.honeybaked.listener.ImageEffectListener;

import com.hbh.honeybaked.supportingfiles.Utility;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener, FragmentActivityListener, AdapterListener, DialogListener {
    protected LinearLayout bottom_layout;
    protected ConnectionDetector cd;
    protected HBDBHelper hb_dbHelper;
    protected PreferenceHelper hbha_pref_helper;
    protected ImageView head_back_img_vw;
    protected ImageView head_img_tv;
    protected ImageView head_nav_img_vw;
    protected ImageView head_set_img_vw;
    protected TextView head_title_tv;
    protected ImageView head_user_set_img_vw;
    protected LinearLayout header_bottom_layout;
    protected ImageView header_bottom_left_img_vw;
    protected TextView header_bottom_right_tv;
    protected TextView header_bottom_title_tv;
    protected LinearLayout header_layout;
    protected LinearLayout header_top_layout;

    protected TextView home_folw_txt;
    protected Application myApplication;
    protected int screenHeight;
    protected int screenWidth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hbha_pref_helper = new PreferenceHelper(this);
        this.hb_dbHelper = new HBDBHelper(this);
        this.cd = new ConnectionDetector(getApplicationContext());
        int[] screenSizes = Utility.getWindowHeightWidth(this);
        this.screenWidth = screenSizes[0];
        this.screenHeight = screenSizes[1];
        this.myApplication = (Application) getApplication();
    }

    public void initViews() {
        this.header_layout = (LinearLayout) findViewById(R.id.header_layout);
        this.head_back_img_vw = (ImageView) findViewById(R.id.head_back_img_vw);
        this.head_set_img_vw = (ImageView) findViewById(R.id.head_set_img_vw);
        this.head_title_tv = (TextView) findViewById(R.id.head_title_tv);
        this.header_top_layout = (LinearLayout) findViewById(R.id.header_top_layout);
        this.head_nav_img_vw = (ImageView) findViewById(R.id.head_nav_img_vw);
        this.head_img_tv = (ImageView) findViewById(R.id.head_img_tv);
        this.head_user_set_img_vw = (ImageView) findViewById(R.id.head_user_set_img_vw);
        this.bottom_layout = (LinearLayout) findViewById(R.id.bottom_lay);

        this.home_folw_txt = (TextView) findViewById(R.id.home_fol_txt);
        this.header_bottom_layout = (LinearLayout) findViewById(R.id.header_bottom_layout);
        this.header_bottom_left_img_vw = (ImageView) findViewById(R.id.header_bottom_left_img_vw);
        this.header_bottom_right_tv = (TextView) findViewById(R.id.header_bottom_right_tv);
        this.header_bottom_title_tv = (TextView) findViewById(R.id.header_bottom_title_tv);
        this.header_bottom_right_tv.setPaintFlags(this.header_bottom_right_tv.getPaintFlags() | 8);
        clickListener();
        performAdapterAction(AppConstants.SET_HEADER_VIEW, Boolean.valueOf(true));
        performAdapterAction(AppConstants.SET_BOTTOM_VIEW, Boolean.valueOf(true));
    }

    private void clickListener() {
        this.head_nav_img_vw.setOnClickListener(this);
        this.header_bottom_left_img_vw.setOnClickListener(this);
        this.head_user_set_img_vw.setOnClickListener(this);
        this.header_bottom_right_tv.setOnClickListener(this);
        this.head_img_tv.setOnClickListener(this);
        this.header_bottom_left_img_vw.setOnTouchListener(new ImageEffectListener(getResources().getColor(R.color.ham_selector)));
    }

    public void performAdapterAction(String tagName, Object data) {
        setProgressDialog(tagName, data);
    }

    public void performDialogAction(String tagName, Object data) {
        setProgressDialog(tagName, data);
    }

    public void performFragmentActivityAction(String tagName, Object data) {
        setProgressDialog(tagName, data);
    }

    public void setProgressDialog(String tagName, Object data) {
        if (tagName.equalsIgnoreCase(AppConstants.SHOW_PROGRESS_DIALOG)) {
            if (getSupportFragmentManager().findFragmentByTag("DIALOG_PROGRESS_VIEW") == null) {
                Utility.showProgressFragment(this, ((Boolean) data).booleanValue());
            }
        } else if (tagName.equalsIgnoreCase(AppConstants.HIDE_PROGRESS_DIALOG)) {
            Utility.hideProgressFragment(this);
        }
    }
}
