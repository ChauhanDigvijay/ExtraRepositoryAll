package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;

import org.json.JSONObject;

public class PassSendActivity extends AppCompatActivity
{
    TextView textEnd;
    String offerId;
    ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_send);
        textEnd = (TextView) findViewById(R.id.offertitle);
        image1 = (ImageView) findViewById(R.id.image);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null)
        {
            offerId = extras.getString("offerId");
        }
        sendsmspass();
    }

    public void sendsmspass()
    {
        JSONObject data = new JSONObject();
        FB_LY_UserOfferService.sharedInstance().sendOfferviaSms(data, offerId, new FB_LY_UserOfferService.ClypSmsCallback()
        {
            @Override
            public void onClypSmsCallback(JSONObject response, Exception error)
            {
                if (response != null)
                {
                    if (response.has("successFlag"))
                    {
                        image1.setImageResource(R.drawable.passsend);
                        textEnd.setText("Mobile Pass is  send successfully to your mobile through sms");
                    }
                    else
                    {
                       // image1.setImageResource(R.drawable.crossdelete);
                        textEnd.setText("Mobile Pass is not send successfully to your mobile through sms");
                    }
                }
                else
                {
                   // image1.setImageResource(R.drawable.crossdelete);
                    textEnd.setText("Mobile Pass is not send successfully to your mobile through sms");
                }

            }
        });
    }

    }

