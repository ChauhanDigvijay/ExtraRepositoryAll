package com.olo.jambajuice.BusinessLogic.Interfaces;

import android.location.Location;

/**
 * Created by Nauman on 17/05/15.
 */
public interface LocationUpdatesCallback {
    public void onLocationCallback(Location location);

    public void onConnectionFailedCallback();
}
