package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloFave;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public interface OloUserFavesCallback
{
    public void onUserFavesCallback(OloFave[] faves, Exception exception);
}
