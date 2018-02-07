package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.Address;

public class SelectAddressItemView extends FrameLayout {
    private TextView f8506a = ((TextView) inflate(getContext(), R.layout.view_model_selectaddress, this).findViewById(R.id.selectaddress_address));
    private Address f8507b;

    public SelectAddressItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Address getAddress() {
        return this.f8507b;
    }

    public void setData(Address address) {
        this.f8507b = address;
        if (VERSION.SDK_INT >= 24) {
            this.f8506a.setText(Html.fromHtml(address.getFormattedAddress(), 0), BufferType.SPANNABLE);
        } else {
            this.f8506a.setText(Html.fromHtml(address.getFormattedAddress()), BufferType.SPANNABLE);
        }
    }
}
