package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;

/**
 * *
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class PassFullActivity extends Activity {
    ImageView uploaderImageUri;
    String Url;
    String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_full);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {


            Url = extras.getString("Url");

            Title = extras.getString("Title");

        }

        uploaderImageUri = (ImageView) findViewById(R.id.passview_full);

        TextView passtitle = (TextView) findViewById(R.id.pass_title);
        if (StringUtilities.isValidString(Title)) {
            passtitle.setText(Title);
        }
        if (StringUtilities.isValidString(Url)) {
            Ion.with(uploaderImageUri).placeholder(R.drawable.product_placeholder).load(Url);
        }
    }
}
