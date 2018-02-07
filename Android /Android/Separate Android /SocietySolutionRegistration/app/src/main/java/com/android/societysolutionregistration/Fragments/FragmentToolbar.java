package com.android.societysolutionregistration.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.societysolutionregistration.Activities.NonGeneric.Authentication.SignIn.SignInActivity;
import com.android.societysolutionregistration.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 16-Nov-16.
 */

public class FragmentToolbar extends Fragment implements View.OnClickListener {
    NetworkImageView titleimage, img_Back, img_Background;
    TextView title, title_welcome;
    LinearLayout profile_way, layout_menu, layout_logout;
    private ImageLoader mImageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.toolbar_all, container, false);
        titleimage = (NetworkImageView) v.findViewById(R.id.backbutton);
        img_Back = (NetworkImageView) v.findViewById(R.id.img_Back);
        title_welcome = (TextView) v.findViewById(R.id.title_welcome);
        profile_way = (LinearLayout) v.findViewById(R.id.profile_way);
        profile_way.setOnClickListener(this);
        title = (TextView) v.findViewById(R.id.text_name);
        layout_logout = (LinearLayout) v.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(this);
        layout_menu = (LinearLayout) v.findViewById(R.id.layout_menu);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();




    }

    public void onCustomResume() {



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_logout:
                logout();
                break;
            case R.id.profile_way:
                getActivity().finish();
                break;
        }

    }


    public void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Logout ");
        alertDialog.setMessage("Press Ok to Logout ");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put("Application", "mobile");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle extras = new Bundle();
                Intent i = new Intent(getContext(), SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(extras);
                startActivity(i);
                getActivity().finish();
            }
        });
        alertDialog.show();
    }
}
