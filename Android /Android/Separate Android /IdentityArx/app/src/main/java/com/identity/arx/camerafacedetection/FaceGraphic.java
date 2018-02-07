package com.identity.arx.camerafacedetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;

import com.google.android.gms.vision.face.Face;
import com.identity.arx.camerafacedetection.cameraui.GraphicOverlay;

class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private static final int[] COLOR_CHOICES = new int[]{-16776961, -16711681, -16711936, -65281, SupportMenu.CATEGORY_MASK, -1, InputDeviceCompat.SOURCE_ANY};
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static int mCurrentColorIndex = 0;
    CaptureFace captureFace;
    int capturestatus = 0;
    float le;
    float leVal;
    private Paint mBoxPaint;
    Context mContext;
    private volatile Face mFace;
    private float mFaceHappiness;
    private int mFaceId;
    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    float re;
    float reVal;

    class C07671 implements Runnable {
        C07671() {
        }

        public void run() {
            if (FaceGraphic.this.reVal > 0.9f && FaceGraphic.this.leVal > 0.9f && FaceGraphic.this.capturestatus == 0) {
                FaceGraphic.this.captureFace.autoCaptureImage();
                FaceGraphic.this.capturestatus = 1;
            }
        }
    }

    FaceGraphic(GraphicOverlay overlay, Context mContext, CaptureFace captureFace) {
        super(overlay);
        this.mContext = mContext;
        this.captureFace = captureFace;
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
        this.mFacePositionPaint = new Paint();
        this.mFacePositionPaint.setColor(selectedColor);
        this.mIdPaint = new Paint();
        this.mIdPaint.setColor(selectedColor);
        this.mIdPaint.setTextSize(ID_TEXT_SIZE);
        this.mBoxPaint = new Paint();
        this.mBoxPaint.setColor(selectedColor);
        this.mBoxPaint.setStyle(Style.STROKE);
        this.mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        this.mFaceId = id;
    }

    void updateFace(Face face) {
        this.mFace = face;
        postInvalidate();
    }

    public void draw(Canvas canvas) {
        Face face = this.mFace;
        if (face != null) {
            float x = translateX(face.getPosition().x + (face.getWidth() / 2.0f));
            float y = translateY(face.getPosition().y + (face.getHeight() / 2.0f));
            canvas.drawCircle(x, y, FACE_POSITION_RADIUS, this.mFacePositionPaint);
            canvas.drawText("id: " + this.mFaceId, ID_X_OFFSET + x, ID_Y_OFFSET + y, this.mIdPaint);
            canvas.drawText("happiness:" + String.format("%.2f", new Object[]{Float.valueOf(face.getIsSmilingProbability())}), x - ID_X_OFFSET, y - ID_Y_OFFSET, this.mIdPaint);
            canvas.drawText("right eye: " + String.format("%.2f", new Object[]{Float.valueOf(face.getIsRightEyeOpenProbability())}), -100.0f + x, 100.0f + y, this.mIdPaint);
            canvas.drawText("left eye: " + String.format("%.2f", new Object[]{Float.valueOf(face.getIsLeftEyeOpenProbability())}), x - -100.0f, y - 100.0f, this.mIdPaint);
            this.re = face.getIsRightEyeOpenProbability();
            this.le = face.getIsLeftEyeOpenProbability();
            if (this.re > 0.9f && this.le > 0.9f && this.capturestatus == 0) {
                this.reVal = face.getIsRightEyeOpenProbability();
                this.leVal = face.getIsLeftEyeOpenProbability();
                new Handler().postDelayed(new C07671(), 1000);
            }
            float xOffset = scaleX(face.getWidth() / 2.0f);
            float yOffset = scaleY(face.getHeight() / 2.0f);
            canvas.drawRect(x - xOffset, y - yOffset, x + xOffset, y + yOffset, this.mBoxPaint);
        }
    }
}
