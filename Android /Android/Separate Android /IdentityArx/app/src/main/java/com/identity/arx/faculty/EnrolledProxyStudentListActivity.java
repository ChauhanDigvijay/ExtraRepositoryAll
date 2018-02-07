package com.identity.arx.faculty;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.gson.Gson;
import com.identity.arx.R;
import com.identity.arx.HashMapJson;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.FacultyReportVo;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrolledProxyStudentListActivity extends MasterActivity {
    EnrolledStudentArrayadapter adapter;
    List<FacultyReportVo> liststudents;
    int selectedCourseId;
    ListView studentListView;

    class C07891 implements OnItemClickListener {
        C07891() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String selectedRollNum = ((FacultyReportVo) EnrolledProxyStudentListActivity.this.liststudents.get(position)).getRollNo();
            Intent intent = new Intent();
            intent.putExtra("ROLL_NUM", selectedRollNum);
            EnrolledProxyStudentListActivity.this.setResult(0, intent);
            EnrolledProxyStudentListActivity.this.finish();
        }
    }

    class C07902 implements AsyncResponse {
        C07902() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            ArrayList<Map<String, String>> usersMap = (ArrayList) response.getBody();
            EnrolledProxyStudentListActivity.this.liststudents = new ArrayList();
            EnrolledProxyStudentListActivity.this.liststudents.clear();
            for (int i = 0; i < usersMap.size(); i++) {
                EnrolledProxyStudentListActivity.this.liststudents.add((FacultyReportVo) new Gson().fromJson(HashMapJson.getJsonObject((Map) usersMap.get(i)).toString(), FacultyReportVo.class));
            }
            EnrolledProxyStudentListActivity.this.adapter = new EnrolledStudentArrayadapter(EnrolledProxyStudentListActivity.this.liststudents);
            EnrolledProxyStudentListActivity.this.studentListView.setAdapter(EnrolledProxyStudentListActivity.this.adapter);
        }
    }

    class C07913 implements OnQueryTextListener {
        C07913() {
        }

        public boolean onQueryTextChange(String newText) {
            EnrolledProxyStudentListActivity.this.adapter.getFilter().filter(newText);
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            EnrolledProxyStudentListActivity.this.adapter.getFilter().filter(query);
            System.out.println("on query submit: " + query);
            return true;
        }
    }

    private class EnrolledStudentArrayadapter extends BaseAdapter implements Filterable {
        List<FacultyReportVo> liststudentDetails;
        List<FacultyReportVo> mStringFilterList;
        ValueFilter valueFilter;

        private class ValueFilter extends Filter {
            private ValueFilter() {
            }

            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() <= 0) {
                    results.count = EnrolledStudentArrayadapter.this.mStringFilterList.size();
                    results.values = EnrolledStudentArrayadapter.this.mStringFilterList;
                } else {
                    ArrayList<FacultyReportVo> filterList = new ArrayList();
                    int i = 0;
                    while (i < EnrolledStudentArrayadapter.this.mStringFilterList.size()) {
                        if (((FacultyReportVo) EnrolledStudentArrayadapter.this.mStringFilterList.get(i)).getStudentName().toUpperCase().contains(constraint.toString().toUpperCase()) || ((FacultyReportVo) EnrolledStudentArrayadapter.this.mStringFilterList.get(i)).getRollNo().toUpperCase().contains(constraint.toString().toUpperCase()) || ((FacultyReportVo) EnrolledStudentArrayadapter.this.mStringFilterList.get(i)).getDeptName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            filterList.add((FacultyReportVo) EnrolledStudentArrayadapter.this.mStringFilterList.get(i));
                        }
                        i++;
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                }
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                EnrolledStudentArrayadapter.this.liststudentDetails = (List) results.values;
                EnrolledStudentArrayadapter.this.notifyDataSetChanged();
            }
        }

        public EnrolledStudentArrayadapter(List<FacultyReportVo> liststudentDetails) {
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


        public View getView(int position, View convertView, ViewGroup parent) {
            View view = EnrolledProxyStudentListActivity.this.getLayoutInflater().inflate(R.layout.enrolled_student_list_item, parent, false);
            FacultyReportVo facultyReportVo = (FacultyReportVo) this.liststudentDetails.get(position);
            TextView student_roll = (TextView) view.findViewById(R.id.proxy_roll_no);
            TextView student_dept = (TextView) view.findViewById(R.id.proxy_dept);
            TextView student_sem_year = (TextView) view.findViewById(R.id.proxy_sem_year);
            TextView student_contact = (TextView) view.findViewById(R.id.proxy_contact_student);
            TextView student_email = (TextView) view.findViewById(R.id.proxy_email_student);
            ((TextView) view.findViewById(R.id.proxy_student_name)).setText(facultyReportVo.getStudentName());
            student_sem_year.setText("Sem/Year     : " + facultyReportVo.getPersuingSemester() + "/" + facultyReportVo.getPersuingYear());
            student_roll.setText("Roll Number : " + facultyReportVo.getRollNo());
            student_dept.setText("Department  : " + facultyReportVo.getDeptName());
            student_contact.setText("Contact Number  : " + facultyReportVo.getContactNumber());
            student_email.setText("Email Id   :" + facultyReportVo.getEmail());
            return view;
        }

        public Filter getFilter() {
            if (this.valueFilter == null) {
                this.valueFilter = new ValueFilter();
            }
            return this.valueFilter;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_course_list);
        setActionBarTitle("Select Student");
        this.studentListView = (ListView) findViewById(R.id.course_list);
        this.selectedCourseId = getIntent().getIntExtra("SELECTEDCOURSEID", 0);
        getHttpStudentsDetails(Integer.valueOf(this.selectedCourseId));
        this.studentListView.setOnItemClickListener(new C07891());
    }

    private void getHttpStudentsDetails(Integer selectedCourseId) {
        FacultyReportVo facultyReportVo = new FacultyReportVo();
        facultyReportVo.setCourseID("" + selectedCourseId);
        facultyReportVo.setLoginStatus(1);
        new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.ins_id + "/getallstudentovercourseId", facultyReportVo, new C07902()).execute(new ResponseEntity[0]);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.institute_list_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(((SearchManager) getSystemService(Event.SEARCH)).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new C07913());
        return true;
    }
}
