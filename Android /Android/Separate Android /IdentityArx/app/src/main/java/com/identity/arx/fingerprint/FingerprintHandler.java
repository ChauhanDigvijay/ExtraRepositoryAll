package com.identity.arx.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.student.GiveAttendanceActivity;


@RequiresApi(api = 23)
public class FingerprintHandler extends AuthenticationCallback {
    private Context context;

    public FingerprintHandler(Context mContext) {
        this.context = mContext;
    }

    public void startAuth(FingerprintManager manager, CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.USE_FINGERPRINT") == 0) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        update("Fingerprint Authentication error\n" + errString);
    }

    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        update("Fingerprint Authentication help\n" + helpString);
    }

    public void onAuthenticationFailed() {
        update("Fingerprint Authentication failed.");
    }

    public void onAuthenticationSucceeded(AuthenticationResult result) {
        ((Activity) this.context).finish();
        this.context.startActivity(new Intent(this.context, GiveAttendanceActivity.class));
    }

    private void update(String e) {
        ((TextView) ((Activity) this.context).findViewById(R.id.errorText)).setText(e);
    }
}
