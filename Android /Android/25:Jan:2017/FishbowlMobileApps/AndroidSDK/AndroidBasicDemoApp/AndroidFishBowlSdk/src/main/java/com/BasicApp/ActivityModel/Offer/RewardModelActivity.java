package com.BasicApp.ActivityModel.Offer;

import android.app.Activity;
import android.content.Intent;
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
import com.BasicApp.Activites.NonGeneric.Advertisement.MobileAddActivity;
import com.BasicApp.ActivityModel.PointBank.PointBankRedeemedModelActivity;
import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.BusinessLogic.Models.RewardsItem;
import com.BasicApp.ModelAdapters.RewardModelAdapter;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Services.FBRewardService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by digvijay(dj)
 */
public class RewardModelActivity extends Activity implements AdapterView.OnItemClickListener

{
    public TextView topWelcome, top_name, offer_number, memberPhone, memberEmail;
    public ImageLoader mImageLoader;
    ImageView backbutton;
    int diifer;
    ImageView userImage;
    LinearLayout userSetting;
    NetworkImageView imlogo, imbackground;
    LinearLayout myRewards, myOffer, logout_layout, menu_layout;
    ProgressBarHandler progressBarHandler;
    private RewardModelAdapter rewardmodeladapter;
    private ListView rewardListView;
    private DrawerLayout drawerLayout;
    private RelativeLayout toolbar;
    private List<RewardsItem> rewardList = new ArrayList<RewardsItem>();

    public List<FBOfferDetailItem> offeList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

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

        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardModelActivity.this.finish();
            }
        });
        FBUtils.setUpNavigationDrawerModel(RewardModelActivity.this);
        LinearLayout map_activity = (LinearLayout) findViewById(R.id.map_activity);
        map_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RewardModelActivity.this, OfferModelActivity.class);
                startActivity(i);
                RewardModelActivity.this.finish();
            }
        });
        rewardListView = (ListView) findViewById(R.id.store_list);
        rewardListView.setOnItemClickListener(this);
        setAdapter();
      //  getRewards();
        getRewardsModel();


    }


    @Override
    protected void onResume() {
        super.onResume();


        rewardmodeladapter.notifyDataSetChanged();

        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
    }


//    private void getRewards() {
//        progressBarHandler.show();
//        if (RewardService.offerSummary != null) {
//
//            setReward(RewardService.offerSummary);
//            updateRewardInfo(RewardService.offerSummary);
//
//        } else {
//            fetchReward();
//        }
//    }

    private void getRewardsModel() {
        progressBarHandler.show();
        fetchModelReward();
    }

//    private void fetchReward() {
//
//        RewardService.getUserReward(this, new RewardSummaryCallback() {
//
//
//            @Override
//            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception error) {
//
//
//                if (rewardSummary != null) {
//                    setReward(rewardSummary);
//                    updateRewardInfo(rewardSummary);
//                } else {
//                    progressBarHandler.dismiss();
//                    FBUtils.tryHandleTokenExpiry(RewardModelActivity.this, error);
//                }
//            }
//        });
//    }


    private void fetchModelReward() {

        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null){
                    setModelReward(response);
                    updateRewardModelInfo(response);
            } else {
                progressBarHandler.dismiss();
                FBUtils.tryHandleTokenExpiry(RewardModelActivity.this, error);
            }
            }
        });
    }

    private void updateRewardModelInfo(FBOfferListItem response) {

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

    private void setModelReward(FBOfferListItem response) {

        if (response.getCategories().length>0) {


            FBOfferDetailItem[] offerarray = response.getCategories();
            offeList= Arrays.asList(offerarray);
            rewardmodeladapter.setOffer(offeList);

            progressBarHandler.dismiss();
        }





    }



//    private void setReward(RewardSummary rewardSummary) {
//        if (rewardSummary.getRewardList() != null) {
//            rewardList = rewardSummary.getRewardList();
//        }
//
//        if (rewardList != null && rewardList.size() > 0) {
//
//            rewardmodeladapter.setOffer(rewardList);
//        }
//
//
//    }


    private void setAdapter() {
        rewardmodeladapter = new RewardModelAdapter(this, R.layout.row_offer_item_bistro, offeList);
        rewardListView.setAdapter(rewardmodeladapter);
        rewardmodeladapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }


    private void updateRewardInfo(RewardSummary rewardSummary) {
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

        if (rewardmodeladapter != null) {
            {

                FBOfferDetailItem item = offeList.get(position);
                int compaignId = item.getCampaignId();
                String url = item.getPassPreviewImageURL();
                String title = item.getCampaignTitle();
                String desc = item.getCampaignDescription();
                String promotioncode = item.getPromotionCode();
                String html = item.getHtmlBody();
                String couponURL = item.getCouponURL();
                int promotionId = item.getPmPromotionID();
                if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                    Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                    if (intent != null) {
                        diifer = FBUtils.daysBetween(new Date(), intent);
                    }
                }
              //  FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(offerId), title, FBEventSettings.OPEN_APP_OFFER);
                Bundle extras;
                Intent intent1;
                if (item.getChannelTypeID() == 6) {

                    intent1 = new Intent(this, PushDetailModelActivity.class);
                    extras = new Bundle();
                    extras.putString("Url", url);
                    extras.putString("Title", title);
                    extras.putInt("Expire", this.diifer);
                    extras.putInt("promotionId", promotionId);
                    extras.putString("offerId", String.valueOf(compaignId));

                    extras.putString("desc", desc);
                    intent1.putExtras(extras);
                    startActivity(intent1);
                } else if (item.getChannelTypeID() == 1) {

                    if (couponURL != null) {
                        intent1 = new Intent(this, PointBankRedeemedModelActivity.class);
                        extras = new Bundle();
                        extras.putSerializable("item", (Serializable) item);
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
                        extras.putString("offerId", String.valueOf(compaignId));
                        extras.putString("promotioncode", promotioncode);
                        // extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        startActivity(intent1);
                    }
                } else if (item.getChannelTypeID() == 7) {

                    intent1 = new Intent(this, MobileAddActivity.class);
                    extras = new Bundle();
                    extras.putString("htmlbody", html);
                    intent1.putExtras(extras);
                    startActivity(intent1);
                } else if (item.getChannelTypeID() == 3) {
                    if (couponURL != null) {
//                        intent1 = new Intent(this, PointBankRedeemedActivity.class);
//                        extras = new Bundle();
//                        extras.putString("htmlbody", couponURL);
//
//                        extras.putSerializable("order", item);
//                        intent1.putExtras(extras);
//                        startActivity(intent1);
                    } else {
                        intent1 = new Intent(this, SmsRewardModelActivity.class);
                        extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putString("offerId", String.valueOf(compaignId));
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
                    extras.putString("offerId", String.valueOf(compaignId));
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
}







