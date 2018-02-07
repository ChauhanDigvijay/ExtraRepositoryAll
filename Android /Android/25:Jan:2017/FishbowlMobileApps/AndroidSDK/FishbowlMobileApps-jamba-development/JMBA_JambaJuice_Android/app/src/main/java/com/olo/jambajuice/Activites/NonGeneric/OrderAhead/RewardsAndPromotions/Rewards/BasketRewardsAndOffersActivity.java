package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PkpassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Promotions.PromotionalCodeActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.RedeemReward.RedeemRewardActivity;
import com.olo.jambajuice.Adapters.BasketOfferAdapter;
import com.olo.jambajuice.Adapters.BasketRewardsOffersAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketRewardsCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.OfferAvailableStore;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.OfferService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public class BasketRewardsAndOffersActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static BasketRewardsAndOffersActivity basketRewardsAndOffersActivity;
    BasketRewardsOffersAdapter basketRewardsAdapter;
    ArrayList<Reward> rewardsList = new ArrayList<Reward>();
    List<OfferItem> offerList;
    List<OfferItem> filteredOfferList;
    int diifer;
    private BasketOfferAdapter adapter1;
    private ListView rewardsListView1;
    private ListView listView;
    private View promotionsView;
    private View promotionsView1;
    private View offerCantCombined;
    private View empty;
    private View empty1;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketreward_offer);
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }
        basketRewardsAndOffersActivity = this;
        isShowBasketIcon = false;
        setToolBar();
        setUi();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
    }

    public void setUi() {
        setUpListView();
        fetchUserRewards();
        getOffer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView.smoothScrollTo(0, 0);
        if (rewardsList != null && filteredOfferList != null) {
            if ((rewardsList.size() == 0) && (filteredOfferList.size() == 0)) {
                hideProgressBar();
                empty.setVisibility(View.VISIBLE);
                empty1.setVisibility(View.VISIBLE);
                offerCantCombined.setVisibility(View.GONE);
            } else if (rewardsList.size() == 0) {
                hideProgressBar();
                empty.setVisibility(View.VISIBLE);
            } else if (filteredOfferList.size() == 0) {
                hideProgressBar();
                empty1.setVisibility(View.VISIBLE);
                offerCantCombined.setVisibility(View.GONE);
            } else {
                empty.setVisibility(View.GONE);
                empty1.setVisibility(View.GONE);
                offerCantCombined.setVisibility(View.VISIBLE);
            }
            adapter1.setOffer(filteredOfferList);
        }
    }

    private void fetchUserRewards() {
        enableScreen(false);
        BasketService.availableRewards(this, new BasketRewardsCallback() {
            @Override
            public void onBasketRewardsCallback(ArrayList<Reward> reward, Exception error) {
                enableScreen(true);
                if (reward != null && reward.size() != 0) {
                    empty.setVisibility(View.GONE);
                    rewardsList.clear();
                    rewardsList.addAll(reward);
                    basketRewardsAdapter.notifyDataSetChanged();
                } else if (error != null) {
                    //Utils.showErrorAlert(BasketRewardsAndOffersActivity.this, error);
                    // Utils.showErrorAlert(BasketRewardsAndOffersActivity.this, "There was a problem retrieving loyalty rewards. Please try again later.");
                }
            }
        });
    }

    private void setUpListView() {

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        offerList = new ArrayList<OfferItem>();
        filteredOfferList = new ArrayList<OfferItem>();
        promotionsView = findViewById(R.id.toolbar1);
        promotionsView1 = findViewById(R.id.toolbar2);
        offerCantCombined = findViewById(R.id.offerCantCombined);
        empty = findViewById(R.id.empty);
        empty1 = findViewById(R.id.empty1);
        promotionsView.setOnClickListener(this);
        promotionsView1.setOnClickListener(this);
        basketRewardsAdapter = new BasketRewardsOffersAdapter(this, R.layout.row_rewards_items, rewardsList);
        adapter1 = new BasketOfferAdapter(this, R.layout.row_basket_offer_item, offerList);
        listView = (ListView) findViewById(R.id.rewardsList);
        rewardsListView1 = (ListView) findViewById(R.id.listView2);
        offerCantCombined.setVisibility(View.VISIBLE);
        rewardsListView1.setOnItemClickListener(this);
        listView.setAdapter(basketRewardsAdapter);
        rewardsListView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }

    private void setToolBar() {
        setUpToolBar(true);
        setTitle("Rewards & Promotions", getResources().getColor(android.R.color.white));
        setBackButton(true, true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbar1:
                showRewardsAndPromos();
                break;

            case R.id.toolbar2:
                showRewardsAndPromos1();
                break;
        }
    }


    private void showRewardsAndPromos() {


        TransitionManager.transitFrom(this, PromotionalCodeActivity.class);


    }

    private void showRewardsAndPromos1() {


        TransitionManager.transitFrom(this, RedeemRewardActivity.class);


    }

    private void getOffer() {
        if (OfferService.offerSummary != null) {

            setDataOffer(OfferService.offerSummary);
        } else {
            fetchDataOffer();
        }
    }

    private void fetchDataOffer() {


        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(OfferSummary offerSummary, String error) {

                hideProgressBar();
                if (offerSummary != null) {
                    setDataOffer(offerSummary);
                } else {
                    //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
                }
            }
        });
    }

    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        // mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setDataOffer(OfferSummary offerSummary) {

        if (offerSummary.getOfferList() != null) {
            offerList = offerSummary.getOfferList();
        }

        if (offerList != null && offerList.size() > 0) {
            for (int i = 0; i < offerList.size(); i++) {
                if (offerList.get(i).getAvailableStoresList() != null
                        && offerList.get(i).getAvailableStoresList().size() > 0) {
                    for (int j = 0; j < offerList.get(i).getAvailableStoresList().size(); j++) {
                        if (Utils.tryIntParse(DataManager.getInstance().getCurrentSelectedStore().getStoreCode())) {
                            if (Integer.parseInt(DataManager.getInstance().getCurrentSelectedStore().getStoreCode())
                                    == Integer.parseInt(offerList.get(i).getAvailableStoresList().get(j).getStoreCode())) {
                                if (offerList.get(i).getOnlineInStore() == 1
                                        || offerList.get(i).getOnlineInStore() == 2) {
                                    filteredOfferList.add(offerList.get(i));
                                }
                            }
                        }
                    }
                } else {
                    if (offerList.get(i).getOnlineInStore() == 1
                            || offerList.get(i).getOnlineInStore() == 2) {
                        filteredOfferList.add(offerList.get(i));
                    }
                }
            }


            empty1.setVisibility(View.GONE);
            offerCantCombined.setVisibility(View.VISIBLE);
            hideProgressBar();
            adapter1.setOffer(filteredOfferList);
        } else {
            empty1.setVisibility(View.VISIBLE);
            offerCantCombined.setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.listView2) {

            if (Utils.isNetworkAvailable(this)) {
                OfferItem item = adapter1.getItem(position);

                String url = item.getOfferIOther();
                String title = item.getOfferIItem();
                String offerId = item.getOfferId();
                String desc = "";
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    desc = item.getNotificationContent();
                } else {
                    desc = item.getOfferIName();
                }
                int promotionId = item.getPmPromotionID();
                String promoCode = item.getPromotioncode();
                Boolean isPMOffer = item.isPMOffer();
                ArrayList<OfferAvailableStore> storeResList = item.getAvailableStoresList();
                if (StringUtilities.isValidString(item.getDatetime())) {
                    Date d2 = Utils.getDateFromString(item.getDatetime(), null);
                    diifer = Utils.daysBetween(new Date(), d2);
                } else {
                    diifer = 400;
                }


                if (item.getChannelID() == 6) {
                    Intent intent = new Intent(this, PkpassdetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putBoolean("isPMOffer", isPMOffer);
                    extras.putString("Url", url);
                    extras.putString("Title", title);
                    extras.putInt("Expire", diifer);
                    extras.putInt("promotionId", promotionId);
                    extras.putString("offerId", offerId);
                    extras.putString("desc", desc);
                    intent.putExtras(extras);
                    //   JambaAnalyticsManager.sharedInstance().track_ItemWith(item.getOfferId(), item.getOfferIItem(), FBEventSettings.PASS_CLICKED);
                    //JambaAnalyticsManager.sharedInstance().track_OfferItemWith(item.getOfferId(), "", item.getOfferId(), item.getOfferIItem(), FBEventSettings.PASS_CLICKED);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PassdetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putBoolean("isPMOffer", isPMOffer);
                    extras.putString("Title", title);
                    extras.putInt("Expire", diifer);
                    extras.putString("offerId", offerId);
                    extras.putInt("promotionId", promotionId);
                    extras.putString("promoCode", promoCode);
                    extras.putString("desc", desc);
                    extras.putSerializable("storeList", storeResList);
                    intent.putExtras(extras);
                    JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, title, FBEventSettings.OPEN_APP_OFFER);
                    startActivity(intent);

                }
            } else {
                Utils.showErrorAlert(BasketRewardsAndOffersActivity.this, "Please check your network connection and try again ");
            }
        }
