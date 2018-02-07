package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloUser;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public interface OloSessionServiceCallback
{
    public void onSessionServiceCallback(OloUser user, Exception exception);
}
