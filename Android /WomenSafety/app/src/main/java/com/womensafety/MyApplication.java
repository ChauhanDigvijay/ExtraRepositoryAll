package com.womensafety;

import android.app.Application;
import android.content.Context;

import com.womensafety.httpimage.FileSystemPersistence;
import com.womensafety.httpimage.FilesStorage;
import com.womensafety.httpimage.HttpImageManager;


public class MyApplication extends Application {
    public static String CODE_LOCK = "CODE_LOCK";
    public static String MyLock = "Lock";
    public static String SERVICE_LOCK = "SERVICE_LOCK";
    public static Context mContext = null;
    private HttpImageManager mHttpImageManager;

    public void onCreate() {
        super.onCreate();
        mContext = this;
        this.mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(FilesStorage.getImageCacheDirectory()));
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public HttpImageManager getHttpImageManager() {
        return this.mHttpImageManager;
    }
}
