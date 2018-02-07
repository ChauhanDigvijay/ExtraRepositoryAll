package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.module.OfferModule;
import com.hbh.honeybaked.supportingfiles.Utility;
import java.util.ArrayList;

public class OfferAdapter extends BaseAdapter {
    Context context = null;
    LayoutInflater l_Inflater = null;
    ArrayList<OfferModule> offer_list_data = null;

    private class ViewHolder {
        TextView offer_date;
        TextView offer_des;
        LinearLayout offer_list_ll;
        TextView offer_title;

        private ViewHolder() {
        }
    }

    public OfferAdapter(FragmentActivity context, ArrayList<OfferModule> offer_list_data) {
        this.context = context;
        this.offer_list_data = offer_list_data;
        this.l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.offer_list_data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.l_Inflater.inflate(R.layout.offer_custom_list_lay, null);
            holder.offer_list_ll = (LinearLayout) convertView.findViewById(R.id.offer_list_ll);
            holder.offer_title = (TextView) convertView.findViewById(R.id.offer_title);
            holder.offer_des = (TextView) convertView.findViewById(R.id.offer_des);
            holder.offer_date = (TextView) convertView.findViewById(R.id.offer_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OfferModule offerModule = (OfferModule) this.offer_list_data.get(position);
        holder.offer_title.setText(offerModule.getCampaignTitle());
        if (Utility.isEmptyString(offerModule.getCampaignDescription())) {
            holder.offer_des.setVisibility(View.GONE);
        } else {
            holder.offer_des.setVisibility(View.GONE);
            holder.offer_des.setText(offerModule.getCampaignDescription());
        }
        if (Utility.isEmptyString(offerModule.getValidityEndDateTime())) {
            holder.offer_date.setVisibility(View.GONE);
        } else {
            holder.offer_date.setVisibility(View.GONE);
            if (offerModule.getOfferValidDate() < 0) {
                holder.offer_date.setText("Offer Expired.");
            } else if (offerModule.getOfferValidDate() > 0) {
                holder.offer_date.setText("Expire in " + offerModule.getOfferValidDate() + " days.");
            } else {
                holder.offer_date.setText("Offer Expiring Today.");
            }
        }
        return convertView;
    }
}
