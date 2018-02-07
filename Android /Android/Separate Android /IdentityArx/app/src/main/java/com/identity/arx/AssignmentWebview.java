package com.identity.arx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.identity.arx.general.MasterActivity;


public class AssignmentWebview extends MasterActivity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;
    String urlPath;

    class C07261 extends WebViewClient {
        C07261() {
        }

        public void onPageFinished(WebView view, String url) {
            Toast.makeText(AssignmentWebview.this, "Finished loading", 0).show();
        }
    }

    class C07272 extends WebChromeClient {
        C07272() {
        }

        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Toast.makeText(AssignmentWebview.this, title, 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_assignment_webview);
        this.urlPath = getIntent().getStringExtra("url");
        this.mWebView = (AdvancedWebView) findViewById(R.id.assignment_webview);
        this.mWebView.setListener((Activity) this, this);
        this.mWebView.setMixedContentAllowed(true);
        this.mWebView.setCookiesEnabled(true);
        this.mWebView.setThirdPartyCookiesEnabled(true);
        this.mWebView.setWebViewClient(new C07261());
        this.mWebView.setWebChromeClient(new C07272());
        this.mWebView.addHttpHeader("X-Requested-With", "");
        this.mWebView.loadUrl(this.urlPath);
    }

    @SuppressLint({"NewApi"})
    protected void onResume() {
        super.onResume();
        this.mWebView.onResume();
    }

    @SuppressLint({"NewApi"})
    protected void onPause() {
        this.mWebView.onPause();
        super.onPause();
    }

    protected void onDestroy() {
        this.mWebView.onDestroy();
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        this.mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    public void onBackPressed() {
        if (this.mWebView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onPageStarted(String url, Bitmap favicon) {
        this.mWebView.setVisibility(4);
    }

    public void onPageFinished(String url) {
        this.mWebView.setVisibility(0);
    }

    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(this, "onPageError(errorCode = " + errorCode + ",  description = " + description + ",  failingUrl = " + failingUrl + ")", 0).show();
    }

    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Toast.makeText(this, "onDownloadRequested(url = " + url + ",  suggestedFilename = " + suggestedFilename + ",  mimeType = " + mimeType + ",  contentLength = " + contentLength + ",  contentDisposition = " + contentDisposition + ",  userAgent = " + userAgent + ")", 1).show();
    }

    public void onExternalPageRequest(String url) {
        Toast.makeText(this, "onExternalPageRequest(url = " + url + ")", 0).show();
    }
}
