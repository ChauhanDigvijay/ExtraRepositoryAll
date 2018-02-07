package com.BasicApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.LoyaltyActivityListItem;
import com.BasicApp.Utils.StringUtilities;
import com.basicmodule.sdk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by schaudhary_ic on 09-Nov-16.
 */

public class MyActivityListAdapter extends BaseAdapter {

    ArrayList<LoyaltyActivityListItem> myList = new ArrayList<LoyaltyActivityListItem>();
    LayoutInflater inflater;
    Context context;

    public MyActivityListAdapter(Context context, ArrayList<LoyaltyActivityListItem> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_my_activity_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        LoyaltyActivityListItem loyaltyitem = myList.get(position);
        if (StringUtilities.isValidString(loyaltyitem.getEventTime())) {

            try {
                String currentDate = loyaltyitem.getEventTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date tempDate = simpleDateFormat.parse(currentDate);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                mViewHolder.dateData.setText(outputDateFormat.format(tempDate));
            } catch (ParseException ex) {
                System.out.println("Parse Exception");
            }
        } else {
            mViewHolder.dateData.setText("eventTime");
        }
        if (StringUtilities.isValidString(loyaltyitem.getName()))
            mViewHolder.activityData.setText(loyaltyitem.getName());

        if (loyaltyitem.getCheckNumber() != 0) {


            mViewHolder.nullData1.setText("Puchase - Check" + "-" + String.valueOf(loyaltyitem.getCheckNumber()));
        } else if (loyaltyitem.getOfferId() != 0 && StringUtilities.isValidString(loyaltyitem.getDesc())) {
            mViewHolder.nullData1.setText("Name of Reward" + "-" + loyaltyitem.getDesc());
        } else {

        }

        if (StringUtilities.isValidString(loyaltyitem.getBalance())) {
            mViewHolder.nullData3.setText(loyaltyitem.getBalance() + " " + "points");
        } else {

        }

        if (loyaltyitem.getPointsEarned() != 0) {
            mViewHolder.nullData4.setText(loyaltyitem.getPointsEarned() + " " + "points");
        } else {

        }

        return convertView;

    }

    private class MyViewHolder {
        TextView dateData, activityData, nullData1, nullData2, nullData3, nullData4;

        public MyViewHolder(View item) {
            dateData = (TextView) item.findViewById(R.id.dateData);
            activityData = (TextView) item.findViewById(R.id.activityData);
            nullData1 = (TextView) item.findViewById(R.id.nullData1);
            nullData2 = (TextView) item.findViewById(R.id.nullData2);
            nullData3 = (TextView) item.findViewById(R.id.nullData3);
            nullData4 = (TextView) item.findViewById(R.id.nullData4);

        }
    }
}