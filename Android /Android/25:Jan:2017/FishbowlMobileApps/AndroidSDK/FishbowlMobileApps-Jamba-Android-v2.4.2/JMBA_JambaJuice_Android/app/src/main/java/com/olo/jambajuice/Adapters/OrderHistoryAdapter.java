package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import java.util.List;

/**
 * Created by Ihsanulhaq on 5/25/2015.
 */
public class OrderHistoryAdapter extends BaseSwipeAdapter {

    private Activity mContext;
    private List<RecentOrder> mData;

    public OrderHistoryAdapter(Activity mContext, List<RecentOrder> data) {
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_order_history, null);
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        SwipeLayout swipe = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipe.setSwipeEnabled(false);
        /* remove the above lines to reveal
        favorite button in each row upon swiping*/

        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView detail = (TextView) convertView.findViewById(R.id.tv_detail);
        TextView total = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView status = (TextView) convertView.findViewById(R.id.order_status);

        //setting data values to layout
        if (mData != null && mData.size() > 0) {
            RecentOrder data = mData.get(position);
            title.setText(Utils.toCamelCase(data.getFofpName()));
            total.setText("");
            detail.setText(data.getOrderTimeStatement());
            if (data.getStatus() != null) {
                status.setText(data.getStatus());
                switch (data.getStatus()) {
                    case "Canceled":
                        status.setTextColor(mContext.getResources().getColor(R.color.cancel_red));
                        break;
                    case "Failed":
                        status.setTextColor(mContext.getResources().getColor(R.color.cancel_red));
                        break;
                    case "Completed":
                        status.setTextColor(mContext.getResources().getColor(R.color.greenEnd));
                        break;
                    case "Scheduled":
                        status.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                        break;
                    default:
                        status.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                        break;
                }
            }
        }
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
