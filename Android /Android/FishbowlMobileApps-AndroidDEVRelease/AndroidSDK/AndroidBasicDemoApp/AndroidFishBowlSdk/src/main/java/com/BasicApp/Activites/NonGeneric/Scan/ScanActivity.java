package com.BasicApp.Activites.NonGeneric.Scan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBUserService;

public class ScanActivity extends Activity {

    private android.support.v7.widget.Toolbar toolbar;
    TextView loyaltyname,loyaltyno,loyaltyno1;
    WebView webView;
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscan);

        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanActivity.this.finish();
            }
        });
        Member member = FBUserService.sharedInstance().member;
        loyaltyname = (TextView) findViewById(R.id.loyaltyname);
        loyaltyno = (TextView) findViewById(R.id.loyaltyno);
      //  loyaltyno1 = (TextView) findViewById(R.id.loyaltyno1);
        webView = (WebView) findViewById(R.id.bar_image);



        loyaltyname.setText(member.firstName );
        loyaltyno.setText(member.loyalityNo);
        //loyaltyno1.setText(member.loyalityNo);

        String  loyalityNo=  member.loyalityNo;
     final String   url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FBUserService.sharedInstance().member.loyalityNo + "&BarCodeType=QRCode&dpi=100&showText=false";
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url5);
                return true;
            }
        });

        //webView.setBackgroundColor(Color.WHITE);
        webView.loadUrl(url5);

    }
    public void onCustomBackPressed() {
        ScanActivity.this.finish();
    }

}
