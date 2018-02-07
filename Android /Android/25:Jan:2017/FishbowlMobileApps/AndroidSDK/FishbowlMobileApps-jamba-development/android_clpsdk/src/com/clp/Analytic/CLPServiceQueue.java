package com.clp.Analytic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;

/**
Created By mohd Vaseem
*/
public class CLPServiceQueue {
	    private CLPEventPref clpEventPref;
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
 
	    void tick() {
	        if (!clpEventPref.isEmptyConnections() && (connectionProcessorFuture_ == null || connectionProcessorFuture_.isDone())) {
	            ensureExecutor();
	            connectionProcessorFuture_ = executor_.submit(new CLPEventService(context_, serverURL_,appKey_, clpEventPref));
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
	            throw new IllegalStateException("ClypAnalytic store has not been set");
	        }
	        if (serverURL_ == null || !ClypAnalytic.isValidURL(serverURL_)) {
	            throw new IllegalStateException("server URL is not valid");
	        }
	       
	    }
	    // for unit testing
	    ExecutorService getExecutor() { return executor_; }
	    void setExecutor(final ExecutorService executor) { executor_ = executor; }
	    Future<?> getConnectionProcessorFuture() { return connectionProcessorFuture_; }
	    void setConnectionProcessorFuture(final Future<?> connectionProcessorFuture) { connectionProcessorFuture_ = connectionProcessorFuture; }
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

	    CLPEventPref getCLPEventPref() {
	        return clpEventPref;
	    }

	    void setCLPEventPref(final CLPEventPref _clpEventPref) {
	        clpEventPref = _clpEventPref;
	    }
}
