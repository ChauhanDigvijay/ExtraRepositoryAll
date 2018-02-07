package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;

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

        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().loginLeftSideImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(leftsideimage, R.color.white, R.color.white));
        leftsideimage.setImageUrl(url, mImageLoader);
    }

    public void newView() {
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().loginLeftSideImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(leftsideimage, R.color.white, R.color.white));
        leftsideimage.setImageUrl(url, mImageLoader);
    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }
}
