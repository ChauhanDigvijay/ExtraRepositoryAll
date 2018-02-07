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
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;


public class FragmentDashboardImage extends Fragment {
    private NetworkImageView leftsideimage;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_image, container, false);
        leftsideimage = (NetworkImageView) v.findViewById(R.id.left_side_image_url);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String url = "http://" + FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginHeaderImageUrl");
        mImageLoader.get(url, ImageLoader.getImageListener(leftsideimage, R.color.white, R.color.white));
        leftsideimage.setImageUrl(url, mImageLoader);
    }
}
