package com.identity.arx.general;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;

import com.identity.arx.R;



public class AppNotification {
    public static void setNotificationMessage(Context mContext, String title, String message, int notiFyID, Class<?> cls) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService("notification");
        CharSequence notiText = "Location Alert";
        long meow = System.currentTimeMillis();
        notificationManager.cancel(notiFyID);
        String contentTitle = title;
        String contentText = message;
        int SERVER_DATA_RECEIVED = notiFyID;
        notificationManager.notify(SERVER_DATA_RECEIVED, new Builder(mContext).setSmallIcon(R.drawable.profile_icon_28).setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setStyle(new BigTextStyle().bigText(message)).setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(mContext, cls), 0)).setSound(RingtoneManager.getDefaultUri(2)).setSmallIcon(R.drawable.profile_icon_28).setPriority(1).setContentText(message).build());
    }
}
