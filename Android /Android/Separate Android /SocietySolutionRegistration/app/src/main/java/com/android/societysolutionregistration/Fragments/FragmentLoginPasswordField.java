package com.android.societysolutionregistration.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.CustomVolleyRequestQueue;
import com.android.societysolutionregistration.Utils.FBUtils;
import com.android.societysolutionregistration.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class FragmentLoginPasswordField extends Fragment implements View.OnClickListener {
    Button bCheckin;
    EditText et, pass;
    TextView txt_signup;
    ProgressBarHandler progressBarHandler;
    String fbm;
    Timer myTimer;
    RelativeLayout button_parent;
    String date;
    private NetworkImageView background;
    private ImageLoader mImageLoader;
    private NetworkImageView footer_imageurl;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_password_field, container, false);


        bCheckin = (Button) v.findViewById(R.id.bt_checkIn);
        background = (NetworkImageView) v.findViewById(R.id.right_side_image_url);
        footer_imageurl = (NetworkImageView) v.findViewById(R.id.footer_image_url);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        progressBarHandler = new ProgressBarHandler(getActivity());
        et = (EditText) v.findViewById(R.id.target);
        pass = (EditText) v.findViewById(R.id.password);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        et.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {
                et.setSelection(et.getText().length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    char c = s.charAt(0);

                    if (c >= '0' && c <= '9') {
                        FBUtils.setUsDashPhone(s, before, et);
                    }
                }
            }

        });

        bCheckin.setOnClickListener(this);
        txt_signup = (TextView) v.findViewById(R.id.txt_signup);
        txt_signup.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.bt_checkIn) {
            if (checkValidation()) {
                loginMember();

            }
        }
        if (v.getId() == R.id.txt_signup) {


        }

    }







    public void loginMember() {
        JSONObject object = new JSONObject();
        progressBarHandler.show();
        String email = et.getText().toString();
        String password = pass.getText().toString();

        Date d = Calendar.getInstance().getTime();

        String format = null;
        if (format == null)
            format = "yyyy-MM-dd'T'hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        try {
            object.put("username", email);
            object.put("password", password);
            object.put("eventDateTime", currentData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public boolean checkValidation() {
        if (!(FBUtils.isValidString(et.getText().toString()) || FBUtils.isValidPhoneNumber(et.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty Phone number");
            return false;
        }


        if (!FBUtils.isValidString(pass.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Empty Password");
            return false;
        }

        return true;
    }


}
