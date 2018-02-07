package com.BasicApp.Activites.NonGeneric.LoyaltyCard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.BasicApp.Utils.BarcodeGenerated;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBMember;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class ViewLoyaltyCardModel extends Activity {
    private android.support.v7.widget.Toolbar toolbar;
    TextView loyaltyname,loyaltyno;
    ImageView bar_image;
    Bitmap bitmap = null;
    FBMember member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_loyalty_card);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewLoyaltyCardModel.this.finish();
            }
        });

        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(ViewLoyaltyCardModel.this).mSharedPreferences.getString("FBUser", "");
        member = gson.fromJson(json, FBMember.class);
        loyaltyname = (TextView) findViewById(R.id.loyaltyname);
        loyaltyno = (TextView) findViewById(R.id.loyaltyno);
        bar_image = (ImageView) findViewById(R.id.bar_image);

        if(member!=null) {
            loyaltyname.setText(member.getFirstName());
            loyaltyno.setText(member.getLoyaltyno());

            String loyalityNo = member.getLoyaltyno();
            try {
                bitmap = BarcodeGenerated.encodeAsBitmap(loyalityNo, BarcodeFormat.QR_CODE, 300, 300);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                bar_image.setImageBitmap(bitmap);
            }
        }

    }
    public void onCustomBackPressed() {
        ViewLoyaltyCardModel.this.finish();
    }
}
