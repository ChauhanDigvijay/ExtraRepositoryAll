package com.identity.arx.QrCode;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.view.ViewCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateBarCodeImage {
    public static Bitmap TextToImageEncode(String Value) throws WriterException {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BarcodeFormat barcodeFormat = BarcodeFormat.DATA_MATRIX;
            BitMatrix bitMatrix = multiFormatWriter.encode(Value, BarcodeFormat.QR_CODE, 500, 500, null);
            int bitMatrixWidth = bitMatrix.getWidth();
            int bitMatrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[(bitMatrixWidth * bitMatrixHeight)];
            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? ViewCompat.MEASURED_STATE_MASK : -1;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Config.ARGB_4444);
            bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
