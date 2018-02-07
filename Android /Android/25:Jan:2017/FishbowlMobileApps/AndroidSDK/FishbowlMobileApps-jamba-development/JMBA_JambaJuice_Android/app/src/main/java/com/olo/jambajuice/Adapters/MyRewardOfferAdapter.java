package com.olo.jambajuice.Adapters;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.R;

import java.util.List;

public class MyRewardOfferAdapter extends ArrayAdapter<Reward> {

    Context context;

    public MyRewardOfferAdapter(Context context, int resourceId,
                                List<Reward> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Reward rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_rewards_items, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtTitle.setText(
                rowItem.getRewardTitle());
        holder.txtTitle1.setText(rowItem.getDescription());

        return convertView;
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

    }
}