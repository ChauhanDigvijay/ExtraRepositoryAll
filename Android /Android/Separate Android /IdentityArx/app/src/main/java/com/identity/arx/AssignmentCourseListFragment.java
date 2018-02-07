package com.identity.arx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.identity.arx.assignment.AssignmentTabActivity;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.textdrawable.TextDrawable;

import java.util.List;

public class AssignmentCourseListFragment extends Fragment {
    List<CourseDetailsObject> listCourseDetailsObject;
    private ListView listviewCourse;

    class C07251 implements OnItemClickListener {
        C07251() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Toast.makeText(AssignmentCourseListFragment.this.getActivity(), "It seems right", 1).show();
            Intent intent = new Intent(AssignmentCourseListFragment.this.getActivity(), AssignmentTabActivity.class);
            intent.putExtra("COURSE_NAME", ((CourseDetailsObject) AssignmentCourseListFragment.this.listCourseDetailsObject.get(position)).getCourseName());
            intent.putExtra("COURSE_id", ((CourseDetailsObject) AssignmentCourseListFragment.this.listCourseDetailsObject.get(position)).getId());
            AssignmentCourseListFragment.this.startActivity(intent);
        }
    }

    private class CustomListAdapter extends ArrayAdapter<CourseDetailsObject> {
        List<CourseDetailsObject> listcourse;

        public CustomListAdapter(List<CourseDetailsObject> listcourse) {
            super(AssignmentCourseListFragment.this.getActivity(), R.layout.schedule_lecture_list_item, listcourse);
            this.listcourse = listcourse;
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            TextDrawable drawable;
            View view = AssignmentCourseListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.schedule_lecture_list_item, parent, false);
            CourseDetailsObject courseDetailsObject = (CourseDetailsObject) this.listcourse.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            TextView textviewlecturetime = (TextView) view.findViewById(R.id.textviewlecturetime);
            if (position % 2 == 0) {
                drawable = TextDrawable.builder().buildRound(String.valueOf(courseDetailsObject.getCourseName().charAt(0)), SupportMenu.CATEGORY_MASK);
            } else {
                drawable = TextDrawable.builder().buildRound(String.valueOf(courseDetailsObject.getCourseName().charAt(0)), -16776961);
            }
            textviewlecturetime.setText(courseDetailsObject.getCourseName());
            imageView.setImageDrawable(drawable);
            return view;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Course List");
        View view = inflater.inflate(R.layout.activity_course_list, container, false);
        this.listviewCourse = (ListView) view.findViewById(R.id.course_list);
        this.listviewCourse.setOnItemClickListener(new C07251());
        this.listCourseDetailsObject = new CourseDetailTable(getActivity()).getAllCourses();
        this.listviewCourse.setAdapter(new CustomListAdapter(this.listCourseDetailsObject));
        return view;
    }
}
