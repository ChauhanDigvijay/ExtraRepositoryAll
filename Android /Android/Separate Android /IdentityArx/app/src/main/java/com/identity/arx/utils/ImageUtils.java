package com.identity.arx.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;

import com.identity.arx.model.FaceResult;

import java.io.IOException;

public class ImageUtils {
    static PointF leftEye;
    static Context mContext;
    static PointF rightEye;

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static final String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static final Bitmap rotate(Bitmap b, float degrees) {
        if (degrees == 0.0f || b == null) {
            return b;
        }
        Matrix m = new Matrix();
        m.setRotate(degrees, ((float) b.getWidth()) / 2.0f, ((float) b.getHeight()) / 2.0f);
        Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
        if (b == b2) {
            return b;
        }
        b.recycle();
        return b2;
    }

    public static Bitmap getBitmap(String filePath, int width, int height) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null) {
            return bitmap;
        }
        try {
            switch (new ExifInterface(filePath).getAttributeInt("Orientation", 1)) {
                case 3:
                    return rotate(bitmap, 180.0f);
                case 6:
                    return rotate(bitmap, 90.0f);
                case 8:
                    return rotate(bitmap, 270.0f);
                default:
                    return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect) {
        Bitmap ret = Bitmap.createBitmap(rect.right - rect.left, rect.bottom - rect.top, bitmap.getConfig());
        new Canvas(ret).drawBitmap(bitmap, (float) (-rect.left), (float) (-rect.top), null);
        bitmap.recycle();
        return ret;
    }

    public static Bitmap cropIris(FaceResult face, Bitmap bitmap, int rotate) {
        float eyesDis = face.eyesDistance();
        PointF mid = new PointF();
        face.getMidPoint(mid);
        Rect rect = new Rect((int) mid.x, (int) (mid.y - (eyesDis / 4.0f)), (int) (mid.x + eyesDis), (int) (mid.y + ((eyesDis / 4.0f) * 1.5f)));
        Config config = Config.RGB_565;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        Bitmap bmp = bitmap.copy(config, true);
        switch (rotate) {
            case 90:
                bmp = rotate(bmp, 90.0f);
                break;
            case 180:
                bmp = rotate(bmp, 180.0f);
                break;
            case 270:
                bmp = rotate(bmp, 270.0f);
                break;
        }
        return cropBitmap(bmp, rect);
    }

    public static Bitmap cropFace(FaceResult face, Bitmap bitmap, int rotate) {
        float eyesDis = face.eyesDistance();
        PointF mid = new PointF();
        face.getMidPoint(mid);
        Rect rect = new Rect((int) (mid.x - eyesDis), (int) (mid.y - eyesDis), (int) (mid.x + eyesDis), (int) (mid.y + (1.5f * eyesDis)));
        Config config = Config.RGB_565;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        Bitmap bmp = bitmap.copy(config, true);
        switch (rotate) {
            case 90:
                bmp = rotate(bmp, 90.0f);
                break;
            case 180:
                bmp = rotate(bmp, 180.0f);
                break;
            case 270:
                bmp = rotate(bmp, 270.0f);
                break;
        }
        return cropBitmap(bmp, rect);
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint);
        return bmpGrayscale;
    }
}
