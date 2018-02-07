package com.identity.arx.progressbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.identity.arx.R;


public class MyCustomProgressDialog extends ProgressDialog {
    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setFlags(16, 16);
        setContentView(R.layout.progressbar);
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }
}
