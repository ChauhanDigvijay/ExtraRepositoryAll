package com.BasicApp.Activites.NonGeneric.Settings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

/**
 * Created by schaudhary_ic on 30-May-16.
 */
public class TermsConditionActivity extends Activity {

    WebView webView;
    RelativeLayout mtoolbar;
    private NetworkImageView loginBackground,headerImage,imlogo;
    private ImageLoader mImageLoader;
    final String url5 = "http://" + FBViewMobileSettingsService.sharedInstance().termsAndConditions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        imlogo = (NetworkImageView) findViewById(R.id.iv_logo);
        webView=(WebView)findViewById(R.id.termsconditionsTextView);
        //    headerImage= (NetworkImageView) findViewById(R.id.headerImage);


        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });

        loginBackground=(NetworkImageView)findViewById(R.id.network_background);

        if(FBViewMobileSettingsService.sharedInstance().termsAndConditions!=null)
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {


                    view.loadUrl(url5);
                    return true;
                }});
        webView.setBackgroundColor(Color.TRANSPARENT);

        webView.loadUrl(url5);


    }
    public void onCustomBackPressed() {
        TermsConditionActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FBViewMobileSettingsService.sharedInstance().checkInButtonColor!=null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getImageLoader();

            //Image URL - This can point to any image file supported by Android
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
//            mImageLoader.get(url, ImageLoader.getImageListener(loginBackground, R.drawable.bgimage, android.R.drawable
//                    .ic_dialog_alert));
      //      loginBackground.setImageUrl(url, mImageLoader);
           /* final String url4 = "http://" + FBViewMobileSettingsService.sharedInstance().loginHeaderImageUrl;

            mImageLoader.get(url4, ImageLoader.getImageListener(headerImage, R.drawable.mybrestoofferimage, android.R.drawable
                    .ic_dialog_alert));

            headerImage.setImageUrl(url4, mImageLoader);*/
            final String url2 = "http://" + FBViewMobileSettingsService.sharedInstance().companyLogoImageUrl;
//            mImageLoader.get(url2, ImageLoader.getImageListener(imlogo, R.drawable.logo, android.R.drawable
//                    .ic_dialog_alert));
//
//            imlogo.setImageUrl(url2, mImageLoader);


        }

    }

  
}
