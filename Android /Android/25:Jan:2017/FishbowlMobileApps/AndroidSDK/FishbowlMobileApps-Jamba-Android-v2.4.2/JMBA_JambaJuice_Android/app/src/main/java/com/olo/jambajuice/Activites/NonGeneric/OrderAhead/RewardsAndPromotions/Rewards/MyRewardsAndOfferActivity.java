package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.Passreward;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PkpassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.RedeemReward.RedeemRewardActivity;
import com.olo.jambajuice.Adapters.MyOfferAdapter;
import com.olo.jambajuice.Adapters.MyRewardOfferAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.OfferAvailableStore;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.BusinessLogic.Models.OfferSummary;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Models.RewardSummary;
import com.olo.jambajuice.BusinessLogic.Services.OfferService;
import com.olo.jambajuice.BusinessLogic.Services.RewardService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MyRewardsAndOfferActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public JambaApplication _app;
    // private SwipeRefreshLayout mSwipeRefreshLayout;
    int diifer;
    private ListView rewardsListView;
    private ListView rewardsListView1;
    private MyRewardOfferAdapter adapter;
    private MyOfferAdapter adapter1;
    private List<Reward> rewardsList = new ArrayList<Reward>();
    ;
    private List<OfferItem> offerList = new ArrayList<OfferItem>();
    private List<OfferItem> filteredOfferList = new ArrayList<>();
    private View promotionsView;
    private View offerAvailable;
    private View empty;
    private View empty1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView offersCount,rewardsCount;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreward_offer);
        context = this;
        boolean isOfferRefresh = getIntent().getBooleanExtra(Constants.B_IS_REFRESH_OFFER,false);
        initComponents();
        setAdapter();
        setAdapter1();
        getRewards();
        if(isOfferRefresh){
            enableScreen(false);
            fetchDataOffer();
        }else {
            getOffer();
        }

        _app = (JambaApplication) getApplication();

    }

    private void getRewards() {
        if (RewardService.rewardSummary != null) {
            hideProgressBar();
            setData(RewardService.rewardSummary);
        } else {
            fetchData();
        }
    }


    private void getOffer() {
        if (OfferService.offerSummary != null) {
            hideProgressBar();
            setDataOffer(OfferService.offerSummary);
        } else {
            fetchDataOffer();
        }
    }


    private void fetchData() {
        RewardService.getUserRewards(this, new RewardSummaryCallback() {
            @Override
            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception exception) {
                hideProgressBar();
                if (rewardSummary != null) {
                    setData(rewardSummary);
                } else {
                    Utils.showErrorAlert(context, exception);
                }
            }
        });
    }


    private void fetchDataOffer() {

        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(OfferSummary offerSummary, String error) {
                enableScreen(true);
                hideProgressBar();
                if (offerSummary != null) {
                    setDataOffer(offerSummary);
                } else {
                    //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((rewardsList.size() == 0) && (filteredOfferList.size() == 0)) {
            hideProgressBar();
            offerAvailable.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            empty1.setVisibility(View.VISIBLE);
        } else if (rewardsList.size() == 0) {
            empty1.setVisibility(View.VISIBLE);
        } else if (filteredOfferList.size() == 0) {
            offerAvailable.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            empty1.setVisibility(View.GONE);
            offerAvailable.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setData(RewardSummary rewardSummary) {
        rewardsList = rewardSummary.getRewardsList();
        rewardsCount = (TextView)findViewById(R.id.rewardsCount);
        int size = rewardsList.size();
        Iterator<Reward> it = rewardsList.iterator();
        int count = 0;
        //Display rewards of type offer only
        while (it.hasNext()) {
            Reward reward = it.next();
            if (reward.getType() == null || (!reward.getType().equals("offer") && !reward.getType().equals("reward"))) {
                count++;
                it.remove();
                empty1.setVisibility(View.VISIBLE);
            }
        }
        rewardsCount.setText(String.valueOf(size - count));
        empty1.setVisibility(View.GONE);
        adapter.setRewards(rewardsList);
    }


    private void setDataOffer(OfferSummary offerSummary) {
        if (offerSummary.getOfferList() != null) {
            offerList = offerSummary.getOfferList();
        }

        if(filteredOfferList != null){
            filteredOfferList.clear();
            filteredOfferList = new ArrayList<>();
        }

        if(offerList != null && offerList.size() > 0) {
            for (int i = 0; i < offerList.size(); i++) {
              //  if (offerList.get(i).getOnlineInStore() == 1
               //         || offerList.get(i).getOnlineInStore() == 2) {
                    filteredOfferList.add(offerList.get(i));
              //  }
            }
            if (filteredOfferList != null && filteredOfferList.size() > 0) {
                empty.setVisibility(View.GONE);
                offersCount = (TextView) findViewById(R.id.offersCount);
                offersCount.setText(String.valueOf(filteredOfferList.size()));
                offerAvailable.setVisibility(View.VISIBLE);
                hideProgressBar();
                adapter1.setOffer(filteredOfferList);
            } else {
                offerAvailable.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        }else {
            offerAvailable.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        adapter = new MyRewardOfferAdapter(this, R.layout.row_rewards_items, rewardsList);
        rewardsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void setAdapter1() {
        adapter1 = new MyOfferAdapter(this, R.layout.row_offer_item, offerList);
        rewardsListView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }


    private void initComponents() {
        setToolbar();
        empty = findViewById(R.id.empty);
        empty1 = findViewById(R.id.empty1);
        offerAvailable = findViewById(R.id.offerAvailable);
        rewardsListView = (ListView) findViewById(R.id.rewardsList);
        rewardsListView1 = (ListView) findViewById(R.id.listView2);
        promotionsView = findViewById(R.id.toolbar1);
        rewardsListView1.setOnItemClickListener(this);
        rewardsListView.setOnItemClickListener(this);
        promotionsView.setOnClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_color);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        offersCount = (TextView)findViewById(R.id.offersCount);
        rewardsCount = (TextView)findViewById(R.id.rewardsCount);
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
        // setHeader();
    }

//    public void enableScreen(boolean isEnabled) {
//
//        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
//        if (screenDisableView != null) {
//            if (!isEnabled) {
//                screenDisableView.setVisibility(View.VISIBLE);
//            } else {
//                screenDisableView.setVisibility(View.GONE);
//            }
//        }
//    }

    private void setToolbar() {
        setUpToolBar(true);
        isSlideDown = true;
        setTitle("My Rewards & Offers");
        setBackButton(false,false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // We just need to get click on redeem instructions

        if (parent.getId() == R.id.listView2) {

            if (adapter1 != null) {
                if (Utils.isNetworkAvailable(this)) {
                    OfferItem item = adapter1.getItem(position);
                    String offerId = item.getOfferId();
                    String url = item.getOfferIOther();
                    String title = item.getOfferIItem();
                    String desc = "";
                    if(StringUtilities.isValidString(item.getNotificationContent())){
                        desc = item.getNotificationContent();
                    }else{
                        desc = item.getOfferIName();
                    }
                    Boolean isPMOffer = item.isPMOffer();
                    int onlineInStore = item.getOnlineInStore();
                    String promoCode = (item.getPromotioncode()!=null)?item.getPromotioncode():"";
                    int promotionId = item.getPmPromotionID();
                    ArrayList<OfferAvailableStore> storeResList = item.getAvailableStoresList();
                    if (StringUtilities.isValidString(item.getDatetime())) {
                        Date d2 = Utils.getDateFromString(item.getDatetime(), null);
                        if (d2 == null) {
                            d2 = Utils.getDateFromStringSlash(item.getDatetime(), null);
                        }
                        diifer = Utils.daysBetween(new Date(), d2);
                    }else{
                        diifer = 400;
                    }
                    if (item.getChannelID() == 6) {
                        // JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, title, FBEventSettings.PASS_CLICKED);
                        //JambaAnalyticsManager.sharedInstance().track_OfferItemWith(offerId, "", offerId, title, FBEventSettings.PASS_CLICKED);

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
                        startActivity(intent);
                    } else {
                        JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, title, FBEventSettings.OPEN_APP_OFFER);
                        Intent intent = new Intent(this, Passreward.class);
                        Bundle extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putString("offerId", offerId);
                        extras.putBoolean("isPMOffer", isPMOffer);
                        extras.putString("desc", desc);
                        extras.putString("promoCode",promoCode);
                        extras.putSerializable("storeList",storeResList);
                        extras.putInt("onlineInStore",onlineInStore);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                } else {
                    Utils.showErrorAlert(context, new Exception("Please check your network connection and try again "));
                }
            }
        }
//
//        else if (position == adapter1.getCount()-1)
//        {
//
//
//            //dj Splendgo Offer Clicked
//
//            try {
//                JSONObject data = new JSONObject();
//                data.put("event_name", "OpenOffer");
//                data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
//                _app.clpsdkObj.updateAppEvent(data);
//
//            } catch (JSONException e) {
//
//                JSONObject data = new JSONObject();
//
//                try {
//                    data.put("event_name", "AppError");
//                    _app.clpsdkObj.updateAppEvent(data);
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//                _app.clpsdkObj.updateAppEvent(data);
//                //      Log.e(_app.CLP_TRACK_ERROR, "PRODUCT_OPEN:" + e.getMessage());
//            }
//
//
//        }
//        else  if (position == adapter.getCount())
//        {
//            TransitionManager.transitFrom(this, RedeemRewardActivity.class);
//        }
    }


    public void loadNewItemsCount() {


    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }
        return false;
    }


    private void setOffer(RewardSummary rewardSummary) {
        rewardsList = rewardSummary.getRewardsList();
        Iterator<Reward> it = rewardsList.iterator();
        //Display rewards of type offer only
        while (it.hasNext()) {
            Reward reward = it.next();
            if (reward.getType() == null || (!reward.getType().equals("offer") && !reward.getType().equals("reward"))) {
                it.remove();
            }
        }
        adapter.setRewards(rewardsList);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbar1:
                showRewardsAndPromos();
                break;

        }
    }

    private void showRewardsAndPromos() {


        TransitionManager.transitFrom(this, RedeemRewardActivity.class);


    }

    @Override
    public void onRefresh() {

        fetchDataOffer();
    }

    private void setDataOffer() {


        setAdapter();
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
