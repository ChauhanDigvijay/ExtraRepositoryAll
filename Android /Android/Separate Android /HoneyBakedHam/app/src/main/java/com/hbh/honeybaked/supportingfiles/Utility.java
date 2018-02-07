package com.hbh.honeybaked.supportingfiles;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fishbowl.basicmodule.Utils.FBUtils;
import com.hbh.honeybaked.activity.LoginMainActivity;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.dialogs.ProgressDialogFragment;
import com.hbh.honeybaked.helper.HBDBHelper;
import com.hbh.honeybaked.helper.PreferenceHelper;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utility {
    public static boolean isUtilityProgress = false;
    public static ImageLoader mImageLoader;
    public static RequestQueue mRequestQueue;

    public static boolean isEmpty(Object data) {
        return data == null;
    }

    public static boolean isEmptyString(String value) {
        return TextUtils.isEmpty(value) || value.equalsIgnoreCase("null");
    }

    public static final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void showToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }

    public static String getStringValue(JSONObject jsonObject, String key) {
        String value = "";
        if (jsonObject == null || key == null || !jsonObject.has(key) || isEmptyString(jsonObject.optString(key))) {
            return value;
        }
        return jsonObject.optString(key);
    }

    public static int getIntegerValue(JSONObject jsonObject, String key) {
        if (jsonObject == null || key == null || !jsonObject.has(key) || isEmptyString(jsonObject.optString(key))) {
            return 0;
        }
        return jsonObject.optInt(key);
    }

    public static double getDoubleValue(JSONObject jsonObject, String key) {
        if (jsonObject == null || key == null || !jsonObject.has(key) || isEmptyString(jsonObject.optString(key))) {
            return 0.0d;
        }
        return jsonObject.optDouble(key);
    }

    public static boolean getBooleanValue(JSONObject jsonObject, String key) {
        if (jsonObject == null || key == null || !jsonObject.has(key) || !jsonObject.optBoolean(key)) {
            return false;
        }
        return jsonObject.optBoolean(key);
    }

    public static int[] getWindowHeightWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return new int[]{display.getWidth(), display.getHeight()};
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public static ImageLoader getImageLoader(Context context) {
        getRequestQueue(context);
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
        }
        return mImageLoader;
    }

    public static void loadImagesToView(Context context, String menu_icon, final View help_img, final int loader) {
        ImageLoader simpleImageLoader = getImageLoader(context);
        if (!isEmptyString(menu_icon)) {
            try {
                simpleImageLoader.get(menu_icon, new ImageListener() {
                    public void onResponse(ImageContainer imageContainer, boolean b) {
                        Bitmap rBitamp = imageContainer.getBitmap();
                        if (rBitamp != null) {
                            if (help_img instanceof ImageView) {
                                ((ImageView) help_img).setImageDrawable(new BitmapDrawable(rBitamp));
                            } else if (help_img instanceof LinearLayout) {
                                ((LinearLayout) help_img).setBackgroundDrawable(new BitmapDrawable(rBitamp));
                            } else if (help_img instanceof RelativeLayout) {
                                ((RelativeLayout) help_img).setBackgroundDrawable(new BitmapDrawable(rBitamp));
                            }
                            help_img.invalidate();
                            if (rBitamp.isRecycled()) {
                                rBitamp.recycle();
                            }
                        }
                    }

                    public void onErrorResponse(VolleyError volleyError) {
                        if (help_img instanceof ImageView) {
                            try {
                                ((ImageView) help_img).setImageResource(loader);
                            } catch (Exception e) {
                                ((ImageView) help_img).setImageResource(0);
                            }
                        } else if (help_img instanceof LinearLayout) {
                            try {
                                ((LinearLayout) help_img).setBackgroundResource(loader);
                            } catch (Exception e2) {
                                ((LinearLayout) help_img).setBackgroundColor(loader);
                            }
                        } else if (help_img instanceof RelativeLayout) {
                            try {
                                ((RelativeLayout) help_img).setBackgroundResource(loader);
                            } catch (Exception e3) {
                                ((RelativeLayout) help_img).setBackgroundColor(loader);
                            }
                        }
                    }
                });
            } catch (OutOfMemoryError e) {
                VolleyLog.e("OutOfMemoryError in performRequest", new Object[0]);
            }
        } else if (help_img instanceof ImageView) {
            ((ImageView) help_img).setImageResource(loader);
        } else if (help_img instanceof LinearLayout) {
            try {
                ((LinearLayout) help_img).setBackgroundResource(loader);
            } catch (Exception e2) {
                ((LinearLayout) help_img).setBackgroundColor(loader);
            }
        } else if (help_img instanceof RelativeLayout) {
            try {
                ((RelativeLayout) help_img).setBackgroundResource(loader);
            } catch (Exception e3) {
                ((RelativeLayout) help_img).setBackgroundColor(loader);
            }
        }
    }

    public static void showProgressFragment(FragmentActivity fragmentActivity, boolean cancelable) {
        removeDialogFragment(fragmentActivity, "DIALOG_PROGRESS_VIEW");
        ProgressDialogFragment.newInstance(cancelable, false).show(fragmentActivity.getSupportFragmentManager(), "DIALOG_PROGRESS_VIEW");
        isUtilityProgress = true;
    }

    public static void hideProgressFragment(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            Fragment prev = fragmentActivity.getSupportFragmentManager().findFragmentByTag("DIALOG_PROGRESS_VIEW");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                if (!(df == null || df.getDialog() == null)) {
                    df.getDialog().dismiss();
                }
            }
            isUtilityProgress = false;
        }
    }

    public static void removeDialogFragment(FragmentActivity fragmentActivity, String sDialogTagName) {
        Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(sDialogTagName);
        if (!isEmpty(fragment)) {
            FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment).commit();
            fragmentTransaction.setTransition(8194);
        }
    }