//        else if (parent.getId() == R.id.rewardsList) {
//            if (rewardsList.size() > 0 && position >= 0) {
//                Reward reward = rewardsList.get(position);
//                if(!reward.isApplied()) {
//                    if(StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())
//                            || DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0){
//                        confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE,reward);
//                    }else {
//                        applyReward(reward);
//                    }
//                }
//            }
//        }

    }

    private void confirmationAlert(String message, final Reward reward) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(BasketRewardsAndOffersActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                applyReward(reward);
            }
        });
        dialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void applyReward(final Reward reward) {
        if (reward != null) {
            JambaAnalyticsManager.sharedInstance().track_ItemWith("", "REWARD_TITLE:" + reward.getRewardTitle() + ";MEMBERSHIP_ID:" + reward.getMembershipId() + ";REFERENCE:" + reward.getReference(), FBEventSettings.APPLY_REWARD);
        }
        enableScreen(false);
        BasketService.applyRewards(this, reward, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(BasketRewardsAndOffersActivity.this, e);
                } else {
                    DataManager.getInstance().getCurrentBasket().setPromoId(0);
                    DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                    TransitionManager.transitFrom(BasketRewardsAndOffersActivity.this, BasketActivity.class, true);
                }
            }
        });
    }

    @Override
    protected void handleAuthTokenFailure() {
        finish();
    }
}
