package com.womensafety.Common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtils {
    public static int getInt(String str) {
        int value = 0;
        if (str == null || str.equalsIgnoreCase("")) {
            return value;
        }
        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while parsing as integer" + e.toString());
        }
        return value;
    }

    public static boolean getBoolean(String str) {
        boolean value = false;
        if (str == null || str.equalsIgnoreCase("")) {
            return value;
        }
        try {
            value = Boolean.parseBoolean(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while parsing as boolean" + e.toString());
        }
        return value;
    }

    public String getImageNameFromUrl(String url) {
        String imgName = "";
        if (url.contains("*/")) {
            return url.substring(url.indexOf("/img/") + 5).replace("/", "").replace("*", "");
        }
        imgName = url.substring(url.lastIndexOf("/"));
        return imgName.substring(0, imgName.indexOf("."));
    }

    public static String getString(boolean str) {
        String value = "";
        try {
            value = String.valueOf(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while getString" + e.toString());
        }
        return value;
    }

    public static String getString(int str) {
        String value = "";
        try {
            value = String.valueOf(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while getString" + e.toString());
        }
        return value;
    }

    public static boolean isValidEmail(String string) {
        return Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+").matcher(string).matches();
    }

    public static float getFloat(String string) {
        float value = 0.0f;
        if (string == null || string.equalsIgnoreCase("") || string.equalsIgnoreCase(".") || string.equalsIgnoreCase("null")) {
            return value;
        }
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static long getLong(String string) {
        long value = 0;
        if (string == null || string.equalsIgnoreCase("")) {
            return value;
        }
        try {
            value = Long.parseLong(string);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while getLong" + e.toString());
        }
        return value;
    }

    public static int toInt(String str) {
        int value = -1;
        if (str == null || str.equalsIgnoreCase("")) {
            return value;
        }
        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while toInt" + e.toString());
        }
        return value;
    }

    public static String checkString(String checkString) {
        return checkString != null ? checkString : "";
    }

    public static String setSuperScriptForNumber(int num) {
        String supStr = num + "";
        switch (num) {
            case 1:
                return supStr + "<sup><small>st</small></sup>";
            case 2:
                return supStr + "<sup><small>nd</small></sup>";
            case 3:
                return supStr + "<sup><small>rd</small></sup>";
            default:
                return supStr + "<sup><small>th</small></sup>";
        }
    }

    public static void write(String data, BufferedWriter out) throws IOException {
        out.write(data);
        out.newLine();
    }

    public static void printViaBluetooth(Context context, String fileName) {
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            try {
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + fileName));
                Intent int_email = new Intent();
                int_email.setAction("android.intent.action.SEND");
                int_email.setType("image/*");
                int_email.putExtra("android.intent.extra.STREAM", uri);
                if (Build.MODEL.equalsIgnoreCase("GT-P1000")) {
                    int_email.setClassName("com.android.bluetooth", "com.broadcom.bt.app.opp.OppLauncherActivity");
                } else {
                    int_email.setClassName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                }
                ((Activity) context).startActivityForResult(int_email, 77);
            } catch (Exception e) {
            }
        }
    }

    public static double getDouble(String str) {
        double value = 0.0d;
        if (str == null || str.equalsIgnoreCase("")) {
            return value;
        }
        try {
            value = Double.parseDouble(str);
        } catch (Exception e) {
            LogUtils.errorLog("StringUtils", "Error occured while parsing as integer" + e.toString());
        }
        return value;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        String responce = "";
        if (inputStream == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (true) {
                int n = reader.read(buffer);
                if (n == -1) {
                    break;
                }
                writer.write(buffer, 0, n);
            }
            responce = writer.toString();
            writer.close();
            return responce;
        } finally {
            inputStream.close();
        }
    }

    public static String getUniqueUUID() {
        return UUID.randomUUID().toString();
    }

    public static String removeSpecialChars(String xmlString) {
        String temp = xmlString;
        try {
            return xmlString.replaceAll("[|^$*&']", "");
        } catch (Exception e) {
            e.printStackTrace();
            return xmlString;
        }
    }
}
