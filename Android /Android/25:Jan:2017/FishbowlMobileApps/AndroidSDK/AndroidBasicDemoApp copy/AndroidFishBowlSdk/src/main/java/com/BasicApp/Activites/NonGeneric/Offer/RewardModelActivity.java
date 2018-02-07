package com.BasicApp.Activites.NonGeneric.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Advertisement.MobileAddActivity;
import com.BasicApp.Activites.NonGeneric.PointBank.PointBankRedeemedModelActivity;
import com.BasicApp.ModelAdapters.RewardModelAdapter;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Services.FBRewardService;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.basicmodule.sdk.R.drawable.reward;

/**
 * Created by digvijay(dj)
 */
public class RewardModelActivity extends BaseActivity implements AdapterView.OnItemClickListener

{
    public TextView offer_number;
    public ImageLoader mImageLoader;
    public List<FBOfferDetailItem> offeList = new ArrayList<>();
    ImageView backbutton;
    int diifer;

    private RewardModelAdapter rewardmodeladapter;
    private ListView rewardListView;
    private DrawerLayout drawerLayout;
    private RelativeLayout toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);




        setUpToolBar(true,true);
        setTitle("Rewards & Offers");
        setBackButton(true,false);



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
        getRewardsModel();


    }


    @Override
    protected void onResume() {
        super.onResume();


        rewardmodeladapter.notifyDataSetChanged();

        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
    }


    private void getRewardsModel() {
       enableScreen(false);
        fetchModelReward();
    }


    private void fetchModelReward() {

        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null) {
                    setModelReward(response);
                    updateRewardModelInfo(response);
                } else {
                    enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(RewardModelActivity.this, error);
                }
            }
        });
    }

    private void updateRewardModelInfo(FBOfferListItem response) {

        enableScreen(true);

        int offerCount = FBPreferences.sharedInstance(this).getOfferCount();
        int rewardCount = FBPreferences.sharedInstance(this).getRewardCount();
      //  int sum = offerCount + rewardCount;

    if (offerCount > 0 || rewardCount > 0) {
        TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

        rewardsValue.setText(String.valueOf(rewardCount));

        TextView offerValue = (TextView) findViewById(R.id.txt_offer);

        offerValue.setText(String.valueOf(offerCount));

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

        if (response.getCategories().length > 0) {


            FBOfferDetailItem[] offerarray = response.getCategories();
            offeList = Arrays.asList(offerarray);
            rewardmodeladapter.setOffer(offeList);

            enableScreen(true);
        }


    }


    private void setAdapter() {
        rewardmodeladapter = new RewardModelAdapter(this, R.layout.row_offer_item_bistro, offeList);
        rewardListView.setAdapter(rewardmodeladapter);
        rewardmodeladapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();


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

        }
    }
}







