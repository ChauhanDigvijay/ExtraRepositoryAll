package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.RedeemReward.RedeemRewardActivity;
import com.olo.jambajuice.Adapters.MyOffersAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Services.OfferService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * *
 * Created by Digvijay Chauhan on 25/4/16.
 */
public class MyOfferActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView rewardsListView;
    private MyOffersAdapter adapter;
    private List<OfferItem> offerList = new ArrayList<OfferItem>();
    ;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myoffer);
        initComponents();
        setAdapter();
        getOffer();
    }


    private void getOffer() {
        if (OfferService.offerSummary != null) {
            hideProgressBar();
            setData(OfferService.offerSummary);
        } else {
            fetchData();
        }
    }


    private void fetchData() {


        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(OfferSummary offerSummary, String error) {

                hideProgressBar();
                if (offerSummary != null) {
                    setData(offerSummary);
                } else {
                    Utils.showErrorAlert(MyOfferActivity.this, error);
                }
            }
        });
    }


    private void setData(OfferSummary offerSummary) {
        if (offerSummary.getOfferList() != null) {
            offerList = offerSummary.getOfferList();
        }

        if (offerList != null && offerList.size() > 0) {
//            Iterator<Reward> it = offerList.iterator();
//            //Display rewards of type offer only
//            while (it.hasNext())
//            {
//                Reward reward = it.next();
//                if (reward.getType() == null || (!reward.getType().equals("offer") && !reward.getType().equals("reward")))
//                {
//                    it.remove();
//                }
//            }

            adapter.setOffers(offerList);
        }

    }


    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setAdapter() {
        adapter = new MyOffersAdapter(this, offerList);
        rewardsListView.setAdapter(adapter);
    }

    private void setHeader() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ImageView layout = (ImageView) layoutInflater.inflate(R.layout.header_rewards, null, false);
        BitmapUtils.loadBitmapResourceWithViewSize(layout, R.drawable.rewards_header, false);
        rewardsListView.addHeaderView(layout);
    }

    private void initComponents() {
        setToolbar();
        rewardsListView = (ListView) findViewById(R.id.rewardsList);
        rewardsListView.setOnItemClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_color);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rewardsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (rewardsListView == null || rewardsListView.getChildCount() == 0) ? 0 : rewardsListView.getChildAt(0).getTop();
                boolean isEnabled = topRowVerticalPosition >= 0;
                mSwipeRefreshLayout.setEnabled(isEnabled);
            }
        });
        setHeader();
    }

    private void setToolbar() {
        setUpToolBar(true);
        isSlideDown = true;
        setTitle("My Offers");
        setBackButton(true,false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (parent.getId() == R.id.rewardsList) {

            if (adapter != null) {


                if (position == adapter.getCount()) {
                    TransitionManager.transitFrom(this, RedeemRewardActivity.class);
                } else if (position != adapter.getCount() - 1 && position != 1 && position != 0) {


                    OfferItem item = adapter.getItem(position - 2);
                    String offerId = item.getOfferId();
                    String url = item.getOfferIOther();
                    String title = item.getOfferIItem();
                    String desc = item.getOfferIName();
                    Date d2 = Utils.getDateFromString(item.getDatetime(), null);
                    int diifer = Utils.daysBetween(new Date(), d2);

                    if (url != null) {
                        Intent intent = new Intent(this, PkpassdetailActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("Url", url);
                        extras.putString("Title", title);
                        intent.putExtras(extras);
                        //JambaAnalyticsManager.sharedInstance().track_OfferItemWith(offerId, "", offerId, title, FBEventSettings.PASS_CLICKED);
                        startActivity(intent);
                    } else if (!StringUtilities.isValidString(url) && StringUtilities.isValidString(offerId)) {

                        Intent intent = new Intent(this, Passreward.class);
                        Bundle extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", diifer);
                        extras.putString("offerId", offerId);
                        extras.putString("desc", desc);
                        intent.putExtras(extras);
                        JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, title, FBEventSettings.OPEN_APP_OFFER);
                        startActivity(intent);

                    }
                }
            }
        }
        // We just need to get click on redeem instructions

    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
