package com.identity.arx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.identity.arx.UserCalander.SetUserCalanderData;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.faculty.FacultyDrawerActivity;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpconnection.HttpUrlConnection;
import com.identity.arx.objectclass.CourseDetailRequestVo;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.objectclass.LectureScheduleRequestVo;
import com.identity.arx.objectclass.StudentTimeTableRequestVo;
import com.identity.arx.progressbar.MyCustomProgressDialog;
import com.identity.arx.student.StudentDrawerActivity;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public class LoaderActivity extends MasterActivity {

    private class LoaderHttpAsyncTask extends AsyncTask<ResponseEntity<?>, Void, ResponseEntity<?>> {
        AsyncResponse asyncResponse;
        Context mContext;
        Object obj;
        MyCustomProgressDialog progressDialog;
        String url;

        class C07391 implements OnClickListener {
            C07391() {
            }

            public void onClick(DialogInterface dialog, int which) {
                LoaderActivity.this.finish();
            }
        }

        public LoaderHttpAsyncTask(Context mContext) {
            this.mContext = mContext;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (this.mContext != null) {
                this.progressDialog = new MyCustomProgressDialog(this.mContext);
                this.progressDialog.setMessage("Please Wait... Setting Up System");
                this.progressDialog.show();
            }
        }

        protected ResponseEntity<?> doInBackground(ResponseEntity<?>... responseEntityArr) {
            ResponseEntity<?> response = null;
            if (ConnectionDetector.isConnectingToInternet(LoaderActivity.this.getApplicationContext())) {
                Gson gson;
                int i;
                String ins_id = LoaderActivity.this.sharedPreference.getString("INTITUTE_ID", "");
                CourseDetailRequestVo courseDetailRequestVo = new CourseDetailRequestVo();
                courseDetailRequestVo.setLoginStatus(1);
                courseDetailRequestVo.setUserType(LoaderActivity.this.sharedPreference.getString("USER_TYPE", ""));
                courseDetailRequestVo.setId(Integer.parseInt(LoaderActivity.this.sharedPreference.getString("USER_ID", "")));
                try {
                    List<Map<String, String>> responseList = (List) HttpUrlConnection.setHttpUrlConnection(courseDetailRequestVo, WebUrl.SERVER_ADDRESS + ins_id + "/course").getBody();
                    CourseDetailTable courseDetailTable = new CourseDetailTable(LoaderActivity.this);
                    gson = new Gson();
                    for (i = 0; i < responseList.size(); i++) {
                        courseDetailTable.insert((CourseDetailsObject) gson.fromJson(HashMapJson.getJsonObject((Map) responseList.get(i)).toString(), CourseDetailsObject.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String labelId = LoaderActivity.this.sharedPreference.getString("LABEL_ID", "");
                List<Map<String, String>> sheduleList;
                LectureDetailTable lectureDetailTable;
                if (LoaderActivity.this.sharedPreference.getString("USER_TYPE", "").equalsIgnoreCase("Faculty") && labelId.equals("35")) {
                    LectureScheduleRequestVo lectureScheduleRequestVo = new LectureScheduleRequestVo();
                    lectureScheduleRequestVo.setFacultyId(Integer.parseInt(LoaderActivity.this.sharedPreference.getString("USER_ID", "")));
                    lectureScheduleRequestVo.setLoginStatus(1);
                    response = HttpUrlConnection.setHttpUrlConnection(lectureScheduleRequestVo, WebUrl.SERVER_ADDRESS + ins_id + "/weeklyLectDetails");
                    try {
                        sheduleList = (List) response.getBody();
                        lectureDetailTable = new LectureDetailTable(LoaderActivity.this);
                        gson = new Gson();
                        lectureDetailTable.deleteTableData();
                        for (i = 0; i < sheduleList.size(); i++) {
                            lectureDetailTable.addLectureDetails((LectureScheduleObject) gson.fromJson(HashMapJson.getJsonObject((Map) sheduleList.get(i)).toString(), LectureScheduleObject.class));
                        }
                        SetUserCalanderData.setUserEventCalander(LoaderActivity.this);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    String rollNum = LoaderActivity.this.sharedPreference.getString("ROLL_NUM", "");
                    StudentTimeTableRequestVo studentTimeTableRequestVo = new StudentTimeTableRequestVo();
                    studentTimeTableRequestVo.setRollNumber(Integer.parseInt(rollNum));
                    studentTimeTableRequestVo.setLoginStatus(1);
                    response = HttpUrlConnection.setHttpUrlConnection(studentTimeTableRequestVo, WebUrl.SERVER_ADDRESS + ins_id + "/weeklyTimeTableDetails");
                    try {
                        sheduleList = (List) response.getBody();
                        lectureDetailTable = new LectureDetailTable(LoaderActivity.this);
                        gson = new Gson();
                        lectureDetailTable.deleteTableData();
                        for (i = 0; i < sheduleList.size(); i++) {
                            lectureDetailTable.addLectureDetails((LectureScheduleObject) gson.fromJson(HashMapJson.getJsonObject((Map) sheduleList.get(i)).toString(), LectureScheduleObject.class));
                        }
                        SetUserCalanderData.setUserEventCalander(LoaderActivity.this);
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            } else {
                ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", LoaderActivity.this).buildDialog(new C07391());
            }
            return response;
        }

        protected void onPostExecute(ResponseEntity<?> response) {
            super.onPostExecute(response);
            String labelId = LoaderActivity.this.sharedPreference.getString("LABEL_ID", "");
            if (labelId.equals("34")) {
                LoaderActivity.this.startActivity(new Intent(LoaderActivity.this, FacultyDrawerActivity.class));
            } else if (labelId.equals("35")) {
                LoaderActivity.this.startActivity(new Intent(LoaderActivity.this, FacultyDrawerActivity.class));
            } else if (labelId.equals("36")) {
                LoaderActivity.this.startActivity(new Intent(LoaderActivity.this, StudentDrawerActivity.class));
            }
            LoaderActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedPrefrence();
        new LoaderHttpAsyncTask(this).execute(new ResponseEntity[0]);
    }
}
