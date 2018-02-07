package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.PassReadService;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.PassSaveService;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;
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

public class PassDetail_Activity extends Activity implements Response.Listener<byte[]>, Response.ErrorListener
{
    String barcode_data = "Digvijay Chauhan";
    Bitmap strip;
    Bitmap bitmap = null;
    ImageView bar_image;
    String Url;
    String Title;
    String desc;
    String offerId;
    Integer Expire;
    TextView offer_promo;
    int count;
    private PassReadService passReadingService;
    String promocode;
    private RelativeLayout ticketRelativeLayout;
    private TextView logoTextView;
    private TextView headerExptext;
    private TextView subHeaderDate;
    private TextView barcodedetailmessage;
    private TextView auxiliaryFieldlabel;
    private TextView auxiliaryFieldvalue;
    private ImageView barcodeImageView;
    private LinearLayout primaryFieldsLayout;
    private LinearLayout secondaryFieldsLayout;
    private FrameLayout auxiliarFieldsLayout;
    private String path;
    private ImageView thumbnailImageView;
    private ImageView stripeImageView;
    private ImageView feedImageView;
    private ImageButton deleteButton;
    private String t = null;
    int promotionId;
    int backgroundColor;
    Boolean isPMOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passdetail);
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null)
        {
            enableScreen(false);
            promotionId = extras.getInt("promotionId");
            Title = extras.getString("Title");
            Expire = extras.getInt("Expire");
            offerId = extras.getString("offerId");
            desc = extras.getString("desc");
            isPMOffer=extras.getBoolean("isPMOffer");
        }

        if(FBUtils.isNetworkAvailable(this))
        {
            CallOfferPassservices();
        }
        else
        {
            FBUtils.showErrorAlert(PassDetail_Activity.this, "Please check your network connection and try again ");
            enableScreen(true);
            onBackPressed();
        }
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

    public void PkpassReadServices(byte[] offerpass)
    {
        InputStream input = new ByteArrayInputStream(offerpass);
        PassSaveService storageService = new PassSaveService(this);
        File dir = null;
        try
        {
            dir = storageService.InflatePkPassInTempDir(input);
            input.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        t = dir.getPath();
        this.logoTextView = (TextView)findViewById(R.id.headerText);
        this.barcodeImageView = (ImageView)findViewById(R.id.barcodeImageView);
        this.thumbnailImageView = (ImageView)findViewById(R.id.thumbnailImageView);
        this.headerExptext = (TextView)findViewById(R.id.headerExptext);
        this.barcodedetailmessage = (TextView)findViewById(R.id.barcodedetailmessage);
        this.subHeaderDate = (TextView)findViewById(R.id.subheaderDate);
        this.deleteButton = (ImageButton)findViewById(R.id.deletenewButton);
        this.auxiliaryFieldlabel = (TextView)findViewById(R.id.auxiliaryfieldlabel);
        this.auxiliaryFieldvalue = (TextView)findViewById(R.id.auxiliaryfieldvalue);
        this.primaryFieldsLayout = (LinearLayout)findViewById(R.id.primaryFieldsLinearLayout);
        this.auxiliarFieldsLayout = (FrameLayout)findViewById(R.id.auxiliaryFieldsLinearLayout);
        this.stripeImageView = (ImageView)findViewById(R.id.stripImageView);
        ImageView B1 = (ImageView)findViewById(R.id.rotatepk);
        try
        {
            this.passReadingService = new PassReadService(t,this);
            enableScreen(true);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        B1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PassDetail_Activity.this,BackPassDetail_Activity.class);
                Bundle extras = new Bundle();
                extras.putString("Title", Title);
                extras.putString("desc", desc);
                extras.putString("Promocode", promocode);
                extras.putInt("backgroundColor", backgroundColor);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        backgroundColor   =this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor"));
        this.primaryFieldsLayout.setBackgroundColor(this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor")));
        this.auxiliarFieldsLayout.setBackgroundColor(this.passReadingService.getColor(this.passReadingService.getValue("backgroundColor")));
        this.logoTextView.setText(this.passReadingService.getValue("logoText"));
        this.logoTextView.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));
        String dateString =this.passReadingService.getValue("expirationDate").toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date convertedDate = new Date();
        try
        {
            convertedDate = dateFormat.parse(dateString);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
            dateFormatter.setTimeZone(TimeZone.getDefault());
            String dt = dateFormatter.format(convertedDate);
            headerExptext.setText("Expires ");
            headerExptext.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("labelColor")));
            subHeaderDate.setText(dt);
            subHeaderDate.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JSONObject eventTicket = this.passReadingService.getJSONObject("coupon");//eventTicket

        if (eventTicket.containsKey("headerFields"))
        {
            JSONArray primaryFields = (JSONArray) eventTicket.get("headerFields");
            for (Object obj : primaryFields)
            {
                JSONObject primaryField = (JSONObject) obj;
                String label = primaryField.get("label").toString();
                if(label.equalsIgnoreCase(("expires")))
                {
                    if(primaryField.containsKey("value"))
                    {
//                        String dateString = primaryField.get("value").toString();//"2015-09-14T00:00:00-07:00"
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//                        Date convertedDate = new Date();
                        try
                        {
//                            convertedDate = dateFormat.parse(dateString);
//                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
//                            dateFormatter.setTimeZone(TimeZone.getDefault());
//                            String dt = dateFormatter.format(convertedDate);
//                            headerExptext.setText("Expires ");
//                            subHeaderDate.setText(dt);
//
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (eventTicket.containsKey("secondaryFields"))
        {
            JSONArray secondaryFields = (JSONArray) eventTicket.get("secondaryFields");
            for (Object obj : secondaryFields)
            {
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

        if (eventTicket.containsKey("auxiliaryFields"))
        {
            JSONArray auxiliaryFields = (JSONArray) eventTicket.get("auxiliaryFields");
            for (int i = 0; i < auxiliaryFields.size(); i = i + 2)
            {
                JSONObject auxField1 = (JSONObject) auxiliaryFields.get(i);
                JSONObject auxField2 = (JSONObject) ((i + 1 < auxiliaryFields.size()) ? auxiliaryFields.get(i + 1) : null);
                auxiliaryFieldvalue.setText(auxField1.get("value").toString());
//                auxiliaryFieldlabel.setText(Html.fromHtml("<a href=\"" + auxField1.get("label").toString() + "\">" + auxField1.get("label").toString() + "</a> "));
//                auxiliaryFieldlabel.setMovementMethod(LinkMovementMethod.getInstance());
//                auxiliaryFieldlabel.setVisibility(View.VISIBLE);
                auxiliaryFieldlabel.setText(auxField1.get("label").toString());
                auxiliaryFieldlabel.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("labelColor")));
                auxiliaryFieldvalue.setText(auxField1.get("value").toString());
                auxiliaryFieldvalue.setTextColor(this.passReadingService.getColor(this.passReadingService.getValue("foregroundColor")));

            }
        }

        try
        {
            if(StringUtilities.isValidString(auxiliaryFieldvalue.getText().toString()))
            {
                if(auxiliaryFieldvalue.getText().toString().contains("youtube"))
                {
                    int ind = auxiliaryFieldvalue.getText().toString().indexOf("=");
                    if(ind>0)
                    {
                        String videoId = auxiliaryFieldvalue.getText().toString().split("=")[1];
                    }
                    auxiliaryFieldvalue.setVisibility(View.GONE);
                }
                else if(auxiliaryFieldvalue.getText().toString().contains("http"))
                {
                    auxiliaryFieldvalue.setVisibility(View.GONE);
                  //  Ion.with(feedImageView).placeholder(R.drawable.product_placeholder).load(auxiliaryFieldvalue.getText().toString());
                }
                else
                {
                    strip = this.passReadingService.getImage("strip");
                    if(strip!=null)
                    {
                        this.stripeImageView.setImageBitmap(strip);
                    }
                }
            }
            else
            {
                strip = this.passReadingService.getImage("strip");
                if(strip!=null)
                {
                    this.stripeImageView.setImageBitmap(strip);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Bitmap icon;
        icon = this.passReadingService.getLogo();

        if (icon != null)
        {
            this.thumbnailImageView.setImageBitmap(icon);
        }
        Bitmap barcode;
        try
        {
            barcode = this.passReadingService.getBarcode();
            if(barcode!=null)
            {
                this.barcodeImageView.setImageBitmap(barcode);
                promocode= this.passReadingService.getPromocode();
                this.barcodedetailmessage.setText(promocode);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }


    private void CallOfferPassservices()
    {
     //   org.json.JSONObject object = new JSONObject();
//        String offerId = DataManager.getInstance().getCurrentBasket().getOfferId();
//        if (StringUtilities.isValidString(offerId)) {
        FB_LY_UserOfferService.sharedInstance().getClypOfferPass(new org.json.JSONObject(),offerId, isPMOffer, new FB_LY_UserOfferService.ClypOfferPassCallback()
        {
            @Override
            public void onClypOfferPassCallback(byte[] offerpass, Exception error)
            {
                if (offerpass != null)
                {
                    //  setDataOffer(offerSummary);
                    if(offerpass.length>0)
                    {
                        PkpassReadServices(offerpass);
                    }
                    else
                    {
                       // Utils.showErrorAlert(PassDetail_Activity.this, "Empty Response");
                        enableScreen(true);
                        onBackPressed();
                    }
                }
                else
                {
                  //  Utils.showErrorAlert(PassDetail_Activity.this, "Empty Response");
                    FBUtils.tryHandleTokenExpiry(PassDetail_Activity.this,error);
                    enableScreen(true);
                }
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE. ERROR:: "+error.getMessage());
    }


    @Override
    public void onResponse(byte[] response)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try
        {
            if (response!=null)
            {
                try
                {
                    InputStream input = new ByteArrayInputStream(response);
                    PassSaveService storageService = new PassSaveService(this);
                    File dir = storageService.InflatePkPassInTempDir(input);
                    input.close();
                    t = dir.getPath();
                    //    PkpassReadServices();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }
}
