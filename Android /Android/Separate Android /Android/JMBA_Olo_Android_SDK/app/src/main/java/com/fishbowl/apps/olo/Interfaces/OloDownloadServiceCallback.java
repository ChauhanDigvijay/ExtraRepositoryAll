package com.fishbowl.apps.olo.Interfaces;

import java.io.File;

/**
 * Created by Nauman Afzaal on 29/04/15.
 */
public interface OloDownloadServiceCallback
{
    public void onDownloadCompetedCallback(File file, Exception exception);
}
