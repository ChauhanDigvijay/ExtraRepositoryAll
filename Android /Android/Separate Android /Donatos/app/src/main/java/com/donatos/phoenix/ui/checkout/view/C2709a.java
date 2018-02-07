package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnClickListener;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.ui.checkout.p140b.C2634m;

public final /* synthetic */ class C2709a implements OnClickListener {
    private final CouponItemView f8516a;

    private C2709a(CouponItemView couponItemView) {
        this.f8516a = couponItemView;
    }

    public static OnClickListener m8098a(CouponItemView couponItemView) {
        return new C2709a(couponItemView);
    }

    public final void onClick(View view) {
        C2508l.m7347a().m7349a(new C2634m(this.f8516a.f8466c));
    }
}
