package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.SettingsActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

public class ConfirmationSentActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_email_sent);
        setUpToolBar(true);
        setBackButton(true,false);
        setTitle("Request Sent");
        if (!isCreatedFromPasswordChangeActivity()) {
            changeTexts();
        }
        Button btn = (Button) findViewById(R.id.okayBtn);
        btn.setOnClickListener(this);

    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.splash_bg_layout);
        BitmapUtils.loadBitmapResource(view, R.drawable.splash_bg, true);
    }

    private void changeTexts() {
        TextView t1 = (TextView) findViewById(R.id.text1);
        TextView t2 = (TextView) findViewById(R.id.text2);
        TextView t3 = (TextView) findViewById(R.id.text3);
        t1.setText("You should receive an email\ncontaining a link to verify\nyour new email address\nshortly.");
        t2.setText("Once your new email address\nhas been verified you will be able\nto use it to log in.");
        t3.setText("");
    }

    private boolean isCreatedFromPasswordChangeActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_CREATED_FROM_PASSWORD, false);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isCreatedFromPasswordChangeActivity()) {
            TransitionManager.transitFrom(this, SignInActivity.class, true);
        } else {
            TransitionManager.transitFrom(this, SettingsActivity.class, true);
        }
    }
}
