package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.MultiReward_Activity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.MultipleOffer_Activity;
import com.fishbowl.LoyaltyTabletApp.Adapters.RewardsOffersAdapter;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardAndOfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardAndOfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 17-Nov-16.
 */

public class FragmentRewardsAndOffers extends Fragment implements AdapterView.OnItemClickListener {

    ListView reward_offer_list;
    RewardsOffersAdapter roa;
    ProgressBarHandler p;
    RewardAndOfferSummary rs;
    private ArrayList<RewardAndOfferItem> rewardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rewards_offers, container, false);
        reward_offer_list = (ListView) v.findViewById(R.id.reward_offer_list);
        p = new ProgressBarHandler(getActivity());
        reward_offer_list.setOnItemClickListener(this);
        return v;
    }

    public void viewInflater() {
        p.show();
        rewardList = new RewardAndOfferSummary().populate();
        if (rewardList != null && rewardList.size() > 0) {
            roa = new RewardsOffersAdapter(getContext(), rewardList);
            reward_offer_list.setAdapter(roa);
            p.dismiss();
        } else {
            p.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (rewardList.get(i) instanceof RewardsItem) {
            int vp = (i);
            Intent intent = new Intent(getContext(), MultiReward_Activity.class);
            Bundle extras = new Bundle();
            extras.putInt("position", vp);
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        } else if (rewardList.get(i) instanceof OfferItem) {
            int vp = i - (rewardList.size() - OfferSummary.offerList.size());
            Intent intent = new Intent(getContext(), MultipleOffer_Activity.class);
            Bundle extras = new Bundle();
            extras.putInt("position", vp);
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
    }
}
