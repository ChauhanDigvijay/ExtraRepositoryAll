package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderDetails;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import java.util.List;

/**
 * Created by VT017 on 3/14/2017.
 */

public class OrderFavoriteAdapter extends BaseSwipeAdapter {
    private Activity mContext;
    private List<FavoriteOrder> mData;
    private String productTitle;

    public OrderFavoriteAdapter(Activity mContext, List<FavoriteOrder> data) {
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_order_favorite, null);
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        SwipeLayout swipe = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipe.setSwipeEnabled(false);
        productTitle = "";
        /* remove the above lines to reveal
        favorite button in each row upon swiping*/

        TextView name = (TextView) convertView.findViewById(R.id.fav_name);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView status = (TextView) convertView.findViewById(R.id.order_status);

        //setting data values to layout
        if (mData != null && mData.size() > 0) {
            FavoriteOrder data = mData.get(position);
            name.setText(data.getName());
            title.setText(data.getVendorname().replace("Jamba Juice",""));
//            if (newData != null && newData.size() > 0) {
//                List<RecentOrderDetails> detail = newData.get(position).getProducts();
//                if (detail != null && detail.size() > 0) {
//                    for (RecentOrderDetails newdetail : detail) {
//                        productTitle += newdetail.getName() + ",";
//                    }
//                    title.setText(Utils.toCamelCase(productTitle.substring(0, productTitle.length() - 1)));
//                } else {
//                    title.setText(Utils.toCamelCase(data.getName()));
//                }
//            }
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
