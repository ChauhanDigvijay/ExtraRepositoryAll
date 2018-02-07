package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloTimeRange;

import java.io.Serializable;

/**
 * Created by Nauman on 16/05/15.
 */
public class TimeRange implements Serializable
{
    private String start;
    private String end;
    private String weekday;

    public TimeRange(OloTimeRange timeRange)
    {
        this.start = timeRange.getStart();
        this.end = timeRange.getEnd();
        this.weekday = timeRange.getWeekday();
    }

    public String getStart()
    {
        return start;
    }

    public String getEnd()
    {
        return end;
    }

    public String getWeekday()
    {
        return weekday;
    }

    public String getFullWeekDayName()
    {
        String fullName = weekday;
        if (weekday.equalsIgnoreCase("Sun"))
        {
            fullName = "Sunday";
        }
        else if (weekday.equalsIgnoreCase("Mon"))
        {
            fullName = "Monday";
        }
        else if (weekday.equalsIgnoreCase("Tue"))
        {
            fullName = "Tuesday";
        }
        else if (weekday.equalsIgnoreCase("Wed"))
        {
            fullName = "Wednesday";
        }
        else if (weekday.equalsIgnoreCase("Thu"))
        {
            fullName = "Thursday";
        }
        else if (weekday.equalsIgnoreCase("Fri"))
        {
            fullName = "Friday";
        }
        else if (weekday.equalsIgnoreCase("Sat"))
        {
            fullName = "Saturday";
        }
        return fullName;
    }
}