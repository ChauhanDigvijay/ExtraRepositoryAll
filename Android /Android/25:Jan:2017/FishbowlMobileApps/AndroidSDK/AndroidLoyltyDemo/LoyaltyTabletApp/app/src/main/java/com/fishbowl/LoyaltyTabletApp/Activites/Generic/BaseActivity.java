package com.fishbowl.LoyaltyTabletApp.Activites.Generic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.R;



/**
 * Created by Nauman Afzaal on 30/04/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    public boolean isBackButtonEnabled;
    public boolean isSlideDown;
    protected boolean isShowBasketIcon;
    protected boolean isAnimated = true;
    MenuItem settingMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolBar(true);
        isShowBasketIcon = true;

        //initToast();
        //  LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_AUTH_TOKEN_FAILURE));
    }
/*
     public void initToast(){
        String screenName = getClass().getSimpleName();
        ToastManager.sharedInstance().initWithContext(this);
        ToastManager.sharedInstance().setOnOff(true);
        ToastManager.sharedInstance().show(screenName);
    }
*/

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
        return true;
    }

    protected void updateSettingMenuTitle() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    return true;

}
    @Override
    public void onBackPressed()
    {

        navigateUp();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //AnalyticsManager.getInstance().startReporting(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //AnalyticsManager.getInstance().stopReporting(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        showOrHideBasketIcon();
    }

    protected void navigateUp()
    {
//        if (isBackButtonEnabled)
//        {
//            Intent intent = NavUtils.getParentActivityIntent(this);
//            if (intent != null)
//            {
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//            finish();
//            if (isAnimated)
//            {
//                if (isSlideDown)
//                {
//                    overridePendingTransition(R.anim.slide_no_anim, R.anim.slide_down_activity);
//                }
//                else
//                {
//                    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
//                }
//            }
//        }
    }

    public void moveBasketToTop(){
//        if (isShowBasketIcon)
//        {
//            BasketFlagViewManager.getInstance().removeBasketFlag();
//            BasketFlagViewManager.getInstance().showOrResetBasketFlag(this, true);
//        }
//        else
//        {
//            BasketFlagViewManager.getInstance().removeBasketFlag();
//        }
    }
    public void resetBasketUI(){
//        if (isShowBasketIcon)
//        {
//            BasketFlagViewManager.getInstance().removeBasketFlag();
//            BasketFlagViewManager.getInstance().showOrResetBasketFlag(this, false);
//        }
//        else
//        {
//            BasketFlagViewManager.getInstance().removeBasketFlag();
//        }
    }
    private void showOrHideBasketIcon()
    {
//        if (isShowBasketIcon)
//        {
//            BasketFlagViewManager.getInstance().showBasketFlag(this);
//        }
//        else
//        {
//            BasketFlagViewManager.getInstance().removeBasketFlag();
//        }
    }

    public void enableScreen(boolean isEnabled)
    {
        isBackButtonEnabled = isEnabled;
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

    protected void handleBroadCastReceiver(Intent intent)
    {
        // Parent classes will override this method and handle it according to intent type.
        // Using one broadcast receiver for multiple intents.
    }

    protected void handleAuthTokenFailure()
    {
        //Auth token failure related handling goes here.
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }



    @Override
    public Intent getIntent()
    {
        Intent intent = super.getIntent();
        if(intent == null)
        {
            intent = new Intent();
        }
        return intent;
    }
}
