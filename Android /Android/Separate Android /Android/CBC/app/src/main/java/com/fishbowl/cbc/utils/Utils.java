package com.fishbowl.cbc.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fishbowl.apps.olo.Utils.Logger;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.fishbowl.cbc.BuildConfig;
import com.fishbowl.cbc.CbcApplication;
import com.fishbowl.cbc.R;
import com.fishbowl.cbc.activities.nonGeneric.home.HomeActivity;
import com.fishbowl.cbc.businesslogic.analytics.AnalyticsManager;
import com.fishbowl.cbc.businesslogic.models.Store;
import com.fishbowl.cbc.businesslogic.services.UserService;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.fishbowl.apps.olo.Utils.Constants.OLO_INVALID_AUTH_STATUS_CODE;
import static com.fishbowl.apps.olo.Utils.Constants.Server_Time_Format;
import static com.fishbowl.cbc.utils.Constants.AVATAR_ICONS;
import static com.fishbowl.cbc.utils.Constants.BROADCAST_REMOVE_BASKET_UI;
import static com.fishbowl.cbc.utils.Constants.BROADCAST_UPDATE_HOME_ACTIVITY;
import static com.fishbowl.cbc.utils.Constants.PASSWORD_MAX_LENGTH;
import static com.fishbowl.cbc.utils.Constants.PASSWORD_MIN_LENGTH;
import static com.fishbowl.cbc.utils.Constants.PHONE_NUMBER_LENGTH;

/**
 * Created by VT027 on 5/22/2017.
 */

public class Utils {
    public static final int MILISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static boolean ispresent = false;
    public static Activity instance;

    public static void showAlert(Context context, String message) {
        showAlert(context, message, null);
    }

