package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloCalendar;
import com.wearehathway.apps.olo.Models.OloTimeRange;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nauman on 16/05/15.
 */
public class StoreTiming implements Serializable
{
    private String type;
    private ArrayList<TimeRange> ranges;

    public StoreTiming(OloCalendar calender)
    {
        this.type = calender.getType();
        this.ranges = new ArrayList<>();
        if(calender.getRanges() != null)
        {
            for (OloTimeRange timeRange : calender.getRanges())
            {
                TimeRange storeTimeRange = new TimeRange(timeRange);
                this.ranges.add(storeTimeRange);
            }
        }
    }
    public String getType() {
        return type;
    }

    public ArrayList<TimeRange> getRanges() {
        return ranges;
    }
}
