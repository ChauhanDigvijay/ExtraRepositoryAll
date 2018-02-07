package com.identity.arx.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.WriterException;
import com.identity.arx.QrCode.GenerateBarCodeImage;
import com.identity.arx.QrCode.ScanBarActivity;
import com.identity.arx.R;


public class ScanQrActivity extends AppCompatActivity {
    private static final int RESULT_CODE = 0;
    ImageView imageView;
    private TextView resultTextview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_scan_qr);
        this.resultTextview = (TextView) findViewById(R.id.scan_header);
        this.imageView = (ImageView) findViewById(R.id.image);
        try {
            this.imageView.setImageBitmap(GenerateBarCodeImage.TextToImageEncode("AKASH SHARMA"));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void takeBarcode(View view) {
        startActivityForResult(new Intent(this, ScanBarActivity.class), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode != 0) {
        } else {
            if (data != null) {
                this.resultTextview.setText("Barcode Found : " + ((Barcode) data.getParcelableExtra("BARCODE")).displayValue);
                return;
            }
            this.resultTextview.setText("No Barcode Found");
        }
    }
}
