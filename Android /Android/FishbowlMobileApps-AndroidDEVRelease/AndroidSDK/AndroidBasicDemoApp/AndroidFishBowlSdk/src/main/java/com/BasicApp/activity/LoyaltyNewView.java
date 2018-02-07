package com.BasicApp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.LoyaltyCardModel;
import com.BasicApp.Utils.BarcodeGenerated;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.zxing.BarcodeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.fishbowl.basicmodule.Utils.BarcodeGenerated;

public class LoyaltyNewView extends Activity {
    private android.support.v7.widget.Toolbar toolbar;
    TextView loyaltyname,loyaltyno;
    ImageView bar_image;
    Bitmap bitmap = null;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    TextView userName, loyaltyNumber, datecreated, loyaltytitle, loyaltytopheader, labelmember, labelnumber, labelname;

    ImageView qrCode;
    WebView webView;
    String url5;
    NetworkImageView companyLogo;
    LinearLayout sendEmail, print, loyaltychild2, loyaltychild3, toplayout;
    ProgressBarHandler progressBarHandler;
    RelativeLayout buttonview;
    String emailID;
    private ImageLoader mImageLoader;
    ImageView backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_newloyalty);

        progressBarHandler = new ProgressBarHandler(LoyaltyNewView.this);
        progressBarHandler.show();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // getMemberLoyaltyCard();
        sendEmail = (LinearLayout) findViewById(R.id.sendEmail);
        print = (LinearLayout) findViewById(R.id.sendPrint);
        loyaltychild2 = (LinearLayout) findViewById(R.id.loyaltychild2);
        loyaltychild3 = (LinearLayout) findViewById(R.id.loyaltychild3);
        toplayout = (LinearLayout) findViewById(R.id.toplayout);
        buttonview = (RelativeLayout)findViewById(R.id.buttonlayout);

        companyLogo = (NetworkImageView) findViewById(R.id.companyLogo);
        userName = (TextView) findViewById(R.id.userName);

        loyaltytitle = (TextView) findViewById(R.id.loyaltytitle);
        loyaltytopheader = (TextView) findViewById(R.id.loyaltytopheader);
        loyaltyNumber = (TextView) findViewById(R.id.loyaltyNumber);
        datecreated = (TextView) findViewById(R.id.datecreated);
        labelmember = (TextView) findViewById(R.id.labelmember);
        labelnumber = (TextView) findViewById(R.id.labelnumber);
        final String firstName = FBUserService.sharedInstance().member.firstName;
        String lastName = FBUserService.sharedInstance().member.lastName;
        String loyalty_number = FBUserService.sharedInstance().member.loyalityNo;
       // String created = FBUserService.sharedInstance().member.created;
        emailID = FBUserService.sharedInstance().member.emailID;
        if (StringUtilities.isValidString(firstName)) {
            userName.setText(upperCaseFirst(firstName) + " " + lastName);
        }
        loyaltyNumber.setText(loyalty_number);

//        try {
//            String currentDate = created;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//            Date tempDate = simpleDateFormat.parse(currentDate);
//            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//            datecreated.setText(outputDateFormat.format(tempDate));
//        } catch (ParseException ex) {
//            System.out.println("Parse Exception");
//        }


        qrCode = (ImageView) findViewById(R.id.qrCode);


        try {
            bitmap = BarcodeGenerated.encodeAsBitmap(loyalty_number, BarcodeFormat.CODE_128, 120, 120);
            if (bitmap != null) {
                qrCode.setImageBitmap(bitmap);
            }

        } catch (Exception e) {
        }

        webView = (WebView) findViewById(R.id.webview);


