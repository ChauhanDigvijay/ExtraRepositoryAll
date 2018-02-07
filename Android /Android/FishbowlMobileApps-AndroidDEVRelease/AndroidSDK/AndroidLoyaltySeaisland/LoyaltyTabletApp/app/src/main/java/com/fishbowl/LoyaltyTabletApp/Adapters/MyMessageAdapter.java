package com.fishbowl.LoyaltyTabletApp.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyMessages;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.loyaltymodule.Utils.StringUtilities;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class MyMessageAdapter extends BaseAdapter {
    ArrayList<LoyaltyMessages> myList = new ArrayList<LoyaltyMessages>();
    LayoutInflater inflater;
    Context context;
    CheckBox checkBox;
    boolean[] itemChecked;

    public MyMessageAdapter(Context context, ArrayList<LoyaltyMessages> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        itemChecked = new boolean[myList.size()];
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_message_row, parent, false);
            mViewHolder = new MyViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final LinearLayout rlayout = (LinearLayout) convertView.findViewById(R.id.rlayout);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);


        checkBox.setClickable(false);


        LoyaltyMessages loyaltyitem = myList.get(position);
        if (StringUtilities.isValidString(loyaltyitem.getMessage())) {
            mViewHolder.message.setText(loyaltyitem.getMessage());
        }


        if (StringUtilities.isValidString(loyaltyitem.getMessageSent())) {
            mViewHolder.date_time.setText(loyaltyitem.getMessageSent());
        }


        if (loyaltyitem.isRead) {
            checkBox.setChecked(false);
            mViewHolder.message.setTextColor(ContextCompat.getColor(context, R.color.textHint));
            mViewHolder.date_time.setTextColor(ContextCompat.getColor(context, R.color.textHint));
        } else {
            checkBox.setChecked(false);

            mViewHolder.message.setTextColor(ContextCompat.getColor(context, R.color.orignalText));
            mViewHolder.date_time.setTextColor(ContextCompat.getColor(context, R.color.orignalText));
        }


        return convertView;

    }

    public class MyViewHolder {
        TextView dateData, activityData, nullData1, nullData2, nullData3, message, date_time;
        CheckBox checkBox;

        public MyViewHolder(View item) {
            message = (TextView) item.findViewById(R.id.message);
            //      checkBox = (CheckBox) item.findViewById(R.id.checkBox);
            date_time = (TextView) item.findViewById(R.id.date_time);


        }
    }


}