package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.EmailOffer;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.BarcodeGenerated;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MultiReward_Activity extends Activity implements View.OnClickListener {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    View signedView;
    WebView webView;
    ImageView im1;
    Bitmap bitmap = null;
    String mimeType = "text/html";
    String encoding = "utf-8";
    int position;
    ProgressBarHandler progressBarHandler;
    int diifer;
    NetworkImageView img_Background;
    EmailOffer emailOffer;
    LinearLayout llayoutmain_one;
    LinearLayout print, sendSMS;
    String created;
    RelativeLayout rewardtoplayoutd;
    private MyViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ImageLoader mImageLoader;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            position = extras.getInt("position");
        }
        setContentView(R.layout.activity_multiple_offer);
        progressBarHandler = new ProgressBarHandler(MultiReward_Activity.this);
        img_Background = (NetworkImageView) findViewById(R.id.img_Background);

        rewardtoplayoutd = (RelativeLayout) findViewById(R.id.rewardtoplayout);
        im1 = (ImageView) findViewById(R.id.bt3);
        im1.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setCurrentItem(position);
        viewPager.setPageMargin(12);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(img_Background, R.drawable.signup, R.drawable.signup));
        img_Background.setImageUrl(url, mImageLoader);
    }

    @Override
    protected void onStop() {
        super.onStop();
        pagerAdapter.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt3:
                onBackPressed();
                break;
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                // image naming and path  to include sd card  appending name you choose for file
                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + now + ".jpg";

                // create bitmap screen capture
                View v1 = llayoutmain_one;
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();

                openScreenshot(imageFile);
            } catch (Throwable e) {
                // Several error may come out with file handling or OOM
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView image = (ImageView) findViewById(R.id.bt3);


        image.setVisibility(View.VISIBLE);
        if (sendSMS != null && print != null) {
            sendSMS.setVisibility(View.VISIBLE);
            print.setVisibility(View.VISIBLE);
        }
        if (rewardtoplayoutd != null) {
            rewardtoplayoutd.setBackgroundColor(Color.parseColor("#BF000000"));
        }

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;
        Activity ctx;
        private ImageLoader mImageLoader;
        private int postion;

        public MyViewPagerAdapter(Activity ctx) {
            this.ctx = ctx;
            inflater = LayoutInflater.from(ctx);
            mImageLoader = CustomVolleyRequestQueue.getInstance(ctx).getImageLoader();
        }

        public void release() {
        }

        @Override
        public int getCount() {
            if (RewardSummary.rewardList != null) {
                if (RewardSummary.rewardList.size() != 0) {
                    return RewardSummary.rewardList.size();
                }
            }
            return 0;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup currentView = null;
            RewardsItem item = null;
            Log.e("MyViewPagerAdapter", "instantiateItem for " + position);
            this.postion = position;
            item = RewardSummary.rewardList.get(position);
            Log.e("MyViewPagerAdapter", "instantiateItem need to create the View");
            currentView = (ViewGroup) inflater.inflate(R.layout.activity_view_basic, container, false);
            Log.i("MyViewPagerAdapter", "instantiateItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
            currentView = setView(currentView, item, postion);
            ((ViewPager) container).addView(currentView);
            return currentView;
        }

        public ViewGroup setView(final ViewGroup currentView, final RewardsItem item, final int position) {
            //EMAIL(HTML OFFER)
            if (item.getChannelID() == 1) {
                currentView.removeAllViews();


                if (item.getChannelID() == 1) {

                    if (item.getChannelID() == 1) {
                        emailoffer(item, currentView);
                    } else if (StringUtilities.isValidString(item.getNotificationContent())) {
                        couponurloffer(item, currentView);
                    } else if (StringUtilities.isValidString(item.getHtmlBody())) {
                        htmloffer(item, currentView);
                    } else {
                        generaloffer(item, currentView);
                    }
                }
            } else if (item.getChannelID() == 2) {
                currentView.removeAllViews();
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else if (StringUtilities.isValidString(item.getHtmlBody())) {
                    htmloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }
            }
            // SmsReward
            else if (item.getChannelID() == 3) {
                String title = item.getCompaignTitle();
                String desc = item.getCompaignDescription();

                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else {
                    smsoffer(item, currentView);
                }
            }
            //  SMS WITH PASSBOOK
            else if (item.getChannelID() == 4) {

                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }
            }
            //PUSH
            else if (item.getChannelID() == 5) {

                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }
            }
            //PUSH WITH PASSBOOK
            else if (item.getChannelID() == 6) {
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }
            }
            // PULL (HTML OFFERS)
            else if (item.getChannelID() == 7) {
                currentView.removeAllViews();
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else if (StringUtilities.isValidString(item.getHtmlBody())) {
                    htmloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }


            }

            // PULL (HTML OFFERS)
            else if (item.getChannelID() == 8) {
                currentView.removeAllViews();
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else if (StringUtilities.isValidString(item.getHtmlBody())) {
                    htmloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }


            } else {
                if (StringUtilities.isValidString(item.getNotificationContent())) {
                    couponurloffer(item, currentView);
                } else if (StringUtilities.isValidString(item.getHtmlBody())) {
                    htmloffer(item, currentView);
                } else {
                    generaloffer(item, currentView);
                }
            }
            return currentView;
        }


        public void emailoffer(final RewardsItem item, final ViewGroup currentView) {
            progressBarHandler.show();
            if (StringUtilities.isValidString(item.getNotificationContent())) {
                couponurloffer(item, currentView);

            }
//            {
//                progressBarHandler.show();
//                JSONObject object = new JSONObject();
//                FB_LY_UserService.sharedInstance().getEmailOfferDetail(object,String.valueOf(item.getCompaignId()),new FB_LY_UserService.FBLoyaltyEmailOfferDetail()
//                {
//                    public void onFBLoyaltyEmailOfferDetailCallback(JSONObject response, Exception error)
//                    {
//                        try
//                        {
//
//                            if (response != null && error==null)
//                            {
//                                if(response.has("preview")) {
//                                    if(!response.getString("preview").equalsIgnoreCase("null")) {
//
//                                        progressBarHandler.hide();
//                                        {
//                                            emailOffer = new EmailOffer();
//                                            EmailOffer emailOffers = emailOffer.initWithJSon(response);
//                                            String html = emailOffers.getPreview();
//                                            signedView = inflater.inflate(R.layout.view_offer_html, currentView, false);
//                                            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
//                                            webView = (WebView) rewardsView.findViewById(R.id.webview);
//                                            webView.loadData(html, mimeType, encoding);
//                                            currentView.addView(signedView);
//                                        }
//                                        couponurloffer(item,currentView);
//
//                                    }
//                                    else   if (StringUtilities.isValidString(item.getNotificationContent()))
//
//                                    {
//                                       // progressBarHandler.hide();
//                                        {
//                                            couponurloffer(item,currentView);
//                                        }
//                                    }
//                                    else if (StringUtilities.isValidString(item.getHtmlBody()))
//                                    {
//                                        progressBarHandler.hide();
//                                        htmloffer(item,currentView);
//                                    }
//                                    else
//                                    {
//                                        progressBarHandler.hide();
//                                        emailofferafterapi(item,currentView);
//                                    }
//                                }
//
//
//                            }
//                            else
//                            {
//                                progressBarHandler.hide();
//                                FBUtils.tryHandleTokenExpiry(MultiReward_Activity.this, error);
//
//                            }
//                        }
//                        catch (Exception e){}
//                    }
//                });
//
//
//            }
        }


        public void smsoffer(final RewardsItem item, final ViewGroup currentView) {

            String title = item.getCompaignTitle();
            String desc = item.getCompaignDescription();
            {
                if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                    Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                    if (intent == null) {
                        intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                    }
                    if (intent != null) {
                        diifer = FBUtils.daysBetween(new Date(), intent);
                    }
                }


                currentView.removeAllViews();
                signedView = inflater.inflate(R.layout.view_offer_sms, currentView, false);
                RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
                TextView offertitle = (TextView) rewardsView.findViewById(R.id.offertitle);
                TextView offerdate = (TextView) rewardsView.findViewById(R.id.offerdate);
                //   TextView offerdesc = (TextView) rewardsView.findViewById(R.id.offerdesc);
                TextView description = (TextView) signedView.findViewById(R.id.nummber_four);
                NetworkImageView image = (NetworkImageView) signedView.findViewById(R.id.img_one);
                TextView offer_promo = (TextView) signedView.findViewById(R.id.offer_promo);
                ImageView bar_image = (ImageView) signedView.findViewById(R.id.bar_image);
                if (StringUtilities.isValidString(title)) {
                    offertitle.setText(title);
                }

                String promotioncode = item.getPromotioncode();
                if (StringUtilities.isValidString(promotioncode)) {
                    try {
                        if (StringUtilities.isValidString(promotioncode)) {
                            offer_promo.setText(promotioncode);
                        }

                        bitmap = BarcodeGenerated.encodeAsBitmap(promotioncode, BarcodeFormat.QR_CODE, 100, 100);

                        if (bitmap != null) {
                            bar_image.setImageBitmap(bitmap);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {

                    offer_promo.setText("No Promo Code");
                }


                if (diifer > 0) {
                    offerdate.setVisibility(View.VISIBLE);
                    offerdate.setText("Expires in" + " " + diifer + " " + "days");
                } else {
                    offerdate.setVisibility(View.GONE);
                }
                currentView.addView(signedView);

                if (item.getPromotioncode() != null) {
                    description.setText(item.getPromotioncode());
                } else {
                    description.setText("Promo Code: xx/xx/xxxx Promo Code");
                }


            }
        }


        public void couponurloffer(final RewardsItem item, final ViewGroup currentView) {

            final String html = item.getNotificationContent();
            signedView = inflater.inflate(R.layout.view_offer_html, currentView, false);
            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
            sendSMS = (LinearLayout) signedView.findViewById(R.id.bt2);
            webView = (WebView) rewardsView.findViewById(R.id.webview);
            llayoutmain_one = (LinearLayout) signedView.findViewById(R.id.llayoutmain_one);
            print = (LinearLayout) signedView.findViewById(R.id.sendPrint);
            webView.getSettings().setJavaScriptEnabled(true);


            currentView.addView(signedView);


            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    {
                        progressBarHandler.dismiss();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Toast.makeText(ContestActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                    progressBarHandler.dismiss();
                }
            });
            webView.loadUrl(html);


            sendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    created = FB_LY_UserService.sharedInstance().member.emailID;
                    sendPassEmail(item.getCompaignTitle(), html, created);
                }
            });

            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // verifyStoragePermissions(MultiReward_Activity.this);
                    doPhotoPrint(item);
                }
            });


        }

        private void doPhotoPrint(RewardsItem item) {
            PrintHelper photoPrinter = new PrintHelper(MultiReward_Activity.this);
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            photoPrinter.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ImageView image = (ImageView) findViewById(R.id.bt3);


            image.setVisibility(View.GONE);
            sendSMS.setVisibility(View.GONE);
            print.setVisibility(View.GONE);
            rewardtoplayoutd.setBackgroundColor(Color.WHITE);

            View v1 = MultiReward_Activity.this.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

            if (StringUtilities.isValidString(item.getCompaignTitle())) {
                String firstname = FB_LY_UserService.sharedInstance().member.firstName;
                photoPrinter.printBitmap(firstname + "_" + item.getCompaignTitle(), bitmap);
            } else {
                photoPrinter.printBitmap("The coupon", bitmap);
            }

        }


        public void htmloffer(final RewardsItem item, final ViewGroup currentView) {
            String html = item.getHtmlBody();
            signedView = inflater.inflate(R.layout.view_offer_html, currentView, false);
            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
            webView = (WebView) rewardsView.findViewById(R.id.webview);
            webView.loadData(html, mimeType, encoding);
            currentView.addView(signedView);


        }


        public void generaloffer(final RewardsItem item, final ViewGroup currentView)

        {
            {
                currentView.removeAllViews();
                signedView = (ViewGroup) inflater.inflate(R.layout.view_offer, currentView, false);
                final TextView title = (TextView) signedView.findViewById(R.id.nummber_one);
                final TextView desc = (TextView) signedView.findViewById(R.id.nummber_four);
                TextView expiry = (TextView) signedView.findViewById(R.id.nummber_five);
                TextView offer_promo = (TextView) signedView.findViewById(R.id.offer_promo);
                ImageView bar_image = (ImageView) signedView.findViewById(R.id.bar_image);
                LinearLayout sendSMS = (LinearLayout) signedView.findViewById(R.id.bt2);
                NetworkImageView image = (NetworkImageView) signedView.findViewById(R.id.img_one);
                currentView.addView(signedView);
                title.setText(item.getCompaignTitle());
                desc.setText(item.getCompaignDescription());

                String promotioncode = item.getPromotioncode();
                if (StringUtilities.isValidString(promotioncode)) {
                    try {
                        if (StringUtilities.isValidString(promotioncode)) {
                            offer_promo.setText(promotioncode);
                        }

                        bitmap = BarcodeGenerated.encodeAsBitmap(promotioncode, BarcodeFormat.QR_CODE, 100, 100);

                        if (bitmap != null) {
                            bar_image.setImageBitmap(bitmap);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {

                    offer_promo.setText("No Promo Code");
                }

                if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                    Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                    if (intent == null) {
                        intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                    }
                    if (intent != null) {
                        diifer = FBUtils.daysBetween(new Date(), intent);
                    }
                }


                if (diifer != 0) {
                    expiry.setVisibility(View.VISIBLE);
                    expiry.setText("Expire Days" + " in " + diifer);
                } else {
                    expiry.setVisibility(View.GONE);
                }

                if (StringUtilities.isValidString(item.getPassCustomStripUrl())) {
                    final String url = "http://" + item.getPassCustomStripUrl();
                    mImageLoader.get(url, ImageLoader.getImageListener(image, R.drawable.mybrestoofferimage, R.drawable.mybrestoofferimage));
                    image.setImageUrl(url, mImageLoader);
                } else {
                    image.setBackgroundResource(R.drawable.mybrestoofferimage);
                }


//                sendSMS.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String offerId = String.valueOf(item.getCompaignId());
//                        Intent intent1 = new Intent(ctx, PassSendActivity.class);
//                        Bundle extras = new Bundle();
//                        extras.putString("offerId", offerId);
//                        intent1.putExtras(extras);
//                        startActivity(intent1);
//                    }
//                });


            }
        }


        public void emailofferafterapi(final RewardsItem item, final ViewGroup currentView)

        {
            {
                currentView.removeAllViews();
                signedView = (ViewGroup) inflater.inflate(R.layout.view_offer_promocode_only, currentView, false);
                final TextView title = (TextView) signedView.findViewById(R.id.nummber_one);
                final TextView desc = (TextView) signedView.findViewById(R.id.nummber_four);
                TextView expiry = (TextView) signedView.findViewById(R.id.nummber_five);
                TextView offer_promo = (TextView) signedView.findViewById(R.id.offer_promo);
                ImageView bar_image = (ImageView) signedView.findViewById(R.id.bar_image);
                LinearLayout sendSMS = (LinearLayout) signedView.findViewById(R.id.bt2);
                NetworkImageView image = (NetworkImageView) signedView.findViewById(R.id.img_one);
                currentView.addView(signedView);
                title.setText(item.getCompaignTitle());
                desc.setText(item.getCompaignDescription());

                String promotioncode = item.getPromotioncode();
                if (StringUtilities.isValidString(promotioncode)) {
                    try {
                        if (StringUtilities.isValidString(promotioncode)) {
                            offer_promo.setText(promotioncode);
                        }

                        bitmap = BarcodeGenerated.encodeAsBitmap(promotioncode, BarcodeFormat.QR_CODE, 100, 100);

                        if (bitmap != null) {
                            bar_image.setImageBitmap(bitmap);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {

                    offer_promo.setText("No Promo Code");
                }

                if (StringUtilities.isValidString(item.getValidityEndDateTime())) {
                    Date intent = FBUtils.getDateFromString(item.getValidityEndDateTime(), (String) null);
                    if (intent == null) {
                        intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                    }
                    if (intent != null) {
                        diifer = FBUtils.daysBetween(new Date(), intent);
                    }
                }

                if (diifer != 0) {
                    expiry.setVisibility(View.VISIBLE);
                    expiry.setText("Expire Days" + " in " + diifer);
                } else {
                    expiry.setVisibility(View.GONE);
                }

                if (StringUtilities.isValidString(item.getPassCustomStripUrl())) {
                    final String url = "http://" + item.getPassCustomStripUrl();
                    mImageLoader.get(url, ImageLoader.getImageListener(image, R.drawable.mybrestoofferimage, R.drawable.mybrestoofferimage));
                    image.setImageUrl(url, mImageLoader);
                } else {
                    //  image.setBackgroundResource(R.drawable.mybrestoofferimage);
                }


//                sendSMS.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String offerId = String.valueOf(item.getCompaignId());
//                        Intent intent1 = new Intent(ctx, PassSendActivity.class);
//                        Bundle extras = new Bundle();
//                        extras.putString("offerId", offerId);
//                        intent1.putExtras(extras);
//                        startActivity(intent1);
//                    }
//                });


            }
        }


        public void sendPassEmail(String subject, String body, String email) {
            progressBarHandler.show();
            JSONObject object = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray.put(email);
                object.put("toAddress", jsonArray);
                object.put("subject", subject);
                object.put("body", "hi hello");
                object.put("couponUrl", body);


            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Ion.with(getApplicationContext()).load(body).asString().setCallback(new FutureCallback<String>() {
//                @Override
//                public void onCompleted(Exception e, String result) {
//
//
//                }
//            });


            FB_LY_UserService.sharedInstance().postsendPassEmail(object, new FB_LY_UserService.FBSendPassEmail() {
                public void onSendPassEmaiCallback(JSONObject response, Exception error) {
                    if (response != null && error == null) {

                        if (response.has("successFlag")) {
                            try {
                                if (response.getString("successFlag").equalsIgnoreCase("true")) {


                                    progressBarHandler.dismiss();
                                    showalertoffer1();
                                    //     FBUtils.showAlert(MultiReward_Activity.this, "Email send Successfully");
                                } else {


                                    //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
                                    progressBarHandler.dismiss();
                                    showalertoffer();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
                            progressBarHandler.dismiss();
                            showalertoffer();
                        }
                    } else {
                        //  FBUtils.tryHandleTokenExpiry( getActivity(), error);
                        progressBarHandler.dismiss();
                        showalertoffer();
                    }
                }
            });


        }


        public void showalertoffer() {
            AlertDialog alertDialog = new AlertDialog.Builder(MultiReward_Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
            alertDialog.setMessage("Email has been Failed");
            alertDialog.setIcon(R.drawable.logomain);
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }


        public void showalertoffer1() {
            AlertDialog alertDialog = new AlertDialog.Builder(MultiReward_Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
            alertDialog.setMessage("The coupon has been sent to your email address" + " " + created);
            alertDialog.setIcon(R.drawable.logomain);
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }


        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("MyViewPagerAdapter", "destroyItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }
    }

}