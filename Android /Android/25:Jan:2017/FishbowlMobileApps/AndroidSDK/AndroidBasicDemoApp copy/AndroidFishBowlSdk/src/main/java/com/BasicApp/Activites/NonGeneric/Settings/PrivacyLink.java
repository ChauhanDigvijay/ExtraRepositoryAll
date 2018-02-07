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
public class PrivacyLink extends BaseActivity {

    final String privacyContent = "http://" + FBViewMobileSettingsService.sharedInstance().privacyContent;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_link);
        webView = (WebView) findViewById(R.id.privacyPolicyTextView);

        setUpToolBar(true, true);
        setTitle("Privacy");
        setBackButton(false, false);

        if (FBViewMobileSettingsService.sharedInstance().privacyContent != null)

            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(privacyContent);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(privacyContent);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}

