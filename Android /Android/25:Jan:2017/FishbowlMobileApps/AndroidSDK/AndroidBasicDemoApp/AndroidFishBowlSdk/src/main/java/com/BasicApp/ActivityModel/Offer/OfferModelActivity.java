package com.BasicApp.ActivityModel.Offer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Offer.SmsRewardActivity;
import com.BasicApp.Activites.NonGeneric.PointBank.PointBankRedeemedActivity;
import com.BasicApp.ActivityModel.PointBank.PointBankRedeemedModelActivity;
import com.BasicApp.BusinessLogic.Services.OfferService;
import com.BasicApp.ModelAdapters.OfferModelAdapter;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Interfaces.OfferSummaryCallback;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Models.FBOfferSummaryItem;
import com.fishbowl.basicmodule.Services.FBRewardService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by digvijay(dj)
 */
public class OfferModelActivity extends Activity implements AdapterView.OnItemClickListener

{
    public TextView topWelcome, top_name, offer_number, memberPhone, memberEmail;
    public ImageLoader mImageLoader;
    int diifer;
    ImageView userImage;
    LinearLayout userSetting;
    ImageView backbutton;
    NetworkImageView imlogo, imbackground;
    LinearLayout myRewards, myOffer, logout_layout, menu_layout;
    ProgressBarHandler progressBarHandler;
    private ListView offerListView;
    private OfferModelAdapter adapter1;
    private DrawerLayout drawerLayout;
    private RelativeLayout toolbar;
    private List<FBOfferItem> offerList = new ArrayList<FBOfferItem>();
    public List<FBOfferDetailItem> offeList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_bistro);
        progressBarHandler = new ProgressBarHandler(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }

        });
        backbutton = (ImageView) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FBUtils.setUpNavigationDrawerModel(OfferModelActivity.this);


        LinearLayout map_activity = (LinearLayout) findViewById(R.id.list_activity);
        map_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OfferModelActivity.this, RewardModelActivity.class);
                startActivity(i);
                OfferModelActivity.this.finish();
            }
        });
        offerListView = (ListView) findViewById(R.id.offer_list);
        offerListView.setOnItemClickListener(this);
        setAdapter();
        getOfferModel();


    }


    @Override
    protected void onResume() {
        super.onResume();

        getOfferModel();
        adapter1.notifyDataSetChanged();

        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();


    }


