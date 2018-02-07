package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnClickListener;
import com.donatos.phoenix.network.common.PaymentResponseContent;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.ui.checkout.p140b.C2635n;

public final /* synthetic */ class C2719k implements OnClickListener {
    private final PaymentResponseContent f8528a;

    private C2719k(PaymentResponseContent paymentResponseContent) {
        this.f8528a = paymentResponseContent;
    }

    public static OnClickListener m8106a(PaymentResponseContent paymentResponseContent) {
        return new C2719k(paymentResponseContent);
    }

    public final void onClick(View view) {
        C2508l.m7347a().m7349a(new C2635n(this.f8528a));
    }
}
