package com.fishbowl.apps.olo.Services;

import android.content.Context;

import com.koushikdutta.ion.ProgressCallback;
import com.fishbowl.apps.olo.Interfaces.OloDownloadServiceCallback;
import com.fishbowl.apps.olo.Misc.Decompress;
import com.fishbowl.apps.olo.Utils.Constants;

import java.io.File;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloExportService
{
    public static void vendorExport(Context context, final String location, ProgressCallback progressCallback, final OloDownloadServiceCallback callback)
    {
        final String fileName = "export.zip";
        String loc = location + "/" + fileName;
        File fileLocation = new File(loc);
        fileLocation.delete();// Delete if any file with same name exists.
        File downloadLocationFile = new File(location);
        if (downloadLocationFile.mkdirs() || downloadLocationFile.isDirectory())
        {
            OloService.getInstance().download(context, Constants.EXPORT, loc, progressCallback, new OloDownloadServiceCallback()
            {
                @Override
                public void onDownloadCompetedCallback(File file, Exception exception)
                {
                    unzipFile(file, callback);
                }
            });
        }
        else
        {
            if (callback != null)
            {
                callback.onDownloadCompetedCallback(null, new Exception("Invalid download location."));
            }
        }
    }

    public static void unzipFile(File zipFile, final OloDownloadServiceCallback callback)
    {
        Exception exception = null;
        File unzippedFile = null;
        boolean isUnZipped = false;
        if (zipFile.exists())
        {
            Decompress decompress = new Decompress();
            if (decompress.isValidZipFile(zipFile))
            {
                String location = zipFile.getParent();
                isUnZipped = decompress.unpackZip(location, zipFile.getName());
                if (isUnZipped)
                {
                    unzippedFile = new File(location);
                }
            }
            if (zipFile != null)
            {
                zipFile.delete();// Delete zip file after unzipping.
            }
        }
        if (!isUnZipped)
        {
            exception = new Exception("Invalid zip file.");
        }
        if (callback != null)
        {
            callback.onDownloadCompetedCallback(unzippedFile, exception);
        }
    }
}
