package com.fishbowl.LoyaltyTabletApp.Activites.Generic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;

import org.json.JSONObject;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public boolean isBackButtonEnabled;
    public boolean isSlideDown;
    protected Toolbar toolbar;
    protected boolean isShowBasketIcon;
    protected boolean isAnimated = true;
    MenuItem settingMenuItem;
    LinearLayout layout_button, layout_logout, layout_loyalty;
    TextView title;

    ProgressBarHandler progressBarHandler;
    String fbm;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide statusbar of Android
        // could also be done later

//        getSupportActionBar().setHomeButtonEnabled(true);
//        setUpToolBar(true);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

     }

    protected void setUpToolBar(boolean isBackButtonEnabled) {
        setUpToolBar(isBackButtonEnabled, false);
    }

    protected void setUpToolBar(boolean isBackButtonEnabled, boolean isSlideDown) {
        this.isBackButtonEnabled = isBackButtonEnabled;
        this.isSlideDown = isSlideDown;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
            } catch (Exception e) {
                // Android 4.2.2, especially on Samsung devices, crash here. See bugreport:
                // https://code.google.com/p/android/issues/detail?id=78377
                e.printStackTrace();
            }
        }
    }
//
//    protected void setBackButton(Boolean isBackButton, Boolean isWhite) {
//        if (toolbar != null) {
//            ImageButton back = (ImageButton) toolbar.findViewById(R.id.back);
//            if (isBackButton) {
//                if (isWhite) {
//                    back.setImageResource(R.drawable.abback1);
//                } else {
//                    back.setImageResource(R.drawable.abback1);
//                }
//            } else {
//                if (isWhite) {
//                    back.setImageResource(R.drawable.abback1);
//                } else {
//                    back.setImageResource(R.drawable.abback1);
//                }
//            }
//            back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onBackPressed();
//                }
//            });
//
//        }
//    }


    protected void setToolbarConfig(Boolean isBackButton, Boolean isWhite) {
        if (toolbar != null) {

            titleimage = (NetworkImageView) toolbar.findViewById(R.id.backbutton);
            titlebackground = (NetworkImageView) toolbar.findViewById(R.id.img_Back);
            title = (TextView) toolbar.findViewById(R.id.title_textb);
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().companyLogoImageUrl;
            mImageLoader.get(url2, ImageLoader.getImageListener(titleimage, R.color.white, R.color.white));
            titleimage.setImageUrl(url2, mImageLoader);
            mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
            final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().loginRightTopImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
            titlebackground.setImageUrl(url, mImageLoader);
            String companyname = FB_LY_MobileSettingService.sharedInstance().companyName;
            title.setText(companyname);
            layout_button = (LinearLayout) toolbar.findViewById(R.id.layout_button);
            layout_button.setOnClickListener(BaseActivity.this);
            layout_logout = (LinearLayout) toolbar.findViewById(R.id.layout_logout);
            layout_logout.setOnClickListener(BaseActivity.this);
            layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
            layout_loyalty.setOnClickListener(BaseActivity.this);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_logout:
                logout();
                break;
            case R.id.layout_button:
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                break;
            case R.id.layout_loyalty:
                Intent ii = new Intent(this, MenuActivity.class);
                startActivity(ii);
                break;
        }

    }



    protected void setTitle(String title, int color) {
        if (toolbar != null) {
            TextView textView = (TextView) toolbar.findViewById(R.id.title);
            textView.setText(title);
            textView.setTextColor(color);
        }
    }

    protected void setTitle(String title) {
        setTitle(title, getResources().getColor(R.color.toolbar_text));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_settings, menu);
//        settingMenuItem = menu.findItem(R.id.action_settings);
//        updateSettingMenuTitle();
//        if (toolbar != null) {
//            toolbar.getMenu().clear();
//        }
        return true;
    }

    protected void updateSettingMenuTitle() {
        if (settingMenuItem != null) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case android.R.id.home:
                navigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void navigateUp() {
        if (isBackButtonEnabled) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            finish();
            if (isAnimated) {
                if (isSlideDown) {
                    overridePendingTransition(R.anim.slide_no_anim, R.anim.slide_down_activity);
                } else {
                    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                }
            }
        }
    }



    public void enableScreen(boolean isEnabled) {
        isBackButtonEnabled = isEnabled;
        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    protected void handleBroadCastReceiver(Intent intent) {
        // Parent classes will override this method and handle it according to intent type.
        // Using one broadcast receiver for multiple intents.
    }

    protected void handleAuthTokenFailure() {
        //Auth token failure related handling goes here.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle("Logout ");
        alertDialog.setMessage("Press ok to Logout ");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put("Application", "mobile");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle extras = new Bundle();
                Intent i = new Intent(BaseActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(extras);
                startActivity(i);
                if (RewardSummary.rewardList != null) {
                    if (RewardSummary.rewardList.size() > 0) {
                        RewardSummary.rewardList.clear();

                    }
                }
                if (OfferSummary.offerList != null) {
                    if (OfferSummary.offerList.size() > 0) {

                        OfferSummary.offerList.clear();
                    }
                }
                BaseActivity.this.finish();
            }
        });
        alertDialog.show();
    }




    @Override
    public Intent getIntent() {
        Intent intent = super.getIntent();
        if (intent == null) {
            intent = new Intent();
        }
        return intent;
    }
}
