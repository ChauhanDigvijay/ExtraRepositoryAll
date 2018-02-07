package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketProductViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 7/8/2015.
 */
public class BasketAdapter {

    LayoutInflater infalInflater;
    LinearLayout parentLayout;
    List<BasketProductViewHolder> holders;
    private Activity mContext;

    public BasketAdapter(Activity mContext, LinearLayout parentLayout) {
        this.mContext = mContext;
        infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parentLayout = parentLayout;
        holders = new ArrayList<>();
    }

    public void updateData(List<BasketProduct> mData) {
        parentLayout.removeAllViews();
        holders.clear();
        if (mData.size() > 0) {
            int i = 0;
            for (BasketProduct basketProduct : mData) {
                View convertView = infalInflater.inflate(R.layout.row_swipe_basket_item, parentLayout, false);
                BasketProductViewHolder holder = new BasketProductViewHolder(mContext, convertView, this);
                holder.initWithValues(basketProduct, i);
                holders.add(holder);
                parentLayout.addView(convertView);
                i++;
            }
        } else {
            View convertView = infalInflater.inflate(R.layout.row_no_basket_item, parentLayout, false);
            parentLayout.addView(convertView);
        }
    }

    public void closeOther(int position) {
        for (int i = 0; i < holders.size(); i++) {
            if (i != position) {
                BasketProductViewHolder holder = holders.get(i);
                holder.closeSwipe();
            }
        }
    }

    public void refreshData() {
        BasketActivity basketActivity = (BasketActivity) mContext;
        basketActivity.refreshData();
    }
}
