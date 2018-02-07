package com.identity.arx;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.identity.arx.db.ExportDatabase;
import com.identity.arx.db.IdentityAxsOpenHelper;
import com.identity.arx.db.UserDetailTable;
import com.identity.arx.faculty.FacultyDrawerActivity;
import com.identity.arx.forgotpasword.ForgotPaswordActivity;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.DeviceInfo;
import com.identity.arx.general.StrongAES;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.LoginRequestObject;
import com.identity.arx.objectclass.LoginResponseObject;
import com.identity.arx.services.StartAlarm;
import com.identity.arx.student.StudentDrawerActivity;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PaswordActivity extends Activity {
    EditText editTextPasword;
    TextView passwordUsername;
    private SharedPreferences sharedPreference;
    Button signIn;

    class C07491 implements AsyncResponse {
        C07491() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            LoginResponseObject loginResponseObject = (LoginResponseObject) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), LoginResponseObject.class);
            String status = loginResponseObject.getStatus();
            String lockStatus = loginResponseObject.getLockStatus();
            if (!status.equals("failed") || !lockStatus.equals("4")) {
                if (!status.equals("failed") || !lockStatus.equals(Boolean.valueOf(false))) {
                    if (!status.equals("failed") || !lockStatus.equals(Boolean.valueOf(true))) {
                        String labelId = loginResponseObject.getLebelId();
                        new UserDetailTable(PaswordActivity.this).addUserDetails(loginResponseObject);
                        if (labelId.equals("34")) {
                            PaswordActivity.this.startActivity(new Intent(PaswordActivity.this, FacultyDrawerActivity.class));
                        } else if (labelId.equals("35")) {
                            PaswordActivity.this.startActivity(new Intent(PaswordActivity.this, FacultyDrawerActivity.class));
                        } else if (labelId.equals("36")) {
                            PaswordActivity.this.startActivity(new Intent(PaswordActivity.this, StudentDrawerActivity.class));
                        }
                        PaswordActivity.this.finish();
                    }
                }
            }
        }
    }

    class C07502 implements OnClickListener {
        C07502() {
        }

        public void onClick(DialogInterface dialog, int which) {
            PaswordActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        this.editTextPasword = (EditText) findViewById(R.id.one_time_password);
        this.signIn = (Button) findViewById(R.id.btnLogin);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        ExportDatabase.exportDB(this, IdentityAxsOpenHelper.DATABASE_NAME, BuildConfig.APPLICATION_ID);
        new StartAlarm().startTracking(this);
        this.passwordUsername = (TextView) findViewById(R.id.usernameAtPassowrd);
        this.passwordUsername.setText("Hi " + this.sharedPreference.getString("NAME", "") + "!!!");
    }

    public void onLogin(View view) {
        if (this.signIn.isEnabled()) {
            this.signIn.setClickable(true);
            this.signIn.setEnabled(true);
        } else {
            this.signIn.setEnabled(false);
        }
        if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C07502());
        } else if (checkValidation()) {
            String pasword = this.editTextPasword.getText().toString();
            String userId = this.sharedPreference.getString("USER_NAME", "");
            LoginRequestObject user = new LoginRequestObject();
            try {
                user.setUserId(userId);
                user.setPasword(StrongAES.encrypt("Arxtechaxs@123"));
                user.setLoginStatus("0");
                user.setDeviceId(DeviceInfo.getDeviceId(this));
                user.setNotificationId("NA");
                new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/loginCheck", user, new C07491()).execute(new ResponseEntity[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean checkValidation() {
        if (this.editTextPasword.getText().toString().trim().length() == 0) {
            this.editTextPasword.setError("Enter The Password");
        }
        if (this.editTextPasword.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public void onClickForgotPasword(View view) {
        startActivity(new Intent(this, ForgotPaswordActivity.class));
    }
}
