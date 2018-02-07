package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class EstimatedDeliveryTimeView extends FrameLayout {
    private TextView f8483a = ((TextView) inflate(getContext(), R.layout.view_model_estimateddeliverytime, this).findViewById(R.id.estimateddeliverytime_label));

    public EstimatedDeliveryTimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setData(String str) {
        this.f8483a.setText(str);
    }
}
