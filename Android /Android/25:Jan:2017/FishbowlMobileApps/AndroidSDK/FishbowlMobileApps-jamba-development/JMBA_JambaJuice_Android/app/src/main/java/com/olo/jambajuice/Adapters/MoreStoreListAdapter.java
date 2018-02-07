package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;

import java.util.List;

/**
 * Created by vt005 on 2/23/17.
 */

public class MoreStoreListAdapter extends ArrayAdapter<Store> {

    Context context;

    public MoreStoreListAdapter(Context context, int resourceId,
                                List<Store> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MoreStoreListAdapter.ViewHolder holder = null;
        Store storeItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //convertView = mInflater.inflate(R.layout.row_rewards_items, null);
            convertView = mInflater.inflate(R.layout.avilable_stores_textview, null);
            holder = new MoreStoreListAdapter.ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtview_storeName);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.txtview_storeAddress);
            convertView.setTag(holder);
        } else
            holder = (MoreStoreListAdapter.ViewHolder) convertView.getTag();


        holder.txtTitle.setText(
                storeItem.getName());
        holder.txtTitle1.setText(storeItem.getCity() +" "+storeItem.getState());

        return convertView;
    }

    public void setRewards(List<Store> storeList) {
        clear();
        addAll(storeList);
        this.notifyDataSetChanged();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;

    }
}
