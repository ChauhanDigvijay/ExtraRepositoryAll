package com.womensafety.httpimage;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBImageTable implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.parse("httpimage.provider.DataProvider/thumbnail");
    public static final String DATA = "Data";
    public static final String NAME = "Name";
    public static final String NUSE = "nUsed";
    public static final String SIZE = "Size";
    public static final String TIMESTAMP = "Timestamp";
}
