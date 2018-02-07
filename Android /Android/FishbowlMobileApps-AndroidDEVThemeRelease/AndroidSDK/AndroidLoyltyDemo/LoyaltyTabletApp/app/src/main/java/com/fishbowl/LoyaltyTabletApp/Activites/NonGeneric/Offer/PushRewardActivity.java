package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.fishbowl.LoyaltyTabletApp.R;

public class PushRewardActivity extends Activity
{
    String htmlbody;
    WebView webView;
    String mimeType = "text/html";
    String encoding = "utf-8";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_reward);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null)
        {
            htmlbody = extras.getString("htmlbody");
        }
        webView = (WebView)findViewById(R.id.webview);
        webView.loadData(htmlbody, mimeType, encoding);
    }
}
