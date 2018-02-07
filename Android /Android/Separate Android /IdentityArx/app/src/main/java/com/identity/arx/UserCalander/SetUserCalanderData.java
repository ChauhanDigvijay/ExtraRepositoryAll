package com.identity.arx.UserCalander;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.objectclass.LectureScheduleObject;

import java.util.HashSet;
import java.util.Set;

public class SetUserCalanderData {
    public static void setUserEventCalander(Context mContext) {
        Set<String> arrayList = new HashSet();
        arrayList.clear();
        SharedPreferences sharedPreference = AppSharedPrefrence.getSharedPrefrence(mContext);
        arrayList = sharedPreference.getStringSet("CALANDER_EVENT_ID", arrayList);
        for (String valStr : arrayList) {
            UserCalander.DeleteCalendarEntry(Integer.parseInt(valStr), mContext);
        }
        String[] aalWeelDyas = CalanderFormat.getWeekDays();
        LectureDetailTable lectureDetailTable = new LectureDetailTable(mContext);
        arrayList.clear();
        for (String getweekdate : aalWeelDyas) {
            for (LectureScheduleObject lectureScheduleObject : lectureDetailTable.getLectureDetails(CalanderFormat.getWeekDayViaDate(getweekdate))) {
                arrayList.add(UserCalander.setUserCalander(mContext, getweekdate + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + lectureScheduleObject.getLectureStartTime(), getweekdate + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + lectureScheduleObject.getLectureEndTime(), lectureScheduleObject.getLectureLocation()) + "");
            }
        }
        Editor edit = sharedPreference.edit();
        edit.putStringSet("CALANDER_EVENT_ID", arrayList);
        edit.commit();
    }
}
