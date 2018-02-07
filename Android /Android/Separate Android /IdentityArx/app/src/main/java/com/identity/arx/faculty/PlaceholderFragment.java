package com.identity.arx.faculty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.general.TimeTrap;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.textdrawable.TextDrawable;

import java.util.List;


public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    CourseDetailTable courseDetailTable;
    ListView listView;

    private class CustomListAdapter extends ArrayAdapter<LectureScheduleObject> {
        List<LectureScheduleObject> listSchedule;

        public CustomListAdapter(List<LectureScheduleObject> listSchedule) {
            super(PlaceholderFragment.this.getActivity(), R.layout.schedule_lecture_list_item, listSchedule);
            this.listSchedule = listSchedule;
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            TextDrawable drawable;
            View view = PlaceholderFragment.this.getActivity().getLayoutInflater().inflate(R.layout.schedule_lecture_list_item, parent, false);
            LectureScheduleObject lectureScheduleObject = (LectureScheduleObject) this.listSchedule.get(position);
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
            CourseDetailsObject courseDetailsObject = PlaceholderFragment.this.courseDetailTable.getCourseOverId(lectureScheduleObject.getCourseId() + "");
            try {
                if (courseDetailsObject.getAssignFaculty().equalsIgnoreCase("known")) {
                    textview_course_name.setText(courseDetailsObject.getCourseName() + "");
                } else {
                    textview_course_name.setText(courseDetailsObject.getCourseName() + " (" + courseDetailsObject.getAssignFaculty() + ")");
                }
            } catch (NullPointerException e) {
            }
            textviewlecturelocation.setText(lectureScheduleObject.getLectureLocation());
            textviewduration.setText(TimeTrap.getTimeDifference(lectureScheduleObject.getLectureStartTime(), lectureScheduleObject.getLectureEndTime()) + " min");
            imageView.setImageDrawable(drawable);
            return view;
        }
    }

    public static PlaceholderFragment newInstance(String day) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, day);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_institute, container, false);
        this.listView = (ListView) view.findViewById(R.id.institute_list);
        LectureDetailTable lectureDetailTable = new LectureDetailTable(getActivity());
        this.courseDetailTable = new CourseDetailTable(getActivity());
        this.listView.setAdapter(new CustomListAdapter(lectureDetailTable.getLectureDetails(getArguments().getString(ARG_SECTION_NUMBER))));
        return view;
    }
}
