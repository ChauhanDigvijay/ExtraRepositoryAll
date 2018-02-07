package com.womensafety.httpimage;

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.Map.Entry;

public class BasicBitmapCache implements BitmapCache {
    private static final boolean DEBUG = false;
    private static final String TAG = "BasicBitmapCache";
    private HashMap<String, CacheEntry> mMap = new HashMap();
    private int mMaxSize;

    private static class CacheEntry {
        public Bitmap data;
        public int nUsed;
        public long timestamp;

        private CacheEntry() {
        }
    }

    public BasicBitmapCache(int size) {
        this.mMaxSize = size;
    }

    public synchronized boolean exists(String key) {
        return this.mMap.get(key) != null;
    }

    public synchronized void invalidate(String key) {
        Bitmap data = ((CacheEntry) this.mMap.get(key)).data;
        this.mMap.remove(key);
    }

    public synchronized void clear() {
        for (String key : this.mMap.keySet()) {
            invalidate(key);
        }
    }

    protected synchronized String findItemToInvalidate() {
        Entry<String, CacheEntry> out;
        out = null;
        for (Entry<String, CacheEntry> e : this.mMap.entrySet()) {
            if (out == null || ((CacheEntry) e.getValue()).timestamp < ((CacheEntry) out.getValue()).timestamp) {
                out = e;
            }
        }
        return (String) out.getKey();
    }

    public synchronized Bitmap loadData(String key) {
        Bitmap bitmap;
        if (exists(key)) {
            CacheEntry res = (CacheEntry) this.mMap.get(key);
            res.nUsed++;
            res.timestamp = System.currentTimeMillis();
            bitmap = res.data;
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    public synchronized void storeData(String key, Object data) {
        if (!exists(key)) {
            CacheEntry res = new CacheEntry();
            res.nUsed = 1;
            res.timestamp = System.currentTimeMillis();
            res.data = (Bitmap) data;
            if (this.mMap.size() >= this.mMaxSize) {
                invalidate(findItemToInvalidate());
            }
            this.mMap.put(key, res);
        }
    }
}
