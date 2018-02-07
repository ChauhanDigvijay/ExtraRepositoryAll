package com.olo.jambajuice.Activites.NonGeneric.Settings;


import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SanSerifTextView;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;


public class PopUpDisableGiftActivity extends BaseActivity implements View.OnClickListener {

    public static String e = "";
    public static PopUpDisableGiftActivity popUpDisableGiftActivity;

    private RelativeLayout pushNotificationMainLayout;
    private LinearLayout pushNotiLayout;
    private ScrollView scrollViewLayout;
    private RelativeLayout jambaLogoLayout, newOfferTextLayout, horLineLayout, bottomLayout;


    private String urlLinks= "https://commentform.marketforce.com/commentform/JambaJuice.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_disable_gift);
        popUpDisableGiftActivity = this;
        setUpToolBar(true, true);

        pushNotificationMainLayout = (RelativeLayout) findViewById(R.id.pushNotificationMainLayout);
        jambaLogoLayout = (RelativeLayout) findViewById(R.id.jambaLogoLayout);
        newOfferTextLayout = (RelativeLayout) findViewById(R.id.newOfferTextLayout);
        horLineLayout = (RelativeLayout) findViewById(R.id.horLineLayout);

        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);

        pushNotiLayout = (LinearLayout) findViewById(R.id.pushNotiLayout);

        scrollViewLayout = (ScrollView) findViewById(R.id.scrollViewLayout);

        SemiBoldButton viewofferButton = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        viewofferButton.setOnClickListener(this);


        ImageView imgHeaderMapIcon1 = (ImageView) findViewById(R.id.imgHeaderMapIcon1);
        imgHeaderMapIcon1.setOnClickListener(this);


        ViewTreeObserver vto = pushNotificationMainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    pushNotificationMainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    pushNotificationMainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                double pushNotificationMainLayoutWidth = pushNotificationMainLayout.getWidth();
                double pushNotificationMainLayoutheight = pushNotificationMainLayout.getHeight();

                double ratio = pushNotificationMainLayoutheight / pushNotificationMainLayoutWidth;

                int pushNotiLayoutWidth = (int) (pushNotificationMainLayoutWidth * (0.8));
                int pushNotiLayoutHeight = (int) (pushNotiLayoutWidth * ratio);

                RelativeLayout.LayoutParams todayButtonParams = (RelativeLayout.LayoutParams) pushNotiLayout.getLayoutParams();
                todayButtonParams.width = pushNotiLayoutWidth;
                todayButtonParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                pushNotiLayout.setLayoutParams(todayButtonParams);

                LinearLayout.LayoutParams jambaLogoViewParams = (LinearLayout.LayoutParams) jambaLogoLayout.getLayoutParams();
                jambaLogoViewParams.height = (int) (pushNotiLayoutHeight * (0.2));
                jambaLogoLayout.setLayoutParams(jambaLogoViewParams);

                LinearLayout.LayoutParams newOfferTextViewParams = (LinearLayout.LayoutParams) newOfferTextLayout.getLayoutParams();
                newOfferTextViewParams.height = (int) (pushNotiLayoutHeight * (0.08));
                newOfferTextLayout.setLayoutParams(newOfferTextViewParams);

                LinearLayout.LayoutParams scrollViewParams = (LinearLayout.LayoutParams) scrollViewLayout.getLayoutParams();
                scrollViewParams.height = (int) (pushNotiLayoutHeight * (0.5));
                scrollViewLayout.setLayoutParams(scrollViewParams);

                LinearLayout.LayoutParams horLineLayoutViewParams = (LinearLayout.LayoutParams) horLineLayout.getLayoutParams();
                horLineLayoutViewParams.height = (int) (pushNotiLayoutHeight * (0.05));
                horLineLayout.setLayoutParams(horLineLayoutViewParams);


                LinearLayout.LayoutParams bottomLayoutViewParams = (LinearLayout.LayoutParams) bottomLayout.getLayoutParams();
                bottomLayoutViewParams.height = (int) (pushNotiLayoutHeight * (0.1));
                bottomLayout.setLayoutParams(bottomLayoutViewParams);


            }
        });
        setOfferData();

    }

    private void setOfferData() {


        ImageButton imgHeaderMapIcon1 = (ImageButton) findViewById(R.id.imgHeaderMapIcon1);
        SemiBoldButton viewOfferBtn = (SemiBoldButton) findViewById(R.id.viewOfferBtn);

        RelativeLayout horLineLayout = (RelativeLayout) findViewById(R.id.horLineLayout);


        com.olo.jambajuice.Views.Generic.SanSerifTextView headerOfferTitle = (SanSerifTextView) findViewById(R.id.popup_body);




        ClickableSpan termsOfServicesClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                WebViewActivity.show(PopUpDisableGiftActivity.this, "Mobile Web", urlLinks);
            }
        };

        ClickableSpan privacyPolicyClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Utils.showDialerConfirmation(PopUpDisableGiftActivity.this, "1-866-473-7848");
            }
        };

        makeLinks(headerOfferTitle, new String[] { "here", "(1-866-473-7848)" }, new ClickableSpan[] {
                termsOfServicesClick, privacyPolicyClick
        });


    }




    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHeaderMapIcon1:
                onBackPressed();
                break;

            case R.id.viewOfferBtn:
                onBackPressed();
                break;

        }
    }
}
