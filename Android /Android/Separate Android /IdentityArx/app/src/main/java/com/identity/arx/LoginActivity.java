package com.identity.arx;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.identity.arx.db.ExportDatabase;
import com.identity.arx.db.IdentityAxsOpenHelper;
import com.identity.arx.db.UserDetailTable;
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
import com.identity.arx.session.SessionManager;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;



public class LoginActivity extends Activity {
    Activity context = this;
    private EditText editTTextInstitute;
    Integer instID;
    private Button loginButton;
    Integer loginStatus;
    String name;
    private EditText pass;
    private String passw;
    String password;
    Integer role;
    SessionManager session;
    private SharedPreferences sharedPreference;
    Editor sharedPreferenceedit;
    private EditText user;
    private String userName;

    class C07421 implements OnClickListener {

        class C07401 implements AsyncResponse {
            C07401() {
            }

            public void asyncResponse(ResponseEntity<?> response) {
                LoginResponseObject loginResponseObject = (LoginResponseObject) new Gson().fromJson(HashMapJson.getJsonObject(new HashMap((LinkedHashMap) response.getBody())).toString(), LoginResponseObject.class);
                String status = loginResponseObject.getStatus();
                String lockStatus = loginResponseObject.getLockStatus();
                if (status.equalsIgnoreCase("failed") && lockStatus.equalsIgnoreCase("4")) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Invalid Username", 1).show();
                } else if (status.equalsIgnoreCase("failed") && lockStatus.equalsIgnoreCase("failed")) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Invalid Password", 1).show();
                } else if (status.equalsIgnoreCase("failed") && lockStatus.equalsIgnoreCase("true")) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "you have attempted 3 times", 1).show();
                } else if (status.equalsIgnoreCase("deviceIdFailed")) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "you cannot login from other device", 1).show();
                } else {
                    String labelId = loginResponseObject.getLebelId();
                    new UserDetailTable(LoginActivity.this).addUserDetails(loginResponseObject);
                    String userType = "";
                    if (labelId.equals("34")) {
                        userType = "faculty";
                    } else if (labelId.equals("35")) {
                        userType = "faculty";
                    } else if (labelId.equals("36")) {
                        userType = "student";
                    }
                    LoginActivity.this.sharedPreferenceedit.putString("LOGIN_STATUS", "second");
                    LoginActivity.this.sharedPreferenceedit.putString("USER_NAME", LoginActivity.this.userName);
                    LoginActivity.this.sharedPreferenceedit.putString("LABEL_ID", labelId);
                    LoginActivity.this.sharedPreferenceedit.putString("NAME", loginResponseObject.getName());
                    LoginActivity.this.sharedPreferenceedit.putString("USER_TYPE", userType);
                    LoginActivity.this.sharedPreferenceedit.putString("USER_ID", loginResponseObject.getUserId());
                    LoginActivity.this.sharedPreferenceedit.putString("ROLL_NUM", loginResponseObject.getRollnum());
                    LoginActivity.this.sharedPreferenceedit.putString("FACULTY_ID", loginResponseObject.getFacultyId());
                    LoginActivity.this.sharedPreferenceedit.commit();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoaderActivity.class));
                    LoginActivity.this.finish();
                }
            }
        }

        class C07412 implements DialogInterface.OnClickListener {
            C07412() {
            }

            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.this.finish();
            }
        }

        C07421() {
        }

        public void onClick(View v) {
            if (LoginActivity.this.loginButton.isEnabled()) {
                LoginActivity.this.loginButton.setClickable(true);
                LoginActivity.this.loginButton.setEnabled(true);
            } else {
                LoginActivity.this.loginButton.setEnabled(false);
            }
            LoginActivity.this.userName = LoginActivity.this.user.getText().toString();
            LoginActivity.this.passw = LoginActivity.this.pass.getText().toString();
            if (!ConnectionDetector.isConnectingToInternet(LoginActivity.this.getApplicationContext())) {
                ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", LoginActivity.this).buildDialog(new C07412());
            } else if (LoginActivity.this.checkValidation()) {
                LoginRequestObject user = new LoginRequestObject();
                try {
                    user.setUserId(LoginActivity.this.userName);
                    user.setPasword(StrongAES.encrypt("Arxtechaxs@123"));
                    user.setLoginStatus("0");
                    user.setDeviceId(DeviceInfo.getDeviceId(LoginActivity.this));
                    user.setNotificationId("himanshu");
                    new HttpAsyncTask(LoginActivity.this, WebUrl.SERVER_ADDRESS + LoginActivity.this.sharedPreference.getString("INTITUTE_ID", "") + "/loginCheck", user, new C07401()).execute(new ResponseEntity[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.pass = (EditText) findViewById(R.id.etPassword);
        this.user = (EditText) findViewById(R.id.etUsername);
        this.editTTextInstitute = (EditText) findViewById(R.id.chooseinstitute);
        this.loginButton = (Button) findViewById(R.id.btnLogin);
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        this.sharedPreferenceedit = this.sharedPreference.edit();
        IdentityAxsOpenHelper identityAxsOpenHelper = new IdentityAxsOpenHelper(this);
        this.session = new SessionManager(getApplicationContext());
        IdentityAxsOpenHelper db1 = new IdentityAxsOpenHelper(this);
        this.loginButton.setOnClickListener(new C07421());
        ExportDatabase.exportDB(this, IdentityAxsOpenHelper.DATABASE_NAME, BuildConfig.APPLICATION_ID);
    }

    boolean checkValidation() {
        if (this.user.getText().toString().trim().length() == 0) {
            this.user.setError("can not be empty");
        }
        if (this.pass.getText().toString().trim().length() == 0) {
            this.pass.setError("can not be empty");
        }
        if (this.editTTextInstitute.getText().toString().trim().length() == 0) {
            this.editTTextInstitute.setError("can not be empty");
        }
        if (this.user.getText().toString().trim().length() == 0 || this.pass.getText().toString().trim().length() == 0 || this.editTTextInstitute.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public void selectInstitute(View view) {
        startActivityForResult(new Intent(this, SelectInstituteActivity.class), 101);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onActivityResult(int r3, int r4, Intent r5) {
        /*
        r2 = this;
        r1 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r3 != r1) goto L_0x0015;
    L_0x0004:
        if (r4 != 0) goto L_0x0013;
    L_0x0006:
        if (r5 == 0) goto L_0x0013;
    L_0x0008:
        r1 = "INTITUTE_NAME";
        r0 = r5.getStringExtra(r1);
        r1 = r2.editTTextInstitute;
        r1.setText(r0);
    L_0x0013:
        if (r4 != 0) goto L_0x0015;
    L_0x0015:
        super.onActivityResult(r3, r4, r5);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: identity.arx.axs.com.identityaxs.LoginActivity.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onClickForgotPasword(View view) {
        startActivityForResult(new Intent(this, ForgotPaswordActivity.class), 101);
    }
}
