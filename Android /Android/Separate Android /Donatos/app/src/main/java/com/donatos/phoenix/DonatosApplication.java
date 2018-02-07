package com.donatos.phoenix;

import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.p007b.C0028a;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.bluelinelabs.logansquare.LoganSquare;
import com.donatos.phoenix.p128a.p130b.C2459a;
import com.donatos.phoenix.p128a.p131c.C2461a;
import com.donatos.phoenix.p128a.p131c.C2462b;
import com.donatos.phoenix.p128a.p131c.C2466e;
import com.donatos.phoenix.p128a.p131c.C2466e.C2465a;
import com.donatos.phoenix.p128a.p132d.C2467a;
import com.donatos.phoenix.p128a.p133e.C2487a;
import com.donatos.phoenix.p128a.p133e.C2489c;
import com.donatos.phoenix.p134b.C2495b;
import com.donatos.phoenix.p134b.C2499c.C24971;
import com.donatos.phoenix.p134b.C2504h;
import com.donatos.phoenix.p134b.p135a.C2492a;
import com.google.android.gms.tagmanager.C3823b;
import com.google.android.gms.tagmanager.C3885f;
import com.p091b.p092a.C2022a;
import com.p126d.p127a.C2442a;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import p001a.p002a.C0006d;
import p027b.p028a.p029a.p030a.C1139c;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder;

public class DonatosApplication extends Application {
    public C2461a f7442a;
    private final int f7443b = 21;

    public static DonatosApplication m7167a(Context context) {
        return (DonatosApplication) context.getApplicationContext();
    }

    static /* synthetic */ void m7168a(C3823b c3823b) {
        C2492a.f7657a = c3823b;
        c3823b.mo3130c();
        if (!c3823b.mo2382b().m11150a()) {
            C2495b.m7330a(new Throwable("Failure loading Google Tag Manager container."));
        }
    }

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (C2491a.f7655b.intValue() < 21) {
            C0028a.m47a((Context) this);
        }
    }

    public void onCreate() {
        Throwable e;
        C2465a m;
        C3885f a;
        super.onCreate();
        if (VERSION.SDK_INT >= 19 && VERSION.SDK_INT <= 23) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
            try {
                Field declaredField = InputMethodManager.class.getDeclaredField("mServedView");
                declaredField.setAccessible(true);
                Field declaredField2 = InputMethodManager.class.getDeclaredField("mServedView");
                declaredField2.setAccessible(true);
                Method declaredMethod = InputMethodManager.class.getDeclaredMethod("finishInputLocked", new Class[0]);
                declaredMethod.setAccessible(true);
                InputMethodManager.class.getDeclaredMethod("focusIn", new Class[]{View.class}).setAccessible(true);
                registerActivityLifecycleCallbacks(new C24971(inputMethodManager, declaredField2, declaredField, declaredMethod));
            } catch (NoSuchMethodException e2) {
                e = e2;
                Log.e("IMMLeaks", "Unexpected reflection exception", e);
                C2442a.m7166a();
                m = C2466e.m7268m();
                m.f7546a = (C2462b) C0006d.m6a(new C2462b(this));
                if (m.f7546a == null) {
                    if (m.f7547b == null) {
                        m.f7547b = new C2467a();
                    }
                    if (m.f7548c == null) {
                        m.f7548c = new C2489c();
                    }
                    if (m.f7549d == null) {
                        m.f7549d = new C2487a();
                    }
                    if (m.f7550e == null) {
                        m.f7550e = new C2459a();
                    }
                    this.f7442a = new C2466e(m);
                    C1139c.m4591a((Context) this, new C2022a());
                    CalligraphyConfig.initDefault(new Builder().setFontAttrId(R.attr.fontPath).build());
                    a = C3885f.m12816a((Context) this);
                    C3885f.m12818b();
                    a.m12819a("GTM-W2QS8FN").mo2348a(C2512b.m7369a(), TimeUnit.SECONDS);
                    LoganSquare.registerTypeConverter(Date.class, new C2504h());
                }
                throw new IllegalStateException(C2462b.class.getCanonicalName() + " must be set");
            } catch (NoSuchFieldException e3) {
                e = e3;
                Log.e("IMMLeaks", "Unexpected reflection exception", e);
                C2442a.m7166a();
                m = C2466e.m7268m();
                m.f7546a = (C2462b) C0006d.m6a(new C2462b(this));
                if (m.f7546a == null) {
                    throw new IllegalStateException(C2462b.class.getCanonicalName() + " must be set");
                }
                if (m.f7547b == null) {
                    m.f7547b = new C2467a();
                }
                if (m.f7548c == null) {
                    m.f7548c = new C2489c();
                }
                if (m.f7549d == null) {
                    m.f7549d = new C2487a();
                }
                if (m.f7550e == null) {
                    m.f7550e = new C2459a();
                }
                this.f7442a = new C2466e(m);
                C1139c.m4591a((Context) this, new C2022a());
                CalligraphyConfig.initDefault(new Builder().setFontAttrId(R.attr.fontPath).build());
                a = C3885f.m12816a((Context) this);
                C3885f.m12818b();
                a.m12819a("GTM-W2QS8FN").mo2348a(C2512b.m7369a(), TimeUnit.SECONDS);
                LoganSquare.registerTypeConverter(Date.class, new C2504h());
            }
        }
        C2442a.m7166a();
        m = C2466e.m7268m();
        m.f7546a = (C2462b) C0006d.m6a(new C2462b(this));
        if (m.f7546a == null) {
            throw new IllegalStateException(C2462b.class.getCanonicalName() + " must be set");
        }
        if (m.f7547b == null) {
            m.f7547b = new C2467a();
        }
        if (m.f7548c == null) {
            m.f7548c = new C2489c();
        }
        if (m.f7549d == null) {
            m.f7549d = new C2487a();
        }
        if (m.f7550e == null) {
            m.f7550e = new C2459a();
        }
        this.f7442a = new C2466e(m);
        C1139c.m4591a((Context) this, new C2022a());
        CalligraphyConfig.initDefault(new Builder().setFontAttrId(R.attr.fontPath).build());
        a = C3885f.m12816a((Context) this);
        C3885f.m12818b();
        a.m12819a("GTM-W2QS8FN").mo2348a(C2512b.m7369a(), TimeUnit.SECONDS);
        LoganSquare.registerTypeConverter(Date.class, new C2504h());
    }
}
