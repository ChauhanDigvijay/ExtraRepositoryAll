package com.olo.jambajuice.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.olo.jambajuice.JambaApplication;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Use this class to load image efficiently for a specific density.
 * FYI: Local images are placed for xxhdpi devices.
 */

public class BitmapUtils {
    private final static float XXHDPIDensityDpi = 3;
    private static HashMap<Integer, WeakReference<Bitmap>> bitmaps = new HashMap<>();

    public static Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight, boolean isCache) {

        Bitmap bitmap = getBitmapFromMap(resId);
        try {
            if (bitmap == null) {
                Resources res = JambaApplication.getAppContext().getResources();
                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(res, resId, options);
                DisplayMetrics displayMetrics = JambaApplication.getAppContext().getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                float scaleFactor = displayMetrics.density / XXHDPIDensityDpi; //Scale down the image for low resolution devices.
                if (Utils.isTablet()) {
                    //Do not scale down image in case of tablets
                    scaleFactor = 1;
                }
                if (reqWidth == 0) {
                    reqWidth = (int) (scaleFactor * options.outWidth);
                }
                if (reqHeight == 0) {
                    reqHeight = (int) (scaleFactor * options.outHeight);
                }

                reqWidth = Math.min(width, reqWidth);
                reqHeight = Math.min(height, reqHeight);
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                BitmapFactory.decodeResource(res, resId, options);
                bitmap = BitmapFactory.decodeResource(res, resId, options);
                if (isCache) {
                    bitmaps.put(resId, new WeakReference<Bitmap>(bitmap));
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    private static Bitmap getBitmapFromMap(int resId) {
        WeakReference<Bitmap> bmpRef = bitmaps.get(resId);
        Bitmap bitmap = null;
        if (bmpRef != null) {
            bitmap = bmpRef.get();
        }
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void loadBitmapResourceWithViewSize(final ImageView viewById, final int resId, final boolean isCache) {
        Utils.addOnGlobalLayoutListener(viewById, new Runnable() {
            @Override
            public void run() {
                viewById.setImageBitmap(decodeSampledBitmapFromResource(resId, viewById.getMeasuredWidth(), viewById.getMeasuredHeight(), isCache));
            }
        });
    }

    public static void loadBitmapResource(final ImageView viewById, final int resId) {
        loadBitmapResource(viewById, resId, false);
    }

    public static void loadBitmapResource(final ImageView viewById, final int resId, final boolean isCache) {
        viewById.setImageBitmap(decodeSampledBitmapFromResource(resId, 0, 0, isCache));
    }
}
