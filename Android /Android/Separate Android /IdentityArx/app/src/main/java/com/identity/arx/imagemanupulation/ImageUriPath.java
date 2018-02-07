package com.identity.arx.imagemanupulation;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ImageUriPath {
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Set<String> getfile(File dir) {
        Set<String> fileList = new HashSet();
        File[] listFile = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            int i = 0;
            while (i < listFile.length) {
                if (listFile[i].getName().endsWith(".png") || listFile[i].getName().endsWith(".jpg") || listFile[i].getName().endsWith(".jpeg") || listFile[i].getName().endsWith(".gif")) {
                    fileList.add(listFile[i].getAbsolutePath());
                }
                i++;
            }
        }
        return fileList;
    }

    public static Set<String> getMp4file(File dir) {
        Set<String> fileList = new HashSet();
        File[] listFile = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            int i = 0;
            while (i < listFile.length) {
                if (listFile[i].getName().endsWith(".mp3") || listFile[i].getName().endsWith(".mp4")) {
                    fileList.add(listFile[i].getAbsolutePath());
                }
                i++;
            }
        }
        return fileList;
    }
}
