package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnClickListener;

final /* synthetic */ class C2712d implements OnClickListener {
    private final DriverTipView f8519a;

    private C2712d(DriverTipView driverTipView) {
        this.f8519a = driverTipView;
    }

    public static OnClickListener m8101a(DriverTipView driverTipView) {
        return new C2712d(driverTipView);
    }

    public final void onClick(View view) {
        DriverTipView.m8087a(this.f8519a, view);
    }
}
