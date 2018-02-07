package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBMobileSettingService;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

public class ForceUpdateActivity extends BaseActivity implements View.OnClickListener {

    private SemiBoldTextView updateMessage,updateButton,cancelButton,dismissButton;
    private String forceUpdate = "1";
    private String updateMessageText ="Please Update !";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_update);

        context = this;
        initComponents();
        getData();
    }

    private void initComponents(){
        updateMessage = (SemiBoldTextView)findViewById(R.id.updateMessage);
        updateButton = (SemiBoldTextView)findViewById(R.id.updateButton);
        cancelButton = (SemiBoldTextView)findViewById(R.id.cancelButton);
        dismissButton = (SemiBoldTextView)findViewById(R.id.dismissButton);



        updateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        dismissButton.setOnClickListener(this);
    }

    private void getData(){
        if(FBMobileSettingService.sharedInstance().mobileSettings != null) {
            forceUpdate = FBMobileSettingService.sharedInstance().mobileSettings.FORCEUPDATE;
            updateMessageText = FBMobileSettingService.sharedInstance().mobileSettings.INFORMATION;
        }
        updateMessage.setText(updateMessageText);
        if(StringUtilities.isValidString(forceUpdate)) {
            if (forceUpdate.equalsIgnoreCase("1")) {
                cancelButton.setText("Exit");
                dismissButton.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dismissButton:
                dismissUpdate();
                break;
            case R.id.updateButton:
                updateApp();
                break;
            case R.id.cancelButton:
                onBackPressed();
                break;
        }
    }

    private void updateApp(){
        String packageName = "";
        try {
            packageName = context.getPackageName();
            packageName = "com.olo.jambajuice";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ packageName));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(browserIntent);

        } catch (android.content.ActivityNotFoundException anfe) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
            context.startActivity(browserIntent);
        }
    }

    // For 'Remind Me Later' and 'Exit Me' options
    private void cancelUpdate(){
        if(StringUtilities.isValidString(forceUpdate)) {
            // If app update is mandatory
            if (forceUpdate.equalsIgnoreCase("1")) {
                //we show alert, if user dismiss it we can't continue to next process of the app 
                //finish();
                //System.exit(0);
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                DataManager.getInstance().setAlreadyShownUpdateScreen(false);
            }
            //If app update is optional
            else if (forceUpdate.equalsIgnoreCase("0")) {
                //we show alert, if user taps, we just close the screen
                DataManager.getInstance().setAlreadyShownUpdateScreen(true);
            }
        }
    }

    private void dismissUpdate(){
        if(FBMobileSettingService.sharedInstance().mobileSettings != null && FBMobileSettingService.sharedInstance().mobileSettings.VERSIONCODE != null) {
            try {
                Long versionCode = Long.parseLong(FBMobileSettingService.sharedInstance().mobileSettings.VERSIONCODE);
                FBPreferences.sharedInstance(context).setLatestBuildCode(versionCode);
                FBPreferences.sharedInstance(context).setUserDismissedAppUpdate(true);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        cancelUpdate();
        super.onBackPressed();
    }
}