//    private void getOffer() {
//        progressBarHandler.show();
//        if (OfferService.FBOfferSummaryItem != null) {
//
//            setDataOffer(OfferService.FBOfferSummaryItem);
//            updateOfferInfo(OfferService.FBOfferSummaryItem);
//
//        } else {
//            fetchDataOffer();
//        }
//
//
//    }

    private void getOfferModel() {
        progressBarHandler.show();
        fetchModelOffer();
    }

    private void fetchModelOffer() {

        FBRewardService.getUserFBOffer(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null){
                    setModeloffer(response);
                    updateModelOfferInfo(response);
                } else {
                    progressBarHandler.dismiss();
                    FBUtils.tryHandleTokenExpiry(OfferModelActivity.this, error);
                }
            }
        });
    }



    private void setModeloffer(FBOfferListItem response) {

        if (response.getCategories().length>0) {


            FBOfferDetailItem[] offerarray = response.getCategories();
            offeList= Arrays.asList(offerarray);
            adapter1.setOffer(offeList);

            progressBarHandler.dismiss();
        }


    }



    private void setAdapter() {
        adapter1 = new OfferModelAdapter(this, R.layout.row_offer_item_bistro, offeList);
        offerListView.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }


    private void setDataOffer(FBOfferSummaryItem FBOfferSummaryItem) {
        if (FBOfferSummaryItem.getOfferList() != null) {
            offerList = FBOfferSummaryItem.getOfferList();
        }

        if (offerList != null && offerList.size() > 0) {

            adapter1.setOffer(offeList);
        }


    }



    private void fetchDataOffer() {


        //   enableScreen(false);

        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(FBOfferSummaryItem FBOfferSummaryItem, Exception error) {


                if (FBOfferSummaryItem != null) {
                    setDataOffer(FBOfferSummaryItem);
                    updateOfferInfo(FBOfferSummaryItem);
                } else {
                    //  enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(OfferModelActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


    }


    private void updateOfferInfo(FBOfferSummaryItem FBOfferSummaryItem) {
        //   enableScreen(true);

        progressBarHandler.dismiss();
        int offer = FBUserService.sharedInstance().member.getOffercount();
        int reward = FBUserService.sharedInstance().member.getRewardcount();

        if (reward > 0 || offer > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText(String.valueOf(reward));

            TextView offerValue = (TextView) findViewById(R.id.txt_offer);

            offerValue.setText(String.valueOf(offer));

        } else {
            {
                TextView offerValue = (TextView) findViewById(R.id.txt_offer);

                offerValue.setText("0");


                TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

                rewardsValue.setText("0");

            }
        }
    }

    private void updateModelOfferInfo(FBOfferListItem response) {
        //   enableScreen(true);

        progressBarHandler.dismiss();
        int offer = FBUserService.sharedInstance().member.getOffercount();
        int reward = FBUserService.sharedInstance().member.getRewardcount();

        if (reward > 0 || offer > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText(String.valueOf(reward));

            TextView offerValue = (TextView) findViewById(R.id.txt_offer);

            offerValue.setText(String.valueOf(offer));

        } else {
            {
                TextView offerValue = (TextView) findViewById(R.id.txt_offer);

                offerValue.setText("0");


                TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

                rewardsValue.setText("0");

            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (adapter1 != null) {
            {

               // FBOfferItem item = FBOfferSummaryItem.offerList.get(position);
                FBOfferDetailItem item = offeList.get(position);
                int compaignId = item.getCampaignId();
                String url = item.getPassPreviewImageURL();
                String title = item.getCampaignTitle();
                String desc = item.getCampaignDescription();
                String html = item.getHtmlBody();
                String couponURL = item.getCouponURL();
                String promotioncode = item.getPromotionCode();
              //  Boolean isPMOffer = Boolean.valueOf(item.isPMOffer());
               // FBAnalyticsManager.sharedInstance().track_ItemWith(compaignId, title, FBEventSettings.OPEN_APP_OFFER);
                int promotionId = item.getPmPromotionID();
                if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                    Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                    if (intent == null) {
                        intent = FBUtils.getDateFromStringSlash(item.getValidityEndDateTime(), null);
                    }
                    diifer = FBUtils.daysBetween(new Date(), intent);
                }

                Bundle extras;
                Intent intent1;
                if (item.getChannelTypeID() == 6) {
                    Uri uri = Uri.parse("http://test.attidomobile.com/PassWallet/Passes/AttidoMobile.pkpass");
                    openPassBook(uri);

                } else if (item.getChannelTypeID() == 1) {

                    if (couponURL != null) {
                        intent1 = new Intent(this, PointBankRedeemedModelActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", couponURL);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else if (html != null) {
                        intent1 = new Intent(this, EmailRewardModelActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", html);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else {
                        intent1 = new Intent(this, PushDetailModelActivity.class);
                        extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putInt("offerId", compaignId);
                       // extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("promotioncode", promotioncode);
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    }

                } else if (item.getChannelTypeID() == 7) {

                    if (couponURL != null) {
                        intent1 = new Intent(this, PointBankRedeemedModelActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", couponURL);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else if (html != null) {
                        intent1 = new Intent(this, EmailRewardModelActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", html);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else {
                        intent1 = new Intent(this, PushDetailModelActivity.class);
                        extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putInt("offerId", compaignId);
                       // extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("promotioncode", promotioncode);
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    }
                } else if (item.getChannelTypeID() == 3) {
                    if (couponURL != null) {
                        intent1 = new Intent(this, PointBankRedeemedActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", couponURL);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else {
                        intent1 = new Intent(this, SmsRewardActivity.class);
                        extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putInt("offerId", compaignId);
                       // extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("promotioncode", promotioncode);
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    }
                } else if (item.getChannelTypeID() == 5) {
                    if (couponURL != null) {
                        intent1 = new Intent(this, PointBankRedeemedActivity.class);
                        extras = new Bundle();
                        extras.putString("htmlbody", couponURL);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    } else {
                        intent1 = new Intent(this, PushDetailModelActivity.class);
                        extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putInt("offerId", compaignId);
                      //  extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("promotioncode", promotioncode);
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    }
                } else {
                    intent1 = new Intent(this, PushDetailModelActivity.class);
                    extras = new Bundle();
                    extras.putString("Title", title);
                    extras.putInt("Expire", this.diifer);
                    extras.putInt("promotionId", promotionId);
                    extras.putInt("offerId", compaignId);
                  //  extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                    extras.putString("promotioncode", promotioncode);
                    extras.putString("desc", desc);
                    intent1.putExtras(extras);
                    startActivity(intent1);
                }
            }

        } else {
            // Utils.showErrorAlert(MyRewardsAndOfferActivity.this, "Please check your network connection and try again ");
        }
    }


    protected boolean openPassBook(Uri uri) {
        if (null != this && FBUtility.isNetworkAvailable(this)) {
            PackageManager packageManager = this.getPackageManager();
            if (null != packageManager) {
                final String strPackageName = "com.attidomobile.passwallet";
                Intent startIntent = new Intent();
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.setAction(Intent.ACTION_VIEW);
                Intent passWalletLaunchIntent = packageManager
                        .getLaunchIntentForPackage(strPackageName);
                if (null == passWalletLaunchIntent) { // PassWallet isn't
                    // installed, open
                    // Google Play:
                    if (FBUtility.checkPlayServices(this)) {
                        String strReferrer = "";
                        try {
                            final String strEncodedURL = URLEncoder.encode(
                                    uri.toString(), "UTF-8");
                            strReferrer = "&referrer=" + strEncodedURL;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            strReferrer = "";
                        }
                        try {
                            startIntent.setData(Uri
                                    .parse("market://details?id="
                                            + strPackageName + strReferrer));
                            this.startActivity(startIntent);
                        } catch (android.content.ActivityNotFoundException anfe) {
                            // Google Play not installed, open via website
                            startIntent
                                    .setData(Uri
                                            .parse("http://play.google.com/store/apps/details?id="
                                                    + strPackageName
                                                    + strReferrer));
                            this.startActivity(startIntent);
                        }
                    }
                } else {
                    final String strClassName = "com.attidomobile.passwallet.activity.TicketDetailActivity";
                    startIntent.setClassName(strPackageName, strClassName);
                    startIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startIntent.setDataAndType(uri,
                            "application/vnd.apple.pkpass");
                    this.startActivity(startIntent);
                    return true;
                }
            }
        }
        return false;
    }
}







