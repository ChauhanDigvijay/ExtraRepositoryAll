package com.wearehathway.apps.olo;

import com.koushikdutta.ion.ProgressCallback;
import com.wearehathway.apps.olo.Interfaces.OloDownloadServiceCallback;
import com.wearehathway.apps.olo.Services.OloExportService;
import com.wearehathway.apps.olo.Utils.Logger;

import junit.framework.Assert;

import java.io.File;

/**
 * Created by Nauman Afzaal on 29/04/15.
 */
public class OloExportServiceTests extends BaseTests
{
    public void testExportFileDownload()
    {
        final String location = getContext().getFilesDir().getAbsolutePath() + "/export";
        Logger.i(location);

        OloExportService.vendorExport(getContext(), location, new ProgressCallback()
        {
            @Override
            public void onProgress(long downloaded, long total)
            {
                Logger.i("Progress: " + downloaded + " / " + total);
            }
        }, new OloDownloadServiceCallback()
        {
            @Override
            public void onDownloadCompetedCallback(File file, Exception exception)
            {
                Assert.assertNull("Vendor Export failed", exception);
                Assert.assertNotNull("Invalid File Downloaded", file);
                Assert.assertEquals("Invalid Download Path", file.getAbsolutePath(), location);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testInvalidFileLocation()
    {
        final String location = "/someInvalidLocation/export";
        OloExportService.vendorExport(getContext(), location, null, new OloDownloadServiceCallback()
        {
            @Override
            public void onDownloadCompetedCallback(File file, Exception exception)
            {
                Assert.assertNotNull("Invalid download location", exception);
            }
        });
    }

    public void testInvalidFileUnzip()
    {
        File invalidFile = new File("/someInvalidFilePath");
        OloExportService.unzipFile(invalidFile, new OloDownloadServiceCallback()
        {
            @Override
            public void onDownloadCompetedCallback(File file, Exception exception)
            {
                Assert.assertNotNull("Invalid download location", exception);
            }
        });
    }
}