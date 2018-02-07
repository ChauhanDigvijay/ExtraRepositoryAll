package com.olo.jambajuice.Adapters;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsAndOffersActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.List;

public class BasketRewardsOffersAdapter extends ArrayAdapter<Reward> {

    BasketRewardsAndOffersActivity context;

    public BasketRewardsOffersAdapter(BasketRewardsAndOffersActivity context, int resourceId,
                                      List<Reward> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Reward rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_reward_apply_item, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            holder.btnApply = (Button) convertView.findViewById(R.id.btn_apply);
            holder.btnRemove = (Button) convertView.findViewById(R.id.btn_remove);
            if (rowItem.isApplied() && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
                holder.btnApply.setVisibility(View.GONE);
                holder.btnRemove.setVisibility(View.VISIBLE);
            } else {
                holder.btnApply.setVisibility(View.VISIBLE);
                holder.btnRemove.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (StringUtilities.isValidString(rowItem.getRewardTitle())) {
            holder.txtTitle.setText(rowItem.getRewardTitle());
        } else {
            holder.txtTitle.setText("No Title");
        }

        if (StringUtilities.isValidString(rowItem.getDescription())) {
            holder.txtTitle1.setText(rowItem.getDescription());
            holder.txtTitle1.setVisibility(View.VISIBLE);
        } else {
            holder.txtTitle1.setVisibility(View.GONE);
        }

        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reward reward = rowItem;
                if (!reward.isApplied()) {
                    if (StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())
                            || DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0) {
                        confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE, reward);
                    } else {
                        applyReward(reward);
                    }
                }
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.enableScreen(false);
                BasketService.removeRewards(new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        context.enableScreen(true);
                        if (e != null) {
                            Utils.showErrorAlert(context, e);
                        } else {
                            TransitionManager.transitFrom(context, BasketActivity.class, true);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return convertView;
    }

    private void confirmationAlert(String message, final Reward reward) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
        context.enableScreen(false);
        BasketService.applyRewards(context, reward, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                context.enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(getContext(), e);
                } else {
                    DataManager.getInstance().getCurrentBasket().setOfferId("");
                    DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                    TransitionManager.transitFrom(context, BasketActivity.class, true);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setRewards(List<Reward> rewards) {
        clear();
        addAll(rewards);
        this.notifyDataSetChanged();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;
        Button btnApply;
        Button btnRemove;
    }
}