    public static void showAlert(Context context, String message, String title) {
        try {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
            if (title != null) {
                alertdialog.setTitle(title);
            }
            alertdialog.setMessage(message).setPositiveButton(android.R.string.yes, null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorAlert(final Context context, Exception exception) {
        if (!tryHandlingAuthTokenExpiry(context, exception)) {
            String error = Utils.getErrorDescription(exception);
            Utils.showAlert(context, error, "Error");
        }
    }

    public static void showErrorAlert(final Activity activity, Exception exception) {
        if (!tryHandlingAuthTokenExpiry(activity.getApplicationContext(), exception)) {
            String error = Utils.getErrorDescription(exception);
            Utils.showAlert(activity, error, "Error");
        }
    }

    public static void showErrorAlert(final Activity activity, String exception) {
        Utils.showAlert(activity, exception, "Error");
    }

    public static boolean tryHandlingAuthTokenExpiry(final Context context, Exception exception) {
        int errorCode = getErrorCode(exception);
        if (errorCode == OLO_INVALID_AUTH_STATUS_CODE) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
            alertdialog.setTitle("Error");
            alertdialog.setCancelable(false);
            alertdialog.setMessage("Your session has expired. Please login to continue.").setPositiveButton(android.R.string.ok, null).setIcon(android.R.drawable.ic_dialog_alert).show();
            return true;
        }
        return false;
    }

//    public static boolean checkEnableGPS(Context context) {
//        int locationMode = 0;
//        String locationProviders = null;
//        boolean isEnable = false;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//                if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
//                    isEnable = true;
//                }
//
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            locationProviders = Settings.Secure.getString(context.getContentResolver(),
//                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//
//            if (locationProviders.contains(android.location.LocationManager.GPS_PROVIDER) && locationProviders.contains(android.location.LocationManager.NETWORK_PROVIDER)) {
//                isEnable = true;
//            }
//
//        }
//
//        return isEnable;
//    }

    public static boolean checkEnableGPS(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo netInfo = null;

        try {
            ConnectivityManager ex = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = ex.getActiveNetworkInfo();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return netInfo != null && netInfo.isAvailable();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidCellPhone(String number) {
        return !TextUtils.isEmpty(number) && android.util.Patterns.PHONE.matcher(number).matches() && (number.trim().length() == PHONE_NUMBER_LENGTH);
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.trim().length() >= PASSWORD_MIN_LENGTH && password.trim().length() <= PASSWORD_MAX_LENGTH;
    }

    public static boolean isValidZip(String zip) {
        return !TextUtils.isEmpty(zip) && zip.trim().length() == 5;
    }

    public static boolean isValidCVV(String cvv) {
        return !TextUtils.isEmpty(cvv) && cvv.trim().length() >= 3 && cvv.trim().length() <= 4;
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return !TextUtils.isEmpty(cardNumber) && cardNumber.trim().length() >= 12 && cardNumber.trim().length() <= 19;
    }

    public static boolean isValidName(String string) {
        return string.matches(".*\\w.*");
    }

    public static boolean isEmptyString(String string) {
        return string.trim().length() == 0;
    }

    /**
     * TODO: Store Location - get direction for selected store
     * @param d1
     * @param d2
     * @return
     */
//    public static void getDirection(Activity activity, Store selectedStore) {
//        if (selectedStore != null && selectedStore.getName() != null) {
//            AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.STORES.value, "get_directions", selectedStore.getName());
//            Double latitude = selectedStore.getLatitude();
//            Double longitude = selectedStore.getLongitude();
//
//            String labelLocation = "";
//            if (DataManager.getInstance().isDebug) {
//                labelLocation = Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", "");
//            } else {
//                labelLocation = selectedStore.getName().replace("Jamba Juice ", "");
//            }
//
//            PackageManager manager = activity.getPackageManager();
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + latitude + ">,<" + longitude + ">?q=<" + latitude + ">,<" + longitude + ">(" + labelLocation + ")"));
//            List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);
//            if (info.size() > 0) {
//                //Then there is application can handle your intent
//                activity.startActivity(intent);
//            } else {
//                //No Application can handle your intent
//                Toast.makeText(activity, "There no application installed on your device to show direction.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    public static int daysBetween(Date d1, Date d2) {

        return (int) ((d2.getTime() - d1.getTime()) / MILISECONDS_IN_DAY);
    }


    public static Date getDateFromStringSlash(String strDate, String format) {
        if (!StringUtilities.isValidString(strDate))
            return null;

        if (format == null)
            format = "yyyy/MM/dd hh:mm:ss";


        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromString(String strDate, String format) {
        if (!StringUtilities.isValidString(strDate))
            return null;

        if (format == null)
            if (strDate.indexOf("-") > 0) {
                format = "yyyy-MM-dd hh:mm:ss";
            } else {
                format = "MM/dd/yyyy hh:mm:ss";
            }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static void openMap(Activity activity, String searchString) {
        if (searchString == null || searchString.trim().equals("")) {
            searchString = "Jamba Juice";
        } else {
            searchString = "Jamba Juice near " + searchString;
        }
        PackageManager manager = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + 0 + ">,<" + 0 + ">?q=" + searchString));
        List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);
        if (info.size() > 0) {
            //Then there is application can handle your intent
            activity.startActivity(intent);
        } else {
            //No Application can handle your intent
            Toast.makeText(activity, "There no application installed on your device to show direction.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showDialerConfirmation(Activity activity, final Store selectedStore) {
        if (selectedStore != null) {
            instance = activity;
            //JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(selectedStore.getId()),selectedStore.getName(),FBEventSettings.CALL_STORE);
            AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.STORES.value, "call_store", selectedStore.getName());
            final String telephoneNumber = selectedStore.getTelephone();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setMessage("Do you want to make a call to " + selectedStore.getName() + "?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermission()) {

                            //             ToastManager.sharedInstance().show("Permission already granted");

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + telephoneNumber));
                            instance.startActivity(intent);
                        } else {
                            //       ToastManager.sharedInstance().show("Please request permission");

                            if (!checkPermission()) {
                                requestPermission();
                            } else {
                                //    ToastManager.sharedInstance().show("Permission already granted.");
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + telephoneNumber));
                                instance.startActivity(intent);
                            }
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + telephoneNumber));
                        instance.startActivity(intent);
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("No", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    public static String getErrorDescription(Exception exception) {
        String errorDescription = "";
        if (exception != null) {
            if (exception instanceof NetworkError || exception instanceof TimeoutError || exception instanceof com.parse.ParseException) {
                errorDescription = "Please check your network connection and try again.";
            } else if (exception instanceof VolleyError) {
                VolleyError serverError = (VolleyError) exception;
                NetworkResponse response = serverError.networkResponse;
                if (response != null && response.data != null) {
                    errorDescription = new String(response.data);
                }
            } else {
                errorDescription = exception.getMessage();
            }
        }
        if (errorDescription == null || errorDescription.equals("")) {
            errorDescription = "Some error occurred while performing your request.";
        }
        return errorDescription;
    }

    public static int getErrorCode(Exception exception) {
        int errorCode = 0;
        if (exception != null && exception instanceof VolleyError) {
            VolleyError serverError = (VolleyError) exception;
            NetworkResponse response = serverError.networkResponse;
            if (response != null) {
                errorCode = response.statusCode;
            }
        }
        return errorCode;
    }

    public static VolleyError getCustomServerError(int code, byte[] message) {
        NetworkResponse networkResponse = new NetworkResponse(code, message, null, false);
        return new VolleyError(networkResponse);
    }

    public static boolean isToday(String startTimeString) {

        Calendar tomorrow = Calendar.getInstance();
        // set the calendar to start of today
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);
        tomorrow.add(Calendar.DATE, 0);

        SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);
        SimpleDateFormat comparisonFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date openDate = serverFormat.parse(startTimeString);
            String date1 = comparisonFormat.format(openDate);
            String date2 = comparisonFormat.format(tomorrow.getTime());

            if (date1.equals(date2)) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static boolean isTomorrow(String startTimeString) {
        Calendar tomorrow = Calendar.getInstance();
        // set the calendar to start of today
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);
        tomorrow.add(Calendar.DATE, 1);

        SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);
        SimpleDateFormat comparisonFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date openDate = serverFormat.parse(startTimeString);
            String date1 = comparisonFormat.format(openDate);
            String date2 = comparisonFormat.format(tomorrow.getTime());

            if (date1.equals(date2)) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static void notifyRemoveBasketUI(Context context) {
        notify(context, BROADCAST_REMOVE_BASKET_UI);
    }

    public static void notifyHomeScreenUpdate(Context context) {
        notify(context, BROADCAST_UPDATE_HOME_ACTIVITY);
    }

    public static void notify(Context context, String intentName) {
        if (context != null) {
            Intent intent = new Intent(intentName);
            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
        }
    }

    public static void notifyHomeScreenUpdateAndTransitBack(Activity activity) {
        if (activity != null) {
            notifyHomeScreenUpdate(activity.getApplicationContext());
            TransitionManager.transitFrom(activity, HomeActivity.class, true);
        }
    }

    public static String getFormatedAddress(String streetAddress, String city, String state, String zip) {
        String address = streetAddress;
        if (city != null) // Incase Of Preferred store street address has city, state and zip
        {
            address += "\n" + city + ",";
        }

        if (state != null) {
            address += " " + state;
        }

        if (zip != null) {
            address += " " + zip;
        }
        return address;
    }

    //Month should be from 0-11
    public static String getFormattedBirthdayString(int year, int month, int day) {
        String monthName = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (month >= 0 && month <= 11) {
            monthName = months[month];
        }
        return monthName + " " + day + ", " + year; // May 30, 1990
    }

    public static String getBirthdayInSpendGoFormat(int year, int month, int day) {
        month++; // Month starts from 0.
        String selectedMonth = month + "";
        String selectedDay = day + "";
        String selectedYear = year + "";
        if (day < 10) {
            selectedDay = "0" + day;
        }
        if (month < 10) {
            selectedMonth = "0" + month;
        }
        return selectedYear + "-" + selectedMonth + "-" + selectedDay;
    }

    public static int getRandomAvatarID() {
        Random rand = new Random();
        // nextInt is exclusive of the top value,
        int randomNum = rand.nextInt(AVATAR_ICONS.length);
        Logger.i("RANDOM AVATAR ID" + randomNum);
        return randomNum;
    }

    public static String capFirstLetter(String input) {
        if (input != null && input.length() > 0) {
            String capitalize = input.substring(0, 1).toUpperCase();
            if (input.length() > 1) {
                capitalize += input.substring(1, input.length());
            }
            return capitalize;
        }
        return input;
    }

    public static String getOrderPlacedTimeStatement(String inputString) {
        String reformattedStr = "Ordered ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
        try {
            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            Date orderedDate = dateFormat.parse(inputString);
            cal.setTime(orderedDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(currentDate);
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayNow = cal.get(Calendar.DAY_OF_MONTH);

            int yearsDiff = yearNow - year;
            int monthsDiff = monthNow - month;
            int daysDiff = dayNow - day;

            if (yearsDiff > 0) {
                reformattedStr = reformattedStr + yearsDiff + " year";
                reformattedStr = reformattedStr + getEndingString(yearsDiff);
            } else if (monthsDiff > 0) {
                reformattedStr = reformattedStr + monthsDiff + " month";
                reformattedStr = reformattedStr + getEndingString(monthsDiff);

            } else if (daysDiff > 0) {
                reformattedStr = reformattedStr + daysDiff + " day";
                reformattedStr = reformattedStr + getEndingString(daysDiff);
            } else {
                reformattedStr = reformattedStr + " today.";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    private static String getEndingString(int value) {
        if (value > 1) {
            return "s ago.";
        } else {
            return " ago.";
        }
    }

    public static boolean checkLocationPermission(Activity context) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissionGps(Activity context) {

        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    public static boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(instance, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            // JambaApplication.getAppContext().clpsdkObj.isLocationService=true;
            return true;
        } else {
            //  JambaApplication.getAppContext().clpsdkObj.isLocationService=false;

            return false;
        }
    }


    public static void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(instance, Manifest.permission.CALL_PHONE)) {

            Toast.makeText(instance, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(instance, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }


    public static SimpleDateFormat getTimeDisplayFormat(Context context) {
        String timeFormat = DateFormat.is24HourFormat(context) ? "HH:mm" : "h:mm a";
        return new SimpleDateFormat(timeFormat);
    }

    public static String getOrderReadyTimeStatement(Context context, String readytime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Server_Time_Format);
        SimpleDateFormat part1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat part2 = getTimeDisplayFormat(context);
        String result = null;
        try {
            if (readytime == null || readytime.length() <= 0) {
                return "";
            }
            Date readyDate = dateFormat.parse(readytime);
            Date currentDate = Calendar.getInstance().getTime();

            String ED = readyDate.after(currentDate) ? "" : "ed";
            String result1 = part1.format(readyDate);
            String result2 = part2.format(readyDate);
            result = "Pick" + ED + " up on " + result1 + " at " + result2 + ".";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * TODO: recentOrderSummary : check with jeeva about this and uncomment
     * @param activity
     */
//    public static String getOrderDispatchTiming(Context context, String readytime, RecentOrderSummary summary) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(Server_Time_Format);
//        SimpleDateFormat currentFormat = new SimpleDateFormat("MMM dd, yyyy");
//        SimpleDateFormat part2 = getTimeDisplayFormat(context);
//        String result = null;
//        StringBuilder str = new StringBuilder();
//        try {
//            Date readyDate = dateFormat.parse(readytime);
//            Date currentDate = Calendar.getInstance().getTime();
//
//            String Dat = currentFormat.format(readyDate);
//            String result1 = part2.format(readyDate);
//            String result2 = part2.format(readyDate);
//
//            str.append("Order will be delivered on ");
//            str.append(Dat).append(" at ");
//            if (summary.getDrivername() != null
//                    && summary.getDriverphonenumber() != null
//                    && summary.getDeliveryservice() != null) {
//                str.append(result1).append(" by ");
//                str.append(summary.getDrivername()).append(" (").append(summary.getDriverphonenumber()).append(") ").append(" from ");
//                str.append(summary.getDeliveryservice()).append(".");
//            } else {
//                str.append(result1).append(".");
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return String.valueOf(str);
//    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static String formatPrice(float price) {
        return String.format("$%.2f", price);
    }

    public static void addOnGlobalLayoutListener(final View view, final Runnable runnable) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        });
    }

    public static float dpToPx(float dipValue) {
        DisplayMetrics metrics = CbcApplication.getInstance().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static String getUnescapedString(String actualString) {
        String newLineCharacter = "&#xD;&#xA;";
        if (actualString.startsWith(newLineCharacter)) {
            actualString = actualString.replaceFirst(newLineCharacter, "");//Remove new line character from the starting of the string.
        }
        return actualString.replaceAll("&amp;", "&").replaceAll(newLineCharacter + newLineCharacter, "\n")// Double line break into one
                .replaceAll(newLineCharacter, "\n");
    }

    public static String toCamelCase(final String init) {
        if (init == null) {
            return "";
        }
        final StringBuilder ret = new StringBuilder(init.length());
        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(toCamelCaseDashSeparatedString(word)); //Camel case string like Mango-A-Go-Go
            }
            if (!(ret.length() == init.length())) {
                ret.append(" ");
            }
        }

        return ret.toString();
    }

    private static String toCamelCaseDashSeparatedString(String dashedString) {
        if (dashedString == null) {
            return "";
        }
        final StringBuilder ret = new StringBuilder(dashedString.length());
        for (final String word : dashedString.split("-")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == dashedString.length())) {
                ret.append("-");
            }
        }
        return ret.toString();
    }

    public static boolean isTablet() {
        return CbcApplication.getInstance().getResources().getBoolean(R.bool.isTablet);
    }

    public static String getVersionNumber() {
        String versionString = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
        if (BuildConfig.BUILD_BRANCH != "") {
            versionString += "-" + BuildConfig.BUILD_BRANCH;
        }
        return versionString;
    }


    public static String getVersionCode() {
        String versionString = String.valueOf(BuildConfig.VERSION_CODE);

        return versionString;
    }


    public static String getVersionNameOnly() {
        String versionString = BuildConfig.VERSION_NAME;

        return versionString;
    }

    /**
     * TODO: uncomment - after creating favorite orders , getfavoritedata
     * @param store
     * @return
     */