//        ImageView finish = (ImageView) findViewById(R.id.finish);
//        finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                getActivity().finish();
//
//            }
//        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendPassEmail("Loyalty Card", "PLease find attached your loyalty card", emailID);

            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPhotoPrint();


            }
        });

        getMemberLoyaltyCard();
    }



    public void getMemberLoyaltyCard() {
        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMemberLoyaltyCard(object, new FBUserService.FBGetMemberLoyaltyCardCallback() {
            public void onGetMemberLoyaltyCardCallback(JSONObject response, Exception error) {
                {
                    if (response != null && error == null) {

                        try {


                            JSONObject obj = response.getJSONObject("passbook");
                            LoyaltyCardModel model = new LoyaltyCardModel(obj);
                            if (model != null) {

//                                if(StringUtilities.isValidString(model.customLogoURL)) {
//                                    final String url2 = "http://" + model.customLogoURL;
//                                    mImageLoader.get(url2, ImageLoader.getImageListener(companyLogo, R.drawable.logomain, R.drawable.logomain));
//                                    companyLogo.setImageUrl(url2, mImageLoader);
//                                }
                                if (StringUtilities.isValidString(model.barcodeType)) {
                                    if (model.barcodeType.equalsIgnoreCase("BAR")) {
                                        url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FBUserService.sharedInstance().member.loyalityNo + "&BarCodeType=Code128&dpi=100&showText=false";

                                        webView.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                view.loadUrl(url5);
                                                return true;
                                            }
                                        });
                                        webView.setBackgroundColor(Color.TRANSPARENT);
                                        webView.loadUrl(url5);
                                    } else if (model.barcodeType.equalsIgnoreCase("QR")) {

                                        url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FBUserService.sharedInstance().member.loyalityNo + "&BarCodeType=QRCode&dpi=100&showText=false";
                                        webView.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                view.loadUrl(url5);
                                                return true;
                                            }
                                        });
                                        webView.setBackgroundColor(Color.TRANSPARENT);
                                        webView.loadUrl(url5);
                                    }
                                } else {
                                    url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FBUserService.sharedInstance().member.loyalityNo + "&BarCodeType=QRCode&dpi=100&showText=false";
                                    webView.setWebViewClient(new WebViewClient() {
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url5);
                                            return true;
                                        }
                                    });
                                    webView.setBackgroundColor(Color.TRANSPARENT);
                                    webView.loadUrl(url5);
                                }

                                if (StringUtilities.isValidString(model.frontHeaderText)) {
                                    loyaltytitle.setText(model.frontHeaderText);
                                }

//                                if(StringUtilities.isValidString(model.frontHeaderFontSize)) {
//                                }    userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontHeaderFontSize));
//

                                if (StringUtilities.isValidString(model.frontHeaderColor)) {
                                    loyaltytitle.setTextColor(Color.parseColor(model.frontHeaderColor));
                                }


                                if (StringUtilities.isValidString(model.frontHeaderFontBold)) {
                                    if (!model.frontHeaderFontBold.equalsIgnoreCase("normal"))
                                        loyaltytitle.setTypeface(null, Typeface.BOLD);
                                }


                                if (StringUtilities.isValidString(model.frontHeaderFontItalic)) {
                                    if (!model.frontHeaderFontItalic.equalsIgnoreCase("normal"))
                                        loyaltytitle.setTypeface(null, Typeface.ITALIC);
                                }

                                if (StringUtilities.isValidString(model.frontHeaderFontUnderline)) {
                                    loyaltytitle.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                }

//username

                                if (StringUtilities.isValidString(model.frontBodyText1Color)) {
                                    userName.setTextColor(Color.parseColor(model.frontBodyText1Color));
                                }
                                if (StringUtilities.isValidString(model.frontBodyText1FontSize)) {
                                    if (model.frontBodyText1FontSize.contains("px")) {

                                        String[] parts = model.frontBodyText1FontSize.split("px");
                                        String part1 = parts[0];
                                        userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(part1));
                                    } else {
                                        userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontBodyText1FontSize));
                                    }
                                }
                                if (StringUtilities.isValidString(model.frontBodyText1FontBold)) {
                                    if (!model.frontBodyText1FontBold.equalsIgnoreCase("normal"))
                                        userName.setTypeface(null, Typeface.BOLD);
                                }

                                if (StringUtilities.isValidString(model.frontBodyText1FontItalic)) {
                                    if (!model.frontBodyText1FontItalic.equalsIgnoreCase("normal"))
                                        userName.setTypeface(null, Typeface.ITALIC);
                                }

                                if (StringUtilities.isValidString(model.frontBodyText1FontUnderline)) {
                                    userName.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                }

