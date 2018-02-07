package com.identity.arx.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.db.UserDetailTable;
import com.identity.arx.objectclass.LoginResponseObject;

public class StudentProfileFragment extends Fragment {
    TextView aadhaar;
    TextView contact;
    TextView deptname;
    TextView dob;
    TextView email;
    TextView name;
    TextView programme;
    TextView pursuing_year;
    TextView sem;
    TextView semester;
    TextView userid;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.activity_student_profile, container, false);
        CollapsingToolbarLayout name_title = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        this.deptname = (TextView) view.findViewById(R.id.student_department);
        this.programme = (TextView) view.findViewById(R.id.programme);
        this.contact = (TextView) view.findViewById(R.id.student_contact);
        this.pursuing_year = (TextView) view.findViewById(R.id.pursuingYear);
        this.email = (TextView) view.findViewById(R.id.student_emailId);
        this.userid = (TextView) view.findViewById(R.id.student_userId);
        this.semester = (TextView) view.findViewById(R.id.semester);
        this.dob = (TextView) view.findViewById(R.id.dob);
        this.aadhaar = (TextView) view.findViewById(R.id.student_aadhar);
        LoginResponseObject loginResponseObject = new UserDetailTable(getContext()).getUserDetails();
        name_title.setTitle(loginResponseObject.getName());
        this.deptname.setText(loginResponseObject.getDeptName());
        this.programme.setText(loginResponseObject.getProgramName());
        this.contact.setText(loginResponseObject.getContact());
        this.pursuing_year.setText(loginResponseObject.getPersuingYear());
        this.dob.setText(loginResponseObject.getDob());
        this.aadhaar.setText(loginResponseObject.getAdhaarNum());
        this.email.setText(loginResponseObject.getEmail());
        this.userid.setText(loginResponseObject.getRollnum());
        this.semester.setText(loginResponseObject.getSemYear());
        return view;
    }
}
