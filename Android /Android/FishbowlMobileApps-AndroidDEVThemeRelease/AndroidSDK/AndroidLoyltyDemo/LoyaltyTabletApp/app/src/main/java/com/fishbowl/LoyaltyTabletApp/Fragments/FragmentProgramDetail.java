package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.toolbox.ImageLoader;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;


/**
 * Created by schaudhary_ic on 05-Dec-16.
 */

public class FragmentProgramDetail extends Fragment {
    final String url5 = "http://" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("ProgramDetail");
    WebView webView;
    ;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_program_deatil, container, false);
        webView = (WebView) v.findViewById(R.id.termsconditionsTextView);

        if (url5 != null)
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url5);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(url5);
        webView.getSettings().setJavaScriptEnabled(true);

        return v;
    }
}
