package com.wearehathway.apps.olo.Misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Nauman Afzaal on 29/04/15.
 */
public class Decompress
{
    public boolean unpackZip(String location, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(location + "/" + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                // Need to create directories if not exists
                File fmd = new File(location +"/" + filename);
                if (ze.isDirectory())
                {
                    fmd.mkdirs();
                    continue;
                }
                else if(fmd.exists())
                {
                    fmd.delete();// Delete file with same name.
                }
                FileOutputStream fout = new FileOutputStream(location + "/" + filename);
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isValidZipFile(final File file)
    {
        ZipFile zipfile = null;
        try
        {
            zipfile = new ZipFile(file);
            return true;
        } catch (Exception e)
        {
            return false;
        } finally
        {
            try
            {
                if (zipfile != null)
                {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e)
            {
            }
        }
    }
}
