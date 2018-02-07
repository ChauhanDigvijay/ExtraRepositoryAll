package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class AddItemView extends FrameLayout {
    public TextView f8446a;
    public FrameLayout f8447b;

    public AddItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_additem, this);
        this.f8446a = (TextView) inflate.findViewById(R.id.additem_title);
        this.f8447b = (FrameLayout) inflate.findViewById(R.id.additem_framelayout);
    }
}
