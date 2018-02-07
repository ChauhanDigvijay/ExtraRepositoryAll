package com.BasicApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.basicmodule.sdk.R;


public class EmailRewardModelActivity extends Activity {
    String htmlbody;
    WebView webView;
    String mimeType = "text/html";
    String encoding = "utf-8";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailRewardModelActivity.this.finish();
            }
        });
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null) {

            htmlbody = extras.getString("htmlbody");

        }

        webView = (WebView) findViewById(R.id.webview);
        webView.loadData(htmlbody, mimeType, encoding);

    }
}