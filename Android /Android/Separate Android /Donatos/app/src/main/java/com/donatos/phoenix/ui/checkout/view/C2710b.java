package com.donatos.phoenix.ui.checkout.view;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

final /* synthetic */ class C2710b implements OnCheckedChangeListener {
    private final DriverTipView f8517a;

    private C2710b(DriverTipView driverTipView) {
        this.f8517a = driverTipView;
    }

    public static OnCheckedChangeListener m8099a(DriverTipView driverTipView) {
        return new C2710b(driverTipView);
    }

    public final void onCheckedChanged(RadioGroup radioGroup, int i) {
        DriverTipView.m8086a(this.f8517a);
    }
}
