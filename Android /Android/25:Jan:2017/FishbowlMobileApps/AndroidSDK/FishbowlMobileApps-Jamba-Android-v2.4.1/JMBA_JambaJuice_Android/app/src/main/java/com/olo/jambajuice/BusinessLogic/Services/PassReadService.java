package com.olo.jambajuice.BusinessLogic.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.olo.jambajuice.Utils.BarcodeGenerated;
import com.olo.jambajuice.Utils.FileReader;
import com.olo.jambajuice.Utils.ImageUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by digvijaychauhan on 28/04/16.
 */


public class PassReadService {

    private String path;
    private FileReader fileReader;
    private JSONParser parser;
    private JSONObject root;
    private Context context;


    public PassReadService(String path, Context context) throws ParseException, IOException {
        this.path = path;
        this.context = context;
        this.fileReader = new FileReader();
        this.parser = new JSONParser();
        this.root = (JSONObject) this.parser.parse(this.fileReader.readTextFile(new File(path, "pass.json")));
    }

    public static int getColor(String rgb) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(rgb);

        if (m.matches()) {
            return Color.rgb(Integer.valueOf(m.group(1)),  // r
                    Integer.valueOf(m.group(2)),  // g
                    Integer.valueOf(m.group(3))); // b
        }

        return 0;
    }

    public Bitmap getBackground() {

        File blurredBitmapFile = new File(this.path, "background-blur.png");
        byte[] bitmapBytes;
        Bitmap bitmap;

        try {
            if (blurredBitmapFile.exists()) {
                bitmapBytes = this.fileReader.readBinaryFile(new File(this.path, "background-blur.png"));
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                return bitmap;
            } else {
                File background = new File(this.path, "background@2x.png");
                if (!background.exists()) {
                    background = new File(this.path, "background.png");
                }
                bitmapBytes = this.fileReader.readBinaryFile(background);
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                Bitmap blurred = ImageUtils.fastblur(bitmap, 10);
                FileOutputStream out = new FileOutputStream(blurredBitmapFile);
                blurred.compress(CompressFormat.PNG, 0, out);
                out.close();
                return blurred;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getLogo() {
        return getImage("logo");
    }

    public Bitmap getImage(String name) {
        byte[] bitmapBytes;
        try {
            File image = new File(this.path, name + "@2x.png");
            if (!image.exists()) {
                image = new File(this.path, name + ".png");
            }
            bitmapBytes = this.fileReader.readBinaryFile(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    public Bitmap getBarcode() throws WriterException, IOException {
        File barcodeFile = new File(this.path, "barcode.png");

        if (!barcodeFile.exists()) {
            JSONObject barcodeJson = this.getJSONObject("barcode");
            String checkbar = barcodeJson.get("format").toString();
            if (!checkbar.equalsIgnoreCase("PKBarcodeFormatNo QR Code Required")) {
                Bitmap barcodeAux = BarcodeGenerated.encodeAsBitmap(barcodeJson.get("altText").toString(), BarcodeFormat.QR_CODE, 300, 300);
                //  Bitmap barcodeAux = new BarcodeEncoder(this.context).getBitmap(barcodeJson.get("message").toString(), barcodeJson.get("format").toString());
                FileOutputStream out = new FileOutputStream(barcodeFile);
                barcodeAux.compress(CompressFormat.PNG, 0, out);
                out.close();
            }
        }

        Bitmap barcode = BitmapFactory.decodeFile(barcodeFile.getPath());
        return barcode;
    }


    public String getPromocode() throws WriterException, IOException {


        JSONObject barcodeJson = this.getJSONObject("barcode");
        String altText = barcodeJson.get("altText").toString();

        return altText;
    }

    public String getValue(String key) {
        return this.root.get(key).toString();
    }

    public JSONObject getJSONObject(String key) {
        return (JSONObject) this.root.get(key);
    }

    public Bitmap getThumbnail() {
        Bitmap thumbnail = getImage("thumbnail");

        if (thumbnail == null) {
            thumbnail = getImage("background");
        }

        return thumbnail;
    }
}
