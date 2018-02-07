package com.womensafety.httpimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

public class DBPersistence implements BitmapCache {
    private static final boolean DEBUG = false;
    private static final String TAG = "DBPersistence";
    private Context mContext;

    public DBPersistence(Context context) {
        this.mContext = context;
    }

    public boolean exists(String key) {
        return false;
    }

    public Bitmap loadData(String key) {
        Bitmap bitmap = null;
        Cursor c = null;
        try {
            c = this.mContext.getContentResolver().query(Uri.withAppendedPath(DBImageTable.CONTENT_URI, key), new String[]{DBImageTable.DATA}, null, null, null);
            if (c.getCount() < 1) {
                return null;
            }
            if (c.getCount() > 1) {
                throw new RuntimeException("shouldn't reach here, make sure the NAME collumn is unique: " + key);
            }
            c.moveToFirst();
            byte[] binary = c.getBlob(c.getColumnIndex(DBImageTable.DATA));
            if (binary != null) {
                bitmap = BitmapUtil.decodeByteArray(binary, HttpImageManager.DECODING_MAX_PIXELS_DEFAULT);
                if (bitmap == null) {
                    throw new RuntimeException("data from db can't be decoded to bitmap");
                }
            }
            if (c != null) {
                c.close();
            }
            return bitmap;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public void storeData(String key, Object data) {
        byte[] ba = (byte[]) data;
        if (ba != null) {
            ContentValues values = new ContentValues();
            values.put(DBImageTable.NAME, key);
            values.put(DBImageTable.DATA, ba);
            values.put(DBImageTable.SIZE, Integer.valueOf(ba.length));
            values.put(DBImageTable.NUSE, Integer.valueOf(1));
            values.put(DBImageTable.TIMESTAMP, Long.valueOf(System.currentTimeMillis()));
            this.mContext.getContentResolver().insert(DBImageTable.CONTENT_URI, values);
        }
    }

    public void clear() {
    }

    public void invalidate(String key) {
    }
}
