package com.BasicApp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

/**
 * Created by schaudhary_ic on 23-May-16.
 */
@SuppressLint("ValidFragment")
public class Image_Fragment extends Fragment {
    private final int imageResourceId;
    private ImageLoader mImageLoader;



    public Image_Fragment(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getActivity())
                .getImageLoader();
       // pd = new TransparentProgressDialog(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newview, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_viewPager);

        imageView.setImageResource(imageResourceId);



        return view;
    }
}