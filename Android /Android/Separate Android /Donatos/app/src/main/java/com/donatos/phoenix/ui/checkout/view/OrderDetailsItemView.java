package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.CartItem;

public class OrderDetailsItemView extends FrameLayout {
    public ImageView f8489a;
    public TextView f8490b;
    public TextView f8491c;
    public TextView f8492d;
    public TextView f8493e;
    public TextView f8494f;
    public TextView f8495g;
    public CartItem f8496h;
    public LinearLayout f8497i;
    public TextView f8498j;

    public OrderDetailsItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_orderdetails, this);
        this.f8489a = (ImageView) inflate.findViewById(R.id.orderdetails_imageview);
        this.f8490b = (TextView) inflate.findViewById(R.id.orderdetails_title);
        this.f8491c = (TextView) inflate.findViewById(R.id.orderdetails_subtitle);
        this.f8492d = (TextView) inflate.findViewById(R.id.orderdetails_price);
        this.f8493e = (TextView) inflate.findViewById(R.id.orderdetails_quantity);
        this.f8494f = (TextView) inflate.findViewById(R.id.orderdetails_edit);
        this.f8495g = (TextView) inflate.findViewById(R.id.orderdetails_delete);
        this.f8497i = (LinearLayout) inflate.findViewById(R.id.orderdetails_actionrow);
        this.f8498j = (TextView) inflate.findViewById(R.id.orderdetails_customized);
    }

    public Integer getCartId() {
        return this.f8496h.getId();
    }
}
