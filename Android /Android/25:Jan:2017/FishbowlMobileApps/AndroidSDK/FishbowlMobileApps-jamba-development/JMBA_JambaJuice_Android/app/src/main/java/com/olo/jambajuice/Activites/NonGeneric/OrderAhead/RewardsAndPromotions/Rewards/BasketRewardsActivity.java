package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Promotions.PromotionalCodeActivity;
import com.olo.jambajuice.Adapters.BasketRewardsAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketRewardsCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 03/07/15.
 */
public class BasketRewardsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    BasketRewardsAdapter basketRewardsAdapter;
    ArrayList<Reward> rewardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_reward);
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }
        isShowBasketIcon = false;
        setToolBar();
        setUpListView();
        fetchUserRewards();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
    }

    private void fetchUserRewards() {
        enableScreen(false);
        BasketService.availableRewards(this, new BasketRewardsCallback() {
            @Override
            public void onBasketRewardsCallback(ArrayList<Reward> reward, Exception error) {
                enableScreen(true);
                if (reward != null) {
                    rewardsList.clear();
                    rewardsList.addAll(reward);
                    basketRewardsAdapter.notifyDataSetChanged();
                } else {
                    //Utils.showErrorAlert(BasketRewardsActivity.this, error);
                }
            }
        });
    }

    private void setUpListView() {
        rewardsList = new ArrayList<>();
        basketRewardsAdapter = new BasketRewardsAdapter(this, rewardsList);
        ListView listView = (ListView) findViewById(R.id.rewardsList);
        listView.setAdapter(basketRewardsAdapter);
        listView.setOnItemClickListener(this);
    }

    private void setToolBar() {
        setUpToolBar(true);
        setTitle("Rewards & Promotions", getResources().getColor(android.R.color.white));
        setBackButton(true,true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (rewardsList.size() > 0 && position > 0 && position <= rewardsList.size()) {
            Reward reward = rewardsList.get(position - 1);
            applyReward(reward);
        } else if (position == parent.getCount() - 1) {
            // Enter promotion Code
            TransitionManager.transitFrom(this, PromotionalCodeActivity.class);
        }
    }

    private void applyReward(Reward reward) {
        enableScreen(false);
        BasketService.applyRewards(this, reward, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(BasketRewardsActivity.this, e);
                } else {
                    TransitionManager.transitFrom(BasketRewardsActivity.this, BasketActivity.class, true);
                }
            }
        });
    }

    @Override
    protected void handleAuthTokenFailure() {
        finish();
    }
}
