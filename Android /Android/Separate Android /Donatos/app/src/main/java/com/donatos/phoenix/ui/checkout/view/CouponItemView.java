package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.locations.Coupon;

public class CouponItemView extends FrameLayout {
    public TextView f8464a;
    public ImageButton f8465b;
    public Coupon f8466c;
    private RelativeLayout f8467d;

    public CouponItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_couponitem, this);
        this.f8467d = (RelativeLayout) inflate.findViewById(R.id.paymentitem_background);
        this.f8464a = (TextView) inflate.findViewById(R.id.couponitem_name);
        this.f8465b = (ImageButton) inflate.findViewById(R.id.couponitem_delete);
    }
}
