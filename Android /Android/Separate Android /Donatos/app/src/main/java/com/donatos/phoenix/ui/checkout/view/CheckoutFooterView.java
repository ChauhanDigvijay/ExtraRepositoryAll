package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.ui.checkout.bi;
import com.donatos.phoenix.ui.common.DrawableAlignedButton;
import java.text.NumberFormat;

public class CheckoutFooterView extends FrameLayout {
    public DrawableAlignedButton f8451a;
    public NumberFormat f8452b = NumberFormat.getCurrencyInstance();
    public TextView f8453c;
    public TextView f8454d;
    public TextView f8455e;
    public TextView f8456f;
    public TextView f8457g;
    public TextView f8458h;
    private TextView f8459i;
    private LinearLayout f8460j;
    private LinearLayout f8461k;
    private LinearLayout f8462l;

    public CheckoutFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_checkoutfooter, this);
        this.f8451a = (DrawableAlignedButton) inflate.findViewById(R.id.checkoutfooter_placeorder_button);
        this.f8453c = (TextView) inflate.findViewById(R.id.checkoutfooter_subtotal);
        this.f8454d = (TextView) inflate.findViewById(R.id.checkoutfooter_discount);
        this.f8455e = (TextView) inflate.findViewById(R.id.checkoutfooter_deliverycharge);
        this.f8460j = (LinearLayout) inflate.findViewById(R.id.checkoutfooter_deliverycharge_row);
        this.f8456f = (TextView) inflate.findViewById(R.id.checkoutfooter_tip);
        this.f8459i = (TextView) inflate.findViewById(R.id.checkoutfooter_tip_label);
        this.f8461k = (LinearLayout) inflate.findViewById(R.id.checkoutfooter_tip_row);
        this.f8457g = (TextView) inflate.findViewById(R.id.checkoutfooter_tax);
        this.f8462l = (LinearLayout) inflate.findViewById(R.id.checkoutfooter_tax_row);
        this.f8458h = (TextView) inflate.findViewById(R.id.checkoutfooter_grandtotal);
    }

    public void setButtonEnabled(boolean z) {
        this.f8451a.setEnabled(z);
    }

    public void setButtonVisibility(boolean z) {
        this.f8451a.setVisibility(z ? 0 : 8);
    }

    public void setOrderType(bi biVar) {
        if (biVar == null || !biVar.equals(bi.DELIVERY)) {
            this.f8460j.setVisibility(8);
            this.f8459i.setText(getContext().getString(R.string.checkout.footer.tip));
            return;
        }
        this.f8460j.setVisibility(0);
        this.f8459i.setText(getContext().getString(R.string.checkout.footer.driver.tip));
    }

    public void setTipVisible(boolean z) {
        this.f8461k.setVisibility(z ? 0 : 8);
    }
}
