package com.olo.jambajuice.Activites.Generic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.Settings.SettingsActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

import static com.olo.jambajuice.Utils.Constants.BROADCAST_AUTH_TOKEN_FAILURE;

/**
 * Created by Nauman Afzaal on 30/04/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public boolean isBackButtonEnabled;
    public boolean isSlideDown;
    protected Toolbar toolbar;
    protected boolean isShowBasketIcon;
    protected boolean isAnimated = true;
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_AUTH_TOKEN_FAILURE)) {
                handleAuthTokenFailure();
            } else {
                handleBroadCastReceiver(intent);
            }

        }
    };
    MenuItem settingMenuItem;
/*
     public void initToast(){
        String screenName = getClass().getSimpleName();
        ToastManager.sharedInstance().initWithContext(this);
        ToastManager.sharedInstance().setOnOff(true);
        ToastManager.sharedInstance().show(screenName);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolBar(true);
        isShowBasketIcon = true;

        //initToast();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_AUTH_TOKEN_FAILURE));
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

    protected void setBackButton(Boolean isBackButton, Boolean isWhite) {
        if (toolbar != null) {
            ImageButton back = (ImageButton) toolbar.findViewById(R.id.back);
            if (isBackButton) {
                if (isWhite) {
                    back.setImageResource(R.drawable.exp_up_white);
                } else {
                    back.setImageResource(R.drawable.exp_up_gray);
                }
            } else {
                if (isWhite) {
                    back.setImageResource(R.drawable.close_white);
                } else {
                    back.setImageResource(R.drawable.close_gray);
                }
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

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
        inflater.inflate(R.menu.menu_settings, menu);
        settingMenuItem = menu.findItem(R.id.action_settings);
        updateSettingMenuTitle();
        if (toolbar != null) {
            toolbar.getMenu().clear();
        }
        return true;
    }

    protected void updateSettingMenuTitle() {
        if (settingMenuItem != null) {
            if (UserService.isUserAuthenticated()) {
                settingMenuItem.setTitle("Settings");
            } else {
                settingMenuItem.setTitle("Terms & Feedback");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case android.R.id.home:
                navigateUp();
                return true;
            case R.id.action_settings:
//                if (UserService.isUserAuthenticated())
//                {
                TransitionManager.slideUp(this, SettingsActivity.class);
//                }
//                else
//                {
//                    TransitionManager.slideUp(this, TermsAndPrivacyActivity.class);
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        trackButtonWithName("Back");
        navigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnalyticsManager.getInstance().startReporting(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AnalyticsManager.getInstance().stopReporting(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrHideBasketIcon();
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

    public void moveBasketToTop() {
        if (isShowBasketIcon) {
            BasketFlagViewManager.getInstance().removeBasketFlag();
            BasketFlagViewManager.getInstance().showOrResetBasketFlag(this, true);
        } else {
            BasketFlagViewManager.getInstance().removeBasketFlag();
        }
    }

    public void resetBasketUI() {
        if (isShowBasketIcon) {
            BasketFlagViewManager.getInstance().removeBasketFlag();
            BasketFlagViewManager.getInstance().showOrResetBasketFlag(this, false);
        } else {
            BasketFlagViewManager.getInstance().removeBasketFlag();
        }
    }

    private void showOrHideBasketIcon() {
        if (isShowBasketIcon) {
            BasketFlagViewManager.getInstance().showBasketFlag(this);
        } else {
            BasketFlagViewManager.getInstance().removeBasketFlag();
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
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).unregisterReceiver(broadcastReceiver);
    }

    protected void trackButton(View view) {
        String title = "[BUTTON HAS NO TITLE]";
        if (view instanceof Button) {
            Button button = (Button) view;
            if (button.getText() != null) {
                title = button.getText().toString();
            }
        }
        String screenName = getClass().getSimpleName();
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "button_press", screenName + "::" + title, 1, screenName);
    }

    protected void trackButtonWithName(String title) {
        String screenName = getClass().getSimpleName();
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "button_press", screenName + "::" + title, 1, screenName);
    }

    protected void trackUXEvent(String action, String label) {
        String screenName = getClass().getSimpleName();
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, action, label, 0, screenName);
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
