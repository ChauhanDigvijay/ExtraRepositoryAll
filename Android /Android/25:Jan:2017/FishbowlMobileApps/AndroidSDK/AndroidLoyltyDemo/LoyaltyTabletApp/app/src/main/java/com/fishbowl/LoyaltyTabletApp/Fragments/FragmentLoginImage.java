package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.graphics.Color;
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
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;

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
        final String loginLeftSideImageUrl = FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideImageUrl");
        //loginleft image/color
        if(StringUtilities.isValidString(loginLeftSideImageUrl)) {
            final String url = "http://" + loginLeftSideImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(leftsideimage, R.color.white, R.color.white));
            leftsideimage.setImageUrl(url, mImageLoader);
        }
         else if(FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideColor")!=null)
        {
            final String loginLeftSideColor =  FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideColor");
            if(StringUtilities.isValidString(loginLeftSideColor))
                leftsideimage.setBackgroundColor(Color.parseColor(loginLeftSideColor));
        }

    }

    public void newView() {
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String loginLeftSideImageUrl =  FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideImageUrl");
       if(StringUtilities.isValidString(loginLeftSideImageUrl)){
        mImageLoader.get(loginLeftSideImageUrl, ImageLoader.getImageListener(leftsideimage, R.color.white, R.color.white));
        leftsideimage.setImageUrl(loginLeftSideImageUrl, mImageLoader);
    }
    else if(FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideColor")!=null)
        {
            final String loginLeftSideColor =  FBThemeMobileSettingsService.sharedInstance().loginmapsetting.get("LoginLeftSideColor");
            if(StringUtilities.isValidString(loginLeftSideColor))
            leftsideimage.setBackgroundColor(Color.parseColor(loginLeftSideColor));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }
}
