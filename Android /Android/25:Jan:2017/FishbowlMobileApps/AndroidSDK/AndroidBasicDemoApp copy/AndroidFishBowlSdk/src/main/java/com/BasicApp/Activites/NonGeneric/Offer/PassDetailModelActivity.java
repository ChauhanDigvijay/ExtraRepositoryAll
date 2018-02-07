package com.BasicApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Services.PassReadService;
import com.BasicApp.BusinessLogic.Services.PassSaveService;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.zxing.WriterException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


/**
 * *
 * Created by Digvijay Chauhan
 */
public class PassDetailModelActivity extends Activity implements Response.Listener<byte[]>, Response.ErrorListener {

    Bitmap strip;
    String Title;
    String desc;
    String offerId;
    Integer Expire;
    String promocode;
    int promotionId;
    int backgroundColor;
    Boolean isPMOffer;
    private PassReadService passReadingService;
    private TextView logoTextView;
    private TextView headerExptext;
    private TextView subHeaderDate;
    private TextView barcodedetailmessage;
    private TextView auxiliaryFieldlabel;
    private TextView auxiliaryFieldvalue;
    private ImageView barcodeImageView;
    private LinearLayout primaryFieldsLayout,secondaryFieldsLayout;
    private FrameLayout auxiliarFieldsLayout;
    private ImageView thumbnailImageView;
    private ImageView stripeImageView;
    private String t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passdetail);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            enableScreen(false);
            promotionId = extras.getInt("promotionId");
            Title = extras.getString("Title");
            Expire = extras.getInt("Expire");
            offerId = extras.getString("offerId");
            desc = extras.getString("desc");
            isPMOffer = extras.getBoolean("isPMOffer");


        }

        //     pknewpassservices();
        if (FBUtils.isNetworkAvailable(this)) {
            //  CallOfferPassservices();
        } else {
            FBUtils.showErrorAlert(PassDetailModelActivity.this, "Please check your network connection and try again ");
            enableScreen(true);
            onBackPressed();

        }
    }


    public void enableScreen(boolean isEnabled) {

        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }


    public void PkpassReadServices(byte[] offerpass) {

        InputStream input = new ByteArrayInputStream(offerpass);
        PassSaveService storageService = new PassSaveService(this);
        File dir = null;
        try {
            dir = storageService.InflatePkPassInTempDir(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        t = dir.getPath();

        this.logoTextView = (TextView) findViewById(R.id.headerText);
        this.barcodeImageView = (ImageView) findViewById(R.id.barcodeImageView);
        this.thumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        this.headerExptext = (TextView) findViewById(R.id.headerExptext);
        this.barcodedetailmessage = (TextView) findViewById(R.id.barcodedetailmessage);
        this.subHeaderDate = (TextView) findViewById(R.id.subheaderDate);
        this.auxiliaryFieldlabel = (TextView) findViewById(R.id.auxiliaryfieldlabel);
        this.auxiliaryFieldvalue = (TextView) findViewById(R.id.auxiliaryfieldvalue);
        this.primaryFieldsLayout = (LinearLayout) findViewById(R.id.primaryFieldsLinearLayout);
        this.secondaryFieldsLayout = (LinearLayout) findViewById(R.id.secondaryFieldsLinearLayout);
        this.auxiliarFieldsLayout = (FrameLayout) findViewById(R.id.auxiliaryFieldsLinearLayout);
        this.stripeImageView = (ImageView) findViewById(R.id.stripImageView);
        ImageView B1 = (ImageView) findViewById(R.id.rotatepk);

        try {
            this.passReadingService = new PassReadService(t, this);
            enableScreen(true);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {

                Intent intent = new Intent(PassDetailModelActivity.this, BackPassDetailModelActivity.class);
                Bundle extras = new Bundle();

                extras.putString("Title", Title);
                extras.putString("desc", desc);
                extras.putString("Promocode", promocode);
                extras.putInt("backgroundColor", backgroundColor);
                intent.putExtras(extras);
                startActivity(intent);
                //overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
            }
        });

        backgroundColor = this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor"));
        this.primaryFieldsLayout.setBackgroundColor(this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor")));
        //  this.secondaryFieldsLayout.setBackgroundColor(this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor")));
        this.auxiliarFieldsLayout.setBackgroundColor(this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor")));

        this.logoTextView.setText(this.passReadingService.getValue("logoText"));
        this.logoTextView.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));
        String dateString = this.passReadingService.getValue("expirationDate").toString();//"2015-09-14T00:00:00-07:00"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
            dateFormatter.setTimeZone(TimeZone.getDefault());
            String dt = dateFormatter.format(convertedDate);
            headerExptext.setText("Expires ");
            headerExptext.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("labelColor")));
            subHeaderDate.setText(dt);
            subHeaderDate.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject eventTicket = this.passReadingService.getJSONObject("coupon");//eventTicket

        if (eventTicket.containsKey("headerFields")) {
            JSONArray primaryFields = (JSONArray) eventTicket.get("headerFields");
            for (Object obj : primaryFields) {
                JSONObject primaryField = (JSONObject) obj;
                String label = primaryField.get("label").toString();
                if (label.equalsIgnoreCase(("expires"))) {
                    if (primaryField.containsKey("value")) {

                        try {


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            }
        }


        if (eventTicket.containsKey("secondaryFields")) {
            JSONArray secondaryFields = (JSONArray) eventTicket.get("secondaryFields");
            for (Object obj : secondaryFields) {
                JSONObject secondaryField = (JSONObject) obj;
                TextView label = new TextView(this);
                label.setTextSize(10);
                label.setText(secondaryField.get("label").toString());
                TextView value = new TextView(this);
                value.setTextSize(15);
                value.setText(secondaryField.get("value").toString());
                this.secondaryFieldsLayout.addView(label);
                this.secondaryFieldsLayout.addView(value);
            }
        }


        if (eventTicket.containsKey("auxiliaryFields")) {
            JSONArray auxiliaryFields = (JSONArray) eventTicket.get("auxiliaryFields");
            for (int i = 0; i < auxiliaryFields.size(); i = i + 2) {
                JSONObject auxField1 = (JSONObject) auxiliaryFields.get(i);
                auxiliaryFieldvalue.setText(auxField1.get("value").toString());
                auxiliaryFieldlabel.setText(auxField1.get("label").toString());
                auxiliaryFieldlabel.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("labelColor")));
                auxiliaryFieldvalue.setText(auxField1.get("value").toString());
                auxiliaryFieldvalue.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));

            }
        }

        try {
            if (StringUtilities.isValidString(auxiliaryFieldvalue.getText().toString())) {
                if (auxiliaryFieldvalue.getText().toString().contains("youtube")) {

                    int ind = auxiliaryFieldvalue.getText().toString().indexOf("=");
                    if (ind > 0) {
                        String videoId = auxiliaryFieldvalue.getText().toString().split("=")[1];

                    }
                    auxiliaryFieldvalue.setVisibility(View.GONE);
                } else if (auxiliaryFieldvalue.getText().toString().contains("http")) {
                    auxiliaryFieldvalue.setVisibility(View.GONE);


                } else {
                    strip = this.passReadingService.getImage("strip");
                    if (strip != null) {
                        this.stripeImageView.setImageBitmap(strip);
                    }
                }
            } else {
                strip = this.passReadingService.getImage("strip");
                if (strip != null) {
                    this.stripeImageView.setImageBitmap(strip);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap icon;
        icon = this.passReadingService.getLogo();
        if (icon != null) {
            this.thumbnailImageView.setImageBitmap(icon);
        }

        Bitmap barcode;
        try {
            barcode = this.passReadingService.getBarcode();
            if (barcode != null) {
                this.barcodeImageView.setImageBitmap(barcode);
                promocode = this.passReadingService.getPromocode();
                this.barcodedetailmessage.setText(promocode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

//
//    private void CallOfferPassservices() {
//
//        FBUserOfferService.sharedInstance().getClypOfferPass(new org.json.JSONObject(),offerId, isPMOffer, new FBOfferPassCallback() {
//            @Override
//            public void OnFBOfferPassCallback(byte[] offerpass, Exception error) {
//
//                if (offerpass != null) {
//
//                    if(offerpass.length>0) {
//                        PkpassReadServices(offerpass);
//                    }
//                    else {
//
//
//                        enableScreen(true);
//                        onBackPressed();
//
//                    }
//                } else {
//                  //  Utils.showErrorAlert(PassDetailModelActivity.this, "Empty Response");
//                    FBUtils.tryHandleTokenExpiry(PassDetailModelActivity.this,error);
//                    enableScreen(true);
//
//
//                }
//            }
//        });
//
//    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE. ERROR:: " + error.getMessage());
    }


    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response != null) {

                try {

                    InputStream input = new ByteArrayInputStream(response);
                    PassSaveService storageService = new PassSaveService(this);
                    File dir = storageService.InflatePkPassInTempDir(input);
                    input.close();
                    t = dir.getPath();
                    //    PkpassReadServices();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }
}
