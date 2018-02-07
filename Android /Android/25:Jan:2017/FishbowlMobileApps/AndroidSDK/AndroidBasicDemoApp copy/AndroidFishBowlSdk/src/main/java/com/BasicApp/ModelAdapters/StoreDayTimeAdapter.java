package com.BasicApp.ModelAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.GetSelectedSotreDetails;
import com.BasicApp.Utils.FBUtils;
import com.basicmodule.sdk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by schaudhary_ic on 13-Oct-16.
 */

public class StoreDayTimeAdapter extends BaseAdapter {
    public List<GetSelectedSotreDetails> storeHourList;
    public Context context;
    String calculatedOpen,calculateClosed;

    public StoreDayTimeAdapter(Context context, List<GetSelectedSotreDetails> storeList) {
        super();
        this.context = context;
        this.storeHourList = storeList;
    }
    @Override
    public int getCount() {
        return storeHourList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeHourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_day_time, parent, false);
        }
        // Lookup view for data population

        TextView day = (TextView) convertView.findViewById(R.id.day);
        TextView opening_time = (TextView) convertView.findViewById(R.id.opening_time);
        TextView closing_time = (TextView) convertView.findViewById(R.id.closing_time);
        // Populate the data into the template view using the data object

        GetSelectedSotreDetails strhrs = storeHourList.get(position);

        String time = strhrs.getOpeningTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            final Date dateObj = sdf.parse(time);
            calculatedOpen = new SimpleDateFormat("h:mm a").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String timeclose = strhrs.getClosingTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            final Date dateObj = sdf.parse(timeclose);
            calculateClosed = new SimpleDateFormat("h:mm a").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (FBUtils.isValidString(strhrs.getClosingTime()) &&FBUtils.isValidString(strhrs.getOpeningTime())) {


            for (int i = 0; i <= storeHourList.size(); i++) {
                Integer dayofWeek;
                dayofWeek = strhrs.getDayOfTheWeek();
                switch (dayofWeek) {
                    case 0:
                        day.setText("Sunday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 1:
                        day.setText("Monday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 2:
                        day.setText("Tuesday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 3:
                        day.setText("Wednesday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 4:
                        day.setText("Thursday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 5:
                        day.setText("Friday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    case 6:
                        day.setText("Saturday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;
                    default:
                        day.setText("Sunday");
                        opening_time.setText(calculatedOpen);
                        closing_time.setText(calculateClosed);
                        break;


                }
            }

        }
        else{
            day.setVisibility(View.GONE);
            opening_time.setVisibility(View.GONE);
            closing_time.setVisibility(View.GONE);

        }

            return convertView;
        }
    }

