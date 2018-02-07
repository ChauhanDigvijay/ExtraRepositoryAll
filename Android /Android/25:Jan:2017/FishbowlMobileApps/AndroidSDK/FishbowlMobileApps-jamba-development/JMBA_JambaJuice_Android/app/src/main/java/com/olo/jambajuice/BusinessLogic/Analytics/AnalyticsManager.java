package com.olo.jambajuice.BusinessLogic.Analytics;

import android.app.Activity;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vt010 on 12/22/16.
 */

public class AnalyticsManager {

    static AnalyticsManager instance;
    public GoogleAnalytics analytics;
    public Tracker tracker;

    public AnalyticsManager(JambaApplication context) {
        analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker(context.getString(R.string.ga_trackingId));
        tracker.enableExceptionReporting(true);
        tracker.enableAutoActivityTracking(true);
        analytics.enableAutoActivityReports(context);
        analytics.setLocalDispatchPeriod(20);
        analytics.getLogger().setLogLevel(Log.WARN);
    }


    public static AnalyticsManager getInstance() {
        if (instance == null) {
            instance = new AnalyticsManager(JambaApplication.getAppContext());
        }
        return instance;
    }


    public void startReporting(Activity activity) {
        analytics.enableAutoActivityReports(activity.getApplication());
        analytics.reportActivityStart(activity);
    }


    public void stopReporting(Activity activity) {
        analytics.reportActivityStop(activity);
    }

    // Track a single event
    public void trackEvent(String category, String action, String label, long value, String screenName) {
        //Google Analytics

        // Build and send an Event.
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        builder.setCategory(category);
        builder.setAction(action);
        if (label != null) {
            builder.setLabel(label);
        }
        if (value > 0) {
            builder.setValue(value);
        }
        if (screenName != null) {
            tracker.setScreenName(screenName);
        }
        tracker.send(builder.build());
        // AppsFlyer
        HashMap<String, Object> eventValues = new HashMap<>();
        eventValues.put("category", category);
        eventValues.put("label", category);
        eventValues.put("value", category);
        eventValues.put("screenName", category);
        AppsFlyerLib.trackEvent(JambaApplication.getAppContext(), action, eventValues);
    }


    public void trackEvent(String category, String action, String label) {
        trackEvent(category, action, label, 0, null);
    }

    public void trackEvent(String category, String action) {
        trackEvent(category, action, null, 0, null);
    }


    //All screens are automatically tracked except fragments and home signedIn and nonSignedIn View.
    public void trackScreen(String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    // Track purchase
    public void trackPurchase(OrderStatus orderStatus, List<BasketProduct> basketProducts) {
        // Build the transaction.
        HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
        transactionBuilder.setTransactionId(orderStatus.getOrderRef());
        transactionBuilder.setAffiliation(orderStatus.getVendorExtRef());
        transactionBuilder.setRevenue(orderStatus.getTotal());
        transactionBuilder.setTax(orderStatus.getSaleTax());
        transactionBuilder.setShipping(0);
        transactionBuilder.setCurrencyCode("USD");
        tracker.send(transactionBuilder.build());
        // Build an item.
        for (BasketProduct basketProduct : basketProducts) {
            HitBuilders.ItemBuilder itemBuilder = new HitBuilders.ItemBuilder();
            itemBuilder.setTransactionId(orderStatus.getOrderRef());
            itemBuilder.setName(basketProduct.getName());
            itemBuilder.setSku(basketProduct.getProductId() + "");
            itemBuilder.setCategory("Product");
            itemBuilder.setPrice(basketProduct.getTotalcost());
            itemBuilder.setQuantity(basketProduct.getQuantity());
            itemBuilder.setCurrencyCode("USD");
            tracker.send(itemBuilder.build());
            // AppsFlyer
            HashMap<String, Object> eventValues = new HashMap<>();
            eventValues.put("af_revenue", basketProduct.getTotalcost());
            eventValues.put("af_content_type", "Product");
            eventValues.put("af_description", basketProduct.getName());
            eventValues.put("af_content_id", basketProduct.getProductId());
            eventValues.put("af_quantity", basketProduct.getQuantity());
            eventValues.put("af_currency", "USD");
            AppsFlyerLib.trackEvent(JambaApplication.getAppContext(), "af_purchase", eventValues);
        }
    }

    /// Track user login
    public void trackUserLogin() {
        trackEvent("user_account", "login");
        HashMap<String, Object> eventValues = new HashMap<>();
        AppsFlyerLib.trackEvent(JambaApplication.getAppContext(), "af_login", eventValues);
    }

    /// Track user registration
    public void trackUserRegistration() {
        trackEvent("user_account", "signup");
        HashMap<String, Object> eventValues = new HashMap<>();
        AppsFlyerLib.trackEvent(JambaApplication.getAppContext(), "af_complete_registration", eventValues);
    }

    // Set/clear the user ID
    public void setUserId(String userId) {
        // Google Analytics
        tracker.set("&uid", userId);
        tracker.set("user_authenticated", userId == null ? "no" : "yes");
        // AppsFlyer
        AppsFlyerLib.setCustomerUserId(userId);
    }

    public void sendPendingEvents() {
        analytics.dispatchLocalHits();
    }
}

