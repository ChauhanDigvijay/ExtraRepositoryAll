package com.identity.arx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.identity.arx.model.FaceResult;
import com.identity.arx.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class FaceImageView extends ImageView {
    private static final int MAX_FACES = 1;
    Context context;
    private Bitmap image;
    private RectF[] rects;

    public FaceImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.rects = new RectF[1];
        this.context = context;
    }

    public FaceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.rects = new RectF[1];
        this.context = context;
    }

    public FaceImageView(Context context) {
        super(context);
        this.rects = new RectF[1];
        this.context = context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.image, 0.0f, 0.0f, new Paint());
        Paint rectPaint = new Paint();
        rectPaint.setStrokeWidth(2.0f);
        rectPaint.setColor(-16776961);
        rectPaint.setStyle(Style.STROKE);
        for (int i = 0; i < 1; i++) {
            RectF r = this.rects[i];
            if (r != null) {
                canvas.drawRect(r, rectPaint);
            }
        }
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap detectFaces() {
        Log.d("FaceDet", "Detecting face....");
        Bitmap tmpBmp = this.image.copy(Config.RGB_565, true);
        Bitmap cropedFace = null;
        Face[] faceList = new Face[1];
        new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 1).findFaces(tmpBmp, faceList);
        for (int i = 0; i < faceList.length; i++) {
            Face face = faceList[i];
            Log.d("FaceDet", "Face [" + face + "]");
            if (face != null) {
                Log.d("FaceDet", "Face [" + i + "] - Confidence [" + face.confidence() + "]");
                PointF pf = new PointF();
                face.getMidPoint(pf);
                Log.d("FaceDet", "\t Eyes distance [" + face.eyesDistance() + "] - Face midpoint [" + pf + "]");
                RectF r = new RectF();
                r.left = pf.x - face.eyesDistance();
                r.right = pf.x + face.eyesDistance();
                r.top = pf.y - face.eyesDistance();
                r.bottom = (pf.y + face.eyesDistance()) + (face.eyesDistance() / 2.0f);
                this.rects[i] = r;
                FaceResult faceResult = new FaceResult();
                faceResult.setFace(0, pf, face.eyesDistance(), face.confidence(), face.pose(1), System.currentTimeMillis());
                cropedFace = getResizedBitmap(ImageUtils.cropFace(faceResult, tmpBmp, 0), 100, 100);
                storeImage(cropedFace);
                Toast.makeText(getContext(), "Eye Distance  " + face.eyesDistance(), 1).show();
                Toast.makeText(getContext(), "rects" + this.rects[i], 1).show();
            }
        }
        invalidate();
        return cropedFace;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/identityakash");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }
        return new File(mediaStorageDir.getPath() + File.separator + ("MI_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".jpg"));
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e2) {
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap detectFaces(Bitmap bitmap) {
        Bitmap cropedFace = null;
        Bitmap tmpBmp = this.image.copy(Config.RGB_565, true);
        Face[] faceList = new Face[1];
        new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 1).findFaces(tmpBmp, faceList);
        ArrayList<FaceResult> faces_ = new ArrayList();
        for (int i = 0; i < 1; i++) {
            if (faceList[i] != null) {
                PointF mid = new PointF();
                faceList[i].getMidPoint(mid);
                float eyesDis = faceList[i].eyesDistance();
                float confidence = faceList[i].confidence();
                float pose = faceList[i].pose(1);
                Rect rect = new Rect((int) (mid.x - (1.2f * eyesDis)), (int) (mid.y - (0.55f * eyesDis)), (int) (mid.x + (1.2f * eyesDis)), (int) (mid.y + (1.85f * eyesDis)));
                FaceResult faceResult = new FaceResult();
                faceResult.setFace(0, mid, eyesDis, confidence, pose, System.currentTimeMillis());
                faces_.add(faceResult);
                cropedFace = getResizedBitmap(ImageUtils.cropFace(faceResult, bitmap, 0), 100, 100);
            }
        }
        return cropedFace;
    }
}
