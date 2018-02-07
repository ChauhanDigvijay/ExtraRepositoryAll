package com.womensafety.httpimage;

import android.graphics.Bitmap;

public interface BitmapCache {
    void clear();

    boolean exists(String str);

    void invalidate(String str);

    Bitmap loadData(String str);

    void storeData(String str, Object obj);
}
