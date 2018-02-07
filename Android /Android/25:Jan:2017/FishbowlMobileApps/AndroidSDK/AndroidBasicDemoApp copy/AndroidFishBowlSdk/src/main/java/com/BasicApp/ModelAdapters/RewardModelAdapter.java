package com.BasicApp.ModelAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBOfferDetailItem;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.util.Date;
import java.util.List;


/**
 * Created by digvijaychauhan on 31/07/16.
 */

public class RewardModelAdapter extends ArrayAdapter<FBOfferDetailItem> {

    Context context;
    private ImageLoader mImageLoader;

    public RewardModelAdapter(Context context, int resourceId,
                         List<FBOfferDetailItem> items) {
        super(context, resourceId, items);
        this.context = context;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                .getImageLoader();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;
        TextView time;
        NetworkImageView puss_img;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        FBOfferDetailItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_offer_item_bistro, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            holder.time = (TextView) convertView.findViewById(R.id.tv_name1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtilities.isValidString(rowItem.getValidityEndDateTime())) {
            Date d2 = FBUtils.getDateFromString(rowItem.getValidityEndDateTime(), null);
            if (d2 != null) {
                int diifer = FBUtils.daysBetween(new Date(), d2);

                if (diifer > 0) {
                    holder.time.setText("Expires in" + " " + diifer + " " + "days");
                } else {
                    holder.time.setText(rowItem.getValidityEndDateTime());
                }
            }
        }
        if (StringUtilities.isValidString(rowItem.getCampaignTitle())) {
            holder.txtTitle.setText(rowItem.getCampaignTitle());
        }
        if (StringUtilities.isValidString(rowItem.getCampaignDescription())) {
            holder.txtTitle1.setText(rowItem.getCampaignDescription());
        }


        return convertView;
    }

    public void setOffer(List<FBOfferDetailItem> offerlist) {
        clear();
        addAll(offerlist);
        this.notifyDataSetChanged();
    }
}