package com.BasicApp.Activites.NonGeneric.PointBank;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.google.gson.Gson;

import static com.basicmodule.sdk.R.drawable.firstname;


/**
 * Created by digvijay(dj)
 */

public class PointBankRedeemedModelActivity extends Activity {
    String htmlbody;
    ProgressDialog dialog;

    WebView webView;
    ProgressBarHandler p;
    LinearLayout print, sendSMS;
    String created;
    FBOfferDetailItem item;
    private Toolbar toolbar;
    FBMember member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.redeem_pointbank_activity);
        p = new ProgressBarHandler(this);
        sendSMS = (LinearLayout) findViewById(R.id.bt2);
        print = (LinearLayout) findViewById(R.id.sendPrint);

        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(PointBankRedeemedModelActivity.this).mSharedPreferences.getString("FBUser", "");
        member = gson.fromJson(json, FBMember.class);

        //dialog = ProgressDialog.show(this, "Please Wait", "Loading...", true);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PointBankRedeemedModelActivity.this.finish();
            }
        });
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null) {

            htmlbody = extras.getString("htmlbody");

            item = (FBOfferDetailItem) extras.getSerializable("item");
        }
        webView = (WebView) findViewById(R.id.webview);


        final String url5 = htmlbody;
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return false;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                p.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                p.hide();
                super.onPageFinished(view, url);
            }
        });


        webView.loadUrl(url5);


        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(member!=null) {
                    created = member.getEmailAddress();
                    //   sendPassEmail(item.getCompaignTitle(), url5, created);
                }
            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // verifyStoragePermissions(MultiReward_Activity.this);
                doPhotoPrint(item);
            }
        });
    }

    private void getRewards() {
        if (RewardSummary.rewardList != null) {
            RewardSummary.rewardList.clear();
            if (RewardSummary.rewardList.size() > 0) {


              //  updateRewardInfo(RewardService.offerSummary);
            } else {
             //   fetchReward();
            }
        } else {
          //  fetchReward();
        }
    }








    private void doPhotoPrint(FBOfferDetailItem item) {
        PrintHelper photoPrinter = new PrintHelper(PointBankRedeemedModelActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ImageView image = (ImageView) findViewById(R.id.bt3);


        sendSMS.setVisibility(View.GONE);
        print.setVisibility(View.GONE);


        View v1 = PointBankRedeemedModelActivity.this.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

        if (StringUtilities.isValidString(item.getCampaignTitle())) {
            if(member!=null) {
                String firstname = member.getFirstName();
            }
            photoPrinter.printBitmap(firstname + "_" + item.getCampaignTitle(), bitmap);
        } else {
            photoPrinter.printBitmap("The coupon", bitmap);
        }

    }

//    public void sendPassEmail(String subject, String body, String email) {
//        p.show();
//        JSONObject object = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        try {
//            jsonArray.put(email);
//            object.put("toAddress", jsonArray);
//            object.put("subject", subject);
//            object.put("body", "hi hello");
//            object.put("couponUrl", body);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
////            Ion.with(getApplicationContext()).load(body).asString().setCallback(new FutureCallback<String>() {
////                @Override
////                public void onCompleted(Exception e, String result) {
////
////
////                }
////            });
//
//
//        FBUserService.sharedInstance().postsendPassEmail(object, new FBUserService.FBSendPassEmail() {
//            public void onSendPassEmaiCallback(JSONObject response, Exception error) {
//                if (response != null && error == null) {
//
//                    if (response.has("successFlag")) {
//                        try {
//                            if (response.getString("successFlag").equalsIgnoreCase("true")) {
//
//
//                                p.dismiss();
//                                showalertoffer1();
//                                //     FBUtils.showAlert(MultiReward_Activity.this, "Email send Successfully");
//                            } else {
//
//
//                                //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
//                                p.dismiss();
//                                showalertoffer();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
//                        p.dismiss();
//                        showalertoffer();
//                    }
//                } else {
//                    //  FBUtils.tryHandleTokenExpiry( getActivity(), error);
//                    p.dismiss();
//                    showalertoffer();
//                }
//            }
//        });
//
//
//    }

    public void showalertoffer() {
        AlertDialog alertDialog = new AlertDialog.Builder(PointBankRedeemedModelActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("Email has been Failed");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void showalertoffer1() {
        AlertDialog alertDialog = new AlertDialog.Builder(PointBankRedeemedModelActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("The coupon has been sent to your email address" + " " + created);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (sendSMS != null && print != null) {
            sendSMS.setVisibility(View.VISIBLE);
            print.setVisibility(View.VISIBLE);
        }
        p.hide();
    }

    @Override
    protected void onStop() {

        super.onStop();
        p.hide();
    }
}
