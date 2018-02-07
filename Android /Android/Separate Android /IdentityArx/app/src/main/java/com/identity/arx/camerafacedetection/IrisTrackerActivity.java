package com.identity.arx.camerafacedetection;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.CameraSource.PictureCallback;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.MultiProcessor.Factory;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.FaceDetector.Builder;
import com.google.gson.Gson;
import com.identity.arx.R;
import com.identity.arx.HashMapJson;
import com.identity.arx.camerafacedetection.cameraui.CameraSourcePreview;
import com.identity.arx.camerafacedetection.cameraui.FaceResponseActivity;
import com.identity.arx.camerafacedetection.cameraui.GraphicOverlay;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.CellModel;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.imagemanupulation.ImageConverter;
import com.identity.arx.model.FaceResult;
import com.identity.arx.objectclass.AttendanceObject;
import com.identity.arx.objectclass.BiometricFaceResponseVo;
import com.identity.arx.objectclass.FacultyLectureAttendance;
import com.identity.arx.objectclass.UploadProfilePic;
import com.identity.arx.utils.ImageUtils;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class IrisTrackerActivity extends MasterActivity implements CaptureFace {
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_GMS = 9001;
    private static final String TAG = "IrisTracker";
    public static TextView textview;
    AttendanceObject attendanceObject;
    byte[] bitmapString;
    int count = 2;
    FacultyLectureAttendance facultyLectureAttendance;
    byte[] image;
    String imageName;
    String imagePath;
    PointF leftEye;
    PointF rightEye;
    private CameraSource mCameraSource = null;
    private volatile Face mFace;
    private GraphicOverlay mGraphicOverlay;
    private CameraSourcePreview mPreview;
    private String messagedata;
    private String status;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView((int) R.layout.camera_main);
        this.mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        this.mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        setSharedPrefrence();
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
        Intent i = getIntent();
        this.attendanceObject = (AttendanceObject) i.getSerializableExtra("Message");
        this.facultyLectureAttendance = (FacultyLectureAttendance) i.getSerializableExtra("imageString");
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");
        final String[] permissions = new String[]{"android.permission.CAMERA"};
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CAMERA")) {
            final Activity thisActivity = this;
            View.OnClickListener listener = new View.OnClickListener() {
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(thisActivity, permissions, 2);
                }
            };
            return;
        }
        ActivityCompat.requestPermissions(this, permissions, 2);
    }

    private void createCameraSource() {
        Context context = getApplicationContext();
        FaceDetector detector = new Builder(context).setTrackingEnabled(false).setLandmarkType(1).setClassificationType(1).build();
        detector.setProcessor(new MultiProcessor.Builder(new GraphicFaceTrackerFactory()).build());
        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }
        this.mCameraSource = new CameraSource.Builder(context, detector).setRequestedPreviewSize(640, 480).setFacing(1).setRequestedFps(30.0f).build();
    }

    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    protected void onPause() {
        super.onPause();
        this.mPreview.stop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mCameraSource != null) {
            this.mCameraSource.release();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 2) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults.length == 0 || grantResults[0] != 0) {
            Log.e(TAG, "Permission not granted: results len = " + grantResults.length + " Result code = " + (grantResults.length > 0 ? Integer.valueOf(grantResults[0]) : "(empty)"));
            OnClickListener listener = new C07792();
        } else {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            createCameraSource();
        }
    }

    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (code != 0) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS).show();
        }
        if (this.mCameraSource != null) {
            try {
                this.mPreview.start(this.mCameraSource, this.mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                this.mCameraSource.release();
                this.mCameraSource = null;
            }
        }
    }

    public void autoCaptureImage() {
        this.mCameraSource.takePicture(null, new C07803());
    }

    public Bitmap detectFaces(Bitmap bitmap) {
        Bitmap cropedFace = null;
        Bitmap tmpBmp = bitmap.copy(Config.RGB_565, false);
        android.media.FaceDetector.Face[] faceList = new android.media.FaceDetector.Face[1];
        new android.media.FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 1).findFaces(tmpBmp, faceList);
        ArrayList<FaceResult> faces_ = new ArrayList();
        for (int i = 0; i < 1; i++) {
            if (faceList[i] != null) {
                PointF mid = new PointF();
                faceList[i].getMidPoint(mid);
                float eyesDis = faceList[i].eyesDistance();
                float confidence = faceList[i].confidence();
                float pose = faceList[i].pose(1);
                FaceResult faceResult = new FaceResult();
                faceResult.setFace(0, mid, eyesDis, confidence, pose, System.currentTimeMillis());
                faces_.add(faceResult);
                FaceDetector faceDetector = new Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(1).setClassificationType(1).build();
                cropedFace = ImageUtils.cropIris(faceResult, bitmap, 0);
                uploadImage(this.sharedPreference.getString("USER_NAME", ""), cropedFace);
            }
        }
        return cropedFace;
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

    private void uploadImage(String fName, Bitmap bitmapImg) {
        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            this.bitmapString = ImageConverter.bitmapToByte(bitmapImg);
            UploadProfilePic uploadProfilePic = new UploadProfilePic();
            uploadProfilePic.setId(Integer.valueOf(Integer.parseInt(this.sharedPreference.getString("USER_ID", ""))));
            uploadProfilePic.setImage(this.bitmapString);
            uploadProfilePic.setLoginStatus(Integer.valueOf(1));
            uploadProfilePic.setRole(Integer.valueOf(Integer.parseInt(this.sharedPreference.getString("LABEL_ID", ""))));
            uploadProfilePic.setMsg(fName);
            new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/eyeauthimage", uploadProfilePic, new C07834()).execute(new ResponseEntity[0]);
            return;
        }
        ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C07845());
    }

    public void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile != null) {
            try {
                byte[] byteImage = ImageConverter.bitmapToByte(image);
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(byteImage);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e2) {
            }
        }
    }

    public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IdentityAXS Profile Pics");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        this.imageName = File.separator + "IMG_" + timeStamp + ".jpg";
        return mediaFile;
    }

    public void onBackPressed() {
    }

    public void sendingOutput(AttendanceObject attendanceObject) {
        try {
            new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/uploadAttndance", attendanceObject, new C07866()).execute(new ResponseEntity[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class C07792 implements OnClickListener {
        C07792() {
        }

        public void onClick(DialogInterface dialog, int id) {
            IrisTrackerActivity.this.finish();
        }
    }

    class C07803 implements PictureCallback {
        C07803() {
        }

        public void onPictureTaken(byte[] bytes) {
            IrisTrackerActivity.this.mCameraSource.stop();
            IrisTrackerActivity.this.mPreview.stop();
            IrisTrackerActivity.this.image = bytes;
            Bitmap bitmap = ImageConverter.byteArrayToBitmap(bytes);
            if (CellModel.getDeviceName().equalsIgnoreCase("Samsung SM-J110H")) {
                bitmap = ImageUtils.rotate(bitmap, 270.0f);
            }
            bitmap = IrisTrackerActivity.this.detectFaces(bitmap);
        }
    }

    class C07834 implements AsyncResponse {

        C07834() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            BiometricFaceResponseVo biometricFaceResponseVo = (BiometricFaceResponseVo) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), BiometricFaceResponseVo.class);
            String message = biometricFaceResponseVo.getMessage();
            IrisTrackerActivity.this.status = biometricFaceResponseVo.getStatus();
            IrisTrackerActivity.this.messagedata = biometricFaceResponseVo.getMessage();
            if (IrisTrackerActivity.this.status.equalsIgnoreCase("YES")) {
                IrisTrackerActivity.this.sendingOutput(IrisTrackerActivity.this.attendanceObject);
            } else if (IrisTrackerActivity.this.count <= 3) {
                ApplicationDialog.setMessage("", "Attendance not sent. \n" + IrisTrackerActivity.this.messagedata, "Retry", "", IrisTrackerActivity.this).buildDialog(new C07811());
            } else {
                Toast.makeText(IrisTrackerActivity.this.getApplicationContext(), "You are not Authorised person", 1);
                ApplicationDialog.setMessage("", "Go for Proxy \n" + IrisTrackerActivity.this.messagedata, "", "OK", IrisTrackerActivity.this).buildDialog(new C07822());
            }
        }

        class C07811 implements OnClickListener {
            C07811() {
            }

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = IrisTrackerActivity.this.getIntent();
                IrisTrackerActivity.this.finish();
                IrisTrackerActivity.this.startActivity(intent);
                IrisTrackerActivity irisTrackerActivity = IrisTrackerActivity.this;
                irisTrackerActivity.count++;
            }
        }

        class C07822 implements OnClickListener {
            C07822() {
            }

            public void onClick(DialogInterface dialog, int which) {
                IrisTrackerActivity.this.finish();
            }
        }
    }

    class C07845 implements OnClickListener {
        C07845() {
        }

        public void onClick(DialogInterface dialog, int which) {
            IrisTrackerActivity.this.finish();
        }
    }

    class C07866 implements AsyncResponse {

        C07866() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            String attendanceMessage;
            if (((AttendanceObject) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), AttendanceObject.class)).getStatus().equalsIgnoreCase("Success")) {
                attendanceMessage = "Great!! Attendance has been marked ";
            } else {
                attendanceMessage = "You have already MARKED the Attendance";
            }
            ApplicationDialog.setMessage("", attendanceMessage, "OK", "", IrisTrackerActivity.this).buildDialog(new C07851());
        }

        class C07851 implements OnClickListener {
            C07851() {
            }

            public void onClick(DialogInterface dialog, int which) {
                IrisTrackerActivity.this.startActivity(new Intent(IrisTrackerActivity.this, FaceResponseActivity.class));
                IrisTrackerActivity.this.finish();
            }
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private FaceGraphic mFaceGraphic;
        private GraphicOverlay mOverlay;

        GraphicFaceTracker(GraphicOverlay overlay) {
            this.mOverlay = overlay;
            this.mFaceGraphic = new FaceGraphic(overlay, IrisTrackerActivity.this, IrisTrackerActivity.this);
        }

        public void onNewItem(int faceId, Face item) {
            this.mFaceGraphic.setId(faceId);
        }

        public void onUpdate(Detections<Face> detections, Face face) {
            this.mOverlay.add(this.mFaceGraphic);
            this.mFaceGraphic.updateFace(face);
        }

        public void onMissing(Detections<Face> detections) {
            this.mOverlay.remove(this.mFaceGraphic);
        }

        public void onDone() {
            this.mOverlay.remove(this.mFaceGraphic);
        }
    }

    private class GraphicFaceTrackerFactory implements Factory<Face> {
        private GraphicFaceTrackerFactory() {
        }

        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(IrisTrackerActivity.this.mGraphicOverlay);
        }
    }
}
