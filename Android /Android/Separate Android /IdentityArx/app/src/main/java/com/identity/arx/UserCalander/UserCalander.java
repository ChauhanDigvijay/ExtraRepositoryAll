package com.identity.arx.UserCalander;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class UserCalander {
    public static long setUserCalander(Context mContext, String startDatetime, String endDateTime, String location) {
        try {
            Uri baseUri;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date theDate = format.parse(startDatetime);
            Calendar myCal = new GregorianCalendar();
            myCal.setTime(theDate);
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(myCal.get(1), myCal.get(2), myCal.get(5), myCal.get(10), myCal.get(12));
            long startMillis = beginTime.getTimeInMillis();
            Date theDate2 = format.parse(endDateTime);
            Calendar myCal2 = new GregorianCalendar();
            myCal2.setTime(theDate2);
            Calendar beginTime2 = Calendar.getInstance();
            beginTime2.set(myCal2.get(1), myCal2.get(2), myCal2.get(5), myCal2.get(10), myCal2.get(12));
            long endMillis = beginTime2.getTimeInMillis();
            ContentValues event = new ContentValues();
            event.put("calendar_id", Integer.valueOf(1));
            event.put("title", "Lecture Schedule");
            event.put("description", "");
            event.put("eventLocation", location);
            event.put("dtstart", Long.valueOf(startMillis));
            event.put("dtend", Long.valueOf(endMillis));
            event.put("allDay", Integer.valueOf(0));
            event.put("hasAlarm", Integer.valueOf(1));
            event.put("eventTimezone", TimeZone.getDefault().getID());
            if (VERSION.SDK_INT >= 8) {
                baseUri = Uri.parse("content://com.android.calendar/events");
            } else {
                baseUri = Uri.parse("content://calendar/events");
            }
            return Long.valueOf(mContext.getContentResolver().insert(baseUri, event).getLastPathSegment()).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int DeleteCalendarEntry(int entryID, Context mContext) {
        return mContext.getContentResolver().delete(ContentUris.withAppendedId(Uri.parse(getCalendarUriBase() + ""), (long) entryID), null, null);
    }

    private static Uri getCalendarUriBase() {
        if (VERSION.SDK_INT >= 8) {
            return Uri.parse("content://com.android.calendar/events");
        }
        return Uri.parse("content://calendar/events");
    }
}
