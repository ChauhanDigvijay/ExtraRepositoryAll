package com.identity.arx.student;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.identity.arx.QrCode.ScanBarActivity;
import com.identity.arx.R;
import com.identity.arx.camerafacedetection.FaceTrackerActivity;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.AESencrp;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.TimeTrap;
import com.identity.arx.gpslocation.GPSTracker;
import com.identity.arx.objectclass.AttendanceObject;
import com.identity.arx.objectclass.FacultyLectureAttendance;
import com.identity.arx.objectclass.LectureScheduleObject;


public class GiveAttendanceActivity extends MasterActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int RESULT_CODE = 0;
    double altitude;
    CourseDetailTable courseDetailTable;
    private TextView courseNameTextview;
    private TextView facultyNmaeTextview;
    GPSTracker gps;
    double latitude;
    LectureScheduleObject lectureScheduleObject;
    double longitude;
    String mPermission = "android.permission.ACCESS_FINE_LOCATION";
    private TextView resultTextview;
    Button scanQr;
    private TextView timingTextview;

    class C08342 implements OnClickListener {
        C08342() {
        }

        public void onClick(DialogInterface dialog, int which) {
            GiveAttendanceActivity.this.finish();
        }
    }

    class C08353 implements OnClickListener {
        C08353() {
        }

        public void onClick(DialogInterface dialog, int which) {
            GiveAttendanceActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_scan_qr);
        setActionBarTitle("No Lecture");
        this.resultTextview = (TextView) findViewById(R.id.scan_header);
        this.scanQr = (Button) findViewById(R.id.start_meet_button);
        this.courseDetailTable = new CourseDetailTable(this);
        this.lectureScheduleObject = getCurrentLecture();
        MasterActivity.deleteCache(getApplicationContext());
        if (this.lectureScheduleObject != null) {
            this.courseDetailTable.getCourseOverId(this.lectureScheduleObject.getCourseId() + "");
            return;
        }
        setContentView((int) R.layout.not_found_layout);
        ((TextView) findViewById(R.id.TextTabletopRPG)).setText("Sorry ! You Have No Current Lecture");
    }

    public void takeBarcode(View view) {
        if (this.scanQr.isEnabled()) {
            this.scanQr.setClickable(true);
            this.scanQr.setEnabled(true);
        } else {
            this.scanQr.setEnabled(false);
        }
        startActivityForResult(new Intent(this, ScanBarActivity.class), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (ContextCompat.checkSelfPermission(this, this.mPermission) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{this.mPermission}, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gps = new GPSTracker(this);
        if (this.gps.canGetLocation()) {
            this.latitude = this.gps.getLatitude();
            this.longitude = this.gps.getLongitude();
            if (this.gps.hasAltitude()) {
                this.altitude = this.gps.getAltitude();
            }
            if (this.latitude == 0.0d) {
                return;
            }
            if (requestCode != 0) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            } else if (resultCode != 0) {
                return;
            } else {
                if (data != null) {
                    Barcode barcode = (Barcode) data.getParcelableExtra("BARCODE");
                    try {
                        this.resultTextview.setText("Barcode Found : " + AESencrp.decrypt(barcode.displayValue));
                        String jsonString = AESencrp.decrypt(barcode.displayValue);
                        FacultyLectureAttendance facultylectureScheduleObject = (FacultyLectureAttendance) new Gson().fromJson(jsonString, FacultyLectureAttendance.class);
                        String roll_no = this.sharedPreference.getString("ROLL_NUM", "");
                        final AttendanceObject attendanceObject = new AttendanceObject();
                        attendanceObject.setRollNum(roll_no);
                        attendanceObject.setReason("NORMAL");
                        attendanceObject.setCourseId(this.lectureScheduleObject.getCourseId() + "");
                        attendanceObject.setStudentId(Integer.parseInt(this.sharedPreference.getString("USER_ID", "")));
                        attendanceObject.setStatus("Request");
                        attendanceObject.setIsProxy("NORMAL");
                        attendanceObject.setAttendence("P");
                        attendanceObject.setLectureid(this.lectureScheduleObject.getId());
                        if (TimeTrap.distFrom(this.latitude, this.longitude, facultylectureScheduleObject.getLatQr(), facultylectureScheduleObject.getLongQr()) > 300.0d || this.altitude != facultylectureScheduleObject.getAltitudeQr()) {
                            ApplicationDialog.setMessage("", "Might Be you are not at Venue", "OK", "", this).buildDialog(new C08353());
                            return;
                        } else if (facultylectureScheduleObject.getLectureId() == this.lectureScheduleObject.getId()) {
                            final FacultyLectureAttendance facultyLectureAttendance = facultylectureScheduleObject;
                            ApplicationDialog.setMessage("", "Please proceed for Face Recognition", "OK", "", this).buildDialog(new OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(GiveAttendanceActivity.this.getApplicationContext(), FaceTrackerActivity.class);
                                    intent.putExtra("Attandence", attendanceObject);
                                    intent.putExtra("FacultyQR", facultyLectureAttendance);
                                    GiveAttendanceActivity.this.startActivity(intent);
                                }
                            });
                            return;
                        } else {
                            ApplicationDialog.setMessage("", "Might Be You have another Lecture at this time", "OK", "", this).buildDialog(new C08342());
                            return;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                this.resultTextview.setText("No Barcode Found");
                return;
            }
        }
        this.gps.showSettingsAlert();
    }

    LectureScheduleObject getCurrentLecture() {
        for (LectureScheduleObject lectureScheduleObject : new LectureDetailTable(this).getCurrentDate(CalanderFormat.getDayofWeek())) {
            long timeDiff = TimeTrap.getTimeDifference(CalanderFormat.getTimeCalender(), lectureScheduleObject.getLectureStartTime());
            if (timeDiff <= 30 && timeDiff >= 0) {
                return lectureScheduleObject;
            }
        }
        return null;
    }

    public void onBackPressed() {
        finish();
    }
}
