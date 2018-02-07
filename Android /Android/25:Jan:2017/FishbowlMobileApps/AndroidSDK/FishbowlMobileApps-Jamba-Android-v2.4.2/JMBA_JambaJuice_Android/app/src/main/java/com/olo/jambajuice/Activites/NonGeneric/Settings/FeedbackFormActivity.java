package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.FeedbackServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.Feedback;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.FeedbackService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

public class FeedbackFormActivity extends BaseActivity implements View.OnClickListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);
        context = this;
        setUpToolBar(true);
        setTitle(getFeedbackType());
        setBackButton(true, false);
        setUpView();
    }

    private void setUpView() {
        findViewById(R.id.submitFeedback).setOnClickListener(this);
        if (UserService.isUserAuthenticated()) {
            findViewById(R.id.email).setVisibility(View.GONE);
            findViewById(R.id.contactNumber).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText contactNumber = (EditText) findViewById(R.id.contactNumber);
        EditText comments = (EditText) findViewById(R.id.comments);
        String emailString = email.getText().toString();
        String contactString = contactNumber.getText().toString();
        String commentString = comments.getText().toString();

        boolean isValid = true;

        if (!UserService.isUserAuthenticated()) {
            if (emailString.isEmpty()) {
                email.requestFocus();
                email.setError("Please enter an email address.");
               // Utils.showAlert(this, "Please enter an email address", "Email Required");
                isValid = false;
                return;
            }
            if (!Utils.isValidEmail(emailString)) {
                email.requestFocus();
                email.setError("Please enter a valid email address.");
               // Utils.showAlert(this, "Please enter a valid email address", "Email Required");
                isValid = false;
                return;
            }
            if (!contactString.isEmpty() && !Utils.isValidCellPhone(contactString)) {
                if (isValid) {
                    contactNumber.requestFocus();
                }
                contactNumber.setError("Please enter a valid " + Constants.PHONE_NUMBER_LENGTH + " digit phone number.");
                isValid = false;
            }
        } else {
            User user = UserService.getUser();
            emailString = user.getEmailaddress();
            contactString = user.getContactnumber();
        }
        if (commentString.isEmpty()) {
            if (isValid) {
                comments.requestFocus();
            }
            comments.setError("Please enter a valid comment.");
            isValid = false;
        }
        if (isValid) {
            Feedback feedback = new Feedback();
            feedback.setEmailAddress(emailString);
            feedback.setPhoneNumber(contactString);
            feedback.setFeedbackType(getFeedbackType());
            feedback.setFeedback(commentString);
            feedback.setAppVersion(Utils.getVersionNumber());
            feedback.setOS("Android");
            feedback.setOSVersion(Build.VERSION.RELEASE);
            enableScreen(false);
            FeedbackService.sendFeedback(feedback, new FeedbackServiceCallback() {
                @Override
                public void onSendFeedbackCallback(Exception exception) {
                    enableScreen(true);
                    if (exception != null) {
                        Utils.showErrorAlert(FeedbackFormActivity.this, exception);
                    } else {
                        new AlertDialog.Builder(FeedbackFormActivity.this).setMessage("Feedback Sent").setCancelable(false).setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TransitionManager.transitFrom(FeedbackFormActivity.this, SettingsActivity.class, true);
                            }
                        }).create().show();
                    }
                }
            });
        }
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        super.enableScreen(isEnabled);
        Utils.hideSoftKeyboard(this);
        Button sendButton = (Button) findViewById(R.id.submitFeedback);
        findViewById(R.id.submitFeedback).setEnabled(isEnabled);
        findViewById(R.id.email).setEnabled(isEnabled);
        findViewById(R.id.contactNumber).setEnabled(isEnabled);
        findViewById(R.id.comments).setEnabled(isEnabled);

        if (isEnabled) {
            sendButton.setText("Send Feedback");
        } else {
            sendButton.setText("Please wait...");
        }
    }

    private String getFeedbackType() {
        String feedbackType = "General";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            feedbackType = bundle.getString(Constants.B_FEEDBACK_TYPE);
        }
        return feedbackType;
    }
}
