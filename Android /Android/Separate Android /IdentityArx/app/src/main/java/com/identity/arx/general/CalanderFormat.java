package com.identity.arx.general;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalanderFormat {
    public static String getDateAndMonthCalender() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    public static String getDashCalender() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getSlashCalender() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    public static String getDayofWeek() {
        return Calendar.getInstance().getDisplayName(7, 2, Locale.getDefault());
    }

    public static String getDateTimeCalender() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000").format(new Date());
    }

    public static String getTimeCalender() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static String getTimeHourCalender() {
        return new SimpleDateFormat("HH").format(new Date());
    }

    public static String getYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getMonth() {
        return new SimpleDateFormat("MM").format(new Date());
    }

    public static String getMonthName() {
        return new SimpleDateFormat("MMM").format(new Date());
    }

    public static Date convertStringToDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static List<String> getdaysListBetweenDates(String str_date, String end_date) {
        List<String> stringDates = new ArrayList();
        List<Date> dates = new ArrayList();
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(str_date);
            long endTime = formatter.parse(end_date).getTime();
            for (long curTime = startDate.getTime(); curTime <= endTime; curTime += 86400000) {
                dates.add(new Date(curTime));
            }
            for (int i = 0; i < dates.size(); i++) {
                stringDates.add(formatter.format((Date) dates.get(i)));
            }
        } catch (Exception e) {
        }
        return stringDates;
    }

    public static String[] getWeekDays() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        now.add(5, (-now.get(7)) + 2);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(5, 1);
        }
        return days;
    }

    public static String getWeekDayViaDate(String dateInString) {
        try {
            Date now = new SimpleDateFormat("yyyy-MM-dd").parse(dateInString);
            Calendar.getInstance().setTime(now);
            return new SimpleDateFormat("EEEE").format(now);
        } catch (Exception e) {
            return null;
        }
    }
}
