package com.fishbowl.LoyaltyTabletApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardAndOfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by schaudhary_ic on 17-Nov-16.
 */

public class RewardsOffersAdapter extends BaseAdapter {
    public int diifer;
    Context ctx;
    LayoutInflater inflater;
    ArrayList<RewardAndOfferItem> rewardOfferLisit;


    public RewardsOffersAdapter(Context context, ArrayList<RewardAndOfferItem> rewardOfferLisit) {
        super();
        this.ctx = context;
        inflater = LayoutInflater.from(ctx);
        this.rewardOfferLisit = rewardOfferLisit;
    }

    @Override
    public int getCount() {

        return rewardOfferLisit.size();

    }

    @Override
    public Object getItem(int i) {

        return rewardOfferLisit.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_list_rewards_offers, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        if (rewardOfferLisit.get(i) instanceof OfferItem) {
            RewardAndOfferItem item = rewardOfferLisit.get(i);

            if (StringUtilities.isValidString(item.getDatetime())) {
                Date intent = FBUtils.getDateFromString(item.getDatetime(), (String) null);
                if (intent == null) {
                    intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                }
                if (intent != null) {
                    diifer = FBUtils.daysBetween(new Date(), intent);
                }
            }
            if (rewardOfferLisit.get(i).getOfferIItem() == null) {
                mViewHolder.offerName.setVisibility(View.GONE);
            } else {
                mViewHolder.offerName.setVisibility(View.VISIBLE);
                mViewHolder.offerName.setText(rewardOfferLisit.get(i).getOfferIItem());
            }
            if (rewardOfferLisit.get(i).getOfferIName() == null) {
                mViewHolder.offerDesc.setVisibility(View.GONE);
            } else {
                mViewHolder.offerDesc.setVisibility(View.VISIBLE);
                mViewHolder.offerDesc.setText(rewardOfferLisit.get(i).getOfferIName());
            }
            if (diifer == 0) {
                mViewHolder.offerExpiry.setVisibility(View.GONE);
            } else {
                mViewHolder.offerExpiry.setVisibility(View.VISIBLE);
                if (diifer != 0) {
                    mViewHolder.offerExpiry.setText("Expire Days" + " in " + diifer);
                }
            }
        } else if (rewardOfferLisit.get(i) instanceof RewardsItem) {
            RewardAndOfferItem item = rewardOfferLisit.get(i);

            if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                if (intent == null) {
                    intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                }
                if (intent != null) {
                    diifer = FBUtils.daysBetween(new Date(), intent);
                }
            }
            if (rewardOfferLisit.get(i).getCompaignTitle() == null) {
                mViewHolder.offerName.setVisibility(View.GONE);
            } else {
                mViewHolder.offerName.setVisibility(View.VISIBLE);
                mViewHolder.offerName.setText(rewardOfferLisit.get(i).getCompaignTitle());
            }
            if (rewardOfferLisit.get(i).getCompaignDescription() == null) {
                mViewHolder.offerDesc.setVisibility(View.GONE);
            } else {
                mViewHolder.offerDesc.setVisibility(View.VISIBLE);
                mViewHolder.offerDesc.setText(rewardOfferLisit.get(i).getCompaignDescription());
            }

            if (diifer == 0) {
                mViewHolder.offerExpiry.setVisibility(View.GONE);
            } else {
                mViewHolder.offerExpiry.setVisibility(View.VISIBLE);
                if (diifer != 0) {
                    mViewHolder.offerExpiry.setText("Expire in " + diifer + " Days");
                }
            }
        }
        return convertView;
    }


    private class MyViewHolder {
        TextView offerName, offerDesc, offerExpiry;


        public MyViewHolder(View item) {
            offerName = (TextView) item.findViewById(R.id.offerName);
            offerDesc = (TextView) item.findViewById(R.id.offerDesc);
            offerExpiry = (TextView) item.findViewById(R.id.offerExpiry);
        }
    }

}
