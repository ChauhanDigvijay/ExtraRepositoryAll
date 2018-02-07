package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn;

/**
 * Created by digvijaychauhan on 21/06/17.
 */
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.Generic.BaseActivity;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentLoginEnrollment;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentLoginPasswordField;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

public class SignInActivityCommon extends BaseActivity {
    TextView title;
    RelativeLayout toolbar;
    ProgressBarHandler progressBarHandler;
    String fbm;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signiin_common);
        setUpToolBar(true, true);
        setToolbarConfig(true, true);
        setUpView();
        progressBarHandler = new ProgressBarHandler(this);

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private void setUpPhoneInView() {
        try {
            FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
            fragment.getView().setVisibility(View.VISIBLE);
            fragment.passwordField();
            FragmentLoginPasswordField fragmentfield = (FragmentLoginPasswordField) getSupportFragmentManager().findFragmentById(R.id.login_password_frag);
            fragmentfield.getView().setVisibility(View.GONE);
        }
        catch (Exception e) {
            e.printStackTrace();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();

        }

    }

    private void setUpPasswordEInView() {
        try {
            FragmentLoginEnrollment fragment = (FragmentLoginEnrollment) getSupportFragmentManager().findFragmentById(R.id.login_key_frag);
            fragment.getView().setVisibility(View.GONE);
            FragmentLoginPasswordField fragmentfield = (FragmentLoginPasswordField) getSupportFragmentManager().findFragmentById(R.id.login_password_frag);
            fragmentfield.getView().setVisibility(View.VISIBLE);
            fragmentfield.passwordField();
        }
        catch (Exception e) {
            e.printStackTrace();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();

        }

    }


    public void setUpView() {

        fbm = FB_LY_MobileSettingService.sharedInstance().passwordEnable;
        if (fbm != null) {
            if (Integer.valueOf(fbm) == 0 || fbm == "false") {
                setUpPhoneInView();
            } else if (Integer.valueOf(fbm) == 1 || fbm == "true") {
                setUpPasswordEInView();
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


    }
}


