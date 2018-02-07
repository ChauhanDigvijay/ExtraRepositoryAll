package com.donatos.phoenix.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.C0703g;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.bluelinelabs.logansquare.LoganSquare;
import com.donatos.phoenix.DonatosApplication;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.pushnotification.PushNotification;
import com.donatos.phoenix.network.pushnotification.PushNotificationReceivedEvent;
import com.donatos.phoenix.p128a.p129a.C2445b;
import com.donatos.phoenix.p128a.p129a.C2458e;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.C2509m;
import com.donatos.phoenix.ui.common.C2728a;
import com.donatos.phoenix.ui.common.C2742m;
import com.donatos.phoenix.ui.common.C2750s;
import com.donatos.phoenix.ui.common.C2750s.C2749a;
import com.donatos.phoenix.ui.common.aa;
import com.donatos.phoenix.ui.main.C2851a;
import com.donatos.phoenix.ui.main.MainActivity;
import com.google.firebase.messaging.C3932a;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p027b.p041c.p046b.C1172b;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends C2728a {
    C2750s f8859o;
    C2509m f8860p;
    private Toolbar f8861q;
    private AppBarLayout f8862r;
    private List<C1172b> f8863s = new ArrayList();

    static /* synthetic */ void m8363a(LoginActivity loginActivity, PushNotification pushNotification) {
        loginActivity.f8860p.m7354a(pushNotification);
        aa.m8117a(loginActivity.findViewById(R.id.activity_main_fragment_container), R.string.notification.dialog.offer.will.be.added);
    }

    static /* synthetic */ void m8364a(LoginActivity loginActivity, C3932a c3932a) {
        IOException e;
        if (c3932a.m12954a().size() == 0) {
            C2742m.m8172a(loginActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2834d.m8369a());
            return;
        }
        PushNotification pushNotification;
        try {
            pushNotification = (PushNotification) LoganSquare.parse((String) c3932a.m12954a().get("payload"), PushNotification.class);
            try {
                pushNotification.setId(c3932a.m12955b());
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                C2742m.m8173a((Context) loginActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2835e.m8370a(), C2836f.m8371a(loginActivity, pushNotification));
            }
        } catch (IOException e3) {
            IOException iOException = e3;
            pushNotification = null;
            e = iOException;
            e.printStackTrace();
            C2742m.m8173a((Context) loginActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2835e.m8370a(), C2836f.m8371a(loginActivity, pushNotification));
        }
        C2742m.m8173a((Context) loginActivity, c3932a.m12956c().m12953b(), c3932a.m12956c().m12952a(), C2835e.m8370a(), C2836f.m8371a(loginActivity, pushNotification));
    }

    static /* synthetic */ void m8365a(LoginActivity loginActivity, Object obj) throws Exception {
        if (obj instanceof C2831a) {
            loginActivity.m8366f();
        }
        if (obj instanceof PushNotificationReceivedEvent) {
            loginActivity.runOnUiThread(C2833c.m8368a(loginActivity, ((PushNotificationReceivedEvent) obj).getMessage()));
        }
    }

    private void m8366f() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public final View mo1599d() {
        return findViewById(R.id.activity_main_fragment_container);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        C2508l.m7347a().m7349a(new C2851a(i, i2, intent));
    }

    public void onBackPressed() {
        if (this.f8859o.f8603a.mo258a(C2838h.f8874a) != null && this.f8859o.f8603a.mo258a(C2838h.f8874a).m1000p()) {
            if (this.f8860p.f7675a.getBoolean("guest", false)) {
                m8366f();
                return;
            }
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        C0703g.m2765l();
        setContentView((int) R.layout.activity_login);
        this.f8861q = (Toolbar) findViewById(R.id.app_toolbar);
        this.f8862r = (AppBarLayout) findViewById(R.id.app_bar);
        this.n = C2458e.m7218a().m7217a(DonatosApplication.m7167a((Context) this).f7442a).m7216a(new C2445b(this, b_(), this.f8862r, this.f8861q)).m7215a();
        this.n.mo1505a(this);
        this.f8859o.f8604b = R.id.activity_main_fragment_container;
        this.f8859o.m8194a(new C2838h(), C2749a.CROSSFADE, C2749a.CROSSFADE, C2838h.f8874a);
        this.f8863s.add(C2508l.m7347a().f7674a.subscribe(new C2832b(this)));
    }

    public void onDestroy() {
        for (C1172b c1172b : this.f8863s) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
        if (this.f8859o != null) {
            this.f8859o.f8603a = null;
        }
        super.onDestroy();
    }
}
