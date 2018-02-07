package com.identity.arx.faculty;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.identity.arx.HashMapJson;
import com.identity.arx.R;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.TimeTrap;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.AttendanceObject;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.textdrawable.TextDrawable;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;



public class ProxyAttendanceActivity extends MasterActivity {
    ArrayAdapter<LectureScheduleObject> adapter;
    final Context f32c = this;
    CourseDetailTable courseDetailTable;
    EditText dialog_editText_reason;
    EditText dialog_editText_rollno;
    LectureDetailTable lectureDetailTable;
    ListView lectureList;
    List<LectureScheduleObject> listLectureObject;
    List<LectureScheduleObject> proxylistLectureObject;
    String reason;
    private final int reqCode = 0;
    String rollNo;
    LectureScheduleObject selectedLectureSchedule;

    class C08001 implements OnItemClickListener {
        C08001() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ProxyAttendanceActivity.this.selectedLectureSchedule = (LectureScheduleObject) ProxyAttendanceActivity.this.listLectureObject.get(position);
            ProxyAttendanceActivity.this.showAlert();
        }
    }

    class C08022 implements AsyncResponse {

        class C08011 implements OnClickListener {
            C08011() {
            }

            public void onClick(DialogInterface dialog, int which) {
                ProxyAttendanceActivity.this.finish();
            }
        }

        C08022() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            String attendanceMessage;
            if (((AttendanceObject) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), AttendanceObject.class)).getStatus().equalsIgnoreCase("Success")) {
                attendanceMessage = "Attendance Sent Successfully";
            } else {
                attendanceMessage = "You have already Given the attendance of this Lecture";
            }
            ApplicationDialog.setMessage("", attendanceMessage, "OK", "", ProxyAttendanceActivity.this).buildDialog(new C08011());
        }
    }

    class C08033 implements OnClickListener {
        C08033() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ProxyAttendanceActivity.this.finish();
        }
    }

    class C08044 implements View.OnClickListener {
        C08044() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(ProxyAttendanceActivity.this, EnrolledProxyStudentListActivity.class);
            intent.putExtra("SELECTEDCOURSEID", ProxyAttendanceActivity.this.selectedLectureSchedule.getCourseId());
            ProxyAttendanceActivity.this.startActivityForResult(intent, 0);
        }
    }

    class C08055 implements OnClickListener {
        C08055() {
        }

        public void onClick(DialogInterface dialogBox, int id) {
            dialogBox.cancel();
        }
    }

    class C08066 implements OnClickListener {
        C08066() {
        }

        public void onClick(DialogInterface dialogBox, int id) {
            ProxyAttendanceActivity.this.rollNo = ProxyAttendanceActivity.this.dialog_editText_rollno.getText().toString();
            ProxyAttendanceActivity.this.reason = ProxyAttendanceActivity.this.dialog_editText_reason.getText().toString();
            ProxyAttendanceActivity.this.submit(ProxyAttendanceActivity.this.selectedLectureSchedule, ProxyAttendanceActivity.this.rollNo, ProxyAttendanceActivity.this.reason);
        }
    }

    private class LectureListAdapter extends ArrayAdapter<LectureScheduleObject> {
        public LectureListAdapter() {
            super(ProxyAttendanceActivity.this, R.layout.schedule_lecture_list_item, ProxyAttendanceActivity.this.listLectureObject);
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            TextDrawable drawable;
            View view = ProxyAttendanceActivity.this.getLayoutInflater().inflate(R.layout.schedule_lecture_list_item, parent, false);
            LectureScheduleObject lectureScheduleObject = (LectureScheduleObject) ProxyAttendanceActivity.this.listLectureObject.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            TextView textviewlecturetime = (TextView) view.findViewById(R.id.textviewlecturetime);
            TextView textview_course_name = (TextView) view.findViewById(R.id.textview_course_name);
            TextView textviewlecturelocation = (TextView) view.findViewById(R.id.textviewlecturelocation);
            TextView textviewduration = (TextView) view.findViewById(R.id.duration);
            if (position % 2 == 0) {
                drawable = TextDrawable.builder().buildRound(String.valueOf(lectureScheduleObject.getLectureDay().charAt(0)), SupportMenu.CATEGORY_MASK);
            } else {
                drawable = TextDrawable.builder().buildRound(String.valueOf(lectureScheduleObject.getLectureDay().charAt(0)), -16776961);
            }
            textviewlecturetime.setText(TimeTrap.convertTime24to12(lectureScheduleObject.getLectureStartTime()) + " - " + TimeTrap.convertTime24to12(lectureScheduleObject.getLectureEndTime()));
            textview_course_name.setText(ProxyAttendanceActivity.this.courseDetailTable.getCourseOverId(lectureScheduleObject.getCourseId() + "").getCourseName() + "");
            textviewlecturelocation.setText(lectureScheduleObject.getLectureLocation());
            textviewduration.setText(TimeTrap.getTimeDifference(lectureScheduleObject.getLectureStartTime(), lectureScheduleObject.getLectureEndTime()) + " min");
            imageView.setImageDrawable(drawable);
            return view;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_proxy_attendance);
        setActionBarTitle("Lecture");
        this.courseDetailTable = new CourseDetailTable(this);
        this.lectureList = (ListView) findViewById(R.id.current_lecture_list);
        this.proxylistLectureObject = new LectureDetailTable(this).getCurrentDate(CalanderFormat.getDayofWeek());
        this.listLectureObject = new ArrayList();
        for (LectureScheduleObject lectureScheduleObject : this.proxylistLectureObject) {
            if (TimeTrap.getTimeDifference(lectureScheduleObject.getLectureStartTime(), CalanderFormat.getTimeCalender()) > 0) {
                this.listLectureObject.add(lectureScheduleObject);
            }
        }
        this.adapter = new LectureListAdapter();
        this.lectureList.setAdapter(this.adapter);
        this.lectureList.setOnItemClickListener(new C08001());
    }

    private void submit(LectureScheduleObject lectureScheduleObject, String rollNo, String reason) {
        if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            return;
        }
        if (checkValidation()) {
            try {
                AttendanceObject attendanceObject = new AttendanceObject();
                attendanceObject.setRollNum(rollNo);
                attendanceObject.setReason(reason);
                attendanceObject.setCourseId(lectureScheduleObject.getCourseId() + "");
                attendanceObject.setStudentId(23);
                attendanceObject.setStatus("Request");
                attendanceObject.setIsProxy("PROXY");
                attendanceObject.setAttendence("P");
                attendanceObject.setLectureid(lectureScheduleObject.getId());
                new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/uploadAttndance", attendanceObject, new C08022()).execute(new ResponseEntity[0]);
                return;
            } catch (Exception e) {
                return;
            }
        }
        ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C08033());
    }

    boolean checkValidation() {
        if (this.dialog_editText_reason.getText().toString().trim().length() == 0) {
            this.dialog_editText_reason.setError("can not be empty");
        }
        if (this.dialog_editText_rollno.getText().toString().trim().length() == 0) {
            this.dialog_editText_rollno.setError("can not be empty");
        }
        if (this.dialog_editText_rollno.getText().toString().trim().length() == 0 || this.dialog_editText_rollno.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    private void showAlert() {
        View mView = LayoutInflater.from(this.f32c).inflate(R.layout.custom_dialog, null);
        Builder alertDialogBuilderUserInput = new Builder(this.f32c);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("Proxy Attendence");
        this.dialog_editText_rollno = (EditText) mView.findViewById(R.id.userInputDialog);
        this.dialog_editText_rollno.setOnClickListener(new C08044());
        this.dialog_editText_reason = (EditText) mView.findViewById(R.id.editText_reason_proxy);
        alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Submit", new C08066()).setNegativeButton("Cancel", new C08055());
        alertDialogBuilderUserInput.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && data != null) {
            this.dialog_editText_rollno.setText(data.getStringExtra("ROLL_NUM"));
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
