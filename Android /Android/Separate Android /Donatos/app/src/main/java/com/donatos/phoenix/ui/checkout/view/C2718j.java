package com.donatos.phoenix.ui.checkout.view;

import android.view.View;
import android.view.View.OnClickListener;
import com.donatos.phoenix.network.common.PaymentResponseContent;

public final /* synthetic */ class C2718j implements OnClickListener {
    private final PaymentResponseContent f8526a;
    private final OnClickListener f8527b;

    private C2718j(PaymentResponseContent paymentResponseContent, OnClickListener onClickListener) {
        this.f8526a = paymentResponseContent;
        this.f8527b = onClickListener;
    }

    public static OnClickListener m8105a(PaymentResponseContent paymentResponseContent, OnClickListener onClickListener) {
        return new C2718j(paymentResponseContent, onClickListener);
    }

    public final void onClick(View view) {
        PaymentItemView.m8096a(this.f8526a, this.f8527b, view);
    }
}
