package com.fishbowl.LoyaltyTabletApp.Activites.Generic;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.TransitionManager;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.Logger;

import static com.fishbowl.loyaltymodule.Utils.FBConstant.B_TITLE;
import static com.fishbowl.loyaltymodule.Utils.FBConstant.B_URL;


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
            final String serverUrl = bundle.getString(FBConstant.B_URL);
            final String title = bundle.getString(FBConstant.B_TITLE);
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
                    Logger.i("event" + event);
                    return true;
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    Logger.i("URL" + url);
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
