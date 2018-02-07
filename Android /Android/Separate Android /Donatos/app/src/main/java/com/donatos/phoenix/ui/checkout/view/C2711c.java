package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnClickListener;

final /* synthetic */ class C2711c implements OnClickListener {
    private final DriverTipView f8518a;

    private C2711c(DriverTipView driverTipView) {
        this.f8518a = driverTipView;
    }

    public static OnClickListener m8100a(DriverTipView driverTipView) {
        return new C2711c(driverTipView);
    }

    public final void onClick(View view) {
        DriverTipView.m8090b(this.f8518a, view);
    }
}
