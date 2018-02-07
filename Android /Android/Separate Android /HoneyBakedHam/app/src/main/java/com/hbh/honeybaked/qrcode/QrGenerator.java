package com.hbh.honeybaked.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;

public final class QrGenerator {
    private int mBgColor;
    private int mColor;
    private String mContent;
    private ErrorCorrectionLevel mEcl;
    private String mFootNote;
    private int mMargin;
    private Bitmap mOverlay;
    private int mOverlayAlpha;
    private int mOverlaySize;
    private int mQrSize;
    private Mode mXfermode;

    public static class Builder {
        private int mBgColor = -1;
        private int mColor = ViewCompat.MEASURED_STATE_MASK;
        private String mContent;
        private ErrorCorrectionLevel mEcl = ErrorCorrectionLevel.L;
        private String mFootNote;
        private int mMargin = 2;
        private Bitmap mOverlay;
        private int mOverlayAlpha = 255;
        private int mOverlaySize;
        private int mQrSize;
        private Mode mXFerMode = Mode.SRC_OVER;



        public Builder content(String content) {
            this.mContent = content;
            return this;
        }

        public Builder qrSize(int widthAndHeight) {
            this.mQrSize = widthAndHeight;
            return this;
        }

        public Builder margin(int margin) {
            this.mMargin = margin;
            return this;
        }

        public Builder color(int color) {
            this.mColor = color;
            return this;
        }

        public Builder color(Context context, int color) {
            return color(context.getResources().getColor(color));
        }

        public Builder bgColor(int bgColor) {
            this.mBgColor = bgColor;
            return this;
        }

        public Builder ecc(ErrorCorrectionLevel ecl) {
            this.mEcl = ecl;
            return this;
        }

        public Builder overlay(Bitmap overlay) {
            this.mOverlay = overlay;
            return this;
        }

        public Builder overlay(Context context, int overlay) {
            return overlay(BitmapFactory.decodeResource(context.getResources(), overlay));
        }

        public Builder overlaySize(int overlaySize) {
            this.mOverlaySize = overlaySize;
            return this;
        }

        public Builder overlayAlpha(int alpha) {
            this.mOverlayAlpha = alpha;
            return this;
        }

        public Builder overlayXfermode(Mode xfermode) {
            this.mXFerMode = xfermode;
            return this;
        }

        public Builder footNote(String note) {
            this.mFootNote = note;
            return this;
        }
    }

    private QrGenerator(Builder builder) {
        this.mMargin = builder.mMargin;
        this.mQrSize = builder.mQrSize;
        this.mColor = builder.mColor;
        this.mBgColor = builder.mBgColor;
        this.mContent = builder.mContent;
        this.mEcl = builder.mEcl;
        this.mOverlay = builder.mOverlay;
        this.mOverlaySize = builder.mOverlaySize;
        this.mOverlayAlpha = builder.mOverlayAlpha;
        this.mXfermode = builder.mXFerMode;
        this.mFootNote = builder.mFootNote;
    }

    private Bitmap createQRCode() throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, this.mEcl);
        hints.put(EncodeHintType.MARGIN, Integer.valueOf(this.mMargin));
        BitMatrix matrix = new MultiFormatWriter().encode(this.mContent, BarcodeFormat.QR_CODE, this.mQrSize, this.mQrSize, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[(width * height)];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[(y * width) + x] = this.mColor;
                } else {
                    pixels[(y * width) + x] = this.mBgColor;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        if (this.mOverlay != null && this.mOverlaySize > 0) {
            Bitmap w = Bitmap.createBitmap(this.mOverlay);
            Bitmap o = w.copy(Config.ARGB_8888, true);
            w.recycle();
            int overlayW = o.getWidth();
            int overlayH = o.getHeight();
            int scaledH = (this.mOverlaySize * overlayW) / overlayH;
            int offsetX = (this.mQrSize - this.mOverlaySize) / 2;
            int offsetY = (this.mQrSize - scaledH) / 2;
            Paint paint = new Paint(2);
            paint.setAlpha(this.mOverlayAlpha);
            paint.setXfermode(new PorterDuffXfermode(this.mXfermode));
            Canvas canvas = new Canvas(bitmap);
            Rect rect = new Rect(0, 0, overlayW, overlayH);
            rect = new Rect(0, 0, this.mOverlaySize, scaledH);
            canvas.translate((float) offsetX, (float) offsetY);
            canvas.drawBitmap(o, rect, rect, paint);
        }
        if (TextUtils.isEmpty(this.mFootNote)) {
            return bitmap;
        }
        Bitmap result = Bitmap.createBitmap(this.mQrSize, (this.mQrSize * 3) / 2, Config.ARGB_8888);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(this.mColor);
        textPaint.setTextSize(20.0f);
        textPaint.setAntiAlias(true);
        StaticLayout mTextLayout = new StaticLayout(this.mFootNote, textPaint, this.mQrSize, Alignment.ALIGN_CENTER, 1.4f, 0.2f, false);

        return result;
    }
}
