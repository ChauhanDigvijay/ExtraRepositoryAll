package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.MenuLanding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ActivityMenu.MyActivityListActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ContactUsMenu.ContactUsActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.FAQMenu.FAQsActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.MessageMenu.MessageActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ProfileMenu.UpdateProfile;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.ActivityService;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 09-Nov-16.
 */

public class MenuActivity extends Activity  implements View.OnClickListener{
    LinearLayout activity,contact_us,faqs,message;
    LinearLayout  home,my_profile;
    Button log;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage,titlebackground,img_Back,img_Background;
    TextView title,title_welcome;
    RelativeLayout toolbar;
    LinearLayout profile_way;
    ProgressBarHandler progressBarHandler;
    LinearLayout layout_button,layout_logout,layout_loyalty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        progressBarHandler = new ProgressBarHandler(MenuActivity.this);
        img_Background = (NetworkImageView) findViewById(R.id.img_Background);
        home = (LinearLayout) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MenuActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        activity = (LinearLayout) findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MyActivityListActivity.class);
                startActivity(i);
            }
        });
        contact_us = (LinearLayout) findViewById(R.id.contact_us);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, ContactUsActivity.class);
                startActivity(i);
            }
        });
        faqs = (LinearLayout) findViewById(R.id.faqs);
        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, FAQsActivity.class);
                startActivity(i);
            }
        });
        message = (LinearLayout) findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MessageActivity.class);
                startActivity(i);
            }
        });
        my_profile = (LinearLayout) findViewById(R.id.my_profile);
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, UpdateProfile.class);
                startActivity(i);
            }
        });

        Log.v("Example", "onCreate");
        getIntent().setAction("Already created");

      //  if (ActivityService.loyaltyactivitylist == null){
            getloyaltyactivity();
    //}
       // getState();
    }
    public void onStart()
    {
        super.onStart();
        inititateui();
    }




    public void   getloyaltyactivity()
  {
    //  progressBarHandler.show();
      JSONObject obj = new JSONObject();

      try
      {
          obj.put("areaType","");
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }

      ActivityService.getActivity(this, obj,new LoyaltyActivitySummaryCallback() {


          @Override
          public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList rewardSummary, Exception error) {


          //    progressBarHandler.hide();
              if (rewardSummary != null) {
                 // updateOfferRewardPoint(rewardSummary);

              } else {
                  //  enableScreen(true);
            //      FBUtils.tryHandleTokenExpiry(MenuActivity.this, error);
              }
          }
      });
  }
    public void inititateui()
    {
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        toolbar= (RelativeLayout) findViewById(R.id.tool_bar);
        titleimage=(NetworkImageView) toolbar.findViewById(R.id.backbutton);
        final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().companyLogoImageUrl;
        mImageLoader.get(url2, ImageLoader.getImageListener(titleimage, R.drawable.signup, R.drawable.signup));
        titleimage.setImageUrl(url2, mImageLoader);
        img_Back = (NetworkImageView) toolbar.findViewById(R.id.img_Back);

        mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
        final String url1 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginRightTopImageUrl;
        mImageLoader.get(url1, ImageLoader.getImageListener(img_Back, R.color.white, R.color.white));
        img_Back.setImageUrl(url1, mImageLoader);


      //  img_Back.setBackgroundResource(R.drawable.header);
        layout_logout = (LinearLayout) toolbar.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(this);
        title_welcome=(TextView) toolbar.findViewById(R.id.title_welcome);
        profile_way = (LinearLayout) toolbar.findViewById(R.id.profile_way);
        profile_way.setOnClickListener(this);
        title=(TextView) toolbar.findViewById(R.id.title_textb);
       /* log = (Button)toolbar. findViewById(R.id.title_textf);
        log.setOnClickListener(this);*/
        layout_button = (LinearLayout) toolbar.findViewById(R.id.layout_button);
        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this,MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
        layout_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        String firstName= FB_LY_UserService.sharedInstance().member.firstName;
        title.setText(firstName);

        //backgroundImage=(NetworkImageView)findViewById(R.id.img_Back);
        final String url = "http://"+ FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(img_Background,R.drawable.signup,R.drawable.signup));
        img_Background.setImageUrl(url, mImageLoader);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_logout:
                logout();
                break;
            case R.id.profile_way:
                onBackPressed();
                break;
        }

    }




    public  void logout()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setTitle("Logout ");
        alertDialog.setMessage("Press ok to Logout ");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                JSONObject object=new JSONObject();
                try
                {
                    object.put("Application","mobile");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Bundle extras = new Bundle();
                Intent i=new Intent(MenuActivity.this,SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                i.putExtras(extras);
                startActivity(i);
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
                MenuActivity.this.finish();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void myCustomRestart(){
        Intent i = new Intent(this,MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
