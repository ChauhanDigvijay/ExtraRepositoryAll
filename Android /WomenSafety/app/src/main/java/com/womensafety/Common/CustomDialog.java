package com.womensafety.Common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.womensafety.R;


public class CustomDialog extends Dialog {
    boolean isCancellable;

    public CustomDialog(Context context, View view) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        this.isCancellable = true;
        requestWindowFeature(1);
        setContentView(view);
    }

    public CustomDialog(Context context, View view, int lpW, int lpH) {
        this(context, view, lpW, lpH, true);
    }

    public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        this.isCancellable = true;
        requestWindowFeature(1);
        setContentView(view, new LayoutParams(lpW, lpH));
        this.isCancellable = isCancellable;
    }

    public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable, int style) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        this.isCancellable = true;
        requestWindowFeature(1);
        setContentView(view, new LayoutParams(lpW, lpH));
        this.isCancellable = isCancellable;
    }

    public void onBackPressed() {
        if (this.isCancellable) {
            super.onBackPressed();
        }
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}
