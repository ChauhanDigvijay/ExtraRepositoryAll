package com.identity.arx.general;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimeTrap {
    public static long getTimeDifference(String time1, String time2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            return TimeUnit.MILLISECONDS.toMinutes(format.parse(time2).getTime() - format.parse(time1).getTime());
        } catch (Exception e) {
            return 0;
        }
    }

    public static String convertTime24to12(String time) {
        try {
            return new SimpleDateFormat("h:mm a").format(new SimpleDateFormat("HH:mm:ss").parse(time)).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Time Stamp";
        }
    }

    public static String getAdditionTime(int addTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(12, 10);
        return new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))) * Math.sin(dLng / 2.0d)) * Math.sin(dLng / 2.0d));
        return 6371000.0d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)));
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double dLat = Math.toRadians((double) (lat2 - lat1));
        double dLng = Math.toRadians((double) (lng2 - lng1));
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians((double) lat1)) * Math.cos(Math.toRadians((double) lat2))) * Math.sin(dLng / 2.0d)) * Math.sin(dLng / 2.0d));
        return (float) (6371000.0d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a))));
    }
}
