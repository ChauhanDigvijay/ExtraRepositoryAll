package com.BasicApp.Activites.NonGeneric.Settings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

/**
 * Created by schaudhary_ic on 30-Nov-16.
 */

public class FAQActivity extends Activity {
    WebView webView;
    final String url5 =  "http://" + FBViewMobileSettingsService.sharedInstance().faq;;
    private ImageLoader mImageLoader;
    RelativeLayout toolbar;
    ImageView backbutton,menu_navigator;
    TextView toolbarTittle;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        webView=(WebView)findViewById(R.id.termsconditionsTextView);

        if(FBViewMobileSettingsService.sharedInstance().faq!=null)
            webView.setWebViewClient(new WebViewClient()
            {
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    view.loadUrl(url5);
                    return true;
                }
            });
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(url5);
        webView.getSettings().setJavaScriptEnabled(true);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbarTittle = (TextView) findViewById(R.id.title_text);
        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawer(FAQActivity.this);
        menu_navigator = (ImageView) toolbar.findViewById(R.id.menu_navigator);
        menu_navigator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        String buttoncolor=FBViewMobileSettingsService.sharedInstance().checkInButtonColor;
        if(buttoncolor!=null)
        {
        //    toolbarTittle.setTextColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
        }
    }
}
