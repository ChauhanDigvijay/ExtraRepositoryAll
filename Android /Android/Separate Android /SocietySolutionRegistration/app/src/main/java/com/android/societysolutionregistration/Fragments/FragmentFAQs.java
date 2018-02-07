package com.android.societysolutionregistration.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;


/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class FragmentFAQs extends Fragment {
    final String url5 = null;
    WebView webView;
    ProgressBarHandler progressBarHandler;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_faqs, container, false);
        webView = (WebView) v.findViewById(R.id.termsconditionsTextView);
        progressBarHandler = new ProgressBarHandler(getActivity());
        // progressBarHandler.show();

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return true;

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    {
                        progressBarHandler.dismiss();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    progressBarHandler.dismiss();
                }
            });
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.loadUrl(url5);



        webView.getSettings().setJavaScriptEnabled(true);

        return v;
    }
}
