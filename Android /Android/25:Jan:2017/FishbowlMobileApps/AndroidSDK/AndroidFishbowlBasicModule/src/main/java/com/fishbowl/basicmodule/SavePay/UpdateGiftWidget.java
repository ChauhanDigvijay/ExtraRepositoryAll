package com.fishbowl.basicmodule.SavePay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.fishbowl.basicmodule.R;
import com.fishbowl.basicmodule.Services.FBGiftService;
import com.fishbowl.basicmodule.Utils.Config;
import com.fishbowl.basicmodule.Utils.FBUtils;
import com.fishbowl.basicmodule.Utils.ProgressBarHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Digvijay Chauhan .
 */

public class UpdateGiftWidget extends LinearLayout implements View.OnClickListener {


    public static final int SAVE_TO_ANDROID = 888;

    private static final String TAG = "UpdateGiftWidget";
    JSONObject expectedjson;
    Activity activity;
    ProgressBarHandler progressBarHandler;
    ImageButton savetowallet, updatetowallet;
    private String SUCCESS_RESPONSE_TEXT;
    private String CANCELED_RESPONSE_TEXT;
    private String ERROR_PREFIX_TEXT;

    public UpdateGiftWidget(Context context) {
        super(context);
    }

    public UpdateGiftWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpdateGiftWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initUpdateGiftWidget(Activity activity, JSONObject expectedjson) {
        this.activity = activity;
        this.expectedjson = expectedjson;
        inflateUpdateGiftWidget(activity);
    }

    private void inflateUpdateGiftWidget(Activity activity) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_update, this);
        progressBarHandler = new ProgressBarHandler(activity);
        updatetowallet = (ImageButton) v.findViewById(R.id.updateToAndroid);
        updatetowallet.setOnClickListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        inflateUpdateGiftWidget(activity);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.updateToAndroid) {
            progressBarHandler.show();
            updateToAndroidPay(v);
        }

    }


    public void updateToAndroidPay(View view) {

        FBGiftService.sharedInstance().updateGiftAndroidSavePay(expectedjson, new FBGiftService.FBGetSaveGiftJWTokenCallback() {
            @Override
            public void onFBGetSaveGiftJWTokenCallback(JSONObject response, Exception error) {

                if (error == null && response != null) {
                    try {
                        progressBarHandler.hide();
                        boolean successFlag = response.getBoolean("successFlag");
                        String message = response.getString("message");
                        Intent updatesavepay = new Intent(Config.UPDATESAVEPAY);
                        updatesavepay.putExtra("message", message);
                        updatesavepay.putExtra("successFlag", successFlag);
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(updatesavepay);

                    } catch (JSONException e) {
                        progressBarHandler.hide();
                        e.printStackTrace();
                    }
                } else {
                    progressBarHandler.hide();
                    boolean successFlag = false;
                    String message = FBUtils.getErrorDescription(error);
                    Intent savepay = new Intent(Config.SAVEPAY);
                    savepay.putExtra("message", message);
                    savepay.putExtra("successFlag", successFlag);
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(savepay);
                    FBUtils.tryHandleTokenExpiry(activity, error);
                }
            }
        });
    }


}
