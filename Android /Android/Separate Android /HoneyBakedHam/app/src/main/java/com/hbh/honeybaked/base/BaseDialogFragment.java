package com.hbh.honeybaked.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import com.hbh.honeybaked.connector.ConnectionDetector;
import com.hbh.honeybaked.helper.HBDBHelper;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.supportingfiles.Utility;

public abstract class BaseDialogFragment extends DialogFragment implements OnClickListener {
    protected ConnectionDetector cd;
    protected HBDBHelper hb_dbHelper;
    protected PreferenceHelper hbha_pref_helper;
    protected int height;
    protected DialogListener mDialogListener;
    protected int orientation;
    protected int width;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.hbha_pref_helper == null) {
            this.hbha_pref_helper = new PreferenceHelper(getActivity());
        }
        this.hb_dbHelper = new HBDBHelper(getActivity());
        this.cd = new ConnectionDetector(getActivity());
        this.hb_dbHelper.openDb();
        int[] heightWidthArray = Utility.getWindowHeightWidth(getActivity());
        this.width = heightWidthArray[0];
        this.height = heightWidthArray[1];
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mDialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mDialogListener = null;
    }

    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }

    public void onClick(View v) {
    }
}
