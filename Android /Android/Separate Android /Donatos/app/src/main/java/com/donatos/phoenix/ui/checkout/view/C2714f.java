package com.donatos.phoenix.ui.checkout.view;

import com.donatos.phoenix.p134b.p135a.C2493b;
import javax.p000a.C0000a;
import p001a.C0003a;

public final class C2714f implements C0003a<DriverTipView> {
    static final /* synthetic */ boolean f8521a = (!C2714f.class.desiredAssertionStatus());
    private final C0000a<C2493b> f8522b;

    private C2714f(C0000a<C2493b> c0000a) {
        if (f8521a || c0000a != null) {
            this.f8522b = c0000a;
            return;
        }
        throw new AssertionError();
    }

    public static C0003a<DriverTipView> m8102a(C0000a<C2493b> c0000a) {
        return new C2714f(c0000a);
    }

    public final /* synthetic */ void mo2a(Object obj) {
        DriverTipView driverTipView = (DriverTipView) obj;
        if (driverTipView == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        driverTipView.f8468a = (C2493b) this.f8522b.mo1a();
    }
}
