package com.womensafety.Common;

import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN_CURRENT_DATE_LOG = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_PATTERN_CURRENT_TIME_FORMAT_24 = "HH.mm";
    public static final String DATE_STD_PATTERN = "yyyy-MM-dd";
    public static final String DATE_STD_PATTERN_ENROLL = "dd-MMM-yyyy";
    public static final String DATE_STD_PATTERN_MONTH = "yyyy-MM";
    public static int WEEK_OF_MONTH;

    public static String getMonthFromNumber(int intMonth) {
        String strMonth = "";
        switch (intMonth) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return strMonth;
        }
    }

    public static int getnumberFromMonth(String month) {
        if (month.equalsIgnoreCase("Jan")) {
            return 0;
        }
        if (month.equalsIgnoreCase("Feb")) {
            return 1;
        }
        if (month.equalsIgnoreCase("Mar")) {
            return 2;
        }
        if (month.equalsIgnoreCase("Apr")) {
            return 3;
        }
        if (month.equalsIgnoreCase("May")) {
            return 4;
        }
        if (month.equalsIgnoreCase("Jun")) {
            return 5;
        }
        if (month.equalsIgnoreCase("Jul")) {
            return 6;
        }
        if (month.equalsIgnoreCase("Aug")) {
            return 7;
        }
        if (month.equalsIgnoreCase("Sep")) {
            return 8;
        }
        if (month.equalsIgnoreCase("Oct")) {
            return 9;
        }
        if (month.equalsIgnoreCase("Nov")) {
            return 10;
        }
        if (month.equalsIgnoreCase("Dec")) {
            return 11;
        }
        return 0;
    }

    public static long getDateDifferenceInMinutes(String dateStart, String dateStop) {
        long diffMinutes = 0;
        try {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(getDateFromString(dateStart, "yyyy-MM-dd'T'HH:mm:ss"));
            calendar2.setTime(getDateFromString(dateStop, "yyyy-MM-dd'T'HH:mm:ss"));
            return (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 60000;
        } catch (Exception e) {
            e.printStackTrace();
            return diffMinutes;
        }
    }

    public static String getMonthAsString(int intMonth) {
        String monthYear = "";
        switch (intMonth) {
            case 0:
                return "January ";
            case 1:
                return "February ";
            case 2:
                return "March ";
            case 3:
                return "April ";
            case 4:
                return "May ";
            case 5:
                return "June ";
            case 6:
                return "July ";
            case 7:
                return "August ";
            case 8:
                return "September ";
            case 9:
                return "October ";
            case 10:
                return "November ";
            case 11:
                return "December ";
            default:
                return monthYear;
        }
    }

    public static void setDate(int year, int month, int day, TextView tvCommon, String format) {
        if (tvCommon != null) {
            SimpleDateFormat sdfReq = new SimpleDateFormat(format);
            SimpleDateFormat sdfGiven = new SimpleDateFormat(DATE_STD_PATTERN);
            try {
                Object obj;
                StringBuilder append = new StringBuilder().append(year < 10 ? "0" + year : Integer.valueOf(year)).append("-");
                if (month + 1 < 10) {
                    obj = "0" + (month + 1);
                } else {
                    obj = Integer.valueOf(month + 1);
                }
                append = append.append(obj).append("-");
                if (day < 10) {
                    obj = "0" + day;
                } else {
                    obj = Integer.valueOf(day);
                }
                String date = append.append(obj).toString();
                tvCommon.setText(sdfReq.format(sdfGiven.parse(date)));
                tvCommon.setTag(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurrentDateAsString() {
        return new SimpleDateFormat(DATE_STD_PATTERN).format(new Date());
    }

    public static String getCurrentDateForJourneyPlan(String date) {
        Calendar ca1 = getCalender(date);
        return "Week " + ca1.get(4) + "," + getDayOfWeek(ca1.get(7));
    }

    public static String getCurrentDateForJourneyPlan2(String date) {
        Calendar ca1 = getCalender(date);
        return "Week " + ca1.get(4) + "," + getDayOfWeek(ca1.get(7));
    }

    public static void setWeekOfMonth(String date) {
        WEEK_OF_MONTH = getCalender(date).get(4);
    }

    public static String getDateFromDateOfJourney(String dateOfJourney) {
        String nextVisitDate = "";
        if (!dateOfJourney.contains("Week ")) {
            return nextVisitDate;
        }
        String[] strWeekNoAndDay = dateOfJourney.substring(dateOfJourney.lastIndexOf("Week ") + 5, dateOfJourney.length()).split(",");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_STD_PATTERN_ENROLL);
        Calendar cal = Calendar.getInstance();
        int weekNumber = StringUtils.getInt(strWeekNoAndDay[0]);
        if (strWeekNoAndDay.length != 2) {
            return nextVisitDate;
        }
        if (WEEK_OF_MONTH % 2 == 1 && weekNumber == 1) {
            weekNumber = WEEK_OF_MONTH;
        } else if (WEEK_OF_MONTH % 2 == 1 && weekNumber == 2) {
            weekNumber = WEEK_OF_MONTH + 1;
        } else if (WEEK_OF_MONTH % 2 == 0 && weekNumber == 1) {
            weekNumber = WEEK_OF_MONTH + 1;
        } else if (WEEK_OF_MONTH % 2 == 0 && weekNumber == 2) {
            weekNumber = WEEK_OF_MONTH;
        }
        cal.set(4, weekNumber);
        if (strWeekNoAndDay[1].equalsIgnoreCase("Monday")) {
            cal.set(7, 2);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Tuesday")) {
            cal.set(7, 3);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Wednesday")) {
            cal.set(7, 4);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Thursday")) {
            cal.set(7, 5);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Friday")) {
            cal.set(7, 6);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Saturday")) {
            cal.set(7, 7);
        } else if (strWeekNoAndDay[1].equalsIgnoreCase("Sunday")) {
            cal.set(7, 1);
        }
        if (compareTo(sdf.format(cal.getTime())) > 0) {
            return strWeekNoAndDay[1] + ", " + sdf.format(cal.getTime());
        }
        return null;
    }

    public static long compareTo(String selected) {
        long diffrence = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_STD_PATTERN_ENROLL);
            String date = sdf.format(new Date());
            diffrence = sdf.parse(selected).getTime() - sdf.parse(date).getTime();
            LogUtils.errorLog("diffrence", "" + diffrence);
            LogUtils.errorLog("selected", "" + selected);
            LogUtils.errorLog("date", "" + date);
            return diffrence;
        } catch (Exception e) {
            return diffrence;
        }
    }

    public static String getCurrentDate() {
        String date = "";
        Calendar c = Calendar.getInstance();
        int year = c.get(1);
        int month = c.get(2);
        return c.get(5) + " " + getMonthFromNumber(month + 1) + ", " + year;
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
    }

    public static int getDiffBtwDatesInDays(String startDate, String endDate) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(getDateFromString(startDate, DATE_STD_PATTERN));
        calendar2.setTime(getDateFromString(endDate, DATE_STD_PATTERN));
        return (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 86400000);
    }

    public static Date getDateFromString(String date, String pattern) {
        Date dateObj = new Date();
        try {
            dateObj = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    public static String getDate(int year, int month, int day) {
        Object obj;
        StringBuilder append = new StringBuilder().append(year < 10 ? "0" + year : Integer.valueOf(year)).append("-");
        if (month < 10) {
            obj = "0" + month;
        } else {
            obj = Integer.valueOf(month);
        }
        append = append.append(obj).append("-");
        if (day < 10) {
            obj = "0" + day;
        } else {
            obj = Integer.valueOf(day);
        }
        return append.append(obj).toString();
    }

    public static String getDayOfWeek(int dayOfWeek) {
        String strDayOfWeek = "";
        switch (dayOfWeek) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return strDayOfWeek;
        }
    }

    public static Calendar getCalender(String date) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        String[] str = date.split("-");
        calendar.set(StringUtils.getInt(str[0]), StringUtils.getInt(str[1]) - 1, StringUtils.getInt(str[2]));
        return calendar;
    }

    public static String getDateNotation(int day) {
        String sep = "th";
        switch (day) {
            case 1:
            case 21:
            case 31:
                return "st";
            case 2:
            case 22:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getCurrentMonth() {
        return new SimpleDateFormat(DATE_STD_PATTERN_MONTH).format(new Date());
    }

    public static String getCurrentTime() {
        String strCurrentTime = "";
        return new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());
    }

    public static String getDeliverydate() {
        Calendar c = Calendar.getInstance();
        c.add(5, 1);
        int year = c.get(1);
        int month = c.get(2);
        int day = c.get(5);
        String strMonth = "";
        String strDate = "";
        if (month < 9) {
            strMonth = "0" + (month + 1);
        } else {
            strMonth = "" + (month + 1);
        }
        if (day < 10) {
            strDate = "0" + day;
        } else {
            strDate = "" + day;
        }
        return year + "-" + strMonth + "-" + strDate;
    }

    public static String getCurrentSalesDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(1);
        int month = c.get(2);
        int day = c.get(5);
        String strDate = "";
        if (day < 10) {
            strDate = "0" + day;
        } else {
            strDate = "" + day;
        }
        return (month + 1) + "/" + strDate + "/" + year;
    }

    public static int getCurrentMonthInt() {
        return Calendar.getInstance().get(2);
    }

    public static String getCurrentTimeToUpload() {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(13);
        int minutes = c.get(12);
        int hours = c.get(11);
        String strMinutes = "";
        String strhours = "";
        String strSeconds = "";
        if (seconds < 10) {
            strSeconds = "0" + seconds;
        } else {
            strSeconds = "" + seconds;
        }
        if (minutes < 10) {
            strMinutes = "0" + minutes;
        } else {
            strMinutes = "" + minutes;
        }
        if (hours < 10) {
            strhours = "0" + hours;
        } else {
            strhours = "" + hours;
        }
        return strhours + "-" + strMinutes + "-" + strSeconds;
    }

    public static String getOrderPostDate() {
        return new SimpleDateFormat(DATE_STD_PATTERN).format(new Date());
    }

    public static String getRetrunTime() {
        Calendar c = Calendar.getInstance();
        int minutes = c.get(12);
        int hours = c.get(11);
        String strMinutes = "";
        String strhours = "";
        if (minutes < 10) {
            strMinutes = "0" + minutes;
        } else {
            strMinutes = "" + minutes;
        }
        if (hours < 10) {
            strhours = "0" + hours;
        } else {
            strhours = "" + hours;
        }
        return strhours + ":" + strMinutes;
    }

    public static String getRetrunTimeToDiffer() {
        Calendar c = Calendar.getInstance();
        c.add(11, 1);
        int minutes = c.get(12);
        int hours = c.get(11);
        String strMinutes = "";
        String strhours = "";
        if (minutes < 10) {
            strMinutes = "0" + minutes;
        } else {
            strMinutes = "" + minutes;
        }
        if (hours < 10) {
            strhours = "0" + hours;
        } else {
            strhours = "" + hours;
        }
        return strhours + ":" + strMinutes;
    }

    public static boolean isValidTimeForTeleOrder() {
        try {
            Calendar calendar1 = Calendar.getInstance();
            Date time1 = new Date(calendar1.get(1), calendar1.get(2), calendar1.get(5), calendar1.get(10), calendar1.get(12), calendar1.get(13));
            long difference2 = time1.getTime() - new Date(calendar1.get(1), calendar1.get(2), calendar1.get(5), 19, 0, 0).getTime();
            if (time1.getTime() - new Date(calendar1.get(1), calendar1.get(2), calendar1.get(5), 9, 0, 0).getTime() <= 0 || difference2 >= 0) {
            }
        } catch (Exception e) {
        }
        return true;
    }

    public static String getFormatedDatefromString(String strDate) {
        String formatedDate = strDate;
        try {
            LogUtils.errorLog(strDate, strDate);
            String[] arrDate;
            if (strDate.contains("T")) {
                arrDate = strDate.split("T")[0].split("-");
                return arrDate[2] + " " + getMonthFromNumber(StringUtils.getInt(arrDate[1])) + ", " + arrDate[0];
            } else if (strDate.contains("-")) {
                arrDate = strDate.split("-");
                return arrDate[2] + " " + getMonthFromNumber(StringUtils.getInt(arrDate[1])) + ", " + arrDate[0];
            } else if (!strDate.contains("/")) {
                return formatedDate;
            } else {
                arrDate = strDate.substring(0, strDate.lastIndexOf(" ")).split("/");
                return arrDate[1] + " " + getMonthFromNumber(StringUtils.getInt(arrDate[0])) + ", " + arrDate[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getFormatedDatefromString(getOrderPostDate());
        }
    }

    public static long getCurrentTimeInMilli() {
        return System.currentTimeMillis();
    }

    public static boolean isMustHaveRuleApplicable() {
        return true;
    }

    public static String getCurrentFormatTime24() {
        return new SimpleDateFormat(DATE_PATTERN_CURRENT_TIME_FORMAT_24, new Locale("en")).format(Long.valueOf(System.currentTimeMillis()));
    }

    public static int getNumberODaysInMonth() {
        return Calendar.getInstance().get(5);
    }

    public static String getCurrentDateAsStringforStoreCheck() {
        return new SimpleDateFormat(DATE_STD_PATTERN).format(new Date());
    }

    public static String getOrderSummaryDate(int year, int month, int day) {
        Object obj;
        month++;
        StringBuilder append = new StringBuilder().append(year < 10 ? "0" + year : Integer.valueOf(year)).append("-");
        if (month < 10) {
            obj = "0" + month;
        } else {
            obj = Integer.valueOf(month);
        }
        append = append.append(obj).append("-");
        if (day < 10) {
            obj = "0" + day;
        } else {
            obj = Integer.valueOf(day);
        }
        return append.append(obj).toString();
    }

    public static String getRequiredDateFormat(String actualDate) {
        String reqDateFormat = "";
        if (actualDate != null) {
            try {
                if (!actualDate.equalsIgnoreCase("")) {
                    String[] strDate1 = actualDate.split("T");
                    String[] strDate2 = strDate1[0].split("-");
                    return strDate2[2] + " " + getMonthFromNumber(StringUtils.getInt(strDate2[1])) + "yy" + strDate2[0] + "yy" + formatTime(strDate1[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return reqDateFormat;
            }
        }
        return getRequiredDateFormat(getCurrentDateTime());
    }

    private static String formatTime(String startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            startTime = new SimpleDateFormat("hh:mm a").format(sdf.parse(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startTime;
    }
}
