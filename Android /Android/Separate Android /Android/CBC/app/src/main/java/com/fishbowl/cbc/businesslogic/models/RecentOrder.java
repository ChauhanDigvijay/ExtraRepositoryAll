package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Models.OloOrdersSatusProduct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fishbowl.apps.olo.Utils.Constants.Server_Time_Format;

/**
 * Created by VT027 on 5/20/2017.
 */

public class RecentOrder {
    private String id;
    private String reference;
    private String name = "";
    private String fofpName = "";
    private String orderTimeStatement;
    private float total;
    private float isFavorite;
    private List<RecentOrderDetails> products;
    private String status = "";
    private String vendorextref;
    private RecentOrderSummary summary;
    private DeliveryAddress deliveryaddress;
    private String deliverymode;

    public RecentOrder(OloOrderStatus order) {
        products = new ArrayList<RecentOrderDetails>();
        for (OloOrdersSatusProduct product : order.getProducts()) {
            products.add(new RecentOrderDetails(product));

            if (!name.isEmpty()) {
                name += ", ";
            }
            name += product.getName();
        }
        if (order.getProducts().size() == 1) {
            fofpName = order.getProducts().get(0).getName();
        } else {
            fofpName = order.getProducts().get(0).getName() + ", ...";
        }
        total = order.getTotal();
        orderTimeStatement = getStatement(order.getTimeplaced());
        summary = new RecentOrderSummary(order);
        id = order.getId();
        reference = order.getOrderref();
        vendorextref = order.getVendorextref();
    }

    public RecentOrder(CbcOrderStatus order) {
        products = new ArrayList<RecentOrderDetails>();
        if (order.getProducts() != null) {
            for (CbcOrderSatusProduct product : order.getProducts()) {
                products.add(new RecentOrderDetails(product));

                if (!name.isEmpty()) {
                    name += ", ";
                }
                name += product.getName();
            }
            if (order.getProducts().size() == 1) {
                fofpName = order.getProducts().get(0).getName();
            } else {
                fofpName = order.getProducts().get(0).getName() + ", ...";
            }
        }
        total = order.getTotal();
        if (order.getTimeplaced() != null) {
            orderTimeStatement = getStatement(order.getTimeplaced());
        }
        summary = new RecentOrderSummary(order);
        id = order.getId();
        reference = order.getOrderref();
        vendorextref = order.getVendorextref();
        status = order.getStatus();
        deliveryaddress = order.getDeliveryaddress();
        deliverymode = order.getDeliverymode();
    }

//    private String getStatement(String inputString) {
//        String reformattedStr = "Ordered ";
//        SimpleDateFormat dateFormat = new SimpleDateFormat(Server_Time_Format);
//        try {
//            Calendar cal = Calendar.getInstance();
//            Date currentDate = cal.getTime();
//            Date orderedDate = dateFormat.parse(inputString);
//            cal.setTime(orderedDate);
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//
//            cal.setTime(currentDate);
//            int yearNow = cal.get(Calendar.YEAR);
//            int monthNow = cal.get(Calendar.MONTH);
//            int dayNow = cal.get(Calendar.DAY_OF_MONTH);
//
//            int yearsDiff = yearNow - year;
//            int monthsDiff = monthNow - month;
//            int daysDiff = dayNow - day;
//
//            if (yearsDiff > 0) {
//                reformattedStr = reformattedStr + yearsDiff + " year";
//                reformattedStr = reformattedStr + getEndingString(yearsDiff);
//            } else if (monthsDiff > 0) {
//                reformattedStr = reformattedStr + monthsDiff + " month";
//                reformattedStr = reformattedStr + getEndingString(monthsDiff);
//
//            } else if (daysDiff > 0) {
//                reformattedStr = reformattedStr + daysDiff + " day";
//                reformattedStr = reformattedStr + getEndingString(daysDiff);
//            } else {
//                reformattedStr = reformattedStr + " today";
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return reformattedStr;
//    }


    public String getFofpName() {
        return fofpName;
    }

    public void setFofpName(String fofpName) {
        this.fofpName = fofpName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderTimeStatement() {
        return orderTimeStatement;
    }

    public float getTotal() {
        return total;
    }

    public float getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(float isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<RecentOrderDetails> getProducts() {
        return products;
    }

    private String getEndingString(int value) {
        if (value > 1) {
            return "s ago";
        } else {
            return " ago";
        }
    }

    public RecentOrderSummary getSummary() {
        return summary;
    }

    public void setSummary(RecentOrderSummary summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getVendorextref() {
        return vendorextref;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliverymode() {
        return deliverymode;
    }

    public DeliveryAddress getDeliveryaddress() {
        return deliveryaddress;
    }

    private String getStatement(String inputString) {
        String reformattedStr = "Ordered ";
        SimpleDateFormat dateFormat = new SimpleDateFormat(Server_Time_Format);

        try {

            Date orderedDate = dateFormat.parse(inputString);
            Date currentDate = Calendar.getInstance().getTime();
            int OrderedInDays = daysBetweenDates(orderedDate, currentDate);
            int OrderedInMonths = monthsBetweenDates(orderedDate, currentDate);
            if (OrderedInDays > 30) {
                if (OrderedInMonths >= 12) {
                    int OrderedInYears = OrderedInMonths / 12;
                    reformattedStr = reformattedStr + OrderedInYears + " year";
                    reformattedStr = reformattedStr + getEndingString(OrderedInYears);
                } else if (OrderedInMonths > 0) {
                    reformattedStr = reformattedStr + OrderedInMonths + " month";
                    reformattedStr = reformattedStr + getEndingString(OrderedInMonths);
                }
            } else if (OrderedInDays > 0) {
                reformattedStr = reformattedStr + OrderedInDays + " day";
                reformattedStr = reformattedStr + getEndingString(OrderedInDays);
            } else {
                reformattedStr = reformattedStr + " today";
            }

            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int daysBetweenDates(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return (int) elapsedDays;
    }

    public int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += (end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
        monthsBetween--;
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

}
