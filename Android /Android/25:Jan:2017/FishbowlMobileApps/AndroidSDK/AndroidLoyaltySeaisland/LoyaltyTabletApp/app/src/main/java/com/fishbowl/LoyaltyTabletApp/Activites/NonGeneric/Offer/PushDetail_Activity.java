package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.BarcodeGenerated;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

public class PushDetail_Activity extends Activity
{
    String barcode_data = "Digvijay Chauhan";
    // barcode image
    Bitmap bitmap = null;
    ImageView bar_image;
    String Url;
    String Title;
    String desc;
    String offerId;
    String  promotioncode;
    Integer Expire;
    TextView offer_promo;
    Boolean isPMOffer;
    int promotionId;
    private NetworkImageView loginBackground,imlogo, imforward,headerImage;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    public  ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushdetail);
        imlogo= (NetworkImageView) findViewById(R.id.headerImage);
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null)
        {
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

        if(StringUtilities.isValidString(promotioncode))
        {
            try
            {
                if (StringUtilities.isValidString(promotioncode))
                {
                    offer_promo.setText(promotioncode);
                }
                hideProgressBar();
                bitmap = BarcodeGenerated.encodeAsBitmap(promotioncode, BarcodeFormat.QR_CODE, 300, 300);

                if(bitmap!=null)
                {
                    bar_image.setImageBitmap(bitmap);
                }
            }
            catch (WriterException e)
            {
                e.printStackTrace();
            }
        }
        else {
            if (StringUtilities.isValidString(offerId) && !isPMOffer)
            {
                fetchUserOffersPromo(offerId, isPMOffer);
            }
            else
            {
                fetchUserOffersPromo(String.valueOf(promotionId), isPMOffer);
            }
        }
        if (StringUtilities.isValidString(Title))
        {
            offertitle.setText(Title);
        }


        if (StringUtilities.isValidString(desc))
        {
            offerdesc.setText(desc);
        }
        else
        {
            offerdesc.setVisibility(View.GONE);
        }
        if(Expire>0)
        {
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

    private void  fetchUserOffersPromo(final String offerId,Boolean isPMIntegrated)
    {
        JSONObject object = new JSONObject();
        FB_LY_UserOfferService.sharedInstance().getClypOfferPromo(object, offerId,isPMIntegrated,new FB_LY_UserOfferService.ClypOfferPromoCallback()
        {
            @Override
            public void onClypOfferPromoCallback(JSONObject offerjson, Exception error)
            {
                if (offerjson != null)
                {
                    if(offerjson.has("promoCode"))
                    {
                        try
                        {
                        String   promocode=(String)offerjson.get("promoCode");
                        try
                        {
                            if (StringUtilities.isValidString(promocode))
                            {
                                offer_promo.setText(promocode);
                            }
                            hideProgressBar();
                            bitmap = BarcodeGenerated.encodeAsBitmap(promocode, BarcodeFormat.QR_CODE, 250, 250);
                            if(bitmap!=null)
                            {
                                bar_image.setImageBitmap(bitmap);
                            }
                        }
                        catch (WriterException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    }
                    else {
                        if (offerjson.has("message"))
                        {
                            try {
                                String    offermessage=(String)offerjson.get("message");
                                //  FBUtils.showErrorAlert(Passreward.this, offermessage);
                                //  code.setText(offermessage);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else
                {
                    FBUtils.tryHandleTokenExpiry(PushDetail_Activity.this,error);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FB_LY_MobileSettingService.sharedInstance().checkInButtonColor!=null)
        {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().companyLogoImageUrl;
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        }
    }
}
