package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.MyRewardsAndOfferActivity;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.HelveticaTextView;
import com.olo.jambajuice.Views.Generic.MediumTextView;
import com.olo.jambajuice.Views.Generic.NexaRustSansBlackTextView;
import com.olo.jambajuice.Views.Generic.SanSerifTextView;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

public class PushNotificationActivity extends BaseActivity implements View.OnClickListener {

    public static String e = "";
    public static PushNotificationActivity pushNotificationActivity;
    Bundle extras;
    private String offerId;
    private String custId;
    private String promoCode;
    private RelativeLayout pushNotificationMainLayout;
    private LinearLayout pushNotiLayout;
    private ScrollView scrollViewLayout;
    private RelativeLayout jambaLogoLayout, newOfferTextLayout, horLineLayout, infoLayout, bottomLayout;
    private String[] urlLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);

        pushNotificationActivity = this;
        setUpToolBar(true, true);
        isShowBasketIcon = false;

        Intent i = getIntent();
        extras = i.getExtras();
        {
            if (extras != null) {

                this.e = extras.getString("Title");
                urlLinks = Utils.extractLinks(e);
                this.offerId = extras.getString("offerId");
                this.custId = extras.getString("custId");
                this.promoCode = extras.getString("promoCode");

            }
        }

        String htmlText = " %s ";
        //String myData = "Your offer is available in the Rewards and Promotion section, and can be redeemed when you checkout";
        String myData = "Your offer is available in the Rewards & Offers section. Offers may be redeemed in-store or online. Some restrictions apply.";
        WebView webView = (WebView) findViewById(R.id.infoText);
        webView.loadData(String.format(htmlText, myData), "text/html", "utf-8");
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFixedFontSize(6);

        pushNotificationMainLayout = (RelativeLayout) findViewById(R.id.pushNotificationMainLayout);
        jambaLogoLayout = (RelativeLayout) findViewById(R.id.jambaLogoLayout);
        newOfferTextLayout = (RelativeLayout) findViewById(R.id.newOfferTextLayout);
        horLineLayout = (RelativeLayout) findViewById(R.id.horLineLayout);
        infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);

        pushNotiLayout = (LinearLayout) findViewById(R.id.pushNotiLayout);

        scrollViewLayout = (ScrollView) findViewById(R.id.scrollViewLayout);

        SemiBoldButton viewofferButton = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        viewofferButton.setOnClickListener(this);
        SemiBoldButton dismissButton = (SemiBoldButton) findViewById(R.id.dismissBtn);
        dismissButton.setOnClickListener(this);


        ImageView imgHeaderMapIcon1 = (ImageView) findViewById(R.id.imgHeaderMapIcon1);
        imgHeaderMapIcon1.setOnClickListener(this);

        if (StringUtilities.isValidString(e)) {
            if (StringUtilities.isValidString(custId)) {
                setOfferData();
            }
        }

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
                scrollViewParams.height = (int) (pushNotiLayoutHeight * (0.3));
                scrollViewLayout.setLayoutParams(scrollViewParams);

                LinearLayout.LayoutParams horLineLayoutViewParams = (LinearLayout.LayoutParams) horLineLayout.getLayoutParams();
                horLineLayoutViewParams.height = (int) (pushNotiLayoutHeight * (0.05));
                horLineLayout.setLayoutParams(horLineLayoutViewParams);


                LinearLayout.LayoutParams infoLayoutViewParams = (LinearLayout.LayoutParams) infoLayout.getLayoutParams();
                infoLayoutViewParams.height = (int) (pushNotiLayoutHeight * (0.2));
                infoLayout.setLayoutParams(infoLayoutViewParams);

                LinearLayout.LayoutParams bottomLayoutViewParams = (LinearLayout.LayoutParams) bottomLayout.getLayoutParams();
                bottomLayoutViewParams.height = (int) (pushNotiLayoutHeight * (0.1));
                bottomLayout.setLayoutParams(bottomLayoutViewParams);


            }
        });


    }

    private void setOfferData() {

        TextView newOfferText = (TextView) findViewById(R.id.newOfferText);
        ImageButton imgHeaderMapIcon1 = (ImageButton) findViewById(R.id.imgHeaderMapIcon1);
        RelativeLayout infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
        SemiBoldButton viewOfferBtn = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        SemiBoldButton dismissBtn = (SemiBoldButton) findViewById(R.id.dismissBtn);
        RelativeLayout horLineLayout = (RelativeLayout) findViewById(R.id.horLineLayout);

        if (StringUtilities.isValidString(promoCode)) {
            newOfferText.setText("Squeeze The Day!");
            imgHeaderMapIcon1.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            viewOfferBtn.setVisibility(View.VISIBLE);
            dismissBtn.setVisibility(View.GONE);
            horLineLayout.setVisibility(View.VISIBLE);
        } else {
            newOfferText.setText("Sip Sip Hooray!");
            newOfferText.setTextColor(ContextCompat.getColor(this, R.color.jamba_corporate_green));
            imgHeaderMapIcon1.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
            viewOfferBtn.setVisibility(View.GONE);
            dismissBtn.setVisibility(View.VISIBLE);
            if (urlLinks != null && urlLinks.length > 0) {
                dismissBtn.setText("Take Me There");
            }
            horLineLayout.setVisibility(View.GONE);
        }

        com.olo.jambajuice.Views.Generic.SanSerifTextView headerOfferTitle = (SanSerifTextView) findViewById(R.id.headerOfferTitle);
        //headerOfferTitle.setText(e);
        //setTextViewHTML(headerOfferTitle,e);
        String myString = e;
        if (urlLinks.length > 0) {
            int i1 = myString.indexOf(urlLinks[0]);
            int i2 = myString.lastIndexOf(urlLinks[0]) + urlLinks[0].length();
            final ClickableSpan myClickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    WebViewActivity.show(PushNotificationActivity.this, "Mobile Web", urlLinks[0]);
                }
            };

            SpannableString mySpannable = new SpannableString(myString);
            mySpannable.setSpan(myClickableSpan, i1, i2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //For UnderLine
            mySpannable.setSpan(new UnderlineSpan(), i1, i2, 0);

//            //For Bold
//            mySpannable.setSpan(new StyleSpan(Typeface.BOLD), i1, i2, 0);

            headerOfferTitle.setMovementMethod(LinkMovementMethod.getInstance());
            headerOfferTitle.setText(mySpannable, TextView.BufferType.SPANNABLE);
        } else {
            headerOfferTitle.setText(e);
        }
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                WebViewActivity.show(PushNotificationActivity.this, "Mobile Web", span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = html;
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHeaderMapIcon1:
                onBackPressed();
                break;

            case R.id.viewOfferBtn:
                if (UserService.isUserAuthenticated()) {
                    onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.B_IS_REFRESH_OFFER, true);
                    TransitionManager.slideUp(PushNotificationActivity.this, MyRewardsAndOfferActivity.class, bundle);
                } else {
                    Toast.makeText(PushNotificationActivity.this, "Please login before viewing the offer", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.dismissBtn:
                if (urlLinks.length > 0) {
                    WebViewActivity.show(this, "Mobile Web", urlLinks[0]);
                    finish();
                } else {
                    onBackPressed();
                }
                break;
        }
    }
}
