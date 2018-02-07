//package com.fishbowl.basicmodule.SavePay;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.net.Uri;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//
//import com.fishbowl.basicmodule.R;
//import com.fishbowl.basicmodule.Services.FBGiftService;
//import com.fishbowl.basicmodule.Utils.Config;
//import com.fishbowl.basicmodule.Utils.FBUtils;
//import com.fishbowl.basicmodule.Utils.ProgressBarHandler;
//import com.fishbowl.basicmodule.Utils.StringUtilities;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Digvijay Chauhan .
// */
//
//public class SaveUpdateGiftWidget extends LinearLayout implements View.OnClickListener{
//
//
//    public static final int SAVE_TO_ANDROID = 888;
//
//    private static final String TAG = "SaveUpdateGiftWidget.java";
//    private String SUCCESS_RESPONSE_TEXT;
//    private String CANCELED_RESPONSE_TEXT;
//    private String ERROR_PREFIX_TEXT;
//    JSONObject expectedjson;
//    Activity activity;
//    ProgressBarHandler progressBarHandler;
//    ImageButton savetowallet,updatetowallet;
//    public SaveUpdateGiftWidget(Context context) {
//        super(context);
//    }
//
//    public SaveUpdateGiftWidget(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public SaveUpdateGiftWidget(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public void initSaveUpdateGiftWidget(Activity activity, JSONObject expectedjson) {
//        this. activity= activity;
//        this.expectedjson=expectedjson;
//        inflateSaveUpdateGiftWidget( activity);
//    }
//
//    private void inflateSaveUpdateGiftWidget(Activity activity) {
//
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflater.inflate(R.layout.activity_saveupdate, this);
//        progressBarHandler = new ProgressBarHandler(activity);
//        savetowallet = (ImageButton) v.findViewById(R.id.saveToAndroid);
//        updatetowallet = (ImageButton) v.findViewById(R.id.updateToAndroid);
//        savetowallet.setOnClickListener(this);
//        updatetowallet.setOnClickListener(this);
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        inflateSaveUpdateGiftWidget(activity);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.saveToAndroid) {
//            progressBarHandler.show();
//            saveToAndroidPay(v);
//        }
//        else if(v.getId() == R.id.updateToAndroid)
//        {
//            progressBarHandler.show();
//            updateToAndroidPay(v);
//        }
//
//    }
//    public void saveToAndroidPay(View view) {
//
//        FBGiftService.sharedInstance().getGiftAndroidPayJWT(expectedjson, new FBGiftService.FBGetSaveGiftJWTokenCallback() {
//            @Override
//            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {
//
//                if (error == null && response != null) {
//                    progressBarHandler.hide();
//                    try {
//                        boolean successFlag = response.getBoolean("successFlag");
//                        String message = response.getString("message");
//
//                        if (successFlag) {
//                            String jwtToken = response.getString("jwtToken");
//                            Intent savepay = new Intent(Config.SAVEPAY);
//                            savepay.putExtra("message", message);
//                            savepay.putExtra("successFlag", successFlag);
//                            savepay.putExtra("jwtToken", jwtToken);
//                            LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/payapp/savetoandroidpay/" + jwtToken));
//                            activity.startActivity(intent);
//                        } else {
//                            if (StringUtilities.isValidString(message)) {
//                                Intent savepay = new Intent(Config.SAVEPAY);
//                                savepay.putExtra("message", message);
//                                savepay.putExtra("successFlag", successFlag);
//                                LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
//                            } else {
//
//                                String staticmessage = "Failed To load in AndroidPay";
//                                Intent savepay = new Intent(Config.SAVEPAY);
//                                savepay.putExtra("message", message);
//                                savepay.putExtra("successFlag", successFlag);
//                                LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        progressBarHandler.hide();
//                        e.printStackTrace();
//                    }
//                } else {
//                    progressBarHandler.hide();
//                    boolean successFlag = false;
//                    String message = FBUtils.getErrorDescription(error);
//                    Intent savepay = new Intent(Config.SAVEPAY);
//                    savepay.putExtra("message", message);
//                    savepay.putExtra("successFlag", successFlag);
//                    LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
//
//                }
//            }
//        });
//    }
//
//    public void updateToAndroidPay(View view) {
//
//        FBGiftService.sharedInstance().updateGiftAndroidPay(expectedjson, new FBGiftService.FBGetSaveGiftJWTokenCallback() {
//            @Override
//            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {
//
//                if (error == null && response != null) {
//                    try {
//                        progressBarHandler.hide();
//                        boolean successFlag = response.getBoolean("successFlag");
//                        String message = response.getString("message");
//                        Intent updatesavepay = new Intent(Config.UPDATESAVEPAY);
//                        updatesavepay.putExtra("message", message);
//                        updatesavepay.putExtra("successFlag", successFlag);
//                        LocalBroadcastManager.getInstance(activity).sendBroadcast(updatesavepay);
//
//                    } catch (JSONException e) {
//                        progressBarHandler.hide();
//                        e.printStackTrace();
//                    }
//                } else {
//                    progressBarHandler.hide();
//                    boolean successFlag = false;
//                    String message = FBUtils.getErrorDescription(error);
//                    Intent savepay = new Intent(Config.SAVEPAY);
//                    savepay.putExtra("message", message);
//                    savepay.putExtra("successFlag", successFlag);
//                    LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
//                   // FBUtils.tryHandleTokenExpiry(activity, error);
//                }
//            }
//        });
//    }
//
//
//
//
//
//
//}
