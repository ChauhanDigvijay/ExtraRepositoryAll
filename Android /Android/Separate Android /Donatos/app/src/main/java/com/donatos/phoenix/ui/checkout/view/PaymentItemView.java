package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.PaymentResponseContent;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.ui.checkout.p140b.C2643v;
import java.text.NumberFormat;

public class PaymentItemView extends FrameLayout {
    public RelativeLayout f8499a;
    public TextView f8500b;
    public TextView f8501c;
    public TextView f8502d;
    public ImageButton f8503e;
    public PaymentResponseContent f8504f;
    public NumberFormat f8505g = NumberFormat.getCurrencyInstance();

    public PaymentItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_paymentitem, this);
        this.f8499a = (RelativeLayout) inflate.findViewById(R.id.paymentitem_background);
        this.f8500b = (TextView) inflate.findViewById(R.id.paymentitem_name);
        this.f8501c = (TextView) inflate.findViewById(R.id.paymentitem_edit);
        this.f8502d = (TextView) inflate.findViewById(R.id.paymentitem_value);
        this.f8503e = (ImageButton) inflate.findViewById(R.id.paymentitem_delete);
    }

    static /* synthetic */ void m8096a(PaymentResponseContent paymentResponseContent, OnClickListener onClickListener, View view) {
        C2508l.m7347a().m7349a(new C2643v(paymentResponseContent));
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public PaymentResponseContent getItem() {
        return this.f8504f;
    }
}
