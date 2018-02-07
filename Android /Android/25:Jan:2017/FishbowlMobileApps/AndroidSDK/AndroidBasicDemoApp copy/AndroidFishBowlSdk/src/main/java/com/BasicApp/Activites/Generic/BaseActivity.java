package com.BasicApp.Activites.Generic;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInModelActivity;
import com.BasicApp.Activites.NonGeneric.Contact.ContactUsModelActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Activites.NonGeneric.Home.UserProfileModelActivity;
import com.BasicApp.Activites.NonGeneric.LoyaltyCard.LoyaltyModelActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.NewMenuModelActivity;
import com.BasicApp.Activites.NonGeneric.Offer.RewardModelActivity;
import com.BasicApp.Activites.NonGeneric.Settings.FAQActivity;
import com.BasicApp.Activites.NonGeneric.Store.StoreListModelActivity;
import com.BasicApp.BusinessLogic.Models.OfferSummary;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.ModelAdapters.OrderConfirmDrawerAdapter;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {
    public boolean isBackButtonEnabled;
    public boolean isSlideDown;
    protected boolean isShowBasketIcon;
    protected boolean isAnimated = true;
    protected Toolbar toolbar;
    public static List<OrderConfirmDrawItem> drawerList;
    public static ListView lv;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.red_color));
        getSupportActionBar().hide();

    }


    public void onCustomBackPressed() {
        this.finish();
    }

    protected void setUpToolBar(boolean isBackButtonEnabled) {
        setUpToolBar(isBackButtonEnabled, false);
    }
    protected void setBottomToolBar() {
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
    }

    protected void setUpToolBar(boolean isBackButtonEnabled, boolean isSlideDown) {
        this.isBackButtonEnabled = isBackButtonEnabled;
        this.isSlideDown = isSlideDown;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
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
            RelativeLayout back = (RelativeLayout) toolbar.findViewById(R.id.toolbar);
            ImageView backbutton = (ImageView) back.findViewById(R.id.backbutton);
            ImageView menunavigator = (ImageView) back.findViewById(R.id.menu_navigator);
            if (isBackButton) {
                if (isWhite) {
                    backbutton.setImageResource(R.drawable.abback);
                    menunavigator.setVisibility(View.GONE);
                } else {
                    backbutton.setImageResource(R.drawable.abback);
                    menunavigator.setVisibility(View.VISIBLE);
                    setBottomToolBar();
                }
            } else {
                if (isWhite) {
                    backbutton.setImageResource(R.drawable.abback);
                } else {
                    backbutton.setImageResource(R.drawable.abback);
                }
            }
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            menunavigator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationDrawerPressed(BaseActivity.this);
                }
            });
        }
    }

    protected void setTitle(String title, int color) {
        if (toolbar != null) {
            TextView textView = (TextView) toolbar.findViewById(R.id.title_text);
            textView.setText(title);
            textView.setTextColor(color);
        }
    }

    protected void setTitle(String title) {
        setTitle(title, getResources().getColor(R.color.toolbar_text));
    }


    @Override
    public void onBackPressed() {
        trackButtonWithName("Back");
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

    protected void trackButton(View view) {
        String title = "[BUTTON HAS NO TITLE]";
        if (view instanceof Button) {
            Button button = (Button) view;
            if (button.getText() != null) {
                title = button.getText().toString();
            }
        }
        String screenName = getClass().getSimpleName();

    }

    protected void trackButtonWithName(String title) {
        String screenName = getClass().getSimpleName();

    }

    protected void trackUXEvent(String action, String label) {
        String screenName = getClass().getSimpleName();

    }

    @Override
    public Intent getIntent() {
        Intent intent = super.getIntent();
        if (intent == null) {
            intent = new Intent();
        }
        return intent;
    }


    public void onNavigationDrawerPressed(final Activity context) {
        setUpNavigationDrawerModel(context);

    }

    public  void NavigationDrawer()
    {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else
            drawerLayout.openDrawer(GravityCompat.END);

    }

    public void setUpNavigationDrawerModel(final Activity context) {


        drawerList = new ArrayList<OrderConfirmDrawItem>();




//            if(StringUtilities.isValidString(member.firstName))
//            {
//           //       drawerList.add(new OrderConfirmDrawItem(true, "WELCOME", member.firstName, R.drawable.ic_launcher)); // adding a spinner to the list
//            }
//        }
//        else {
//          //   drawerList.add(new OrderConfirmDrawItem(true, "Sign in or Sign up", "Enjoy the benefit of UNO.", R.drawable.ic_launcher)); // adding a spinner to the list
//        }
        drawerList.add(new OrderConfirmDrawItem("HOME", R.drawable.home));
        drawerList.add(new OrderConfirmDrawItem("My Loyalty", R.drawable.reward));
        drawerList.add(new OrderConfirmDrawItem("Menu", R.drawable.menuicon));
        drawerList.add(new OrderConfirmDrawItem("Location", R.drawable.maplocation));
        drawerList.add(new OrderConfirmDrawItem("Rewards and Offers", R.drawable.gift));
        // drawerList.add(new OrderConfirmDrawItem("Setting", R.drawable.setting));
        drawerList.add(new OrderConfirmDrawItem("My Profile", R.drawable.profile));
        drawerList.add(new OrderConfirmDrawItem("FAQs", R.drawable.faqs));
        drawerList.add(new OrderConfirmDrawItem("Contact Us", R.drawable.contact_us));
        drawerList.add(new OrderConfirmDrawItem(true, "Sign Out", "", R.drawable.logout)); // adding a spinner to the list
        // drawerList.add(new OrderConfirmDrawItem("Privacy Policy | Term of use",0,0)); // adding a header to the list
        OrderConfirmDrawerAdapter adapter = new OrderConfirmDrawerAdapter(context, R.layout.list_drawer, drawerList);
        lv = (ListView) context.findViewById(R.id.left_drawer);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          navigateToModel(position, context);
                                      }
                                  }
        );

        BaseActivity.this.NavigationDrawer();
    }

    public static void navigateToModel(int position, Activity activity) {
        switch (position) {
            case 0:

                Intent homeintent = new Intent(activity, DashboardModelActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(homeintent);
                activity.finish();
                break;

            case 1:
                Intent myloyalty = new Intent(activity, LoyaltyModelActivity.class);
                myloyalty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(myloyalty);

                break;

            case 2:

                Intent menu = new Intent(activity, NewMenuModelActivity.class);
                menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle extras = new Bundle();
                extras.putBoolean("checkdirectmenu", true);
                menu.putExtras(extras);
                activity.startActivity(menu);

                break;
            case 3:
                Intent location = new Intent(activity, StoreListModelActivity.class);
                location.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(location);
                break;

            case 4:
                Intent offer = new Intent(activity, RewardModelActivity.class);
                offer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(offer);
                break;

//            case 6:
//                Intent setting = new Intent(activity, MultipleOffer_Activity.class);
//                // offer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                setting.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                activity. startActivity(setting);
//                break;

            case 5:
                Intent userprofile = new Intent(activity, UserProfileModelActivity.class);
                userprofile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(userprofile);
                break;
            case 6:
                Intent faq = new Intent(activity, FAQActivity.class);
                faq.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(faq);
                break;
           /* case 7:
                Intent contact = new Intent(activity, ContactUsActivity.class);
                contact.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(contact);
                break;*/
            case 8:
                logout(activity);
                break;
//
//            case 8:
//                Intent i  =new Intent(activity, NewMenuActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                activity.startActivity(i);
//                break;
            case 7:
                Intent contact = new Intent(activity, ContactUsModelActivity.class);
                contact.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(contact);
                break;

        }
    }

    public static void logout(final Activity activity) {


        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        // Setting Dialog Title
        alertDialog.setTitle("Logout ");

        // Setting Dialog Message
        alertDialog.setMessage("Press Ok to Logout ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        FBSessionService.logout("mobilesdk",new FBSessionServiceCallback() {
                            @Override
                            public void onSessionServiceCallback(final FBSessionItem response, Exception error) {
                                if (response != null) {

                                    Bundle extras = new Bundle();
                                    Intent i = new Intent(activity, SignInModelActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                    i.putExtras(extras);

                                    activity.startActivity(i);
                                    if(RewardSummary.rewardList!=null) {
                                        if (RewardSummary.rewardList.size() > 0) {
                                            RewardSummary.rewardList.clear();

                                        }
                                    }
                                    if(OfferSummary.offerList!=null) {
                                        if (OfferSummary.offerList.size() > 0) {

                                            OfferSummary.offerList.clear();
                                        }
                                    }

                                    FBPreferences.sharedInstance(activity).setSignin(false);
                                    activity.finish();


                                } else {
                                    FBUtils.tryHandleTokenExpiry(activity, error);

                                }
                            }
                        });

                    }
                }


        );

        alertDialog.show();


    }





    public static void showErrorAlert(final Activity activity, String exception)
    {

        {

            FBUtils.showAlert(activity, exception, "Error");
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo netInfo = null;

        try {
            ConnectivityManager ex = (ConnectivityManager)context.getSystemService("connectivity");
            netInfo = ex.getActiveNetworkInfo();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return netInfo != null && netInfo.isAvailable();
    }


}
