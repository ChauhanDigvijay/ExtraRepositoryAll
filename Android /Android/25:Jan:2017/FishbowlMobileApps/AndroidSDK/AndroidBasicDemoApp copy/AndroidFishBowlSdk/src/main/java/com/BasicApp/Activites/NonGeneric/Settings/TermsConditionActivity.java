package com.BasicApp.Activites.NonGeneric.Settings;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

/**
 * Created by digvijay(dj)
 */
public class TermsConditionActivity extends BaseActivity {

    final String termsAndConditions = "http://" + FBViewMobileSettingsService.sharedInstance().termsAndConditions;
    WebView webView;
    RelativeLayout mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        webView = (WebView) findViewById(R.id.termsconditionsTextView);
        if (FBViewMobileSettingsService.sharedInstance().termsAndConditions != null)
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {


                    view.loadUrl(termsAndConditions);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(termsAndConditions);

        setUpToolBar(true,true);
        setTitle("Term & Condition");
        setBackButton(false,false);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


}
