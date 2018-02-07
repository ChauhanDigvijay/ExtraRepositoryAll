package com.identity.arx.faculty;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.gson.Gson;
import com.identity.arx.R;
import com.identity.arx.HashMapJson;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.FacultyReportVo;
import com.identity.arx.student.AttendanceReportActivity;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacultyReportActivity extends MasterActivity {
    FacultyReportArrayadapter adapter;
    private GoogleApiClient client;
    CourseDetailTable courseDetailTable;
    List<CourseDetailsObject> listCourseDetailsObject;
    TextView resultFoundTextview;
    int selectedCourseId;
    Spinner spinner;
    ListView studentList;
    ListView studentListView;

    class C07931 implements OnItemSelectedListener {
        C07931() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            FacultyReportActivity.this.selectedCourseId = ((CourseDetailsObject) FacultyReportActivity.this.listCourseDetailsObject.get(position)).getId();
            FacultyReportActivity.this.getHttpStudentsDetails(FacultyReportActivity.this.selectedCourseId);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07942 implements OnQueryTextListener {
        C07942() {
        }

        public boolean onQueryTextChange(String newText) {
            FacultyReportActivity.this.adapter.getFilter().filter(newText);
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            FacultyReportActivity.this.adapter.getFilter().filter(query);
            System.out.println("on query submit: " + query);
            return true;
        }
    }

    class C07953 implements AsyncResponse {
        C07953() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            ArrayList<Map<String, String>> usersMap = (ArrayList) response.getBody();
            List<FacultyReportVo> liststudents = new ArrayList();
            liststudents.clear();
            for (int i = 0; i < usersMap.size(); i++) {
                liststudents.add((FacultyReportVo) new Gson().fromJson(HashMapJson.getJsonObject((Map) usersMap.get(i)).toString(), FacultyReportVo.class));
            }
            FacultyReportActivity.this.resultFoundTextview.setText(liststudents.size() + " Result Found");
            FacultyReportActivity.this.adapter = new FacultyReportArrayadapter(liststudents);
            FacultyReportActivity.this.studentListView.setAdapter(FacultyReportActivity.this.adapter);
        }
    }

    private class FacultyReportArrayadapter extends BaseAdapter implements Filterable {
        List<FacultyReportVo> liststudentDetails;
        List<FacultyReportVo> mStringFilterList;
        ValueFilter valueFilter;

        private class ValueFilter extends Filter {
            private ValueFilter() {
            }

            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() <= 0) {
                    results.count = FacultyReportArrayadapter.this.mStringFilterList.size();
                    results.values = FacultyReportArrayadapter.this.mStringFilterList;
                } else {
                    ArrayList<FacultyReportVo> filterList = new ArrayList();
                    int i = 0;
                    while (i < FacultyReportArrayadapter.this.mStringFilterList.size()) {
                        if (((FacultyReportVo) FacultyReportArrayadapter.this.mStringFilterList.get(i)).getStudentName().toUpperCase().contains(constraint.toString().toUpperCase()) || ((FacultyReportVo) FacultyReportArrayadapter.this.mStringFilterList.get(i)).getRollNo().toUpperCase().contains(constraint.toString().toUpperCase()) || ((FacultyReportVo) FacultyReportArrayadapter.this.mStringFilterList.get(i)).getDeptName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            filterList.add((FacultyReportVo) FacultyReportArrayadapter.this.mStringFilterList.get(i));
                        }
                        i++;
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                }
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                FacultyReportArrayadapter.this.liststudentDetails = (List) results.values;
                FacultyReportArrayadapter.this.notifyDataSetChanged();
            }
        }

        public FacultyReportArrayadapter(List<FacultyReportVo> liststudentDetails) {
            this.liststudentDetails = liststudentDetails;
            this.mStringFilterList = liststudentDetails;
        }

        public int getCount() {
            return this.liststudentDetails.size();
        }

        public Object getItem(int position) {
            return this.liststudentDetails.get(position);
        }

        public long getItemId(int position) {
            return (long) this.liststudentDetails.indexOf(getItem(position));
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            FacultyReportActivity.this.resultFoundTextview.setText(this.liststudentDetails.size() + " Result Found");
            View view = FacultyReportActivity.this.getLayoutInflater().inflate(R.layout.student_list_item, parent, false);
            final FacultyReportVo facultyReportVo = (FacultyReportVo) this.liststudentDetails.get(position);
            TextView student_roll = (TextView) view.findViewById(R.id.roll_no);
            TextView student_dept = (TextView) view.findViewById(R.id.dept);
            TextView student_sem_year = (TextView) view.findViewById(R.id.sem_year);
            TextView student_attendance = (TextView) view.findViewById(R.id.attendence_percent);
            ImageView call = (ImageView) view.findViewById(R.id.call);
            ImageView mail = (ImageView) view.findViewById(R.id.mail);
            ImageView report = (ImageView) view.findViewById(R.id.report);
            ((TextView) view.findViewById(R.id.student_name)).setText(facultyReportVo.getStudentName());
            student_sem_year.setText("Sem/Year     : " + facultyReportVo.getPersuingSemester() + "/" + facultyReportVo.getPersuingYear());
            student_roll.setText("Roll Number : " + facultyReportVo.getRollNo());
            student_dept.setText("Department  : " + facultyReportVo.getDeptName());
            student_attendance.setText("Attendance  : " + facultyReportVo.getAttendancePercentage() + "%");
            call.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    FacultyReportActivity.this.startActivity(new Intent("android.intent.action.DIAL", Uri.fromParts("tel", facultyReportVo.getContactNumber(), null)));
                }
            });
            mail.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    FacultyReportActivity.this.startActivity(new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", facultyReportVo.getEmail(), null)));
                }
            });
            report.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(FacultyReportActivity.this.getApplicationContext(), AttendanceReportActivity.class);
                    intent.putExtra("COURSE_NAME", FacultyReportActivity.this.spinner.getSelectedItem().toString());
                    intent.putExtra("COURSE_id", FacultyReportActivity.this.selectedCourseId);
                    intent.putExtra("rollNo", facultyReportVo.getRollNo());
                    FacultyReportActivity.this.startActivity(intent);
                }
            });
            return view;
        }

        public Filter getFilter() {
            if (this.valueFilter == null) {
                this.valueFilter = new ValueFilter();
            }
            return this.valueFilter;
        }
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, 17367043, objects);
            this.mDropDownHelper = new Helper(context);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = this.mDropDownHelper.getDropDownViewInflater().inflate(17367043, parent, false);
            } else {
                view = convertView;
            }
            ((TextView) view.findViewById(16908308)).setText((CharSequence) getItem(position));
            return view;
        }

        public Theme getDropDownViewTheme() {
            return this.mDropDownHelper.getDropDownViewTheme();
        }

        public void setDropDownViewTheme(Theme theme) {
            this.mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.studentListView = (ListView) findViewById(R.id.listview);
        this.resultFoundTextview = (TextView) findViewById(R.id.resulfoundtextview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSharedPrefrence();
        this.courseDetailTable = new CourseDetailTable(this);
        this.listCourseDetailsObject = this.courseDetailTable.getAllCourses();
        String[] spinnerArraydata = new String[this.listCourseDetailsObject.size()];
        for (int i = 0; i < spinnerArraydata.length; i++) {
            spinnerArraydata[i] = ((CourseDetailsObject) this.listCourseDetailsObject.get(i)).getCourseName();
        }
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.spinner.setAdapter(new MyAdapter(toolbar.getContext(), spinnerArraydata));
        this.spinner.setOnItemSelectedListener(new C07931());
        this.client = new Builder(this).addApi(AppIndex.API).build();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.institute_list_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(((SearchManager) getSystemService(Event.SEARCH)).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new C07942());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Action getIndexApiAction() {
        return new Action.Builder(Action.TYPE_VIEW).setObject(new Thing.Builder().setName("FacultyReport Page").setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]")).build()).setActionStatus("http://schema.org/CompletedActionStatus").build();
    }

    public void onStart() {
        super.onStart();
        this.client.connect();
        AppIndex.AppIndexApi.start(this.client, getIndexApiAction());
    }

    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(this.client, getIndexApiAction());
        this.client.disconnect();
    }

    private void getHttpStudentsDetails(int courseId) {
        FacultyReportVo facultyReportVo = new FacultyReportVo();
        facultyReportVo.setCourseID("" + courseId);
        facultyReportVo.setLoginStatus(1);
        new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.ins_id + "/getallstudentovercourseId", facultyReportVo, new C07953()).execute(new ResponseEntity[0]);
    }
}
