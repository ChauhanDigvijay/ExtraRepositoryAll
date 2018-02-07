package com.BasicApp.ActivityModel.PointBank;

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

import com.BasicApp.BusinessLogic.Models.OfferSummary;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryCallback;
import com.BasicApp.BusinessLogic.Models.RewardSummaryPointCallback;
import com.BasicApp.BusinessLogic.Services.OfferService;
import com.BasicApp.BusinessLogic.Services.RewardPointService;
import com.BasicApp.BusinessLogic.Services.RewardService;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.OfferSummaryCallback;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferSummaryItem;
import com.fishbowl.basicmodule.Services.FBUserService;


/**
 * Created by schaudhary_ic on 09-Feb-17.
 */

public class PointBankRedeemedModelActivity extends Activity {
    String htmlbody;
    ProgressDialog dialog;

    WebView webView;
    private Toolbar toolbar;
    ProgressBarHandler p ;
    LinearLayout print, sendSMS;
    String created;
    FBOfferDetailItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.redeem_pointbank_activity);
        p = new ProgressBarHandler(this);
        sendSMS = (LinearLayout)findViewById(R.id.bt2);
        print = (LinearLayout) findViewById(R.id.sendPrint);

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
                created = FBUserService.sharedInstance().member.emailID;
             //   sendPassEmail(item.getCompaignTitle(), url5, created);
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


                updateRewardInfo(RewardService.offerSummary);
            } else {
                fetchReward();
            }
        } else {
            fetchReward();
        }
    }


    private void fetchRewardPoint() {
        p.show();
        //dialog = ProgressDialog.show(this, "Please Wait", "Loading...", true);
        RewardPointService.getUserRewardPoint(this, new RewardSummaryPointCallback() {


            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {


                if (rewardSummary != null) {
                    updateOfferRewardPoint(rewardSummary);

                } else {
                    //  enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(PointBankRedeemedModelActivity.this, error);
                }
            }
        });
    }




    private void fetchReward() {

        RewardService.getUserReward(this, new RewardSummaryCallback() {


            @Override
            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception error) {


                if (rewardSummary != null) {

                    updateRewardInfo(rewardSummary);
                } else {
                    //  enableScreen(true);
                    // FBUtils.tryHandleTokenExpiry(Rewards_Activity.this,error);
                }
            }
        });
    }


    private void updateRewardInfo(RewardSummary rewardSummary) {
        //   enableScreen(true);


        int value = FBUserService.sharedInstance().member.getOffercount();
        int value1 = rewardSummary.getRewardCount();
        int sum = value + value1;
        FBUserService.sharedInstance().member.setRewardcount(value1);
        if (sum > 0) {
           // TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

           // rewardsValue.setText(String.valueOf(sum));


        } else {
            {
              //  TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

               // rewardsValue.setText("0");

            }
        }
        p.hide();
       /* if (dialog != null && dialog.isShowing())
            dialog.dismiss();
*/
    }

    private void getOffer() {
    p.show();
     //   dialog = ProgressDialog.show(this, "Please Wait", "Loading...", true);
        if (OfferSummary.offerList != null) {

            if (OfferSummary.offerList.size() > 0) {
                updateOfferInfo(OfferService.FBOfferSummaryItem);
            } else {
                fetchDataOffer();
            }
        } else {
            fetchDataOffer();
        }


    }


    private void fetchDataOffer() {


        //   enableScreen(false);
        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(FBOfferSummaryItem FBOfferSummaryItem, Exception error) {


                if (FBOfferSummaryItem != null) {

                    updateOfferInfo(FBOfferSummaryItem);
                } else {
                    //  enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(PointBankRedeemedModelActivity.this, error);
                }
            }
        });
    }


    private void updateOfferInfo(FBOfferSummaryItem FBOfferSummaryItem) {
        //   enableScreen(true);


        int value = FBOfferSummaryItem.getOfferCount();

        if (value > 0) {
            //TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);
            FBUserService.sharedInstance().member.setOffercount(value);

            //    rewardsValue.setText( String.valueOf(value));

        }
        getRewards();
    }

    private void updateOfferRewardPoint(RewardPointSummary rewardSummary) {
        //   enableScreen(true);


        int EarnedPoints = RewardPointSummary.earnedPoints;
        //  int b = (int) Math.round(PointsToNextReward);
        if (EarnedPoints > 0) {
           // TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

           // rewardsValue.setText(String.valueOf(EarnedPoints));

        } else {
         //   TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

         //   rewardsValue.setText("0");
        }
       /* if (dialog != null && dialog.isShowing())
            dialog.dismiss();*/
       p.hide();

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
            String firstname = FBUserService.sharedInstance().member.firstName;
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
        /*if (dialog != null && dialog.isShowing())
            dialog.dismiss();*/
        p.hide();
    }
}
