package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderDetailViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderSummaryViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderDetails;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderSummary;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ihsanulhaq on 5/15/2015.
 */
public class OrderDetailAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_DETAIL = 1;
    private final Activity activity;
    private List<RecentOrderDetails> mData = new ArrayList<RecentOrderDetails>();
    private LayoutInflater mInflater;
    private RecentOrderSummary summary;
    private Store storeInfo;
    private String status;

    public OrderDetailAdapter(Context context, RecentOrder newData) {
        activity = (Activity) context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (newData != null) {
            mData.addAll(newData.getProducts());
            summary = newData.getSummary();
            status = newData.getStatus();
        }
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            return mData.get(position);
        } else {
            return summary;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mData.size()) {
            return TYPE_DETAIL;
        }
        return TYPE_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // based on rowType Holders are being assigned to view in layout
        int rowType = getItemViewType(position);

        switch (rowType) {
            case TYPE_ITEM:
                OrderDetailViewHolder holder1;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.row_order_details_products, null);
                    holder1 = new OrderDetailViewHolder(convertView);
                } else {
                    holder1 = (OrderDetailViewHolder) convertView.getTag();
                }
                holder1.invalidate(mData.get(position));
                convertView.setTag(holder1);
                break;
            case TYPE_DETAIL:
                OrderSummaryViewHolder holder2;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.row_order_detail_history, null);
                    holder2 = new OrderSummaryViewHolder(convertView);
                } else {
                    holder2 = (OrderSummaryViewHolder) convertView.getTag();
                }
                holder2.invalidate(activity, summary, storeInfo, status);
                convertView.setTag(holder2);
                break;
        }

        return convertView;
    }

    // setting storeinfo after setting adapter for TYPE_DETAIL from detail activity
    public void setStoreInfo(Store storeInfo) {
        this.storeInfo = storeInfo;
        this.notifyDataSetChanged();
    }
}
