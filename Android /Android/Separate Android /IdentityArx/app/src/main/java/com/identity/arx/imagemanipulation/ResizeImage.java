package com.identity.arx.imagemanipulation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResizeImage {
    public Bitmap getBitmap(Uri uri) {
        try {
            InputStream in = new FileInputStream(uri.getPath());
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int inWidth = options.outWidth;
            int inHeight = options.outHeight;
            in = new FileInputStream(uri.getPath());
            options = new Options();
            options.inSampleSize = Math.max(inWidth / 1000, inHeight / 1000);
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0.0f, 0.0f, (float) roughBitmap.getWidth(), (float) roughBitmap.getHeight()), new RectF(0.0f, 0.0f, 1000.0f, 1000.0f), ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (((float) roughBitmap.getWidth()) * values[0]), (int) (((float) roughBitmap.getHeight()) * values[4]), true);
            try {
                resizedBitmap.compress(CompressFormat.JPEG, 80, new FileOutputStream(uri.getPath()));
                return resizedBitmap;
            } catch (Exception e) {
                Log.e("Image", e.getMessage(), e);
                return null;
            }
        } catch (IOException e2) {
            Log.e("Image", e2.getMessage(), e2);
        }
        return null;
    }
}
