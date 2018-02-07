package com.olo.jambajuice.Activites.Generic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.olo.jambajuice.Activites.NonGeneric.Settings.PushNotificationActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.wearehathway.apps.olo.Utils.Logger;

import static com.olo.jambajuice.Utils.Constants.B_TITLE;
import static com.olo.jambajuice.Utils.Constants.B_URL;

public class WebViewActivity extends BaseActivity implements View.OnClickListener
{
    public static void show(Activity activity, String title, String url)
    {
        Bundle bundle = new Bundle();
        bundle.putString(B_URL, url);
        bundle.putString(B_TITLE, title);
        TransitionManager.transitFrom(activity, WebViewActivity.class, bundle);
    }

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setUpToolBar(true);
        setBackButton(true,false);
        SemiBoldButton defaultLaunchButton = (SemiBoldButton) findViewById(R.id.defaultLaunch);
        defaultLaunchButton.setOnClickListener(this);
        Uri url = getIntent().getData();
        Bundle bundle = getIntent().getExtras();
        if(PushNotificationActivity.pushNotificationActivity != null){
            PushNotificationActivity.pushNotificationActivity.finish();
        }
        if(bundle != null || url != null)
        {
            String serverUrl = null;
            String title = null;
            if(bundle != null) {
                serverUrl = bundle.getString(Constants.B_URL);
                title = bundle.getString(Constants.B_TITLE);
            }
            if(serverUrl == null){
                if(url != null) {
                    serverUrl = url.toString();
                }
            }
            mUrl = serverUrl;
            if(title == null){
                setTitle("Mobile Web");
            }else {
                setTitle(title);
            }

            final WebView webview = (WebView) findViewById(R.id.webView);
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

            final String finalTitle = title;
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
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url)
                {
                    progressBar.setVisibility(View.GONE);
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
                {
                    Utils.showAlert(WebViewActivity.this, "Unable to load " + finalTitle, "Error");
                }
            });
            webview.loadUrl(serverUrl);
        }
    }

    private void openInDefaultBrowser(){
        getIntent().setAction("");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        startActivity(browserIntent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.defaultLaunch:
                openInDefaultBrowser();
        }
    }
}
