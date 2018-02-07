package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.OfferViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.R;

import java.util.List;

/**
 * Created by Ihsanulhaq on 5/27/2015.
 */
public class MyOfferAdaptertest extends ArrayAdapter<OfferItem> {
    private final static int SectionHeader = 0;
    private final static int RewardsItem = 1;
    private final static int NoRewardsItem = 2;
    private final static int RedeemInstruction = 3;
    List<OfferItem> rewardsList;
    private LayoutInflater mInflater;

    public MyOfferAdaptertest(Activity activity, List<OfferItem> rewardsList) {
        super(activity, R.layout.row_reward_item, rewardsList);
        this.rewardsList = rewardsList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        int rewardsSize = rewardsList.size() > 0 ? rewardsList.size() : 1;
        return rewardsSize + 2; // 2 headers and 1 for redeem instruction
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (rewardsList.size() == 0 && position == 2) || (rewardsList.size() > 0 && position == rewardsList.size() + 1)) {
            return SectionHeader;
        } else if (rewardsList.size() == 0 && position == 1) {
            return NoRewardsItem;
        } else if (rewardsList.size() > 0 && position > 0 && position <= rewardsList.size()) {
            return RewardsItem;
        } else {
            return RedeemInstruction;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OfferViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == RewardsItem) {
                convertView = mInflater.inflate(R.layout.row_reward_item, parent, false);
                holder = new OfferViewHolder(convertView);
            } else if (type == SectionHeader) {
                convertView = mInflater.inflate(R.layout.row_reward_section_header, parent, false);
            } else if (type == NoRewardsItem) {
                convertView = mInflater.inflate(R.layout.row_no_reward_available, parent, false);
            } else {
                convertView = mInflater.inflate(R.layout.row_redeem_instruction, parent, false);
            }
            convertView.setTag(holder);
        }
        if (type == RewardsItem) {
            holder = (OfferViewHolder) convertView.getTag();
            holder.invalidate(getItem(position - 1)); // -1 because of first header
        } else if (type == SectionHeader) {
            TextView sectionTitle = (TextView) convertView.findViewById(R.id.sectionTitle);
            if (position == 0) {
                sectionTitle.setText("Offer");
            } else {
                sectionTitle.setText("Redeem Your Rewards");
            }
        }

        return convertView;
    }

    public void setOffer(List<OfferItem> rewards) {
        clear();
        addAll(rewards);
        this.notifyDataSetChanged();
    }
}
