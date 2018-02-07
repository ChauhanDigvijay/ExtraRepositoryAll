package com.fishbowl.basicmodule.Analytics;

import android.content.Context;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by digvijay(dj)
 */
public class FBServiceQueue {
    private FBEventPreference clpEventPref;
    private ExecutorService executor_;
    private String appKey_;
    private Context context_;
    private String serverURL_;
    private Future<?> connectionProcessorFuture_;


    void recordEvents(final String eventsList) {
        checkInternalState();

        clpEventPref.addConnection(eventsList);

        tick();
    }


    void ensureExecutor() {
        if (executor_ == null) {
            executor_ = Executors.newSingleThreadExecutor();
        }
    }

    public void tick() {
        if (!clpEventPref.isEmptyConnections() && (connectionProcessorFuture_ == null || connectionProcessorFuture_.isDone())) {
            ensureExecutor();
            if (StringUtilities.isValidString(FBPreferences.sharedInstance(context_).getUserMemberforAppId())) {
                connectionProcessorFuture_ = executor_.submit(new FBEventService(context_, serverURL_, appKey_, clpEventPref));
            }
            else
            {
                connectionProcessorFuture_ = executor_.submit(new FBEventServiceGuest(context_, serverURL_, appKey_, clpEventPref));
            }
        }
    }


    void checkInternalState() {
        if (context_ == null) {
            throw new IllegalStateException("context has not been set");
        }
        if (appKey_ == null || appKey_.length() == 0) {
            throw new IllegalStateException("app key has not been set");
        }
        if (clpEventPref == null) {
            throw new IllegalStateException("FBAnalytic store has not been set");
        }
        if (serverURL_ == null || !FBAnalytic.isValidURL(serverURL_)) {
            throw new IllegalStateException("server URL is not valid");
        }

    }

    // for unit testing
    ExecutorService getExecutor() {
        return executor_;
    }

    void setExecutor(final ExecutorService executor) {
        executor_ = executor;
    }

    Future<?> getConnectionProcessorFuture() {
        return connectionProcessorFuture_;
    }

    void setConnectionProcessorFuture(final Future<?> connectionProcessorFuture) {
        connectionProcessorFuture_ = connectionProcessorFuture;
    }

    // Getters are for unit testing
    String getAppKey() {
        return appKey_;
    }

    void setAppKey(final String appKey) {
        appKey_ = appKey;
    }

    Context getContext() {
        return context_;
    }

    void setContext(final Context context) {
        context_ = context;
    }

    String getServerURL() {
        return serverURL_;
    }

    void setServerURL(final String serverURL) {
        serverURL_ = serverURL;
    }

    FBEventPreference getCLPEventPref() {
        return clpEventPref;
    }

    void setCLPEventPref(final FBEventPreference _clpEventPref) {
        clpEventPref = _clpEventPref;
    }
}
