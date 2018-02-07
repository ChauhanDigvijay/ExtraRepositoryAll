package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentLoginEnrollment;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentLoginPasswordField;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

import static com.fishbowl.LoyaltyTabletApp.Utils.Constants.SPLASH_TIME_OUT;

public class SignInActivity extends FragmentActivity {
    TextView title;
    RelativeLayout toolbar;
    ProgressBarHandler progressBarHandler;
    String fbm;
    private ImageLoader mImageLoader;
    View tagseperator;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarHandler = new ProgressBarHandler(this);
        progressBarHandler.show();

        FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
        fragment.getView().setVisibility(View.VISIBLE);
        FragmentLoginPasswordField fragmentfield = (FragmentLoginPasswordField) getSupportFragmentManager().findFragmentById(R.id.login_password_frag);
        fragmentfield.getView().setVisibility(View.GONE);
        getMobileSettingsfor();
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        titleimage = (NetworkImageView) toolbar.findViewById(R.id.backbutton);
        titlebackground = (NetworkImageView) toolbar.findViewById(R.id.img_Back);
        title = (TextView) toolbar.findViewById(R.id.title_textb);
        // getThemeMobileSettingsServiceSettings();
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inititateui();
            }
        }, SPLASH_TIME_OUT);
    }

//    public void getThemeMobileSettingsServiceSettings() {
//
//        final JSONObject object = new JSONObject();
//
//        FBThemeMobileSettingsService.sharedInstance().getThemeSettings(object, new FBThemeMobileSettingsService.FBThemeSettingsCallback() {
//            @Override
//            public void onThemeSettingsCallback(JSONObject response, final Exception error) {
//
//
//                if(response !=null) {
//
//                    //  FBThemeMobileSettingsService.sharedInstance().initFromJson(response);
//                }else {
//
//                    FBUtils.tryHandleTokenExpiry(SignInActivity.this,error);
//                }
//            }
//        });
//    }

    public void getMobileSettingsfor() {

        fbm = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("PasswordEnable");

        if (fbm != null) {
            if (Integer.valueOf(fbm) == 0 ||fbm == "false") {
                FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
                fragment.getView().setVisibility(View.VISIBLE);
                fragment.passwordField();
                FragmentLoginPasswordField fragmentfield = (FragmentLoginPasswordField) getSupportFragmentManager().findFragmentById(R.id.login_password_frag);
                fragmentfield.getView().setVisibility(View.GONE);
            } else if ( Integer.valueOf(fbm) == 1 ||fbm == "true") {
                FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
                fragment.getView().setVisibility(View.GONE);
                FragmentLoginPasswordField fragmentfield = (FragmentLoginPasswordField) getSupportFragmentManager().findFragmentById(R.id.login_password_frag);
                fragmentfield.getView().setVisibility(View.VISIBLE);
                fragmentfield.passwordField();
            }



        }

        FB_LY_UserService.sharedInstance().getAllStore(new FB_LY_UserService.FBAllStoreCallback() {


            @Override
            public void OnAllStoreCallback(JSONObject response, String error) {

                try {
                    if (error == null && response != null) {


                    }
                } catch (Exception e) {
                }
            }


        });
    }

    public void inititateui() {
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        titleimage = (NetworkImageView) toolbar.findViewById(R.id.backbutton);
        titlebackground = (NetworkImageView) toolbar.findViewById(R.id.img_Back);
        tagseperator = (View) toolbar.findViewById(R.id.tagseperator);

        title = (TextView) toolbar.findViewById(R.id.title_textb);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();


        //header image/color
        final String generalLogoImageUrl =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderImageUrl");
        if(StringUtilities.isValidString(generalLogoImageUrl))
        {
            final String loginleftbgurl = "http://" + generalLogoImageUrl;
            mImageLoader.get(loginleftbgurl, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
            titlebackground.setImageUrl(loginleftbgurl, mImageLoader);
        }
        else if(FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderColor")!=null)
        {
            final String generalHeaderColor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderColor");
            if(StringUtilities.isValidString(generalHeaderColor))
                titlebackground.setBackgroundColor(Color.parseColor(generalHeaderColor));
        }
        //headerlogo image
        final String generalHeaderImageUrl = "http://" +  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");
        mImageLoader.get(generalHeaderImageUrl, ImageLoader.getImageListener(titleimage, R.color.white, R.color.white));
        titleimage.setImageUrl(generalHeaderImageUrl, mImageLoader);

        //headerlogo title
        String companyname = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("CompanyName");;
        if(StringUtilities.isValidString(companyname))
        {
            tagseperator.setVisibility(View.VISIBLE);
            title.setText(companyname);
        }
        else
        {
            tagseperator.setVisibility(View.GONE);
            title.setText(" ");
        }

        FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
        if(fragment!=null) {
            fragment.passwordField();
        }
//        FragmentLoginImage fragment1 = (FragmentLoginImage) getSupportFragmentManager().findFragmentById(R.id.login_image_frag);
//        if(fragment1!=null) {
//            fragment1.newView();
//        }
        progressBarHandler.dismiss();


    }

}


