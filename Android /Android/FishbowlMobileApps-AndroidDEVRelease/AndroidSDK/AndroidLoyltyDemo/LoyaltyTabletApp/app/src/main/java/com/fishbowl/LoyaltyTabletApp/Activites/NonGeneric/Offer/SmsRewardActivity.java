package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;

public class SmsRewardActivity extends Activity
{
    String barcode_data = "Digvijay Chauhan";
    // barcode image
    Bitmap bitmap = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_push);
        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SmsRewardActivity.this.finish();
            }
        });
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
        }

        TextView offertitle = (TextView) findViewById(R.id.offertitle);
        TextView offerdate = (TextView) findViewById(R.id.offerdate);
        TextView offer_location = (TextView) findViewById(R.id.offer_location_name);
        TextView offerdesc = (TextView) findViewById(R.id.offerdesc);
     //   offer_promo = (TextView) findViewById(R.id.offer_promo);

        if (StringUtilities.isValidString(offerId)&&!isPMOffer)
        {
       //     fetchUserOffersPromo(offerId,isPMOffer);
        }
        else
        {
           // fetchUserOffersPromo(String.valueOf(promotionId),isPMOffer);
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

    @Override
    protected void onStart()
    {
        super.onStart();

//        if(FB_LY_MobileSettingService.sharedInstance().checkInButtonColor!=null) {
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//
//
//
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
//
//            final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
//            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
//            background.setImageUrl(url, mImageLoader);
//        }

    }
}
