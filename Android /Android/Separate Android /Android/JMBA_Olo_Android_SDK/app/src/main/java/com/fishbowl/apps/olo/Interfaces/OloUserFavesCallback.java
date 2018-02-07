package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloFave;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public interface OloUserFavesCallback
{
    public void onUserFavesCallback(OloFave[] faves, Exception exception);
}
