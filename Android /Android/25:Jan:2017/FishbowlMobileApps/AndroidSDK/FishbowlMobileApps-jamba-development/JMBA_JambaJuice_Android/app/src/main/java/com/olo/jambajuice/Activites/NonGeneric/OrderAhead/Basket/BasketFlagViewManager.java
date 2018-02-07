package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;

import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

/**
 * Created by Nauman Afzaal on 26/05/15.
 */
public class BasketFlagViewManager {
    static BasketFlagViewManager instance;
    public int basketHeight = 0;
    WindowManager windowManager;
    boolean showAtTop = false;
    private BasketFlagView basketFlagView;

    public static BasketFlagViewManager getInstance() {
        if (instance == null) {
            instance = new BasketFlagViewManager();
        }
        return instance;
    }


    public void showOrResetBasketFlag(Activity activity, boolean showAtTop) {
        this.showAtTop = showAtTop;
        //Marshmallow and above devices
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(activity)) {
//                //alert User for grantpermission
//                openDrawLayoutSettings(activity);
//            } else {
//                DataManager manager = DataManager.getInstance();
//                if (basketFlagView == null && manager.isBasketPresent()) {
//                    basketFlagView = new BasketFlagView(activity);
//                    windowManager = (WindowManager) activity.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);
//                    WindowManager.LayoutParams params = getButtonParams(activity);
//                    windowManager.addView(basketFlagView, params);
//                }
//                if (basketFlagView != null) {
//                    basketFlagView.updateContext(activity);
//                    setBasketHeight(basketFlagView.getHeight());
//                }
//            }
//        } else {
        //Below Marshmallow devices
        DataManager manager = DataManager.getInstance();
        if (basketFlagView == null && manager.isBasketPresent()) {
            basketFlagView = new BasketFlagView(activity);
            windowManager = (WindowManager) activity.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);
            WindowManager.LayoutParams params = getButtonParams(activity);
            windowManager.addView(basketFlagView, params);
        }
        if (basketFlagView != null) {
            basketFlagView.updateContext(activity);
            setBasketHeight(basketFlagView.getHeight());
        }
        //      }

    }


    public void showBasketFlag(Activity activity) {
        this.showAtTop = false;
        //Marshmallow and above devices
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(activity)) {
//                //alert User for grantpermission
//                openDrawLayoutSettings(activity);
//            } else {
//                DataManager manager = DataManager.getInstance();
//                if (basketFlagView == null && manager.isBasketPresent()) {
//                    basketFlagView = new BasketFlagView(activity);
//                    windowManager = (WindowManager) activity.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);
//                    WindowManager.LayoutParams params = getButtonParams(activity);
//                    windowManager.addView(basketFlagView, params);
//                }
//                if (basketFlagView != null) {
//                    basketFlagView.updateContext(activity);
//                    setBasketHeight(basketFlagView.getHeight());
//                }
//            }
//        } else {
        //Below Marshmallow devices
        DataManager manager = DataManager.getInstance();
        if (basketFlagView == null && manager.isBasketPresent()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    //alert User for grantpermission
                    openDrawLayoutSettings(activity);
                } else {
                    basketFlagView = new BasketFlagView(activity);
                    windowManager = (WindowManager) activity.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);
                    WindowManager.LayoutParams params = getButtonParams(activity);
                    basketFlagView.setContentDescription("basket_fab_normal");
                    windowManager.addView(basketFlagView, params);
                }
            } else {
                basketFlagView = new BasketFlagView(activity);
                windowManager = (WindowManager) activity.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);
                WindowManager.LayoutParams params = getButtonParams(activity);
                basketFlagView.setContentDescription("basket_fab_normal");
                windowManager.addView(basketFlagView, params);
            }
        }
        if (basketFlagView != null) {

            basketFlagView.updateContext(activity);
            if (basketFlagView.isInTouchMode()) {
                setBasketHeight(basketFlagView.getHeight());
            }
        }
//        }

    }

    public void openDrawLayoutSettings(final Activity activity) {
        //  if (DataManager.getInstance().isBasketPresent()) {
        //show alert only when Home screen is not in foreground. But show in other screens
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("We need your permission to display Basket icon on top of this App. Please grant permission!");
        alertDialogBuilder.setTitle("Basket Permission");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, Constants.REQUEST_CODE_ASK_PERMISSIONS);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        //    }
    }

    public void checkdrawPermission(Activity activity) {

    }

    public void updateCount() {
        if (basketFlagView != null) {
            basketFlagView.updateCount();
        }
    }

    public void removeBasketFlag() {
        try {
            if (basketFlagView != null && windowManager != null) {
                windowManager.removeView(basketFlagView);
                basketFlagView = null;
                windowManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private WindowManager.LayoutParams getButtonParams(Activity activity) {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        }
        if (showAtTop == true) {
            params.y = 0;
        } else {
            params.y = (int) activity.getResources().getDimension(R.dimen.basket_icon_top_margin);
        }
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        return params;
    }

    private void setBasketHeight(int height) {
        if (height != 0 && height > basketHeight) {
            basketHeight = height;
        }
    }
}
