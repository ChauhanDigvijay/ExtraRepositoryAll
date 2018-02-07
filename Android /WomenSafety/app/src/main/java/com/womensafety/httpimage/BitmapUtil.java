package com.womensafety.httpimage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Environment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {
    private static final int UNCONSTRAINED = -1;

    public static Bitmap decodeByteArray(byte[] bytes, int maxNumOfPixels) {
        Bitmap bitmap = null;
        if (bytes != null) {
            try {
                Options option = new Options();
                option.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
                option.inJustDecodeBounds = false;
                option.inSampleSize = computeSampleSize(option, -1, maxNumOfPixels);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
            } catch (OutOfMemoryError oom) {
                oom.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        try {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();
            float scale = Math.max(((float) newWidth) / ((float) sourceWidth), ((float) newHeight) / ((float) sourceHeight));
            float scaledWidth = scale * ((float) sourceWidth);
            float scaledHeight = scale * ((float) sourceHeight);
            float left = (((float) newWidth) - scaledWidth) / 2.0f;
            float top = (((float) newHeight) - scaledHeight) / 2.0f;
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            new Canvas(dest).drawBitmap(source, null, targetRect, null);
            return dest;
        } catch (OutOfMemoryError e) {
            return source;
        } catch (Exception e2) {
            return source;
        }
    }

    public static Bitmap decodeStream(InputStream is, int maxNumOfPixels) {
        Bitmap bitmap = null;
        if (is != null) {
            try {
                bitmap = decodeByteArray(readStream(is), maxNumOfPixels);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String filePath, int maxNumOfPixels) {
        Bitmap decodeByteArray;
        IOException e;
        Throwable th;
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(new File(filePath));
            try {
                decodeByteArray = decodeByteArray(readStream(fis2), maxNumOfPixels);
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e2) {
                    }
                }
                fis = fis2;
            } catch (IOException e3) {
                e = e3;
                fis = fis2;
                try {
                    e.printStackTrace();
                    decodeByteArray = null;
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e4) {
                        }
                    }
                    return decodeByteArray;
                } catch (Throwable th2) {
                    th = th2;
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e5) {
                        }
                    }

                }
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (fis != null) {
                    fis.close();
                }

            }
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            decodeByteArray = null;
            if (fis != null) {

            }
            return null;
        }
        return null;
    }

    private static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize > 8) {
            return ((initialSize + 7) / 8) * 8;
        }
        int roundedSize = 1;
        while (roundedSize < initialSize) {
            roundedSize <<= 1;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int lowerBound;
        int upperBound;
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        if (maxNumOfPixels == -1) {
            lowerBound = 1;
        } else {
            lowerBound = (int) Math.ceil(Math.sqrt((w * h) / ((double) maxNumOfPixels)));
        }
        if (minSideLength == -1) {
            upperBound = 128;
        } else {
            upperBound = (int) Math.min(Math.floor(w / ((double) minSideLength)), Math.floor(h / ((double) minSideLength)));
        }
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (minSideLength != -1) {
            return upperBound;
        }
        return lowerBound;
    }

    private static byte[] readStream(InputStream is) throws IOException {
        if (is instanceof ByteArrayInputStream) {
            int size = is.available();
            byte[] buf = new byte[size];
            int len = is.read(buf, 0, size);
            return buf;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (true) {
            int len = is.read(buf, 0, 1024);
            if (len == -1) {
                return bos.toByteArray();
            }
            bos.write(buf, 0, len);
        }
    }

    public static String saveBitmap(Bitmap bitmap) {
        String filePath = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory() + "/BitsFlower");
            f.mkdirs();
            filePath = f.getAbsolutePath() + "/malabar_image" + ".png";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return filePath;
    }

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

    public static Bitmap decodeSampledBitmapFromResource(String imagePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateInSampleSize(options, options.outWidth / 3, options.outHeight / 3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }
}
