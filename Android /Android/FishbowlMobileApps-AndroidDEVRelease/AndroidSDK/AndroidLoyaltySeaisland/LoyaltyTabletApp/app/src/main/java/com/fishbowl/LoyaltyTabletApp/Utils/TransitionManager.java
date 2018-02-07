package com.fishbowl.LoyaltyTabletApp.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fishbowl.LoyaltyTabletApp.R;


/**
 * Created by Nauman Afzaal on 07/05/15.
 */
public class TransitionManager
{

    public static void transitFrom(Activity activity, Class toActivity, boolean isBack, Bundle bundle)
    {
        startActivity(activity, toActivity, bundle);
        if (isBack)
        {
            activity.overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        }
        else
        {
            activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    }

    public static void slideUp(Activity activity, Class toActivity, boolean isBack, Bundle bundle)
    {
        startActivity(activity, toActivity, bundle);
        if (isBack)
        {
            activity.overridePendingTransition(R.anim.slide_no_anim, R.anim.slide_down_activity);
        }
        else
        {
            activity.overridePendingTransition(R.anim.slide_up_activity, R.anim.slide_no_anim);
        }
    }

    public static void startActivity(Activity activity, Class toActivity, Bundle bundle)
    {
        Intent i = new Intent(activity, toActivity);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null)
        {
            i.putExtras(bundle);
        }
        activity.startActivity(i);
    }

    public static void slideUp(Activity activity, Class toActivity)
    {
        TransitionManager.slideUp(activity, toActivity, false, null);
    }

    public static void slideUp(Activity activity, Class toActivity, Bundle bundle)
    {
        TransitionManager.slideUp(activity, toActivity, false, bundle);
    }

    public static void slideUp(Activity activity, Class toActivity, boolean isBack)
    {
        TransitionManager.slideUp(activity, toActivity, isBack, null);
    }

    public static void transitFrom(Activity activity, Class toActivity)
    {
        TransitionManager.transitFrom(activity, toActivity, false);
    }

    public static void transitFrom(Activity activity, Class toActivity, boolean isBack)
    {
        TransitionManager.transitFrom(activity, toActivity, isBack, null);
    }

    public static void transitFrom(Activity activity, Class toActivity, Bundle bundle)
    {
        TransitionManager.transitFrom(activity, toActivity, false, bundle);
    }
}
