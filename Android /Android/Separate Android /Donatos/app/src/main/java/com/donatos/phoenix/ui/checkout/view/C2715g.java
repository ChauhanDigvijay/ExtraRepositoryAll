package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;

final /* synthetic */ class C2715g implements OnFocusChangeListener {
    private final LocationGuestView f8523a;

    private C2715g(LocationGuestView locationGuestView) {
        this.f8523a = locationGuestView;
    }

    public static OnFocusChangeListener m8104a(LocationGuestView locationGuestView) {
        return new C2715g(locationGuestView);
    }

    public final void onFocusChange(View view, boolean z) {
        LocationGuestView.m8092a(this.f8523a, z);
    }
}
