package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloCalendar;
import com.fishbowl.apps.olo.Models.OloTimeRange;

import java.util.ArrayList;

/**
 * Created by VT027 on 5/22/2017.
 */

public class StoreTiming {
    private String type;
    private ArrayList<TimeRange> ranges;

    public StoreTiming(OloCalendar calender) {
        this.type = calender.getType();
        this.ranges = new ArrayList<>();
        if (calender.getRanges() != null) {
            for (OloTimeRange timeRange : calender.getRanges()) {
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
