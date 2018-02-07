package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyCardModel;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.BarcodeGenerated;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;
import com.fishbowl.loyaltymodule.Utils.ExifUtils;
import com.fishbowl.loyaltymodule.Utils.StringUtilities;
import com.google.zxing.BarcodeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by schaudhary_ic on 11-Nov-16.
 */

public class FragmentLoyaltyCard extends Fragment {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    TextView userName, loyaltyNumber, datecreated, loyaltytitle, loyaltytopheader, labelmember, labelnumber, labelname;
    Bitmap bitmap = null;
    ImageView qrCode;
    WebView webView;
    String url5;
    NetworkImageView companyLogo;
    LinearLayout sendEmail, print, loyaltychild2, loyaltychild3, toplayout;
    ProgressBarHandler progressBarHandler;
    RelativeLayout buttonview;
    String emailID;
    private ImageLoader mImageLoader;

    public static String upperCaseFirst(String value) {
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loyalty_card, container, false);
        progressBarHandler = new ProgressBarHandler(getContext());
        progressBarHandler.show();
        getMemberLoyaltyCard();
        sendEmail = (LinearLayout) v.findViewById(R.id.sendEmail);
        print = (LinearLayout) v.findViewById(R.id.sendPrint);
        loyaltychild2 = (LinearLayout) v.findViewById(R.id.loyaltychild2);
        loyaltychild3 = (LinearLayout) v.findViewById(R.id.loyaltychild3);
        toplayout = (LinearLayout) v.findViewById(R.id.toplayout);
        buttonview = (RelativeLayout) v.findViewById(R.id.buttonlayout);

        companyLogo = (NetworkImageView) v.findViewById(R.id.companyLogo);
        userName = (TextView) v.findViewById(R.id.userName);

        loyaltytitle = (TextView) v.findViewById(R.id.loyaltytitle);
        loyaltytopheader = (TextView) v.findViewById(R.id.loyaltytopheader);
        loyaltyNumber = (TextView) v.findViewById(R.id.loyaltyNumber);
        datecreated = (TextView) v.findViewById(R.id.datecreated);
        labelmember = (TextView) v.findViewById(R.id.labelmember);
        labelnumber = (TextView) v.findViewById(R.id.labelnumber);
        final String firstName = FB_LY_UserService.sharedInstance().member.firstName;
        String lastName = FB_LY_UserService.sharedInstance().member.lastName;
        String loyalty_number = FB_LY_UserService.sharedInstance().member.loyalityNo;
        String created = FB_LY_UserService.sharedInstance().member.created;
        emailID = FB_LY_UserService.sharedInstance().member.emailID;
        if (StringUtilities.isValidString(firstName)) {
            userName.setText(upperCaseFirst(firstName) + " " + lastName);
        }
        if (StringUtilities.isValidString(loyalty_number)) {
            loyaltyNumber.setText(loyalty_number);
        }
        try {
            if(StringUtilities.isValidString(created)) {
                String currentDate = created;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date tempDate = simpleDateFormat.parse(currentDate);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                datecreated.setText(outputDateFormat.format(tempDate));
            }
        } catch (ParseException ex) {
            System.out.println("Parse Exception");
        }


        qrCode = (ImageView) v.findViewById(R.id.qrCode);


        try {
            bitmap = BarcodeGenerated.encodeAsBitmap(loyalty_number, BarcodeFormat.CODE_128, 120, 120);
            if (bitmap != null) {
                qrCode.setImageBitmap(bitmap);
            }

        } catch (Exception e) {
        }

        webView = (WebView) v.findViewById(R.id.webview);


