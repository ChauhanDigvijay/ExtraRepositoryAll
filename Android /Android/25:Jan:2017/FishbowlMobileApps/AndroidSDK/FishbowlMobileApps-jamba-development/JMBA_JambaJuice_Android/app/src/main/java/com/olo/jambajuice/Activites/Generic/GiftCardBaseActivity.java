package com.olo.jambajuice.Activites.Generic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.olo.jambajuice.R;


/**
 * Created by vthink on 17/08/16.
 */
public abstract class GiftCardBaseActivity extends BaseActivity {

    public void loaderText(String text){
        TextView proBarText = (TextView)findViewById(R.id.proBarText);
        proBarText.setText(text);
    }

//    public void enableScreen(boolean isEnabled)
//    {
//        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
//        if (screenDisableView != null)
//        {
//            if (!isEnabled)
//            {
//                screenDisableView.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                screenDisableView.setVisibility(View.GONE);
//            }
//        }
//    }

//    @Override
//    public void onBackPressed()
//    {
//        navigateUp();
//    }
//
//    protected void navigateUp()
//    {
//            Intent intent = NavUtils.getParentActivityIntent(this);
//            if (intent != null)
//            {
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//            finish();
//        overridePendingTransition(R.anim.slide_no_anim, R.anim.slide_down_activity);
//
//    }

    protected void handleUpdateGiftCardLists(Intent intent)
    {
        // Parent classes will override this method and handle it according to intent type.
        // Using one broadcast receiver for multiple intents.
    }
    protected void handleUIRefreshGiftCardLists(Intent intent)
    {
        // Parent classes will override this method and handle it according to intent type.
        // Using one broadcast receiver for multiple intents.
    }

    protected void handleAuthTokenFailure()
    {
        //Auth token failure related handling goes here.
    }

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("BROADCAST_AUTH_TOKEN_FAILURE"))
            {
                handleAuthTokenFailure();
            }
            else if(intent.getAction().equals("BROADCAST_UPDATE_GF_HOME_ACTIVITY"))
            {
                handleUpdateGiftCardLists(intent);
            }else if(intent.getAction().equals("BROADCAST_GF_HOME_ACTIVITY_REFRESH_UI")){
                handleUIRefreshGiftCardLists(intent);
            }
        }
    };

}
