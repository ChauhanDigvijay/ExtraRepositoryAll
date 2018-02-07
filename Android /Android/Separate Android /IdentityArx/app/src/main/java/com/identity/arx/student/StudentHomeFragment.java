package com.identity.arx.student;

import android.content.DialogInterface;
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
import com.identity.arx.HashMapJson;
import com.identity.arx.R;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.fingerprint.FingerprintActivity;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.objectclass.StudentTimeTableRequestVo;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class StudentHomeFragment extends Fragment implements OnClickListener {
    private Button btnGiveAttendance;
    private Button btnattendancereport;
    private Button btnlectureSchedule;
    SharedPreferences sharedPreference;

    class C08361 implements AsyncResponse {
        C08361() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            try {
                List<Map<String, String>> sheduleList = (List) response.getBody();
                LectureDetailTable lectureDetailTable = new LectureDetailTable(StudentHomeFragment.this.getActivity());
                Gson gson = new Gson();
                lectureDetailTable.deleteTableData();
                for (int i = 0; i < sheduleList.size(); i++) {
                    lectureDetailTable.addLectureDetails((LectureScheduleObject) gson.fromJson(HashMapJson.getJsonObject((Map) sheduleList.get(i)).toString(), LectureScheduleObject.class));
                }
                StudentHomeFragment.this.startActivity(new Intent(StudentHomeFragment.this.getActivity(), FingerprintActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C08372 implements DialogInterface.OnClickListener {
        C08372() {
        }

        public void onClick(DialogInterface dialog, int which) {
            StudentHomeFragment.this.getActivity().finish();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);
        this.btnGiveAttendance = (Button) view.findViewById(R.id.btn_give_attendance);
        this.btnGiveAttendance.setOnClickListener(this);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(getActivity());
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_give_attendance) {
            if (this.btnGiveAttendance.isEnabled()) {
                this.btnGiveAttendance.setClickable(true);
                this.btnGiveAttendance.setEnabled(true);
            } else {
                this.btnGiveAttendance.setEnabled(false);
            }
            if (ConnectionDetector.isConnectingToInternet(getActivity())) {
                String rollNum = this.sharedPreference.getString("ROLL_NUM", "");
                StudentTimeTableRequestVo studentTimeTableRequestVo = new StudentTimeTableRequestVo();
                studentTimeTableRequestVo.setRollNumber(Integer.parseInt(rollNum));
                studentTimeTableRequestVo.setLoginStatus(1);
                new HttpAsyncTask(getActivity(), WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/weeklyTimeTableDetails", studentTimeTableRequestVo, new C08361()).execute(new ResponseEntity[0]);
                return;
            }
            ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", getActivity()).buildDialog(new C08372());
        }
    }
}
