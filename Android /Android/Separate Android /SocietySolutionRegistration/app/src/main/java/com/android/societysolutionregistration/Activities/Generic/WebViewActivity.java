package com.android.societysolutionregistration.Activities.Generic;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.Constants;
import com.android.societysolutionregistration.Utils.FBUtils;
import com.android.societysolutionregistration.Utils.TransitionManager;

import static com.android.societysolutionregistration.Utils.Constants.B_TITLE;
import static com.android.societysolutionregistration.Utils.Constants.B_URL;


public class WebViewActivity extends BaseActivity
{
    public static void show(Activity activity, String title, String url)
    {
        Bundle bundle = new Bundle();
        bundle.putString(B_URL, url);
        bundle.putString(B_TITLE, title);
       TransitionManager.transitFrom(activity, WebViewActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_common);
        setUpToolBar(true);
        //setBackButton(true,false);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            final String serverUrl = bundle.getString(Constants.B_URL);
            final String title = bundle.getString(Constants.B_TITLE);
          //  setTitle(title);

            final WebView webview = (WebView) findViewById(R.id.webView);
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

            webview.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
                {

                    return true;
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {

                    return true;
                }

                public void onPageFinished(WebView view, String url)
                {
                    progressBar.setVisibility(View.GONE);
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
                {
                    FBUtils.showAlert(WebViewActivity.this, "Unable to load " + title, "Error");
                }
            });
            webview.loadUrl(serverUrl);
        }
    }
}
