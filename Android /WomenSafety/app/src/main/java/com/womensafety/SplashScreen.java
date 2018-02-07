package com.womensafety;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

import com.womensafety.Common.NetworkUtility;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.splash);
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new C07011(), 300);
    }

    private void checkforPermissions() {
        int permission1 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
        int permission2 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        int permission3 = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS");
        if (permission1 == 0 && permission2 == 0 && permission3 == 0) {
            performNext();
        } else {
            requestAppPermission(permission1, permission2, permission3);
        }
    }

    private void requestAppPermission(int p1, int p2, int p3) {
        Toast.makeText(this, "Please allow all the permissions to continue.", 0).show();
        ArrayList<String> arrList = new ArrayList();
        if (p1 != 0) {
            arrList.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (p2 != 0) {
            arrList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (p3 != 0) {
            arrList.add("android.permission.READ_CONTACTS");
        }
        String[] permissons = new String[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            permissons[i] = (String) arrList.get(i);
        }
        requestPermissions(permissons, 123);
    }

    private void performNext() {
        new Thread(new C07032()).start();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            checkforPermissions();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            System.exit(0);
        }
        return true;
    }

    class C07011 implements Runnable {
        C07011() {
        }

        public void run() {
            try {
                if (VERSION.SDK_INT >= 23) {
                    SplashScreen.this.checkforPermissions();
                } else {
                    SplashScreen.this.performNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C07032 implements Runnable {

        C07032() {
        }

        public void run() {
            SplashScreen.this.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SplashScreen.this.runOnUiThread(new C07021());
        }

        class C07021 implements Runnable {
            C07021() {
            }

            public void run() {
                if (NetworkUtility.isGooglePlayServicesAvailable(SplashScreen.this)) {
                    SplashScreen.this.finish();
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, HelpMeScreen.class));
                    SplashScreen.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    return;
                }
                Toast.makeText(SplashScreen.this, "Google play services not installed.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
