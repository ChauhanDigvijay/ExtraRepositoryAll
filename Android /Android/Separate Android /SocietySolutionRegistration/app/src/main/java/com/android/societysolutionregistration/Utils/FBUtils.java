package com.android.societysolutionregistration.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.activity.MainActivity;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FBUtils
{
    public static final int MILISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
    Activity activity;
    public static ListView lv;

    public static void showAlert(Context context, String message)
    {
        showAlert(context, message, null);
    }


    public static void tryHandleTokenExpiry(final Activity activity, Exception exception)
    {
        String message = getErrorDescription(exception);

        if (message.equalsIgnoreCase("Invalid access token"))
        {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
            alertdialog.setTitle("Error");
            alertdialog.setCancelable(false);
            alertdialog.setMessage("Your session has expired. Please login to continue.");
            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Bundle extras = new Bundle();
                    Intent i = new Intent(activity, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtras(extras);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
            alertdialog.setIcon(R.drawable.logomain);
            alertdialog.show();
        }
        else
        {
            FBUtils.showErrorAlert(activity,exception);
        }
    }

    public static void showAlert(Context context, String message, String title)
    {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (title != null)
        {
            alertdialog.setTitle(title);
        }
        alertdialog.setMessage(message).setPositiveButton(android.R.string.yes, null).show();
    }

    public static void showErrorAlert(final Activity activity, Exception exception)
    {
        String error = FBUtils.getErrorDescription(exception);
        FBUtils.showAlert(activity, error, "Error");
    }

    public static void showErrorAlert(final Activity activity, String exception)
    {
        {
            FBUtils.showAlert(activity, exception, "Error");
        }
    }

    public static boolean isNetworkAvailable(Context context)
    {
        NetworkInfo netInfo = null;

        try
        {
            ConnectivityManager ex = (ConnectivityManager)context.getSystemService("connectivity");
            netInfo = ex.getActiveNetworkInfo();
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }
        return netInfo != null && netInfo.isAvailable();
    }


    public static boolean isValidZip(String zip)
    {
        return !TextUtils.isEmpty(zip) && zip.trim().length() == 5;
    }

    public static boolean isValidCVV(String cvv)
    {
        return !TextUtils.isEmpty(cvv) && cvv.trim().length() >= 3 && cvv.trim().length() <= 4;
    }

    public static boolean isValidCardNumber(String cardNumber)
    {
        return !TextUtils.isEmpty(cardNumber) && cardNumber.trim().length() >= 12 && cardNumber.trim().length() <= 19;
    }

    public static boolean isValidName(String string)
    {
        return string.matches(".*\\w.*");
    }

    public static boolean isEmptyString(String string)

    {
        return string.trim().length() == 0;
    }

    public static boolean isValidString(String string)
    {
        if ( string == null || string.equals("") )
        {
            return false;
        }
        return true;
    }

    public static boolean isValidDoubleToString(String string)
    {
        if ( string == null || string.equals("") || string.equals("0")||string.equals(" ")||string.equals("0.0")||string.equals("US$0.0") )
        {
            return false;
        }
        return true;
    }

    public static boolean isValidIntergerToString(String string)
    {

        if ( string == null || string.equals("") || string.equals("0.0") )
        {
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static final boolean isValidPhoneNumber(CharSequence target)
    {
        if (target == null || TextUtils.isEmpty(target))
        {
            return false;
        }
        else
        {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static int daysBetween(Date d1, Date d2)
    {
        return (int)( (d2.getTime() - d1.getTime()) / MILISECONDS_IN_DAY);
    }


    public static Date getDateFromString(String strDate, String format)
    {
        if (!isValidString(strDate))
            return null;

        if (format == null)
            format = "yyyy-MM-dd hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = formatter.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }



    public static String getPhone(Long phoneNum)
    {
        StringBuilder sb = new StringBuilder(15);
        StringBuilder temp = new StringBuilder(phoneNum.toString());

        while (temp.length() < 10)
            temp.insert(0, "0");

        char[] chars = temp.toString().toCharArray();
        sb.append("(");

        for (int i = 0; i < chars.length; i++)
        {
            if (i == 3)
                sb.append(") ");
            else if (i == 6)
                sb.append("-");
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    public static String getPhoneDash(Long phoneNum)
    {
        StringBuilder sb = new StringBuilder(15);
        StringBuilder temp = new StringBuilder(phoneNum.toString());

        while (temp.length() < 10)
            temp.insert(0, "0");
        char[] chars = temp.toString().toCharArray();

        sb.append("");
        for (int i = 0; i < chars.length; i++)
        {
            if (i == 3)
                sb.append("-");
            else if (i == 6)
                sb.append("-");
            sb.append(chars[i]);
        }

        return sb.toString();
    }


    public static void openMap(Activity activity, String searchString)
    {
        if (searchString == null || searchString.trim().equals(""))
        {
            searchString = "Jamba Juice";
        }
        else
        {
            searchString = "Jamba Juice near " + searchString;
        }
        PackageManager manager = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + 0 + ">,<" + 0 + ">?q=" + searchString));
        List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);

        if (info.size() > 0)
        {
            activity.startActivity(intent);
        }
        else
        {
            Toast.makeText(activity, "There no application installed on your device to show direction.", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getErrorDescription(Exception exception)
    {
        String errorDescription = "";
        if (exception != null)
        {
            if (exception instanceof NetworkError || exception instanceof TimeoutError )
            {
                errorDescription = "Please check your network connection and try again.";
            }
            else if (exception instanceof VolleyError)
            {
                VolleyError serverError = (VolleyError) exception;
                NetworkResponse response = serverError.networkResponse;

                if (response != null && response.data != null)
                {
                    errorDescription = new String(response.data);
                }
            }
            else
            {
                errorDescription = exception.getMessage();
            }
        }

        if (errorDescription == null || errorDescription.equals(""))
        {
            errorDescription = "Some error occurred while performing your request.";
        }
        return errorDescription;
    }

    public static int getErrorCode(Exception exception)
    {
        int errorCode = 0;

        if (exception != null && exception instanceof VolleyError)
        {
            VolleyError serverError = (VolleyError) exception;
            NetworkResponse response = serverError.networkResponse;

            if (response != null)
            {
                errorCode = response.statusCode;
            }
        }
        return errorCode;
    }

    public static VolleyError getCustomServerError(int code, byte[] message)
    {
        NetworkResponse networkResponse = new NetworkResponse(code, message, null, false);
        return new VolleyError(networkResponse);
    }



    public static String getFormatedAddress(String streetAddress, String city, String state, String zip)
    {
        String address = streetAddress;

        if (city != null) // Incase Of Preferred store street address has city, state and zip
        {
            address += "\n" + city + ", " + state + " " + zip;
        }
        return address;
    }

    //Month should be from 0-11
    public static String getFormattedBirthdayString(int year, int month, int day)
    {
        String monthName = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        if (month >= 0 && month <= 11)
        {
            monthName = months[month];
        }
        return monthName + " " + day + ", " + year; // May 30, 1990
    }

    public static String getBirthdayInSpendGoFormat(int year, int month, int day)
    {
        month++; // Month starts from 0.
        String selectedMonth = month + "";
        String selectedDay = day + "";
        String selectedYear = year + "";

        if (day < 10)
        {
            selectedDay = "0" + day;
        }

        if (month < 10)
        {
            selectedMonth = "0" + month;
        }
        return selectedYear + "-" + selectedMonth + "-" + selectedDay;
    }

    public static String capFirstLetter(String input)
    {
        if (input != null && input.length() > 0)
        {
            String capitalize = input.substring(0, 1).toUpperCase();

            if (input.length() > 1)
            {
                capitalize += input.substring(1, input.length());
            }
            return capitalize;
        }
        return input;
    }

    public static String getOrderPlacedTimeStatement(String inputString)
    {
        String reformattedStr = "Ordered ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
        try
        {
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

            if (yearsDiff > 0)
            {
                reformattedStr = reformattedStr + yearsDiff + " year";
                reformattedStr = reformattedStr + getEndingString(yearsDiff);
            }
            else if (monthsDiff > 0)
            {
                reformattedStr = reformattedStr + monthsDiff + " month";
                reformattedStr = reformattedStr + getEndingString(monthsDiff);

            }
            else if (daysDiff > 0)
            {
                reformattedStr = reformattedStr + daysDiff + " day";
                reformattedStr = reformattedStr + getEndingString(daysDiff);
            }
            else
            {
                reformattedStr = reformattedStr + " today.";
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    private static String getEndingString(int value)
    {
        if (value > 1)
        {
            return "s ago.";
        }
        else
        {
            return " ago.";
        }
    }

    public static SimpleDateFormat getTimeDisplayFormat(Context context)
    {
        String timeFormat = DateFormat.is24HourFormat(context) ? "HH:mm" : "h:mm a";
        return new SimpleDateFormat(timeFormat);
    }

    public static void hideSoftKeyboard(Activity activity)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e)
        {
        }
    }

    public static String formatPrice(float price)
    {
        return String.format("$%.2f", price);
    }

    public static void addOnGlobalLayoutListener(final View view, final Runnable runnable)
    {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else
                {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        });
    }
   /*
    public static float dpToPx(float dipValue)
    {
        DisplayMetrics metrics = Auth.getAppContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    */

    public static String getUnescapedString(String actualString)
    {
        String newLineCharacter = "&#xD;&#xA;";
        if(actualString.startsWith(newLineCharacter))
        {
            actualString = actualString.replaceFirst(newLineCharacter, "");//Remove new line character from the starting of the string.
        }
        return actualString.replaceAll("&amp;", "&").replaceAll(newLineCharacter+newLineCharacter, "\n")// Double line break into one
                .replaceAll(newLineCharacter, "\n");
    }

    public static String toCamelCase(final String init)
    {
        if (init == null)
        {
            return "";
        }
        final StringBuilder ret = new StringBuilder(init.length());
        for (final String word : init.split(" "))
        {
            if (!word.isEmpty())
            {
                ret.append(toCamelCaseDashSeparatedString(word)); //Camel case string like Mango-A-Go-Go
            }
            if (!(ret.length() == init.length()))
            {
                ret.append(" ");
            }
        }

        return ret.toString();
    }

    private static String toCamelCaseDashSeparatedString(String dashedString)
    {
        if (dashedString == null)
        {
            return "";
        }
        final StringBuilder ret = new StringBuilder(dashedString.length());

        for (final String word : dashedString.split("-"))
        {
            if (!word.isEmpty())
            {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }

            if (!(ret.length() == dashedString.length()))
            {
                ret.append("-");
            }
        }
        return ret.toString();
    }

    public static String removeLastChar(String s)
    {
        if (s == null || s.length() == 0)
        {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public static void setUsDashPhone(CharSequence s , int before, EditText editText)
    {
        int lenth=s.length();
        StringBuilder builder=new StringBuilder();
        builder.append(s);

        if(before!=1)
        {
            if(lenth==4){
                builder.insert(3,"-");
                editText.setText(builder.toString());
            }

            if(lenth==8){
                builder.insert(7,"-");
                editText.setText(builder.toString());
            }
        }
    }


    public static void setUsDatePhone(CharSequence s , int before, EditText editText)
    {
        int lenth=s.length();
        StringBuilder builder=new StringBuilder();
        builder.append(s);


        if(before!=1)
        {
            if(lenth==3){
                builder.insert(2,"/");
                editText.setText(builder.toString());
            }

            if(lenth==6){
                builder.insert(5,"/");
                editText.setText(builder.toString());
            }
        }

    }


    public static Date getDateFromStringSlash(String strDate, String format)
    {
        if (!isValidString(strDate))
            return null;

        if (format == null)
            format = "yyyy/MM/dd hh:mm:ss";


        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
    public static String setREUsDashPhone(CharSequence s )
    {
        String offerTitle =s.toString();

        if(offerTitle.length()>3 && offerTitle.length()<=6)
        {
            offerTitle = offerTitle.replaceFirst("(\\d{3})","$1-");
            return offerTitle;
        }

        if(offerTitle.length()>6 && offerTitle.length()<10)
        {
            offerTitle = offerTitle.replaceFirst("(\\d{3})(\\d{3})", "$1-$2-");

            return offerTitle;
        }
        return offerTitle;
    }


    public static boolean isValidDateFormat(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}