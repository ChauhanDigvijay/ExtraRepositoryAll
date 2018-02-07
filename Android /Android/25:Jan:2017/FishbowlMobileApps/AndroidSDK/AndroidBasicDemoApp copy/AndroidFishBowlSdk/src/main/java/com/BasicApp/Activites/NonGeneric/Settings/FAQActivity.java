package com.BasicApp.Activites.NonGeneric.Settings;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

/**
 * Created by digvijay(dj)
 */

public class FAQActivity extends BaseActivity {
    final String faq = "http://" + FBViewMobileSettingsService.sharedInstance().faq;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        webView = (WebView) findViewById(R.id.termsconditionsTextView);

        if (FBViewMobileSettingsService.sharedInstance().faq != null)
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(faq);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(faq);
        webView.getSettings().setJavaScriptEnabled(true);
        setUpToolBar(true,true);
        setTitle("FAQs");
        setBackButton(true,false);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
