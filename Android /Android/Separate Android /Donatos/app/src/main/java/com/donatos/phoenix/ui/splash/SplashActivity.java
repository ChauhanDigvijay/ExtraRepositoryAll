package com.donatos.phoenix.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.C0702e;
import android.support.v7.app.C0703g;
import android.util.Log;
import com.bluelinelabs.logansquare.LoganSquare;
import com.donatos.phoenix.DonatosApplication;
import com.donatos.phoenix.network.pushnotification.PushNotification;
import com.donatos.phoenix.network.pushnotification.PushNotificationSubscriptionService;
import com.donatos.phoenix.p128a.p129a.C2445b;
import com.donatos.phoenix.p128a.p129a.C2458e;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.C2509m;
import com.donatos.phoenix.ui.login.LoginActivity;
import com.donatos.phoenix.ui.main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p027b.p041c.p046b.C1172b;

public class SplashActivity extends C0702e {
    private static final String f9041p = SplashActivity.class.getSimpleName();
    C2509m f9042n;
    PushNotificationSubscriptionService f9043o;
    private List<C1172b> f9044q = new ArrayList();

    private PushNotification m8504d() {
        IOException e;
        if (getIntent().hasExtra("payload")) {
            for (String str : getIntent().getExtras().keySet()) {
                Log.i(f9041p, "Key: " + str + " Value: " + getIntent().getExtras().get(str));
            }
            String str2 = getIntent().getStringExtra("payload");
            if (str2 != null) {
                PushNotification pushNotification;
                try {
                    pushNotification = (PushNotification) LoganSquare.parse(str2, PushNotification.class);
                    try {
                        pushNotification.setId(getIntent().getStringExtra("google.message_id"));
                        return pushNotification;
                    } catch (IOException e2) {
                        e = e2;
                    }
                } catch (IOException e3) {
                    IOException iOException = e3;
                    pushNotification = null;
                    e = iOException;
                    e.printStackTrace();
                    return pushNotification;
                }
            }
        }
        return null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        C0703g.m2765l();
        C2458e.m7218a().m7217a(DonatosApplication.m7167a((Context) this).f7442a).m7216a(new C2445b(this)).m7215a().mo1513a(this);
        PushNotification d = m8504d();
        if (d != null) {
            this.f9042n.m7354a(d);
        }
        if (this.f9042n.m7358a()) {
            this.f9043o.addOrUpdateActiveUserSubscription(FirebaseInstanceId.m12888a().m12896b());
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    public void onDestroy() {
        for (C1172b c1172b : this.f9044q) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
        this.f9043o.cleanUp();
        super.onDestroy();
    }
}
