package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman on 16/05/15.
 */
public class OloCalendar {
    private String type;
    private ArrayList<OloTimeRange> ranges;

    public String getType() {
        return type;
    }

    public ArrayList<OloTimeRange> getRanges() {
        return ranges;
    }
}
