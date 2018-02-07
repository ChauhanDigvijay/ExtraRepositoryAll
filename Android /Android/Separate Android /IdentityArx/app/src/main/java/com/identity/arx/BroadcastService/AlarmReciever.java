package com.identity.arx.BroadcastService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.identity.arx.UserCalander.SetUserCalanderData;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.notificationservice.LectureScheduleNotification;


public class AlarmReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        new LectureScheduleNotification(context).currentLecture();
        if (CalanderFormat.getWeekDayViaDate(CalanderFormat.getDashCalender()).equalsIgnoreCase("Sunday")) {
            SetUserCalanderData.setUserEventCalander(context);
        }
    }
}
