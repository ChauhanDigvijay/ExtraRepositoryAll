package com.identity.arx.camerafacedetection.cameraui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.student.StudentDrawerActivity;


public class FaceResponseActivity extends AppCompatActivity {
    private Button continueAfterFace;
    private ImageView cropedFace;
    private TextView distance;
    private SharedPreferences sharedPreference;
    private TextView username;

    class C07881 implements OnClickListener {
        C07881() {
        }

        public void onClick(View v) {
            FaceResponseActivity.this.startActivity(new Intent(FaceResponseActivity.this, StudentDrawerActivity.class));
            FaceResponseActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_face_response);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        this.distance = (TextView) findViewById(R.id.responseBYRserve);
        this.username = (TextView) findViewById(R.id.diplay_name);
        this.cropedFace = (ImageView) findViewById(R.id.cropedFace);
        this.continueAfterFace = (Button) findViewById(R.id.continueAfterFace);
        this.username.setText("Hey " + this.sharedPreference.getString("NAME", "") + ",you have done a great job!!!");
        MasterActivity.deleteCache(getApplicationContext());
        this.continueAfterFace.setOnClickListener(new C07881());
    }

    public void onBackPressed() {
    }
}
