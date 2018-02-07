package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import java.util.Date;


/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class OfferViewHolder {

    private TextView rewardName;
    private TextView description;
    private LinearLayout passlayout;

    private TextView time;

    public OfferViewHolder(View view) {
        rewardName = (TextView) view.findViewById(R.id.tv_name);
        description = (TextView) view.findViewById(R.id.tv_expiry);
        passlayout = (LinearLayout) view.findViewById(R.id.passlayout);
        time = (TextView) view.findViewById(R.id.tv_name1);
    }

    public void invalidate(OfferItem item) {

        rewardName.setText(item.getOfferIItem());
        description.setText(item.getOfferIName());
        Date d2 = Utils.getDateFromString(item.getDatetime(), null);
        int differ = Utils.daysBetween(new Date(), d2);
        if (item.getChannelID() != 0 && item.getChannelID() > 0) {
            if (item.getChannelID() == 6) {
                passlayout.setVisibility(View.VISIBLE);
            } else {
                passlayout.setVisibility(View.GONE);
            }
        }

        if (differ > 0) {
            time.setText("Expires in" + " " + differ + " " + ((differ == 1)?"day":"days"));
        }
    }


}
