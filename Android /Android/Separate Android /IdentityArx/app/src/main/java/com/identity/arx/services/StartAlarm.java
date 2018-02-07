package com.identity.arx.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.identity.arx.BroadcastService.AlarmReciever;


public class StartAlarm {
    private PendingIntent pendingIntent;

    public void startTracking(Context ctx) {
        this.pendingIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(ctx, AlarmReciever.class), 0);
        ((AlarmManager) ctx.getSystemService("alarm")).setInexactRepeating(0, System.currentTimeMillis(), (long) 100000, this.pendingIntent);
    }
}
