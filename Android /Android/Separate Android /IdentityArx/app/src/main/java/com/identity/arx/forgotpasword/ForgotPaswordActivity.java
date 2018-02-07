package com.identity.arx.forgotpasword;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.general.AppSharedPrefrence;


public class ForgotPaswordActivity extends Activity {
    EditText editTextPasword;
    TextView forgotUsername;
    private SharedPreferences sharedPreference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.editTextPasword = (EditText) findViewById(R.id.one_time_password);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        this.forgotUsername = (TextView) findViewById(R.id.userNameAtForgot);
        this.forgotUsername.setText(this.sharedPreference.getString("NAME", ""));
    }

    boolean checkValidation() {
        if (this.editTextPasword.getText().toString().trim().length() == 0) {
            this.editTextPasword.setError("can not be empty");
        }
        if (this.editTextPasword.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }
}
