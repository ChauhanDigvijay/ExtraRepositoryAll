package com.womensafety.httpimage;

import android.content.Context;
import android.os.Environment;

import com.womensafety.Common.CalendarUtils;
import com.womensafety.Common.LogUtils;
import com.womensafety.Common.StringUtils;
import com.womensafety.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilesStorage {
    public static String BACK_UP;
    public static String DOWNLOADS;
    public static String EOT_BACK_UP;
    public static String IMAGE_CACHE_DIR = "";
    public static String IMAGE_FAVOURITE_DIR;
    public static String ROOT_DIR;
    public static String SDCARD_ROOT;
    public static String TEMP_DIR;

    public static void CreateStorageDirs(Context context) {
        if (Environment.getExternalStorageState().equalsIgnoreCase("removed")) {
            SDCARD_ROOT = context.getFilesDir().toString() + "/";
        } else {
            SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + "/";
        }
        ROOT_DIR = SDCARD_ROOT + "Blase/";
        IMAGE_CACHE_DIR = ROOT_DIR + "ImageCache/";
        IMAGE_FAVOURITE_DIR = ROOT_DIR + "Favourite/";
        TEMP_DIR = ROOT_DIR + "Temp/";
        DOWNLOADS = ROOT_DIR + "Downloads/";
        BACK_UP = ROOT_DIR + "Backup/";
        EOT_BACK_UP = ROOT_DIR + "EotbackUp/";
        new File(ROOT_DIR).mkdirs();
        new File(BACK_UP).mkdirs();
        new File(EOT_BACK_UP).mkdirs();
        if (!new File(DOWNLOADS).isDirectory()) {
            new File(DOWNLOADS).mkdirs();
        }
    }

    public static void clearDir(String dirPath) {
        try {
            File[] filesList = new File(dirPath).listFiles();
            for (File delete : filesList) {
                try {
                    delete.delete();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
        }
    }

    public static void copy(String source, String destination) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
        FileOutputStream fos = new FileOutputStream(destination);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] byt = new byte[8192];
        while (true) {
            int noBytes = bis.read(byt);
            if (noBytes != -1) {
                bos.write(byt, 0, noBytes);
            } else {
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
                return;
            }
        }
    }

    public static void deleteOldDatabse(String source) {
        File[] filesFound = new File(source).listFiles();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                try {
                    long diffrence = CalendarUtils.getCurrentTimeInMilli() - StringUtils.getLong(file.getName().split("_")[0]);
                    if (diffrence > 0) {
                        diffrence = (long) (((int) diffrence) / 86400000);
                    }
                    if (diffrence > 6 || diffrence == 0) {
                        file.delete();
                    }
                    LogUtils.errorLog("diffrence", "" + diffrence);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getRootDirector() {
        if (Environment.getExternalStorageState().equalsIgnoreCase("removed")) {
            SDCARD_ROOT = MyApplication.mContext.getFilesDir().toString() + "/";
        } else if (MyApplication.mContext.getExternalCacheDir() != null) {
            SDCARD_ROOT = MyApplication.mContext.getExternalCacheDir().toString() + "/";
        } else {
            SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + "/";
        }
        ROOT_DIR = SDCARD_ROOT + "Pran/";
        return ROOT_DIR;
    }

    public static String getTempDownload() {
        if (Environment.getExternalStorageState().equalsIgnoreCase("removed")) {
            SDCARD_ROOT = MyApplication.mContext.getFilesDir().toString() + "/";
        } else {
            SDCARD_ROOT = MyApplication.mContext.getExternalCacheDir().toString() + "/";
        }
        ROOT_DIR = SDCARD_ROOT + "EMS/";
        TEMP_DIR = ROOT_DIR + "TempDownload/";
        return TEMP_DIR;
    }

    public static String getImageCacheDirectory() {
        IMAGE_CACHE_DIR = getRootDirector() + "ImageCache/";
        return IMAGE_CACHE_DIR;
    }

    public static String getHttpCacheDirectory() {
        IMAGE_CACHE_DIR = getRootDirector() + "Http/";
        return IMAGE_CACHE_DIR;
    }
}
