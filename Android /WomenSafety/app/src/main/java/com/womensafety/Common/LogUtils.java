package com.womensafety.Common;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogUtils {
    public static boolean isLogEnable = false;

    public static void errorLog(String tag, String msg) {
        if (isLogEnable) {
            Log.e("" + tag, "" + msg);
        }
    }

    public static void infoLog(String tag, String msg) {
        if (isLogEnable) {
            Log.i(tag, msg);
        }
    }

    public static void printMessage(String msg) {
        if (isLogEnable) {
            System.out.println(msg);
        }
    }

    public static void setLogEnable(boolean isEnable) {
        isLogEnable = isEnable;
    }

    public static void convertResponseToFile(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Response.xml");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] byt = new byte[1024];
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

    public static void convertRequestToFile(String is) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("/sdcard/Request.xml"));
        fos.write(is.getBytes());
        fos.close();
    }

    public static StringBuffer getDataFromInputStream(InputStream inpStrData) {
        if (inpStrData != null) {
            try {
                BufferedReader brResp = new BufferedReader(new InputStreamReader(inpStrData));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String stringTemporaryVariable = brResp.readLine();
                    if (stringTemporaryVariable != null) {
                        stringBuffer.append(stringTemporaryVariable);
                    } else {
                        brResp.close();
                        inpStrData.close();
                        return stringBuffer;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorLog("LogUtils", "Exception occured in getDataFromInputStream(): " + e.toString());
            }
        }
        return null;
    }
}
