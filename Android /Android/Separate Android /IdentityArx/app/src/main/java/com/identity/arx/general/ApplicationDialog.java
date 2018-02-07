package com.identity.arx.general;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Typeface;

public class ApplicationDialog {
    private static String NegButtonName;
    private static ApplicationDialog instance = null;
    static Context mContext;
    private static String meaagae;
    private static String posButtonName;
    private static String titlee;
    private static Typeface type;
    OnClickListener negativeButton;

    class C08131 implements OnClickListener {
        C08131() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    static class C08142 implements OnShowListener {
        C08142() {
        }

        public void onShow(DialogInterface arg0) {
        }
    }

    private ApplicationDialog() {
    }

    public static ApplicationDialog setMessage(String title, String message, String positiveButtonName, String nagativeButtonName, Context mmContext) {
        mContext = mmContext;
        titlee = title;
        meaagae = message;
        posButtonName = positiveButtonName;
        NegButtonName = nagativeButtonName;
        if (instance == null) {
            instance = new ApplicationDialog();
        }
        return instance;
    }

    public void buildDialog(OnClickListener positiveButton) {
        createDialog(positiveButton, this.negativeButton);
        this.negativeButton = new C08131();
    }

    public void buildDialog(OnClickListener positiveButton, OnClickListener negativeButtonn) {
        this.negativeButton = negativeButtonn;
        createDialog(positiveButton, this.negativeButton);
    }

    private static void createDialog(OnClickListener positiveButton, OnClickListener negativeButton) {
        Builder builder = new Builder(mContext);
        builder.setMessage(meaagae).setCancelable(false).setPositiveButton(posButtonName, positiveButton).setNegativeButton(NegButtonName, negativeButton);
        AlertDialog alert = builder.create();
        alert.setOnShowListener(new C08142());
        alert.show();
    }
}
