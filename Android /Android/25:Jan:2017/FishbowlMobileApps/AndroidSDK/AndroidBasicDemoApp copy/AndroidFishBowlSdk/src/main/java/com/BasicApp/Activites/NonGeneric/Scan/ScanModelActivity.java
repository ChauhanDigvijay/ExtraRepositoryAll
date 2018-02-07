package com.BasicApp.Activites.NonGeneric.Scan;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBMember;
import com.google.gson.Gson;

/**
 * Created by digvijay(dj)
 */
public class ScanModelActivity extends BaseActivity {

    TextView loyaltyname, loyaltyno;
    WebView webView;

    FBMember member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscan);

        setUpToolBar(true, true);
        setTitle("Scan");
        setBackButton(false, false);
   //     enableScreen(false);

        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(ScanModelActivity.this).mSharedPreferences.getString("FBUser", "");
        member = gson.fromJson(json, FBMember.class);

        loyaltyname = (TextView) findViewById(R.id.loyaltyname);
        loyaltyno = (TextView) findViewById(R.id.loyaltyno);
        webView = (WebView) findViewById(R.id.bar_image);


        if (member != null) {
            loyaltyname.setText(member.getFirstName());
            loyaltyno.setText(member.getLoyaltyno());

            final String url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" +member.getLoyaltyno() + "&BarCodeType=QRCode&dpi=100&showText=false";
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                   ;
                    view.loadUrl(url5);
                    return true;
                }
            });


            webView.loadUrl(url5);
         //   enableScreen(true);

        }
    }

}
