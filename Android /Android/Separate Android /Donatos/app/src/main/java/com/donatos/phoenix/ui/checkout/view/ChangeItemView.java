package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class ChangeItemView extends FrameLayout {
    public TextView f8448a;
    public TextView f8449b;
    public TextView f8450c;

    public ChangeItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_changeitem, this);
        this.f8448a = (TextView) inflate.findViewById(R.id.changeitem_title);
        this.f8449b = (TextView) inflate.findViewById(R.id.changeitem_selection);
        this.f8450c = (TextView) inflate.findViewById(R.id.changeitem_change);
    }
}
