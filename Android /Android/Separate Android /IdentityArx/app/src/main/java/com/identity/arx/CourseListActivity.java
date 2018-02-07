package com.identity.arx;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.StudentTimeTableRequestVo;
import com.identity.arx.student.AttendanceReportActivity;
import com.identity.arx.textdrawable.TextDrawable;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseListActivity extends MasterActivity implements OnItemClickListener {
    CourseDetailTable courseDetailTable;
    String courseId;
    List<CourseDetailsObject> listCourseDetailsObject;
    private ListView listviewCourse;
    String loginStatus;
    String rollNo;
    String studentAttendencePercent;

    class C07281 implements AsyncResponse {
        C07281() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            ArrayList<Map<String, String>> attendPer = (ArrayList) response.getBody();
            for (int i = 0; i < attendPer.size(); i++) {
                Map<String, String> insList = (Map) attendPer.get(i);
                String attendancepercent = (String) insList.get("Attendancepercent");
                String courseId = String.valueOf(insList.get("courseId"));
                for (CourseDetailsObject courseDetailsObject : CourseListActivity.this.listCourseDetailsObject) {
                    if (String.valueOf(courseDetailsObject.getId()).equalsIgnoreCase(courseId)) {
                        try {
                            courseDetailsObject.setNoOfLectures(Integer.parseInt(attendancepercent));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            CourseListActivity.this.listviewCourse.setAdapter(new CustomListAdapter(CourseListActivity.this.listCourseDetailsObject));
        }
    }

    class C07292 implements OnClickListener {
        C07292() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CourseListActivity.this.finish();
        }
    }

    private class CustomListAdapter extends ArrayAdapter<CourseDetailsObject> {
        List<CourseDetailsObject> listcourse;

        public CustomListAdapter(List<CourseDetailsObject> listcourse) {
            super(CourseListActivity.this, R.layout.schedule_lecture_list_item, listcourse);
            this.listcourse = listcourse;
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            TextDrawable drawable;
            View view = CourseListActivity.this.getLayoutInflater().inflate(R.layout.schedule_lecture_list_item, parent, false);
            CourseDetailsObject courseDetailsObject = (CourseDetailsObject) this.listcourse.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            TextView textviewlecturetime = (TextView) view.findViewById(R.id.textviewlecturetime);
            TextView textview_course_name = (TextView) view.findViewById(R.id.textview_course_name);
            TextView textviewlecturelocation = (TextView) view.findViewById(R.id.textviewlecturelocation);
            TextView textviewduration = (TextView) view.findViewById(R.id.duration);
            if (position % 2 == 0) {
                drawable = TextDrawable.builder().buildRound(String.valueOf(courseDetailsObject.getCourseName().charAt(0)), SupportMenu.CATEGORY_MASK);
            } else {
                drawable = TextDrawable.builder().buildRound(String.valueOf(courseDetailsObject.getCourseName().charAt(0)), -16776961);
            }
            textviewlecturetime.setText(courseDetailsObject.getCourseName());
            textview_course_name.setText(courseDetailsObject.getAssignFaculty() + "");
            textviewlecturelocation.setText("Attendance: " + courseDetailsObject.getNoOfLectures() + "%");
            textviewduration.setText("");
            imageView.setImageDrawable(drawable);
            return view;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_course_list);
        setActionBarTitle("Course List");
        this.listviewCourse = (ListView) findViewById(R.id.course_list);
        this.listviewCourse.setOnItemClickListener(this);
        this.listCourseDetailsObject = new CourseDetailTable(this).getAllCourses();
        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            StudentTimeTableRequestVo studentTimeTableRequestVo = new StudentTimeTableRequestVo();
            try {
                this.rollNo = this.sharedPreference.getString("ROLL_NUM", "");
                studentTimeTableRequestVo.setRollNumber(Integer.parseInt(this.rollNo));
                studentTimeTableRequestVo.setLoginStatus(1);
                new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/getAttendancePercent", studentTimeTableRequestVo, new C07281()).execute(new ResponseEntity[0]);
                return;
            } catch (Exception e) {
                return;
            }
        }
        ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C07292());
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String rollNo = this.sharedPreference.getString("ROLL_NUM", "");
        Intent intent = new Intent(this, AttendanceReportActivity.class);
        intent.putExtra("COURSE_NAME", ((CourseDetailsObject) this.listCourseDetailsObject.get(position)).getCourseName());
        intent.putExtra("COURSE_id", ((CourseDetailsObject) this.listCourseDetailsObject.get(position)).getId());
        intent.putExtra("rollNo", rollNo);
        startActivity(intent);
    }
}
