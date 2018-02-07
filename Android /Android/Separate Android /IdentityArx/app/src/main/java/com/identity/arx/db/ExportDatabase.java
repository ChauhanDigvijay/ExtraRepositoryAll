package com.identity.arx.db;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ExportDatabase {
    public static void exportDB(Context mContext, String DbName, String packageName) {
        File sd = Environment.getExternalStorageDirectory();
        String backupDBPath = DbName;
        File currentDB = new File(Environment.getDataDirectory(), "/data/" + packageName + "/databases/" + DbName);
        File backupDB = new File(sd, backupDBPath);
        try {
            FileChannel source = new FileInputStream(currentDB).getChannel();
            FileChannel destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, e + "", 1).show();
        }
    }
}
