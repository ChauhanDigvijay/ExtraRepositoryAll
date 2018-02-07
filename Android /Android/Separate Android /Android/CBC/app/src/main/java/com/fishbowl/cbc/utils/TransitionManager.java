package com.fishbowl.cbc.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fishbowl.cbc.R;

/**
 * Created by VT027 on 4/22/2017.
 */

public class TransitionManager {
    public static void transitFrom(Activity activity, Class toActivity, boolean isBack, Bundle bundle) {
        startActivity(activity, toActivity, bundle);
        if (isBack) {
            activity.overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_no_anim);
        }
    }

    public static void slideUp(Activity activity, Class toActivity, boolean isBack, Bundle bundle) {
        startActivity(activity, toActivity, bundle);
        if (isBack) {
            activity.overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_no_anim);
            activity.finish();
        }
    }

    public static void startActivity(Activity activity, Class toActivity, Bundle bundle) {
        Intent i = new Intent(activity, toActivity);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        activity.startActivity(i);
    }

    public static void slideInDown(Activity activity, Class toActivity) {
        TransitionManager.slideUp(activity, toActivity, false, null);
    }

    public static void slideOutUp(Activity activity, Class toActivity, Bundle bundle) {
        TransitionManager.slideUp(activity, toActivity, false, bundle);
    }

    public static void slideUp(Activity activity, Class toActivity, boolean isBack) {
        TransitionManager.slideUp(activity, toActivity, isBack, null);
    }

    public static void transitFrom(Activity activity, Class toActivity) {
        TransitionManager.transitFrom(activity, toActivity, false);
    }

    public static void transitFrom(Activity activity, Class toActivity, boolean isBack) {
        TransitionManager.transitFrom(activity, toActivity, isBack, null);
    }

    public static void transitFrom(Activity activity, Class toActivity, Bundle bundle) {
        TransitionManager.transitFrom(activity, toActivity, false, bundle);
    }
}
