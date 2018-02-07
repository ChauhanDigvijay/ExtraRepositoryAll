package com.womensafety;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.womensafety.Common.CustomDialog;
import com.womensafety.Common.NetworkUtility;
import com.womensafety.Common.Preference;
import com.womensafety.httpimage.HttpImageManager;

import java.text.DecimalFormat;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    public static float px;
    public DecimalFormat deffLoc;
    public LayoutInflater inflater;
    public LinearLayout llBody;
    public Preference preference;
    public InputFilter speFilter = new C06803();
    private String blockCharacterSet = "~#^|$%&*!";
    private CustomDialog customDialog;
    private ProgressDialog progressdialog;
    private Toast toast;

    public abstract void initialize();

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.base);
        this.llBody = (LinearLayout) findViewById(R.id.llBody);
        this.inflater = getLayoutInflater();
        this.preference = new Preference(this);
        this.deffLoc = new DecimalFormat("##.#######");
        this.deffLoc.setMinimumFractionDigits(6);
        this.deffLoc.setMaximumFractionDigits(6);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
        initialize();
        setTypeFace(this.llBody);
    }

    public void showLoader(String msg) {
        runOnUiThread(new RunShowLoader(msg, ""));
    }

    public void showLoader(String msg, String title) {
        runOnUiThread(new RunShowLoader(msg, title));
    }

    public void hideLoader() {
        runOnUiThread(new C06781());
    }

    public void onDialogClick(String mssg) {
    }

    public void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        hideLoader();
        super.onDestroy();
    }

    public void hideKeyBoard(View v) {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isNetworkConnectionAvailable(Context context) {
        return NetworkUtility.isNetworkConnectionAvailable(context);
    }

    public void disableView(View view) {
        view.setClickable(false);
        view.setEnabled(false);
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    public void setActionOnView(View view, boolean action) {
        view.setClickable(action);
        view.setEnabled(action);
        view.setFocusable(action);
        view.setFocusableInTouchMode(action);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    public void enableView(View view) {
        view.setClickable(true);
        view.setEnabled(true);
    }

    public void setTypeFace(ViewGroup group) {
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = group.getChildAt(i);
            if ((v instanceof TextView) || (v instanceof Button) || (v instanceof EditText)) {
                ((TextView) v).setTypeface(Typeface.DEFAULT);
            } else if (v instanceof ViewGroup) {
                setTypeFace((ViewGroup) v);
            }
        }
    }

    public void disableViewWithHandler(final View view) {
        view.setEnabled(false);
        view.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
                view.setClickable(true);
            }
        }, 600);
    }

    private StringBuffer getDeviceInfo() {
        StringBuffer message = new StringBuffer();
        TelephonyManager mngr = (TelephonyManager) getSystemService("phone");
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        message.append("IMEI: ").append(mngr.getDeviceId()).append('\n');
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(getPackageName());
        }
        message.append("Phone Model: ").append(Build.MODEL).append('\n');
        message.append("Android Version: ").append(VERSION.RELEASE).append('\n');
        message.append("Board: ").append(Build.BOARD).append('\n');
        message.append("Brand: ").append(Build.BRAND).append('\n');
        message.append("Device: ").append(Build.DEVICE).append('\n');
        message.append("Host: ").append(Build.HOST).append('\n');
        message.append("ID: ").append(Build.ID).append('\n');
        message.append("Model: ").append(Build.MODEL).append('\n');
        message.append("Product: ").append(Build.PRODUCT).append('\n');
        message.append("Type: ").append(Build.TYPE).append('\n');
        return message;
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @SuppressLint({"ShowToast"})
            public void run() {
                if (BaseActivity.this.toast != null) {
                    BaseActivity.this.toast.cancel();
                }
                BaseActivity.this.toast = Toast.makeText(BaseActivity.this, message, 0);
                BaseActivity.this.toast.setGravity(1, 0, 0);
                BaseActivity.this.toast.show();
            }
        });
    }

    public void setMaxLength(EditText editText, int length) {
        editText.setFilters(new InputFilter[]{new LengthFilter(length)});
    }

    protected void topBarMenuClick(final LinearLayout llFirst, final LinearLayout llSecond) {
        llFirst.clearAnimation();
        llSecond.clearAnimation();
        if (llFirst.isShown()) {
            Animation animationBody1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            Animation animationBody2 = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            animationBody2.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    llFirst.setVisibility(View.VISIBLE);
                    llSecond.setVisibility(View.VISIBLE);
                    llFirst.clearAnimation();
                    llSecond.clearAnimation();
                }
            });
            llFirst.setAnimation(animationBody1);
            llSecond.setAnimation(animationBody2);
            return;
        }
        Animation animationBody1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation   animationBody2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        llSecond.setAnimation(animationBody1);
        llFirst.setAnimation(animationBody2);
        llFirst.setVisibility(View.VISIBLE);
        llSecond.setVisibility(View.VISIBLE);
    }

    public void showDialog(String title, String message, String fButton, String sButton, final int TYPE, boolean isCancelable) {
        Builder alert = new Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(isCancelable);
        if (!TextUtils.isEmpty(fButton)) {
            alert.setPositiveButton(fButton, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BaseActivity.this.onFButotnClicked(TYPE);
                }
            });
        }
        if (!TextUtils.isEmpty(sButton)) {
            alert.setNegativeButton(sButton, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BaseActivity.this.onSButotnClicked(TYPE);
                }
            });
        }
        alert.create().show();
    }

    public void onFButotnClicked(int TYPE) {
    }

    public void onSButotnClicked(int TYPE) {
    }

    public Bitmap displayImage(String imageURL, ImageView ivItem) {
        if (imageURL.contains(" ")) {
            imageURL = imageURL.replace(" ", "%20");
        }
        Uri uri = Uri.parse(imageURL);
        Bitmap bitmap = null;
        if (uri != null) {
            bitmap = getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, ivItem, imageURL));
            if (bitmap != null) {
                ivItem.setImageBitmap(bitmap);
            }
        }
        return bitmap;
    }

    private HttpImageManager getHttpImageManager() {
        return ((MyApplication) getApplication()).getHttpImageManager();
    }

    class C06781 implements Runnable {
        C06781() {
        }

        public void run() {
            try {
                if (BaseActivity.this.progressdialog != null && BaseActivity.this.progressdialog.isShowing()) {
                    BaseActivity.this.progressdialog.dismiss();
                }
                BaseActivity.this.progressdialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C06803 implements InputFilter {
        C06803() {
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source == null || !BaseActivity.this.blockCharacterSet.contains("" + source)) {
                return null;
            }
            return "";
        }
    }

    class RunShowLoader implements Runnable {
        private String strMsg;
        private String title;

        public RunShowLoader(String strMsg, String title) {
            this.strMsg = strMsg;
            this.title = title;
        }

        public void run() {
            try {
                if (BaseActivity.this.progressdialog == null || !(BaseActivity.this.progressdialog == null || BaseActivity.this.progressdialog.isShowing())) {
                    BaseActivity.this.progressdialog = ProgressDialog.show(BaseActivity.this, this.title, this.strMsg);
                } else if (BaseActivity.this.progressdialog == null || (BaseActivity.this.progressdialog != null && BaseActivity.this.progressdialog.isShowing())) {
                    BaseActivity.this.progressdialog.setMessage(this.strMsg);
                }
            } catch (Exception e) {
                BaseActivity.this.progressdialog = null;
            }
        }
    }
}
