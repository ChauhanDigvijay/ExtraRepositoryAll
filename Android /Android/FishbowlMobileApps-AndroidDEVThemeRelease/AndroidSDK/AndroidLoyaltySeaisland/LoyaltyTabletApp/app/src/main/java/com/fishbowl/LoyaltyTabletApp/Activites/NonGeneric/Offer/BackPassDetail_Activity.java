package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;

public class BackPassDetail_Activity extends Activity
{
    String Title;
    String desc;
    int  backgroundColor;
    String promocode;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpassdetail);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null)
        {
            Title = extras.getString("Title");
            promocode = extras.getString("Promocode");
            desc = extras.getString("desc");
            backgroundColor = extras.getInt("backgroundColor");
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BackPassDetail_Activity.this.finish();
            }
        });

        LinearLayout backpktop = (LinearLayout) findViewById(R.id.backpktop);
        TextView offerdescriptiondetail = (TextView) findViewById(R.id.offerdescriptiondetail);
        TextView offerpromocodedetail = (TextView) findViewById(R.id.offerpromocodedetail);
        TextView offerstorelimitdetail = (TextView) findViewById(R.id.offerstorelimitdetail);


        if (StringUtilities.isValidString(Title))
        {
            offerdescriptiondetail.setText(Title);
        }


        if (StringUtilities.isValidString(promocode))
        {
            offerpromocodedetail.setText(promocode);
        }
        backpktop.setBackgroundColor(backgroundColor);

    }



}
