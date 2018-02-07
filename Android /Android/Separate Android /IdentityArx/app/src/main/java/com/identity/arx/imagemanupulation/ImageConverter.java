package com.identity.arx.imagemanupulation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class ImageConverter {
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean compress = bitmap.compress(CompressFormat.JPEG, 80, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public static Bitmap stringToBitMap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, 0);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        boolean compress = bitmap.compress(CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] bitmapdata) {
        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }
}
