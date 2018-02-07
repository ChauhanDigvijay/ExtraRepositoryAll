package com.BasicApp.Activites.NonGeneric.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 20-Dec-16.
 */

public class ProgramDetailActvity extends FragmentActivity implements View.OnClickListener{
    Button log;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage,titlebackground;
    TextView title,title_welcome;
    RelativeLayout toolbar,toolbar2;
    NetworkImageView backgroundImage;
    LinearLayout profile_way;
    LinearLayout layout_button,layout_logout;
    WebView webView;
    ImageView backbutton;
    final String url5 =  "http://" + FBViewMobileSettingsService.sharedInstance().programDetail;;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView=(WebView)findViewById(R.id.termsconditionsTextView);

        if(FBViewMobileSettingsService.sharedInstance().programDetail!=null)
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


    }
    @Override
    public void onStart()
    {
        super.onStart();

    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