//    public static RecentOrder getFavoriteData(FavoriteOrder favoriteOrder) {
//        final RecentOrder[] recentOrder = {null};
//        UserService.getFavoriteOrderDetail(favoriteOrder.getId(), new RecentOrderCallback() {
//            @Override
//            public void onOrderCallback(List<RecentOrder> status, Exception e) {
//                if (e == null) {
//                    recentOrder[0] = status.get(0);
//                }
//            }
//        });
//        return recentOrder[0];
//    }

    // testing purpose only
    public static Store setDemoStoreName(Store store) {
        if (store != null) {
            if (store.getRestaurantId() == Constants.OloDemoVendorStoreId) {
                store.setName("Demo Vendor");
            } else if (store.getRestaurantId() == Constants.OloDemoLabVendorStoreId) {
                store.setName("Demo Lab Vendor");
            }
        }
        return store;
    }

    // testing purpose only

    /**
     * TODO: setting olo demo store name
     * @return
     */
//    public static OloRestaurant setOloDemoStoreName(OloRestaurant store) {
//        if (store != null) {
//            if (store.getId() == Constants.OloDemoVendorStoreId) {
//                store.setName("Demo Vendor");
//            } else if (store.getId() == Constants.OloDemoLabVendorStoreId) {
//                store.setName("Demo Lab Vendor");
//            }
//        }
//        return store;
//    }

    // testing purpose only
    public static String getDemoStoreName() {
        String storeName = "";
        if (UserService.getUser().getOloFavoriteRestaurantId() == Constants.OloDemoVendorStoreId) {
            storeName = "Demo Vendor";
        } else if (UserService.getUser().getOloFavoriteRestaurantId() == Constants.OloDemoLabVendorStoreId) {
            storeName = "Demo Lab Vendor";
        } else {
            storeName = UserService.getUser().getStoreName().replace("Jamba Juice ", "");
        }
        return storeName;
    }

}
