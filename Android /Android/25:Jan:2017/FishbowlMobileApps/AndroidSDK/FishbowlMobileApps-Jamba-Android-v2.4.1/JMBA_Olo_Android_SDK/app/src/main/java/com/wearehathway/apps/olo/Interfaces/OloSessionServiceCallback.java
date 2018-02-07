package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloUser;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public interface OloSessionServiceCallback
{
    public void onSessionServiceCallback(OloUser user, Exception exception);
}
