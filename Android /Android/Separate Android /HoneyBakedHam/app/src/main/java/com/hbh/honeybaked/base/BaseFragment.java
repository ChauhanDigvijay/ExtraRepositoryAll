package com.hbh.honeybaked.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;
import com.hbh.honeybaked.connector.ConnectionDetector;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.helper.HBDBHelper;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.listener.FragmentActivityListener;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

public abstract class BaseFragment extends Fragment implements OnClickListener, AdapterListener, DialogListener {
    protected AdapterListener adapterListener;
    protected ConnectionDetector cd;
    protected FragmentActivityListener fragmentActivityListener;
    protected HBDBHelper hb_dbHelper;
    protected PreferenceHelper hbha_pref_helper;
    protected String menu_name;
    protected int screenHeight;
    protected int screenWidth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hbha_pref_helper = new PreferenceHelper(getActivity());
        this.hb_dbHelper = new HBDBHelper(getActivity());
        this.cd = new ConnectionDetector(getActivity());
        this.fragmentActivityListener = (FragmentActivityListener) getActivity();
        this.adapterListener = (AdapterListener) getActivity();
        int[] screenSizes = Utility.getWindowHeightWidth(getActivity());
        this.screenWidth = screenSizes[0];
        this.screenHeight = screenSizes[1];
        if (getArguments() != null) {
            MenuModel mMenuModel = (MenuModel) getArguments().getSerializable("MenuModel");
            if (!Utility.isEmpty(mMenuModel)) {
                this.menu_name = mMenuModel.getMenu_title();
            }
        }
    }

    public void initViews() {
        clickListener();
        performAdapterAction(AppConstants.SET_HEADER_VIEW, Boolean.valueOf(true));
        performAdapterAction(AppConstants.SET_BOTTOM_VIEW, Boolean.valueOf(true));
    }

    private void clickListener() {
    }

    public void performAdapterAction(String tagName, Object data) {
        setProgressDialog(tagName, data);
    }

    public void performDialogAction(String tagName, Object data) {
        setProgressDialog(tagName, data);
    }

    public void setProgressDialog(String tagName, Object data) {
        if (tagName.equalsIgnoreCase(AppConstants.SHOW_PROGRESS_DIALOG)) {
            if (getActivity().getSupportFragmentManager().findFragmentByTag("DIALOG_PROGRESS_VIEW") == null) {
                Utility.showProgressFragment(getActivity(), ((Boolean) data).booleanValue());
            }
        } else if (tagName.equalsIgnoreCase(AppConstants.HIDE_PROGRESS_DIALOG)) {
            Utility.hideProgressFragment(getActivity());
        }
    }
}
