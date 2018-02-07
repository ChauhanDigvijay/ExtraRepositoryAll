package com.fishbowl.LoyaltyTabletApp.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.fishbowl.LoyaltyTabletApp.R;

/**
 * Created by mohdvaseem on 31/05/16.
 */

public class ProgressBarHandler extends Dialog {
    private ProgressBar mProgressBar;
    private Context mContext;

    public ProgressBarHandler(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(R.layout.customdialog);
    }


    @Override
    public void show()
    {
        super.show();

    }
}