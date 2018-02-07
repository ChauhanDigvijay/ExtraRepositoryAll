package com.identity.arx.faculty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.identity.arx.R;
import com.identity.arx.HashMapJson;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.WebUrl;
import com.identity.arx.gpslocation.GPSTracker;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.objectclass.LectureScheduleRequestVo;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class FacultyHomeFragment extends Fragment implements OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 2;
    private Button btnLectureSchedule;
    private Button btnProxyAttendance;
    private Button btnReport;
    private Button btnTakeAttendance;
    GPSTracker gps;
    double latitude;
    double longitude;
    String mPermission = "android.permission.ACCESS_FINE_LOCATION";
    SharedPreferences sharedPreference;

    class C07921 implements AsyncResponse {
        C07921() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            try {
                List<Map<String, String>> sheduleList = (List) response.getBody();
                LectureDetailTable lectureDetailTable = new LectureDetailTable(FacultyHomeFragment.this.getActivity());
                Gson gson = new Gson();
                lectureDetailTable.deleteTableData();
                for (int i = 0; i < sheduleList.size(); i++) {
                    lectureDetailTable.addLectureDetails((LectureScheduleObject) gson.fromJson(HashMapJson.getJsonObject((Map) sheduleList.get(i)).toString(), LectureScheduleObject.class));
                }
                FacultyHomeFragment.this.startActivity(new Intent(FacultyHomeFragment.this.getActivity(), TakeAttendanceActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_home, container, false);
        this.btnTakeAttendance = (Button) view.findViewById(R.id.btn_take_attendance);
        this.btnTakeAttendance.setOnClickListener(this);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(getActivity());
        getActivity().setTitle("Home");
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_take_attendance) {
            if (this.btnTakeAttendance.isEnabled()) {
                this.btnTakeAttendance.setClickable(true);
                this.btnTakeAttendance.setEnabled(true);
            } else {
                this.btnTakeAttendance.setEnabled(false);
            }
            String userId = this.sharedPreference.getString("FACULTY_ID", "");
            LectureScheduleRequestVo lectureScheduleRequestVo = new LectureScheduleRequestVo();
            lectureScheduleRequestVo.setFacultyId(Integer.parseInt(userId));
            lectureScheduleRequestVo.setLoginStatus(1);
            new HttpAsyncTask(getActivity(), WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/weeklyLectDetails", lectureScheduleRequestVo, new C07921()).execute(new ResponseEntity[0]);
        }
    }
}
