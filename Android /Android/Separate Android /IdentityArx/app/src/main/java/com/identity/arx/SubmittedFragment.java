package com.identity.arx;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.identity.arx.assignment.AssignmentTabActivity;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.AssignmentObject;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.textdrawable.TextDrawable;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubmittedFragment extends Fragment {
    public static String urlPath = null;
    CustomListAdapter adapter;
    CourseDetailTable courseDetailTable;
    int course_id;
    String labelId;
    List<CourseDetailsObject> listCourseDetailsObject;
    private ListView listViewSubmitted;
    String rollNo;
    SharedPreferences sharedPreference;
    String url;

    class C07591 implements AsyncResponse {
        C07591() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            List<Map<String, String>> usersMap = (List) response.getBody();
            List<AssignmentObject> assignmentlist = new ArrayList();
            for (int i = 0; i < usersMap.size(); i++) {
                assignmentlist.add((AssignmentObject) new Gson().fromJson(HashMapJson.getJsonObject((Map) usersMap.get(i)).toString(), AssignmentObject.class));
            }
            SubmittedFragment.this.adapter = new CustomListAdapter(assignmentlist);
            SubmittedFragment.this.listViewSubmitted.setAdapter(SubmittedFragment.this.adapter);
        }
    }

    class C07602 implements OnItemClickListener {
        C07602() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Toast.makeText(SubmittedFragment.this.getActivity(), "It seems OK", Toast.LENGTH_LONG).show();
            SubmittedFragment.this.url = "http://docs.google.com/gview?embedded=true&url=" + "https://drive.google.com/file/d/0ByeIyqyoheR7M1ZiUHd3MF9XTTQ/view?usp=drivesdk";
            Intent intent = new Intent(SubmittedFragment.this.getActivity(), AssignmentWebview.class);
            intent.putExtra("url", SubmittedFragment.this.url);
            SubmittedFragment.this.startActivity(intent);
        }
    }

    class C07613 implements OnClickListener {
        C07613() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SubmittedFragment.this.getActivity().finish();
        }
    }

    private class CustomListAdapter extends BaseAdapter {
        List<AssignmentObject> listassignment;

        public CustomListAdapter(List<AssignmentObject> listcourse) {
            this.listassignment = listcourse;
        }

        public int getCount() {
            return this.listassignment.size();
        }

        public Object getItem(int position) {
            return this.listassignment.get(position);
        }

        public long getItemId(int position) {
            return (long) this.listassignment.indexOf(getItem(position));
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = SubmittedFragment.this.getActivity().getLayoutInflater();
            AssignmentObject assignmentObject = (AssignmentObject) this.listassignment.get(position);
            if (assignmentObject.getAssignment_Title().equals("not available.")) {
                View view = inflater.inflate(R.layout.not_found_layout, parent, false);
                ((TextView) view.findViewById(R.id.TextTabletopRPG)).setText("Sorry ! You Have No Assignments");
                return view;
            }
            TextDrawable drawable;
            View view = inflater.inflate(R.layout.assignment_lecture_list_item, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            TextView textviewlecturetime = (TextView) view.findViewById(R.id.textviewlecturetime);
            TextView textview_course_name = (TextView) view.findViewById(R.id.textview_course_name);
            TextView textviewlecturelocation = (TextView) view.findViewById(R.id.textviewlecturelocation);
            TextView textviewduration = (TextView) view.findViewById(R.id.duration);
            if (position % 2 == 0) {
                drawable = TextDrawable.builder().buildRound(String.valueOf(assignmentObject.getAssignment_Title().charAt(0)), SupportMenu.CATEGORY_MASK);
            } else {
                drawable = TextDrawable.builder().buildRound(String.valueOf(assignmentObject.getAssignment_Title().charAt(0)), -16776961);
            }
            textviewlecturetime.setText(assignmentObject.getAssignment_Title());
            textviewlecturelocation.setText("Submission Date :" + assignmentObject.getDue_Date() + "");
            textviewduration.setText("Roll No" + assignmentObject.getRollNo());
            textview_course_name.setText(assignmentObject.getTot_marks());
            imageView.setImageDrawable(drawable);
            SubmittedFragment.urlPath = assignmentObject.getPath();
            return view;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_webview_assignment_list, container, false);
        this.listViewSubmitted = (ListView) view.findViewById(R.id.assignment_list);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(getActivity());
        this.courseDetailTable = new CourseDetailTable(getActivity());
        this.listCourseDetailsObject = this.courseDetailTable.getAllCourses();
        AssignmentObject assignmentobject = new AssignmentObject();
        this.course_id = ((AssignmentTabActivity) getActivity()).getCourseId();
        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            try {
                this.labelId = this.sharedPreference.getString("LABEL_ID", "");
                this.rollNo = this.sharedPreference.getString("ROLL_NUM", "");
                assignmentobject.setCourseId(String.valueOf(this.course_id));
                assignmentobject.setLoginStatus(Integer.valueOf(1));
                assignmentobject.setRollNo(this.rollNo);
                assignmentobject.setCourseType("SUBMITTED");
                assignmentobject.setLabelId(this.labelId);
                new HttpAsyncTask(getActivity(), WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/studentCourseList", assignmentobject, new C07591()).execute(new ResponseEntity[0]);
            } catch (Exception e) {
            }
            this.listViewSubmitted.setOnItemClickListener(new C07602());
        } else {
            ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", getActivity()).buildDialog(new C07613());
        }
        return view;
    }
}
