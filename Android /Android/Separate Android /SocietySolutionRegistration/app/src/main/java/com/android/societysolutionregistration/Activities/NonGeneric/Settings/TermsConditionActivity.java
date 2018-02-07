package com.android.societysolutionregistration.Activities.NonGeneric.Settings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.societysolutionregistration.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


/**
 * Created by schaudhary_ic on 30-May-16.
 */
public class TermsConditionActivity extends Activity {

    final String url5 =null;
    WebView webView;
    RelativeLayout mtoolbar;
    TextView toolbar_title;
    private NetworkImageView loginBackground, headerImage, imlogo;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        webView = (WebView) findViewById(R.id.termsconditionsTextView);

        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) mtoolbar.findViewById(R.id.title_text);
        toolbar_title.setText("Term & Condition");
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });

        loginBackground = (NetworkImageView) findViewById(R.id.network_background);

        if (url5 != null)
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url5);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(url5);
    }

    public void onCustomBackPressed() {
        TermsConditionActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