        ImageView finish = (ImageView) v.findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                getActivity().finish();

            }
        });

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

        return v;
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


        FB_LY_UserService.sharedInstance().postsendPassLoyalty(object, new FB_LY_UserService.FBSendPassEmail() {
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void doPhotoPrint() {

        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ImageView image = (ImageView) getActivity().findViewById(R.id.bt3);

        FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.loyatlybg);

        image.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        print.setVisibility(View.GONE);


        View v1 = getActivity().getWindow().getDecorView().getRootView();
        frame.setBackgroundColor(Color.WHITE);
        // v1.setBackgroundColor(Color.WHITE);

        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        if (StringUtilities.isValidString(FB_LY_UserService.sharedInstance().member.firstName)) {
            photoPrinter.printBitmap(FB_LY_UserService.sharedInstance().member.firstName + "_" + "LoyaltyCard", bitmap);
        } else {
            photoPrinter.printBitmap("LoyaltyCard", bitmap);
        }


    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + now + ".jpg";
            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());


            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        // intent.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);

    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {

            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                // image naming and path  to include sd card  appending name you choose for file
                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + now + ".jpg";

                // create bitmap screen capture


                View v1 = loyaltychild2;

                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

//                Matrix matrix = new Matrix();
//
//                if(bm.getWidth() > bm.getHeight()) {
//                    matrix.postRotate(90);
//                }else{}
//                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache(), 0, 0, v1.getDrawingCache().getWidth(), v1.getDrawingCache().getHeight(), matrix, true);


                //    Bitmap bitmap=  Bitmap.createScaledBitmap(v1.getDrawingCache())
                v1.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();

                openScreenshot(imageFile);
            } catch (Throwable e) {
                // Several error may come out with file handling or OOM
                e.printStackTrace();
            }
        }
    }


    public void getMemberLoyaltyCard() {
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getMemberLoyaltyCard(object, new FB_LY_UserService.FBGetMemberLoyaltyCardCallback() {
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
                                        url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FB_LY_UserService.sharedInstance().member.loyalityNo + "&BarCodeType=Code128&dpi=180&showText=false";

                                        webView.setWebViewClient(new WebViewClient() {
                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                view.loadUrl(url5);
                                                return true;
                                            }
                                        });
                                        webView.setBackgroundColor(Color.TRANSPARENT);
                                        webView.loadUrl(url5);
                                    } else if (model.barcodeType.equalsIgnoreCase("QR")) {

                                        url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FB_LY_UserService.sharedInstance().member.loyalityNo + "&BarCodeType=QRCode&dpi=150&showText=false";
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
                                    url5 = "https://promotionsmanager.fishbowl.com/PromotionsManager/Handlers/BarCodeGen.ashx?valueToEncode=" + FB_LY_UserService.sharedInstance().member.loyalityNo + "&BarCodeType=QRCode&dpi=150&showText=false";
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


    public void sendPassEmail() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getsendPassEmail(object, new FB_LY_UserService.FBSendPassEmail() {
            public void onSendPassEmaiCallback(JSONObject response, Exception error) {
                if (response != null && error == null) {

                    if (response.has("successFlag")) {
                        try {
                            if (response.getString("successFlag").equalsIgnoreCase("true")) {


                                progressBarHandler.dismiss();
                                FBUtils.showAlert(getContext(), "Email send Successfully");
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

    public void decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b = ExifUtils.rotateBitmap(filePath, b1);

        // image.setImageBitmap(bitmap);
    }


    public void showalertoffer() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("Email has been Failed");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showalertoffer1() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setMessage("Your loyalty card has been sent to your email address" + " " + emailID);
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().companyLogoImageUrl;
        mImageLoader.get(url2, ImageLoader.getImageListener(companyLogo, R.drawable.logomain, R.drawable.logomain));
        companyLogo.setImageUrl(url2, mImageLoader);


    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView image = (ImageView) getActivity().findViewById(R.id.bt3);
        image.setVisibility(View.VISIBLE);
        FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.loyatlybg);
        frame.setBackgroundColor(Color.parseColor("#80000000"));
        if (sendEmail != null && print != null) {
            sendEmail.setVisibility(View.VISIBLE);
            print.setVisibility(View.VISIBLE);
        }
    }
}
