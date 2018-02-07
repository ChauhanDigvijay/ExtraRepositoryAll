package com.identity.arx.QrCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.barcode.BarcodeDetector.Builder;
import com.identity.arx.R;
import com.identity.arx.student.StudentDrawerActivity;

import java.io.IOException;

public class ScanBarActivity extends Activity {
    private SurfaceView surfaceView;

    class C07522 implements Processor<Barcode> {
        C07522() {
        }

        public void release() {
        }

        public void receiveDetections(Detections<Barcode> detections) {
            SparseArray<Barcode> barcodes = detections.getDetectedItems();
            if (barcodes.size() > 0) {
                Intent intent = new Intent();
                Intent barcode = intent.putExtra("BARCODE", (Parcelable) barcodes.valueAt(0));
                ScanBarActivity.this.setResult(0, intent);
                ScanBarActivity.this.finish();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        this.surfaceView = (SurfaceView) findViewById(R.id.barcode_scanner);
        createCameraSource();
    }

    public void createCameraSource() {
        BarcodeDetector barcodeDetector = new Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1024).build();
        this.surfaceView.getHolder().addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(ScanBarActivity.this, "android.permission.CAMERA") == 0) {
                        cameraSource.start(ScanBarActivity.this.surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new C07522());
    }

    public void onBackPressed() {
        startActivity(new Intent(this, StudentDrawerActivity.class));
        finish();
        finish();
    }
}