//
                                if (StringUtilities.isValidString(model.frontBodyText2Color)) {
                                    labelmember.setTextColor(Color.parseColor(model.frontBodyText2Color));
                                    datecreated.setTextColor(Color.parseColor(model.frontBodyText2Color));
                                }
                                if (StringUtilities.isValidString(model.frontBodyText2FontSize)) {
                                    if (model.frontBodyText2FontSize.contains("px")) {

                                        String[] parts = model.frontBodyText2FontSize.split("px");
                                        String part1 = parts[0];

                                        labelmember.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(part1));
                                        datecreated.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(part1));
                                    } else {

                                        labelmember.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontBodyText2FontSize));
                                        datecreated.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontBodyText2FontSize));
                                    }
                                }

                                if (StringUtilities.isValidString(model.frontBodyText2FontBold)) {
                                    if (!model.frontBodyText2FontBold.equalsIgnoreCase("normal")) {
                                        //  userName.setTypeface(null, Typeface.BOLD);
                                        labelmember.setTypeface(null, Typeface.BOLD);
                                        datecreated.setTypeface(null, Typeface.BOLD);
                                    }
                                }
//


                                if (StringUtilities.isValidString(model.frontBodyText2FontItalic)) {
                                    if (!model.frontBodyText2FontItalic.equalsIgnoreCase("normal")) {
                                        //  userName.setTypeface(null, Typeface.BOLD);
                                        //  userName.setTypeface(null, Typeface.ITALIC);
                                        labelmember.setTypeface(null, Typeface.ITALIC);
                                        datecreated.setTypeface(null, Typeface.ITALIC);
                                    }
                                }


                                if (StringUtilities.isValidString(model.frontBodyText2FontUnderline)) {
                                    //  userName.setTypeface(null, Typeface.BOLD);
                                    //  userName.setTypeface(null, Typeface.ITALIC);
                                    // userName.setPaintFlags(userName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                                    labelmember.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    datecreated.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                }


                                if (StringUtilities.isValidString(model.frontBodyText3Color)) {
                                    labelnumber.setTextColor(Color.parseColor(model.frontBodyText3Color));
                                }
                                if (StringUtilities.isValidString(model.frontBodyText3FontSize)) {

                                    if (model.frontBodyText3FontSize.contains("px")) {

                                        String[] parts = model.frontBodyText3FontSize.split("px");
                                        String part1 = parts[0];
                                        labelnumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(part1));
                                    } else {
                                        labelnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontBodyText3FontSize));

                                    }
                                }

                                if (StringUtilities.isValidString(model.frontBodyText3FontBold)) {
                                    if (!model.frontBodyText3FontBold.equalsIgnoreCase("normal")) {
                                        labelnumber.setTypeface(null, Typeface.BOLD);
                                    }
                                }

                                if (StringUtilities.isValidString(model.frontBodyText3FontItalic)) {
                                    if (!model.frontBodyText3FontItalic.equalsIgnoreCase("normal"))
                                        labelnumber.setTypeface(null, Typeface.ITALIC);
                                }

                                if (StringUtilities.isValidString(model.frontBodyText3FontUnderline)) {
                                    labelnumber.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                }


                                if (StringUtilities.isValidString(model.frontBodyText4Color)) {
                                    loyaltyNumber.setTextColor(Color.parseColor(model.frontBodyText4Color));
                                }


                                if (StringUtilities.isValidString(model.frontBodyText4FontSize)) {

                                    if (model.frontBodyText4FontSize.contains("px")) {

                                        String[] parts = model.frontBodyText4FontSize.split("px");
                                        String part1 = parts[0];
                                        loyaltyNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(part1));
                                    } else {
                                        loyaltyNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(model.frontBodyText4FontSize));
                                    }
                                }


                                if (StringUtilities.isValidString(model.frontBodyText4FontBold)) {
                                    if (!model.frontBodyText4FontBold.equalsIgnoreCase("normal"))
                                        loyaltyNumber.setTypeface(null, Typeface.BOLD);
                                }

                                if (StringUtilities.isValidString(model.frontBodyText4FontItalic)) {
                                    if (!model.frontBodyText4FontItalic.equalsIgnoreCase("normal"))
                                        loyaltyNumber.setTypeface(null, Typeface.ITALIC);
                                }

                                if (StringUtilities.isValidString(model.frontBodyText4FontUnderline)) {
                                    loyaltyNumber.setPaintFlags(userName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                }


                                if (StringUtilities.isValidString(model.frontBackgroundColor)) {
                                    loyaltychild3.setBackgroundColor(Color.parseColor(model.frontBackgroundColor));
                                }


                                progressBarHandler.dismiss();
                                //  loyaltyNumber.setTypeface(null, Typeface.BOLD);


//                                loyaltytopheader.setTextColor(Color.parseColor("#" + "a3ff00"));
//                                loyaltytopheader.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
//                                loyaltytopheader.setTypeface(null, Typeface.BOLD);


                                // userName.setTypeface(textview.getTypeface(), Typeface.DEFAULT_BOLD);


                            }

                        } catch (Exception e) {
                            progressBarHandler.dismiss();
                        }
                    }
                }
            }
        });
    }


    public void sendPassEmail(String subject, String body, String email) {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(email);
            object.put("toAddress", jsonArray);
            object.put("subject", subject);
            object.put("body", "hi hello");
            object.put("themeId", "0");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        FBUserService.sharedInstance().postsendPassLoyalty(object, new FBUserService.FBSendPassEmail() {
            public void onSendPassEmaiCallback(JSONObject response, Exception error) {
                if (response != null && error == null) {

                    if (response.has("successFlag")) {
                        try {
                            if (response.getString("successFlag").equalsIgnoreCase("true")) {


                                progressBarHandler.dismiss();
                                showalertoffer1();

                            } else {


                                //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
                                progressBarHandler.dismiss();
                                showalertoffer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //  FBUtils.tryHandleTokenExpiry(getActivity(), error);
                        progressBarHandler.dismiss();
                        showalertoffer();
                    }
                } else {
                    //  FBUtils.tryHandleTokenExpiry( getActivity(), error);
                    progressBarHandler.dismiss();
                    showalertoffer();
                }
            }
        });


    }

    public void showalertoffer() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoyaltyNewView.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("Email has been Failed");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showalertoffer1() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoyaltyNewView.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("Your loyalty card has been sent to your email address" + " " + emailID);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void doPhotoPrint() {

        PrintHelper photoPrinter = new PrintHelper(LoyaltyNewView.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       // ImageView image = (ImageView) findViewById(R.id.bt3);

    //    FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.loyatlybg);

      //  image.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        print.setVisibility(View.GONE);


        View v1 = LoyaltyNewView.this.getWindow().getDecorView().getRootView();
//        frame.setBackgroundColor(Color.WHITE);
//        // v1.setBackgroundColor(Color.WHITE);

        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        if (StringUtilities.isValidString(FBUserService.sharedInstance().member.firstName)) {
            photoPrinter.printBitmap(FBUserService.sharedInstance().member.firstName + "_" + "LoyaltyCard", bitmap);
        } else {
            photoPrinter.printBitmap("LoyaltyCard", bitmap);
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        if (sendEmail != null && print != null) {
            sendEmail.setVisibility(View.VISIBLE);
            print.setVisibility(View.VISIBLE);
        }
    }



    public static String upperCaseFirst(String value) {
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }


    public void onCustomBackPressed() {
        LoyaltyNewView.this.finish();
    }
}
