package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class CheckoutHeaderView extends LinearLayout {
    private TextView f8463a = ((TextView) inflate(getContext(), R.layout.view_model_checkoutheader, this).findViewById(R.id.checkoutheader_textview));

    public CheckoutHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(1);
    }

    public void setPaddingBottom(Integer num) {
        if (num != null) {
            this.f8463a.setPadding(this.f8463a.getPaddingLeft(), this.f8463a.getPaddingTop(), this.f8463a.getPaddingRight(), num.intValue());
        }
    }

    public void setTitle(SpannableString spannableString) {
        this.f8463a.setText(spannableString);
    }
}