//    public static void lockScreenOrientation(FragmentActivity mActivity) {
//        if (mActivity == null) {
//            return;
//        }
//        if (mActivity.getResources().getBoolean(R.bool.isTablet)) {
//            switch (mActivity.getWindowManager().getDefaultDisplay().getRotation()) {
//                case 0:
//                    mActivity.setRequestedOrientation(14);
//                    return;
//                case 1:
//                    mActivity.setRequestedOrientation(0);
//                    return;
//                case 2:
//                    mActivity.setRequestedOrientation(9);
//                    return;
//                case 3:
//                    mActivity.setRequestedOrientation(8);
//                    return;
//                default:
//                    mActivity.setRequestedOrientation(1);
//                    return;
//            }
//        }
//        mActivity.setRequestedOrientation(1);
//    }

//    public static void releaseLockScreenOrientation(FragmentActivity mActivity) {
//        if (mActivity == null) {
//            return;
//        }
//        if (mActivity.getResources().getBoolean(R.bool.isTablet)) {
//            mActivity.setRequestedOrientation(10);
//        } else {
//            mActivity.setRequestedOrientation(1);
//        }
//    }

    public static void hideSoftKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(Activity context, EditText editText) {
        if (context.getCurrentFocus() != null) {
            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

//    public static String encryptIt(String value, String cryptoPass) {
//        try {
//            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(cryptoPass.getBytes(UrlUtils.UTF8)));
//            byte[] clearText = value.getBytes(UrlUtils.UTF8);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(1, key);
//            return Base64.encodeToString(cipher.doFinal(clearText), 0);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//            return value;
//        } catch (UnsupportedEncodingException e2) {
//            e2.printStackTrace();
//            return value;
//        } catch (InvalidKeySpecException e3) {
//            e3.printStackTrace();
//            return value;
//        } catch (NoSuchAlgorithmException e4) {
//            e4.printStackTrace();
//            return value;
//        } catch (BadPaddingException e5) {
//            e5.printStackTrace();
//            return value;
//        } catch (NoSuchPaddingException e6) {
//            e6.printStackTrace();
//            return value;
//        } catch (IllegalBlockSizeException e7) {
//            e7.printStackTrace();
//            return value;
//        }
//    }

//    public static String decryptIt(String value, String cryptoPass) {
//        try {
//            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(cryptoPass.getBytes(UrlUtils.UTF8)));
//            byte[] encrypedPwdBytes = Base64.decode(value, 0);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(2, key);
//            return new String(cipher.doFinal(encrypedPwdBytes));
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e2) {
//            e2.printStackTrace();
//        } catch (InvalidKeySpecException e3) {
//            e3.printStackTrace();
//        } catch (NoSuchAlgorithmException e4) {
//            e4.printStackTrace();
//        } catch (BadPaddingException e5) {
//            e5.printStackTrace();
//        } catch (NoSuchPaddingException e6) {
//            e6.printStackTrace();
//        } catch (IllegalBlockSizeException e7) {
//            e7.printStackTrace();
//        }
//        return value;
//    }

    public static String getErrorDescription(Exception exception) {
        String errorDescription = "";
        if (exception != null) {
            if ((exception instanceof NetworkError) || (exception instanceof TimeoutError)) {
                errorDescription = "Please check your network connection and try again.";
            } else if (exception instanceof VolleyError) {
                NetworkResponse response = ((VolleyError) exception).networkResponse;
                if (!(response == null || response.data == null)) {
                    errorDescription = new String(response.data);
                }
            } else {
                errorDescription = exception.getMessage();
            }
        }
        if (errorDescription == null || errorDescription.equals("")) {
            return "Some error occurred while performing your request.";
        }
        return errorDescription;
    }

    public static void tryHandleTokenExpiry(Activity activity, Exception exception) {
        String message = FBUtils.getErrorDescription(exception);
        if (message.equalsIgnoreCase("Invalid access token")) {
            showHBHErrorAlert(activity, message, true);
        } else if (message.equalsIgnoreCase("Invalid Email/Phone Number or Password") || message.equalsIgnoreCase("Invalid Username")) {
            showHBHErrorAlert(activity, "Invalid Phone Number or Password", false);
        } else if (message.equalsIgnoreCase("Something went wrong")) {
            showHBHErrorAlert(activity, "So sorry - something went wrong. Please try again later.", true);
        } else {
            FBUtils.showErrorAlert(activity, exception);
        }
    }

    public static void showHBHErrorAlert(final Activity activity, String mes_, final boolean redriection) {
        Builder alertdialog = new Builder(activity);
        alertdialog.setTitle("Error");
        alertdialog.setCancelable(false);
        alertdialog.setMessage(mes_);
        alertdialog.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PreferenceHelper hbha_pref_helper = new PreferenceHelper(activity);
                HBDBHelper hb_dbHelper = new HBDBHelper(activity);
                hbha_pref_helper.saveIntValue("login_flag", 0);
                hb_dbHelper.openDb();
                hbha_pref_helper.saveStringValue("reg_fnm", "");
                hbha_pref_helper.saveStringValue("reg_lnm", "");
                hbha_pref_helper.saveStringValue("reg_mail", "");
                hbha_pref_helper.saveStringValue("reg_pw", "");
                hbha_pref_helper.saveStringValue("reg_dob", "");
                hbha_pref_helper.saveStringValue("reg_ph_no", "");
                hbha_pref_helper.saveStringValue("reg_conf_pw", "");
                hbha_pref_helper.saveStringValue("reg_conf_ph_no", "");
                hbha_pref_helper.saveStringValue("reg_store_id", "");
                hbha_pref_helper.saveStringValue("reg_store_nm", "");
                hbha_pref_helper.saveStringValue("reg_store_add", "");
                hbha_pref_helper.saveStringValue("reg_store_city", "");
                hbha_pref_helper.saveStringValue("reg_store_ph", "");
                hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME, "");
                hbha_pref_helper.saveBooleanValue("last_reg_ph_no_flg", false);
                hbha_pref_helper.saveIntValue("rule_id", 0);
                hbha_pref_helper.saveBooleanValue("reward_rule", false);
                hbha_pref_helper.saveBooleanValue("reg_email_option_check", false);
                hbha_pref_helper.saveBooleanValue("reg_privacy_terms_check", false);
                hb_dbHelper.deleteTable("hbha_offer_table");
                hb_dbHelper.deleteTable("hbha_reward_table");
                if (redriection) {
                    activity.startActivity(new Intent(activity, LoginMainActivity.class));
                    activity.finish();
                }
            }
        });

        alertdialog.show();
    }

    public static String convertToUsFormat(String sMobileNo) {
        if (isEmptyString(sMobileNo)) {
            return sMobileNo;
        }
        sMobileNo = sMobileNo.replaceAll("-", "").replaceAll("\\(", "").replaceAll("\\)", "").trim();
        if (sMobileNo.length() == 10) {
            return "(" + sMobileNo.substring(0, 3) + ") " + sMobileNo.substring(3, 6) + "-" + sMobileNo.substring(6, 10);
        }
        return sMobileNo;
    }

    public static long getDateDifferent(String inputString1) {
        if (!isEmptyString(inputString1)) {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy");
            String inputString2 = myFormat.format(Long.valueOf(new Date().getTime()));
            try {
                return TimeUnit.DAYS.convert(myFormat.parse(inputString1).getTime() - myFormat.parse(inputString2).getTime(), TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String getStoreValues(String sKeyValue, String sStoreFullName) {
        if (isEmptyString(sKeyValue)) {
            return sStoreFullName;
        }
        if (isEmptyString(sStoreFullName)) {
            return sKeyValue.trim();
        }
        if (sStoreFullName.trim().endsWith(",")) {
            return sStoreFullName + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + sKeyValue.trim();
        }
        return sStoreFullName + ", " + sKeyValue.trim();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, boolean value) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), Integer.MIN_VALUE);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (value) {
                totalHeight = (int) (((double) totalHeight) * 0.1d);
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
