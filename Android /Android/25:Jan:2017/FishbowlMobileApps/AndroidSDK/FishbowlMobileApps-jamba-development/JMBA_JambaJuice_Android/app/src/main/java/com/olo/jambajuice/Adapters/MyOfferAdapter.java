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

import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.Utils;

import java.util.Date;
import java.util.List;

public class MyOfferAdapter extends ArrayAdapter<OfferItem> {

    Context context;

    public MyOfferAdapter(Context context, int resourceId,
                          List<OfferItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        OfferItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_offer_item, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            holder.time = (TextView) convertView.findViewById(R.id.tv_name1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtilities.isValidString(rowItem.getDatetime())) {
            Date d2 = Utils.getDateFromString(rowItem.getDatetime(), null);
            if (d2 == null) {
                d2 = Utils.getDateFromStringSlash(rowItem.getDatetime(), null);
            }
            int diifer = Utils.daysBetween(new Date(), d2);
            if (diifer > 1) {
                holder.time.setText("Expires in" + " " + diifer + " " + "days");
            }else if(diifer < 0){
                holder.time.setText("Expired");
            }else if(diifer == 1){
                holder.time.setText("Expires in" + " " + diifer + " " + "day");
            }else if(diifer == 0){
                holder.time.setText("Expires today");
            }
        }else{
            holder.time.setText("Never Expires");
        }


        holder.txtTitle.setText(rowItem.getOfferIItem());
        if(StringUtilities.isValidString(rowItem.getOfferIName())){
            String[] separated = rowItem.getOfferIName().split("Promo Code:");
            holder.txtTitle1.setText(separated[0]);
        }else{
            holder.txtTitle1.setText("");
        }


        return convertView;
    }

    public void setOffer(List<OfferItem> rewards) {
        clear();
        addAll(rewards);
        this.notifyDataSetChanged();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;
        TextView time;

    }
}