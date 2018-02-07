package com.BasicApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Utils.BarcodeGenerated;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by digvijaychauhan on 31/07/16.
 */


public class SmsRewardActivity extends Activity {
    String barcode_data = "Digvijay Chauhan";
    // barcode image
    Bitmap bitmap = null;
    ImageView bar_image;
    TextView offer_promo;
    String Url;
    String Title;
    String desc;
    String offerId;
    Integer Expire;
 //   TextView offer_promo;
    Boolean isPMOffer;
    int promotionId;
    private NetworkImageView  imforward,headerImage;
    private ImageLoader mImageLoader;
    private Toolbar toolbar;
    private NetworkImageView background;
    public  ImageView backbutton;
    String  promotioncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms_reward);
        background = (NetworkImageView) findViewById(R.id.img_Back);


        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsRewardActivity.this.finish();
            }
        });

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null) {
            promotionId = extras.getInt("promotionId");
            Title = extras.getString("Title");
            Expire = extras.getInt("Expire");
            offerId = extras.getString("offerId");
            desc = extras.getString("desc");
            isPMOffer=extras.getBoolean("isPMOffer");
            promotioncode = extras.getString("promotioncode");
        }

        bar_image = (ImageView) findViewById(R.id.bar_image);
        TextView offertitle = (TextView) findViewById(R.id.offertitle);
        TextView offerdate = (TextView) findViewById(R.id.offerdate);
        TextView offer_location = (TextView) findViewById(R.id.offer_location_name);
        TextView offerdesc = (TextView) findViewById(R.id.offerdesc);
        offer_promo = (TextView) findViewById(R.id.offer_promo);

        if(StringUtilities.isValidString(promotioncode)) {
            try {

                if (StringUtilities.isValidString(promotioncode)) {
                    offer_promo.setText(promotioncode);
                }

                hideProgressBar();
                bitmap = BarcodeGenerated.encodeAsBitmap(promotioncode, BarcodeFormat.QR_CODE, 300, 300);
                if(bitmap!=null) {
                    bar_image.setImageBitmap(bitmap);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
        else {
            if (StringUtilities.isValidString(offerId) && !isPMOffer) {

                fetchUserOffersPromo(offerId, isPMOffer);
            } else {
                fetchUserOffersPromo(String.valueOf(promotionId), isPMOffer);
            }
        }

        if (StringUtilities.isValidString(Title)) {
            offertitle.setText(Title);
        }


        if (StringUtilities.isValidString(desc)) {
            offerdesc.setText(desc);
        }
        else
        {
            offerdesc.setVisibility(View.GONE);
        }
        if(Expire>0) {
            offerdate.setText("Expires in" + " " + Expire + " " + "days");
        }


    }

    private void hideProgressBar()
    {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }


    public void enableScreen(boolean isEnabled)
    {

        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
        if (screenDisableView != null)
        {
            if (!isEnabled)
            {
                screenDisableView.setVisibility(View.VISIBLE);
            }
            else
            {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    private void  fetchUserOffersPromo(final String offerId,Boolean isPMIntegrated) {

        JSONObject object = new JSONObject();
        FBUserOfferService.sharedInstance().getFBOfferPromo(object, offerId,isPMIntegrated,new FBUserOfferService.FBOfferPromoCallback() {
            @Override
            public void OnFBOfferPromoCallback(JSONObject offerjson, Exception error)
            {


                if (offerjson != null)
                {
                    if(offerjson.has("promoCode"))
                    { try {
                        String   promocode=(String)offerjson.get("promoCode");


                        try {

                            if (StringUtilities.isValidString(promocode)) {
                                offer_promo.setText(promocode);
                            }

                            hideProgressBar();
                            bitmap = BarcodeGenerated.encodeAsBitmap(promocode, BarcodeFormat.QR_CODE, 300, 300);
                            if(bitmap!=null) {
                                bar_image.setImageBitmap(bitmap);
                            }
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                    else {
                        if (offerjson.has("message")) {
                            try {
                                String    offermessage=(String)offerjson.get("message");
                                //  FBUtils.showErrorAlert(Passreward.this, offermessage);
                                //  code.setText(offermessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else
                {


                    FBUtils.tryHandleTokenExpiry(SmsRewardActivity.this,error);


                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

//        if(FBViewMobileSettingsService.sharedInstance().checkInButtonColor!=null) {
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//
//
//
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
//
//            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
//            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
//            background.setImageUrl(url, mImageLoader);
//
//
//
//
//
//        }

    }
}
