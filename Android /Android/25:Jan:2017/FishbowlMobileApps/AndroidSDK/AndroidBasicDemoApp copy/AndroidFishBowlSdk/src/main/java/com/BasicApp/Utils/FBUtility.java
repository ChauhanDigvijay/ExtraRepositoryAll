package com.BasicApp.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//Created By Mohd Vaseem
public class FBUtility {

    public static FBUtility instance;

    public static FBUtility sharedInstance() {
        if (instance == null) {
            instance = new FBUtility();
        }
        return instance;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo netInfo = null;
        try {
            ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = connec.getActiveNetworkInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (netInfo != null && netInfo.isAvailable() == true) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //  android device id for// customer
    public static String getAndroidDeviceID(Context context) {
        String android_id = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        return android_id;
    }


    public static boolean isLocationServiceProviderAvailable(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean providerAvailable = false;
        final List<String> providers = manager.getProviders(true);
        for (final String provider : providers) {
            if (manager.isProviderEnabled(provider)
                    && provider.equals(LocationManager.GPS_PROVIDER)) {
                providerAvailable = true;
            }
        }
        return providerAvailable;
    }

    public static String getDeviceCarier(Context context) {
        String device_carrier = "";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getNetworkOperatorName() != null) {
            String provider = tm.getNetworkOperatorName();
            if (provider == null) {
            } else {
                if (!provider.equalsIgnoreCase("null")) {
                    device_carrier = tm.getNetworkOperatorName();// getNetworkOperatorName()
                }
            }
        }
        return device_carrier;
    }

    public static String getAndroidDeviceName() {
        String deviceName = "";

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            deviceName = capitalize(model);
        } else {
            deviceName = capitalize(manufacturer) + " " + model;
        }
        if (deviceName != null && !deviceName.equalsIgnoreCase("null")) {
            return deviceName;
        } else {
            return "";
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char first = str.charAt(0);
        if (Character.isUpperCase(first)) {
            return str;
        } else {
            return Character.toUpperCase(first) + str.substring(1);
        }
    }

    public static String formatedCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);// "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String formatedDate = formatter.format(date);
        return formatedDate;
    }

    public static boolean checkPlayServices(Context context) {
        // Getting status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        // Showing status
        if (status == ConnectionResult.SUCCESS)
            return true;
        else {
            return false;
        }
    }

    public static boolean isBLESupportedDevice(Context context) {
        // GEt current API level of device
        int currentapiVersion = Build.VERSION.SDK_INT;
        // Check API level is > than 18
        if ((currentapiVersion > 18)
                && (context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))) {
            return true;
        }
        return false;
    }

    public static void enableBLEDevice(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if ((mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
                && FBUtility.isBLESupportedDevice(context)) {
            mBluetoothAdapter.enable();
        }
    }

    public static boolean isBluetoothEnabled(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()
                || !FBUtility.isBLESupportedDevice(context)) {
            return false;
        } else {
            return true;
        }
    }

    // Get Application Name
    public static String getAppName(Context context) {
        CharSequence appname = context.getPackageManager().getApplicationLabel(
                context.getApplicationInfo());

        return (String) appname;
    }


    // Get ring tone file names
    public static Uri getNotificationURI(Context context, String soundfileNameFn) {

        Uri notificationuri = null;
        if (soundfileNameFn == null || soundfileNameFn.length() == 0
                || soundfileNameFn.contains("nosound")) {
            return null;// nosound
        }
        if (soundfileNameFn.equals("chime")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/chime");
        } else if (soundfileNameFn.equals("bell")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/bell");
        } else if (soundfileNameFn.equals("crickets")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/crickets");
        } else if (soundfileNameFn.equals("vibrate")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/vibrate");
        } else if (soundfileNameFn.equals("tring")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/tring");
        } else if (soundfileNameFn.equals("double bell")) {
            notificationuri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/raw/double_bell");
        } else {
            notificationuri = null;// default
        }
        return notificationuri;
    }

    public static void playAndroidSound(Context context, String fileName) {
        try {
            Uri notificationuri = getNotificationURI(context, fileName);
            // //Play ringtone when pass book is open
            if (notificationuri != null) {
                Ringtone ringtone;
                ringtone = RingtoneManager
                        .getRingtone(context, notificationuri);
                ringtone.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int inDp(int Whatever_valueInDP, Context context) {
        int Value_In_Pixel = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, Whatever_valueInDP, context.getResources()
                        .getDisplayMetrics());

        return Value_In_Pixel;

    }

    public static void alertBox(String title, String message, Context context) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
