package com.identity.arx.faculty;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.identity.arx.HashMapJson;
import com.identity.arx.QrCode.GenerateBarCodeImage;
import com.identity.arx.R;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.AESencrp;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.TimeTrap;
import com.identity.arx.general.WebUrl;
import com.identity.arx.gpslocation.GPSTracker;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.FacultyLectureAttendance;
import com.identity.arx.objectclass.LectureScheduleObject;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TakeAttendanceActivity extends MasterActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    double altitude;
    Bitmap bitmap;
    Button btnLectureStartEnd;
    CourseDetailTable courseDetailTable;
    FacultyLectureAttendance facultyLectureAttendanceResponse;
    GPSTracker gps;
    ImageView imageQRCode;
    ImageView imageQRCodeLagre;
    double latitude;
    int lectCourseId;
    LectureScheduleObject lectureScheduleObject;
    double longitude;
    String mPermission = "android.permission.ACCESS_FINE_LOCATION";
    TextView textViewLectureTime;
    TextView textViewcourseCode;
    TextView textViewcourseName;

    class C08101 implements OnClickListener {

        class C08091 implements AsyncResponse {

            class C08071 implements OnClickListener {
                C08071() {
                }

                public void onClick(View v) {
                    TakeAttendanceActivity.this.imageQRCodeLagre.setVisibility(0);
                    TakeAttendanceActivity.this.imageQRCodeLagre.setImageBitmap(TakeAttendanceActivity.this.bitmap);
                }
            }

            class C08082 implements OnClickListener {
                C08082() {
                }

                public void onClick(View v) {
                }
            }

            C08091() {
            }

            public void asyncResponse(ResponseEntity<?> response) {
                JSONObject jsonObject = HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody()));
                Gson gson = new Gson();
                TakeAttendanceActivity.this.facultyLectureAttendanceResponse = (FacultyLectureAttendance) gson.fromJson(jsonObject.toString(), FacultyLectureAttendance.class);
                if (TakeAttendanceActivity.this.facultyLectureAttendanceResponse.getStatus().equalsIgnoreCase("Success")) {
                    try {
                        String jsonLecture = AESencrp.encrypt(gson.toJson(TakeAttendanceActivity.this.facultyLectureAttendanceResponse));
                        try {
                            TakeAttendanceActivity.this.bitmap = GenerateBarCodeImage.TextToImageEncode(jsonLecture);
                            TakeAttendanceActivity.this.imageQRCode.setOnClickListener(new C08071());
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    TakeAttendanceActivity.this.imageQRCode.setImageBitmap(TakeAttendanceActivity.this.bitmap);
                    TakeAttendanceActivity.this.btnLectureStartEnd.setText("Complete Lecture");
                    TakeAttendanceActivity.this.btnLectureStartEnd.setOnClickListener(new C08082());
                    return;
                }
                Toast.makeText(TakeAttendanceActivity.this, "Try Again", 1).show();
            }
        }

        C08101() {
        }

        public void onClick(View v) {
            if (TakeAttendanceActivity.this.btnLectureStartEnd.isEnabled()) {
                TakeAttendanceActivity.this.btnLectureStartEnd.setClickable(true);
                TakeAttendanceActivity.this.btnLectureStartEnd.setEnabled(true);
            } else {
                TakeAttendanceActivity.this.btnLectureStartEnd.setEnabled(false);
            }
            try {
                if (ContextCompat.checkSelfPermission(TakeAttendanceActivity.this, TakeAttendanceActivity.this.mPermission) != 0) {
                    ActivityCompat.requestPermissions(TakeAttendanceActivity.this, new String[]{TakeAttendanceActivity.this.mPermission}, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TakeAttendanceActivity.this.gps = new GPSTracker(TakeAttendanceActivity.this);
            if (TakeAttendanceActivity.this.gps.canGetLocation()) {
                TakeAttendanceActivity.this.latitude = TakeAttendanceActivity.this.gps.getLatitude();
                TakeAttendanceActivity.this.longitude = TakeAttendanceActivity.this.gps.getLongitude();
                if (TakeAttendanceActivity.this.gps.hasAltitude()) {
                    TakeAttendanceActivity.this.altitude = TakeAttendanceActivity.this.gps.getAltitude();
                }
                String userId = TakeAttendanceActivity.this.sharedPreference.getString("USER_ID", "");
                FacultyLectureAttendance facultyLectureAttendance = new FacultyLectureAttendance();
                facultyLectureAttendance.setLectureId(TakeAttendanceActivity.this.lectureScheduleObject.getId());
                facultyLectureAttendance.setCreatedBy(Integer.parseInt(userId));
                facultyLectureAttendance.setCourseId(TakeAttendanceActivity.this.lectCourseId);
                facultyLectureAttendance.setLatQr(TakeAttendanceActivity.this.latitude);
                facultyLectureAttendance.setLongQr(TakeAttendanceActivity.this.longitude);
                facultyLectureAttendance.setAltitudeQr(TakeAttendanceActivity.this.altitude);
                new HttpAsyncTask(TakeAttendanceActivity.this, WebUrl.SERVER_ADDRESS + TakeAttendanceActivity.this.sharedPreference.getString("INTITUTE_ID", "") + "/uploadFacultyLectureAttendance", facultyLectureAttendance, new C08091()).execute(new ResponseEntity[0]);
                return;
            }
            TakeAttendanceActivity.this.gps.showSettingsAlert();
        }
    }

    class C08112 implements DialogInterface.OnClickListener {
        C08112() {
        }

        public void onClick(DialogInterface dialog, int which) {
            TakeAttendanceActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_take_attendance);
        setActionBarTitle("No Lecture");
        this.imageQRCode = (ImageView) findViewById(R.id.image_qr_code);
        this.imageQRCodeLagre = (ImageView) findViewById(R.id.image_lagre_qr_code);
        this.textViewcourseCode = (TextView) findViewById(R.id.textview_course_code);
        this.textViewcourseName = (TextView) findViewById(R.id.textview_course_name);
        this.textViewLectureTime = (TextView) findViewById(R.id.textview_timing);
        this.btnLectureStartEnd = (Button) findViewById(R.id.start__button);
        this.lectureScheduleObject = getCurrentLecture();
        if (this.lectureScheduleObject != null) {
            this.courseDetailTable = new CourseDetailTable(this);
            this.lectCourseId = this.lectureScheduleObject.getCourseId();
            CourseDetailsObject courseDetailsObject = this.courseDetailTable.getCourseOverId(this.lectCourseId + "");
            this.textViewcourseCode.setText(courseDetailsObject.getCourseId());
            this.textViewcourseName.setText(courseDetailsObject.getCourseName() + "(" + this.lectureScheduleObject.getLectureLocation() + ")");
            setActionBarTitle(courseDetailsObject.getCourseName());
            this.textViewLectureTime.setText(TimeTrap.convertTime24to12(this.lectureScheduleObject.getLectureStartTime()) + " - " + TimeTrap.convertTime24to12(this.lectureScheduleObject.getLectureEndTime()));
        } else {
            setContentView((int) R.layout.not_found_layout);
            ((TextView) findViewById(R.id.TextTabletopRPG)).setText("Nothing Scheduled for Now");
        }
        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            this.btnLectureStartEnd.setOnClickListener(new C08101());
        } else {
            ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C08112());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (this.imageQRCodeLagre.isShown()) {
                    this.imageQRCodeLagre.setVisibility(8);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.imageQRCodeLagre.isShown()) {
            this.imageQRCodeLagre.setVisibility(8);
        } else {
            finish();
        }
    }

    LectureScheduleObject getCurrentLecture() {
        for (LectureScheduleObject lectureScheduleObject : new LectureDetailTable(this).getCurrentDate(CalanderFormat.getDayofWeek())) {
            String lectStartTime = lectureScheduleObject.getLectureStartTime();
            String currentTime = CalanderFormat.getTimeCalender();
            String addTime = TimeTrap.getAdditionTime(10);
            long timeDiff = TimeTrap.getTimeDifference(currentTime, lectStartTime);
            long timeDiffAfter = TimeTrap.getTimeDifference(lectStartTime, addTime);
            if (timeDiff <= 30 && timeDiff >= 0) {
                return lectureScheduleObject;
            }
            if (timeDiffAfter <= 20 && timeDiffAfter > 0) {
                return lectureScheduleObject;
            }
        }
        return null;
    }
}
