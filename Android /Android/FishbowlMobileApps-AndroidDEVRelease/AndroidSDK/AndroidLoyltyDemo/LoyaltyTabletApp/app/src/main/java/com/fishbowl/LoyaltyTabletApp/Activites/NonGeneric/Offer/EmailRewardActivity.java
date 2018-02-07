package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.fishbowl.LoyaltyTabletApp.R;

public class EmailRewardActivity extends Activity
{
    String htmlbody;
    WebView webView;
    private Toolbar toolbar;
    String mimeType = "text/html";
    String encoding = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EmailRewardActivity.this.finish();
            }
        });
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