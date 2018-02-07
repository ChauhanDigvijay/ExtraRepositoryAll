package com.donatos.phoenix.ui.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class HeaderView extends LinearLayout {
    public TextView f8619a;
    public TextView f8620b;

    public HeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //setOrientation(0);
        View inflate = inflate(getContext(), R.layout.view_model_header, this);
        this.f8619a = (TextView) inflate.findViewById(R.id.header_textview);
        this.f8620b = (TextView) inflate.findViewById(R.id.header_descriptionview);
    }
}
