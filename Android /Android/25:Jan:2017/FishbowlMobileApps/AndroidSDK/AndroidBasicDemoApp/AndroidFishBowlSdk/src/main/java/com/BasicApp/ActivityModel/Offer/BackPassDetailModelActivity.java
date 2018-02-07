package com.BasicApp.ActivityModel.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.StringUtilities;


/**
 **
 * Created by Digvijay Chauhan on 03/5/16.
 */
public class BackPassDetailModelActivity extends Activity {


    String Url;
    String Title;
    String desc;
    int  backgroundColor;
    String offerId;
    String promocode;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpassdetail);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {


            Title = extras.getString("Title");
            promocode = extras.getString("Promocode");
            desc = extras.getString("desc");
            backgroundColor = extras.getInt("backgroundColor");

        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPassDetailModelActivity.this.finish();
            }
        });
        LinearLayout backpktop = (LinearLayout) findViewById(R.id.backpktop);
        TextView offerdescriptiondetail = (TextView) findViewById(R.id.offerdescriptiondetail);
        TextView offerpromocodedetail = (TextView) findViewById(R.id.offerpromocodedetail);
        TextView offerstorelimitdetail = (TextView) findViewById(R.id.offerstorelimitdetail);

      //  TextView done = (TextView) findViewById(R.id.done);

//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//
//            {
//
//                onBackPressed();
//              //  overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//            }
//        });
        if (StringUtilities.isValidString(Title)) {
            offerdescriptiondetail.setText(Title);
        }


        if (StringUtilities.isValidString(promocode)) {
            offerpromocodedetail.setText(promocode);
        }
        backpktop.setBackgroundColor(backgroundColor);

    }



}
