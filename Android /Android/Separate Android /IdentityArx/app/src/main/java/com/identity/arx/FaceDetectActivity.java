package com.identity.arx;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.imagemanupulation.ImageConverter;
import com.identity.arx.objectclass.AttendanceObject;
import com.identity.arx.objectclass.BiometricFaceResponseVo;
import com.identity.arx.objectclass.UploadProfilePic;

import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FaceDetectActivity extends MasterActivity {
    private static final int CAMERA_RETURN_CODE = 1410;
    AttendanceObject attendanceObject;
    private TextView biometricResponse;
    private FaceImageView faceImageView;

    class C07341 implements AsyncResponse {

        class C07321 implements OnClickListener {
            C07321() {
            }

            public void onClick(DialogInterface dialog, int which) {
                FaceDetectActivity.this.finish();
            }
        }

        class C07332 implements OnClickListener {
            C07332() {
            }

            public void onClick(DialogInterface dialog, int which) {
                FaceDetectActivity.this.finish();
            }
        }

        C07341() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            BiometricFaceResponseVo biometricFaceResponseVo = (BiometricFaceResponseVo) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), BiometricFaceResponseVo.class);
            Toast.makeText(FaceDetectActivity.this.getApplicationContext(), biometricFaceResponseVo.getMessage(), 0).show();
            FaceDetectActivity.this.biometricResponse = (TextView) FaceDetectActivity.this.findViewById(R.id.biometricResponse);
            if (biometricFaceResponseVo.getStatus().equalsIgnoreCase("true")) {
                FaceDetectActivity.this.biometricResponse.setText("Attendance sent Successfully!!!");
                ApplicationDialog.setMessage("", "Attendance sent Successfully!", "OK", "", FaceDetectActivity.this).buildDialog(new C07321());
                return;
            }
            FaceDetectActivity.this.biometricResponse.setText("Attendance not sent !!!");
            ApplicationDialog.setMessage("", "Attendance not sent.", "OK", "", FaceDetectActivity.this).buildDialog(new C07332());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_face_detect);
        this.faceImageView = (FaceImageView) findViewById(R.id.facedet);
        setSharedPrefrence();
        this.attendanceObject = (AttendanceObject) getIntent().getSerializableExtra("Attandence");
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), CAMERA_RETURN_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RETURN_CODE) {
            Bitmap cameraBmp = (Bitmap) data.getExtras().get("data");
            this.faceImageView.setImage(cameraBmp);
            Bitmap imgBitmap = this.faceImageView.detectFaces(cameraBmp);
            String timeStamp = new SimpleDateFormat("ss").format(new Date());
            uploadImage("test", imgBitmap);
        }
    }

    private void uploadImage(String fName, Bitmap bitmapImg) {
        byte[] bitmapString = ImageConverter.bitmapToByte(bitmapImg);
        UploadProfilePic uploadProfilePic = new UploadProfilePic();
        uploadProfilePic.setId(Integer.valueOf(1));
        uploadProfilePic.setImage(bitmapString);
        uploadProfilePic.setLoginStatus(Integer.valueOf(1));
        uploadProfilePic.setRole(Integer.valueOf(Integer.parseInt(this.sharedPreference.getString("LABEL_ID", ""))));
        uploadProfilePic.setMsg(fName);
        new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/faceauthimage", uploadProfilePic, new C07341()).execute(new ResponseEntity[0]);
    }
}
