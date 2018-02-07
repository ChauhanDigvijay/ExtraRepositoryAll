package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloUser;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public interface OloUserServiceCallback
{
    public void onUserServiceCallback(OloUser user, Exception exception);
}
