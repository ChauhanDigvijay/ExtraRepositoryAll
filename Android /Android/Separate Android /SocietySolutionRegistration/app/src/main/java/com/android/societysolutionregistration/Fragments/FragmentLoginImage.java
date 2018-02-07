package com.android.societysolutionregistration.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class FragmentLoginImage extends Fragment {
    NetworkImageView leftsideimage;
    ProgressBarHandler progressBarHandler;
    View v;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login_image, container, false);
        leftsideimage = (NetworkImageView) v.findViewById(R.id.left_side_image_url);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }
}
