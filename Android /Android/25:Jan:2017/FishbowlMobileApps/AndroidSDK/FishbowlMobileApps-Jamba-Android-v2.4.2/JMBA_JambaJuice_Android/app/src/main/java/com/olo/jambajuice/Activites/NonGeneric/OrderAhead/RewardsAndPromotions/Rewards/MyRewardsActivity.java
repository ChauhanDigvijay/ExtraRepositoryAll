package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.RedeemReward.RedeemRewardActivity;
import com.olo.jambajuice.Adapters.MyRewardsAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Models.RewardSummary;
import com.olo.jambajuice.BusinessLogic.Services.RewardService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyRewardsActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView rewardsListView;
    private MyRewardsAdapter adapter;
    private List<Reward> rewardsList = new ArrayList<Reward>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        initComponents();
        setAdapter();
        getRewards();
    }

    private void getRewards() {
        if (RewardService.rewardSummary != null) {
            hideProgressBar();
            setData(RewardService.rewardSummary);
        } else {
            fetchData();
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
                    Utils.showErrorAlert(MyRewardsActivity.this, exception);
                }
            }
        });
    }

    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setData(RewardSummary rewardSummary) {
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

    private void setAdapter() {
        adapter = new MyRewardsAdapter(this, rewardsList);
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
        setTitle("My Rewards");
        setBackButton(false,false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // We just need to get click on redeem instructions
        if (position == adapter.getCount()) {
            TransitionManager.transitFrom(this, RedeemRewardActivity.class);
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
