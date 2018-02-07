package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloFaveLocation;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public interface OloUserFaveLocationsCallback
{
    public void onUserFaveLocationsCallback(ArrayList<OloFaveLocation> locations, Exception exception);
}
